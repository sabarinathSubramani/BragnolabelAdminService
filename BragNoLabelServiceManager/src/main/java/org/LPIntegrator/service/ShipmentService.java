package org.LPIntegrator.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.LPIntegrator.hibernate.OrderLineItemEntity;
import org.LPIntegrator.hibernate.ShipmentTrackingEntity;
import org.LPIntegrator.hibernate.daos.OrderEntityDAO;
import org.LPIntegrator.hibernate.daos.OrderLineItemsEntityDAO;
import org.LogisticsPartner.Error;
import org.LogisticsPartner.ShipmentTrackingRequest;
import org.LogisticsPartner.ShipmentTrackingResponse;
import org.LogisticsPartner.Tracking;
import org.LogisticsPartner.Client.LPClientFactory;
import org.LogisticsPartner.Client.LPLastMileClient;
import org.LogisticsPartner.Client.LPWareHouseClient;
import org.apache.log4j.Logger;
import org.shopifyApis.client.ShopifyOrdersClient;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class ShipmentService {

	static Logger logger = Logger.getLogger(OrderService.class);

	/*	@Inject
	private OrderLineItemsEntityDAO orderLineItemsEntityDAO;*/
	@Inject
	javax.ws.rs.client.Client jerseyClient;

	@Inject 
	private OrderLineItemsEntityDAO orderLineItemsEntityDAO;


	public ShipmentTrackingResponse updateTrackingInformation(ShipmentTrackingRequest shipmentTrackingRequest) {
		Set<String> trackingNumbers = shipmentTrackingRequest.getTrackingNumbers();
		ShipmentTrackingResponse response = null;
		OrderLineItemEntity[] inTransitOrdersByLP = null;
		if(trackingNumbers == null || trackingNumbers.isEmpty()) {
			trackingNumbers = Sets.newHashSet();
			try {
				logger.info("Fetch all in transit orders");
				inTransitOrdersByLP = orderLineItemsEntityDAO.getInTransitOrdersByLP(shipmentTrackingRequest.getLp()).toArray( t -> { return new OrderLineItemEntity[t];});
				for(OrderLineItemEntity ole : inTransitOrdersByLP) {
					trackingNumbers.add(ole.getTrackingNumber().getTrackingNumber());
				}
				shipmentTrackingRequest.setTrackingNumbers(trackingNumbers);

			}catch(Exception e) {
				logger.error("unable to get tracking numbers from Database", e);
				response = ShipmentTrackingResponse.getResponseWithError(500, "unable to get tracking numbers for tracking.");
				response.setRequest(shipmentTrackingRequest);
				throw new WebApplicationException(Response.status(500).entity(response).build());
			}
		}else {
			inTransitOrdersByLP = orderLineItemsEntityDAO.getOrdersByTrackingNumbers(shipmentTrackingRequest.getTrackingNumbers()).toArray( t -> { return new OrderLineItemEntity[t];});
		}
		LPLastMileClient lpClient = (LPLastMileClient) LPClientFactory.getClient(shipmentTrackingRequest.getLp(), jerseyClient);
		response = lpClient.trackPackage(shipmentTrackingRequest);
		response.initError();
		for(OrderLineItemEntity ole : inTransitOrdersByLP) {
			Tracking tracking = response.getTracking().get(ole.getTrackingNumber().getTrackingNumber());
			if(tracking == null) {
				response.getError().getErrorMessages().add("Tracking Information missing for tracking number- "+ ole.getTrackingNumber().getTrackingNumber());
			}else {
				try {
				ShipmentTrackingEntity ste = ole.getTrackingNumber();
				ste.setShipmentStatus(tracking.getCurrentStatus().getStatus());
				ste.setStatusDate(tracking.getCurrentStatus().getStatusDate().toDate());
				ste.setShipmentLocation(tracking.getCurrentStatus().getLocation());
				}catch(Exception e) {
					response.getError().getErrorMessages().add("Unable to update Shipping info for - "+ tracking.getWaybillNumber()+". Exception -"+e.getMessage() );
				}
			}
		}
		try {
			orderLineItemsEntityDAO.saveOrderLineItemsEntity(inTransitOrdersByLP);
		}catch(Exception e) {
			logger.error("Unable to save tracking info in DB", e);
			response.getError().getErrorMessages().add("Unable to save tracking info in DB");
			throw new WebApplicationException(Response.status(500).entity(response).build());
		}

		return response;
	}

}
