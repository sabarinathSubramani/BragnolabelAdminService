package org.Delhivery.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import org.Delhivery.Transformers.TrackingResponseTransformer;
import org.Delhivery.models.DelhiveryTrackingResponse;
import org.LogisticsPartner.CreatePackageRequest;
import org.LogisticsPartner.Error;
import org.LogisticsPartner.LPClientRequest;
import org.LogisticsPartner.LPClientResponse;
import org.LogisticsPartner.ShipmentTrackingRequest;
import org.LogisticsPartner.ShipmentTrackingResponse;
import org.LogisticsPartner.Client.LPLastMileClient;
import org.ShopifyInegration.models.Address;
import org.ShopifyInegration.models.OrderType;
import org.ShopifyInegration.models.ShopifyOrder;
import org.ShopifyInegration.models.ShopifyOrderLineItem;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

public class DelhiveryLMClientImpl implements LPLastMileClient{

	private javax.ws.rs.client.Client jerseyClient;
	public DelhiveryLMClientImpl(javax.ws.rs.client.Client client){
		this.jerseyClient=client;
	}

	@Override
	public ShipmentTrackingResponse trackPackage(ShipmentTrackingRequest shipmentTrackingRequest) {

		ShipmentTrackingResponse stResponse = null; 
		Error error = new Error();
		String token = checkClientParamsAndReturnToken(shipmentTrackingRequest);
		String trackingNumbers = "";
		if(shipmentTrackingRequest.getTrackingNumbers()== null || shipmentTrackingRequest.getTrackingNumbers().isEmpty()) {
			stResponse = new ShipmentTrackingResponse();
			stResponse.setRequest(shipmentTrackingRequest);
			error.setMessage("Tracking numbers list empty. Please check the request");
			error.setCode(400);
			stResponse.setError(error);
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(stResponse).build());
		}
		else {
			for(String t : shipmentTrackingRequest.getTrackingNumbers()) {
				if(!trackingNumbers.isEmpty()) {
					trackingNumbers = trackingNumbers+",";
				}
				trackingNumbers = trackingNumbers + t;
			}
		}


