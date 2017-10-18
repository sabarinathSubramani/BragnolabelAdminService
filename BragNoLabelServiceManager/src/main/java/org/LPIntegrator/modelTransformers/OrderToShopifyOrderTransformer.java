package org.LPIntegrator.modelTransformers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.LPIntegrator.utils.LPIntegratorUtils;
import org.ShopifyInegration.models.Address;
import org.ShopifyInegration.models.FinancialStatus;
import org.ShopifyInegration.models.FullFillMentStatus;
import org.ShopifyInegration.models.OrderStatus;
import org.ShopifyInegration.models.ShopifyOrder;
import org.ShopifyInegration.models.Tax;
import org.ShopifyInegration.models.Tax.TaxType;
import org.ShopifyInegration.models.User;
import org.apache.commons.lang3.BooleanUtils;

import com.shopify.api.models.Customer;
import com.shopify.api.models.Order;
import com.shopify.api.models.ShippingLine;
import com.shopify.api.models.ShopifyAddress;
import com.shopify.api.models.TaxLines;

public class OrderToShopifyOrderTransformer implements Function<Order, ShopifyOrder>{

	public ShopifyOrder apply(Order o) {
		ShopifyOrder shopifyOrder = new ShopifyOrder();
		shopifyOrder.setId(String.valueOf(o.getId()));
		shopifyOrder.setCreatedAt(LPIntegratorUtils.getShopifyOrderDateTime(o.getCreated_at()));
		shopifyOrder.setBillingAddress(billingAddressToAddress(o.getBilling_address()));
		shopifyOrder.setCustomer(customerToUser(o.getCustomer()));
		if(o.getFinancial_status()!=null){
			shopifyOrder.setFinancialStatus(FinancialStatus.valueOf(o.getFinancial_status()));
			shopifyOrder.setOrderStatus(OrderStatus.toOrderStatus(shopifyOrder.getFinancialStatus().toString()));
		}
		if(o.getFulfillment_status()!=null)
			shopifyOrder.setFullFillMentStatus(FullFillMentStatus.valueOf(o.getFulfillment_status()));
		Address shippingAddressToAddress = shippingAddressToAddress(o.getShipping_address());
		if(o.getCustomer()!= null)
			shippingAddressToAddress.setEmail(o.getCustomer().getEmail());
		shopifyOrder.setShippingAddress(shippingAddressToAddress);
		shopifyOrder.setTotalPrice(Double.valueOf(o.getTotal_price()));
		List<Tax> tax = new ArrayList<Tax>();
		for(TaxLines taxline : o.getTax_lines())
			tax.add(taxLinesToTax(taxline));
		shopifyOrder.setDiscount(Double.valueOf(o.getTotal_discounts()!=null?o.getTotal_discounts():"0"));
		ShippingLine[] shipping_lines = o.getShipping_lines();
		double shippingPrice = 0;
		if(shipping_lines!= null){
			for(ShippingLine shippingLine : shipping_lines){
				shippingPrice = shippingPrice+Double.valueOf(shippingLine.getPrice());
				for(TaxLines taxline  : shippingLine.getTax_lines()){
					shippingPrice=shippingPrice+Double.valueOf(taxline.getPrice());
				}
			}
			shopifyOrder.setShippingFees(shippingPrice);
		}
		shopifyOrder.setTotalTax(tax);
		shopifyOrder.setTotalWeight(Double.valueOf(o.getTotal_weight()));
		shopifyOrder.setUpdatedAt(LPIntegratorUtils.getShopifyOrderDateTime(o.getUpdated_at()));
		shopifyOrder.setOrderLineItems(o.getLine_items().stream().map(new LineItemToShopifyOrderLineItemTransformer()).collect(Collectors.toCollection(ArrayList::new)));
		shopifyOrder.setTestOrder(BooleanUtils.toBoolean(o.getTest()));
		return shopifyOrder;
	}

	private Address billingAddressToAddress(ShopifyAddress bAddress){
		if(bAddress== null)
			return null;
		Address address = new Address();
		address.setAddressLine1(bAddress.getAddress1());
		address.setAddressLine2(bAddress.getAddress2());
		address.setCity(bAddress.getCity());
		address.setCountry(bAddress.getCountry());
		address.setFirstName(bAddress.getFirst_name());
		address.setLastName(bAddress.getLast_name());
		address.setState(bAddress.getProvince());
		address.setZip(address.getZip());
		return address;
	}


	private Address shippingAddressToAddress(ShopifyAddress shippingAddress){
		if(shippingAddress== null)
			return null;
		Address address = new Address();
		address.setAddressLine1(shippingAddress.getAddress1());
		address.setAddressLine2(shippingAddress.getAddress2());
		address.setCity(shippingAddress.getCity());
		address.setCountry(shippingAddress.getCountry());
		address.setFirstName(shippingAddress.getFirst_name());
		address.setLastName(shippingAddress.getLast_name());
		address.setState(shippingAddress.getProvince());
		address.setZip(shippingAddress.getZip());
		address.setPhone(shippingAddress.getPhone());
		return address;
	}

	private User customerToUser(Customer customer){
		return null;
	}

	public Tax taxLinesToTax(TaxLines taxLine){
		if(taxLine!= null)
			return new Tax(TaxType.valueOf(taxLine.getTitle()),Double.valueOf(taxLine.getPrice()), taxLine.getRate());
		return null;
	}
}
