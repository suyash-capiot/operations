package com.coxandkings.travel.operations.config.schedularConfigurations.managearrivallist;

import com.coxandkings.travel.operations.config.schedularConfigurations.QuartzConfig;
import com.coxandkings.travel.operations.schedular.managearrivallist.FlightArrivalListSchedular;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import java.util.Properties;

import javax.sql.DataSource;


@Configuration
@ComponentScan("com.coxandkings.travel.operations")
public class FlightArrivalListSchedulerConfiguration {

    
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

    @Bean(value = "ArrivalListFlightJobDetailFactoryBean")
    public JobDetailFactoryBean flightJobDetailFactoryBean() {
        System.out.println("Enter into FlightJobDetailFactoryBean ");
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(FlightArrivalListSchedular.class);
        factory.setDurability(true);
        factory.setGroup("OperationsGroup");
        factory.setName("ManageArrivalListFlightBatchJob");
        return factory;
    }
    
    

    @Bean(value = "ArrivalListFlightCronTriggerFactoryBean")
    public CronTriggerFactoryBean flightCronTriggerFactoryBean() {
        System.out.println("FlightCronTriggerFactoryBean");
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
        stFactory.setJobDetail(flightJobDetailFactoryBean().getObject());
        stFactory.setStartDelay(3000);
        stFactory.setName("OperationsGroup");
        stFactory.setGroup("ManageArrivalListBatchJob");
        stFactory.setCronExpression(cronExpression);
        return stFactory;
    }

    @Bean(value = "ArrivalListFlightSchedulerFactoryBean")
    public SchedulerFactoryBean flightSchedulerFactoryBean() {
        System.out.println("FlightSchedulerFactoryBean");
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
