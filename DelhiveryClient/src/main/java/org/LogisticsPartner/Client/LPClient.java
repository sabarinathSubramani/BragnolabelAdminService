package org.LogisticsPartner.Client;

import org.DelhiveryClient.models.CancelOrderRequest;
import org.DelhiveryClient.models.CreateOrderRequest;
import org.DelhiveryClient.models.CreateOrderResponse;

public interface LPClient {

	public CreateOrderResponse pushOrdersToWareHouse(CreateOrderRequest createOrderRequest);
	
	public String cancelWareHouseOrder(CancelOrderRequest cancelOrderRequest);
}

