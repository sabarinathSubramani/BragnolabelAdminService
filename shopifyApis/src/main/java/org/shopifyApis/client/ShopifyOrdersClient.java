package org.shopifyApis.client;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.Response;

import org.shopifyApis.models.ShopifyApiException;
import org.shopifyApis.models.ShopifyOrdersQuery;

import com.shopify.api.models.FulfillmentRequest;
import com.shopify.api.models.Order;

public interface ShopifyOrdersClient {
	
	public Optional<List<Order>> getOrders(Optional<ShopifyOrdersQuery> shopifyOrdersQuery) throws ShopifyApiException;

	public Response updateFulfillment(String orderId, FulfillmentRequest fulfillmentRequest);
	
	/*public ShopifyResponse getOrder(Optional<String> orderId);
	
	public ShopifyResponse updateOrder(Optional<ShopifyRequest> request);*/

}
