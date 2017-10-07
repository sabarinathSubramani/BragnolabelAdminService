package org.ShopifyInegration.models;

import java.util.List;

import org.joda.time.DateTime;

import lombok.Data;

@Data
public class ShopifyOrder {

	private String id;
	private FinancialStatus financialStatus;
	private FullFillMentStatus fullFillMentStatus;
	private DateTime createdAt;
	private DateTime updatedAt;
	private double totalPrice;
	private double totalWeight;
	private List<Tax> totalTax;
	private Address billingAddress;
	private Address shippingAddress;
	private User Customer;
	private List<ShopifyOrderLineItem> orderLineItems;
	private String clientId;
	private boolean testOrder;
	private double shippingFees;
	private double discount;
	private boolean pushedToWareHouse;
	private String shippingTax;
	
}
