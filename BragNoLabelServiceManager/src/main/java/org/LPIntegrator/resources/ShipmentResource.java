package org.LPIntegrator.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.LPIntegrator.DropWizard.ClientAuthorization;
import org.LPIntegrator.service.OrderService;
import org.LPIntegrator.service.models.OrderStatusUpdateRequest;
import org.ShopifyInegration.models.ShopifyOrder;

import com.google.inject.Inject;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/shipment")
public class ShipmentResource {
	
	@Inject
	private OrderService orderService;
	
	@POST
	@Path("/updateShipment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public List<String> getOrder(OrderStatusUpdateRequest orderStatusUpdateRequest) throws Throwable{
		return orderService.updateShipmentStatus(orderStatusUpdateRequest);
	}

}
