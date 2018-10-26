package com.coxandkings.travel.operations.service.accountsummary;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.helper.booking.payment.AccountSummary;
import com.coxandkings.travel.operations.model.accountsummary.*;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.GuaranteeToSupplier;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PayToSupplier;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PaymentDetails;
import com.coxandkings.travel.operations.model.refund.Refund;
import com.coxandkings.travel.operations.model.refund.finaceRefund.InvoiceDetail;
import com.coxandkings.travel.operations.model.refund.finaceRefund.TypeOFNote;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentdetails.PaymentDetailsRepository;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviseBELoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviseMDMLoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.AutomaticPaymentAdviceGenerationService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.PaymentAdviceLoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.PaymentAdviceService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentdetails.PaymentDetailsService;
import com.coxandkings.travel.operations.service.refund.RefundService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.FinalServiceOrderService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ProvisionalServiceOrderService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AccountSummaryServiceImpl implements AccountSummaryService {

    @Value( "${account_overview.get_booking_invoices}" )
    private String GetInvoicesForBookingURL;

    @Value( "${account_overview.get_invoice_receipts}" )
    private String GetInvoicePaymentsURL;

    @Value( "${account_overview.get_credit_debit_note_for_invoice}" )
    private String GetCreditDebitNoteForInvoice;

    @Autowired
    private RefundService refundService;

    @Autowired
    private JsonObjectProvider jsonFilter;

    @Autowired
    private PaymentAdviceService paymentAdviceService;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private OpsBookingService bookingService;

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @Autowired
    private PaymentAdviseBELoaderService paymentAdviseBELoaderService;

    @Autowired
    private PaymentAdviseMDMLoaderService paymentAdviseMDMLoaderService;

    @Autowired
    private AutomaticPaymentAdviceGenerationService automaticPaymentAdvice;

    @Autowired
    private PaymentAdviceLoaderService paymentAdviceLoaderService;

    @Autowired
    private UserService userService;

    @Autowired
    private FinalServiceOrderService finalServiceOrderService;

    @Autowired
    private ProvisionalServiceOrderService provisionalServiceOrderService;


    private static Logger logger = LogManager.getLogger( AccountSummaryServiceImpl.class );

    @Override
    public List<InvoiceBasicInfo> getInvoiceInfoForBooking( String bookingID ) throws OperationException {

        logger.info("Entering getInvoiceInfoForBooking() method " + bookingID);
        List<InvoiceBasicInfo> invoiceInfoForBooking = new ArrayList<>();
        try {
            logger.info("Invoking Finance for Account summary info: \n" +
                    GetInvoicesForBookingURL + bookingID);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GetInvoicesForBookingURL + bookingID);
            ResponseEntity<String> invoiceDetails = RestUtils.exchange(builder.toUriString(), HttpMethod.GET, getHttpEntity(), String.class);
            String invoiceDetailsResponse = invoiceDetails.getBody();

            if (invoiceDetailsResponse != null && invoiceDetailsResponse.trim().length() > 0) {
                List<String> invoiceDetailsList = jsonFilter.getChildrenCollection(invoiceDetailsResponse, "$", String.class);
                for (String aInvoiceDetailsResponse : invoiceDetailsList) {
                    String invoiceNumber = jsonFilter.getAttributeValue(aInvoiceDetailsResponse, "$.invoiceNumber", String.class);
                    String invoiceDate = jsonFilter.getAttributeValue(aInvoiceDetailsResponse, "$.createdOn", String.class);
                    String invoiceAmount = jsonFilter.getAttributeValue(aInvoiceDetailsResponse, "$.totalCost", String.class);
                    String invoiceCurrency = jsonFilter.getAttributeValue(aInvoiceDetailsResponse, "$.invoiceCurrency", String.class);
                    String outStandingAmount = jsonFilter.getAttributeValue(aInvoiceDetailsResponse, "$.outStandingAmount", String.class);;
                    InvoiceBasicInfo aInvoiceBasicInfo = new InvoiceBasicInfo();
                    aInvoiceBasicInfo.setInvoiceID(invoiceNumber);
                    aInvoiceBasicInfo.setInvoiceDate(invoiceDate);
                    aInvoiceBasicInfo.setInvoiceAmount(invoiceAmount);
                    aInvoiceBasicInfo.setInvoiceCurrency(invoiceCurrency);
                    aInvoiceBasicInfo.setOutStandingAmount(outStandingAmount);
                    invoiceInfoForBooking.add(aInvoiceBasicInfo);

                    List<InvoicePaymentInfo> invoicePaymentInfoList = getInvoicePaymentInfo(invoiceNumber);
                    aInvoiceBasicInfo.setInvoicePayments(invoicePaymentInfoList);

                    List<InvoiceCreditDebitNoteInfo> creditDebitNotesList = getCreditDebitNoteInfoForInvoice(invoiceNumber);
                    aInvoiceBasicInfo.setInvoiceCreditDebitNotes(creditDebitNotesList);

                    List<InvoiceRefundInfo> invoiceRefundsList = getInvoiceRefundInfo(bookingID, invoiceNumber);
                    aInvoiceBasicInfo.setInvoiceRefunds(invoiceRefundsList);
                }
            }
        }
        catch( Exception e )    {
            //e.printStackTrace();
            logger.error( "Error occurred while loading Account Summary details", e );
        }

        return invoiceInfoForBooking;
    }

    @Override
    public List<InvoicePaymentInfo> getInvoicePaymentInfo( String invoiceID ) throws OperationException {
        logger.info("Entering getInvoicePaymentInfo() method " + invoiceID);
        List<InvoicePaymentInfo> invoicePaymentsList = new ArrayList<>();
        logger.info("URL to Finance is:" + GetInvoicePaymentsURL + invoiceID);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString( GetInvoicePaymentsURL + invoiceID );
        ResponseEntity<String> receiptDetailsRes = RestUtils.exchange( builder.toUriString(), HttpMethod.GET, getHttpEntity(), String.class );
        String receiptDetailsResponse = receiptDetailsRes.getBody();
        if( receiptDetailsResponse != null && receiptDetailsResponse.trim().length() > 0 )  {
            List<String> receiptDetailsListResponse = jsonFilter.getChildrenCollection( receiptDetailsResponse, "$.content", String.class );
            if( receiptDetailsListResponse != null && receiptDetailsListResponse.size() > 0 )   {
                for( String aReceiptDetailsInfo : receiptDetailsListResponse )   {
                    if( aReceiptDetailsInfo != null && aReceiptDetailsInfo.trim().length() > 0 ) {

                        // Get Payment towards
                        String paymentTowards = jsonFilter.getAttributeValue(aReceiptDetailsInfo, "$.receiptPurpose", String.class);
                        paymentTowards = (paymentTowards == null) ? new String() : paymentTowards;

                        // Get Payment Date
                        String paymentDate = jsonFilter.getAttributeValue(aReceiptDetailsInfo, "$.paymentEntity.transactionDate", String.class);
                        if (paymentDate != null && paymentDate.trim().length() > 0) {
                            Instant paymentDateInstant = Instant.ofEpochMilli(Long.parseLong(paymentDate));
                            ZonedDateTime paymentDateDT = ZonedDateTime.ofInstant(paymentDateInstant, ZoneId.systemDefault());
                            String opsDateFormat = "yyyy-MM-dd HH:mm";
                            DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern(opsDateFormat);
                            paymentDate = dtFormatter.format(paymentDateDT);
                        }

                        // Get payment method
                        String paymentMethod = jsonFilter.getAttributeValue(aReceiptDetailsInfo, "$.paymentEntity.paymentModeType", String.class);
                        paymentMethod = (paymentMethod == null) ? new String() : paymentMethod;

                        // Get payment status
                        String paymentStatus = jsonFilter.getAttributeValue(aReceiptDetailsInfo, "$.paymentEntity.paymentStatus", String.class);
                        paymentStatus = (paymentStatus == null) ? new String() : paymentStatus;

                        // Get receipt number
                        String receiptNumber = jsonFilter.getAttributeValue(aReceiptDetailsInfo, "$.receiptNumber", String.class);
                        receiptNumber = (receiptNumber == null) ? new String() : receiptNumber;

                        List<String> receiptApplicationEntitySet = jsonFilter.getChildrenCollection( aReceiptDetailsInfo, "$.receiptApplicationEntitySet", String.class );

                        if( receiptApplicationEntitySet != null && receiptApplicationEntitySet.size() > 0 ) {
                            for( String aReceiptPaymentInfo : receiptApplicationEntitySet ) {
                                if( aReceiptPaymentInfo != null && aReceiptPaymentInfo.trim().length() > 0 ) {
                                    String invoiceIDFromAReceiptEntity = jsonFilter.getAttributeValue( aReceiptPaymentInfo, "$.id", String.class);
                                    if( invoiceIDFromAReceiptEntity != null && invoiceIDFromAReceiptEntity.trim().length() > 0 ) {
                                        if( invoiceIDFromAReceiptEntity.equalsIgnoreCase( invoiceID )) {

                                            // Get payment amount
                                            String paymentAmount = jsonFilter.getAttributeValue(aReceiptPaymentInfo, "$.appliedAmount", String.class);
                                            if (paymentAmount == null || paymentAmount.trim().length() == 0) {
                                                paymentAmount = new String();
                                            }

                                            // Get payment currency
                                            String paymentCurrency = jsonFilter.getAttributeValue(aReceiptPaymentInfo, "$.currency", String.class);
                                            if (paymentCurrency == null || paymentCurrency.trim().length() == 0) {
                                                paymentCurrency = new String();
                                            }

                                            InvoicePaymentInfo anInvoicePaymentInfo = new InvoicePaymentInfo();
                                            anInvoicePaymentInfo.setPaymentDate(paymentDate);
                                            anInvoicePaymentInfo.setPaymentStatus(paymentStatus);
                                            anInvoicePaymentInfo.setPaymentTowards(paymentTowards);
                                            anInvoicePaymentInfo.setReceiptNumber(receiptNumber);
                                            anInvoicePaymentInfo.setRefundable(false);// not sure why this is required?
                                            anInvoicePaymentInfo.setPaymentCurrency(paymentCurrency);
                                            anInvoicePaymentInfo.setPaymentCollection(paymentCurrency + " " + paymentAmount + ", " + paymentDate + ", " + paymentMethod);
                                            invoicePaymentsList.add(anInvoicePaymentInfo);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return invoicePaymentsList;
    }

    @Override
    public List<InvoiceCreditDebitNoteInfo> getCreditDebitNoteInfoForInvoice(String invoiceID) throws OperationException {
        logger.info("Entering getCreditDebitNoteInfoForInvoice() method " + invoiceID);
        List<InvoiceCreditDebitNoteInfo> creditDebitNotesList = new ArrayList<>();
        logger.info("*** URL to Finance is::\n " + GetCreditDebitNoteForInvoice + invoiceID);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString( GetCreditDebitNoteForInvoice + invoiceID );
        ResponseEntity<String> creditDebitNotesRes = RestUtils.exchange( builder.toUriString(), HttpMethod.GET, getHttpEntity(), String.class);
        String creditDebitNotesResponse = creditDebitNotesRes.getBody();
        if( creditDebitNotesResponse != null && creditDebitNotesResponse.trim().length() > 0 )  {
            List<String> creditDebitNotesDataList = jsonFilter.getChildrenCollection( creditDebitNotesResponse, "$", String.class );
            if( creditDebitNotesDataList != null && creditDebitNotesDataList.size() > 0 )   {
                for( String aCreditDebitNotesData : creditDebitNotesDataList )  {
                    if( aCreditDebitNotesData != null && aCreditDebitNotesData.trim().length() > 0 ) {
                        InvoiceCreditDebitNoteInfo aNoteInfo = new InvoiceCreditDebitNoteInfo();
                        String noteNumber = jsonFilter.getAttributeValue(aCreditDebitNotesData, "$.noteNumber", String.class);
                        String noteType = jsonFilter.getAttributeValue(aCreditDebitNotesData, "$.noteType", String.class);
                        aNoteInfo.setCreditDebitNoteNumber( noteNumber );
                        aNoteInfo.setNoteType( noteType );
                        creditDebitNotesList.add( aNoteInfo );
                    }
                }
            }
        }

        return creditDebitNotesList;
    }

    @Override
    public List<InvoiceRefundInfo> getInvoiceRefundInfo( String bookID, String invoiceID ) throws OperationException {
        logger.info("Entering getInvoiceRefundInfo() method - Booking ID" + bookID + " Invoice ID:: " + invoiceID);
        List<InvoiceRefundInfo> accSummaryRefundsList = new ArrayList<>();
        List<Refund> bookingRefundsList = refundService.refundClaimsByBookingReferenceNo( bookID );
        if( bookingRefundsList != null && bookingRefundsList.size() > 0 )   {
            for( Refund aRefund : bookingRefundsList )  {
                Set<InvoiceDetail> refundInvoices = aRefund.getInvoiceDetail();
                for( InvoiceDetail aInvDetail : refundInvoices )    {
                    String tmpInvoiceID = aInvDetail.getInvoiceOrCreditNote();
                    if( tmpInvoiceID.equalsIgnoreCase( invoiceID ) && (aInvDetail.getTypeOfNote().equals(TypeOFNote.INV )) )   {
                        // Matching Claim found for input Invoice!!
                        InvoiceRefundInfo aRefundInfo = new InvoiceRefundInfo();
                        aRefundInfo.setClaimID( aRefund.getClaimNo() );
                        aRefundInfo.setAmount( aRefund.getRefundAmount().toPlainString() );
                        aRefundInfo.setCurrency( aRefund.getClaimCurrency() );
                        aRefundInfo.setStatus( aRefund.getRefundStatus().getStatus() );
                        accSummaryRefundsList.add( aRefundInfo );
                    }
                }
            }
        }
        return accSummaryRefundsList;
    }

    @Override
    public List<BookingPaymentAdviseInfo> loadPaymentAdvisesForBooking( OpsBooking aBooking) throws OperationException, IOException {
        logger.info("Entering loadPaymentAdvisesForBooking() method - Booking ID" + aBooking.getBookID() );
        BookingPaymentAdviseInfo bookingPaymentAdviseInfo = null;
        List<BookingPaymentAdviseInfo> bookingPaymentAdviseInfoList = new ArrayList<>();
        String bookingID = aBooking.getBookID();
        OpsBookingStatus status = aBooking.getStatus();
//        List<BookingPaymentAdviseInfo> paymentAdviseInfoList = new ArrayList<>();
//        try {
//            PaymentCriteria paymentSearchCriteria = new PaymentCriteria();
//            paymentSearchCriteria.setBookingRefId(bookingID);
//            List<PaymentAdvice> paymentAdvisesList = paymentAdviceService.searchSupplierPayment(paymentSearchCriteria);
//
//            if (paymentAdvisesList != null && paymentAdvisesList.size() > 0) {
//                //to group by suppliers
//                HashMap<String, ArrayList<PaymentAdvice>> supplierPaymentsMap = new HashMap<>();
//                for (PaymentAdvice aPaymentAdvise : paymentAdvisesList) {
//                    String supplierID = aPaymentAdvise.getSupplierRefId();
//                    if (supplierPaymentsMap.containsKey(supplierID)) {
//                        ArrayList<PaymentAdvice> paymentAdvisesForSupplier = supplierPaymentsMap.get(supplierID);
//                        paymentAdvisesForSupplier.add(aPaymentAdvise);
//                    } else {
//                        ArrayList<PaymentAdvice> paymentAdvisesForSupplier = new ArrayList<>();
//                        paymentAdvisesForSupplier.add(aPaymentAdvise);
//                        supplierPaymentsMap.put(supplierID, paymentAdvisesForSupplier);
//                    }
//                }
//
//                for (Map.Entry<String, ArrayList<PaymentAdvice>> aMapEnry : supplierPaymentsMap.entrySet()) {
//                    BookingPaymentAdviseInfo aPaymentAdviseForBooking = new BookingPaymentAdviseInfo();
//                    ArrayList<PaymentAdvice> aSupplierPaymentAdvises = aMapEnry.getValue();
//                    PaymentAdvice tmpPaymentAdvice = aSupplierPaymentAdvises.get(0);
//                    aPaymentAdviseForBooking.setNetPayableToSupplier(tmpPaymentAdvice.getNetPayableToSupplier());
//                    aPaymentAdviseForBooking.setPaymentCurrency(tmpPaymentAdvice.getSupplierCurrency());
//                    aPaymentAdviseForBooking.setSupplierName(tmpPaymentAdvice.getSupplierName());
//
//                    ArrayList<PaymentAdvisePaymentDetails> paymentDetailsList = new ArrayList<>();
//                    for (PaymentAdvice aPaymentAdvice : aSupplierPaymentAdvises) {
//                        PaymentAdvisePaymentDetails outPaymentAdvice = new PaymentAdvisePaymentDetails();
//                        PaymentDetails paymentDetails = paymentDetailsRepository.serachPaymentDetails(aPaymentAdvice.getPaymentAdviceNumber());
//                        if (paymentDetails!=null)
//                        {
//                            if (paymentDetails.getPayToSupplierSet() != null) {
//                                List<PayToSupplier> payToSupplierSet = paymentDetails.getPayToSupplierSet();
//                                for (PayToSupplier payToSupplier : payToSupplierSet) {
//                                    outPaymentAdvice.setPaymentRefNumber(payToSupplier.getPaymentReferenceNumber());
//                                    outPaymentAdvice.setPaymentDate(DateTimeFormatter.ofPattern("dd-MM-yyyy").format(payToSupplier.getDateOfPayment()).toString());
//                                }
//                            } else {
//                                GuaranteeToSupplier guaranteeToSupplier = paymentDetails.getGuaranteeToSupplier();
////                                outPaymentAdvice.setPaymentRefNumber(payToSupplier.getPaymentReferenceNumber());
//                                outPaymentAdvice.setPaymentDate(DateTimeFormatter.ofPattern("dd-MM-yyyy").format(guaranteeToSupplier.getDateOfGuarantee()).toString());
//
//                            }
//                        }
//                        outPaymentAdvice.setAmount(aPaymentAdvice.getAmountPayableForSupplier());
//                        outPaymentAdvice.setCurrency(aPaymentAdvice.getSelectedSupplierCurrency());
//                        outPaymentAdvice.setPaymentAdviseID(aPaymentAdvice.getPaymentAdviceNumber());
//                        ZonedDateTime paymentAdviseDueDateZDT = aPaymentAdvice.getPaymentDueDate();
//                        String opsDateFormat = "dd-MM-yyyy";
//                        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern(opsDateFormat);
//                        String paymentDueDate = dtFormatter.format(paymentAdviseDueDateZDT);
//                        outPaymentAdvice.setPaymentDueDate(paymentDueDate);
//                        outPaymentAdvice.setTypeOfSettlement(aPaymentAdvice.getTypeOfSettlement());
//                        outPaymentAdvice.setStatus(aPaymentAdvice.getPaymentAdviceStatus().getPaymentAdviseStatus());
//
//                        Set<PaymentAdviceOrderInfo> paymentAdviseOrders = aPaymentAdvice.getPaymentAdviceOrderInfoSet();
//                        OpsProduct opsProduct = getOpsProduct( aBooking, paymentAdviseOrders.iterator().next().getOrderId() );
//                        aPaymentAdviseForBooking.setProductType(opsProduct.getProductSubCategory());
//                        paymentDetailsList.add(outPaymentAdvice);
//                    }
//                    aPaymentAdviseForBooking.setPaymentAdviseDetailsList(paymentDetailsList);
//
//                    paymentAdviseInfoList.add(aPaymentAdviseForBooking);
//                }
//            }
//            else {
//                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//                OpsBooking booking = bookingService.getBooking(bookingID);
//                List<OpsProduct> products = booking.getProducts();
//                OpsProduct opsProduct = products.get(0);
//                String orderId = opsProduct.getOrderID();
//                String supplierID = opsProduct.getSupplierID();
//                DateWisePaymentPercentage dateWisePaymentPercentage = paymentAdviseMDMLoaderService.getPaymentDueDate(bookingID, orderId, supplierID);
//                BigDecimal netPayableToSupplier = paymentAdviseBELoaderService.getNetPayableToSupplier(bookingID, orderId);
//
//                String supplierCurrency = getSupplierCurrency(bookingID, products.get(0).getOrderID());
//                PaymentAdvisePaymentDetails paymentAdvisePaymentDetails = new PaymentAdvisePaymentDetails();
//                paymentAdvisePaymentDetails.setTypeOfSettlement(paymentAdviseMDMLoaderService.getTypeOfSettlement(supplierID, false));
//                paymentAdvisePaymentDetails.setPaymentDueDate(formatter.format(dateWisePaymentPercentage.getPaymentDueDate()));
//                paymentAdvisePaymentDetails.setAmount(automaticPaymentAdvice.getAmountPayableForSupplier(netPayableToSupplier, netPayableToSupplier, dateWisePaymentPercentage.getPercentage()));
//                paymentAdvisePaymentDetails.setStatus("Pending");
//                paymentAdvisePaymentDetails.setCurrency(supplierCurrency);
//
//
//                BookingPaymentAdviseInfo bookingPaymentAdviseInfo = new BookingPaymentAdviseInfo();
//                bookingPaymentAdviseInfo.setNetPayableToSupplier(netPayableToSupplier);
//                bookingPaymentAdviseInfo.setPaymentAdviseDetailsList(Collections.singletonList(paymentAdvisePaymentDetails));
//                bookingPaymentAdviseInfo.setPaymentCurrency(supplierCurrency);
//                bookingPaymentAdviseInfo.setProductType(products.get(0).getProductSubCategory());
//                bookingPaymentAdviseInfo.setSupplierID(supplierID);
//                bookingPaymentAdviseInfo.setSupplierName(products.get(0).getEnamblerSupplierName());
//                paymentAdviseInfoList.add(bookingPaymentAdviseInfo);
//            }
//        }
//        catch( Exception e )    {
//            e.printStackTrace();
//            logger.error( "Fetch Account Summary: Error occurred while loading payment advise " + e );
//        }
//        return paymentAdviseInfoList;
        List<OpsProduct> products = aBooking.getProducts();
        for (OpsProduct opsProduct: products)
        {
            bookingPaymentAdviseInfo = new BookingPaymentAdviseInfo();
            bookingPaymentAdviseInfo.setProductType(opsProduct.getProductSubCategory());
            bookingPaymentAdviseInfo.setSupplierName(opsProduct.getEnamblerSupplierName());
            bookingPaymentAdviseInfo.setNetPayableToSupplier(paymentAdviceLoaderService.getNetPayableToSupplier(bookingID,opsProduct.getOrderID()));
            bookingPaymentAdviseInfo.setPaymentCurrency(getSupplierCurrency(bookingID,opsProduct.getOrderID()));
            String typeOfSettlement = paymentAdviseMDMLoaderService.getTypeOfSettlement(opsProduct.getSupplierID(), false);

            List<PaymentAdvisePaymentDetails> paymentDetailsList = new ArrayList<>();
            PaymentAdvisePaymentDetails details = null;
            if ("No-Credit Deposit".equalsIgnoreCase(typeOfSettlement))
            {
                ServiceOrderResource serviceOrderResource = null;
                details = new PaymentAdvisePaymentDetails();
                details.setTypeOfSettlement(typeOfSettlement);
                ServiceOrderSearchCriteria searchCriteria = new ServiceOrderSearchCriteria();
                searchCriteria.setBookingRefNo(bookingID);
                searchCriteria.setOrderId(opsProduct.getOrderID());
                Map<String, Object> finalServiceOrders = null;

                finalServiceOrders = finalServiceOrderService.getFinalServiceOrders(searchCriteria, false);
                if (finalServiceOrders!=null){
                    List<ServiceOrderResource> orderResource = (List<ServiceOrderResource>) finalServiceOrders.get("result");
                    if (orderResource.size() !=0) {
                        serviceOrderResource = orderResource.get(0);
                        if (!StringUtils.isEmpty(serviceOrderResource.getNetPayableToSupplier())) {
                            BigDecimal amountPaidToSupplier = serviceOrderResource.getNetPayableToSupplier();
                            if (!OpsBookingStatus.TKD.equals(status)){
                                int i = amountPaidToSupplier.compareTo(new BigDecimal(0));
                                if (i==0) {
                                    getPSOWhenFSONotFound(paymentDetailsList, details, searchCriteria);
                                }
                            }
                            else {
                                details.setAmount(amountPaidToSupplier);
                            }
                        }
                        details.setStatus("Paid");
                        paymentDetailsList.add(details);
                    }else{
                        getPSOWhenFSONotFound(paymentDetailsList, details, searchCriteria);
                    }
                }
                else {
                    getPSOWhenFSONotFound(paymentDetailsList, details, searchCriteria);
                }
            }
            if ("No-Credit Pre Payment".equalsIgnoreCase(typeOfSettlement) || "Credit".equalsIgnoreCase(typeOfSettlement))
            {
                PaymentCriteria paymentSearchCriteria = new PaymentCriteria();
                paymentSearchCriteria.setBookingRefId(bookingID);
                paymentSearchCriteria.setOrderId(opsProduct.getOrderID());
                paymentSearchCriteria.setPrePayment(true);
                List<PaymentAdvice> paymentAdvisesList = paymentAdviceService.searchSupplierPayment(paymentSearchCriteria);
                if (paymentAdvisesList!=null && paymentAdvisesList.size()>0)
                {
                    for (PaymentAdvice advice : paymentAdvisesList)
                    {
                        details = new  PaymentAdvisePaymentDetails();
                        details.setTypeOfSettlement(typeOfSettlement);
                        ZonedDateTime paymentAdviseDueDateZDT = advice.getPaymentDueDate();
                        String opsDateFormat = "dd-MM-yyyy";
                        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern(opsDateFormat);
                        String paymentDueDate = dtFormatter.format(paymentAdviseDueDateZDT);
                        details.setPaymentDate(paymentDueDate);
                        BigDecimal amountPayableForSupplier = advice.getAmountPayableForSupplier();
                        details.setAmount(amountPayableForSupplier);

                        PaymentDetails paymentDetails = paymentDetailsRepository.serachPaymentDetails(advice.getPaymentAdviceNumber());
                        if (paymentDetails!=null)
                        {
                            if (paymentDetails.getPayToSupplierSet() != null)
                            {
                                List<PayToSupplier> payToSupplierSet = paymentDetails.getPayToSupplierSet();
                                BigDecimal amt = new BigDecimal(0);
                                for (PayToSupplier payToSupplier : payToSupplierSet) {
                                    amt = amt.add(payToSupplier.getAmountPayableForSupplier());
                                    details.setPaymentRefNumber(payToSupplier.getPaymentReferenceNumber());
                                    details.setPaymentDate(DateTimeFormatter.ofPattern("dd-MM-yyyy").format(payToSupplier.getDateOfPayment()).toString());
                                }
                                int i = amountPayableForSupplier.compareTo(amt);
                                details.setStatus(i==0?"Paid":i==1?"Partially Paid":"");
                                paymentDetailsList.add(details);
                            } else {
                                GuaranteeToSupplier guaranteeToSupplier = paymentDetails.getGuaranteeToSupplier();
                                details.setPaymentRefNumber("");
                                details.setPaymentDate(DateTimeFormatter.ofPattern("dd-MM-yyyy").format(guaranteeToSupplier.getDateOfGuarantee()).toString());
                                paymentDetailsList.add(details);
                            }
                        }
                        else {
                            details.setPaymentRefNumber("-");
                            details.setPaymentDate("-");
                            details.setStatus("Pending");
                            paymentDetailsList.add(details);
                        }
                    }
                }
            }
            bookingPaymentAdviseInfo.setPaymentAdviseDetailsList(paymentDetailsList);
            bookingPaymentAdviseInfoList.add(bookingPaymentAdviseInfo);
        }
        return bookingPaymentAdviseInfoList;
    }

    private void getPSOWhenFSONotFound(List<PaymentAdvisePaymentDetails> paymentDetailsList, PaymentAdvisePaymentDetails details, ServiceOrderSearchCriteria searchCriteria) throws OperationException, IOException {
        ServiceOrderResource serviceOrderResource;Map<String, Object> provisionalServiceOrders = provisionalServiceOrderService.getProvisionalServiceOrders(searchCriteria, false);
        if (provisionalServiceOrders != null) {
            List<ServiceOrderResource> value = (List<ServiceOrderResource>) provisionalServiceOrders.get("result");
            if (value.size()!=0) {
                serviceOrderResource = value.get(0);
                details.setAmount(serviceOrderResource.getNetPayableToSupplier());
                details.setStatus("Paid");
                paymentDetailsList.add(details);
            }
        }
    }

    @Override
    public void updateAccountSummaryForPayments(AccountSummary actSummary) throws OperationException {
        try {
            BigDecimal clientTotalCollection;
            BigDecimal clientTotalOutstandingAmount = BigDecimal.ZERO;
            BigDecimal totalAmountPaid = BigDecimal.ZERO;
            BigDecimal totalNetAmount = BigDecimal.ZERO;
            BigDecimal clientTotalCost = BigDecimal.ZERO;
            String invoiceCcy = "";
            //Fetching the Payment details form Invoice rather then BookingEngine.
            List<InvoiceBasicInfo> invoiceBasicInfos = actSummary.getInvoiceInfoList();
            for(InvoiceBasicInfo invoiceBasicInfo : invoiceBasicInfos){
                invoiceCcy = invoiceBasicInfo.getInvoiceCurrency();
                if(invoiceBasicInfo.getOutStandingAmount()!=null && !invoiceBasicInfo.getOutStandingAmount().isEmpty())
                    clientTotalOutstandingAmount = clientTotalOutstandingAmount.add(new BigDecimal(invoiceBasicInfo.getOutStandingAmount()));
                clientTotalCost = clientTotalCost.add(new BigDecimal(invoiceBasicInfo.getInvoiceAmount()));
            }
            clientTotalCollection = clientTotalCost.subtract(clientTotalOutstandingAmount);

       /*     for( OpsPaymentInfo aPaymentInfo : paymentsList )   {
                String paymentAmount = aPaymentInfo.getAmountPaid();
                if( paymentAmount != null && paymentAmount.trim().length() > 0 )    {
                    try {
                        BigDecimal tmpPaymentAmount = new BigDecimal( paymentAmount );
                        clientTotalCollection = clientTotalCollection.add( tmpPaymentAmount );
                        logger.info("ClientTotalCollection: "+clientTotalCollection);
                    }
                    catch( Exception e )    {
                        logger.error( "Error occurred while parsing paymentAmount", e );
                    }
                }

                String totalAmount = aPaymentInfo.getTotalAmount();
                if( totalAmount != null && totalAmount.trim().length() > 0 )    {
                    try {
                        BigDecimal tmpTotalAmount = new BigDecimal( totalAmount );
                        tmpTotalAmount=  tmpTotalAmount.subtract(clientTotalCollection);
                        clientTotalOutstandingAmount = tmpTotalAmount;
                    }
                    catch( Exception e )    {
                        logger.error( "Error parsing totalAmount data", e );
                    }
                }
            }*/

            List<BookingPaymentAdviseInfo> paymentAdviseList = actSummary.getPaymentAdviseList();
            if (paymentAdviseList!=null && paymentAdviseList.size()>0)
            {
                for (BookingPaymentAdviseInfo bookingPayment : paymentAdviseList)
                {
                    BigDecimal netPayableToSupplier = bookingPayment.getNetPayableToSupplier();
                    List<PaymentAdvisePaymentDetails> paymentAdviseDetailsList = bookingPayment.getPaymentAdviseDetailsList();
                    for (PaymentAdvisePaymentDetails paymentDetails: paymentAdviseDetailsList)
                    {
                        if (paymentDetails.getPaymentRefNumber()==null )
                        {
                            String paymentAdviseID = paymentDetails.getPaymentAdviseID();
                            if (paymentAdviseID != null) {
                                PaymentDetails paymentDetails1 = paymentDetailsRepository.serachPaymentDetails(paymentAdviseID);
                                GuaranteeToSupplier guaranteeToSupplier = paymentDetails1.getGuaranteeToSupplier();
                                if (guaranteeToSupplier != null) {
                                    totalAmountPaid = totalNetAmount.add(guaranteeToSupplier.getGuaranteeForAmtToSupplier());
                                } else {
                                    totalAmountPaid = totalAmountPaid.add(BigDecimal.ZERO);
                                }

                            }
                            else {
                                totalAmountPaid = totalAmountPaid.add(paymentDetails.getAmount());

                            }

                        }
                        else
                        {
                            PaymentDetails paymentDetails1 = paymentDetailsService.getPaymentDetails(paymentDetails.getPaymentAdviseID());
                            List<PayToSupplier> payToSupplierSet = paymentDetails1.getPayToSupplierSet();
                            if (payToSupplierSet!=null && payToSupplierSet.size()>0) {
                                for (PayToSupplier payToSupplier: payToSupplierSet)
                                {
                                    totalAmountPaid = totalAmountPaid.add(payToSupplier.getAmountPayableForSupplier());
                                }
                            }
                            else
                            {
                                GuaranteeToSupplier guaranteeToSupplier = paymentDetails1.getGuaranteeToSupplier();
                                if (guaranteeToSupplier!=null)
                                {
                                    totalAmountPaid = totalAmountPaid.add(guaranteeToSupplier.getGuaranteeForAmtToSupplier());
                                }
                            }
                        }
                    }
                    if(bookingPayment.getNetPayableToSupplier()!=null)
                        totalNetAmount = totalNetAmount.add(bookingPayment.getNetPayableToSupplier());
                }
            }

            actSummary.setSupplierTotalAmountPaid(totalAmountPaid.toPlainString());
            actSummary.setSupplierBalanceAmountDue((totalNetAmount.subtract(totalAmountPaid)).toPlainString());
            actSummary.setClientCollection(clientTotalCollection);
            actSummary.setClientOutstandingAmount(clientTotalOutstandingAmount);
            actSummary.setClientTotalCollection( clientTotalCollection.toPlainString() + " " + invoiceCcy);
            actSummary.setClientTotalOutstandingAmount( clientTotalOutstandingAmount.toPlainString() + " " + invoiceCcy);
            logger.info("ClientTotalCollection: "+clientTotalCollection);

        }
        catch( Exception e )    {
            logger.error( "Error occurred while updating payment information", e );
        }
    }

    @Override
    public AccountSummary getClientPaymentDetails(String bookingRefId) throws OperationException{
        AccountSummary bookingAccSummary = new AccountSummary();
        try{
            List<InvoiceBasicInfo> invoiceInfoList = getInvoiceInfoForBooking(bookingRefId);
            logger.info("Loaded Invoice information from Finance module");
            bookingAccSummary.setInvoiceInfoList(invoiceInfoList);
            updateAccountSummaryForPayments(bookingAccSummary);
        } catch (Exception e) {
            logger.error("Error occurred in loading Account Summary" + e);
            throw new OperationException(Constants.OPS_ERR_11205);
        }
        return  bookingAccSummary;
    }

    private String getProductName( OpsBooking aBooking, String anOrderID )    {
        StringBuilder buffer = new StringBuilder();

        List<OpsProduct> productsList = aBooking.getProducts();
        if( productsList != null && productsList.size() > 0 )   {
            for( OpsProduct aProduct : productsList )   {
                String tmpOrderID = aProduct.getOrderID();
                if( anOrderID.equalsIgnoreCase( tmpOrderID ))   {
                    OpsProductSubCategory productSubCategory = aProduct.getOpsProductSubCategory();
                    switch( productSubCategory )    {
                        case PRODUCT_SUB_CATEGORY_FLIGHT:   {
                            OpsFlightDetails flightDetails = aProduct.getOrderDetails().getFlightDetails();
                            List<OpsOriginDestinationOption> odoList = flightDetails.getOriginDestinationOptions();
                            if( odoList != null && odoList.size() > 0 )   {
                                OpsOriginDestinationOption anODO = odoList.get( 0 );
                                List<OpsFlightSegment> flightSegmentList = anODO.getFlightSegment();
                                OpsFlightSegment aFlightSegment = flightSegmentList.get( 0 );
                                OpsOperatingAirline operatingAirline = aFlightSegment.getOperatingAirline();
                                buffer.append( operatingAirline.getAirlineCode() + " - " );
                                buffer.append( operatingAirline.getFlightNumber() );
                                buffer.append( " (" + productSubCategory.getSubCategory() + ")" );
                            }
                        }
                        break;

                        case PRODUCT_SUB_CATEGORY_HOTELS:   {
                            OpsHotelDetails hotelDetails = aProduct.getOrderDetails().getHotelDetails();
                            buffer.append( hotelDetails.getHotelName() );
                            buffer.append( " (" + productSubCategory.getSubCategory() + ")" );
                        }
                        break;
                    }
                }
            }
        }

        return buffer.toString();
    }

    private String getSupplierCurrency(String bookingId,String orderId) throws OperationException {
        String currencyCode = null;

        OpsBooking opsBooking = bookingService.getBooking(bookingId);
        OpsProduct opsProduct = bookingService.getOpsProduct(opsBooking,orderId);
        OpsProductCategory productCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
        if (!StringUtils.isEmpty(productCategory)) {
            OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(productCategory, opsProduct.getProductSubCategory());
            if (!StringUtils.isEmpty(opsProductSubCategory)) {
                switch (productCategory) {
                    case PRODUCT_CATEGORY_ACCOMMODATION: {

                        switch (opsProductSubCategory) {
                            case PRODUCT_SUB_CATEGORY_HOTELS:
                                currencyCode = opsProduct.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getCurrencyCode();
                                break;
                        }
                    }

                    case PRODUCT_CATEGORY_TRANSPORTATION: {
                        switch (opsProductSubCategory) {
                            case PRODUCT_SUB_CATEGORY_FLIGHT:
                                currencyCode = opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getCurrencyCode();
                                break;
                        }
                    }

                }

            }
        }
        return currencyCode;

    }

    private OpsProduct getOpsProduct( OpsBooking opsBooking,String orderId )    {
        OpsProduct opsProduct = null;
        List<OpsProduct> productsList = opsBooking.getProducts();
        for( OpsProduct aProduct : productsList )   {
            String tmpOrderID = aProduct.getOrderID();
            if( orderId.equalsIgnoreCase( tmpOrderID )) {
                opsProduct = aProduct;
                break;
            }
        }

        return opsProduct;
    }

    private String getSupplierCurrency( OpsBooking opsBooking,String orderId) throws OperationException {
        String currencyCode = null;
        OpsProduct opsProduct =  getOpsProduct( opsBooking, orderId );
        OpsProductCategory productCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
        if (!StringUtils.isEmpty(productCategory)) {
            OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(productCategory, opsProduct.getProductSubCategory());
            if (!StringUtils.isEmpty(opsProductSubCategory)) {
                switch (productCategory) {
                    case PRODUCT_CATEGORY_ACCOMMODATION: {

                        switch (opsProductSubCategory) {
                            case PRODUCT_SUB_CATEGORY_HOTELS:
                                currencyCode = opsProduct.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getCurrencyCode();
                                break;
                        }
                    }

                    case PRODUCT_CATEGORY_TRANSPORTATION: {
                        switch (opsProductSubCategory) {
                            case PRODUCT_SUB_CATEGORY_FLIGHT:
                                currencyCode = opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getCurrencyCode();
                                break;
                        }
                    }

                }

            }
        }
        return currencyCode;

    }

    private HttpEntity getHttpEntity(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = userService.getLoggedInUserToken();
        headers.add("Authorization", token);
        return new HttpEntity(headers);
    }

    private HttpEntity getHttpEntity(Object object) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = userService.getLoggedInUserToken();
        headers.add("Authorization", token);
        return new HttpEntity(object, headers);
    }
}
