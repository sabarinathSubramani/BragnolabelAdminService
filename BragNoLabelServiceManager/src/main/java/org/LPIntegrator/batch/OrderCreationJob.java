package org.LPIntegrator.batch;

import org.LPIntegrator.service.OrderService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.inject.Inject;

public class OrderCreationJob implements Job{

	static Logger logger = Logger.getLogger(OrderCreationJob.class);
	
	@Inject
	OrderService orderService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Starting the job to push orders to WareHouse");
		orderService.PushEligibleOrdersToWarehouse(1);
		logger.info("completed pusing orders to Warehouse");
	}

}
