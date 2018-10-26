package com.coxandkings.travel.operations.service.holidayinvoice.impl;

import com.coxandkings.travel.ext.model.finance.invoice.InvoiceDto;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.holidayinvoice.CheckHolidayInvoiceResource;
import com.coxandkings.travel.operations.resource.holidayinvoice.HolidayInvoiceResource;
import com.coxandkings.travel.operations.resource.holidayinvoice.HolidayResource;
import com.coxandkings.travel.operations.service.holidayinvoice.HolidayFinanceService;
import com.coxandkings.travel.operations.service.refund.RefundFinanceService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.coxandkings.travel.operations.utils.Constants.OPS_ERR_41502;

@Service
public class HolidayFinanceServiceImpl implements HolidayFinanceService {
    private static final Logger logger = LogManager.getLogger(HolidayFinanceServiceImpl.class);

    @Value("${holiday_invoice.finance.generate_holiday_invoice}")
    private String generateHolidayInvoice;

    @Value("${holiday_invoice.finance.checkHolidayInvoice}")
    private String checkHolidayInvoice;

    @Autowired
    private RefundFinanceService refundFinanceService;


    @Autowired
    private MDMRestUtils mdmRestUtils;

    /**
     * Call this method before updating ROE in BE
     **/
    public Boolean checkHolidayInvoiceStatus(String invoiceNumber) throws OperationException {
        CheckHolidayInvoiceResource checkHolidayInvoiceResource = null;
        try {
            checkHolidayInvoiceResource = mdmRestUtils.exchange(UriComponentsBuilder.fromUriString(checkHolidayInvoice).queryParam("invoiceNumber",invoiceNumber).build().toUri(), HttpMethod.GET, CheckHolidayInvoiceResource.class);
        } catch (Exception e) {
            logger.error(OPS_ERR_41502, e);
            throw new OperationException(Constants.OPS_ERR_41500, invoiceNumber);
        }
        if ("AMS-200".equalsIgnoreCase(checkHolidayInvoiceResource.getCode())) {
            return true;
        } else {
            logger.error(checkHolidayInvoiceResource.getMessage());
            throw new OperationException(checkHolidayInvoiceResource.getMessage());

        }
    }

    public String generateHolidayInvoice(HolidayResource holidayResource) throws OperationException {


        JSONObject jsonObject = new JSONObject();
        String invoiceNumber = refundFinanceService.getInvoicesByBookingDetails(holidayResource.getBookingNo(), holidayResource.getOrderId()).getInvoiceNumber();

        HolidayInvoiceResource holidayInvoiceResource = new HolidayInvoiceResource(invoiceNumber);

        jsonObject.put("invoiceNumber", holidayResource.getInvoiceNo());
        URI uri = UriComponentsBuilder.fromUriString(generateHolidayInvoice).build().toUri();

        ResponseEntity<InvoiceDto> exchange = null;
        try {
            exchange = mdmRestUtils.exchange(uri, HttpMethod.POST, holidayInvoiceResource, InvoiceDto.class, MediaType.APPLICATION_JSON);
        } catch (HttpClientErrorException he) {
            logger.error(Constants.OPS_ERR_41501 + " " + invoiceNumber);
            throw new OperationException(Constants.OPS_ERR_41501);

        } catch (Exception e) {

            logger.error("Finance is not able to generate Holiday Invoice " + holidayResource, e);

        }
        InvoiceDto invoice = null;
        if (null != exchange) {
            invoice = exchange.getBody();
        } else {
            logger.error("Unable to generate holiday Invoice");
            throw new OperationException(Constants.OPS_ERR_41500, invoiceNumber);
        }

        return "Holiday Invoice is generated";
    }

}
