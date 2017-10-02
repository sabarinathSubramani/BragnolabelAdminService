package org.LPIntegrator.service.models;

import lombok.Data;

@Data
public class Orderlines
{
    private String waybill_number;

    private String order_line_id;

    private String status;

    private Invoice invoice;

    private String payment_mode;

    private String fulfillment_center;

    private String courier;

    private String shipment_id;

    private String order_id;

    private Products[] products;

    private String client_store;
    
}