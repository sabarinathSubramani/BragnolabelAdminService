package org.LPIntegrator.batch;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.google.inject.Inject;

public class JobScheduler {

	static Logger logger = Logger.getLogger(JobScheduler.class);
	
	@Inject
	Scheduler scheduler;
	
	public void configureJob(){
		
		 try {
		 JobDetail job = JobBuilder.newJob(OrderCreationJob.class)
			      .withIdentity("WOC_Job", "ClearLane")
			      .build();
		 Trigger trigger = TriggerBuilder.newTrigger().withIdentity("WOC_Trigger",  "ClearLane").startNow().withSchedule(SimpleScheduleBuilder.repeatHourlyForever()).build();
		
			scheduler.scheduleJob(job, trigger);
			logger.info("successfully configured the quartz job - "+ OrderCreationJob.class.getName());
		} catch (SchedulerException e) {
			logger.error("failed to schedule job", e);
		}
	}
}
