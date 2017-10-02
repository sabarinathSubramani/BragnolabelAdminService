package org.LogisticsPartner.Client;

import java.util.Optional;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import org.DelhiveryClient.Transformers.ShopifyOrderTransformer;
import org.DelhiveryClient.models.CreateOrderRequest;
import org.DelhiveryClient.models.DelhiveryOrder;
import org.DelhiveryClient.models.SubOrders;

import com.google.inject.Inject;

public class DelhiveryClientImpl implements LPClient{

	private javax.ws.rs.client.Client jerseyClient;
	public DelhiveryClientImpl(javax.ws.rs.client.Client client){
		this.jerseyClient=client;
	}
	
	@Override
	public String pushOrdersToWareHouse(CreateOrderRequest createOrderRequest) {

		org.ShopifyInegration.models.Client caller = createOrderRequest.getClient();
		if(caller== null)
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("Client information not valid").build());
		String token = caller.getCredentials().get("delhivery").get("token");
		String accessKey = caller.getCredentials().get("delhivery").get("accessKey");

		WebTarget target = jerseyClient.target(DelhiveryClientConfiguration.url).path(DelhiveryClientConfiguration.createOrdersPath);
		DelhiveryOrder delhiveryOrder = Optional.of(createOrderRequest.getShopifyOrder()).map(ShopifyOrderTransformer.toDelhiveryOrder()).get();
		for(SubOrders so : delhiveryOrder.getSubOrders())
			so.setAccess_key(accessKey);
		
		Response response = target.request().header("Authorization", "Token "+token).post(Entity.entity(delhiveryOrder, MediaType.APPLICATION_JSON));
		if(response.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)){
			return response.getEntity().toString();
		}else{
			throw new WebApplicationException(response.getStatus());
		}
	}

}
