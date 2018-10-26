package com.coxandkings.travel.operations.service.newsupplierfirstbooking.impl;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.newsupplierfirstbooking.NewSupplierCommunication;
import com.coxandkings.travel.operations.model.newsupplierfirstbooking.NewSupplierDetails;
import com.coxandkings.travel.operations.repository.newsupplierfirstbooking.NewSupplierCommunicationRepository;
import com.coxandkings.travel.operations.repository.newsupplierfirstbooking.NewSupplierDetailsRepository;
import com.coxandkings.travel.operations.resource.SupplierEmailResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.newsupplierfirstbooking.NewSupplierFirstBookingService;
import com.coxandkings.travel.operations.service.newsupplierfirstbooking.NewSupplierMDMService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.EmailUtils;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewSupplierFirstBookingServiceImpl implements NewSupplierFirstBookingService {
    private static final Logger logger = LogManager.getLogger(NewSupplierFirstBookingServiceImpl.class);

    @Value(value = "${new-supplier-first-booking.response.accept_url}")
    private String acceptUrl;

    @Value(value = "${new-supplier-first-booking.response.reject_url}")
    private String rejectUrl;

    @Value(value = "${new-supplier-first-booking.be.getAirOrders}")
    private String urlToGetAirOrders;

    @Value(value = "${new-supplier-first-booking.be.getAccoOrders}")
    private String urlToGetAccoOrders;

    @Value(value = "${new-supplier-first-booking.path.supplierEmailId}")
    private String pathToGetSupplierEmailID;

    @Value(value = "${new-supplier-first-booking.alertConfig.businessProcess}")
    private String businessProcess;

    @Value(value = "${new-supplier-first-booking.alertConfig.function}")
    private String alertFunction;

    @Value(value = "${new-supplier-first-booking.alert.receive_alert}")
    private String receiveAlertName;

    @Value(value = "${new-supplier-first-booking.function}")
    private String emailFunction;

    @Value(value = "${new-supplier-first-booking.checkDetails.scenario}")
    private String emailScenario;

    @Value(value = "${new-supplier-first-booking.checkDetails.subject}")
    private String emailSubject;


    @Autowired
    private NewSupplierDetailsRepository newSupplierDetailsRepository;

    @Autowired
    private NewSupplierCommunicationRepository communicationRepository;

    @Autowired
    private NewSupplierMDMService newSupplierMDMService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private UserService userService;

    /**
     * Checks wheather first booking for supplier not
     *
     * @param opsBooking
     */
    @Override
    public void firstReservationChecks(OpsBooking opsBooking) {
        ResponseEntity<List> res = null;
        for (OpsProduct opsProduct : opsBooking.getProducts()) {
            if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                try {
                    //getting air orders based on supplierId and checks if single record
                    res = RestUtils.exchange(urlToGetAirOrders + opsProduct.getSupplierID(), HttpMethod.GET, null, List.class);
                    if (res != null && res.getBody().size() == 1) {
                        //flag the booking as first reservation check required.
                        //saving into DB
                        NewSupplierDetails details = newSupplierDetailsRepository.getByOrderID(opsProduct.getOrderID());
                        if (details == null) {
                            NewSupplierDetails newSupplierDetails = new NewSupplierDetails();
                            newSupplierDetails.setBookId(opsBooking.getBookID());
                            newSupplierDetails.setOrderId(opsProduct.getOrderID());
                            newSupplierDetails.setFirstReservationCheckFlag(true);
                            newSupplierDetailsRepository.saveSupplierDetials(newSupplierDetails);
                        }
                        String message = "First Booking For New Supplier is Received " + opsBooking.getBookID();
                        try {
                            alertService.createAlert(businessProcess, alertFunction, receiveAlertName, opsBooking.getCompanyId(), userService.getSystemUserIdFromMDMToken(), message);
                        } catch (Exception e) {
                            logger.error("Error while sending alert to logged in user when first booking for supplier is created" + e);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Error occurred while retrieving AirOrder details from Booking Engine Module", e);
                }
            } else if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                try {
                    res = RestUtils.exchange(urlToGetAccoOrders + opsProduct.getSupplierID(), HttpMethod.GET, null, List.class);
                    if (res != null && res.getBody().size() == 1) {
                        //flag the booking as first reservation check required.
                        //saving into DB
                        NewSupplierDetails details = newSupplierDetailsRepository.getByOrderID(opsProduct.getOrderID());
                        if (details == null) {
                            NewSupplierDetails newSupplierDetails = new NewSupplierDetails();
                            newSupplierDetails.setBookId(opsBooking.getBookID());
                            newSupplierDetails.setOrderId(opsProduct.getOrderID());
                            newSupplierDetails.setFirstReservationCheckFlag(true);
                            newSupplierDetailsRepository.saveSupplierDetials(newSupplierDetails);
                        }
                        String message = "First Booking For New Supplier is Received " + opsBooking.getBookID();
                        try {
                            alertService.createAlert(businessProcess, alertFunction, opsBooking.getCompanyId(), receiveAlertName, userService.getSystemUserIdFromMDMToken(), message);
                        } catch (Exception e) {
                            logger.error("Error while sending alert to logged in user when first booking for supplier is created" + e);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Error occurred while retrieving AccoOrder details from Booking Engine Module", e);
                }
            }
        }
    }

    @Override
    public EmailResponse sendEmailToSupplier(SupplierEmailResource supplierEmailResource) throws OperationException {
        String supplierEmail = null;
        String jsonObject = null;
        OpsBooking opsBooking = null;
        //get the email id from MDM by passing supplier Id
        //Todo
        jsonObject = newSupplierMDMService.getSupplierDetails(supplierEmailResource.getSupplierId());
        if (jsonObject != null) {
            supplierEmail = jsonObjectProvider.getAttributeValue(jsonObject, pathToGetSupplierEmailID, String.class);
        }
        //send email to supplier with booking details
        opsBooking = opsBookingService.getBooking(supplierEmailResource.getBookId());
        EmailResponse emailResponse = null;
        if (supplierEmail != null) {
            //ToDo create template in MDM
            String body = getTemplate(opsBooking.getBookID());
            Map<String, String> dynamicVariables = new HashMap<>();
            dynamicVariables.put("book_ref_id", supplierEmailResource.getBookId());
            dynamicVariables.put("orderId", supplierEmailResource.getOrderId());
            dynamicVariables.put("link ", body);
            try {
                emailResponse = emailUtils.buildClientMail(emailFunction,
                        emailScenario,
                        supplierEmail,
                        emailSubject,
                        dynamicVariables, null, null);
            } catch (Exception e) {
                logger.error("Error while sending email to supplier" + e);
            }
        }
        return emailResponse;
    }

    private String getTemplate(String bookId) {
        String acceptedURL = null;
        String rejectedURL = null;

        acceptedURL = acceptUrl + bookId;
        rejectedURL = rejectUrl + bookId;

        return "<a href='" + acceptedURL + "'>Accept</a>" + "<a href='" + rejectedURL + "'>Reject</a>";
    }

    @Transactional
    @Override
    public NewSupplierCommunication saveOrUpdateCommunication(NewSupplierCommunication newSupplierCommunication) {
        return communicationRepository.saveOrUpdate(newSupplierCommunication);
    }

    @Override
    public boolean getFlag(String orderId) {
        NewSupplierDetails aSupplierDetails = newSupplierDetailsRepository.getByOrderID(orderId);
        if (aSupplierDetails != null) {
            return aSupplierDetails.isFirstReservationCheckFlag();
        } else
            return false;
    }
}
