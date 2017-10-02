package org.LPIntegrator.DropWizard;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.LPIntegrator.service.cache.CacheEnum;
import org.LPIntegrator.utils.cache.CacheRegistry;
import org.ShopifyInegration.models.Client;
import org.apache.log4j.Logger;

import com.google.common.cache.LoadingCache;

import io.dropwizard.hibernate.UnitOfWork;

@Provider
@Delhivery
public class DelhiveryClientRequestFilter implements ClientRequestFilter {

	Logger logger = Logger.getLogger(DelhiveryClientRequestFilter.class);

	@Override
	@UnitOfWork
	public void filter(ClientRequestContext requestContext) throws IOException {
		
		List<Object> list = requestContext.getHeaders().get("X-CLIENT-ID");
		if(list!=null && list.size()>0){
			Integer clientId = Integer.valueOf(list.get(0).toString());
			LoadingCache<Integer, Client> asLoadingCache = CacheRegistry.getAsLoadingCache(CacheEnum.ClientCache);
			try {
				Client client = asLoadingCache.get(clientId);
				if(client != null)
					requestContext.getHeaders().add("Authorization", client.getCredentials().get("Delhivery").get("access_key"));
			} catch (ExecutionException e) {
				logger.error("unable to retreive client from cache. denying the request");
			}
		}
		else{
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Delhivery Client credentials not found").build());
		}

	}

}
