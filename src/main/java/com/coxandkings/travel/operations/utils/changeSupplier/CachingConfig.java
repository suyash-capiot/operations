package com.coxandkings.travel.operations.utils.changeSupplier;

import com.coxandkings.travel.operations.service.changesuppliername.impl.ChangeSupplierNameServiceImplV2;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Configuration
@ComponentScan
@EnableCaching
public class CachingConfig {

    public final String cmssuppliercache = "cmssuppliercache";

    @Bean
    public CacheManager cacheManager() {
        List<ConcurrentMapCache> caches = Arrays.asList(new ConcurrentMapCache("cmssuppliercache"));
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @CacheEvict(allEntries = true, value = {cmssuppliercache})
    @Scheduled(fixedDelay = 86400000 ,  initialDelay = 500)
    public void reportCacheEvict() {
        System.out.println("Flush Cache " + new Date());
    }

//    @Bean
//    public ChangeSupplierNameServiceImplV2 getTest()
//    {
//        return new ChangeSupplierNameServiceImplV2();
//    }
}
