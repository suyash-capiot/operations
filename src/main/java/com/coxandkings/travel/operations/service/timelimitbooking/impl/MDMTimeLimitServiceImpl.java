package com.coxandkings.travel.operations.service.timelimitbooking.impl;

import com.coxandkings.travel.operations.resource.timelimitbooking.TimeLimitMDMFilter;
import com.coxandkings.travel.operations.service.timelimitbooking.MDMTimeLimitService;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class MDMTimeLimitServiceImpl implements MDMTimeLimitService {
    private static final Logger logger = LogManager.getLogger(MDMTimeLimitServiceImpl.class);

    @Value(value = "${timelimit.mdm.timelimit-config-master}")
    private String getTimeLimitMDMUrl;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    //ToDo need proper data BE clientId and MDM entityID
    //ToDo in MDM TimeLimitMaster Hotel settings are available. Flight setings is not there

    @Override
    public String getMDMInfoByClientId(String clientId) {
        logger.debug("entering into getMDMInfoByClientId method");
        String filterString="";
        TimeLimitMDMFilter mdmFilter = new TimeLimitMDMFilter();
        mdmFilter.setEntityId(clientId);

        ObjectMapper aMapper = new ObjectMapper();
        try {
            filterString =  aMapper.writeValueAsString( mdmFilter );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        URI url = UriComponentsBuilder.fromUriString(getTimeLimitMDMUrl + filterString).build().encode().toUri();
        // passing client ID to MDM, to get list of Products
        ResponseEntity<String> responseMDM = null;
        try {
            responseMDM = mdmRestUtils.exchange(url, HttpMethod.GET,null,String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseMDM.getBody();
    }

}
