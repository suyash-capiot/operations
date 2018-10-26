package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.ext.model.be.BookingActionConstants;
import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.consumer.listners.ForexBookingListener;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.service.forex.ForexBookingService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ForexBookingListenerImpl implements ForexBookingListener {

    private static final Logger logger = Logger.getLogger(ForexBookingListenerImpl.class);
    @Autowired
    private ForexBookingService forexBookingService;

    @Async
    @Override
    public void processBooking(OpsBooking opsBooking, KafkaBookingMessage kafkaBookingMessage) throws OperationException {

        List<OpsProduct> opsProducts = opsBooking.getProducts();
        List<OpsHolidaysPaxInfo> forexPassengersList = new ArrayList<>();

        List<OpsPaymentInfo> paymentInfos = opsBooking.getPaymentInfo();

        Boolean isPaymentDone = false;
        if(paymentInfos.size()!=0)
            isPaymentDone = paymentInfos.stream().anyMatch(s-> s.getPaymentType().equalsIgnoreCase("full"));

        //TODO : To check if booking is confirmed and then proceed.
        //TODO : Add a check for full payment done , BE will send a kafka message for payment done as discusses with pritish.
        if (kafkaBookingMessage.getActionType().equals(BookingActionConstants.JSON_PROP_NEW_BOOKING) && isPaymentDone){
            for (OpsProduct opsProduct : opsProducts) {
                //Fetch the Holidays Booking
                if (OpsProductCategory.getProductCategory(opsProduct.getProductCategory()).equals(OpsProductCategory.PRODUCT_CATEGORY_HOLIDAYS)) {

                    OpsHolidaysDetails opsHolidaysDetails = opsProduct.getOrderDetails().getPackageDetails();
                    if (true || opsHolidaysDetails.getIsMultiCcyBooking()) {
                        //If Multi-currency Booking then check if the passengers opted for forex.
                        List<OpsHolidaysPaxInfo> opsHolidaysPaxInfos = opsHolidaysDetails.getPaxInfo();
                        for (OpsHolidaysPaxInfo opsHolidaysPaxInfo : opsHolidaysPaxInfos) {
                            //TODO: Set the value of isForexRequired in OpsHolidaysAdapter
                            if (true || opsHolidaysPaxInfo.getForexRequired())
                                forexPassengersList.add(opsHolidaysPaxInfo);

                        }
                        //If even a single passenger opted for Forex then create a forex request.
                        //TODO: If only the passengers that require forex need to be processed then pass "forexPassengersList".
                        if (forexPassengersList.size() > 0) {
                            logger.info("The Holidays booking for bookId " + opsBooking.getBookID() + " qualifies for forex Booking");
                            forexBookingService.process(opsBooking, opsProduct, opsHolidaysDetails.getPaxInfo());
//                      forexBookingService.process(opsBooking, opsProduct, forexPassengersList);
                        }

                    }
                }
            }
        }
    }

    @Override
    public BookingListenerType getListenerType() {
        return BookingListenerType.FOREX_BOOKING;
    }


}
