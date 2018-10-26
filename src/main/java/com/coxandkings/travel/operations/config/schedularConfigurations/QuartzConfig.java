package com.coxandkings.travel.operations.config.schedularConfigurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "org.quartz")
public class QuartzConfig {

	private String threadCount;
    private String threadClass;
	private String jobStoreClass;
	private String jobStoreDriverDelegateClass;      
	
	
	public String getThreadCount() {
		return threadCount;
	}
	public void setThreadCount(String threadCount) {
		this.threadCount = threadCount;
	}
	public String getThreadClass() {
		return threadClass;
	}
	public void setThreadClass(String threadClass) {
		this.threadClass = threadClass;
	}
	public String getJobStoreClass() {
		return jobStoreClass;
	}
	public void setJobStoreClass(String jobStoreClass) {
		this.jobStoreClass = jobStoreClass;
	}
	public String getJobStoreDriverDelegateClass() {
		return jobStoreDriverDelegateClass;
	}
	public void setJobStoreDriverDelegateClass(String jobStoreDriverDelegateClass) {
		this.jobStoreDriverDelegateClass = jobStoreDriverDelegateClass;
	}
	
}
