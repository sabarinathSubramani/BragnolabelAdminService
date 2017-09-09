package org.shopifyApis.models;

import javax.ws.rs.core.Response.StatusType;
import lombok.Data;

@Data
public class ShopifyResponse {
	
	private String response;
	private ErrorMessage error;
	private StatusType status;
	
	public ShopifyResponse(String response, ErrorMessage error, StatusType status) {
		this.response = response;
		this.error = error;
		this.status = status;
	}
	
}
