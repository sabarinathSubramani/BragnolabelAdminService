package org.LogisticsPartner.Client;

import java.util.Optional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import org.DelhiveryClient.Transformers.ShopifyOrderTransformer;
import org.DelhiveryClient.models.CancelOrderRequest;
import org.DelhiveryClient.models.CreateOrderRequest;
import org.DelhiveryClient.models.CreateOrderResponse;
import org.DelhiveryClient.models.DelhiveryOrder;
import org.DelhiveryClient.models.SubOrders;
import org.apache.commons.lang3.ArrayUtils;

public class DelhiveryClientImpl implements LPClient{

	private javax.ws.rs.client.Client jerseyClient;
	public DelhiveryClientImpl(javax.ws.rs.client.Client client){
		this.jerseyClient=client;
	}

	@Override
	public CreateOrderResponse pushOrdersToWareHouse(CreateOrderRequest createOrderRequest) {

		CreateOrderResponse coResponse = new CreateOrderResponse();
		org.ShopifyInegration.models.Client caller = createOrderRequest.getClient();
		if(caller== null)
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("Client information not valid").build());
		String token = caller.getCredentials().get("delhivery").get("token");
		String accessKey = caller.getCredentials().get("delhivery").get("accessKey");

		WebTarget target = jerseyClient.target(DelhiveryClientConfiguration.url).path(DelhiveryClientConfiguration.createOrdersPath);
		DelhiveryOrder delhiveryOrder = Optional.of(createOrderRequest.getShopifyOrder()).map(ShopifyOrderTransformer.toDelhiveryOrder(caller)).get();
		for(SubOrders so : delhiveryOrder.getSubOrders())
			so.setAccess_key(accessKey);
		DelhiveryOrder[] array = ArrayUtils.toArray(delhiveryOrder);
		Response response = target.request().header("Authorization", "Token "+token).header("version","V2").post(Entity.entity(array, MediaType.APPLICATION_JSON));
		if(response.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)){
			coResponse.setResponse(response);
			return coResponse;
		}else{
			throw new WebApplicationException(response.getStatus());
		}
	}

	@Override
	public String cancelWareHouseOrder(CancelOrderRequest cancelOrderRequest) {
		org.ShopifyInegration.models.Client caller = cancelOrderRequest.getClient();
		if(caller== null)
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("Client information not valid").build());
		
		String token = caller.getCredentials().get("delhivery").get("token");

		WebTarget target = jerseyClient.target(DelhiveryClientConfiguration.url).path(DelhiveryClientConfiguration.cancelOrdersPath);

		target = target.queryParam("client_store", caller.getCredentials().get("delhivery").get("clientStore"))
			  .queryParam("fulfillment_center", caller.getCredentials().get("delhivery").get("fulfillmentCenter"))
			  .queryParam("version", "2014.09")
			  .queryParam("order_id", cancelOrderRequest.getOrderId())
			  .queryParam("sub_order_id", cancelOrderRequest.getSubOrderId());
		Response response = target.request().header("Authorization", "Token "+token).header("version","V2").header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON_TYPE).put(Entity.entity("", MediaType.APPLICATION_JSON), Response.class);
		if(response.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)){
			return response.getEntity().toString();
		}else{
			throw new WebApplicationException(response.getEntity().toString(), response.getStatus());
		}	
	}


}
