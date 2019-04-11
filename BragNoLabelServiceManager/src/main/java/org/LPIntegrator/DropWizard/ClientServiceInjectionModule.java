package org.LPIntegrator.DropWizard;

import org.LPIntegrator.hibernate.daos.ClientEntityDAO;
import org.LPIntegrator.service.ClientService;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;

public class ClientServiceInjectionModule extends AbstractModule{


	private final HibernateBundle hibernateBundle;
	

	public ClientServiceInjectionModule(HibernateBundle hibernateBundle) {
		this.hibernateBundle = hibernateBundle;
	}


	@Provides
	@Singleton
	private ClientService clientService() {
		return  new UnitOfWorkAwareProxyFactory(hibernateBundle).create(ClientService.class, ClientEntityDAO.class, new ClientEntityDAO(hibernateBundle.getSessionFactory()));
	}


	@Override
	protected void configure() {
		
	}

}

