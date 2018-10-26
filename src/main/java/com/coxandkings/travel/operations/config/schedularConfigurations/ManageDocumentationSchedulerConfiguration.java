package com.coxandkings.travel.operations.config.schedularConfigurations;

import com.coxandkings.travel.operations.schedular.ManageDocumentationScheduler;
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
public class ManageDocumentationSchedulerConfiguration {
     
	@Autowired
	SpringBeanJobFactory springBeanJobFactory;
	
    @Autowired
    private QuartzConfig quartzConfig;
    
    @Autowired
    DataSource dataSource;


    @Value("${manageDocumentation.batch-job-scheduling-interval}")
    private String manageDocumentationSchedulerCronExpression;
    @Value("${batchJob.isEnabled}")
    private boolean isEnabled;

    @Bean(value = "ManageDocumentationJobDetailFactoryBean")
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(ManageDocumentationScheduler.class);
        factory.setDurability(true);
        factory.setGroup("OperationsGroup");
        factory.setName("ManageDocumentationBatchJob");
        return factory;
    }
    

    @Bean(value = "ManageDocumentationCronTriggerFactoryBean")
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
        stFactory.setJobDetail(jobDetailFactoryBean().getObject());
        stFactory.setStartDelay(3000);
        stFactory.setName("OperationsGroup");
        stFactory.setGroup("ManageDocumentationBatchJob");
        stFactory.setCronExpression(manageDocumentationSchedulerCronExpression);
        return stFactory;
    }

    @Bean(value = "ManageDocumentationSchedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean() {
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
