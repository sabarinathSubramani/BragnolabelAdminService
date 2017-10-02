package org.DelhiveryClient.Transformers;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.DelhiveryClient.models.Consignee;
import org.DelhiveryClient.models.CreateOrderRequest;
import org.DelhiveryClient.models.DelhiveryOrder;
import org.DelhiveryClient.models.InvoiceDetails;
import org.DelhiveryClient.models.ProductDetails;
import org.DelhiveryClient.models.ShipmentDetails;
import org.DelhiveryClient.models.SubOrders;
import org.ShopifyInegration.models.ShopifyOrder;
import org.ShopifyInegration.models.ShopifyOrderLineItem;
import org.ShopifyInegration.models.Tax;
import org.ShopifyInegration.models.Tax.TaxType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ShopifyOrderTransformer {

	public static Function<ShopifyOrder, DelhiveryOrder> toDelhiveryOrder(){
		return so -> {
			DelhiveryOrder o = new DelhiveryOrder();
			Consignee c = new Consignee();
			c.setAddress1(so.getShippingAddress().getAddressLine1());
			c.setAddress2(so.getShippingAddress().getAddressLine2());
			c.setCity(so.getShippingAddress().getCity());
			c.setCountry(so.getShippingAddress().getCountry());
			c.setEmail(so.getShippingAddress().getEmail());
			c.setName(so.getShippingAddress().getFullName());
			c.setPhone1(so.getShippingAddress().getPhone());
			c.setPincode(String.valueOf(so.getShippingAddress().getZip()));
			c.setState(so.getShippingAddress().getState());
			o.setConsignee(c);
			SubOrders[] subOrders = new SubOrders[so.getOrderLineItems().size()];
			for(int i =0; i < so.getOrderLineItems().size(); i++){
				ShopifyOrderLineItem sol = so.getOrderLineItems().get(i);
				SubOrders s = new SubOrders();
				s.setSubOrderNumber(String.valueOf(sol.getOrderLineItem()));
				s.setDispatchAfterDate(dateToString(so.getCreatedAt()));
				s.setExpectedShipDate(dateToString(so.getCreatedAt()));
				s.setFulfillmentCenter("FCDEL1");
				InvoiceDetails invoiceDetails = new InvoiceDetails();
				double totalTax = 0;
				for(Tax tax : sol.getTax()){
					switch(tax.getTaxType()){
					case CGST:{
						invoiceDetails.setCgstAmount(String.valueOf(tax.getValue()));
						invoiceDetails.setCgstPercentage(String.valueOf(tax.getRate()));
						totalTax= totalTax+tax.getValue();
						break;
					}
					case SGST:{

						invoiceDetails.setSgstAmount(String.valueOf(tax.getValue()));
						invoiceDetails.setSgstPercentage(String.valueOf(tax.getRate()));
						totalTax= totalTax+tax.getValue();
						break;
					}
					case IGST:{

						invoiceDetails.setIgstAmount(String.valueOf(tax.getValue()));
						invoiceDetails.setIgstPercentage(String.valueOf(tax.getRate()));
						totalTax= totalTax+tax.getValue();
						break;
					}
					}
				}
				invoiceDetails.setNetAmount(String.valueOf(totalTax));
				invoiceDetails.setGrossValue(String.valueOf(sol.getPrice()));
				invoiceDetails.setInvoiceDate(dateToString(so.getCreatedAt()));
				invoiceDetails.setTotalPrice(String.valueOf(sol.getPrice()));
				invoiceDetails.setTotalTaxes(String.valueOf(totalTax));
				s.setInvoiceDetails(invoiceDetails);

				ProductDetails productDetails = new ProductDetails();
				// set hsn code
				// productDetails.setHsnCode(hsnCode);
				productDetails.setDescription(sol.getProductTitle());
				productDetails.setName(sol.getProductTitle());
				productDetails.setNumber(sol.getSku());
				productDetails.setSku(sol.getSku());
				productDetails.setQuantity(String.valueOf(sol.getQuantity()));
				s.setProductDetails(productDetails);

				ShipmentDetails shipmentDetails = new ShipmentDetails();
				shipmentDetails.setCourier("Delhivery");
				shipmentDetails.setPaymentMode("PREPAID");
				shipmentDetails.setShipmentNumber("1");
				shipmentDetails.setShippingLevel("");
				shipmentDetails.setSortingCode("");
				s.setShipmentDetails(shipmentDetails);

				s.setGstin("TestGSTN");
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
		formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:sssZ");
	}

	public static DateTime toDateTime(String date){
		return formatter.parseDateTime(date);
	}

	public static String dateToString(DateTime date){
		return formatter.print(date);

	}

}
