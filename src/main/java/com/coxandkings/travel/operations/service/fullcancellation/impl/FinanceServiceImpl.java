package com.coxandkings.travel.operations.service.fullcancellation.impl;


import com.coxandkings.travel.ext.model.finance.cancelinvoice.InvoiceDto;
import com.coxandkings.travel.ext.model.finance.cancelinvoice.InvoiceParticularsDto;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.resource.fullcancellation.OrderCancellation;
import com.coxandkings.travel.operations.service.fullcancellation.FinanceService;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Service
public class FinanceServiceImpl implements FinanceService {

    private static final Logger logger = LogManager.getLogger(FinanceServiceImpl.class);

    @Value("${full_cancellation.get_invoice}")
    private String invoiceCheckAPIUrl;

    @Value("${full_cancellation.update_invoice_only_for_cancellation}")
    private String invoiceUpdateAPIUrl;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Override
    public void updateInvoice(OrderCancellation cancellations, KafkaBookingMessage kafkaBookingMessage) throws OperationException {
        logger.info("*** Full Cancellation: started canceling invoice ***");
        InvoiceDto invoicesByBookingDetails = getInvoicesByBookingDetails(kafkaBookingMessage);
        Set<InvoiceParticularsDto> invoiceParticularsDto = invoicesByBookingDetails.getInvoiceParticularsDto();
     /*   Double cancellationCharges = cancellations.getCompanyCancelCharges() + cancellations.getSupplierCancelCharges();
        int index = 0;

        for (InvoiceParticularsDto invoiceParticulars : invoiceParticularsDto) {
            if (invoiceParticulars.getOrderID().equalsIgnoreCase(kafkaBookingMessage.getOrderNo())) {

                invoiceParticulars.setSellingPrice((int) (invoiceParticulars.getSellingPrice() - (cancellationCharges)));
                invoiceParticularsDto.set(index, invoiceParticulars);
                break;
            }
            index++;
        }*/
        //Todo:need to update tax details by calling tax engine


        for (InvoiceParticularsDto invoiceParticularsDto1 : invoiceParticularsDto) {
            if (invoiceParticularsDto1.getOrderID().equals(kafkaBookingMessage.getOrderNo())) {
                invoiceParticularsDto1.setAmendmentCharges(100d);
                break;
            }
        }


        try {
            mdmRestUtils.put(invoiceUpdateAPIUrl.concat("=").concat(kafkaBookingMessage.getBookId()), invoicesByBookingDetails);
        } catch (Exception e) {
            logger.error("Unable to update invoice for cancellation order No:" + kafkaBookingMessage.getOrderNo(), e);
            throw new OperationException("Unable to update invoice for cancellation order No:" + kafkaBookingMessage.getOrderNo());
        }
        logger.info("*** Full Cancellation: Invoice cancelled***");
    }

    public InvoiceDto getInvoicesByBookingDetails(KafkaBookingMessage kafkaBookingMessage) throws OperationException {
        logger.info("*** Full Cancellation: Calling fiance to get invoice ***");
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(invoiceCheckAPIUrl)
                .queryParam("bookingRef", kafkaBookingMessage.getBookId())
                .queryParam("orderID", kafkaBookingMessage.getOrderNo());
        InvoiceDto invoice = null;
        try {
            InvoiceDto[] invoiceDtos = mdmRestUtils.getForObject(builder.toUriString(), InvoiceDto[].class);
            invoice = invoiceDtos[0];

        } catch (HttpServerErrorException he) {
            logger.error("*** Full Cancellation: 500 Internal Server in Finance", he);
            throw new OperationException("500 Internal Serve in finance");
        } catch (Exception e) {
            logger.error("*** Full Cancellation: Unable to get data from finance for invoice", e);
            throw new OperationException("Unable to get data from finance for invoice");
        }


        return invoice;
    }

}
