package com.coxandkings.travel.operations.service.productsharing.impl;

import com.coxandkings.travel.ext.model.finance.invoice.InvoiceDto;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.productsharing.FinanceService;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriComponentsBuilder;

@Service("HolidayFinanceService")
public class FinanceServiceImpl implements FinanceService {
    private static final Logger logger = LogManager.getLogger(FinanceServiceImpl.class);

    @Value("${product_sharing.finance.get_invoice}")
    private String getInvoiceURL;

    @Value("${product_sharing.finance.amend_invoice}")
    private String amendInvoiceURL;

    @Value("${product_sharing.finance.cancel_invoice}")
    private String cancelInvoiceURL;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    /**
     * @param bookingNo
     * @param orderId
     * @return
     * @throws OperationException
     */
    @Override
    public InvoiceDto getInvoicesByBookingDetails(String bookingNo, String orderId) throws OperationException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getInvoiceURL)
                .queryParam("bookingRef", bookingNo)
                .queryParam("orderID", orderId);
        InvoiceDto invoice = null;
        try {
            invoice = mdmRestUtils.getForObject(builder.toUriString(), InvoiceDto.class);
        } catch (HttpServerErrorException he) {
            logger.error("Exception raised in finance API", he);
        } catch (Exception e) {
            logger.error("Exception raised in finance API ", e);

        }
        return invoice;
    }

    /**
     * @param request
     * @param invoiceNumber
     * @return
     * @throws OperationException
     */
    @Override
    public InvoiceDto amendInvoiceDetails(InvoiceDto request, String invoiceNumber) throws OperationException {
        String url = amendInvoiceURL + invoiceNumber;
        InvoiceDto invoice = null;
        try {
            mdmRestUtils.put(url, request);
        } catch (HttpServerErrorException he) {
            logger.error("Exception raised in finance API", he);
        } catch (Exception e) {
            logger.error("Exception raised in finance API ", e);
        }
        return invoice;
    }

    /**
     * @param request
     * @param oldBookingReferenceNumber
     * @return
     * @throws OperationException
     */
    @Override
    public InvoiceDto cancelInvoiceDetails(InvoiceDto request, String oldBookingReferenceNumber) throws OperationException {
        String url = cancelInvoiceURL + oldBookingReferenceNumber;
        InvoiceDto invoice = null;
        try {
            mdmRestUtils.put(url, request);

        } catch (HttpServerErrorException he) {
            logger.error("Exception raised in finance API", he);
        } catch (Exception e) {
            logger.error("Exception raised in finance API ", e);
        }
        return invoice;
    }
}
