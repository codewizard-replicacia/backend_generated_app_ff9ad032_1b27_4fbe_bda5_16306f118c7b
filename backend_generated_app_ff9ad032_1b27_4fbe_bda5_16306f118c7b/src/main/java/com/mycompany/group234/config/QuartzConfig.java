package com.mycompany.group234.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import com.mycompany.group234.util.SchedularNotifyJob;

@Configuration
public class QuartzConfig {
	@Bean
	public JobDetailFactoryBean schedularJobDetail() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		factory.setJobClass(SchedularNotifyJob.class);
		factory.setDurability(true);
		return factory;
	}

	@Bean
	public Trigger schedularJobTrigger(JobDetail schedularJobDetail) {
		return TriggerBuilder.newTrigger()
				.forJob(schedularJobDetail)
				.withIdentity("schedularJobTrigger")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")) // 12 AM
				.build();
	}
}
