package com.coxandkings.travel;

import com.coxandkings.travel.operations.config.TrackingContextPatternConverter;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.MultipartConfigElement;
import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan
@EntityScan(basePackages = {"com.coxandkings.travel"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class OperationsApplication extends SpringBootServletInitializer {

    @Autowired
    ApplicationContext applicationContext;

    static {
        System.out.println("Username:=" + System.getenv("username"));
        System.setProperty("BE_CONSUMER_GROUP_ID", UUID.randomUUID().toString());
        PluginManager.addPackage(TrackingContextPatternConverter.class.getPackage().getName());
    }

    public static void main(String[] args) {
        SpringApplication.run(OperationsApplication.class, args);
        java.util.Date date = new Date("Sat Dec 01 00:00:00 GMT 2012");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OperationsApplication.class);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("5120MB");
        factory.setMaxRequestSize("5120MB");
        return factory.createMultipartConfig();
    }
}
