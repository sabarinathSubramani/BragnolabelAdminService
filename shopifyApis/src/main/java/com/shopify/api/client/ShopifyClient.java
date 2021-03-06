package com.shopify.api.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.client.Client;
import com.shopify.api.APIAuthorization;
import com.shopify.api.credentials.Credential;
import com.shopify.api.endpoints.BaseShopifyService;
import com.shopify.api.endpoints.EndpointImpl;
import com.shopify.api.models.ShopifyResource;
import com.shopify.api.resources.json.ShopifyRequestWriter;
import com.shopify.api.resources.json.ShopifyResponseReader;


public class ShopifyClient {
	private Client client;
	private APIAuthorization auth;
	private Credential creds;
	private ShopifyResponseReader reader = new ShopifyResponseReader();
	private ShopifyRequestWriter writer = new ShopifyRequestWriter();
	
	public ShopifyClient(Credential creds, Client client) {
		this.creds = creds;
		this.auth = new APIAuthorization(this.creds);
		this.client = client;
	}

	private HashMap<String, String> constructConfiguration(){
		return new HashMap<String, String>(){{
			put("service.end-point", getEndpoint());
		}};
	}
	
	private String getEndpoint() {
		return "https://"+creds.getShopName()+".myshopify.com";
	}
	
	/*private HttpClientRestService constructClientRestService() {
		return new HttpClientRestService(auth.getAuthorizedClient());
	}*/
	
	public <T extends BaseShopifyService> T constructService(Class<T> interfaze){
			return constructEndpointImpl(interfaze);
	}
	
   /*public <T extends BaseShopifyService> T constructInterface(Class<T> interfaze){
		return crestClient.build(interfaze);
	}*/
	
	public <T extends BaseShopifyService> T constructEndpointImpl(Class<T> clazz) {
		try {
			EndpointImpl instance = (EndpointImpl)clazz.newInstance();
			instance.setEndpoint(getEndpoint());
			instance.setHttpClient(auth.getAuthorizedClient());
			
			T service = constructService((Class<T>)instance.getServiceClass());
			instance.setServiceInterface(service);
			
			return (T) instance;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public <T extends ShopifyResource> List<T> handleResponse(InputStream in, Class<T> resource){
		return reader.read(new InputStreamReader(in), resource);
	}
	
	public <T extends ShopifyResource> String convertToJson(T object) throws IOException {
		StringWriter w = new StringWriter();
		writer.write(w, object);
		return w.toString();
	}

}
