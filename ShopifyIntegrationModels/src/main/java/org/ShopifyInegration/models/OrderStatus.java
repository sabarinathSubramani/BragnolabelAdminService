package org.ShopifyInegration.models;

public enum OrderStatus {

	open, closed, cancelled;
	
	public static OrderStatus toOrderStatus(String status){
		switch(status){
		case "paid":
			return OrderStatus.open;
		case "fulfilled":
			return OrderStatus.closed;
		case "partially_refunded":
			return OrderStatus.closed;
		case "refunded":
			return OrderStatus.closed;
		case "cancelled":
			return OrderStatus.cancelled;
		default: 
			return OrderStatus.open;
		}
	}
}
