package com.coxandkings.travel.operations.config.schedularConfigurations;


import com.coxandkings.travel.operations.schedular.MailRoomBatchJob;
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
public class MailRoomSchedulerConfiguration {
	@Autowired
	SpringBeanJobFactory springBeanJobFactory;
    
    @Autowired
    private QuartzConfig quartzConfig;
    
    @Autowired
    DataSource dataSource;


    @Value("${manage-mailroom.batch-job-scheduling-interval}")
    private String mailRoomSchedulerInterval;

    @Value("${batchJob.isEnabled}")
    private boolean isEnabled;

    @Bean(value = "mailRoomJobDetail")
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(MailRoomBatchJob.class);
        factoryBean.setDurability(true);
        factoryBean.setGroup("Operations");
        factoryBean.setName("MailRoomBatch");
        factoryBean.setDurability(true);
        return factoryBean;
    }
    
    

    @Bean(value = "mailRoomCronTrigger")
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetailFactoryBean().getObject());
        factoryBean.setStartDelay(5000);
        factoryBean.setGroup("Operations");
        factoryBean.setName("MailRoomTrigger");
        factoryBean.setCronExpression(mailRoomSchedulerInterval);
        return factoryBean;
    }

    @Bean(value = "mailRoomSchedulerBean")
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
