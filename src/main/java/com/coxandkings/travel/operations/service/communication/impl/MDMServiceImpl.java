package com.coxandkings.travel.operations.service.communication.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.communication.MDMService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class MDMServiceImpl implements MDMService {


    private static final Logger logger = LogManager.getLogger(MDMServiceImpl.class);

    @Value("${mdm.common.supplier.supplier-by-id}")
    private String supplierURL;
    @Value("${email.mdm.b2b-client}")
    private String b2bURL;
    @Value("${email.mdm.b2e-user}")
    private String b2eURL;
    @Value("${email.mdm.b2c-client}")
    private String b2cURL;


    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Override
    public List<String> searchSupplierEmail(String criteria) throws OperationException {
        String supplierURLWithFilter = supplierURL + "?filter={\"contactInfo.contactDetails.email\":\"/" + criteria + "/\"}&select=contactInfo.contactDetails.email";
        URI uri = UriComponentsBuilder.fromUriString(supplierURLWithFilter).build().encode().toUri();

        String supplierResponse;
        try {
            supplierResponse = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
        } catch (Exception e) {
            logger.error("Unable to get data from MDM ", e);
            throw new OperationException("Unable to get data from MDM " + e.getMessage());
        }
        if (null != supplierResponse) {
            return (List<String>) jsonObjectProvider.getChildObject(supplierResponse, "$.data[*].contactInfo.contactDetails.email", List.class);
        } else {
            throw new OperationException("No record found for given criteria");
        }

    }

    //$.data[:1].adminUserDetails.users[0].email
    @Override
    public List<String> searchB2BEmail(String criteria) throws OperationException {
        //String supplierURLWithFilter = supplierURL + "?filter={\"contactInfo.contactDetails.email\":\"/" + criteria + "/\"}&select=contactInfo.contactDetails.email";
        String supplierURLWithFilter = b2bURL+"?filter={\"adminUserDetails.users.email\":{\"$regex\":\"(?i).*" + criteria + ".*\"}}";
        URI uri = UriComponentsBuilder.fromUriString(supplierURLWithFilter).build().encode().toUri();

        String supplierResponse;
        try {
            supplierResponse = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
        } catch (Exception e) {
            logger.error("Unable to get data from MDM ", e);
            throw new OperationException("Unable to get data from MDM " + e.getMessage());
        }
        if (null != supplierResponse) {
            return (List<String>) jsonObjectProvider.getChildObject(supplierResponse, "$.data[*].adminUserDetails.users[*].email", List.class);
        } else {
            throw new OperationException("No record found for given criteria");
        }

    }

    //http://10.24.2.5:10010/usermgmt/v1/user?filter={userDetails.email":"/a/"}
    @Override
    public List<String> searchB2EEmail(String criteria) throws OperationException {
        String supplierURLWithFilter = b2eURL + "?filter={userDetails.email\":\"/" + criteria + "/\"}";
        URI uri = UriComponentsBuilder.fromUriString(supplierURLWithFilter).build().encode().toUri();

        String clientResponse;
        try {
            clientResponse = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
        } catch (Exception e) {
            logger.error("Unable to get data from MDM ", e);
            throw new OperationException("Unable to get data from MDM " + e.getMessage());
        }
        if (null != clientResponse) {
            return (List<String>) jsonObjectProvider.getChildObject(clientResponse, "$.data[*].userDetails.email", List.class);
        } else {
            throw new OperationException("No record found for given criteria");
        }

    }

    @Override
    public List<String> searchB2CEmail(String criteria) throws OperationException {

        String supplierURLWithFilter = b2cURL + "?filter={\"travellerDetails.employmentDetails.officeEmailId\":\"/" + criteria + "/\"}";
        URI uri = UriComponentsBuilder.fromUriString(supplierURLWithFilter).build().encode().toUri();

        String clientResponse;
        try {
            clientResponse = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
        } catch (Exception e) {
            logger.error("Unable to get data from MDM ", e);
            throw new OperationException("Unable to get data from MDM " + e.getMessage());
        }
        if (null != clientResponse) {
            return (List<String>) jsonObjectProvider.getChildObject(clientResponse, "$.data[*].travellerDetails.employmentDetails.officeEmailId", List.class);
        } else {
            throw new OperationException("No record found for given criteria");
        }

    }
}

