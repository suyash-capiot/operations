package com.coxandkings.travel.operations.config.schedularConfigurations.managearrivallist;


import com.coxandkings.travel.operations.config.schedularConfigurations.QuartzConfig;
import com.coxandkings.travel.operations.schedular.managearrivallist.GeneralArrivalListSchedular;
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
public class ArrivalListSchedulerConfiguration {

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

    @Bean(value = "ArrivalListGeneralJobDetailFactoryBean")
    public JobDetailFactoryBean jobDetailFactoryBean() {
        System.out.println("Enter into JobDetailFactoryBean ");
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(GeneralArrivalListSchedular.class);
        factory.setDurability(true);
        factory.setGroup("OperationsGroup");
        factory.setName("ManageArrivalListBatchJob");
        return factory;
    }
    
   

    @Bean(value = "ArrivalListGeneralCronTriggerFactoryBean")
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        System.out.println("CronTriggerFactoryBean");
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
        stFactory.setJobDetail(jobDetailFactoryBean().getObject());
        stFactory.setStartDelay(3000);
        stFactory.setName("OperationsGroup");
        stFactory.setGroup("ManageArrivalListBatchJob");
        stFactory.setCronExpression(cronExpression);
        return stFactory;
    }

    @Bean(value = "ArrivalListGeneralSchedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean() {
        System.out.println("SchedulerFactoryBean");
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setAutoStartup(isEnabled);
        scheduler.setTriggers(cronTriggerFactoryBean().getObject());
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

