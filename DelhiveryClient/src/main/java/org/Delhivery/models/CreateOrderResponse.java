package org.Delhivery.models;

import javax.ws.rs.core.Response;

import org.LogisticsPartner.LPClientRequest;
import org.ShopifyInegration.models.ShopifyOrder;

import lombok.Data;

@Data
public class CreateOrderResponse extends LPClientRequest{
	
	private ShopifyOrder shopifyOrder;
	private Response response;
	
}
