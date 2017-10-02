package org.LPIntegrator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.LPIntegrator.hibernate.ClientEntity;
import org.LPIntegrator.hibernate.daos.ClientEntityDAO;
import org.LPIntegrator.modelTransformers.ClientEntityTransformer;
import org.ShopifyInegration.models.Client;

import io.dropwizard.hibernate.UnitOfWork;

public class ClientService {

	ClientEntityDAO clientEntityDAO;
	
	public ClientService(ClientEntityDAO clientEntityDAO){
		this.clientEntityDAO = clientEntityDAO;
	}
	
	public List<Client> getAllClients(){
		List<Client> clientList = new ArrayList<>();
		List<ClientEntity> allClients = clientEntityDAO.getAllClients();
		allClients.stream().map(ClientEntityTransformer.clientEntityToClient()).forEach(clientList::add);
		return clientList;
	}
	
	@UnitOfWork
	public Client getClientById(int id){
		 Optional<Client> clientOptional = clientEntityDAO.getClientById(id).map(ClientEntityTransformer.clientEntityToClient());;
		 return clientOptional.orElse(null);
	}
	
}
