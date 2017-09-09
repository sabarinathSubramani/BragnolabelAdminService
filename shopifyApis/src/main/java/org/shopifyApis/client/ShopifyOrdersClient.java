package org.shopifyApis.client;

import java.util.List;
import java.util.Optional;

import org.shopifyApis.models.ShopifyApiException;
import org.shopifyApis.models.ShopifyOrdersQuery;
import org.shopifyApis.models.ShopifyRequest;
import org.shopifyApis.models.ShopifyResponse;

import com.shopify.api.models.Order;

public interface ShopifyOrdersClient {
	
	public Optional<List<Order>> getOrders(Optional<ShopifyOrdersQuery> shopifyOrdersQuery) throws ShopifyApiException;
	
	/*public ShopifyResponse getOrder(Optional<String> orderId);
	
	public ShopifyResponse updateOrder(Optional<ShopifyRequest> request);*/

}
