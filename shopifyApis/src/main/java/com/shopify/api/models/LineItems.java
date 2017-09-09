package com.shopify.api.models;

import lombok.Data;

@Data
public class LineItems{
    private String variant_title;

    private String product_id;

    private String taxable;

    private String total_discount;

    private String vendor;

    private String variant_id;

    private String fulfillable_quantity;

    private String[] properties;

    private String gift_card;

    private TaxLines[] tax_lines;

    private String sku;

    private String fulfillment_service;

    private String product_exists;

    private long id;

    private String variant_inventory_management;

    private String title;

    private String price;

    private String requires_shipping;

    private String name;

    private double grams;

    private int quantity;

    private String fulfillment_status;
    
    private Location origin_location;
    
    private Location destination_location;

    @Override
    public String toString()
    {
        return "ClassPojo [variant_title = "+variant_title+", product_id = "+product_id+", taxable = "+taxable+", total_discount = "+total_discount+", vendor = "+vendor+", variant_id = "+variant_id+", fulfillable_quantity = "+fulfillable_quantity+", properties = "+properties+", gift_card = "+gift_card+", tax_lines = "+tax_lines+", sku = "+sku+", fulfillment_service = "+fulfillment_service+", product_exists = "+product_exists+", id = "+id+", variant_inventory_management = "+variant_inventory_management+", title = "+title+", price = "+price+", requires_shipping = "+requires_shipping+", name = "+name+", grams = "+grams+", quantity = "+quantity+", fulfillment_status = "+fulfillment_status+"]";
    }
}