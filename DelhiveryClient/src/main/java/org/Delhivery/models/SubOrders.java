package org.Delhivery.models;

import lombok.Data;

@Data
public class SubOrders{
	
    private ProductDetails productDetails;

    private InvoiceDetails invoiceDetails;

    private String expectedShipDate;

    private String dispatchAfterDate;

    private String gstin;

    private String fulfillmentCenter;

    private String access_key;

    private ShipmentDetails shipmentDetails;

    private String subOrderNumber;
    
    @Override
    public String toString()
    {
        return "product_details = "+productDetails+", invoice_details = "+invoiceDetails+", expected_ship_date = "+expectedShipDate+", dispatch_after_date = "+dispatchAfterDate+", gstin = "+gstin+", fulfillment_center = "+fulfillmentCenter+", access_key = "+access_key+", shipment_details = "+shipmentDetails+", sub_order_number = "+subOrderNumber;
    }
    
}