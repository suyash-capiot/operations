package com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentdetails.impl;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentDetailsType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.GuaranteeToSupplier;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PayToSupplier;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PaymentDetails;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.chequeordddetails.ChequeOrDdDetailsRepository;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentadvice.PaymentAdviceRepository;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentdetails.PaymentDetailsRepository;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.wiretransferorneft.WireTransferOrNeftRepository;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentReferenceResource;
import com.coxandkings.travel.operations.resource.documentLibrary.NewDocumentResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateResource;
import com.coxandkings.travel.operations.resource.email.EmailWithBodyResource;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.notification.NotificationResource;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentdetails.MediaResource;
import com.coxandkings.travel.operations.resource.pricedetails.ROE;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.commercialstatements.SupplierCommercialStatementService;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.PaymentAdviceService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentdetails.PaymentDetailsService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityService;
import com.coxandkings.travel.operations.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.jcr.RepositoryException;
import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service(value = "PaymentDetailsServiceImpl")
@Transactional
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

    @Autowired
    private MDMRestUtils mdmRestUtil;

    @Autowired
    private DocumentLibraryService documentLibraryService;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;


    @Autowired
    private WireTransferOrNeftRepository wireTransferOrNeftRepository;


    @Autowired
    private ChequeOrDdDetailsRepository chequeOrDdDetailsRepository;


    @Autowired
    private PaymentAdviceService paymentAdviceService;

    @Autowired
    private PaymentAdviceRepository paymentAdviceRepository;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${mdm.get-supplier-roe}")
    private String supplierROEUrl;

    @Value("${pre-payment-to-supplier.mdm.extranet-access}")
    private String extranetAccess;

    @Value("${communication.email.emailwithbody}")
    private String emailWithBody;

    @Value(value = "${communication.email.from_address}")
    private String emailAddress;

    @Value(value = "${pre_payment_to_supplier.template_config.function}")
    private String function;

    @Value(value = "${pre_payment_to_supplier.template_config.scenario}")
    private String scenario;

    @Value(value = "${pre_payment_to_supplier.template_config.subject}")
    private String subject;

    @Value(value = "${pre_payment_to_supplier.dynamic_variables.first_name}")
    private String firstName;

    @Value(value = "${pre_payment_to_supplier.dynamic_variables.product_name}")
    private String htmlTemplate;

    @Value(value = "${pre_payment_to_supplier.alert.notificationType}")
    private String notificationType;

    @Value(value = "${pre_payment_to_supplier.alert.supplier.alertName}")
    private String supplierAlertName;

    @Value(value = "${pre_payment_to_supplier.alert.opsUser.alertName}")
    private String opsUserAlertName;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    AlertService alertService;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private Md5Utils md5Utils;

    @Autowired
    private SupplierCommercialStatementService supplierCommercialStatementService;

    private static final Logger logger = LogManager.getLogger(PaymentDetailsServiceImpl.class);



    @Override
    public String uploadDocs(MultipartFile file, MediaResource mediaResource) {
        DocumentReferenceResource documentReferenceResource = null;
        NewDocumentResource newDocumentResource = new NewDocumentResource();
        newDocumentResource.setType("pdf");
        newDocumentResource.setName(mediaResource.getFileName());
        newDocumentResource.setCategory(mediaResource.getFileCategory());
        newDocumentResource.setSubCategory("");
        newDocumentResource.setDescription(mediaResource.getFileDescription());
        newDocumentResource.setExtension("pdf");
        newDocumentResource.setAdditionalAttributes(mediaResource.getAdditionalAttributes());
        try {
            documentReferenceResource = documentLibraryService.create(file, newDocumentResource, null);
        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            e.printStackTrace();
        }

        return documentReferenceResource.getId().toString();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public PaymentDetails savePaymentDetails(PaymentDetails paymentDetails) throws OperationException {
        BigDecimal amtPayable = BigDecimal.ZERO;

        if (paymentDetails != null) {
            if (StringUtils.isEmpty(paymentDetails.getId())) {
                List<PayToSupplier> payToSupplierSet = paymentDetails.getPayToSupplierSet();
                if (payToSupplierSet != null && payToSupplierSet.size()>0)
                {
                    for (PayToSupplier payToSupplier : payToSupplierSet)
                    {
                    /*if (payToSupplier.getModeOfPayment().equalsIgnoreCase("Credit Card"))
                    {
                        String encrptedCCNumber = md5Utils.encrptCreditNumber(payToSupplier.getCreditCard().getCardNumber());
                        CreditCard creditCard = new CreditCard();
                        CopyUtils.copy(payToSupplier.getCreditCard(),creditCard);
                        creditCard.setCardNumber(encrptedCCNumber);
                        payToSupplier.setCreditCard(creditCard);
                    }*/
                        amtPayable = amtPayable.add(payToSupplier.getAmountPayableForSupplier());
                    }
                    int i = amtPayable.compareTo(paymentDetails.getPaymentAdviceNumber().getAmountPayableForSupplier());
                    if (i == 1) {
                        throw new OperationException(Constants.OPS_ERR_21410);
                    }
                } else {
                    GuaranteeToSupplier guaranteeToSupplier = paymentDetails.getGuaranteeToSupplier();
                    amtPayable = amtPayable.add(guaranteeToSupplier.getGuaranteeForAmtToSupplier());
                    int i = amtPayable.compareTo(paymentDetails.getPaymentAdviceNumber().getAmountPayableForSupplier());
                    if (i == 1) {
                        throw new OperationException(Constants.OPS_ERR_21410);
                    }

                }
            } else {

                PaymentDetails exitstingPaymentDetail = getPaymentDetailsById(paymentDetails.getId());
                if (exitstingPaymentDetail != null) {
                    CopyUtils.copy(paymentDetails, exitstingPaymentDetail);
                    paymentDetails = exitstingPaymentDetail;
                }
            }
        }


        try {
            paymentDetails = paymentDetailsRepository.savePaymentDetails(paymentDetails);
        } catch (Exception e) {
            throw new OperationException("Payment Details not Saved");
        }

        try {
            if (amtPayable.compareTo(paymentDetails.getPaymentAdviceNumber().getAmountPayableForSupplier())==0) {
                if (paymentDetails.getPaymentAdviceNumber().getPaymentAdviceStatementInfoSet().isEmpty())
                    serviceOrderAndSupplierLiabilityService.linkPaymentAdviceWithServiceOrder(paymentDetails);
                else supplierCommercialStatementService.updatePaymentDetails(paymentDetails.getPaymentAdviceNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            NotificationResource notificationResourceForOpsUser = sendAnAlertToOpsUser(paymentDetails);
            if (!StringUtils.isEmpty(notificationResourceForOpsUser.get_id())) {
                logger.info("Alert has been sent to Ops User");
            }
        } catch (Exception e) {
            throw new OperationException("alert not sent to Ops User");
        }

        String supplierName = paymentDetails.getPaymentAdviceNumber().getSupplierName();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("supplier.name", supplierName);
        String url = extranetAccess + jsonObject.toString() + "&select=extranetAccess";
        URI toUri = UriComponentsBuilder.fromUriString(url).build().toUri();
        ResponseEntity<String> responseEntity = mdmRestUtil.exchange(toUri, HttpMethod.GET, null, String.class);
        String responseInString = responseEntity.getBody();
        String extranetAttribute = jsonObjectProvider.getAttributeValue(responseInString, "$.data[0].extranetAccess", Boolean.class);
        if (!StringUtils.isEmpty(extranetAttribute)) {
            if (extranetAttribute.equalsIgnoreCase("true")) {
                NotificationResource notificationResourceForExtranetSupplier = sendAAlertToExtranetSuuplier(paymentDetails);
                if (!StringUtils.isEmpty(notificationResourceForExtranetSupplier.get_id())) {
                    logger.info("Alert has been sent to Extranet Supplier");
                    logger.info("Alert id is " + notificationResourceForExtranetSupplier.get_id());
                } else {
                    logger.debug("alert not sent to Extranet Supplier");
                }
            }
        }

           /* if (paymentDetails.getPaymentAdviceNumber()!=null)
            {
                PaymentCriteria paymentCriteria = new PaymentCriteria();
                paymentCriteria.setPaymentAdviceNumber(paymentDetails.getPaymentAdviceNumber().getPaymentAdviceNumber());
                PaymentAdvice paymentAdvice = paymentAdviceService.searchSupplierPayment(paymentCriteria).get(0);
              //  paymentAdvice.setPaymentDetails(paymentDetails);
                paymentAdviceRepository.updatePaymentAdvice(paymentAdvice);
            }
*/

        /*catch (Exception ex) {
            throw new OperationException("Payment Details not Saved");
        }*/

        try {
            serviceOrderAndSupplierLiabilityService.linkPaymentAdviceWithServiceOrder(paymentDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }



        return paymentDetails;
    }

    @Override
    public PaymentDetails getPaymentDetails(String paymentAdviceNumber) throws ParseException, OperationException
    {
        PaymentCriteria paymentCriteria = new PaymentCriteria();
        paymentCriteria.setPaymentAdviceNumber(paymentAdviceNumber);

        PaymentDetails paymentDetails = paymentDetailsRepository.serachPaymentDetails(paymentAdviceNumber);
        if (paymentDetails == null)
        {
            paymentDetails = new PaymentDetails();
            paymentCriteria.setSearchByPaymentAdviceNumber(true);
            PaymentAdvice advice = paymentAdviceRepository.searchPaymentAdvise(paymentCriteria).get(0);
            if (advice!=null)
            {
                if (PaymentAdviceStatusValues.APPROVED.equals(advice.getPaymentAdviceStatus()))
                {
                    if (advice.isPayToSupplier()) {
                        paymentDetails.setPaymentDetailsType(PaymentDetailsType.PAY_TO_SUPPLIER);
                        paymentDetails.setPayToSupplierSet(new ArrayList<>(1));
                        paymentDetails.setPaymentAdviceNumber(advice);
                        return paymentDetails;
                    }
                    if (advice.isGuaranteeToSupplier()) {
                        paymentDetails.setPaymentDetailsType(PaymentDetailsType.GUARANTEE_TO_SUPPLIER);
                        paymentDetails.setGuaranteeToSupplier(new GuaranteeToSupplier());
                        paymentDetails.setPaymentAdviceNumber(advice);
                        return paymentDetails;
                    }
                }
                else
                {
                    logger.debug("Payment Advice is in "+advice.getPaymentAdviceStatus()+".So you can not generate Payment Details");
                    throw new OperationException("Payment Details can not generate until Payment Advice is Approved.");
                }
            }
        }
        return paymentDetails;
    }

    @Override
    public BigDecimal getRoe(String supplierCurrency, String remittanceCurrency, String dateOfPayment, String supplierId, String roeType) throws OperationException {
        BigDecimal roeValue = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ROE.fromCurrency", supplierCurrency);
        jsonObject.put("ROE.toCurrency", remittanceCurrency);
        jsonObject.put("roeType", roeType);
        JSONArray dateJson = new JSONArray();
        JSONObject effectiveFrom = new JSONObject();
        JSONObject lteEffectiveFrom = new JSONObject();
        lteEffectiveFrom.put("$lte", dateOfPayment);
        effectiveFrom.put("effectiveFrom", lteEffectiveFrom);

        JSONObject effectiveTo = new JSONObject();
        JSONObject gteEffectiveTo = new JSONObject();
        gteEffectiveTo.put("$gte", dateOfPayment);
        effectiveTo.put("effectiveTo", gteEffectiveTo);

        dateJson.put(effectiveFrom);
        dateJson.put(effectiveTo);

        jsonObject.put("$and", dateJson);
        jsonObject.put("supplier.id", supplierId);


        String response = null;
        try {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(this.supplierROEUrl + "?filter=" + jsonObject);
            URI uri = URI.create(uriComponentsBuilder.toUriString());

            response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);

            List<ROE> roeList = jsonObjectProvider.getChildrenCollection(response, "$.data[0].ROE.*", ROE.class);
            if (roeList == null) {
                throw new OperationException("Unable to get ROE from mdm");
            }
            for (ROE roe : roeList) {
                if (roe.getFromCurrency().equalsIgnoreCase(supplierCurrency) && roe.getToCurrency().equalsIgnoreCase(remittanceCurrency)) {
                    roeValue = roe.getRoe();
                    break;
                }
            }
        } catch (Exception e) {
            throw new OperationException("Unable to get ROE from mdm");
        }

        return roeValue;
    }

    @Override
    public PaymentDetails getPaymentDetailsById(String id) {


        return paymentDetailsRepository.getPaymentDetailsById(id);
    }

    @Override
    public EmailResponse sendAnEmailToSupplier(PaymentDetails paymentDetails) throws OperationException {
        URI url = UriComponentsBuilder.fromUriString(emailWithBody).build().encode().toUri();
        EmailResponse emailResponse = emailUtils.buildClientMail(function, scenario,
                getSupplierEmailId(paymentDetails.getPaymentAdviceNumber().getSupplierName()),
                subject, getHtmlBody(paymentDetails), null, null);
        return emailResponse;
    }

    private Map<String,String> getHtmlBody(PaymentDetails paymentDetails) throws OperationException
    {

        Map<String, String> dynamicVariables = new HashMap<>();
        String header = new String();
        if (PaymentDetailsType.PAY_TO_SUPPLIER.equals(paymentDetails.getPaymentDetailsType())) {
            header = "<table style=\"width:100%;border: 1px solid black;border-collapse: collapse;\">\n" +
                    "  <tr>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Payment Type</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Mode Of Payment</th> \n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Supplier Currency</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Payment Reference Number</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">SAP Refernce Number</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Date of Payment</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Remittance Curreny</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Amount Remittance to Supplier</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">MSF & Remittance Charges</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Amount Payable for Supplier</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">ROE</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Total Amount Remittance</th>\n" +
                    "  </tr>\n" +
                    "  ";
            for (PayToSupplier payToSupplier : paymentDetails.getPayToSupplierSet()) {
                header = header.concat(
                        "<tr>\n" +
                                "    <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + paymentDetails.getPaymentDetailsType() + "</td>\n" +
                                "    \n" +
                                "    <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + payToSupplier.getModeOfPayment() + "</td>\n" +
                                "    \n" +
                                "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + payToSupplier.getSupplierCurrency() + "</td>\n" +
                                "     \n" +
                                "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + payToSupplier.getPaymentReferenceNumber() + "</td>\n" +
                                "     \n" +
                                "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + payToSupplier.getSapReferenceNumber() + "</td>\n" +
                                "     \n" +
                                "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + payToSupplier.getDateOfPayment() + "</td>\n" +
                                "     \n" +
                                "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + payToSupplier.getRemittanceCurrency() + "</td>\n" +
                                "     \n" +
                                "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + payToSupplier.getAmountRemittedToSupplier() + "</td>\n" +
                                "     \n" +
                                "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + payToSupplier.getMsfAndRemittanceCharges() + "</td>\n" +
                                "     \n" +
                                "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + payToSupplier.getAmountPayableForSupplier() + "</td>\n" +
                                "     \n" +
                                "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + payToSupplier.getRoe() + "</td>\n" +
                                "     \n" +
                                "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + payToSupplier.getTotalAmountRemitted() + "</td>\n" +
                                "    \n" +
                                "  </tr>");

            }
        } else if (PaymentDetailsType.GUARANTEE_TO_SUPPLIER.equals(paymentDetails.getPaymentDetailsType())) {
            GuaranteeToSupplier guaranteeToSupplier = paymentDetails.getGuaranteeToSupplier();
            header = "<table style=\"width:100%;border: 1px solid black;border-collapse: collapse;\">\n" +
                    "  <tr>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Payment Type</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Mode Of Payment</th> \n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Date of Guarantee</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Supplier Currency</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Guarantee for Amount to Supplier</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Credit Card Name</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Credit Card Number</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Card Type</th>\n" +
                    "    <th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Credit Card Expriry Due Date</th>\n" +
                    "  </tr>\n" +
                    "  ";
            header = header.concat(
                    "<tr>\n" +
                            "    <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + paymentDetails.getPaymentDetailsType() + "</td>\n" +
                            "    \n" +
                            "    <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + guaranteeToSupplier.getModeOfPayment() + "</td>\n" +
                            "    \n" +
                            "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(guaranteeToSupplier.getDateOfGuarantee()) + "</td>\n" +
                            "     \n" +
                            "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + guaranteeToSupplier.getSupplierCurrency() + "</td>\n" +
                            "     \n" +
                            "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + guaranteeToSupplier.getGuaranteeForAmtToSupplier() + "</td>\n" +
                            "     \n" +
                            "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + guaranteeToSupplier.getCreditCard().getNameOnCard() + "</td>\n" +
                            "     \n" +
                            "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + guaranteeToSupplier.getCreditCard().getCardNumber() + "</td>\n" +
                            "     \n" +
                            "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + guaranteeToSupplier.getCreditCard().getCardType() + "</td>\n" +
                            "     \n" +
                            "     <td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(guaranteeToSupplier.getCreditCard().getExpiryOfCard()) + "</td>\n" +
                            "     \n" +
                            "  </tr>");
        } else {
            logger.info("No Payment Type Found");
        }
        dynamicVariables.put(firstName,paymentDetails.getPaymentAdviceNumber().getSupplierName());
        dynamicVariables.put(htmlTemplate,header);
        return dynamicVariables;
    }

    private NotificationResource sendAAlertToExtranetSuuplier(PaymentDetails paymentDetails)
    {
        InlineMessageResource inlineMessageResource = new InlineMessageResource();
        NotificationResource notificationResource = null;
        inlineMessageResource.setAlertName(supplierAlertName);
        inlineMessageResource.setNotificationType(notificationType);
        ConcurrentHashMap<String, String> dynamicVariables = new ConcurrentHashMap<>();
        if (paymentDetails.getPaymentAdviceNumber().getPaymentAdviceStatementInfoSet().isEmpty()) {
            dynamicVariables.put("bookingRefNo", paymentDetails.getPaymentAdviceNumber().getPaymentAdviceOrderInfoSet().iterator().next().getBookingRefId());
            dynamicVariables.put("orderId", paymentDetails.getPaymentAdviceNumber().getPaymentAdviceOrderInfoSet().iterator().next().getOrderId());
        }
        dynamicVariables.put("paymentAdviceNumber", paymentDetails.getPaymentAdviceNumber().getPaymentAdviceNumber());
        inlineMessageResource.setDynamicVariables(dynamicVariables);
        try {
            notificationResource = alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return notificationResource;
    }

    private NotificationResource sendAnAlertToOpsUser(PaymentDetails paymentDetails)
    {
        InlineMessageResource inlineMessageResource = new InlineMessageResource();
        NotificationResource notificationResource = null;
        inlineMessageResource.setAlertName(opsUserAlertName);
        inlineMessageResource.setNotificationType(notificationType);
        ConcurrentHashMap<String, String> dynamicVariables = new ConcurrentHashMap<>();
        dynamicVariables.put("paymentAdviceNumber", paymentDetails.getPaymentAdviceNumber().getPaymentAdviceNumber());
        if (paymentDetails.getPaymentAdviceNumber().getPaymentAdviceStatementInfoSet().isEmpty()) {
            dynamicVariables.put("bookingRefNo", paymentDetails.getPaymentAdviceNumber().getPaymentAdviceOrderInfoSet().iterator().next().getBookingRefId());
            dynamicVariables.put("orderId", paymentDetails.getPaymentAdviceNumber().getPaymentAdviceOrderInfoSet().iterator().next().getOrderId());
        }
        inlineMessageResource.setDynamicVariables(dynamicVariables);
        try {
            notificationResource = alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return notificationResource;
    }

    private String getSupplierEmailId(String supplierName) throws OperationException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("supplier.name",supplierName);
        String url = extranetAccess+jsonObject.toString()+"&select=contactInfo.contactDetails.email";
        URI toUri = UriComponentsBuilder.fromUriString(url).build().toUri();
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = mdmRestUtil.exchange(toUri, HttpMethod.GET,null,String.class);
        } catch (OperationException e) {
            throw  new OperationException("Email id is not configured for Supplier "+supplierName);
        }
        String responseInString = responseEntity.getBody();
        String emailId = jsonObjectProvider.getAttributeValue(responseInString,"$.data[0].contactInfo.contactDetails.email",String.class);
        return emailId;
    }
}

