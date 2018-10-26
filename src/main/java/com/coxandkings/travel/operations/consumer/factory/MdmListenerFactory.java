package com.coxandkings.travel.operations.consumer.factory;

import com.coxandkings.travel.operations.consumer.listners.impl.SupplierListenerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MdmListenerFactory {

    @Autowired
    private ApplicationContext context;

    public void processSupplier(String payload) {
        SupplierListenerImpl supplierListener = getSupplierListener(this.context);
        supplierListener.processSupplier(payload);
    }

    public SupplierListenerImpl getSupplierListener(ApplicationContext context) {
        return context.getBean(SupplierListenerImpl.class);
    }

}
