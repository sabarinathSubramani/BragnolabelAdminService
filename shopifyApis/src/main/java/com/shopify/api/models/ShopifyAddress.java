package com.shopify.api.models;

import lombok.Data;

@Data
public class ShopifyAddress
{
    private int zip;

    private String phone;

    private String province_code;

    private String address1;

    private String address2;

    private String country_code;

    private String city;

    private String country;

    private String first_name;

    private String name;

    private String company;

    private String province;

    private String last_name;

    private String longitude;

    private String latitude;


    @Override
    public String toString()
    {
        return "ClassPojo [zip = "+zip+", phone = "+phone+", province_code = "+province_code+", address1 = "+address1+", address2 = "+address2+", country_code = "+country_code+", city = "+city+", country = "+country+", first_name = "+first_name+", name = "+name+", company = "+company+", province = "+province+", last_name = "+last_name+", longitude = "+longitude+", latitude = "+latitude+"]";
    }
}