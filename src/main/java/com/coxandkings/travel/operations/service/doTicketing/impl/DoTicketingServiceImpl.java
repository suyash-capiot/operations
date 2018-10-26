package com.coxandkings.travel.operations.service.doTicketing.impl;

import com.coxandkings.travel.ext.model.be.Booking;
import com.coxandkings.travel.ext.model.be.PaymentInfo;
import com.coxandkings.travel.operations.enums.doTicketing.AbsorbRetainScenarios;
import com.coxandkings.travel.operations.enums.doTicketing.ApprovalTypeValues;
import com.coxandkings.travel.operations.enums.doTicketing.FareAmountComparator;
import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;
import com.coxandkings.travel.operations.enums.refund.RefundStatus;
import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.helper.booking.payment.AccountSummary;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.doTicketing.DoTicketing;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ProductDetail;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.doTicketing.DoTicketingRepository;
import com.coxandkings.travel.operations.resource.doTicketing.ApproveRejectResource;
import com.coxandkings.travel.operations.resource.doTicketing.DoTicketingResource;
import com.coxandkings.travel.operations.resource.doTicketing.SendForApprovalResource;
import com.coxandkings.travel.operations.resource.refund.RefundResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.accountsummary.AccountSummaryService;
import com.coxandkings.travel.operations.service.beconsumer.air.AirBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.doTicketing.DoTicketingService;
import com.coxandkings.travel.operations.service.mdmservice.CompanyMasterDataService;
import com.coxandkings.travel.operations.service.productbookedthrother.mdm.MdmClientService;
import com.coxandkings.travel.operations.service.refund.RefundService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Service
public class DoTicketingServiceImpl implements DoTicketingService{

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private BookingEngineElasticData bookingEngineElasticData;

    @Autowired
    private UserService userService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private CompanyMasterDataService companyMasterDataService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private MdmClientService mdmClientService;

    @Autowired
    private AccountSummaryService accountSummaryService;

    @Autowired
    private DoTicketingRepository doTicketingRepository;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private AirBookingEngineConsumptionService airBookingEngineConsumptionService;

    @Value(value = "${mdm.common.supplier.credentials}")
    private String getSupplierCredDetails;

    @Value(value = "${mdm.common.supplier.supplierCompanyMarket}")
    private String getSuppCompMarketMapping;

    @Value("${booking-engine-core-services.air.reprice}")
    private String airRePriceURL;

    @Value("${booking-engine-core-services.air.book}")
    private String airBookURL;

    @Value("${booking-engine-core-services.air.issueTicket}")
    private String airIssueTicketURL;

    private static Logger logger = LogManager.getLogger(DoTicketingServiceImpl.class);

