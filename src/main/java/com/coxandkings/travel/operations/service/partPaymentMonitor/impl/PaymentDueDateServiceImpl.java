package com.coxandkings.travel.operations.service.partPaymentMonitor.impl;

import com.coxandkings.travel.operations.enums.partpaymentmonitor.MDMDurationType;
import com.coxandkings.travel.operations.enums.partpaymentmonitor.PaymentDueDateType;
import com.coxandkings.travel.operations.enums.partpaymentmonitor.PaymentScheduleType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.resource.partpaymentmonitor.PartPaymentMDMFilter;
import com.coxandkings.travel.operations.resource.partpaymentmonitor.PaymentDue;
import com.coxandkings.travel.operations.resource.partpaymentmonitor.PaymentSchedule;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.partPaymentMonitor.PaymentDueDateService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class PaymentDueDateServiceImpl implements PaymentDueDateService {
    private static final Logger logger = LogManager.getLogger(PaymentDueDateServiceImpl.class);

    @Value(value = "${part-payment-monitor.mdm.part-payment-config-master}")
    private String getPartPaymentMDMUrl;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Override
    public ZonedDateTime calculatePaymentDueDate(String bookID) throws OperationException {
        ZonedDateTime calculatedPaymentDueDate = null;
        OpsBooking opsBooking = opsBookingService.getBooking(bookID);
        String mdmResponse = this.getMDMPartPaymentInfo(opsBooking.getClientID());
        List<PaymentSchedule> paymentScheduleList = jsonObjectProvider.getChildrenCollection(mdmResponse, "$.data[0].paymentSchedule.*", PaymentSchedule.class);
        if (paymentScheduleList.size() == 1) {
            for (PaymentSchedule aPaymentSchedule : paymentScheduleList) {
                if (aPaymentSchedule.getPaymentSchdType().equalsIgnoreCase(PaymentScheduleType.BALANCE_PAYMENT.getType()) &&
                        aPaymentSchedule.getPaymentDueDate().equalsIgnoreCase(PaymentDueDateType.FROM_BOOKING_DATE.getType())) {
                    PaymentDue paymentDue = aPaymentSchedule.getPaymentDue();
                    if (paymentDue != null) {
                        if (MDMDurationType.DAYS.getType().equalsIgnoreCase(paymentDue.getDurationType())) {

                        } else if (MDMDurationType.MONTHS.getType().equalsIgnoreCase(paymentDue.getDurationType())) {

                        }
                    }
                } else if (aPaymentSchedule.getPaymentSchdType().equalsIgnoreCase(PaymentScheduleType.BALANCE_PAYMENT.getType()) &&
                        aPaymentSchedule.getPaymentDueDate().equalsIgnoreCase(PaymentDueDateType.PRIOR_TO_TRAVEL_DATE.getType())) {
                    PaymentDue paymentDue = aPaymentSchedule.getPaymentDue();
                    if (paymentDue != null) {
                        if (MDMDurationType.DAYS.getType().equalsIgnoreCase(paymentDue.getDurationType())) {

                        } else if (MDMDurationType.MONTHS.getType().equalsIgnoreCase(paymentDue.getDurationType())) {

                        }
                    }
                } else {
                    //Full Payment Appplicable if initial deposite is selected
                }
            }
        } else if (paymentScheduleList.size() > 1) {
            //ToDo Entire balence payment should be scheduled to be collected as per supplier settlement due date
        }
        return null;
    }

    private List<PaymentSchedule> getPaymentSchedule(String mdmResponse) {
        return null;
    }

    private String getMDMPartPaymentInfo(String clientId) {
        logger.debug("entering into getPartPaymentInfo method");
        String filterString = "";
        PartPaymentMDMFilter mdmFilter = new PartPaymentMDMFilter();
        mdmFilter.setEntityId(clientId);
        ObjectMapper aMapper = new ObjectMapper();
        try {
            filterString = aMapper.writeValueAsString(mdmFilter);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        URI url = UriComponentsBuilder.fromUriString(getPartPaymentMDMUrl + filterString).build().encode().toUri();
        ResponseEntity<String> responseMDM = null;
        try {
            responseMDM = mdmRestUtils.exchange(url, HttpMethod.GET, null, String.class);
        } catch (Exception e) {
            logger.info("Error While Retriving partPayment response from MDM" + e);
        }
        return responseMDM.getBody();
    }

}
