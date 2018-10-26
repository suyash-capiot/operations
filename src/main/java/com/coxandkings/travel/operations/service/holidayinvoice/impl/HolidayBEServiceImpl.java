package com.coxandkings.travel.operations.service.holidayinvoice.impl;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.holidayinvoice.BeRoeResource;
import com.coxandkings.travel.operations.resource.holidayinvoice.HolidayResource;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.holidayinvoice.HolidayBEService;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class HolidayBEServiceImpl implements HolidayBEService {

    private static final Logger logger = LogManager.getLogger(HolidayBEServiceImpl.class);
    @Value("${holiday_invoice.roe_update}")
    private String beROEApi;
    @Autowired
    private MDMRestUtils mdmRestUtils;


    @Override
    public String updateRoe(HolidayResource holidayResource) throws OperationException {

        BeRoeResource beRoeResource = new BeRoeResource();
        beRoeResource.setBookID(holidayResource.getBookingNo());
        beRoeResource.setOrderID(holidayResource.getOrderId());
        beRoeResource.setRoe(holidayResource.getRoe());

        UsernamePasswordAuthenticationToken userPwdAuth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        OpsUser loggedInUser = (OpsUser) userPwdAuth.getPrincipal();
        String loggedInUserID = loggedInUser.getUserID();

        beRoeResource.setUserID(loggedInUserID);
        ResponseEntity<String> exchange = null;
        try {
            exchange = mdmRestUtils.exchange(UriComponentsBuilder.fromUriString(beROEApi).build().toUri(), HttpMethod.PUT, beRoeResource, String.class);

        } catch (Exception e) {

            logger.error("Unable to update ROE in booking engine " + holidayResource, e);
            throw new OperationException("Unable to update ROE in booking engine");
        }
        String beResponse = exchange.getBody();
        logger.info("*** ROE Updated***" + beResponse);
        return beResponse;
    }
}
