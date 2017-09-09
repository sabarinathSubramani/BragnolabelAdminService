package com.shopify.api.models;

import lombok.Data;

@Data
public class TaxLines
{
    private String title;

    private double rate;

    private String price;

    @Override
    public String toString()
    {
        return "ClassPojo [title = "+title+", rate = "+rate+", price = "+price+"]";
    }
}