package org.LPIntegrator.modelTransformers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.LPIntegrator.hibernate.OrderEntity;
import org.LPIntegrator.hibernate.OrderLineItemEntity;
import org.LPIntegrator.hibernate.ShippingAddressEntity;
import org.ShopifyInegration.models.ShopifyOrder;
import org.ShopifyInegration.models.ShopifyOrderLineItem;
import org.apache.commons.lang3.BooleanUtils;

import com.google.common.collect.Lists;

public class OrderEntityTransformer {

	public static Function<ShopifyOrder, OrderEntity> toOrderEntity(){
		return o -> {
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
			oe.setTestOrder(o.isTestOrder()?1:0);
			oe.setShippingFees(o.getShippingFees());
			oe.setDiscount(o.getDiscount());
			ShippingAddressEntity shippingAddressEntity = Optional.of(o.getShippingAddress()).map(new AddressEntityTransformer(oe.getOrderid()).toAddressEntity()).get();
			shippingAddressEntity.setOrderEntity(oe);
			oe.setAddressEntity(shippingAddressEntity);
			ArrayList<OrderLineItemEntity> orderLineItemList = Lists.newArrayList();
			o.getOrderLineItems().stream().map(OrderLineItemEntityTransformers.toOrderLineItemEntity()).forEach(orderLineItemList::add);
			for(OrderLineItemEntity ole : orderLineItemList){
				ole.setOrderEntity(oe);
			}
			oe.setOrderLineItems(orderLineItemList);
			oe.setPushedToWareHouse(o.isPushedToWareHouse()?1:0);
			return oe;
		};
	}
	
	public static Function<OrderEntity, ShopifyOrder> toShopifyOrder(){
		return oe -> {
			ShopifyOrder so = new ShopifyOrder();
			so.setCreatedAt(oe.getCreatedAt());
			so.setShippingAddress(Optional.of(oe.getAddressEntity()).map(new AddressEntityTransformer(0).toAddress()).get());
			List<ShopifyOrderLineItem> list = new ArrayList<ShopifyOrderLineItem>();
			oe.getOrderLineItems().stream().map(OrderLineItemEntityTransformers.toShopifyOrderLineItem()).forEach(list::add);
			so.setOrderLineItems(list);
			so.setId(String.valueOf(oe.getOrderid()));
			so.setTotalPrice(oe.getTotalPrice());
			so.setTestOrder(BooleanUtils.toBooleanObject(oe.getTestOrder()));
			so.setShippingFees(oe.getShippingFees());
			so.setDiscount(oe.getDiscount());
			so.setPushedToWareHouse(BooleanUtils.toBooleanObject(oe.getPushedToWareHouse()));
			return so;
		};
	}
	
	

}
