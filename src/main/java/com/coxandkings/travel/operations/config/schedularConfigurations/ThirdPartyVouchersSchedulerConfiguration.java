package com.coxandkings.travel.operations.config.schedularConfigurations;

import com.coxandkings.travel.operations.schedular.ThirdPartyVoucherBatchJob;
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
public class ThirdPartyVouchersSchedulerConfiguration {


    @Autowired
    private QuartzConfig quartzConfig;
    
    @Autowired
	SpringBeanJobFactory springBeanJobFactory;
    
    @Autowired
    DataSource dataSource;
    

    @Value("${third-party-vouchers.batch-job-scheduling-interval}")
    private String thirdPartyVouchersSchedulerInterval;
    @Value("${batchJob.isEnabled}")
    private boolean isEnabled;

    @Bean(value = "thirdPartyVouchersServiceJobDetail")
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(ThirdPartyVoucherBatchJob.class);
        factoryBean.setGroup("Operations");
        factoryBean.setDurability(true);
        factoryBean.setName("thirdPartyVouchers");
        return factoryBean;
    }
    

    @Bean(value = "thirdPartyVouchersCronTrigger")
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetailFactoryBean().getObject());
        factoryBean.setStartDelay(5000);
        factoryBean.setGroup("Operations");
        factoryBean.setName("thirdPartyVouchersTrigger");
        factoryBean.setCronExpression(thirdPartyVouchersSchedulerInterval);
        return factoryBean;
    }

    @Bean(value = "thirdPartyVouchersSchedulerBean")
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
