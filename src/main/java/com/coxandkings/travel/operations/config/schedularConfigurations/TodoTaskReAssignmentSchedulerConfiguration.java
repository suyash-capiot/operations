package com.coxandkings.travel.operations.config.schedularConfigurations;

import com.coxandkings.travel.operations.schedular.ToDoTaskReassignmentScheduler;
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
public class TodoTaskReAssignmentSchedulerConfiguration {

    @Autowired
    private QuartzConfig quartzConfig;
    
    @Autowired
	SpringBeanJobFactory springBeanJobFactory;
    
    @Autowired
    DataSource dataSource;

    @Value("${todotask-reassignment.batch-job-scheduling-interval}")
    private String todoTaskReassignmentSchedulerCronExpression;
    @Value("${batchJob.isEnabled}")
    private boolean isEnabled;

    @Bean(value = "TodoTaskReassignmentJobDetailFactoryBean")
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(ToDoTaskReassignmentScheduler.class);
        factory.setDurability(true);
        factory.setGroup("OperationsGroup");
        factory.setName("TodoTaskReassignmentBatchJob");
        return factory;
    }
    
  
    @Bean(value = "TodoTaskReassignmentCronTriggerFactoryBean")
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
        stFactory.setJobDetail(jobDetailFactoryBean().getObject());
        stFactory.setStartDelay(3000);
        stFactory.setName("OperationsGroup");
        stFactory.setGroup("TodoTaskReassignmentBatchJob");
        stFactory.setCronExpression(todoTaskReassignmentSchedulerCronExpression);
        return stFactory;
    }

    @Bean(value = "TodoTaskReassignmentSchedulerFactoryBean")
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
