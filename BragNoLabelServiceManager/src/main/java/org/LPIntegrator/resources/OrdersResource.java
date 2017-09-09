 package org.LPIntegrator.resources;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.LPIntegrator.modelTransformers.GetOrdersRequestToShopifyOrdersQuery;
import org.LPIntegrator.modelTransformers.OrderToShopifyOrderTransformer;
import org.LPIntegrator.models.GetOrdersRequest;
import org.LPIntegrator.models.ShopifyOrder;
import org.LPIntegrator.service.OrderService;
import org.shopifyApis.models.ShopifyOrdersQuery;

import com.shopify.api.models.Order;

import io.dropwizard.hibernate.UnitOfWork;


@Path("/orders")
public class OrdersResource {

	private OrderService orderService = null;
	
	public OrdersResource(OrderService orderService){
		this.orderService =orderService;
	}
	
	@POST
	@Path("/updateOrders")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public List<ShopifyOrder> getOpenOrders(GetOrdersRequest request){
		Optional<GetOrdersRequest> requestOptional = Optional.of(request);
		Optional<ShopifyOrdersQuery> shopifyOrdersQueryOptional = requestOptional.map(new GetOrdersRequestToShopifyOrdersQuery());
		return orderService.getOrders(shopifyOrdersQueryOptional);
	}
	
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public void create(Order order){
		Optional<ShopifyOrder> shopifyOrderOptional = Optional.of(order).map(new OrderToShopifyOrderTransformer());
		orderService.saveOrder(shopifyOrderOptional);
	}
	
	@POST
	@Path("/getOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public void getOrder(String orderId){
		orderService.getOrder(orderId);
	}
	
}
