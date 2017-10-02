package org.LPIntegrator.service.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.LPIntegrator.service.ClientService;
import org.LPIntegrator.utils.cache.Cacheable;
import org.LPIntegrator.utils.cache.RefreshPolicy;
import org.LPIntegrator.utils.cache.SizeBasedExpiration;
import org.LPIntegrator.utils.cache.TimedExpiration;
import org.ShopifyInegration.models.Client;

import com.google.inject.Inject;

public class CacheableClient implements Cacheable<Integer,Client> {

	@Inject
	ClientService clientService;

	@Override
	public Client loadValue(Integer key) {
		return clientService.getClientById(key);
	}


	@Override
	public TimedExpiration timedExpiration() {
		return null;
	}

	@Override
	public SizeBasedExpiration<Integer, Client> sizeBaseExpiration() {
		return new SizeBasedExpiration<>(100);
	}

	@Override
	public Map<Integer, Client> preLoad() {
		Map<Integer, Client> map = null; 
		List<Client> allClients = clientService.getAllClients();
		if(allClients != null && allClients.size() >0){
			map = new HashMap<>();
			for(Client client : allClients){
				map.put(client.getClientId(), client);
			}
		}
		return map!=null?map:Collections.emptyMap();
	}


	@Override
	public RefreshPolicy refreshPolicy() {
		return new RefreshPolicy(true, 30, TimeUnit.MINUTES);
	}


	@Override
	public int initialCapacity() {
		return 0;
	}


	@Override
	public int concurrencyLevel() {
		return 0;
	}


}
