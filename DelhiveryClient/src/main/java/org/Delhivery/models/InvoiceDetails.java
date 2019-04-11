package org.Delhivery.models;

import lombok.Data;

@Data
public class InvoiceDetails{
	
    private double vatPercentage;

    private double totalCst;

    private double netAmount;

    private double totalTaxes;

    private double mrp;

    private String invoiceDate;

    private double unitPrice;

    private double igstPercentage;

    private double grossValue;

    private String invoiceLink;

    private double cgstAmount;

    private double shippingPrice;

    private double codAmount;

    private double sgstPercentage;

    private double advancePayment;

    private String imei;

    private double cgstPercentage;

    private double sgstAmount;

    private String invoiceNumber;

    private double totalPrice;

    private double discount;

    private String referenceNumber;

    private double cstPercentage;

    private double unitTaxes;

    private double totalVat;

    private double igstAmount;

    private double roundOff;

    private double taxPercentage;
    
}