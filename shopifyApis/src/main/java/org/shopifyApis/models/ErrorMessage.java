package org.shopifyApis.models;

import lombok.Data;

@Data
public class ErrorMessage {

	private String errorMessage;
	private String errorType;
	
	public ErrorMessage(String errorMessage, String errorType) {
		super();
		this.errorMessage = errorMessage;
		this.errorType = errorType;
	}
	
	
}
