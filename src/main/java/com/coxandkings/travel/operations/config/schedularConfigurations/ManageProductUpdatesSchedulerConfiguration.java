package com.coxandkings.travel.operations.config.schedularConfigurations;

import com.coxandkings.travel.operations.schedular.ManageProductUpdatesScheduler;
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
public class ManageProductUpdatesSchedulerConfiguration {

	@Autowired
	SpringBeanJobFactory springBeanJobFactory;
	
    @Autowired
    private QuartzConfig quartzConfig;
    
    @Autowired
    private DataSource dataSource;
    


    @Value("${updatesToProductMaster.batch-job-scheduling-interval}")
    private String manageProductUpdatesSchedulerCronExpression;
    @Value("${batchJob.isEnabled}")
    private boolean isEnabled;

    @Bean(value = "ManageProductUpdatesJobDetailFactoryBean")
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(ManageProductUpdatesScheduler.class);
        factory.setDurability(true);
        factory.setGroup("ProductUpatesBatchJobGroup");
        factory.setName("ProductUpatesBatchJob");
        return factory;
    }
    

    @Bean(value = "ManageProductUpdatesCronTriggerFactoryBean")
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
        stFactory.setJobDetail(jobDetailFactoryBean().getObject());
        stFactory.setStartDelay(3000);
        stFactory.setName("ProductUpdatesTrigger");
        stFactory.setGroup("OperationsGroup");
        stFactory.setCronExpression(manageProductUpdatesSchedulerCronExpression);
        return stFactory;
    }


    @Bean(value = "ManageProductUpdatesSchedulerFactoryBean")
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
