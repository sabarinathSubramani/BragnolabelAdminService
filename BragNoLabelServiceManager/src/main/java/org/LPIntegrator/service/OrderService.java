package org.LPIntegrator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
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
import org.ShopifyInegration.models.ShopifyOrder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.shopifyApis.client.ShopifyOrdersClient;
import org.shopifyApis.models.ShopifyApiException;
import org.shopifyApis.models.ShopifyOrdersQuery;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	public String createOrderinWH(String orderId, int clientId){ 
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
		CreateOrderRequest createOrderRequest = new CreateOrderRequest();
		createOrderRequest.setClient(client);
		createOrderRequest.setShopifyOrder(shopifyOrder);
		LP findWareHousePartner = LPFinderService.findWareHousePartner(shopifyOrder, clientId);
		LPClient lpClient = LPClientFactory.getClient(findWareHousePartner, jerseyClient);
		CreateOrderResponse response = lpClient.pushOrdersToWareHouse(createOrderRequest);
		if(response.getResponse().getStatusInfo().getFamily().equals(Family.SUCCESSFUL)){
			orderEntityDAO.updatePushedToWareHouse(Long.valueOf(orderId));
			logger.info("order successfully created in Warehouse");
		}
		
		return response.getResponse().getEntity().toString();

	}

	public List<String> updateShipmentStatus(OrderStatusUpdateRequest request){
		Orderlines[] orderlines = request.getOrderlines();
		Map<Long, String> trackingNumberMap = Maps.newHashMap();
		try{
			for(Orderlines orderline: orderlines){
				trackingNumberMap.put(Long.valueOf(orderline.getOrder_line_id()), orderline.getWaybill_number());
			}
			orderLineItemsEntityDAO.updateTrackingNumber(trackingNumberMap);
		}catch(Exception e){
			logger.error("unable to update tracking number in db", e);
		}

		Map<String, FulfillmentRequest> populateFulfillmentRequest = populateFulfillmentRequest(orderlines);
		List<String> response = new ArrayList<>(); 
		for(Entry<String, FulfillmentRequest> entrySet : populateFulfillmentRequest.entrySet()){
			try{
				Response updateFulfillment = shopifyOrdersClient.updateFulfillment( entrySet.getKey(), entrySet.getValue());
				response.add(updateFulfillment.getEntity().toString());
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

	private Map<String, FulfillmentRequest> populateFulfillmentRequest(Orderlines[] orderlines){

		Map<String, FulfillmentRequest> fulfillmentRequestMap = new HashMap<>();

		for(Orderlines ol : orderlines){

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

		return fulfillmentRequestMap;
	}

	public Map<String, String> getOrderId(List<String> orderlineItemIds){
		return new HashMap<>();

	}



}
