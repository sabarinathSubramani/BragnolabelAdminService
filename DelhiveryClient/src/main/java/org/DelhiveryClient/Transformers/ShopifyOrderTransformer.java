package org.DelhiveryClient.Transformers;

import java.util.function.Function;
import javax.ws.rs.WebApplicationException;

import org.DelhiveryClient.models.Consignee;
import org.DelhiveryClient.models.DelhiveryOrder;
import org.DelhiveryClient.models.InvoiceDetails;
import org.DelhiveryClient.models.ProductDetails;
import org.DelhiveryClient.models.ShipmentDetails;
import org.DelhiveryClient.models.SubOrders;
import org.ShopifyInegration.models.Client;
import org.ShopifyInegration.models.OrderType;
import org.ShopifyInegration.models.ShopifyOrder;
import org.ShopifyInegration.models.ShopifyOrderLineItem;
import org.ShopifyInegration.models.Tax;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class ShopifyOrderTransformer {


	public static Function<ShopifyOrder, DelhiveryOrder> toDelhiveryOrder(Client client){
		
		String gstIN;
		String fulfillmentCentre;
		try{
			  fulfillmentCentre = client.getCredentials().get("delhivery").get("fulfillmentCenter");
			  gstIN= client.getCredentials().get("clientData").get("gstIN");
		}catch(Exception e){
			throw new WebApplicationException("gstIN not found in client data - "+client.getCredentials().toString(), 400);
		}
		return so -> {
			DelhiveryOrder o = new DelhiveryOrder();
			Consignee c = new Consignee();
			c.setAddress1(so.getShippingAddress().getAddressLine1());
			c.setAddress2(so.getShippingAddress().getAddressLine2());
			c.setCity(so.getShippingAddress().getCity());
			c.setCountry(so.getShippingAddress().getCountry());
			c.setEmail(so.getShippingAddress().getEmail());
			c.setName(so.getShippingAddress().getFullName());
			String deleteWhitespace = StringUtils.deleteWhitespace(so.getShippingAddress().getPhone());
			if(deleteWhitespace!=null){
				c.setPhone1(StringUtils.substring(deleteWhitespace, deleteWhitespace.length()-10,deleteWhitespace.length()));
			}
			c.setPincode(String.valueOf(so.getShippingAddress().getZip()));
			c.setState(so.getShippingAddress().getState());
			o.setConsignee(c);
			int noOfOrders = so.getOrderLineItems().size();
			SubOrders[] subOrders = new SubOrders[noOfOrders];

			for(int i =0; i < so.getOrderLineItems().size(); i++){
				ShopifyOrderLineItem sol = so.getOrderLineItems().get(i);
				SubOrders s = new SubOrders();
				s.setSubOrderNumber(String.valueOf(sol.getOrderLineItem()));
				//s.setDispatchAfterDate(dateToString(so.getCreatedAt()));
				//s.setExpectedShipDate(dateToString(so.getCreatedAt()));
				s.setFulfillmentCenter(fulfillmentCentre);
				InvoiceDetails invoiceDetails = new InvoiceDetails();
				double totalTax = 0;
				for(Tax tax : sol.getTax()){
					switch(tax.getTaxType()){
					case CGST:{
						invoiceDetails.setCgstAmount(tax.getValue());
						invoiceDetails.setCgstPercentage(tax.getRate()*100);
						totalTax= totalTax+tax.getValue();
						break;
					}
					case SGST:{

						invoiceDetails.setSgstAmount(tax.getValue());
						invoiceDetails.setSgstPercentage(tax.getRate()*100);
						totalTax= totalTax+tax.getValue();
						break;
					}
					case IGST:{

						invoiceDetails.setIgstAmount(tax.getValue());
						invoiceDetails.setIgstPercentage(tax.getRate()*100);
						totalTax= totalTax+tax.getValue();
						break;
					}
					}
				}

				invoiceDetails.setGrossValue(sol.getUnitPrice()*sol.getQuantity()-totalTax);
				invoiceDetails.setMrp(sol.getUnitPrice());
				invoiceDetails.setDiscount(Math.round(so.getDiscount()/noOfOrders));
				invoiceDetails.setNetAmount(sol.getUnitPrice()*sol.getQuantity()-invoiceDetails.getDiscount());
				invoiceDetails.setInvoiceDate(dateToString(so.getCreatedAt()));
				invoiceDetails.setTotalPrice(invoiceDetails.getNetAmount());
				invoiceDetails.setTotalTaxes(totalTax);
				invoiceDetails.setShippingPrice(so.getShippingFees());
				invoiceDetails.setCodAmount(invoiceDetails.getNetAmount());
				s.setInvoiceDetails(invoiceDetails);
				

				ProductDetails productDetails = new ProductDetails();
				// set hsn code
				//	productDetails.setHsnCode(hsnCode);
				productDetails.setDescription(sol.getProductTitle());
				productDetails.setName(sol.getProductTitle());
				productDetails.setNumber(sol.getSku());
				productDetails.setSku(sol.getSku());
				productDetails.setQuantity(sol.getQuantity());
				s.setProductDetails(productDetails);

				ShipmentDetails shipmentDetails = new ShipmentDetails();
				shipmentDetails.setCourier("Delhivery");
				if(so.getOrderType().equals(OrderType.COD))
					shipmentDetails.setPaymentMode("COD");
				else{
					shipmentDetails.setPaymentMode("PREPAID");
				}
				shipmentDetails.setShipmentNumber("");
				shipmentDetails.setShippingLevel("");
				shipmentDetails.setSortingCode("");
				s.setShipmentDetails(shipmentDetails);
				s.setGstin(gstIN);
				subOrders[i]=s;
			}

			o.setOrderDate(dateToString(so.getCreatedAt()));
			o.setSubOrders(subOrders);
			o.setOrderNumber(so.getId());
			return o;
		};
	}


	private static DateTimeFormatter formatter = null;
	static{
		formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	}

	public static DateTime toDateTime(String date){
		return formatter.parseDateTime(date);
	}

	public static String dateToString(DateTime date){
		return formatter.print(date);

	}

}
