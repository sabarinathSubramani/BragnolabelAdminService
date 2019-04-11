package org.LogisticsPartner.Client;

import org.Delhivery.models.CancelOrderRequest;
import org.Delhivery.models.CreateOrderRequest;
import org.Delhivery.models.CreateOrderResponse;

public interface LPWareHouseClient extends LPClient{

	public CreateOrderResponse pushOrdersToWareHouse(CreateOrderRequest createOrderRequest);
	
	public String cancelWareHouseOrder(CancelOrderRequest cancelOrderRequest);
}

