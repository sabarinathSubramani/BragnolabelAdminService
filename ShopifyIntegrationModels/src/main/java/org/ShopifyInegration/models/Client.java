package org.ShopifyInegration.models;

import java.util.Map;
import lombok.Data;

@Data
public class Client {

	private int clientId;
	private String clientName;
	private boolean isActive;
	private Map<String,Map<String, String>> credentials;
	
}
