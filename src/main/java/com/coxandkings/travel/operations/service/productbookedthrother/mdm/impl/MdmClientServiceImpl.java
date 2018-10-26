package com.coxandkings.travel.operations.service.productbookedthrother.mdm.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2BClient;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2CClient;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.ClientFilter;
import com.coxandkings.travel.operations.service.productbookedthrother.mdm.MdmClientService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;

import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@Service(value = "MdmClientServiceImpl")
public class MdmClientServiceImpl implements MdmClientService
{

    private static final Logger logger = LogManager.getLogger(MdmClientServiceImpl.class);

    @Value(value = "${mdm.common.client.b2b_client}")
    private String b2bClientUrl;

    @Value(value = "${mdm.common.client.b2c_client}")
    private String b2cClientUrl;

    @Autowired
    MDMRestUtils mdmRestUtils;

    @Autowired
    JsonObjectProvider jsonObjectProvider;

    @Autowired
    RestUtils restUtils;
    
    @Override
    public B2BClient getB2bClient(String clientId) throws OperationException
    {
        B2BClient b2BClient = new B2BClient();
        try {
            String url = b2bClientUrl + clientId;
            URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
            
            ResponseEntity<String> b2cClientValue = mdmRestUtils.exchange(uri, HttpMethod.GET, restUtils.getHttpEntity(), String.class);
            String value = b2cClientValue.getBody();
            b2BClient.setClientID(clientId);
            b2BClient.setClientName(jsonObjectProvider.getAttributeValue(value, "$.clientProfile.clientDetails.clientName", String.class));
//        b2BClient.setClientGroup(jsonObjectProvider.getAttributeValue(value,"$.generalInfo.clientGroupId"));
            b2BClient.setStatus(jsonObjectProvider.getAttributeValue(value, "$.status.status.status", String.class));
            b2BClient.setClientMarket(jsonObjectProvider.getAttributeValue(value, "$.clientProfile.clientDetails.clientMarket", String.class));
            b2BClient.setClientCategory(jsonObjectProvider.getAttributeValue(value, "$.clientProfile.clientDetails.clientCategory", String.class));
            b2BClient.setClientSubCategory(jsonObjectProvider.getAttributeValue(value, "$.clientProfile.clientDetails.clientSubCategory", String.class));
            b2BClient.setLanguage(jsonObjectProvider.getAttributeValue(value, "$.clientProfile.clientDetails.language", String.class));
            b2BClient.setCompanyId(jsonObjectProvider.getAttributeValue(value, "$.clientProfile.orgHierarchy.companyId", String.class));
            b2BClient.setSBU(jsonObjectProvider.getAttributeValue(value, "$.clientProfile.orgHierarchy.SBU", String.class));
            b2BClient.setBU(jsonObjectProvider.getAttributeValue(value, "$.clientProfile.orgHierarchy.BU", String.class));
            b2BClient.setEmail(Collections.singletonList(jsonObjectProvider.getAttributeValue(value, "$.adminUserDetails.users[0].email", String.class)));
            b2BClient.setFirstName(jsonObjectProvider.getAttributeValue(value, "$.clientProfile.clientDetails.clientName", String.class));
        }
        catch( Exception e )    {
            logger.error( "Error in loading B2B client info from MDM", e );
        }
        return b2BClient;
    }

    @Override
    public B2CClient getB2cClient(String clientId) throws OperationException {
        B2CClient b2CClient = new B2CClient();
        try {
            String url = b2cClientUrl + "?filter=" + makeURLForClientDetails(clientId);
            URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
            ResponseEntity<String> b2cClientValue = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
            String value = b2cClientValue.getBody();
//        b2CClient.setClientID(jsonObjectProvider.getAttributeValue(value,"$.data[0].clientDetails.clientId"));
//        b2CClient.setClientName(jsonObjectProvider.getAttributeValue(value,"$.data[0].clientDetails.clientName"));
//        b2CClient.setClientGroup();
//        b2CClient.setStatus(jsonObjectProvider.getAttributeValue(value,"$.data[0].userDetails.status"));
//        b2CClient.setClientCategory(jsonObjectProvider.getAttributeValue(value,"$.data[0].clientDetails.clientCategory"));
//        b2CClient.setEmail(Collections.singletonList(jsonObjectProvider.getAttributeValue(value, "$.data[0].travellerDetails.employee.personalEmailId")));
//        b2CClient.setFirstName(jsonObjectProvider.getAttributeValue(value,"$.data[0].travellerDetails.employee.firstName"));
        }
        catch( Exception e )    {
            logger.error( "Error in loading B2C client info from MDM", e );
        }
        return b2CClient;
    }


    private String makeURLForClientDetails(String clientId )
    {
        ClientFilter filter = new ClientFilter( );
        filter.setClientId( clientId );
        return ClientFilter.getUrl( filter );
    }
}
