package com.shopify.api.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@JsonSnakeCase
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingLine
{
    private String id;

    private String phone;

    private String title;

    private String price;

    private String delivery_category;

    private String source;

    private String code;

    private TaxLines[] tax_lines;

    private String carrier_identifier;

    private String requested_fulfillment_service_id;
    
   // private String discounted_price;


    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", phone = "+phone+", title = "+title+", price = "+price+", delivery_category = "+delivery_category+", source = "+source+", code = "+code+", tax_lines = "+tax_lines+", carrier_identifier = "+carrier_identifier+", requested_fulfillment_service_id = "+requested_fulfillment_service_id+"]";
    }
}