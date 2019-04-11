package org.LogisticsPartner;

import java.util.List;

import org.ShopifyInegration.models.ShopifyOrder;

import lombok.Data;

@Data
public class CreatePackageRequest extends LPClientRequest{
	
	private List<ShopifyOrder> shopifyOrders;
	private LP lp;


}
