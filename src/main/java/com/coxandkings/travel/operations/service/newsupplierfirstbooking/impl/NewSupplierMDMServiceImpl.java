package com.coxandkings.travel.operations.service.newsupplierfirstbooking.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.newsupplierfirstbooking.NewSupplierMDMService;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class NewSupplierMDMServiceImpl implements NewSupplierMDMService {
    private static final Logger logger = LogManager.getLogger(NewSupplierMDMServiceImpl.class);

    @Value(value = "${new-supplier-first-booking.mdm.supplierDetails}")
    private String urlToGetSupplierDetails;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Override
    public String getSupplierDetails(String supplierID) {
        String res=null;
        String URL=this.createURL(supplierID);
        try {
            res=mdmRestUtils.exchange(URL, HttpMethod.GET,String.class);
        } catch (OperationException e) {
            e.printStackTrace();
            logger.info("Error occurred while retrieving Supplier Details from MDM module"+e);
        }
        return res;
    }
    public String createURL(String supplierID)
    {
        return urlToGetSupplierDetails+supplierID;
    }
}
