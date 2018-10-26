package com.coxandkings.travel.operations.service.refund.impl;

import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.refund.RefundConfiguration;
import com.coxandkings.travel.operations.service.refund.RefundMDMService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Service
public class RefundMDMServiceImpl implements RefundMDMService {

    private static final Logger logger = LogManager.getLogger(RefundMDMServiceImpl.class);


    @Value("${mdm.common.client.b2b_client}")
    private String clientB2BURL;

    @Value("${mdm.common.client.b2c_client}")
    private String clientB2CUrl;
    @Value("${mdm.common.client.client-group}")
    private String clientGroup;
    @Value("${refund.refundConfiguration}")
    private String refundConfigurationUrl;

    @Value("${get_market_details}")
    private String marketURL;


    @Autowired
    private MDMRestUtils mdmRestUtil;
    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    public String getB2BClient(String clientId) throws OperationException {

        String responseJSON = null;
        try {
            responseJSON = mdmRestUtil.getResponseJSON(clientB2BURL + clientId);
        } catch (Exception e) {
            logger.error("Not able to get data may be token expired", e);
            throw new OperationException("Not able to get data may be token expired");
        }

        return responseJSON;
    }

    @Override
    public String getB2CClient(String clientId) throws OperationException {
        String b2cUrl = clientB2CUrl.concat("?filter={ \"_id\":\"").concat(clientId).concat("\"}");
        URI url = UriComponentsBuilder.fromUriString(b2cUrl).build().encode().toUri();
        String responseJson = null;
        try {
            responseJson = mdmRestUtil.exchange(url, HttpMethod.GET, String.class);
        } catch (Exception e) {
            logger.error("Not able to get data from MDM ", e);
            throw new OperationException("Not able to get data from MDM");
        }
        if (responseJson != null) {
            responseJson = jsonObjectProvider.getChildJSON(responseJson, "$.data[0]");
        } else {
            throw new OperationException("No client data found for client id " + clientId);
        }

        return responseJson;
    }

    @Override
    public String getB2BClientEmail(String clientID) throws OperationException {
        return jsonObjectProvider.getAttributeValue(getB2BClient(clientID), "$.adminUserDetails.users[0].email", String.class);
    }

    @Override
    public String getB2CClientEmail(String clientID) throws OperationException {

        return jsonObjectProvider.getAttributeValue(getB2CClient(clientID), "  $.data[:1].travellerDetails.employmentDetails.officeEmailId", String.class);
    }

    public String[] getClientGroup(String groupId) throws OperationException {
        String groupDetails = mdmRestUtil.exchange(clientGroup.concat("/").concat(groupId), HttpMethod.GET, String.class);
        String clientGroupName = jsonObjectProvider.getAttributeValue(groupDetails, "$.clientGroupName", String.class);
        String companyMarket = jsonObjectProvider.getAttributeValue(groupDetails, "$.companyMarket", String.class);

        return new String[]{clientGroupName, companyMarket};
    }

    @Override
    public RefundTypes getRefundConfiguration(RefundConfiguration refundConfiguration) throws OperationException {
        ResponseEntity<RefundTypes> refundTypesResponseEntity;
        try {
            refundTypesResponseEntity = mdmRestUtil.exchange(UriComponentsBuilder.fromUriString(refundConfigurationUrl).build().toUri(), HttpMethod.POST, refundConfiguration, RefundTypes.class);
        } catch (Exception e) {
            logger.error("Unable to connect refund configuration master", e);
            throw new OperationException(Constants.UnableToConnectRefundConfiguration);
        }
        return refundTypesResponseEntity.getBody();
    }

    public String getMarketName(String id) throws OperationException {
        String result = null;
        String filter = "?filter={\"_id\":\"" + id + "\"}";
        URI uri = UriComponentsBuilder.fromUriString(marketURL + filter).build().toUri();
        String marketResponse = null;
        try {
            marketResponse = mdmRestUtil.exchange(uri, HttpMethod.GET, String.class);
        } catch (Exception e) {
            logger.error("Unable to get market details id:" + id);

        }
        if (null != marketResponse) {
            result = jsonObjectProvider.getAttributeValue(marketResponse, "$.data[:1].name", String.class);
        }
        return result;


    }
}
