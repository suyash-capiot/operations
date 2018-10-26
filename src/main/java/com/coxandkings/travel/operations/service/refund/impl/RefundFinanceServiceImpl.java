package com.coxandkings.travel.operations.service.refund.impl;

import com.coxandkings.travel.ext.model.finance.Enums.FundType;
import com.coxandkings.travel.ext.model.finance.Enums.NoteType;
import com.coxandkings.travel.ext.model.finance.creditdebitnote.CostHeaderCharge;
import com.coxandkings.travel.ext.model.finance.creditdebitnote.CreditDebitNote;
import com.coxandkings.travel.ext.model.finance.invoice.InvoiceDto;
import com.coxandkings.travel.operations.enums.debitNote.NotePhase;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.refund.finaceRefund.RefundClaim;
import com.coxandkings.travel.operations.model.refund.finaceRefund.RefundProcess;
import com.coxandkings.travel.operations.model.refund.finaceRefund.RefundReason;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.refund.RefundFinanceService;
import com.coxandkings.travel.operations.systemlogin.MDMDataSource;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class RefundFinanceServiceImpl implements RefundFinanceService {

    private static final Logger logger = LogManager.getLogger(RefundFinanceServiceImpl.class);

    @Value("${refund.finance.invoice.get_invoice}")
    private String invoiceCheckAPIUrl;
    @Autowired
    private AlertService alertService;

    @Value("${refund.finance.finance-refund.save-refund-claim}")
    private String saveRefundClaimUrl;
    @Value("${refund.finance.finance-refund.get-claim-by-id}")
    private String getRefundClaimById;
    @Value("${refund.finance.finance-refund.updated_refund_claim}")
    private String updatedRefundClaimUrl;
    @Value("${refund.finance.finance-refund.refund_by_booking_reference}")
    private String refundByBookingReference;

    @Value("${refund.finance.credit_debit_note.get_credit_note}")
    private String getCreditNoteUrlByNoteNo;

    @Value("${refund.finance.credit_debit_note.get_credit_note_id}")
    private String getCreditNoteUrlById;

    @Value("${refund.finance.credit_debit_note.create_credit_debit_note}")
    private String creditDebitMemoAPIUrl;

    @Autowired
    @Qualifier(value = "mDMDataSource")
    private MDMDataSource mdmDataSource;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Value("${refund.finance.my-account.get-balance}")
    private String getMyAccountBalanceUrl;


    @PostConstruct
    public void init() {
        logger.info("RefundFinanceServiceImpl: MDM Datasource hahscode is: " + mdmDataSource.hashCode());
        logger.debug("refund_url: " + saveRefundClaimUrl);
        logger.debug("GetRefundClaimById: " + getRefundClaimById);
        logger.debug("updated_refund_claim: " + updatedRefundClaimUrl);
        logger.debug("Credit Note Url: " + getCreditNoteUrlById);
    }

    @Override
    public RefundClaim updateRefundClaim(RefundClaim refundClaim) throws OperationException {
        RefundClaim responseRefundClaim;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        ResponseEntity<RefundClaim> response = null;

        try {
            response = mdmRestUtils.exchange(UriComponentsBuilder.fromUriString(updatedRefundClaimUrl + refundClaim.getId()).build().toUri(), HttpMethod.PUT, refundClaim, RefundClaim.class);
        } catch (Exception e) {
            logger.error("While accessing URL: " + updatedRefundClaimUrl + " got error", e);
            throw new OperationException("Unable to update refundClaim in refund");
        }
        if (response != null) {

            responseRefundClaim = response.getBody();

        } else {
            logger.error(updatedRefundClaimUrl + " not able to get any data");
            throw new OperationException(updatedRefundClaimUrl + " not able to get any data");
        }
        return responseRefundClaim;
    }

    public BigDecimal getMyAccountBalance(RefundClaim refundClaim) throws OperationException {

        String uri = "?companyId=" + refundClaim.getCompanyDetail().getCompanyId() + "&entityType=CLIENT&entityId=" + refundClaim.getClientDetail().getClientId();
        UriComponentsBuilder componentsBuilder = UriComponentsBuilder.fromUriString(getMyAccountBalanceUrl + uri);
        BigDecimal accountBalance = null;
        try {
            String amount = mdmRestUtils.exchange(componentsBuilder.build().toUri(), HttpMethod.GET, String.class);
            String attributeValue = jsonObjectProvider.getAttributeValue(amount, "$." + refundClaim.getClientDetail().getClientId(), BigDecimal.class);
            if (StringUtils.isEmpty(attributeValue)) {
                accountBalance = BigDecimal.valueOf(0.0);

            } else {
                accountBalance = new BigDecimal(attributeValue);
            }
        } catch (Exception e) {
            logger.error("Unable to get client my account balance");
            throw new OperationException("Unable to get client my account balance");
        }


        return accountBalance;
    }

    public RefundClaim getRefundClaim(String claimNo) throws OperationException {
        RefundClaim refundClaim = null;
        try {
            refundClaim = mdmRestUtils.getForObject(getRefundClaimById + claimNo, RefundClaim.class, claimNo);
        } catch (Exception e) {
            logger.error("Unable to get data for Claim No " + claimNo, e);
            throw new OperationException("Unable to get data for claim No " + claimNo);


        }

        return refundClaim;
    }

    @Override
    public RefundClaim saveRefundClaim(RefundProcess refundProcess) throws OperationException {
        ResponseEntity<RefundClaim> refundClaimResponseEntity = null;
        try {
            refundClaimResponseEntity = mdmRestUtils.postForEntity(saveRefundClaimUrl, refundProcess, RefundClaim.class);
        } catch (Exception e) {
            logger.error("Unable to save RefundClaim record", e);
            throw new OperationException("Unable to save RefundClaim");
        }
        logger.debug(refundClaimResponseEntity.getBody());
        return refundClaimResponseEntity.getBody();
    }


    public List<RefundClaim> getRefundByBookingReferenceNo(String bookingReferenceNo, RestTemplate restTemplate) throws OperationException {
        ResponseEntity<List<RefundClaim>> refundClaimResponse;
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(refundByBookingReference + bookingReferenceNo).build();


        try {
            refundClaimResponse = mdmRestUtils.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<RefundClaim>>() {
            });


        } catch (Exception e) {
            logger.error("Unable to get data from URL" + uriComponents.toUri(), e);
            throw new OperationException(Constants.ER01, "Unable to get data from finance: ");
        }
        return refundClaimResponse.getBody();
    }

    @Override
    public Boolean isPromoUsed(String claimNo) throws OperationException {
        URI url = UriComponentsBuilder.fromUriString(getCreditNoteUrlByNoteNo + claimNo).build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = null;
        try {
            response = mdmRestUtils.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            logger.error("Not able to get data " + url);
            throw new OperationException("Not able to get Credit note details " + claimNo);
        }
        String jsonResponse = response.getBody();
        return (Boolean) jsonObjectProvider.getChildObject(jsonResponse, "$.consumed", Boolean.class);


    }

    public String getGetCreditNote(String creditNoteNo) throws OperationException {
        URI url = UriComponentsBuilder.fromUriString(getCreditNoteUrlByNoteNo + creditNoteNo).build().encode().toUri();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", mdmDataSource.getToken().getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = null;

        try {
            response = mdmRestUtils.exchange(url, HttpMethod.GET, null, String.class);
        } catch (Exception e) {
            logger.error("Not able to get data " + url);
            throw new OperationException("Not able to get data from finance for credit note ");
        }
        return response.getBody();

    }

    public CreditDebitNote updateCreditNote(CreditDebitNote creditDebitNote) throws OperationException {


        UriComponentsBuilder componentsBuilder = UriComponentsBuilder.fromUriString(creditDebitMemoAPIUrl + "/" + creditDebitNote.getId());
        ResponseEntity<CreditDebitNote> exchange = null;
        try {
            exchange = mdmRestUtils.exchange(componentsBuilder.build().encode().toUri(), HttpMethod.PUT, creditDebitNote, CreditDebitNote.class);
        } catch (Exception e) {
            throw new OperationException("Unable to update credit note");
        }
        return exchange.getBody();
    }

    public InvoiceDto getInvoicesByBookingDetails(String bookingNo, String orderId) throws OperationException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(invoiceCheckAPIUrl)
                .queryParam("bookingRef", bookingNo)
                .queryParam("orderID", orderId);
        InvoiceDto invoice = null;
        try {
            InvoiceDto[] invoiceDtos = mdmRestUtils.getForObject(builder.toUriString(), InvoiceDto[].class);
            invoice = invoiceDtos[0];
        } catch (HttpServerErrorException he) {
            logger.error("500 Internal Server in Finance", he);
            throw new OperationException("500 Internal Serve in finance");
        } catch (Exception e) {
            logger.error("Unable to get data from finance for invoice", e);
            throw new OperationException(Constants.OPS_ERR_30213);
        }


        return invoice;
    }

    @Override
    public CreditDebitNote createCreditDebitNote(RefundProcess refundProcess) throws OperationException {
        CreditDebitNote creditDebitNote = null;
        ResponseEntity<CreditDebitNote> creditDebitNoteResponseEntity = null;
        try {

            InvoiceDto invoicesByBookingDetails = getInvoicesByBookingDetails(refundProcess.getBookingReferenceNumber(), refundProcess.getProductDetail().getOrderId());


            creditDebitNote = new CreditDebitNote();
            creditDebitNote.setBookingRefNumber(Collections.singleton(refundProcess.getBookingReferenceNumber()));
            creditDebitNote.setInvoiceNumber(invoicesByBookingDetails.getInvoiceNumber());
            creditDebitNote.setTypeOfInvoice(invoicesByBookingDetails.getInvoiceType().toString());
            creditDebitNote.setNoteType(NoteType.CREDIT.toString());
            creditDebitNote.setClient_supplierID(refundProcess.getClientDetail().getClientId());
            creditDebitNote.setNotePhase(NotePhase.Final.toString());
            creditDebitNote.setNoteType(NoteType.CREDIT.toString());
            creditDebitNote.setClientType(refundProcess.getClientDetail().getClientType().getClientType());

            creditDebitNote.setIssuedDate(new Date().getTime());
            creditDebitNote.setIssuedTo("CLIENT");
            creditDebitNote.setClient_supplierName(refundProcess.getClientDetail().getClientName());
            creditDebitNote.setCurrency(refundProcess.getRefundDetail().getClaimCurrency());
            creditDebitNote.setRoe(refundProcess.getRefundDetail().getClaimROE().doubleValue());
            creditDebitNote.setStatus("APPROVED");
            creditDebitNote.setTransactionDate(new Date().getTime());
            creditDebitNote.setFundType(FundType.REDEEMABLE.toString());
            creditDebitNote.setTaxApplicable(false);
            creditDebitNote.setClientType(refundProcess.getClientDetail().getClientType().toString());
            creditDebitNote.setCompanyId(refundProcess.getCompanyDetail().getCompanyId());
            creditDebitNote.setTaxType("NA");
            creditDebitNote.setConsumed(false);

            CostHeaderCharge costHeaderAd = new CostHeaderCharge();
            costHeaderAd.setPassengerType("ADULT");

            if (RefundReason.Compensation.equals(refundProcess.getRefundDetail().getRefundReason())) {
                costHeaderAd.setCostHeader("SERVICE COMPLAINTS COMPENSATIONS");
            } else if (RefundReason.Reduction.equals(refundProcess.getRefundDetail().getRefundReason())) {
                costHeaderAd.setCostHeader("Reduction in Services");
            } else if (RefundReason.Discount.equals(refundProcess.getRefundDetail().getRefundReason())) {
                costHeaderAd.setCostHeader("Discounts/surcharges");
            }

            costHeaderAd.setAmount(refundProcess.getRefundDetail().getRefundAmount().doubleValue());
            costHeaderAd.setPassengerQuantity(1);
            costHeaderAd.setTotalAmount(refundProcess.getRefundDetail().getRefundAmount().doubleValue());
            List<CostHeaderCharge> chargesEntities = new ArrayList<>();
            chargesEntities.add(costHeaderAd);

            creditDebitNote.setCostHeaderCharges(chargesEntities);

            creditDebitNote.setProductCategory(refundProcess.getProductDetail().getProductCategory());
            creditDebitNote.setProductCategorySubType(refundProcess.getProductDetail().getProductCategorySubType());
            creditDebitNote.setProductName(refundProcess.getProductDetail().getProductName());
            creditDebitNote.setProductSubName(refundProcess.getProductDetail().getProductSubName());

            creditDebitNote.setTotalAmount(refundProcess.getRefundDetail().getNetAmountPayable().doubleValue());
            creditDebitNote.setCreatedBy(refundProcess.getCreatedBy());
            creditDebitNote.setLinkedNoteNumber(invoicesByBookingDetails.getInvoiceNumber());
            creditDebitNote.setLinkedNoteType("INVOICE");
            creditDebitNoteResponseEntity = mdmRestUtils.postForEntity(creditDebitMemoAPIUrl, creditDebitNote, CreditDebitNote.class);


        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException(Constants.DEBIT_NOTE_CREATION_FAIL);
        }
        return creditDebitNoteResponseEntity.getBody();


    }


}