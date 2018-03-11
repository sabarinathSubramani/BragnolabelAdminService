package org.LPIntegrator.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;

import org.DelhiveryClient.models.CancelOrderRequest;
import org.DelhiveryClient.models.CreateOrderRequest;
import org.DelhiveryClient.models.CreateOrderResponse;
import org.LPIntegrator.hibernate.OrderEntity;
import org.LPIntegrator.hibernate.daos.OrderEntityDAO;
import org.LPIntegrator.hibernate.daos.OrderLineItemsEntityDAO;
import org.LPIntegrator.modelTransformers.OrderEntityTransformer;
import org.LPIntegrator.modelTransformers.OrderToShopifyOrderTransformer;
import org.LPIntegrator.service.cache.CacheEnum;
import org.LPIntegrator.service.models.OrderStatusUpdateRequest;
import org.LPIntegrator.service.models.Orderlines;
import org.LPIntegrator.utils.cache.CacheRegistry;
import org.LogisticsPartner.LP;
import org.LogisticsPartner.Client.LPClient;
import org.LogisticsPartner.Client.LPClientFactory;
import org.ShopifyInegration.models.Client;
import org.ShopifyInegration.models.FullFillMentStatus;
import org.ShopifyInegration.models.ShopifyOrder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.shopifyApis.client.ShopifyOrdersClient;
import org.shopifyApis.models.ShopifyApiException;
import org.shopifyApis.models.ShopifyOrdersQuery;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.shopify.api.models.Fulfillment;
import com.shopify.api.models.FulfillmentRequest;
import com.shopify.api.models.LineItems;
import com.shopify.api.models.Order;

public class OrderService {

	static Logger logger = Logger.getLogger(OrderService.class);

	/*	@Inject
	private OrderLineItemsEntityDAO orderLineItemsEntityDAO;*/
	@Inject
	javax.ws.rs.client.Client jerseyClient;

	@Inject
	private OrderEntityDAO orderEntityDAO;

	@Inject
	private ShopifyOrdersClient shopifyOrdersClient;

	@Inject 
	private OrderLineItemsEntityDAO orderLineItemsEntityDAO;


	public List<ShopifyOrder> getOrders(Optional<ShopifyOrdersQuery> shopifyOrdersQuery, int clientId ){
		Optional<List<Order>> orders = null;
		if(shopifyOrdersQuery.isPresent())
			try {
				orders = shopifyOrdersClient.getOrders(shopifyOrdersQuery);
			} catch (ShopifyApiException e) {
				e.printStackTrace();
			}
		List<ShopifyOrder> ShopifyOrdersList = new ArrayList<ShopifyOrder>();
		if(orders.isPresent() && orders.get().size()>0){
			ShopifyOrdersList = new ArrayList<ShopifyOrder>();
			orders.get().stream().map(new OrderToShopifyOrderTransformer()).forEach(ShopifyOrdersList::add);
		}
		return ShopifyOrdersList;
	}

	public List<ShopifyOrder> pullOrders(Optional<ShopifyOrdersQuery> shopifyOrdersQuery, int clientId ){

		List<ShopifyOrder> ShopifyOrdersList = getOrders(shopifyOrdersQuery, clientId);
		if(ShopifyOrdersList!= null && ShopifyOrdersList.size()>0){
			List<OrderEntity> orderEntityList = new ArrayList<OrderEntity>();
			Stream<OrderEntity> map = ShopifyOrdersList.stream().map(OrderEntityTransformer.toOrderEntity());
			map.forEach((oe) -> {
				oe.setClientId(clientId);
				orderEntityList.add(oe);
			});
			orderEntityDAO.insertOrderDetails(orderEntityList);
		}
		return ShopifyOrdersList;
	}

	public void saveOrder(Optional<ShopifyOrder> shopifyOrderOptional){

		OrderEntity oe = shopifyOrderOptional.map(OrderEntityTransformer.toOrderEntity()).get();
		orderEntityDAO.insertOrderDetails(Lists.newArrayList(oe));
	}

	public Optional<OrderEntity> getOrder(String orderId){
		return orderEntityDAO.findOrderByOrderId(Long.valueOf(orderId));
	}

	public ShopifyOrder getShopifyOrder(String orderId){
		OrderEntity orderEntity = getOrder(orderId).orElseThrow(  () -> { return new WebApplicationException(404);});
		return OrderEntityTransformer.toShopifyOrder().apply(orderEntity);
	}

	public Response createOrderinWH(String orderId, int clientId){ 
		ShopifyOrder shopifyOrder = getShopifyOrder(orderId);

		if(shopifyOrder.isTestOrder() || shopifyOrder.isPushedToWareHouse() || shopifyOrder.getOrderStatus().equals("cancelled") || shopifyOrder.getOrderStatus().equals("closed") ){
			throw new WebApplicationException("Order not eligible for warehouse order creation, either test order or already pushed to warehouse", 401);
		}
		LoadingCache<Integer, Client> asLoadingCache = CacheRegistry.getAsLoadingCache(CacheEnum.ClientCache);
		Client client = null;
		try {
			client = asLoadingCache.get(clientId);
		} catch (ExecutionException e) {
			throw new WebApplicationException("Client not found", 401);
		}
		return createOrderinWH(shopifyOrder, client);

	}

