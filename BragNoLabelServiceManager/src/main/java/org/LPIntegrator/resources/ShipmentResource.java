package org.LPIntegrator.resources;

import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.LPIntegrator.service.OrderService;
import org.LPIntegrator.service.ShipmentService;
import org.LPIntegrator.service.cache.CacheEnum;
import org.LPIntegrator.service.models.OrderStatusUpdateRequest;
import org.LPIntegrator.utils.cache.CacheRegistry;
import org.LogisticsPartner.ShipmentTrackingRequest;
import org.LogisticsPartner.ShipmentTrackingResponse;
import org.ShopifyInegration.models.Client;
import org.apache.log4j.Logger;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;

@Path("/shipment")
public class ShipmentResource {

	Logger logger = Logger.getLogger(ShipmentResource.class);

	@Inject
	private OrderService orderService;
	
	@Inject
	private ShipmentService shipmentService;


	@POST
	@Path("/shipmentNotification")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public List<String> updateShipmentStatus(OrderStatusUpdateRequest orderStatusUpdateRequest) throws Throwable{

		return orderService.updateShipmentStatus(orderStatusUpdateRequest);
	}
	
	@POST
	@Path("/{clientId}/trackShipments")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public ShipmentTrackingResponse updateTrackingInfo(@PathParam("clientId") int clientId, ShipmentTrackingRequest shipmentTrackingRequest) throws Throwable{
		LoadingCache<Integer, Client> asLoadingCache = CacheRegistry.getAsLoadingCache(CacheEnum.ClientCache);
		Client client = null;
		try {
			client = asLoadingCache.get(clientId);
		} catch (ExecutionException e) {
			throw new WebApplicationException("Client not found", 401);
		}
		
		ShipmentTrackingResponse stResponse = null;
		if(shipmentTrackingRequest.getLp() == null) {
			stResponse = new ShipmentTrackingResponse();
			stResponse.setRequest(shipmentTrackingRequest);
			org.LogisticsPartner.Error error = new org.LogisticsPartner.Error();
			error.setMessage("Logistics Parnter is not valid");
			error.setCode(400);
			stResponse.setError(error);
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(stResponse).build());
		}
		shipmentTrackingRequest.setClient(client);
		return shipmentService.updateTrackingInformation(shipmentTrackingRequest);
	}
}
