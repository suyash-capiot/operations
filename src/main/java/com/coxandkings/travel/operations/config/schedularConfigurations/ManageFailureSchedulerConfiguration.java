package com.coxandkings.travel.operations.config.schedularConfigurations;


import com.coxandkings.travel.operations.schedular.ManageFailureScheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class ManageFailureSchedulerConfiguration {

    private static final Logger logger = LogManager.getLogger(ManageFailureSchedulerConfiguration.class);
    
    
    @Autowired
    private QuartzConfig quartzConfig;
    
    @Autowired
	SpringBeanJobFactory springBeanJobFactory;
    
    @Autowired
    DataSource dataSource;

    @Value("${failures.batch-job-scheduling-interval}")
    private String manageFailureCronExpression;
    @Value("${batchJob.isEnabled}")
    private boolean isEnabled;

    @Bean(value = "ManageFailureJobDetailFactoryBean")
    public JobDetailFactoryBean jobDetailFactoryBean() {
        logger.info("In Manage Failure JobDetailFactoryBean");
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(ManageFailureScheduler.class);
        factory.setDurability(true);
        factory.setGroup("ManageFailureSchedulerGroup");
        factory.setName("failureSchedulerJob");
        return factory;
    }
    
    

    @Bean(value = "ManageFailureCronTriggerFactoryBean")
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        logger.info("In Manage Failure CronTriggerFactoryBean");
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetailFactoryBean().getObject());
        cronTriggerFactoryBean.setStartDelay(3000);
        cronTriggerFactoryBean.setName("ManageFailureTrigger");
        cronTriggerFactoryBean.setGroup("OperationGroup");
        cronTriggerFactoryBean.setCronExpression(manageFailureCronExpression);
        return cronTriggerFactoryBean;
    }

    @Bean(value = "ManageFailureSchedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean() {
        logger.info("In Manage Failure Scheduler Factory Bean");
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
