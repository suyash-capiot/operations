package com.coxandkings.travel.operations.consumer.factory;

import com.coxandkings.travel.operations.consumer.listners.impl.ManageProductUpdateListenerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MngPrdtUpdateListenerFactory {

    @Autowired
    private ApplicationContext context;

    public void processMngPrdtUpdate(String payload) {
        ManageProductUpdateListenerImpl mngPrdtUpdateRef = getManageProductUpdateListener(this.context);
        mngPrdtUpdateRef.processMngPrdtUpdate(payload);
    }

    public ManageProductUpdateListenerImpl getManageProductUpdateListener(ApplicationContext context) {
        return context.getBean(ManageProductUpdateListenerImpl.class);
    }

}
