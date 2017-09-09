package org.LPIntegrator.service;

import java.util.logging.Logger;

import javax.ws.rs.client.Client;

import org.LPIntegrator.hibernate.OrderEntity;
import org.LPIntegrator.hibernate.OrderLineItemEntity;
import org.LPIntegrator.hibernate.ShippingAddressEntity;
import org.LPIntegrator.hibernate.daos.OrderEntityDAO;
import org.LPIntegrator.hibernate.daos.OrderLineItemsEntityDAO;
import org.LPIntegrator.modelTransformers.AddressToAddressEntity;
import org.LPIntegrator.resources.OrdersResource;
import org.glassfish.jersey.filter.LoggingFilter;
import org.shopifyApis.client.ShopifyApiClientFactory;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DropWizardApplicaton extends Application<DropWizardConfiguration> {

	private HibernateBundle<DropWizardConfiguration> hibernateBundle;

	@Override
	public void run(DropWizardConfiguration configuration, Environment environment) throws Exception {

		environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

		Client client = new JerseyClientBuilder(environment)
				.using(configuration.getJerseyClient())
				.build("client");
		client.register(new LoggingFilter(Logger.getLogger("ClientLogger"), true));
		OrderService orderService = new OrderService(new OrderEntityDAO(hibernateBundle.getSessionFactory()), new OrderLineItemsEntityDAO(hibernateBundle.getSessionFactory()), ShopifyApiClientFactory.create(client));
		environment.jersey().register(new OrdersResource(orderService));
	}

	/**
	 * Initializes the application bootstrap.
	 *
	 * @param bootstrap the application bootstrap
	 */
	public void initialize(Bootstrap<DropWizardConfiguration> bootstrap) {
		super.initialize(bootstrap);
		bootstrap.setConfigurationSourceProvider(
	            new ResourceConfigurationSourceProvider());

		hibernateBundle =
				new HibernateBundle<DropWizardConfiguration>(OrderEntity.class, OrderLineItemEntity.class, ShippingAddressEntity.class) {
			@Override
			public PooledDataSourceFactory getDataSourceFactory(DropWizardConfiguration configuration) {
				return configuration.getDataSource();
			}
		};

		bootstrap.addBundle(hibernateBundle);

		bootstrap.addBundle(new MigrationsBundle<DropWizardConfiguration>() {
			@Override
			public PooledDataSourceFactory getDataSourceFactory(DropWizardConfiguration configuration) {
				return configuration.getDataSource();
			}
		});

	}
	
	  public static void main(String[] args) throws Exception {
		    System.out.println("Hello from a Java Program");
		    for (int i=0; i < args.length; i++) {
		      System.out.println("args[" + i + "]=" + args[i]);
		    }
		    new DropWizardApplicaton().run(args);
		  }

}
