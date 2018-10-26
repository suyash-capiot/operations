package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.consumer.listners.FileProfitabilityListener;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.service.FileProfitability.impl.FileProfitabilityServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;


public class FileProfitabilityListenerImpl implements FileProfitabilityListener {
    private static final Logger logger = LogManager.getLogger(FileProfitabilityListenerImpl.class);
    @Autowired
    FileProfitabilityServiceImpl fileProfitabilityService;

    @Override
    public BookingListenerType getListenerType() {
        return BookingListenerType.FILE_PROFITABILITY;
    }

    @Async
    @Override
    public void processBooking(KafkaBookingMessage kafkaBookingMessage) throws JsonProcessingException, OperationException {
        logger.info("in processBooking() of FileProfitabilityListenerImpl class");
        if (kafkaBookingMessage != null)
            logger.info("in processBooking() of FileProfitabilityListenerImpl class checking kafkaBookingMessage " + kafkaBookingMessage.getBookId());
        fileProfitabilityService.opsBookingFromKafka(kafkaBookingMessage);
    }


}
