package com.coxandkings.travel.operations.service.reconfirmation.common;

import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateResource;
import com.coxandkings.travel.operations.service.email.EmailService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.adapter.OpsBookingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 */
@Service("commonService")
public class ReconfirmationUtilityServiceImpl implements ReconfirmationUtilityService {

    private static Logger logger = LogManager.getLogger(ReconfirmationUtilityServiceImpl.class);

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private Environment environment;

    @Autowired
    private OpsBookingAdapter opsBookingAdapter;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private EmailService emailService;


    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private UserService userService;


    @Value(value = "${reconfirmation.ops.sendEmailWithTemplate}")
    private String urlForSendEmailWithTemplate;

    //Supplier Related Messages
    @Value(value = "${reconfirmation.supplier.communication.subject}")
    private String supplierCommunicationSubject;


    //Client Related Messages
    @Value(value = "${reconfirmation.client.communication.subject}")
    private String clientCommunicationSubject;


    @Value(value = "${reconfirmation.client.communication.accept.url}")
    private String acceptClientReconfirmation;


    @Value(value = "${reconfirmation.client.communication.reject.url}")
    private String rejectClientReconfirmation;


    @Value(value = "${reconfirmation.supplier.communication.accept.url}")
    private String acceptSupplierReconfirmation;

    @Value(value = "${reconfirmation.supplier.communication.reject.url}")
    private String rejectSupplierReconfirmation;

    @Value(value = "${reconfirmation.supplier.communication.template.business_process}")
    private String RECON_SPPLR_BUSINESS_PROCESS;

    @Value(value = "${reconfirmation.supplier.communication.template.function}")
    private String RECON_SPPLR_FUNCTION;

    @Value(value = "${reconfirmation.supplier.communication.template.scenario}")
    private String RECON_SPPLR_SCENARIO;


    @Value(value = "${reconfirmation.client.communication.template.business_process}")
    private String RECON_CLIENT_BUSINESS_PROCESS;

    @Value(value = "${reconfirmation.client.communication.template.function}")
    private String RECON_CLIENT_FUNCTION;

    @Value(value = "${reconfirmation.client.communication.template.scenario}")
    private String RECON_CLIENT_SCENARIO;

