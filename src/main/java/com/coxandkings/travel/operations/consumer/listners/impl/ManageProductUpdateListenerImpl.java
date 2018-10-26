package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.operations.consumer.listners.ManageProductUpdateListener;
import com.coxandkings.travel.operations.service.manageproductupdates.ManageProductUpdatesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ManageProductUpdateListenerImpl implements ManageProductUpdateListener {

    @Autowired
    private ManageProductUpdatesService manageProductUpdatesService;

    private Logger logger = LogManager.getLogger(ManageProductUpdateListenerImpl.class);

    @Override
    public void processMngPrdtUpdate(String payload) {
        try {
            //alert the products
            manageProductUpdatesService.alertManageProductUpdates(payload);
        } catch (Exception e) {
            logger.debug("Error ocurred in mdm");
        }
    }
}
