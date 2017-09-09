package org.LPIntegrator.modelTransformers;

import java.util.function.Function;

import org.LPIntegrator.hibernate.OrderLineItemEntity;
import org.LPIntegrator.models.ShopifyOrderLineItem;
import org.LPIntegrator.models.Tax;

public class ShopifyLineItemToOrderLineItemEntityTransformers implements Function<ShopifyOrderLineItem, OrderLineItemEntity> {
	
	@Override
	public OrderLineItemEntity apply(ShopifyOrderLineItem t) {
		OrderLineItemEntity ole = new OrderLineItemEntity();
		ole.setOrderItemId(t.getOrderLineItem());
		ole.setProductTitle(t.getProductTitle());
		ole.setQuantity(t.getQuantity());
		ole.setSku(t.getSku());
		if(t.getTax()!= null && t.getTax().size()>0){
			Tax tax = t.getTax().get(0);
			ole.setTaxRate(tax.getRate());
			ole.setTaxType(tax.getTaxType());
		}
		ole.setTotalPrice(t.getPrice());
		ole.setWeight(t.getWeight());
		return ole;
	}

}
