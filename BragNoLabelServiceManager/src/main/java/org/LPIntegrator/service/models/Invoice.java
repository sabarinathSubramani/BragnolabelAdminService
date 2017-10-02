package org.LPIntegrator.service.models;

import lombok.Data;

@Data
public class Invoice
{
    private String total_cst;

    private String vat_percentage;

    private String cst_percentage;

    private String cod_amount;

    private String total_taxes;

    private String total_vat;

    private String mrp;

    private String invoice_number;

    private String invoice_date;

    private String tax_percentage;

    private String total_price;

    private String shipping_price;
}