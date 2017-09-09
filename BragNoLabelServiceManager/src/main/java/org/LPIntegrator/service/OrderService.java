package org.LPIntegrator.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.LPIntegrator.hibernate.OrderEntity;
import org.LPIntegrator.hibernate.OrderLineItemEntity;
import org.LPIntegrator.hibernate.ShippingAddressEntity;
import org.LPIntegrator.hibernate.daos.OrderEntityDAO;
import org.LPIntegrator.hibernate.daos.OrderLineItemsEntityDAO;
import org.LPIntegrator.modelTransformers.AddressToAddressEntity;
import org.LPIntegrator.modelTransformers.OrderToShopifyOrderTransformer;
import org.LPIntegrator.modelTransformers.OrdertoOrderEntityTransformer;
import org.LPIntegrator.modelTransformers.ShopifyLineItemToOrderLineItemEntityTransformers;
import org.LPIntegrator.models.ShopifyOrder;
import org.shopifyApis.client.ShopifyOrdersClient;
import org.shopifyApis.models.ShopifyApiException;
import org.shopifyApis.models.ShopifyOrdersQuery;

import com.google.common.collect.Lists;
import com.shopify.api.models.Order;

public class OrderService {

	private OrderLineItemsEntityDAO orderLineItemsEntityDAO;
	private OrderEntityDAO orderEntityDAO;
	private ShopifyOrdersClient shopifyOrdersClient;
	public OrderService(OrderEntityDAO orderEntityDAO ,OrderLineItemsEntityDAO orderLineItemsEntityDAO, ShopifyOrdersClient shopifyOrdersClient){
		this.orderLineItemsEntityDAO=orderLineItemsEntityDAO;
		this.shopifyOrdersClient=shopifyOrdersClient;
		this.orderEntityDAO = orderEntityDAO;
	}

	public List<ShopifyOrder> getOrders(Optional<ShopifyOrdersQuery> shopifyOrdersQuery ){
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
			ShopifyOrdersList.stream().map(new OrdertoOrderEntityTransformer()).forEach(orderEntityList::add);
			orderEntityDAO.insertOrderDetails(orderEntityList);
		}
		return ShopifyOrdersList;
	}

	public void saveOrder(Optional<ShopifyOrder> shopifyOrderOptional){

		OrderEntity oe = shopifyOrderOptional.map(new OrdertoOrderEntityTransformer()).get();
		orderEntityDAO.insertOrderDetails(Lists.newArrayList(oe));
	}
	
	public void getOrder(String orderId){
		Optional<OrderEntity> findOrderByOrderId = orderEntityDAO.findOrderByOrderId(Long.valueOf(orderId));

	}

}
