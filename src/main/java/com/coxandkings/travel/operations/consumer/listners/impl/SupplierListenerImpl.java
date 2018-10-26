package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.operations.consumer.listners.SupplierListener;
import com.coxandkings.travel.operations.resource.supplierbillpassing.ApolloSupplierResource;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.jayway.jsonpath.JsonPath;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SupplierListenerImpl implements SupplierListener {

    @Value("${apollo.createSupplier}")
    private String createSupplier;

    private Logger logger = LogManager.getLogger(SupplierListenerImpl.class);

    @Override
    public void processSupplier(String payload) {
        try {
            String supplierId = JsonPath.parse(payload).read("$.data._id");
            String supplierName = JsonPath.parse(payload).read("$.data.supplier.name");
            System.out.println(" kafka entered in supplier listener" + supplierId + supplierName);
            ApolloSupplierResource apolloSupplierResource = new ApolloSupplierResource();
            apolloSupplierResource.setAdapterId("2");
            apolloSupplierResource.setCategoryId("1");
            apolloSupplierResource.setClientSupplierId(supplierId);
            apolloSupplierResource.setOrganizationId("1");
            apolloSupplierResource.setUserId("1");
            apolloSupplierResource.setSupplierName(supplierName);
            HttpEntity httpEntity = new HttpEntity(apolloSupplierResource);
            ResponseEntity<String> response = RestUtils.getTemplate().exchange(createSupplier, HttpMethod.POST, httpEntity, String.class);
            logger.info(response);
        } catch (Exception e) {
            logger.debug("Error ocurred in mdm supplier listener");
        }
    }
}
