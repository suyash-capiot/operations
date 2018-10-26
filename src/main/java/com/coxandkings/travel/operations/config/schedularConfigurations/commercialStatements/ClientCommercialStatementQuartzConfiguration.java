package com.coxandkings.travel.operations.config.schedularConfigurations.commercialStatements;

import com.coxandkings.travel.operations.config.schedularConfigurations.QuartzConfig;
import com.coxandkings.travel.operations.schedular.commercialStatements.ClientCommercialStatementBatchJob;
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
public class ClientCommercialStatementQuartzConfiguration {

	@Autowired
	SpringBeanJobFactory springBeanJobFactory;
	
    @Autowired
    private QuartzConfig quartzConfig;
    @Value("${commercial-statements.batch-job-interval}")
    private String commercialStatementSchedulerInterval;
    @Value("${batchJob.isEnabled}")
    private boolean isEnabled;
    
    @Autowired
    private DataSource dataSource;


    @Bean(value = "clientCommercialStatementServiceJobDetail")
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(ClientCommercialStatementBatchJob.class);
        factoryBean.setDurability(true);
        factoryBean.setGroup("Operations");
        factoryBean.setName("commercialStatements");
        return factoryBean;
    }
    

    @Bean(value = "clientCommercialStatementsCronTrigger")
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetailFactoryBean().getObject());
        factoryBean.setStartDelay(5000);
        factoryBean.setGroup("Operations");
        factoryBean.setName("commercialStatements");
        factoryBean.setCronExpression(commercialStatementSchedulerInterval);
        return factoryBean;
    }

    @Bean(value = "clientCommercialStatementSchedulerBean")
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
