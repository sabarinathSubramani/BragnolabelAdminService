package org.LPIntegrator.modelTransformers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.LPIntegrator.models.ShopifyOrderLineItem;
import org.LPIntegrator.models.Tax;

import com.shopify.api.models.LineItems;
import com.shopify.api.models.TaxLines;


public class LineItemToShopifyOrderLineItemTransformer implements Function<LineItems, ShopifyOrderLineItem> {

	public ShopifyOrderLineItem apply(LineItems t) {
		ShopifyOrderLineItem olt = new ShopifyOrderLineItem();
		olt.setProductTitle(t.getTitle());
		olt.setOrderLineItem(t.getId());
		olt.setPrice(Double.valueOf(t.getPrice()));
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
		Tax tax = new Tax();
		tax.setRate(taxLine.getRate());
		tax.setTaxType(taxLine.getTitle());
		tax.setValue(Double.valueOf(taxLine.getPrice()));
		return tax;
	}

}
