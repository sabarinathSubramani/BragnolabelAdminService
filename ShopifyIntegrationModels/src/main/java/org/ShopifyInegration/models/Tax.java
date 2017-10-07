package org.ShopifyInegration.models;

import lombok.Data;

@Data
public class Tax {
	
	public Tax(TaxType taxType, double value, double rate){
		this.rate=rate;
		this.taxType= taxType;
		this.value= value;
	}
	
	private TaxType taxType;
	private double value;
	private double rate;
	
	public static enum TaxType{
		IGST,CGST,SGST,GST;
	}
}
