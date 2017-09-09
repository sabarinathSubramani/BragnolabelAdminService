package com.shopify.api.models;

import lombok.Data;

@Data
public class ShippingLine
{
    private String id;

    private String phone;

    private String title;

    private String price;

    private String delivery_category;

    private String source;

    private String code;

    private String[] tax_lines;

    private String carrier_identifier;

    private String requested_fulfillment_service_id;


    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", phone = "+phone+", title = "+title+", price = "+price+", delivery_category = "+delivery_category+", source = "+source+", code = "+code+", tax_lines = "+tax_lines+", carrier_identifier = "+carrier_identifier+", requested_fulfillment_service_id = "+requested_fulfillment_service_id+"]";
    }
}