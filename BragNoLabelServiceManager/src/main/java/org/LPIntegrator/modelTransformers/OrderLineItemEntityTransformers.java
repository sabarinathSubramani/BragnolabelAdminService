package org.LPIntegrator.modelTransformers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.LPIntegrator.hibernate.OrderLineItemEntity;
import org.ShopifyInegration.models.ShopifyOrderLineItem;
import org.ShopifyInegration.models.Tax;
import org.ShopifyInegration.models.Tax.TaxType;
import org.apache.commons.lang3.StringUtils;

public class OrderLineItemEntityTransformers  {


	public static  Function<ShopifyOrderLineItem, OrderLineItemEntity> toOrderLineItemEntity(){
		return t -> {
			OrderLineItemEntity ole = new OrderLineItemEntity();
			ole.setOrderItemId(t.getOrderLineItem());
			ole.setProductTitle(t.getProductTitle());
			ole.setQuantity(t.getQuantity());
			ole.setSku(t.getSku());
			for(Tax tax  : t.getTax()){
				
				switch(tax.getTaxType()){
				case CGST:{
					ole.setCgstTaxRate(tax.getRate());
					ole.setCgstTaxValue(tax.getValue());
					break;
				}
				case SGST:{

					ole.setSgstTaxRate(tax.getRate());
					ole.setSgstTaxValue(tax.getValue());
					break;
				}
				case IGST:{

					ole.setIgstTaxRate(tax.getRate());
					ole.setIgstTaxValue(tax.getValue());
					break;
				}
				}

			}
			ole.setUnitPrice(t.getUnitPrice());
			ole.setWeight(t.getWeight());
			return ole;
		};
	}

	public static  Function<OrderLineItemEntity, ShopifyOrderLineItem> toShopifyOrderLineItem(){

		return oel -> {

			ShopifyOrderLineItem sol = new ShopifyOrderLineItem();
			sol.setOrderLineItem(oel.getOrderItemId());
			sol.setUnitPrice(oel.getUnitPrice());
			sol.setProductTitle(oel.getProductTitle());
			sol.setQuantity(oel.getQuantity());
			sol.setSku(oel.getSku());
			sol.setWeight(oel.getWeight());
			List<Tax> taxList = new ArrayList<Tax>();
			if(oel.getCgstTaxValue()>0){
				Tax tax = new Tax(TaxType.CGST,oel.getCgstTaxValue() , oel.getCgstTaxRate());
				taxList.add(tax);
			}
			if(oel.getIgstTaxValue()>0){
				Tax tax = new Tax(TaxType.IGST,oel.getIgstTaxValue() , oel.getIgstTaxRate());
				taxList.add(tax);
			}
			if(oel.getSgstTaxValue()>0){
				Tax tax = new Tax(TaxType.SGST,oel.getSgstTaxValue() , oel.getSgstTaxRate());
				taxList.add(tax);
			}
			sol.setTax(taxList);
			return sol;
		};
	}

}
