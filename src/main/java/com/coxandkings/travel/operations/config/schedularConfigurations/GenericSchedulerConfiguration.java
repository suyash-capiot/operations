package com.coxandkings.travel.operations.config.schedularConfigurations;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
@ComponentScan("com.coxandkings.travel.operations")
public class GenericSchedulerConfiguration {

	@Autowired
    private ApplicationContext applicationContext;
	
    @Bean("GenericScheduler")
    public Scheduler schedulerFactoryBean() throws SchedulerException {
        StdSchedulerFactory factory = new StdSchedulerFactory();
        factory.initialize();

        Scheduler scheduler = factory.getScheduler();
        scheduler.start();

        return scheduler;
    }
    
    @Bean
    @Primary
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
}
