package org.DelhiveryClient.models;

import org.ShopifyInegration.models.ShopifyOrder;

import lombok.Data;

@Data
public class CreateOrderResponse extends LPClientRequest{
	
	private ShopifyOrder shopifyOrder;
	
}
