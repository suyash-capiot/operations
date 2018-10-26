package com.coxandkings.travel.operations.service.booking.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.booking.InvoiceService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.coxandkings.travel.operations.utils.Constants.OPS_ERR_30213;

@Service
public class InvoiceServiceImpl implements InvoiceService {


    @Value("${account_overview.get_booking_invoices}")
    private String GetInvoicesForBookingURL;
    @Autowired
    private UserService userService;
    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    private static Logger logger = LogManager.getLogger(InvoiceServiceImpl.class);

    @Override
    public JSONObject getLatestInvoice(String bookingNo) throws OperationException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GetInvoicesForBookingURL + bookingNo);
        ResponseEntity<String> invoiceDetails = null;
        String latestInvoice = "";
        JSONObject invoiceJson = new JSONObject();
        try {
            invoiceDetails = RestUtils.exchange(builder.toUriString(), HttpMethod.GET, getHttpEntity(), String.class);
        } catch (Exception e) {
            logger.error("Not able to get Invoice form finance");
            throw new OperationException(OPS_ERR_30213);

        }
        String body = "";
        if (null != invoiceDetails) {
            body = invoiceDetails.getBody();
        } else {
            throw new OperationException(OPS_ERR_30213);
        }


        List<String> invoices = (List<String>) jsonObjectProvider.getChildObject(body, "$.[*].invoiceNumber", List.class);

        if (invoices.size() == 0 || null == invoices) {
            logger.error("Not able to get Invoice form finance");
        } else {
            latestInvoice = invoices.get(invoices.size() - 1);
        }
        invoiceJson.put("LatestInvoice", latestInvoice);
        return invoiceJson;
    }

    private HttpEntity getHttpEntity() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = userService.getLoggedInUserToken();
        headers.add("Authorization", token);
        return new HttpEntity(headers);
    }
}

