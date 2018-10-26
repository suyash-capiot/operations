package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.ext.model.be.BookingActionConstants;
import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.consumer.listners.QcManagementListener;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.qcmanagement.QCManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QCManagementListenerImpl implements QcManagementListener {

    @Autowired
    private QCManagementService qcManagementService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Override
    public BookingListenerType getListenerType() {
        return BookingListenerType.QC_MANAGEMENT;
    }

    @Override
    @Async
    public void processBooking(KafkaBookingMessage kafkaBookingMessage) throws OperationException {
        OpsBooking opsBooking = opsBookingService.getBooking(kafkaBookingMessage.getBookId());

        if (kafkaBookingMessage.getOperation().equalsIgnoreCase(BookingActionConstants.JSON_PROP_NEW_BOOKING)) {
            qcManagementService.qcCheck(opsBooking);
        }
        if (kafkaBookingMessage.getOperation().equalsIgnoreCase(BookingActionConstants.JSON_PROP_AMEND)
                || kafkaBookingMessage.getOperation().equalsIgnoreCase(BookingActionConstants.JSON_PROP_CANCEL)) {
            OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking, kafkaBookingMessage.getOrderNo());
            //qcManagementService.qcCheckForAmendmentAndCancellation(opsProduct, kafkaBookingMessage.getOperation(), kafkaBookingMessage.getActionType());
        }
    }
}
