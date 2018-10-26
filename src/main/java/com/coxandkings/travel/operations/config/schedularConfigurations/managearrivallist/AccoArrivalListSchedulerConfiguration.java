package com.coxandkings.travel.operations.config.schedularConfigurations.managearrivallist;


import com.coxandkings.travel.operations.config.schedularConfigurations.QuartzConfig;
import com.coxandkings.travel.operations.schedular.managearrivallist.AccoArrivalListSchedular;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;
import java.util.Properties;

import javax.sql.DataSource;

@Configuration
@Component
@ComponentScan("com.coxandkings.travel.operations")
public class AccoArrivalListSchedulerConfiguration {

	@Autowired
	SpringBeanJobFactory springBeanJobFactory;
	
    @Autowired
    private QuartzConfig quartzConfig;
    
    @Autowired
    DataSource dataSource;

    @Value("${manage-arrival-list.batch-job-scheduling-interval}")
    private String cronExpression;

    @Value("${batchJob.isEnabled}")
    private boolean isEnabled;

    @Bean(value = "ArrivalListAccoJobDetailFactoryBean")
    public JobDetailFactoryBean flightJobDetailFactoryBean() {
        System.out.println("Enter into AccoJobDetailFactoryBean ");
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(AccoArrivalListSchedular.class);
        factory.setDurability(true);
        factory.setGroup("OperationsGroup");
        factory.setName("ManageArrivalListAccoBatchJob");
        return factory;
    }
    
    

    @Bean(value = "ArrivalListAccoCronTriggerFactoryBean")
    public CronTriggerFactoryBean flightCronTriggerFactoryBean() {
        System.out.println("AccoCronTriggerFactoryBean");
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
        stFactory.setJobDetail(flightJobDetailFactoryBean().getObject());
        stFactory.setStartDelay(3000);
        stFactory.setName("OperationsGroup");
        stFactory.setGroup("ManageArrivalListBatchJob");
        stFactory.setCronExpression(cronExpression);
        return stFactory;
    }

    @Bean(value = "ArrivalListAccoSchedulerFactoryBean")
    public SchedulerFactoryBean flightSchedulerFactoryBean() {
        System.out.println("AccoSchedulerFactoryBean");
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setAutoStartup(isEnabled);
        scheduler.setTriggers(flightCronTriggerFactoryBean().getObject());
        
        scheduler.setJobFactory(springBeanJobFactory);
        scheduler.setDataSource(dataSource);
        
        Properties props=new Properties();
        props.setProperty("org.quartz.jobStore.class",quartzConfig.getJobStoreClass());
        props.setProperty("org.quartz.jobStore.driverDelegateClass",quartzConfig.getJobStoreDriverDelegateClass());
        props.setProperty("org.quartz.threadPool.class",quartzConfig.getThreadClass());
        props.setProperty("org.quartz.threadPool.threadCount",quartzConfig.getThreadCount());
        
        scheduler.setQuartzProperties(props);
        return scheduler;
    }

}

