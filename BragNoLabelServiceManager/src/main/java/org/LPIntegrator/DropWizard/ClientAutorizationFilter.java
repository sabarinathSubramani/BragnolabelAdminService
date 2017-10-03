package org.LPIntegrator.DropWizard;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.LPIntegrator.service.cache.CacheEnum;
import org.LPIntegrator.utils.cache.CacheRegistry;
import org.ShopifyInegration.models.Client;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.common.cache.LoadingCache;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.logging.LoggingUtil;

@ClientAuthorization
@Provider
public class ClientAutorizationFilter implements ContainerRequestFilter {
	
	Logger logger = Logger.getLogger(ClientAutorizationFilter.class);
	@Override
	@UnitOfWork
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(requestContext.hasEntity()){
			logger.info("request body  : "+IOUtils.toString(requestContext.getEntityStream(), "UTF-8"));
			
		}

		boolean authorized = false;
		List<String> list = requestContext.getUriInfo().getPathParameters().get("clientId");
		if(list!=null && list.size()>0){
			Integer clientId = Integer.valueOf(list.get(0));
			LoadingCache<Integer, Client> asLoadingCache = CacheRegistry.getAsLoadingCache(CacheEnum.ClientCache);
			try {
				Client client = asLoadingCache.get(clientId);
				if(client != null && client.isActive())
					authorized=true;
			} catch (ExecutionException e) {
				logger.error("unable to retreive client from cache. denying the request");
				authorized=false;
			}
		}
		if(!authorized)
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Client not allowed to make this call").build());

	}

}
