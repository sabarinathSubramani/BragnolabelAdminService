package com.shopify.api.models;

import lombok.Data;

@Data
public class Location
{
    private String id;

    private String zip;

    private String province_code;

    private String name;

    private String address1;

    private String address2;

    private String country_code;

    private String city;

  
    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", zip = "+zip+", province_code = "+province_code+", name = "+name+", address1 = "+address1+", address2 = "+address2+", country_code = "+country_code+", city = "+city+"]";
    }
}
