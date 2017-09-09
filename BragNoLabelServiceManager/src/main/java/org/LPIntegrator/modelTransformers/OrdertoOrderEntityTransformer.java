package org.LPIntegrator.modelTransformers;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

import org.LPIntegrator.hibernate.OrderEntity;
import org.LPIntegrator.hibernate.OrderLineItemEntity;
import org.LPIntegrator.hibernate.ShippingAddressEntity;
import org.LPIntegrator.models.ShopifyOrder;

import com.google.common.collect.Lists;

public class OrdertoOrderEntityTransformer implements Function<ShopifyOrder, OrderEntity> {

	public OrderEntity apply(ShopifyOrder o) {
		OrderEntity oe = new OrderEntity();
		if(o.getFinancialStatus()!=null)
		oe.setFinancialStatus(o.getFinancialStatus().toString());
		if(o.getFullFillMentStatus() != null)
		oe.setFulfillmentStatus(o.getFullFillMentStatus().toString());
		oe.setOrderid(Long.valueOf(o.getId()));
		oe.setOrderStatus(o.getFinancialStatus().toString());
		oe.setTotalPrice(o.getTotalPrice());
		oe.setCreatedAt(o.getCreatedAt());
		oe.setLastUpdatedAt(o.getUpdatedAt());
		ShippingAddressEntity shippingAddressEntity = Optional.of(o.getShippingAddress()).map(new AddressToAddressEntity(oe.getOrderid())).get();
		shippingAddressEntity.setOrderEntity(oe);
		oe.setAddressEntity(shippingAddressEntity);
		ArrayList<OrderLineItemEntity> orderLineItemList = Lists.newArrayList();
		o.getOrderLineItems().stream().map(new ShopifyLineItemToOrderLineItemEntityTransformers()).forEach(orderLineItemList::add);
		for(OrderLineItemEntity ole : orderLineItemList){
			ole.setOrderEntity(oe);
		}
		oe.setOrderLineItems(orderLineItemList);
		return oe;
	}

}