		Builder request = jerseyClient.target(DelhiveryLMClientConfiguration.url)
				.path(DelhiveryLMClientConfiguration.TRACK_SHIPMENT)
				.queryParam("token", token)
				.queryParam("waybill",trackingNumbers)
				.queryParam("verbose", 1)
				.request();
		request.accept(MediaType.APPLICATION_JSON_TYPE);
		Response response = request.get();
		response.bufferEntity();
		if(response.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)){
			try {
				DelhiveryTrackingResponse readEntity = response.readEntity(DelhiveryTrackingResponse.class);
				Optional<ShipmentTrackingResponse> optional = Optional.of(readEntity).map(TrackingResponseTransformer.toTrackingResponse());
				stResponse = optional.get();
			}catch(Exception e) {
				stResponse = new ShipmentTrackingResponse();
				stResponse.setRequest(shipmentTrackingRequest);
				error.setMessage("Error transforming response");
				error.setErrorMessages(Lists.newArrayList(e.getMessage()));
				error.setCode(500);
				stResponse.setError(error);
				throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity(stResponse).build());
			}
		}else {
			throw new WebApplicationException(response);
		}
		return stResponse;

	}

	@Override
	public DelhiveryCreatePackageResponse createPackage(CreatePackageRequest createPackageRequest) {
		DelhiveryCreatePackageResponse cpResponse = null;
		String token = checkClientParamsAndReturnToken(createPackageRequest);
		DelhiveryCreatePackageRequest request = new DelhiveryCreatePackageRequest();

		List<ShopifyOrder> shopifyOrders = createPackageRequest.getShopifyOrders();
		List<Shipment> shipments  = new ArrayList<Shipment>();

		for(ShopifyOrder shopifyOrder: shopifyOrders) {

			Shipment shipment = new Shipment();
			
			//set buyer address location
			Address shippingAddress = shopifyOrder.getShippingAddress();
			shipment.setName(shopifyOrder.getShippingAddress().getFullName());
			String address = shippingAddress.getAddressLine1() +", "+ shippingAddress.getAddressLine2();
			shipment.setAdd(address);
			shipment.setCity(shippingAddress.getCity());
			shipment.setCountry(shippingAddress.getCountry());
			shipment.setPhone(shippingAddress.getPhone());
			shipment.setPin(String.valueOf(shippingAddress.getZip()));
			shipment.setState(shippingAddress.getState());

			shipment.setReturnName("Clearlane Fashions Private Limited");
			shipment.setReturnAdd("No.16, 3rd Floor, 2nd Cross, Samrudhi Layout, Banjara Arches, Horamavu");
			shipment.setReturnCity("Bangalore");
			shipment.setReturnState("Karnataka");
			shipment.setReturnPin("560043");
			shipment.setReturnCountry("India");


			shipment.setOrder(shopifyOrder.getId());
			shipment.setTotalAmount(String.valueOf(shopifyOrder.getTotalPrice()));

			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			shipment.setOrderDate(formatter.print(shopifyOrder.getCreatedAt()));
			int qty = 0;
			for(ShopifyOrderLineItem sol : shopifyOrder.getOrderLineItems()) {
				qty = qty + sol.getQuantity();
			}
			shipment.setQuantity(String.valueOf(qty));
			shipment.setSellerName("Clearlane Fashions Private Limited");
			shipment.setSellerAdd("No.16, 3rd Floor, 2nd Cross, Samrudhi Layout, Banjara Arches, Horamavu");
			shipment.setSellerTin("");
			if(shopifyOrder.getOrderType().equals(OrderType.COD)) {
				shipment.setPaymentMode("COD");
				shipment.setCodAmount(String.valueOf(shopifyOrder.getTotalPrice()-shopifyOrder.getDiscount()));
			}else{
				shipment.setPaymentMode("Prepaid");
			}
			shipments.add(shipment);
		}

		request.setShipments(shipments);
		
		//set pickup location
		PickupLocation pickupLocation = new PickupLocation();
		pickupLocation.setName("BRAGNOLABEL");
		pickupLocation.setAdd("No.16, 3rd Floor, 2nd Cross, Samrudhi Layout, Banjara Arches, Horamavu");
		pickupLocation.setCity("Bangalore");
		pickupLocation.setState("Karnataka");
		pickupLocation.setPin("560043");
		pickupLocation.setCountry("India");
		request.setPickupLocation(pickupLocation);
		

		Builder builder = jerseyClient.target(DelhiveryLMClientConfiguration.url)
				.path(DelhiveryLMClientConfiguration.CREATE_PACKAGE)
				.request();
		builder.accept(MediaType.APPLICATION_JSON_TYPE);
		builder.header("Authorization", "Token "+token);
		String toString = null;
		try {
			ObjectMapper om = new ObjectMapper();
			toString = om.writeValueAsString(request);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
			throw new WebApplicationException("unable to convert request to string");
		}
		String requestString = "format=json&data="+toString; 
		Response response = builder.post(Entity.entity(requestString, MediaType.TEXT_PLAIN_TYPE));
		if(response.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)){
			try {

				Error error =new Error();
				error.setErrorMessages(Lists.newArrayList());
				cpResponse = response.readEntity(DelhiveryCreatePackageResponse.class);
				for(Package p : cpResponse.getPackages()) {
					if(!p.getStatus().equals("Success")) {
						error.getErrorMessages().add("Failed creating package for Order number - "+p.getRefnum()+". Reason - "+p.getRemarks());
					}
				}
				cpResponse.setError(error);
			}catch(Exception e) {
				response.bufferEntity();
				throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build());
			}
		
		}
		return cpResponse;
	}


	private String checkClientParamsAndReturnToken(LPClientRequest request) {
		org.LogisticsPartner.Error error = new org.LogisticsPartner.Error();
		org.ShopifyInegration.models.Client caller = request.getClient();
		if(caller== null) {
			LPClientResponse response = new LPClientResponse();
			response.setRequest(request);
			error.setMessage("Client information not valid. Please check the response");
			error.setCode(401);
			response.setError(error); 	
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity(response).build());
		}
		String token = null;
		try{
			token = caller.getCredentials().get("delhivery").get("lmToken");
		}catch(Exception e) {
			LPClientResponse response = new LPClientResponse();
			response.setRequest(request);
			error.setMessage("Lastmile api token missing. Please check config");
			error.setCode(406);
			response.setError(error);
			throw new WebApplicationException(Response.status(Status.NOT_ACCEPTABLE).entity(response).build());
		}
		return token;
	}


}
