package org.DelhiveryClient.models;

import lombok.Data;

@Data
public class ShipmentDetails{
	
    private String waybillNumber;

    private String courier;

    private String paymentMode;

    private String shippingLevel;

    private String shipmentNumber;

    private String sortingCode;

    private String packingSlipLink;
    
}
