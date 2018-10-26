package com.coxandkings.travel.operations.service.retrieveorderstatusservice.impl;

import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsOrderStatus;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateResource;
import com.coxandkings.travel.operations.resource.managebookingstatus.UpdateOrderStatusResource;
import com.coxandkings.travel.operations.service.beconsumer.pnrstatus.RetrievePnrStatusService;
import com.coxandkings.travel.operations.service.booking.ManageBookingStatusService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.email.EmailService;
import com.coxandkings.travel.operations.service.retrieveorderstatusservice.RetrieveOrderStatusService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.supplier.CommunicationType;
import com.coxandkings.travel.operations.utils.supplier.SupplierDetailsService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class RetrieveOrderStatusServiceImpl implements RetrieveOrderStatusService {

    @Autowired
    private RetrievePnrStatusService retrievePnrStatusService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ManageBookingStatusService manageBookingStatusService;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @Value(value = "${communication.email.from_address}")
    private String emailAddress;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private EmailService emailService;

    @Override
    public void updateOrderStatusFromPnr(KafkaBookingMessage kafkaBookingMessage) throws OperationException {


        OpsBooking aOpsBooking = opsBookingService.getBooking(kafkaBookingMessage.getBookId());

        OpsProduct aOpsProduct = opsBookingService.getOpsProduct(aOpsBooking, kafkaBookingMessage.getOrderNo());

        String supplierDetailStr  = supplierDetailsService.getSupplierDetails(aOpsProduct.getSupplierID());
        if (null == supplierDetailStr) {
            throw new OperationException(Constants.SUPPLIER_DETAILS_NOT_AVAILABLE, aOpsProduct.getSupplierID());
        }

        JSONObject suppDetailJSON = new JSONObject(supplierDetailStr);
        /**
         * INFO: Checking whether supplier is online or offline
         */
        if (suppDetailJSON.getJSONObject(Constants.JSON_CONTENT_UPDATE_FREQUENCY).getBoolean(Constants.JSON_IS_API_XML_SUPPLIER)) {
            String pnrStatusRes = retrievePnrStatusService.retrievePnrStatusFromPnr(aOpsBooking, aOpsProduct);
            OpsOrderStatus opsOrderStatus = null;
            UpdateOrderStatusResource updateOrderStatusResource = new UpdateOrderStatusResource();
            updateOrderStatusResource.setOrderId(aOpsProduct.getOrderID());
            updateOrderStatusResource.setProductSubCategory(aOpsProduct.getOpsProductSubCategory());

            if (pnrStatusRes.trim().equalsIgnoreCase("CONF") || pnrStatusRes.trim().equalsIgnoreCase("RESERVED")) {
                opsOrderStatus = OpsOrderStatus.OK;
                updateOrderStatusResource.setOrderStatus(opsOrderStatus);
                manageBookingStatusService.updateOrderStatus(updateOrderStatusResource);
            }
        }
        else {
            EmailResponse emailResponse = new EmailResponse();
            EmailUsingTemplateResource emailUsingTemplateResource = null;
            OpsProductSubCategory opsProductSubCategory = aOpsProduct.getOpsProductSubCategory();
            String supplierReferenceId = null;

            switch(opsProductSubCategory) {
                case PRODUCT_SUB_CATEGORY_FLIGHT:
                    supplierReferenceId = aOpsProduct.getOrderDetails().getFlightDetails().getAirlinePNR();
                    break;
                case PRODUCT_SUB_CATEGORY_BUS:
                    supplierReferenceId = aOpsProduct.getSupplierReferenceId();
                    break;
                case PRODUCT_SUB_CATEGORY_RAIL:
                    break;
                case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
                    break;
                case PRODUCT_SUB_CATEGORY_CAR:
                    break;
                case PRODUCT_SUB_CATEGORY_HOTELS:
                    break;
                case PRODUCT_SUB_CATEGORY_EVENTS:
                    break;
                case PRODUCT_SUB_CATEGORY_HOLIDAYS:
                    break;
            }
            try {
                emailUsingTemplateResource = getEmailUsingTemplateResourceForSupplier(aOpsBooking.getBookID(),
                        aOpsProduct.getOrderID() , supplierReferenceId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                emailResponse = emailService.sendEmail(emailUsingTemplateResource);
            } catch (Exception e) {
                throw new OperationException(Constants.OPS_ERR_20016);
            }
        }
    }

    private EmailUsingTemplateResource getEmailUsingTemplateResourceForSupplier(String bookId, String orderId,
                                                                    String supplierReferenceId) throws Exception {
        EmailUsingTemplateResource emailUsingTemplateResource = new EmailUsingTemplateResource();
        Map<String, String> dynamicVariables = new HashMap<>();

        OpsProduct opsProduct = opsBookingService.getProduct(bookId, orderId);
        String supplierId = opsProduct.getSupplierID();
        String supplierName = null;
        CommunicationType communicationType = supplierDetailsService.getSupplierCommunicationTypeBySupplierId(supplierId);

        if (communicationType == null) {
            throw new Exception(Constants.ER01);
        } else {
            switch (communicationType) {
                case EMAIL:
                    String jsonSupplier = supplierDetailsService.getSupplierDetails(supplierId);
                    String emailId = String.valueOf(jsonObjectProvider.getChildObject(jsonSupplier,
                            "$.contactInfo.contactDetails.email", String.class));
                    supplierName = String.valueOf(jsonObjectProvider.getChildObject(jsonSupplier,
                            "$.supplier.name", String.class));

                    emailUsingTemplateResource.setToMail(Collections.singletonList(emailId));
                    break;

                case FAX:
                    throw new OperationException(Constants.ER45 + opsProduct.getEnamblerSupplierName());

                case PHONE:
                    throw new OperationException(Constants.ER45 + opsProduct.getEnamblerSupplierName());
            }
        }

        emailUsingTemplateResource.setSubject(ConstantsSendEmail.SEND_EMAIL_SUBJECT);
        emailUsingTemplateResource.setPriority(EmailPriority.HIGH);
        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setIsActive(true);
        templateInfo.setGroupOfCompanies("");
        templateInfo.setGroupCompany("");
        templateInfo.setCompanyName("");
        templateInfo.setBusinessUnit("");
        templateInfo.setSubBusinessUnit("");
        templateInfo.setMarket("");
        templateInfo.setOffice("");
        templateInfo.setSource("");
        templateInfo.setProductCategory("");
        templateInfo.setProductCategorySubType("");
        templateInfo.setProcess(ConstantsSendEmail.SEND_EMAIL_PROCESS);
        templateInfo.setScenario(ConstantsSendEmail.SEND_EMAIL_SCENARIO);
        templateInfo.setRule1("");
        templateInfo.setRule2("");
        templateInfo.setRule3("");
        templateInfo.setCommunicationType("");
        templateInfo.setCommunicateTo("");
        templateInfo.setIncomingCommunicationType("");
        templateInfo.setDestination("");
        templateInfo.setBrochure("");
        templateInfo.setTour("");
        templateInfo.setClientType("");
        templateInfo.setClientGroup("");
        templateInfo.setClientName("");
        templateInfo.setSupplier("");
        templateInfo.setFunction(ConstantsSendEmail.SEND_EMAIL_FUNCTION);
        emailUsingTemplateResource.setTemplateInfo(templateInfo);
        emailUsingTemplateResource.setFromMail(emailAddress);

        dynamicVariables.put("Supplier Name", supplierName);
        dynamicVariables.put("Supplier Refrence ID", supplierName);

        emailUsingTemplateResource.setDynamicVariables(dynamicVariables);

        return emailUsingTemplateResource;
    }
}
