package org.LPIntegrator.modelTransformers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.LPIntegrator.models.Address;
import org.LPIntegrator.models.FinancialStatus;
import org.LPIntegrator.models.FullFillMentStatus;
import org.LPIntegrator.models.ShopifyOrder;
import org.LPIntegrator.models.Tax;
import org.LPIntegrator.models.User;
import org.LPIntegrator.utils.LPIntegratorUtils;

import com.shopify.api.models.Customer;
import com.shopify.api.models.Order;
import com.shopify.api.models.ShopifyAddress;
import com.shopify.api.models.TaxLines;

public class OrderToShopifyOrderTransformer implements Function<Order, ShopifyOrder>{

	public ShopifyOrder apply(Order o) {
		ShopifyOrder shopifyOrder = new ShopifyOrder();
		shopifyOrder.setId(String.valueOf(o.getId()));
		shopifyOrder.setCreatedAt(LPIntegratorUtils.getShopifyOrderDateTime(o.getCreated_at()));
		shopifyOrder.setBillingAddress(billingAddressToAddress(o.getBilling_address()));
		shopifyOrder.setCustomer(customerToUser(o.getCustomer()));
		if(o.getFinancial_status()!=null)
			shopifyOrder.setFinancialStatus(FinancialStatus.valueOf(o.getFinancial_status()));
		if(o.getFulfillment_status()!=null)
			shopifyOrder.setFullFillMentStatus(FullFillMentStatus.valueOf(o.getFulfillment_status()));
		Address shippingAddressToAddress = shippingAddressToAddress(o.getShipping_address());
		shippingAddressToAddress.setEmail(o.getCustomer().getEmail());
		shopifyOrder.setShippingAddress(shippingAddressToAddress);
		shopifyOrder.setTotalPrice(Double.valueOf(o.getTotal_price()));
		List<Tax> tax = new ArrayList<Tax>();
		for(TaxLines taxline : o.getTax_lines())
			tax.add(taxLinesToTax(taxline));
		shopifyOrder.setTotalTax(tax);
		shopifyOrder.setTotalWeight(Double.valueOf(o.getTotal_weight()));
		shopifyOrder.setUpdatedAt(LPIntegratorUtils.getShopifyOrderDateTime(o.getUpdated_at()));
		shopifyOrder.setOrderLineItems(o.getLine_items().stream().map(new LineItemToShopifyOrderLineItemTransformer()).collect(Collectors.toCollection(ArrayList::new)));
		return shopifyOrder;
	}

	private Address billingAddressToAddress(ShopifyAddress bAddress){

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
		Tax tax = new Tax();
		tax.setRate(taxLine.getRate());
		tax.setTaxType(taxLine.getTitle());
		tax.setValue(Double.valueOf(taxLine.getPrice()));
		return tax;
	}
}
