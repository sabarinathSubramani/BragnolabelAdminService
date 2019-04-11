package org.Delhivery.models;

import org.LogisticsPartner.LPClientRequest;

import lombok.Data;

@Data
public class CancelOrderRequest extends LPClientRequest{

	String subOrderId;
	String orderId;
}
