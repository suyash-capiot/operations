package com.coxandkings.travel.operations.config.schedularConfigurations;

import com.coxandkings.travel.operations.schedular.ProductReviewScheduler;
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
@ComponentScan("com.coxandkings.operations")
public class ManageProductReviewConfiguration {

    
    
    @Autowired
    private QuartzConfig quartzConfig;
    
    @Autowired
	SpringBeanJobFactory springBeanJobFactory;
    
    @Autowired
    DataSource dataSource;


    @Value("${review.batch-job-scheduling-interval}")
    private String reviewScheduleInterval;
    @Value("${batchJob.isEnabled}")
    private boolean isEnabled;

    @Bean(value = "ProductReviewFactoryBean")
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(ProductReviewScheduler.class);
        factoryBean.setDurability(true);
        factoryBean.setGroup("OperationsGroup");
        factoryBean.setName("ProductReviewServiceBatchJob");
        return factoryBean;
    }
    
   

    @Bean(value = "ProductReviewSchedulerCronTriggerFactoryBean")
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
        triggerFactoryBean.setJobDetail(jobDetailFactoryBean().getObject());
        triggerFactoryBean.setStartDelay(3000);
        triggerFactoryBean.setName("ProductReviewSchedulerTrigger");
        triggerFactoryBean.setCronExpression(reviewScheduleInterval);
        return triggerFactoryBean;
    }

    @Bean(value = "ProductReviewSchedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setTriggers(cronTriggerFactoryBean().getObject());
        schedulerFactoryBean.setAutoStartup(isEnabled);
        schedulerFactoryBean.setJobFactory(springBeanJobFactory);
        schedulerFactoryBean.setDataSource(dataSource);
        Properties props=new Properties();
        props.setProperty("org.quartz.jobStore.class",quartzConfig.getJobStoreClass());
        props.setProperty("org.quartz.jobStore.driverDelegateClass",quartzConfig.getJobStoreDriverDelegateClass());
        props.setProperty("org.quartz.threadPool.class",quartzConfig.getThreadClass());
        props.setProperty("org.quartz.threadPool.threadCount",quartzConfig.getThreadCount());
        
        schedulerFactoryBean.setQuartzProperties(props);
        return schedulerFactoryBean;
    }
}
