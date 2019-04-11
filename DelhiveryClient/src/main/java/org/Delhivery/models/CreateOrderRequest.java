package org.Delhivery.models;

import org.LogisticsPartner.LPClientRequest;
import org.ShopifyInegration.models.ShopifyOrder;

import lombok.Data;

@Data
public class CreateOrderRequest extends LPClientRequest{
	
	private ShopifyOrder shopifyOrder;
}
