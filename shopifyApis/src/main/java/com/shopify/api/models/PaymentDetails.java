package com.shopify.api.models;

import lombok.Data;

@Data
public class PaymentDetails
{
    private String credit_card_company;

    private String avs_result_code;

    private String credit_card_number;

    private String credit_card_bin;

    private String cvv_result_code;

    @Override
    public String toString()
    {
        return "ClassPojo [credit_card_company = "+credit_card_company+", avs_result_code = "+avs_result_code+", credit_card_number = "+credit_card_number+", credit_card_bin = "+credit_card_bin+", cvv_result_code = "+cvv_result_code+"]";
    }
}