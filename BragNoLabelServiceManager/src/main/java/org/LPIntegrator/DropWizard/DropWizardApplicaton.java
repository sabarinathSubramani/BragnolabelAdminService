package org.LPIntegrator.DropWizard;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.LPIntegrator.hibernate.ClientEntity;
import org.LPIntegrator.hibernate.OrderEntity;
import org.LPIntegrator.hibernate.OrderLineItemEntity;
import org.LPIntegrator.hibernate.ShippingAddressEntity;
import org.LPIntegrator.hibernate.daos.ClientEntityDAO;
import org.LPIntegrator.resources.OrdersResource;
import org.LPIntegrator.resources.ShipmentResource;
import org.LPIntegrator.service.ClientService;
import org.LPIntegrator.service.cache.CacheEnum;
import org.LPIntegrator.service.cache.CacheableClient;
import org.LPIntegrator.utils.cache.CacheRegistry;
import org.glassfish.jersey.logging.LoggingFeature;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.logging.LoggingUtil;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DropWizardApplicaton extends Application<DropWizardConfiguration> {

	private HibernateBundle<DropWizardConfiguration> hibernateBundle;

	
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
				new HibernateBundle<DropWizardConfiguration>(OrderEntity.class, OrderLineItemEntity.class, ShippingAddressEntity.class, ClientEntity.class) {
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

	@Override
	public void run(DropWizardConfiguration configuration, Environment environment) throws Exception {

		environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
		environment.getObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		environment.jersey().register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), Level.ALL,LoggingFeature.Verbosity.PAYLOAD_TEXT, null));

		Injector injector = Guice.createInjector(new InjectionModule(configuration, environment, hibernateBundle.getSessionFactory()), new ClientServiceInjectionModule(hibernateBundle));
		environment.jersey().register(injector.getInstance(OrdersResource.class));
		environment.jersey().getResourceConfig().register(ClientAutorizationFilter.class);
		LoggingUtil.getLoggerContext().getLogger(this.getClass()).info("Initlaizing cache here");
		CacheRegistry.initializeCache(CacheEnum.ClientCache, injector.getInstance(CacheableClient.class));
	}
	
	public static void main(String[] args) throws Exception {
		new DropWizardApplicaton().run(args);
	}

}
