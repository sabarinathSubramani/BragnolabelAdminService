package org.shopifyApis.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopifyApiException extends Exception{
	

	private ErrorMessage errorMessage;
	public ShopifyApiException( ErrorMessage errorMessage){
		super("Api call failed with status - "+errorMessage.getErrorType());
		this.errorMessage=errorMessage;
	}
	
}
