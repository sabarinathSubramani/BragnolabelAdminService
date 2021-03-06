package org.ShopifyInegration.models;

import java.util.List;

import lombok.Data;

@Data
public class ShopifyOrderLineItem {

	private long orderLineItem;
	private String productTitle;
	private String variantId;
	private int quantity;
	private double unitPrice;
	private double weight;
	private List<Tax> tax;
	private String sku;
}
