package com.coxandkings.travel.operations.config.schedularConfigurations;


import com.coxandkings.travel.operations.schedular.InboundEmailsScheduler;
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
public class InboundEmailsSchedulerConfiguration {

    /*@Autowired
    EmailService emailService;*/
	@Autowired
	SpringBeanJobFactory springBeanJobFactory;
    @Autowired
    private QuartzConfig quartzConfig;
    @Autowired
    DataSource dataSource;
    @Value("${email.inbound.scheduler-config}")
    private String inboundEmailSchedulerCronExpression;
    @Value("${batchJob.isEnabled}")
    private boolean isEnabled;

    @Bean(value = "InboundEmailsJobDetailFactoryBean")
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(InboundEmailsScheduler.class);
        
       /* Map<String, Object> jobDataMap = new HashMap<String, Object>();
        jobDataMap.put("InboundEMailsBatchJobService", emailService);
        factory.setJobDataAsMap(jobDataMap);*/
        factory.setDurability(true);
        factory.setGroup("OperationsGroup");
        factory.setName("InboundEmailsBatchJob");
        return factory;
    }
    
   /* @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }*/

    @Bean(value = "InboundEmailsCronTriggerFactoryBean")
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
        stFactory.setJobDetail(jobDetailFactoryBean().getObject());
        stFactory.setStartDelay(60000);
        stFactory.setName("InboundEmailsTrigger");
        stFactory.setGroup("OperationsGroup");
        stFactory.setCronExpression(inboundEmailSchedulerCronExpression);
        return stFactory;
    }

    @Bean(value = "InboundEmailsSchedulerFactoryBean")
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