	private Response createOrderinWH(ShopifyOrder shopifyOrder, Client client){
		try{
			logger.info("pushing order "+shopifyOrder.getId()+" to warehouse");
			CreateOrderRequest createOrderRequest = new CreateOrderRequest();
			createOrderRequest.setClient(client);
			createOrderRequest.setShopifyOrder(shopifyOrder);
			LP findWareHousePartner = LPFinderService.findWareHousePartner(shopifyOrder, client.getClientId());
			LPClient lpClient = LPClientFactory.getClient(findWareHousePartner, jerseyClient);
			CreateOrderResponse response = lpClient.pushOrdersToWareHouse(createOrderRequest);
			if(response.getResponse().getStatusInfo().getFamily().equals(Family.SUCCESSFUL)){
				orderEntityDAO.updatePushedToWareHouse(Long.valueOf(shopifyOrder.getId()));
				logger.info("order successfully created in Warehouse");
			}
			return response.getResponse();
		}catch(Exception e){
			return Response.serverError().entity("error while pushing order to warehouse for orderid - "+shopifyOrder.getId()).build();
		}
	}

	public List<String> updateShipmentStatus(OrderStatusUpdateRequest request){
		Orderlines[] orderlines = request.getOrderlines();
		Map<Long, String> trackingNumberMap = Maps.newHashMap();

		try{
			for(Orderlines orderline: orderlines){
				if(orderline.getStatus().equals("PAK") && orderline.getWaybill_number()!=null)
					trackingNumberMap.put(Long.valueOf(orderline.getOrder_line_id()), orderline.getWaybill_number());
			}
			orderLineItemsEntityDAO.orderEntity(trackingNumberMap.keySet()).forEach(t -> {if(t.getTrackingNumber()!=null)trackingNumberMap.remove(t.getOrderItemId());});
			orderLineItemsEntityDAO.updateTrackingNumber(trackingNumberMap);
		}catch(Exception e){
			logger.error("unable to update tracking number in db", e);
		}

		if(trackingNumberMap.size()==0){
			logger.error("no order line items eligible for fulfillment update");
			throw new WebApplicationException(Response.status(Status.NOT_ACCEPTABLE).entity("no order line items eligible for fulfillment update").build());
		}

		Map<String, FulfillmentRequest> populateFulfillmentRequest = populateFulfillmentRequest(orderlines, trackingNumberMap.keySet());
		List<String> response = new ArrayList<>(); 
		for(Entry<String, FulfillmentRequest> entrySet : populateFulfillmentRequest.entrySet()){
			try{
				String updateFulfillment = shopifyOrdersClient.updateFulfillment( entrySet.getKey(), entrySet.getValue());
				response.add(updateFulfillment);
			}catch(Exception e){
				response.add(e.getMessage());
				throw e;
			}

		}
		return response;
	}

	public Map<String, String> cancelWareHouseOrder(int clientId, Order order){


		if(order ==null || order.getLine_items()==null || order.getLine_items().size()==0)
			throw new WebApplicationException("not a valid order", 400);

		LoadingCache<Integer, Client> asLoadingCache = CacheRegistry.getAsLoadingCache(CacheEnum.ClientCache);
		Client client = null;
		try {
			client = asLoadingCache.get(clientId);
		} catch (ExecutionException e) {
			throw new WebApplicationException("Client not found", 401);
		}
		LP findWareHousePartner = LPFinderService.findWareHousePartner(null, client.getClientId());
		LPClient lpClient = LPClientFactory.getClient(findWareHousePartner, jerseyClient);
		Map<String, String> responseMap = new HashMap<String, String>();
		for(LineItems lt : order.getLine_items()){
			CancelOrderRequest coRequest = new CancelOrderRequest();
			coRequest.setClient(client);
			coRequest.setOrderId(order.getId());
			coRequest.setSubOrderId(String.valueOf(lt.getId()));
			try{
				lpClient.cancelWareHouseOrder(coRequest);
				responseMap.put(String.valueOf(lt.getId()), lpClient.cancelWareHouseOrder(coRequest) );
			}catch(Exception e){
				//responseMap.put(String.valueOf(lt.getId()), "cancel order api failed with reason - "+e.getMessage() );
				throw e;
			}
			orderEntityDAO.updateOrderStatus(Long.valueOf(order.getId()), "cancelled");
		}
		return responseMap;
	}