    public JSONObject repriceForAir(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException{

        JSONObject repriceRq = null;
        String res = null;
        try {
            repriceRq = airBookingEngineConsumptionService.getRePriceRequestForAirJson(opsBooking, opsProduct);
        }catch(Exception e){
            logger.error("Unable to create Air BE-RePrice request for BookId" + opsBooking.getBookID());
        }
        repriceRq.getJSONObject("requestBody").put("bookID", opsBooking.getBookID());
        JSONObject pricedItinerary = repriceRq.getJSONObject("requestBody").getJSONArray("pricedItinerary").getJSONObject(0);
        pricedItinerary.put("bookID", opsBooking.getBookID());
        pricedItinerary.put("orderID", opsProduct.getOrderID());
        logger.info("Reprice_RQ is : " + repriceRq.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(repriceRq.toString(), headers);
        try {
            ResponseEntity<String> ratesResponseEntity = RestUtils.exchange(this.airRePriceURL, HttpMethod.POST, httpEntity, String.class);
            res = ratesResponseEntity.getBody();
        } catch (Exception e) {
            //TODO: throw an appropriate exception
            logger.error("Unable to perform Reprice :" + e.getMessage());
        }
        if(res == null)
            throw new OperationException("Unable to perform Reprice");

        logger.info("Reprice_RS is : " + res.toString());
        JSONObject repriceRes = new JSONObject(new JSONTokener(res));
        return repriceRes;
    }

    @Override
    public JSONObject repriceForAir(String bookId, String orderId) throws OperationException{

        OpsBooking opsBooking = opsBookingService.getBookingByPostCall(bookId);
        OpsProduct opsProduct = getOpsProduct(orderId, opsBooking);
        return repriceForAir(opsBooking, opsProduct);
    }

    @Override
    public DoTicketingResource repriceAndCalculateDifference(DoTicketingResource doTicketingResource) throws OperationException {

        DoTicketing doTicketing = repriceAndIssueTicket(doTicketingResource);
        doTicketingRepository.saveOrUpdate(doTicketing);
        modelToResourceConvert(doTicketing, doTicketingResource);
        return doTicketingResource;
    }

    @Override
    public DoTicketing repriceAndIssueTicket(DoTicketingResource doTicketingResource) throws OperationException {

        String id = doTicketingResource.getId();
        String bookId = doTicketingResource.getBookId();
        String orderId = doTicketingResource.getOrderId();
        DoTicketing doTicketing = doTicketingRepository.getById(id);
        if(doTicketing==null)
            throw new OperationException("No record found");

        OpsBooking opsBooking = opsBookingService.getBookingByPostCall(bookId);
        OpsProduct opsProduct = getOpsProduct(orderId, opsBooking);
        BigDecimal prevTotalAmount = new BigDecimal(opsProduct.getOrderDetails().getFlightDetails().getTotalPriceInfo().getTotalPrice());
        String oldAmountCcy = opsProduct.getOrderDetails().getFlightDetails().getTotalPriceInfo().getCurrencyCode();
        doTicketing.setOldAmount(prevTotalAmount);
        doTicketing.setOldAmountCcy(oldAmountCcy);
        doTicketing.setTicketingCredential(doTicketing.getTicketingCredential());
        if(validateIfSameDayIssueTicket(opsProduct.getCreatedAt())) {
            //As discussed with Dhananjay, Ashish and Venkatesh on 03-10-2018.
            //Perform Reprice only after midnight(as specified by suppliers) else directly issue Ticket.
            //If Issuing-Ticket Time < bookingDate Midnight, No need to reprice because the prices do not change as suggested by SI.
            issueTicketForAir(opsBooking, opsProduct, doTicketingResource);
            doTicketing.setDifference(BigDecimal.ZERO);
            doTicketing.setDifferenceCcy(oldAmountCcy);
            doTicketing.setFareComparator(FareAmountComparator.EQUAL.getValue());
            doTicketing.setStatus("Ticketed");
            return doTicketing;
        }

        JSONObject repriceRes = repriceForAir(opsBooking, opsProduct);
        JSONObject resBody = repriceRes.getJSONObject("responseBody");
        JSONArray pricedItinArr = resBody.optJSONArray("pricedItinerary");

        if(pricedItinArr==null || pricedItinArr.length()==0) {
            logger.error("No pricedItinerary in Reprice");
            throw new OperationException("Invalid Reprice Response received");
        }

        JSONObject pricedItin = pricedItinArr.getJSONObject(0);
        BigDecimal newTotalAmount = pricedItin.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").getBigDecimal("amount");
        String newAmountCcy = pricedItin.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").getString("currencyCode");

        doTicketing.setNewAmount(newTotalAmount);
        doTicketing.setNewAmountCcy(newAmountCcy);

        if(oldAmountCcy.equals(newAmountCcy)) {
            doTicketing.setDifference(newTotalAmount.subtract(prevTotalAmount));
            doTicketing.setDifferenceCcy(oldAmountCcy);
            logger.info("Difference after re-pricing: "+ doTicketing.getDifference()+" "+doTicketing.getDifferenceCcy());
        }
        else{
            //TODO: use ROE to convert.
            //Ideally there will not be a difference as BE already handles this and converts supplier currency to client currency
        }

        if(doTicketing.getDifference().compareTo(BigDecimal.ZERO) == 0) { //Fare is Lower or Equal
            doTicketing.setFareComparator(FareAmountComparator.EQUAL.getValue());
            bookAndIssueTicket(opsBooking, opsProduct, repriceRes, doTicketingResource);
            doTicketing.setStatus("Ticketed");
        }else if(doTicketing.getDifference().compareTo(BigDecimal.ZERO) == -1){
            doTicketing.setFareComparator(FareAmountComparator.LOWER.getValue());
            bookAndIssueTicket(opsBooking, opsProduct, repriceRes, doTicketingResource);
            doTicketing.setStatus("Ticketed");
        }else
            doTicketing.setFareComparator(FareAmountComparator.HIGHER.getValue());

        return doTicketing;

    }

    @Override
    public JSONObject repriceAndBook(String bookId, String orderId) throws OperationException{

        OpsBooking opsBooking = opsBookingService.getBookingByPostCall(bookId);
        OpsProduct opsProduct = getOpsProduct(orderId, opsBooking);
        JSONObject repriceRs = null;
        JSONObject repriceRq = null;
        try {
            repriceRq = airBookingEngineConsumptionService.getRePriceRequestForAirJson(opsBooking, opsProduct);
        }catch(Exception e){
            logger.error("Unable to create Air Re-Price request for BookId: " + bookId);
        }
        if(repriceRq == null || repriceRq.length()==0)
            return null;

        JSONObject pricedItinerary = repriceRq.getJSONObject("requestBody").getJSONArray("pricedItinerary").getJSONObject(0);
        pricedItinerary.put("bookID", bookId);
        pricedItinerary.put("orderID", orderId);
        try {
            repriceRs = airBookingEngineConsumptionService.getRePriceResponseForAirJson(repriceRq);
        }catch(Exception e){
            logger.error("Unable to get Air BE Re-Price response for BookId: " + bookId);
        }
        return bookFromRepriceRS(opsBooking, opsProduct, repriceRs);
    }

    @Override
    public DoTicketingResource retainOrRefund(DoTicketingResource doTicketingResource) throws OperationException{

        if(doTicketingResource.getId()==null)
            throw new OperationException("No record Id specified");

        AbsorbRetainScenarios scenario = AbsorbRetainScenarios.fromString(doTicketingResource.getScenario());
        if(scenario==null)
            throw new OperationException(Constants.OPS_ERR_60009);

        DoTicketing doTicketing = doTicketingRepository.getById(doTicketingResource.getId());
        resourceToModelConvert(doTicketingResource, doTicketing);
        switch (scenario){

            case PASS_ON_TO_CLIENT:
                throw new OperationException(Constants.OPS_ERR_60008);
            case ABSORB_BY_COMPANY:
                throw new OperationException(Constants.OPS_ERR_60008);
            case CHARGE_TO_CLIENT:
                //As discussed with Ashish, DJ and pritish. Send the additional amount in Part-Payment schedule of BE.
                updatePaymentInfoInBE(doTicketingResource.getBookId());
                break;

            case RETAIN_BY_COMPANY:
                //TODO: Discuss where to store the profit or margin.
                doTicketing = doTicketingRepository.saveOrUpdate(doTicketing);
                modelToResourceConvert(doTicketing, doTicketingResource);
                return doTicketingResource;

        }
        return null;
    }

    private void updatePaymentInfoInBE(String bookId) throws OperationException{

        Booking booking = opsBookingService.getRawBooking(bookId);
        List<PaymentInfo> paymentInfo = booking.getBookingResponseBody().getPaymentInfo();

    }

    public JSONObject bookAndIssueTicket(OpsBooking opsBooking, OpsProduct opsProduct, JSONObject repriceRs, DoTicketingResource doTicketingResource) throws OperationException{

        bookFromRepriceRS(opsBooking, opsProduct, repriceRs);
        //As a booking is done PNR changes.
        //Get new Booking data
        opsBooking = opsBookingService.getBooking(opsBooking.getBookID());
        opsProduct = getOpsProduct(opsProduct.getOrderID(), opsBooking);
        JSONObject issueTicketRs = issueTicketForAir(opsBooking ,opsProduct, doTicketingResource);
        return issueTicketRs;
    }

    public JSONObject bookFromRepriceRS(OpsBooking opsBooking, OpsProduct opsProduct, JSONObject repriceRs) throws OperationException{

        String res = null;
        JSONObject bookRq = null;
        JSONObject pricedItinerary;
        try {
            bookRq = airBookingEngineConsumptionService.createBEAirBookRQFromRepriceRS(repriceRs, opsBooking, opsProduct);
        }catch(Exception e){
            logger.error("Unable to create Air BE Book request for BookId: " + opsBooking.getBookID());
        }
        bookRq.getJSONObject("requestBody").put("bookID", opsBooking.getBookID());
        pricedItinerary = bookRq.getJSONObject("requestBody").getJSONArray("pricedItinerary").getJSONObject(0);
        pricedItinerary.put("bookID", opsBooking.getBookID());
        pricedItinerary.put("orderID", opsProduct.getOrderID());
        logger.info("Book_RQ is : " + bookRq.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(bookRq.toString(), headers);
        try {
            ResponseEntity<String> ratesResponseEntity = RestUtils.exchange(this.airBookURL, HttpMethod.POST, httpEntity, String.class);
            res = ratesResponseEntity.getBody();
        } catch (Exception e) {
            throw new OperationException("Unable to perform Book operation for Issue Ticket");
        }
        if(res==null)
            return null;

        logger.info("Book_RS is : " + res.toString());
        JSONObject bookRes = new JSONObject(new JSONTokener(res));
        JSONArray suppBookRefs = bookRes.getJSONObject("responseBody").optJSONArray("supplierBookReferences");
        if(suppBookRefs==null || suppBookRefs.length()==0)
            throw new OperationException("Unable to perform Book operation for Issue Ticket");

        return bookRes;
    }

    public void issueTicketValidator(String bookId, String orderId) throws OperationException {
        OpsBooking opsBooking = opsBookingService.getBooking(bookId);
        OpsProduct opsProduct = getOpsProduct(orderId, opsBooking);
        issueTicketValidator(opsBooking, opsProduct);
    }

    @Override
    public DoTicketingResource sendForApproval(SendForApprovalResource sendForApprovalResource) throws OperationException{

        DoTicketingResource doTicketingResource = new DoTicketingResource();
        ApprovalTypeValues approvalTypeValue = ApprovalTypeValues.fromString(sendForApprovalResource.getOpsApprovalType());
        if(approvalTypeValue==null)
            throw new OperationException(Constants.OPS_ERR_60006);

        ToDoTask toDoTask;
        DoTicketing doTicketing;
        String id  = sendForApprovalResource.getId();
        String bookId = sendForApprovalResource.getBookId();
        String orderId = sendForApprovalResource.getOrderId();
        if(bookId==null || bookId.isEmpty() || orderId==null || orderId.isEmpty())
            throw new OperationException("BookId or OrderId is not specified");

        OpsBooking opsBooking = opsBookingService.getBooking(sendForApprovalResource.getBookId());
        OpsProduct opsProduct = getOpsProduct(sendForApprovalResource.getOrderId(), opsBooking);
        if(id!=null && !id.isEmpty()){
            doTicketing = doTicketingRepository.getById(id);
            if(doTicketing == null)
                throw new OperationException("No record Found");
        }else{
            doTicketing = doTicketingRepository.getByBookAndOrderId(bookId, orderId);
        }

        if(doTicketing==null){
            doTicketing = new DoTicketing();
            doTicketing.setBookId(sendForApprovalResource.getBookId());
            doTicketing.setOrderId(sendForApprovalResource.getOrderId());
            doTicketing.setStatus("Pending");
            doTicketing = doTicketingRepository.saveOrUpdate(doTicketing);
            sendForApprovalResource.setId(doTicketing.getId());
            toDoTask = createToDoTask(opsBooking, opsProduct, sendForApprovalResource);
        }else{
            if(doTicketing.getOpsApprovalStatus()!=null && doTicketing.getOpsApprovalStatus().equalsIgnoreCase("Pending")) {
                logger.error("Already waiting for approval for " + doTicketing.getOpsApprovalType());
                throw new OperationException("Already waiting for approval For " + doTicketing.getOpsApprovalType());
            }
            if(approvalTypeValue == ApprovalTypeValues.ABSORB_BY_COMPANY || approvalTypeValue== ApprovalTypeValues.REFUND_TO_CLIENT) {
                doTicketing.setScenario(AbsorbRetainScenarios.fromString(sendForApprovalResource.getScenario()));
                doTicketing.setCompanyAmount(sendForApprovalResource.getCompanyAmount());
                doTicketing.setCustomerAmount(sendForApprovalResource.getCustomerAmount());
            }
            toDoTask = createToDoTask(opsBooking, opsProduct, sendForApprovalResource);
        }

        doTicketing.setOpsApprovalStatus("Pending");
        doTicketing.setOpsApprovalType(approvalTypeValue);
        doTicketing.setReasonForRequest(sendForApprovalResource.getReasonForRequest());
        doTicketing.setApproverTodoTaskId(toDoTask.getId());
        doTicketing = doTicketingRepository.saveOrUpdate(doTicketing);
        modelToResourceConvert(doTicketing, doTicketingResource);
        return doTicketingResource;
    }

    @Override
    public DoTicketingResource approveOrReject(ApproveRejectResource approveRejectResource) throws OperationException {

        DoTicketingResource doTicketingResource = new DoTicketingResource();
        String toDoId;
        ToDoTask toDoTask = null;
        DoTicketing doTicketing = doTicketingRepository.getById(approveRejectResource.getId());
        if (doTicketing == null)
            throw new OperationException("No record found");

        if (doTicketing.getOpsApprovalStatus() == null || doTicketing.getOpsApprovalStatus().isEmpty())
            throw new OperationException("This was never sent for approval");

        ApprovalTypeValues approvalTypeValue = doTicketing.getOpsApprovalType();
        if (doTicketing.getOpsApprovalStatus().equalsIgnoreCase("Pending")) {
            if (approveRejectResource.getStatus().equalsIgnoreCase("Approved")) {
                switch (approvalTypeValue) {
                    case REFUND_TO_CLIENT:
                        String creditNo = createCreditNote(doTicketing.getBookId(), doTicketing.getOrderId(), doTicketing);
                        doTicketing.setCreditNo(creditNo);
                        break;
                    case ABSORB_BY_COMPANY:
                        //TODO: To understand what should be done in this case
                        break;
                    case PAYMENT_PENDING:
                        break;
                }
            }
            doTicketing.setOpsApprovalStatus(approveRejectResource.getStatus());
            doTicketing.setApproverRemark(approveRejectResource.getRemarks());
            doTicketing = doTicketingRepository.update(doTicketing);
            toDoId = doTicketing.getApproverTodoTaskId();
            try {
                toDoTask = toDoTaskService.getById(toDoId);
            } catch (Exception e) {
                logger.error("IOException in To-Do task");
            }
            toDoTaskService.updateToDoTaskStatus(toDoTask.getReferenceId(), ToDoTaskSubTypeValues.fromString(doTicketing.getOpsApprovalType().getValue()), ToDoTaskStatusValues.CLOSED);
            modelToResourceConvert(doTicketing, doTicketingResource);
            return doTicketingResource;
        }
        else
            throw new OperationException("Already " + doTicketing.getOpsApprovalStatus());
    }

    @Override
    public DoTicketingResource getDoTicketing(String bookId, String orderId) throws OperationException{

        issueTicketValidator(bookId, orderId);
        DoTicketing doTicketing = doTicketingRepository.getByBookAndOrderId(bookId, orderId);
        //TODO: To decide whether to create a new record if null.
        if(doTicketing==null){
            doTicketing = new DoTicketing();
            doTicketing.setBookId(bookId);
            doTicketing.setOrderId(orderId);
            doTicketing.setStatus("Pending");
            doTicketing = doTicketingRepository.saveOrUpdate(doTicketing);
        }
        DoTicketingResource doTicketingResource = new DoTicketingResource();
        modelToResourceConvert(doTicketing, doTicketingResource);
        return doTicketingResource;
    }

    @Override
    public DoTicketingResource getDoTicketing(String id){

        DoTicketingResource doTicketingResource = new DoTicketingResource();
        DoTicketing doTicketing = doTicketingRepository.getById(id);
        modelToResourceConvert(doTicketing, doTicketingResource);
        return doTicketingResource;
    }

    private ToDoTask createToDoTask(OpsBooking opsBooking,OpsProduct opsProduct, SendForApprovalResource sendForApprovalResource) throws OperationException{

        ToDoTaskResource toDoTaskResource = getTodoForOpsApproval(opsBooking, opsProduct, sendForApprovalResource);
        ToDoTask toDoTask;
        try {
            toDoTask = toDoTaskService.save(toDoTaskResource);
            logger.info("todo task id " + toDoTask.getId());
        } catch (Exception e) {
            logger.debug("Error occured in saving todo task");
            throw new OperationException("Unable to send for approval");
        }
        return toDoTask;

    }

    public void issueTicketValidator(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException {

        logger.info("Validating Issue Ticket for BookID " + opsBooking.getBookID() + " orderID " + opsProduct.getOrderID());
        DoTicketing doTicketing = doTicketingRepository.getByBookAndOrderId(opsBooking.getBookID(), opsProduct.getOrderID());
        if (opsBooking.getStatus() == OpsBookingStatus.TKD &&(opsProduct.getStatus()!=null && opsProduct.getStatus().equalsIgnoreCase("Ticketed"))){
//            if(doTicketing==null) // Means Manual Ticketing was not done since no record
                throw new OperationException(Constants.OPS_ERR_60004);
        }

        if(opsBooking.getStatus() == OpsBookingStatus.CNF ||
                (opsProduct.getStatus()!=null && opsProduct.getStatus().equalsIgnoreCase("Confirmed"))) {

            AccountSummary accountSummary = accountSummaryService.getClientPaymentDetails(opsBooking.getBookID());
            BigDecimal paymentReceived = accountSummary.getClientCollection();
            //TODO: Check the amount paid by client. Ideally Receipt should be checked.
            //For Now checking the paymentInfo from OpsBooking.
            BigDecimal initialDepositAmount = BigDecimal.ZERO;
            List<OpsPaymentInfo> paymentList = opsBooking.getPaymentInfo();
            for (OpsPaymentInfo paymentInfo : paymentList) {
                if (paymentInfo.getPartPaymentSchedule() != null) {
                    for (OpsPartPaymentSchedule opsPartPaymentSchedule : paymentInfo.getPartPaymentSchedule())
                        if ("Initial Deposit".equalsIgnoreCase(opsPartPaymentSchedule.getPaymentScheduleType())) {
                            initialDepositAmount = opsPartPaymentSchedule.getDueAmount();
                            break;
                        }
                }
            }
            //If Amount paid is Zero or payment Received < initial deposit,then send it for approval.
            if (paymentReceived.compareTo(BigDecimal.ZERO) == 0 || paymentReceived.compareTo(initialDepositAmount) < -1) {

                if(doTicketing==null)
                    throw new OperationException(Constants.OPS_ERR_60002);

                if(doTicketing.getOpsApprovalType()!=null && doTicketing.getOpsApprovalType() == ApprovalTypeValues.PAYMENT_PENDING){
                    if(doTicketing.getOpsApprovalStatus().equalsIgnoreCase("Pending"))
                        throw new OperationException(Constants.OPS_ERR_60003);
                    if(doTicketing.getOpsApprovalStatus().equalsIgnoreCase("Rejected"))
                        throw new OperationException(Constants.OPS_ERR_60007);
                }
            }
        }
        else
            throw new OperationException(Constants.OPS_ERR_60001);
    }

    public ToDoTaskResource getTodoForOpsApproval(OpsBooking opsBooking, OpsProduct opsProduct, SendForApprovalResource sendForApprovalResource) throws OperationException {

        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskResource.setReferenceId(sendForApprovalResource.getId());
        toDoTaskResource.setClientTypeId(opsBooking.getClientType());

        toDoTaskResource.setCompanyId(opsBooking.getCompanyId());
        toDoTaskResource.setClientId(opsBooking.getClientID());

        toDoTaskResource.setClientCategoryId(opsBooking.getClientCategory());
        toDoTaskResource.setCompanyMarketId(opsBooking.getCompanyId());
        toDoTaskResource.setClientSubCategoryId(opsBooking.getClientSubCategory());

        toDoTaskResource.setProductCategory(opsProduct.getProductCategory());
        toDoTaskResource.setProductSubCategory(opsProduct.getProductSubCategory());
        toDoTaskResource.setDueOnDate(ZonedDateTime.now());
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
        toDoTaskResource.setTaskOrientedTypeId(ToDoTaskOrientedValues.APPROVAL_ORIENTED.getValue());
        toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        toDoTaskResource.setTaskSubTypeId(ApprovalTypeValues.fromString(sendForApprovalResource.getOpsApprovalType()).toString());
        toDoTaskResource.setBookingRefId(opsBooking.getBookID());
        toDoTaskResource.setFileHandlerId(sendForApprovalResource.getUserId());
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());//ToDoFunctionalAreaValues.OPERATIONS.getValue()); //Ops Approver

        return toDoTaskResource;
    }

    public JSONObject issueTicketForAir(OpsBooking opsBooking, OpsProduct opsProduct, DoTicketingResource doTicketingResource) throws OperationException{

        JSONObject res = airBookingEngineConsumptionService.getIssueTicketResponseAir(opsBooking, opsProduct, doTicketingResource);
        if (res == null || res.length()==0)
            throw new OperationException(Constants.OPS_ERR_60005);

        logger.info("Issue Ticket Response is : " + res.toString());
        JSONObject responseBody = res.getJSONObject("responseBody");
        JSONArray suppBookRef = responseBody.optJSONArray("supplierBookReferences");
        if(suppBookRef!=null && suppBookRef.length()!=0){
            String status = suppBookRef.getJSONObject(0).getString("status");
            if(status.equalsIgnoreCase("Success")) {
                //TODO: To auto-generate document E-Ticket if issueTicket successful.
                //TODO: This should get triggered from Kafka Message that B.E will send.
                return res;
            }
        }
        throw new OperationException(Constants.OPS_ERR_60005);
    }

    @Override
    public JSONObject issueTicketForAir(DoTicketingResource doTicketingResource) throws OperationException{

        String bookId = doTicketingResource.getBookId();
        String orderId = doTicketingResource.getOrderId();
        OpsBooking opsBooking = opsBookingService.getBookingByPostCall(bookId);
        OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking, orderId);
        return issueTicketForAir(opsBooking ,opsProduct, doTicketingResource);
    }

    @Override
    public JSONObject issueTicketForAir(String bookId, String orderId) throws OperationException{

        OpsBooking opsBooking = opsBookingService.getBookingByPostCall(bookId);
        OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking, orderId);
        return issueTicketForAir(opsBooking ,opsProduct, null);
    }

    private String createCreditNote(String bookingId, String orderId, DoTicketing doTicketing) throws OperationException {
        OpsBooking opsBooking = null;
        OpsProduct opsProduct = null;
        String creditNo;

        opsBooking = opsBookingService.getBooking(bookingId);
        opsProduct = getOpsProduct(orderId ,opsBooking);
        RefundResource refundResource = new RefundResource();

        refundResource.setBookingReferenceNo(bookingId);
        refundResource.setBookingRefId(bookingId);
        refundResource.setClaimCurrency(opsBooking.getClientCurrency());
        refundResource.setClientId(opsBooking.getClientID());
        refundResource.setClientName(opsBooking.getClientID());
        refundResource.setClientType(opsBooking.getClientType());
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductSubName(opsProduct.getProductSubCategory());
        productDetail.setProductCategorySubType(opsProduct.getProductSubCategory());
        productDetail.setProductCategory(opsProduct.getProductCategory());
        productDetail.setProductName(opsProduct.getProductName());
        productDetail.setOrderId(orderId);
        productDetail.setProductName(opsProduct.getProductName());
        refundResource.setProductDetail(productDetail);
        OpsPaymentInfo opsPaymentInfo = opsBooking.getPaymentInfo().get(0);
        refundResource.setDefaultModeOfPayment(opsPaymentInfo.getPaymentMethod());// TODO
        refundResource.setRequestedModeOfPayment(opsPaymentInfo.getPaymentMethod());
        //refundResource.setDueOn(ZonedDateTime.now().plusDays(10));//ToDosuggested by manoj
        // refundResource.setNetAmountPayable(amendSupplierCommercial.getRevisedSupplierCommercialResource().getPassOnClientAmount());//ToDo suggested by manoj
        // refundResource.setRefundCutOff(ZonedDateTime.now().plusDays(12));//ToDo suggested by manoj
        refundResource.setReasonForRequest(ReasonForRequest.REPRICING_DURING_TICKETING);
        refundResource.setRefundAmount(doTicketing.getCustomerAmount());//ToDo
        // refundResource.setRefundProcessedDate(ZonedDateTime.now().plusDays(5));//ToDo
        refundResource.setRefundStatus(RefundStatus.PENDING_WITH_OPS.getStatus());
        //ROE is set as ONE because margin is calculated in Client currency, therefore difference in margin will be in client currency
        refundResource.setRoeAsInClaim(BigDecimal.ONE);
        refundResource.setRoeRequested(BigDecimal.ONE);
        // refundResource.setRoeRequested(new BigDecimal(200));//ToDo
        refundResource.setRefundType(RefundTypes.REFUND_REDEEMABLE);
        try {
            creditNo = refundService.add(refundResource).getCreditNoteNo();
        }catch(Exception e){
            throw new OperationException(Constants.OPS_ERR_60010);
        }
        return creditNo;
    }

    @Override
    public JSONArray getSupplierCredentials(String bookId, String orderId) throws OperationException{

        JSONArray pccCredList = new JSONArray();
        JSONArray mappings = new JSONArray();
        OpsBooking opsBooking = opsBookingService.getBooking(bookId);
        OpsProduct opsProduct = getOpsProduct(orderId, opsBooking);

        String bookingCmpyMkt = companyMasterDataService.getCompanyMarketNameByID(opsBooking.getCompanyMarket());
        String supplierCredUrl = getSupplierCredDetails + "?filter={filter}";
        String suppCompMarketUrl = getSuppCompMarketMapping + "?filter={filter}";
        JSONObject filter = new JSONObject();
        //TODO: To check which supplier to use Source or Enabler.
        filter.put("supplier.supplierId", opsProduct.getSupplierID());
        String credRes = null;
        try {
            credRes = mdmRestUtils.exchange(supplierCredUrl, HttpMethod.GET, String.class, filter.toString());
        }catch (Exception e){
            logger.warn("Unable to fetch credentials list");
        }
        if (credRes == null) {
            return new JSONArray();
        }

        JSONArray credentialsList = new JSONObject(credRes).optJSONArray("data");

        filter = new JSONObject();
        filter.put("basicDetails.supplierId", opsProduct.getSupplierID());
        String marketMappingRes = null;
        try{
            marketMappingRes = mdmRestUtils.exchange(suppCompMarketUrl, HttpMethod.GET, String.class, filter.toString());
        }catch (Exception e){
            logger.warn("Unable to fetch supplier/company market mappings");
        }
        if(marketMappingRes!= null) {
            JSONArray marketMappingList = new JSONObject(marketMappingRes).optJSONArray("data");
            if (marketMappingList != null && marketMappingList.length() != 0)
                mappings = marketMappingList.getJSONObject(0).optJSONArray("mappings");
        }

        for(int i=0;i<credentialsList.length();i++){
            JSONObject credential = credentialsList.getJSONObject(i);
            JSONObject pccCred = new JSONObject();
            List<String> cmpyMkts = new ArrayList<>();
            JSONArray suppMarkets = credential.optJSONArray("supplierMarkets");
            String suppMkt =  (suppMarkets!=null && suppMarkets.length()!=0) ? suppMarkets.getString(0) : "";
            String clientType = credential.optString("clientType");
            pccCred.put("ticketingCredential", credential.getString("_id"));
            pccCred.put("supplierName", credential.getJSONObject("supplier").optString("supplierName"));
            pccCred.put("supplierId", credential.getJSONObject("supplier").optString("supplierId"));
            pccCred.put("clientType", clientType);
            pccCred.put("ownership", credential.getJSONObject("ownership").optString("ownershipWith"));
            //As per discussion with Dhananjay and Ashish on 15-10-2018, one Air supplier Credential will be mapped with only one supplier market.
            pccCred.put("supplierMarket", suppMkt);
            for(int j=0;j<mappings.length();j++){
                JSONObject mapping = mappings.getJSONObject(j);
                if(suppMkt.equals(mapping.optString("supplierMarket"))){
                    cmpyMkts.add(mapping.optString("companyMarket"));
                }
            }
            pccCred.put("companyMarket", cmpyMkts.contains(bookingCmpyMkt) ? bookingCmpyMkt : (cmpyMkts.size()!=0 ? cmpyMkts.get(0) : ""));
            pccCred.put("isCrossBorder", cmpyMkts.contains(bookingCmpyMkt) ? false : true);
            pccCredList.put(pccCred);
        }
        logger.info("Credentials List --: " + pccCredList);
        return pccCredList;
    }

    private OpsProduct getOpsProduct(String orderId, OpsBooking opsBooking) throws OperationException {
        OpsProduct opsProduct = null;
        for(OpsProduct product : opsBooking.getProducts()){
            if(product.getOrderID().equals(orderId))
                opsProduct = product;
        }
        if(opsProduct==null)
            throw new OperationException(Constants.PRODUCT_NOT_FOUND_FOR_ORDER, orderId);
        return opsProduct;
    }

    private void modelToResourceConvert(DoTicketing doTicketing, DoTicketingResource doTicketingResource) {
        if(doTicketing==null)
            return;

        CopyUtils.copy(doTicketing, doTicketingResource);

        if(doTicketing.getOpsApprovalType()!=null)
            doTicketingResource.setOpsApprovalType(doTicketing.getOpsApprovalType().getValue());
        if(doTicketing.getScenario()!=null)
            doTicketingResource.setScenario(doTicketing.getScenario().getValue());
    }

    private void resourceToModelConvert(DoTicketingResource doTicketingResource, DoTicketing doTicketing) {
        if(doTicketingResource==null)
            return;

        doTicketing.setId(doTicketingResource.getId());
        doTicketing.setBookId(doTicketingResource.getBookId());
        doTicketing.setOrderId(doTicketingResource.getOrderId());
        doTicketing.setStatus(doTicketingResource.getStatus());
        doTicketing.setTicketingCredential(doTicketingResource.getTicketingCredential());
        doTicketing.setOldAmount(doTicketingResource.getOldAmount());
        doTicketing.setOldAmountCcy(doTicketingResource.getOldAmountCcy());
        doTicketing.setNewAmount(doTicketingResource.getNewAmount());
        doTicketing.setNewAmountCcy(doTicketingResource.getNewAmountCcy());
        doTicketing.setDifference(doTicketingResource.getDifference());
        doTicketing.setDifferenceCcy(doTicketingResource.getDifferenceCcy());
        doTicketing.setScenario(AbsorbRetainScenarios.fromString(doTicketingResource.getScenario()));
        doTicketing.setCreditNo(doTicketingResource.getCreditNo());
        doTicketing.setCompanyAmount(doTicketingResource.getCompanyAmount());
        doTicketing.setCustomerAmount(doTicketingResource.getCustomerAmount());
        doTicketing.setOpsApprovalType(ApprovalTypeValues.fromString(doTicketingResource.getOpsApprovalType()));
        doTicketing.setApproverRemark(doTicketingResource.getApproverRemark());
        doTicketing.setApproverTodoTaskId(doTicketingResource.getApproverTodoTaskId());
        doTicketing.setReasonForRequest(doTicketingResource.getReasonForRequest());
        doTicketing.setOpsApprovalStatus(doTicketingResource.getOpsApprovalStatus());
        doTicketing.setClientApprovalStatus(doTicketingResource.getClientApprovalStatus());
        doTicketing.setFareComparator(doTicketingResource.getFareComparator());
    }

    public Boolean validateIfSameDayIssueTicket(ZonedDateTime bookingDateTime){

        // Issuing-Ticket Time < bookingDate Midnight.
        long currentDateTime = ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli();
        long bookingDateMidnight = bookingDateTime.plusDays(1).toInstant().truncatedTo(ChronoUnit.DAYS).toEpochMilli();
        if(currentDateTime < bookingDateMidnight)
            return true;

        return false;
    }

}
