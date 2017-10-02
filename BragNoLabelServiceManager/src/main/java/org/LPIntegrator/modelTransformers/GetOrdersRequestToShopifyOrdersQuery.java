package org.LPIntegrator.modelTransformers;

import java.util.function.Function;

import org.LPIntegrator.service.models.GetOrdersRequest;
import org.LPIntegrator.utils.LPIntegratorUtils;
import org.shopifyApis.models.ShopifyOrdersQuery;
import org.shopifyApis.models.ShopifyOrdersQuery.FinancialStatus;
import org.shopifyApis.models.ShopifyOrdersQuery.FullFillMentStatus;

public class GetOrdersRequestToShopifyOrdersQuery implements Function<GetOrdersRequest, ShopifyOrdersQuery> {

	@Override
	public ShopifyOrdersQuery apply(GetOrdersRequest t) {
		ShopifyOrdersQuery query = new ShopifyOrdersQuery();
		if(t.getOrderIds()!=null)
			query.setIds(t.getOrderIds());
		if(t.getCreatedAtMax()!= null)
			query.setCreated_at_max(LPIntegratorUtils.getShopifyOrderAsString(t.getCreatedAtMax()));
		if(t.getCreatedAtMin()!= null)
			query.setCreated_at_min(LPIntegratorUtils.getShopifyOrderAsString(t.getCreatedAtMin()));
		if(t.getFinancialStatus()!= null)
			query.setFinancial_status(FinancialStatus.valueOf(t.getFinancialStatus()));
		if(t.getFulfilmentStatus()!= null)
			query.setFulfillment_status(FullFillMentStatus.valueOf(t.getFulfilmentStatus()));
		return query;
	}

	
}
