package org.LPIntegrator.hibernate.daos;

import java.util.List;
import java.util.Optional;

import org.LPIntegrator.hibernate.ClientEntity;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.google.inject.Inject;

import io.dropwizard.hibernate.AbstractDAO;

public class ClientEntityDAO extends AbstractDAO<ClientEntity> {

	@Inject
	public ClientEntityDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	public List<ClientEntity> getAllClients(){
		Query<ClientEntity> query = currentSession().createQuery("from ClientEntity", ClientEntity.class);
		return query.getResultList();
	}
	
	public Optional<ClientEntity> getClientById(int id){
		Query<ClientEntity> query = currentSession().createQuery("from ClientEntity where id = :id", ClientEntity.class);
		query.setParameter("id", id);
		return query.uniqueResultOptional();
	}

}
