package org.LPIntegrator.DropWizard;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Data;

@Data
public class DropWizardConfiguration extends Configuration {

	private DataSourceFactory dataSource;
	private JerseyClientConfiguration jerseyClient;
}