	/**
	 * @param orderlines - from delhivery notification payload 
	 * @param orderLineItems - list of order line items eligible for fulfillment update(ie. order lines that are not already fulfilled)
	 * @return
	 */
	private Map<String, FulfillmentRequest> populateFulfillmentRequest(Orderlines[] orderlines, Collection<Long> orderLineItems){

		Map<String, FulfillmentRequest> fulfillmentRequestMap = new HashMap<>();

		if(orderLineItems!=null && orderLineItems.size()>0){
			for(Orderlines ol : orderlines){
				if(ol.getWaybill_number()!=null && orderLineItems.contains(Long.valueOf(ol.getOrder_line_id()))){
					if(fulfillmentRequestMap.get(ol.getOrder_id())== null){
						FulfillmentRequest fulfillmentRequest = new FulfillmentRequest();
						fulfillmentRequest.getFulfillment().setTracking_company("Delhivery");
						fulfillmentRequest.getFulfillment().setNotify_customer(true);
						fulfillmentRequestMap.put(ol.getOrder_id(),  fulfillmentRequest);
					}
					FulfillmentRequest fulfillmentRequest = fulfillmentRequestMap.get(ol.getOrder_id());
					Fulfillment fulfulfillment = fulfillmentRequest.getFulfillment();
					LineItems lineItem = new LineItems();
					lineItem.setId(Long.valueOf(ol.getOrder_line_id()));
					lineItem.setQuantity(Integer.valueOf(ol.getProducts()[0].getProd_qty()));
					if(fulfulfillment.getLine_items()== null)
						fulfulfillment.setLine_items(ArrayUtils.toArray(lineItem));
					else
						fulfulfillment.setLine_items(ArrayUtils.add(fulfillmentRequest.getFulfillment().getLine_items(), lineItem));

					if(fulfulfillment.getTracking_number()==null ){
						if(fulfulfillment.getTracking_numbers()!= null)
							fulfulfillment.setTracking_numbers(ArrayUtils.add(fulfulfillment.getTracking_numbers(), ol.getWaybill_number()));
						else
							fulfulfillment.setTracking_number(ol.getWaybill_number());
					}else{
						if(!fulfulfillment.getTracking_number().equals(ol.getWaybill_number())){
							fulfulfillment.setTracking_numbers(ArrayUtils.toArray(fulfulfillment.getTracking_number(), ol.getWaybill_number()));
							fulfulfillment.setTracking_number(null);
						}
					}

				}
			}
		}

		return fulfillmentRequestMap;
	}


	public void PushEligibleOrdersToWarehouse(int clientId){

		List<String> orderIds = Lists.newArrayList();
		List<String> pushedOrders = Lists.newArrayList();
		List<String> failedOrders = Lists.newArrayList();
		List<ShopifyOrder> shopifyOrders = Lists.newArrayList();

		try {

			logger.info("Querying database to get eligible orders to push to warehouse");
			List<OrderEntity> ordersForWareHouse = orderEntityDAO.getOrdersForWareHouse();

			// partioning the list into smaller ones of size 10
			List<List<OrderEntity>> partitionedList = Lists.partition(ordersForWareHouse, 10);

			for(List<OrderEntity> toBePushed : partitionedList){
				
				if(!toBePushed.isEmpty()) {
					toBePushed.parallelStream().map(t -> String.valueOf(t.getOrderid())).forEach(orderIds::add);
				}
				try{
					DateTime dt = DateTime.now().minusMinutes(15);
					ShopifyOrdersQuery squery = new ShopifyOrdersQuery();
					squery.setIds(String.join(",", orderIds));
					squery.setStatus(ShopifyOrdersQuery.Status.open);
					squery.setCreated_at_max(dt);
					shopifyOrders = getOrders(Optional.of(squery), clientId);
				}catch(Exception e){
					logger.error("error while trying to get order from shopify. skipping the batch", e);
				}
				LoadingCache<Integer, Client> asLoadingCache = CacheRegistry.getAsLoadingCache(CacheEnum.ClientCache);
				final Client client = asLoadingCache.get(clientId);

				logger.info("call shopify orders api to validate order's current status");
				shopifyOrders.stream().forEach(order -> {
					if(order.getFullFillMentStatus() != null && ! order.getFullFillMentStatus().equals(FullFillMentStatus.fulfilled)){
						Response response = createOrderinWH(order, client);
						if(Response.Status.Family.familyOf(response.getStatus()).equals(Response.Status.Family.SUCCESSFUL)){
							response.close();
							pushedOrders.add(order.getId());
						}						
						else{
							failedOrders.add(order.getId());
						}
					}else{
						logger.info("order is already fulfilled - "+order.getId());
					}

				});
				orderIds.clear();
				shopifyOrders.clear();
			}

		} catch (ExecutionException e) {
			throw new WebApplicationException("Client not found", 401);
		}

		logger.info("Orders that are pushed successfully - "+pushedOrders.toString());
		logger.info("orders failed while pushing - "+failedOrders.toString());
	}

}
