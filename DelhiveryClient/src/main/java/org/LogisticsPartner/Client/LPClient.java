package org.LogisticsPartner.Client;

import org.DelhiveryClient.models.CreateOrderRequest;

public interface LPClient {

	public String pushOrdersToWareHouse(CreateOrderRequest createOrderRequest);
}
