package com.coxandkings.travel.operations.service.reconfirmation.supplier.impl;

import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationBookingAttribute;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.repository.reconfirmation.client.ClientReconfirmationRepository;
import com.coxandkings.travel.operations.repository.reconfirmation.supplier.SupplierReconfirmationRepository;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.common.CustomSupplierDetails;
import com.coxandkings.travel.operations.service.reconfirmation.common.HashGenerator;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationUtilityService;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.SupplierReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.SupplierReconfirmationUtilityService;
import com.coxandkings.travel.operations.service.template.TemplateLoaderService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.adapter.OpsBookingAdapter;
import com.coxandkings.travel.operations.utils.adapter.OpsFlightAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;


@Service("supplierReconfirmationUtilityService")
public class SupplierReconfirmationUtilityServiceImpl implements SupplierReconfirmationUtilityService {
    @Autowired
    private ReconfirmationUtilityService reconfirmationUtilityService;

    @Autowired
    private ClientReconfirmationRepository clientReconfirmationRepository;

    @Autowired
    private SupplierReconfirmationRepository supplierReconfirmationRepository;

    @Autowired
    private SupplierReconfirmationService supplierReconfirmationService;

    @Autowired
    private TemplateLoaderService templateLoaderService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private OpsBookingAdapter opsBookingAdapter;

    @Autowired
    private OpsFlightAdapter opsFlightAdapter;

    @Autowired
    private ReconfirmationService reconfirmationService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ReconfirmationMDMService reconfirmationMDMService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private HashGenerator hashGenerator;

    @Value(value = "${reconfirmation.supplier.communication.subject}")
    private String supplierCommunicationSubject;

    /**
     *
     * @param bookingDetails
     * @param hotels
     * @param supplierReconDetails
     * @param supplierReconfirmationDate
     * @return
     * @throws OperationException
     */
    @Override
    public SupplierReconfirmationDetails emailProcessForSupplier(OpsBooking bookingDetails, OpsProduct hotels, SupplierReconfirmationDetails supplierReconDetails, ZonedDateTime supplierReconfirmationDate) throws OperationException {
        CustomSupplierDetails supplierDetails = this.reconfirmationMDMService.getSupplierContactDetails(hotels.getSupplierID());
        if (supplierDetails == null) {
            throw new OperationException(Constants.ER303);
        }
        try {
            supplierReconDetails.setHash(this.hashGenerator.getHash());
            boolean status =   reconfirmationUtilityService.composeEmailForSupplier(supplierReconDetails,bookingDetails,hotels,supplierDetails,supplierReconDetails.getHash(),false,true);
            if (status) {
                supplierReconDetails.setReconfirmationSentToSupplier(status);
                supplierReconDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);
                supplierReconDetails.setExpiredLink(false);
                supplierReconDetails.setNumberOfTimesReconfirmationSent(supplierReconDetails.getNumberOfTimesReconfirmationSent() == null ? 1 : supplierReconDetails.getNumberOfTimesReconfirmationSent() + 1);
                supplierReconDetails.setSupplierReconfirmationDate(supplierReconfirmationDate);
                supplierReconDetails.setSupplierName(supplierDetails.getSupplierName());
                supplierReconDetails.setAutoReconfirmation(true);
                supplierReconDetails = this.supplierReconfirmationRepository.saveOrUpdateSupplierReconfirmation(supplierReconDetails);
                return supplierReconDetails;
            } else {
                supplierReconDetails.setReconfirmationSentToSupplier(status);
                supplierReconDetails.setSupplierReconfirmationDate(supplierReconfirmationDate);
                supplierReconDetails.setSupplierName(supplierDetails.getSupplierName());
                supplierReconDetails = this.supplierReconfirmationRepository.saveOrUpdateSupplierReconfirmation(supplierReconDetails);
                return supplierReconDetails;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException(Constants.ER306);
        }
    }
}
