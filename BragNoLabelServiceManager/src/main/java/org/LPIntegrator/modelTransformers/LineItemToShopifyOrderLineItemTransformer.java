package org.LPIntegrator.modelTransformers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.ShopifyInegration.models.ShopifyOrderLineItem;
import org.ShopifyInegration.models.Tax;
import org.ShopifyInegration.models.Tax.TaxType;

import com.shopify.api.models.LineItems;
import com.shopify.api.models.TaxLines;


public class LineItemToShopifyOrderLineItemTransformer implements Function<LineItems, ShopifyOrderLineItem> {

	public ShopifyOrderLineItem apply(LineItems t) {
		ShopifyOrderLineItem olt = new ShopifyOrderLineItem();
		olt.setProductTitle(t.getTitle());
		olt.setOrderLineItem(t.getId());
		olt.setUnitPrice(Double.valueOf(t.getPrice()));
		olt.setQuantity(t.getQuantity());
		olt.setSku(t.getSku());
		List<Tax> tax = new ArrayList<Tax>();
		for(TaxLines taxline : t.getTax_lines())
			tax.add(taxLinesToTax(taxline));
		olt.setTax(tax);
		olt.setVariantId(t.getVariant_id());
		olt.setWeight(t.getGrams());
		return olt;
	}

	public Tax taxLinesToTax(TaxLines taxLine){
		return new Tax(TaxType.valueOf(taxLine.getTitle()),Double.valueOf(taxLine.getPrice()), taxLine.getRate() );
	}

}
