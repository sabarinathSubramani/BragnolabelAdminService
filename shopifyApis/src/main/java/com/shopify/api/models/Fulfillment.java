package com.shopify.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Fulfillment
{
    private String id;

    private LineItems[] line_items;

    private String tracking_url;

    private String updated_at;

    private String[] tracking_urls;

    private String status;

    private String service;

    private String tracking_number;

    private String created_at;

    private Reciept receipt;

    private String order_id;

    private String[] tracking_numbers;

    private String tracking_company;

    private String shipment_status;
    
    private boolean notify_customer;

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", line_items = "+line_items+", tracking_url = "+tracking_url+", updated_at = "+updated_at+", tracking_urls = "+tracking_urls+", status = "+status+", service = "+service+", tracking_number = "+tracking_number+", created_at = "+created_at+", receipt = "+receipt+", order_id = "+order_id+", tracking_numbers = "+tracking_numbers+", tracking_company = "+tracking_company+", shipment_status = "+shipment_status+"]";
    }
}