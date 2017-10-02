package org.LPIntegrator.DropWizard;

import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

import org.LPIntegrator.service.ClientService;
import org.glassfish.jersey.filter.LoggingFilter;
import org.hibernate.SessionFactory;
import org.shopifyApis.client.ShopifyOrdersClient;
import org.shopifyApis.client.internals.ShopifyOrdersClientImpl;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.cfg.JaxRSFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Environment;

public class InjectionModule extends AbstractModule{
	

	private final DropWizardConfiguration dropWizardConfiguration;
	  private final Environment environment;
	  private final SessionFactory sessionFactory;

	  public InjectionModule(DropWizardConfiguration dropWizardConfiguration, Environment environment,
	                       SessionFactory sessionFactory) {
	    this.dropWizardConfiguration = dropWizardConfiguration;
	    this.environment = environment;
	    this.sessionFactory = sessionFactory;
	  }

	  @Override
	  protected void configure() {
	    bind(DropWizardConfiguration.class).toInstance(dropWizardConfiguration);
	    bind(DataSourceFactory.class).toInstance(dropWizardConfiguration.getDataSource());
	    bind(SessionFactory.class).toInstance(sessionFactory);
	    bind(ShopifyOrdersClient.class).to(ShopifyOrdersClientImpl.class);
	  }

	  @Provides
	  @Singleton
	  private Client getClient() {
		  JacksonJsonProvider jackson_json_provider = new JacksonJaxbJsonProvider()
				  .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				  .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
				  
	    Client client = new JerseyClientBuilder(environment)
	        .using(dropWizardConfiguration.getJerseyClient())
	        .build("client");
	    client.register(new LoggingFilter(Logger.getLogger("ClientLogger"), true));
	    client.register(jackson_json_provider);
	    return client;
	  }
	 
	  

	}
