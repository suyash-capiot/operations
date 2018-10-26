package com.coxandkings.travel.operations.config.schedularConfigurations;

import com.coxandkings.travel.operations.schedular.PaymentAdviceSchedular;
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
import org.springframework.stereotype.Component;

import java.util.Properties;

import javax.sql.DataSource;

@Configuration
@Component
@ComponentScan("com.coxandkings.travel.operations")
public class PaymentAdviceSchedulerConfiguration {

    private static final Logger logger = LogManager.getLogger(PaymentAdviceSchedulerConfiguration.class);
    
    @Autowired
	SpringBeanJobFactory springBeanJobFactory;
    @Autowired
    private QuartzConfig quartzConfig;
    @Autowired
    DataSource dataSource;


    @Value("${pre-payment-to-supplier.batch-job-scheduling-interval}")
    private String cronExpression;
    @Value("${batchJob.isEnabled}")
    private boolean isEnabled;

    @Bean(value = "AutomaticPaymentAdviceGenerationJobDetailFactoryBean")
    public JobDetailFactoryBean jobDetailFactoryBean() {
        logger.info("Enter into JobDetailFactoryBean ");
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(PaymentAdviceSchedular.class);
        factory.setDurability(true);
        factory.setGroup("OperationsGroup");
        factory.setName("PaymentAdvice");
        return factory;
    }

        
    @Bean(value = "AutomaticPaymentAdviceGenerationCronTriggerFactoryBean")
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        logger.info("CronTriggerFactoryBean");
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
        stFactory.setJobDetail(jobDetailFactoryBean().getObject());
        stFactory.setStartDelay(3000);
        stFactory.setName("OperationsGroup");
        stFactory.setGroup("PaymentAdvice");
        stFactory.setCronExpression(cronExpression);
        return stFactory;
    }

    @Bean(value = "AutomaticPaymentAdviceGenerationSchedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean() {
        logger.info("SchedulerFactoryBean");
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
