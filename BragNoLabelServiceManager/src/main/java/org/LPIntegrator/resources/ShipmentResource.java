package org.LPIntegrator.resources;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.LPIntegrator.service.OrderService;
import org.LPIntegrator.service.models.OrderStatusUpdateRequest;
import org.apache.log4j.Logger;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;

@Path("/shipment")
public class ShipmentResource {

	Logger logger = Logger.getLogger(ShipmentResource.class);

	@Inject
	private OrderService orderService;


	/*@POST
	@Path("/updateShipment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public List<String> getOrder(OrderStatusUpdateRequest orderStatusUpdateRequest) throws Throwable{
		return orderService.updateShipmentStatus(orderStatusUpdateRequest);
	}*/

	@POST
	@Path("/shipmentNotification")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public List<String> updateShipmentStatus(OrderStatusUpdateRequest orderStatusUpdateRequest) throws Throwable{

		return orderService.updateShipmentStatus(orderStatusUpdateRequest);
	}
}
