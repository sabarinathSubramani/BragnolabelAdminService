package org.shopifyApis.client.internals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.shopifyApis.client.ShopifyApiConfiguration;
import org.shopifyApis.client.ShopifyOrdersClient;
import org.shopifyApis.models.ErrorMessage;
import org.shopifyApis.models.ShopifyApiException;
import org.shopifyApis.models.ShopifyOrdersQuery;
import org.shopifyApis.models.ShopifyResponse;

import com.shopify.api.models.Order;
import com.shopify.api.resources.json.ShopifyResponseReader;

public class ShopifyOrdersClientImpl implements ShopifyOrdersClient {

	private final Logger logger  = Logger.getLogger(ShopifyOrdersClientImpl.class);
	private final String authorizationKey;
	private final Client client;

	public ShopifyOrdersClientImpl(Client client){
		authorizationKey = generateAuthorizationKey();
		this.client = client;
	}


	public Optional<List<Order>> getOrders(Optional<ShopifyOrdersQuery> ordersQuery) throws ShopifyApiException  {

		WebTarget target = client.target(ShopifyApiConfiguration.url).path(ShopifyApiConfiguration.getOrdersPath);

		Optional<Map<String, Object>> queryParams = ordersQuery.map(new ShopifyApiQueryGenerator());

		if(queryParams.isPresent()){
			for(Entry<String, Object> e :queryParams.get().entrySet() ){
				if(e.getValue()!=null)
					target.queryParam(e.getKey(), e.getValue());
			}
		}

		Response response = target.request().header("Authorization", "Basic "+authorizationKey).get();
		/*try {
			logger.info("response string");
			logger.info(IOUtils.toString((InputStream) response.getEntity()));
			response.bufferEntity();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		if(response.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)){
			return Optional.of(new ShopifyResponseReader().read(new InputStreamReader((InputStream) response.getEntity()), Order.class));
		}else{
			ErrorMessage errorMessage = new ErrorMessage(response.getStatusInfo().getReasonPhrase(), response.getStatusInfo().getFamily().toString());
			throw new ShopifyApiException(errorMessage);
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

}
