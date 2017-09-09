package org.shopifyApis.client;

import javax.ws.rs.client.Client;

import org.shopifyApis.client.internals.ShopifyOrdersClientImpl;

public class ShopifyApiClientFactory {

	public static ShopifyOrdersClient create(Client client){
		return new ShopifyOrdersClientImpl(client);
	}
}
