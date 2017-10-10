package org.LPIntegrator.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.LPIntegrator.service.OrderService;
import org.LPIntegrator.service.models.OrderStatusUpdateRequest;
import org.LPIntegrator.service.models.Orderlines;
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
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public Response updateShipmentStatus(OrderStatusUpdateRequest orderStatusUpdateRequest) throws Throwable{
		for(Orderlines ol : orderStatusUpdateRequest.getOrderlines()){
			logger.info("Shipment status. yet to be implemented - current status reported by delhivery for order line item id - "+ol.getOrder_id()+"_"+ol.getOrder_line_id()+" - "+ol.getStatus());
		}
		return Response.accepted().build();
	}
}
