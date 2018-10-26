package com.coxandkings.travel.operations.service.reconfirmation.client.impl;

import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationBookingAttribute;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.repository.reconfirmation.client.ClientReconfirmationRepository;
import com.coxandkings.travel.operations.repository.reconfirmation.supplier.SupplierReconfirmationRepository;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ClientReconfirmationUtilityService;
import com.coxandkings.travel.operations.service.reconfirmation.common.CustomClientDetails;
import com.coxandkings.travel.operations.service.reconfirmation.common.HashGenerator;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationUtilityService;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.SupplierReconfirmationService;
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

/**
 *
 */
@Service("clientReconfirmationUtilityService")
public class ClientReconfirmationUtilityServiceImpl implements ClientReconfirmationUtilityService {

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

    @Value(value = "${reconfirmation.client.communication.subject}")
    private String clientCommunicationSubject;


    /**
     *
     * @param bookingDetails
     * @param hotels
     * @param clientReconDetails
     * @param reconfirmationCutOffDate
     * @return
     * @throws OperationException
     */
    @Override
    public ClientReconfirmationDetails emailProcessForClient(OpsBooking bookingDetails, OpsProduct hotels, ClientReconfirmationDetails clientReconDetails, ZonedDateTime reconfirmationCutOffDate) throws OperationException {
        String email = null;

        CustomClientDetails clientContactDetails = reconfirmationMDMService.getB2BClientContactDetails(bookingDetails.getClientID(), bookingDetails.getClientType());
        if (clientContactDetails == null) {
            throw new OperationException(Constants.ER303);
        }
        try {
            clientReconDetails.setHash(this.hashGenerator.getHash());
            boolean status =   reconfirmationUtilityService.composeEmailForClient(clientReconDetails,bookingDetails,hotels,clientContactDetails,clientReconDetails.getHash(),true,false);

            if (status) {
                clientReconDetails.setReconfirmationSentToClient(status);
                clientReconDetails.setClientOrCustomerReconfirmationDate(reconfirmationCutOffDate);
                clientReconDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);
                clientReconDetails.setExpiredLink(false);
                clientReconDetails.setNumberOfTimesReconfirmationSent(clientReconDetails.getNumberOfTimesReconfirmationSent() == null ? 1 : clientReconDetails.getNumberOfTimesReconfirmationSent() + 1);
                clientReconDetails = this.clientReconfirmationRepository.saveOrUpdateClientReconfirmation(clientReconDetails);
                return clientReconDetails;
            } else {
                clientReconDetails.setReconfirmationSentToClient(status);
                clientReconDetails.setClientOrCustomerReconfirmationDate(reconfirmationCutOffDate);
                // clientReconfDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);
                clientReconDetails = this.clientReconfirmationRepository.saveOrUpdateClientReconfirmation(clientReconDetails);
                return clientReconDetails;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException(Constants.ER306);
        }
    }
}
