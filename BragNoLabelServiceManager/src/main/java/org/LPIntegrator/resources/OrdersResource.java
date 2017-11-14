package org.LPIntegrator.resources;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.LPIntegrator.DropWizard.ClientAuthorization;
import org.LPIntegrator.batch.Job;
import org.LPIntegrator.modelTransformers.GetOrdersRequestToShopifyOrdersQuery;
import org.LPIntegrator.modelTransformers.OrderToShopifyOrderTransformer;
import org.LPIntegrator.service.OrderService;
import org.LPIntegrator.service.models.GetOrdersRequest;
import org.LPIntegrator.service.models.OrderStatusUpdateRequest;
import org.ShopifyInegration.models.ShopifyOrder;
import org.apache.log4j.Logger;
import org.shopifyApis.models.ShopifyOrdersQuery;
import com.google.inject.Inject;
import com.shopify.api.models.Order;
import io.dropwizard.hibernate.UnitOfWork;


@Path("{clientId}/orders")
public class OrdersResource {

	Logger logger = Logger.getLogger(OrdersResource.class);

	@Inject
	private OrderService orderService;


	@POST
	@Path("/updateOrders")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	@ClientAuthorization
	public List<ShopifyOrder> getOpenOrders(@PathParam("clientId") int clientId, GetOrdersRequest request){
		logger.info("client id - "+clientId);
		Optional<GetOrdersRequest> requestOptional = Optional.of(request);
		Optional<ShopifyOrdersQuery> shopifyOrdersQueryOptional = requestOptional.map(new GetOrdersRequestToShopifyOrdersQuery());
		return orderService.pullOrders(shopifyOrdersQueryOptional,  clientId);
	}


	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	@ClientAuthorization
	public Response create(@PathParam("clientId") int clientId, Order order){
		Optional<ShopifyOrder> shopifyOrderOptional = Optional.of(order).map(new OrderToShopifyOrderTransformer());
		shopifyOrderOptional.ifPresent(t -> {t.setClientId(String.valueOf(clientId));});
		orderService.saveOrder(shopifyOrderOptional);
		return Response.noContent().status(javax.ws.rs.core.Response.Status.CREATED).build();
	}

	@GET
	@Path("/getOrder/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	@ClientAuthorization
	public ShopifyOrder getOrder(@PathParam("orderId") String orderId) throws Throwable{
		return orderService.getShopifyOrder(orderId);
	}

	@POST
	@Path("/whorder/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	@ClientAuthorization
	public Response createOrderinWareHouse(@PathParam("clientId") int clientId, @PathParam("orderId") String orderId) throws Throwable{
		orderService.createOrderinWH(orderId, clientId);
		return Response.noContent().status(javax.ws.rs.core.Response.Status.CREATED).build();

	}

	@POST
	@Path("/updateshipmentstatus")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	@ClientAuthorization
	public List<String> getOrder(OrderStatusUpdateRequest orderStatusUpdateRequest) throws Throwable{
		return orderService.updateShipmentStatus(orderStatusUpdateRequest);
	}


	@POST
	@Path("/whorder/cancel")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	@ClientAuthorization
	public Map<String, String> cancelWareHouseOrder(@PathParam("clientId") int clientId, Order order) throws Throwable{
		return orderService.cancelWareHouseOrder(clientId, order);
	}


	@POST
	@Path("/triggerJob/{jobName}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	@ClientAuthorization
	public void triggerJob(@PathParam("clientId") int clientId, @PathParam("jobName") String jobName, String orderStatusUpdateRequest) throws Throwable{

		logger.info("job name - "+jobName);
		Job job = Job.valueOf(jobName);
		if(job==null){
			logger.error("invlaid job name. not proceeding");
			throw new WebApplicationException(Response.status(Status.NOT_ACCEPTABLE).entity("invalid job name").build());
		}

		logger.info("Triggering job with job name - "+jobName);
		
		switch(job){
		case WHOrderCreation:{
			orderService.PushEligibleOrdersToWarehouse(clientId);
		}
		default:{
			logger.error("no jobs configured for this job name");
		}
		}


	}

}
