package org.LPIntegrator.modelTransformers;

import java.util.Map;
import java.util.function.Function;

import org.LPIntegrator.hibernate.ClientEntity;
import org.LPIntegrator.utils.LPIntegratorUtils;
import org.ShopifyInegration.models.Client;
import org.apache.commons.lang3.BooleanUtils;

public class ClientEntityTransformer{

	public static Function<Client, ClientEntity> clientToClientEntity(){
		return c -> { 
			ClientEntity clientEntity = new ClientEntity();
			clientEntity.setClientName(c.getClientName());
			clientEntity.setId(c.getClientId());
			clientEntity.setIsActive(BooleanUtils.toInteger(c.isActive()));
			clientEntity.setCredentials(LPIntegratorUtils.toJsonConverter(c.getCredentials()));
			return clientEntity;};
	}

	public static Function<ClientEntity, Client> clientEntityToClient(){
		return c -> { 
			Client client = new Client();
			client.setClientName(c.getClientName());
			client.setClientId(c.getId());
			client.setActive(BooleanUtils.toBoolean(c.getIsActive()));
			client.setCredentials(LPIntegratorUtils.jsonConverter(c.getCredentials(), Map.class));
			return client;};
	}


}