    /**
     * @param object
     * @return
     */
    @Override
    public String prettyJSON(Object object) {
        String s = null;
        try {
            s = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            if (logger.isDebugEnabled()) {
               // logger.debug(s);
            }
           // System.err.println(s);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug(e.toString());
            }
            return s;
        }
    }

    /**
     * @param URL
     * @param token
     * @param httpMethod
     * @param responseType
     * @param args
     * @param <T>
     * @return
     * @throws OperationException
     */
    @Override
    public <T> T exchange(String URL, String token, HttpMethod httpMethod, Class<T> responseType, Object... args) throws OperationException {
        T body = null;
        body = this.mdmRestUtils.exchange(URL, httpMethod, responseType, args);
        return body;
    }

    /**
     * @param URL
     * @param token
     * @param httpMethod
     * @param responseType
     * @param args
     * @param <T>
     * @return
     * @throws OperationException
     */
    public <T> T exchange(URI URL, String token, HttpMethod httpMethod, Class<T> responseType, Object... args) throws OperationException {
        T body = null;
        body = this.mdmRestUtils.exchange(URL, httpMethod, responseType, args);
        return body;
    }

    /**
     * @param duration
     * @param durationType
     * @return
     */
    @Override
    public long convertToHours(Integer duration, String durationType) {
        if (durationType.equalsIgnoreCase("days")) {
            int hours = duration * 24;
            return hours;
        }
        return duration;
    }

    /**
     * @param msgCode
     * @param locale
     * @param args
     * @return
     */
    @Override
    public String getMessage(String msgCode, Locale locale, String... args) {
        String message = messageSource.getMessage(msgCode, args, locale);
        return message;
    }

    /**
     * @param emailUsingTemplateResource
     */
    //@Async
    private boolean sendAnEmail(EmailUsingTemplateResource emailUsingTemplateResource) {
        prettyJSON(emailUsingTemplateResource);
        URI uri = UriComponentsBuilder.fromUriString(urlForSendEmailWithTemplate).build().encode().toUri();
        ResponseEntity<EmailResponse> emailResponse = null;
        try {
            emailResponse = mdmRestUtils.exchange(uri, HttpMethod.POST, emailUsingTemplateResource, EmailResponse.class);
            EmailResponse body = emailResponse.getBody();
            if (body.getStatus().equalsIgnoreCase("success")) {
                logger.info(" Email Status success   " + body.getStatus());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    /**
     * @param details
     * @param opsBooking
     * @param opsProduct
     * @param customClientDetails
     * @param hash
     * @param isClient
     * @param isSupplier
     * @return
     */
    @Override
    public boolean composeEmailForClient(ClientReconfirmationDetails details, OpsBooking opsBooking, OpsProduct opsProduct, CustomClientDetails customClientDetails, String hash, boolean isClient, boolean isSupplier) {

        try {
            String acceptedURL = null;
            String rejectedURL = null;
            if (isSupplier) {
                acceptedURL = acceptSupplierReconfirmation + hash;
                acceptedURL = removeHttp(acceptedURL);

                rejectedURL = rejectSupplierReconfirmation + hash;
                rejectedURL = removeHttp(rejectedURL);
            }
            if (isClient) {
                acceptedURL = acceptClientReconfirmation + hash;
                acceptedURL = removeHttp(acceptedURL);

                rejectedURL = rejectClientReconfirmation + hash;
                rejectedURL = removeHttp(rejectedURL);
            }
            EmailUsingTemplateResource emailUsingTemplateResource = new EmailUsingTemplateResource();

            ArrayList<String> emails = new ArrayList<>();
            if (customClientDetails.getEmailId() != null && customClientDetails.getEmailId().isEmpty() == false) {
                emails.add(customClientDetails.getEmailId());
            }
            emailUsingTemplateResource.setCommunicationTagResource(getCommunicationTagResource(opsBooking, opsProduct));
            emailUsingTemplateResource.setToMail(emails);
            emailUsingTemplateResource.setSubject(clientCommunicationSubject);
            emailUsingTemplateResource.setTemplateInfo(getTemplateInfo(RECON_CLIENT_BUSINESS_PROCESS, RECON_CLIENT_SCENARIO, RECON_CLIENT_FUNCTION));
            emailUsingTemplateResource.setFunction(RECON_CLIENT_FUNCTION);
            emailUsingTemplateResource.setProcess(RECON_CLIENT_BUSINESS_PROCESS);
            emailUsingTemplateResource.setScenario(RECON_CLIENT_SCENARIO);
            emailUsingTemplateResource.setPriority(EmailPriority.HIGH);
            emailUsingTemplateResource.setSupplier(opsProduct.getSupplierID() != null ? opsProduct.getSupplierID() : "NA");
            emailUsingTemplateResource.setBookId(opsBooking.getBookID());
            emailUsingTemplateResource.setProductSubCategory(opsProduct.getProductSubCategory());
            emailUsingTemplateResource.setFromMail("'smtp.aws' <smtp.aws@coxandkings.com>");
            try {
                if (this.userService.getLoggedInUserId() != null) {
                    emailUsingTemplateResource.setUserId(this.userService.getLoggedInUserId());
                } else {
                    emailUsingTemplateResource.setUserId("NA");
                }
            } catch (Exception e) {
                e.printStackTrace();
                emailUsingTemplateResource.setUserId("NA");
            }
            Map<String, String> dynamicVariables = new HashMap<>();
            dynamicVariables.put("acceptURL", acceptedURL);
            dynamicVariables.put("rejectURL", rejectedURL);

            if (customClientDetails != null && customClientDetails.getFirstName() != null) {
                dynamicVariables.put("First Name", customClientDetails.getFirstName());
            } else {
                dynamicVariables.put("First Name", "user");
            }

            if (customClientDetails != null && customClientDetails.getLastName() != null) {
                dynamicVariables.put("Last Name", customClientDetails.getLastName());
            } else {
                dynamicVariables.put("Last Name", "user");
            }

            dynamicVariables.put("Booking_Ref_Id", opsBooking.getBookID());
            dynamicVariables.put("Order_Id", opsProduct.getOrderID());

            if (details.getClientOrCustomerReconfirmationDate() != null) {
                dynamicVariables.put("Reconfirmation Date", details.getClientOrCustomerReconfirmationDate().toOffsetDateTime().toString());
            }

            dynamicVariables.put("Supplier Name", customClientDetails.getEmailId());
            dynamicVariables.put("Product_Name", opsProduct.getProductName() != null ? opsProduct.getProductName() : "");
            dynamicVariables.put("Product Type", opsProduct.getProductSubCategory() != null ? opsProduct.getProductSubCategory() : "");

            emailUsingTemplateResource.setDynamicVariables(dynamicVariables);
            boolean result = this.sendAnEmail(emailUsingTemplateResource);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String removeHttp(String inputString) {
        boolean b = inputString.startsWith("http://");
        if (b) {
            inputString = inputString.substring("http://".length(), inputString.length());
        }
        return inputString;
    }

    /**
     * @param details
     * @param opsBooking
     * @param opsProduct
     * @param customSupplierDetails
     * @param hash
     * @param isClient
     * @param isSupplier
     * @return
     */
    @Override
    public boolean composeEmailForSupplier(SupplierReconfirmationDetails details, OpsBooking opsBooking, OpsProduct opsProduct, CustomSupplierDetails customSupplierDetails, String hash, boolean isClient, boolean isSupplier) {
        try {
            String acceptedURL = null;
            String rejectedURL = null;
            if (isSupplier) {
                acceptedURL = acceptSupplierReconfirmation + hash;
                acceptedURL = removeHttp(acceptedURL);

                rejectedURL = rejectSupplierReconfirmation + hash;
                rejectedURL = removeHttp(rejectedURL);
            }
            if (isClient) {
                acceptedURL = acceptClientReconfirmation + hash;
                acceptedURL = removeHttp(acceptedURL);

                rejectedURL = rejectClientReconfirmation + hash;
                rejectedURL = removeHttp(rejectedURL);
            }

            EmailUsingTemplateResource emailUsingTemplateResource = new EmailUsingTemplateResource();

            ArrayList<String> emails = new ArrayList<>();
            if (customSupplierDetails.getEmailId() != null && customSupplierDetails.getEmailId().isEmpty() == false) {
                emails.add(customSupplierDetails.getEmailId());
            }
            emailUsingTemplateResource.setCommunicationTagResource(getCommunicationTagResource(opsBooking, opsProduct));
            emailUsingTemplateResource.setToMail(emails);
            emailUsingTemplateResource.setSubject(supplierCommunicationSubject);
            emailUsingTemplateResource.setTemplateInfo(getTemplateInfo(RECON_SPPLR_BUSINESS_PROCESS, RECON_SPPLR_SCENARIO, RECON_SPPLR_FUNCTION));
            emailUsingTemplateResource.setFunction(RECON_SPPLR_FUNCTION);
            emailUsingTemplateResource.setProcess(RECON_SPPLR_BUSINESS_PROCESS);
            emailUsingTemplateResource.setScenario(RECON_SPPLR_SCENARIO);
            emailUsingTemplateResource.setPriority(EmailPriority.HIGH);
            emailUsingTemplateResource.setSupplier(opsProduct.getSupplierID() != null ? opsProduct.getSupplierID() : "NA");
            emailUsingTemplateResource.setBookId(opsBooking.getBookID());
            emailUsingTemplateResource.setProductSubCategory(opsProduct.getProductSubCategory());
            emailUsingTemplateResource.setFromMail("'smtp.aws' <smtp.aws@coxandkings.com>");
            try {
                if (this.userService.getLoggedInUserId() != null) {
                    emailUsingTemplateResource.setUserId(this.userService.getLoggedInUserId());
                } else {
                    emailUsingTemplateResource.setUserId("NA");
                }
            } catch (Exception e) {
                e.printStackTrace();
                emailUsingTemplateResource.setUserId("NA");
            }

            Map<String, String> dynamicVaribles = new HashMap<>();
            dynamicVaribles.put("acceptURL", acceptedURL);
            dynamicVaribles.put("rejectURL", rejectedURL);
            dynamicVaribles.put("First Name", customSupplierDetails.getSupplierName());
            dynamicVaribles.put("Last Name", customSupplierDetails.getSupplierName());
            dynamicVaribles.put("Booking_Ref_Id", opsBooking.getBookID());
            dynamicVaribles.put("Order_Id", opsProduct.getOrderID());
            if (details.getSupplierReconfirmationDate() != null) {
                dynamicVaribles.put("Reconfirmation Date", details.getSupplierReconfirmationDate().toOffsetDateTime().toString());
            }

            dynamicVaribles.put("Supplier Name", customSupplierDetails.getSupplierName());
            dynamicVaribles.put("Product_Name", opsProduct.getProductName() != null ? opsProduct.getProductName() : "");
            dynamicVaribles.put("Product Type", opsProduct.getProductSubCategory() != null ? opsProduct.getProductSubCategory() : "");
            emailUsingTemplateResource.setDynamicVariables(dynamicVaribles);
            boolean result =  this.sendAnEmail(emailUsingTemplateResource);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * @param details
     * @param opsBooking
     * @param opsProduct
     * @param customSupplierDetails
     * @param hash
     * @param isClient
     * @param isSupplier
     * @return
     */
    @Override
    public boolean composeEmailForServiceProvider(SupplierReconfirmationDetails details, OpsBooking opsBooking, OpsProduct opsProduct, CustomSupplierDetails customSupplierDetails, String hash, boolean isClient, boolean isSupplier) {
        try {
            String acceptedURL = null;
            String rejectedURL = null;
            if (isSupplier) {
                acceptedURL = acceptSupplierReconfirmation + hash;
                acceptedURL = removeHttp(acceptedURL);

                rejectedURL = rejectSupplierReconfirmation + hash;
                rejectedURL = removeHttp(rejectedURL);
            }
            if (isClient) {
                acceptedURL = acceptClientReconfirmation + hash;
                acceptedURL = removeHttp(acceptedURL);

                rejectedURL = rejectClientReconfirmation + hash;
                rejectedURL = removeHttp(rejectedURL);
            }

            EmailUsingTemplateResource emailUsingTemplateResource = new EmailUsingTemplateResource();

            ArrayList<String> emails = new ArrayList<>();
            if (customSupplierDetails.getEmailId() != null && customSupplierDetails.getEmailId().isEmpty() == false) {
                emails.add(customSupplierDetails.getEmailId());
            }
            emailUsingTemplateResource.setCommunicationTagResource(getCommunicationTagResource(opsBooking, opsProduct));
            emailUsingTemplateResource.setToMail(emails);
            emailUsingTemplateResource.setSubject(supplierCommunicationSubject);
            emailUsingTemplateResource.setTemplateInfo(getTemplateInfo(RECON_SPPLR_BUSINESS_PROCESS, RECON_SPPLR_SCENARIO, RECON_SPPLR_FUNCTION));
            emailUsingTemplateResource.setFunction(RECON_SPPLR_FUNCTION);
            emailUsingTemplateResource.setProcess(RECON_SPPLR_BUSINESS_PROCESS);
            emailUsingTemplateResource.setScenario(RECON_SPPLR_SCENARIO);
            emailUsingTemplateResource.setPriority(EmailPriority.HIGH);
            emailUsingTemplateResource.setSupplier(opsProduct.getSupplierID() != null ? opsProduct.getSupplierID() : "NA");
            emailUsingTemplateResource.setBookId(opsBooking.getBookID());
            emailUsingTemplateResource.setProductSubCategory(opsProduct.getProductSubCategory());
            emailUsingTemplateResource.setFromMail("'smtp.aws' <smtp.aws@coxandkings.com>");
            try {
                if (this.userService.getLoggedInUserId() != null) {
                    emailUsingTemplateResource.setUserId(this.userService.getLoggedInUserId());
                } else {
                    emailUsingTemplateResource.setUserId("NA");
                }
            } catch (Exception e) {
                e.printStackTrace();
                emailUsingTemplateResource.setUserId("NA");
            }

            Map<String, String> dynamicVaribles = new HashMap<>();
            dynamicVaribles.put("acceptURL", acceptedURL);
            dynamicVaribles.put("rejectURL", rejectedURL);
            dynamicVaribles.put("First Name", customSupplierDetails.getSupplierName());
            dynamicVaribles.put("Last Name", customSupplierDetails.getSupplierName());
            dynamicVaribles.put("Booking_Ref_Id", opsBooking.getBookID());
            dynamicVaribles.put("Order_Id", opsProduct.getOrderID());
            if (details.getSupplierReconfirmationDate() != null) {
                dynamicVaribles.put("Reconfirmation Date", details.getSupplierReconfirmationDate().toOffsetDateTime().toString());
            }

            dynamicVaribles.put("Supplier Name", customSupplierDetails.getSupplierName());
            dynamicVaribles.put("Product_Name", opsProduct.getProductName() != null ? opsProduct.getProductName() : "");
            dynamicVaribles.put("Product Type", opsProduct.getProductSubCategory() != null ? opsProduct.getProductSubCategory() : "");
            emailUsingTemplateResource.setDynamicVariables(dynamicVaribles);
            boolean result =  this.sendAnEmail(emailUsingTemplateResource);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * @param message
     * @param color
     * @return
     */
    @Override
    public String convertToHtml(String message, String color) {

        switch (color) {
            case "red": {
                return "<center><h3><font color='red'>" + message + "</font></h3></center>";
            }
            case "green": {
                return "<center><h3><font color='green'>" + message + "</font></h3></center>";
            }
            case "blue": {
                return "<center><h3><font color='blue'>" + message + "</font></h3></center>";
            }
            default: {
                return "<center><h3><font color='blue'>" + message + "</font></h3></center>";
            }
        }
    }

    /**
     * @param businessProcess
     * @param scenario
     * @param function
     * @return
     */
    @Override
    public TemplateInfo getTemplateInfo(String businessProcess, String scenario, String function) {
        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setIsActive(true);
        templateInfo.setGroupOfCompanies("");
        templateInfo.setGroupCompany("");
        templateInfo.setDestination("");
        templateInfo.setBrochure("");
        templateInfo.setTour("");
        templateInfo.setSubBusinessUnit("");
        templateInfo.setRule2("");
        templateInfo.setRule3("");
        templateInfo.setMarket("");
        templateInfo.setOffice("");
        templateInfo.setSource("");
        templateInfo.setProductCategory("");
        templateInfo.setProductCategorySubType("");
        templateInfo.setRule1("");
        templateInfo.setCommunicationType("");
        templateInfo.setCommunicateTo("");
        templateInfo.setIncomingCommunicationType("");
        templateInfo.setBusinessUnit("");
        templateInfo.setClientGroup("");
        templateInfo.setClientType("");
        templateInfo.setClientName("");
        templateInfo.setSupplier("");
        //Only need to set this values
        templateInfo.setCompanyName("");
        templateInfo.setProcess(businessProcess);
        templateInfo.setScenario(scenario);
        templateInfo.setFunction(function);
        return templateInfo;
    }

    /**
     * @param opsBooking
     * @param opsProduct
     * @return
     */
    @Override
    public CommunicationTagResource getCommunicationTagResource(OpsBooking opsBooking, OpsProduct opsProduct) {
        CommunicationTagResource communicationTagResource = new CommunicationTagResource();
        communicationTagResource.setActionType("EMAIL");
        communicationTagResource.setBookId(opsBooking.getBookID());
        communicationTagResource.setOrderId(opsProduct.getOrderID());
        communicationTagResource.setClientId(opsBooking.getClientID());
        communicationTagResource.setCustomerId(opsBooking.getClientID());
        communicationTagResource.setSupplierId(opsProduct.getSupplierID());
        communicationTagResource.setFunction(RECON_SPPLR_FUNCTION);
        communicationTagResource.setProcess(RECON_SPPLR_BUSINESS_PROCESS);
        communicationTagResource.setScenario(RECON_SPPLR_SCENARIO);
        return communicationTagResource;
    }


}
