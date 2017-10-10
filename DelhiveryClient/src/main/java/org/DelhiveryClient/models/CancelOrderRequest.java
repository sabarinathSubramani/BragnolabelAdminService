package org.DelhiveryClient.models;

import lombok.Data;

@Data
public class CancelOrderRequest extends LPClientRequest{

	String subOrderId;
	String orderId;
}
