package org.shopifyApis.client.internals;

import java.io.InputStream;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import org.shopifyApis.client.ShopifyApiConfiguration;
import org.shopifyApis.client.ShopifyOrdersClient;
import org.shopifyApis.models.ErrorMessage;
import org.shopifyApis.models.ShopifyApiException;
import org.shopifyApis.models.ShopifyOrdersQuery;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.shopify.api.models.FulfillmentRequest;
import com.shopify.api.models.Order;

import ch.qos.logback.classic.Logger;
import io.dropwizard.logging.LoggingUtil;
import lombok.Data;

public class ShopifyOrdersClientImpl implements ShopifyOrdersClient {

	Logger logger  = LoggingUtil.getLoggerContext().getLogger(ShopifyOrdersClientImpl.class);
	private final String authorizationKey;

	@Inject
	private Client client;

	public ShopifyOrdersClientImpl(){
		authorizationKey = generateAuthorizationKey();
	}


	public Optional<List<Order>> getOrders(Optional<ShopifyOrdersQuery> ordersQuery) throws ShopifyApiException  {

		/*ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, state)*/
		Response response = null;
		try{

			WebTarget target = client.target(ShopifyApiConfiguration.url).path(ShopifyApiConfiguration.getOrdersPath);

			Optional<Map<String, Object>> queryParams = ordersQuery.map(new ShopifyApiQueryGenerator());

			if(queryParams.isPresent()){
				for(Entry<String, Object> e :queryParams.get().entrySet() ){
					if(e.getValue()!=null)
						target =target.queryParam(e.getKey(), e.getValue());
				}
			}

			response = target.request().header("Authorization", "Basic "+authorizationKey).get();

			if(response.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)){
				return Optional.of(response.readEntity(OrdersList.class).getOrders());
				//return Optional.of(new ShopifyResponseReader().read(new InputStreamReader((InputStream) response.getEntity()), Order.class));
			}else{
				ErrorMessage errorMessage = new ErrorMessage(response.getStatusInfo().getReasonPhrase(), response.getStatusInfo().getFamily().toString());
				throw new ShopifyApiException(errorMessage);
			}
		}finally{
			if(response != null)
				response.close();
		}

	}


	private String generateAuthorizationKey(){
		logger.info("Generating authorization credentials");
		try{
			InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			properties.load(resourceAsStream);
			String apikey = properties.getProperty("apikey");
			String password = properties.getProperty("password");
			Encoder encoder = Base64.getEncoder();
			logger.info("Authorization key generated successfully");
			return encoder.encodeToString((apikey+":"+password).getBytes());
		}catch(Exception e){
			throw new RuntimeException("Authorization key generation failed");
		}
	}

	@Data
	public static class OrdersList{
		List<Order> orders;
	}

	@Override
	public String updateFulfillment(String orderId, FulfillmentRequest fulfillmentRequest) {

		Response response = null;
			String fulfillmentPath = ShopifyApiConfiguration.updateFulfillmentPath.replace("{orderId}", orderId);
			WebTarget target = client.target(ShopifyApiConfiguration.url).path(fulfillmentPath);
			ObjectMapper obj = new ObjectMapper();
			obj.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			String req = null;
			try {
				req = obj.writeValueAsString(fulfillmentRequest);
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			response = target.request().header("Authorization", "Basic "+authorizationKey).header("Content-Type", "application/json").post(Entity.entity(req, MediaType.APPLICATION_JSON));

			if(response.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)){
				response.close();
				return "fulfillment updated successfully - "+orderId;
			}else{
				throw new WebApplicationException(response);
			}
	}

}
