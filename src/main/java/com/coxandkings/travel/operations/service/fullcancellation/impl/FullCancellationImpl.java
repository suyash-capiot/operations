package com.coxandkings.travel.operations.service.fullcancellation.impl;


import com.coxandkings.travel.ext.model.finance.cancelinvoice.InvoiceDto;
import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;
import com.coxandkings.travel.operations.enums.refund.RefundStatus;
import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.fullCancellation.FullCancellationIdentity;
import com.coxandkings.travel.operations.model.fullCancellation.FullCancellationKafkaMessage;
import com.coxandkings.travel.operations.model.fullCancellation.SupplierUniqueRef;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ProductDetail;
import com.coxandkings.travel.operations.repository.fullcancellation.FullCancellationKafkaMessageRepository;
import com.coxandkings.travel.operations.repository.fullcancellation.SupplierUniqueRefRepository;
import com.coxandkings.travel.operations.resource.fullcancellation.OrderCancellation;
import com.coxandkings.travel.operations.resource.fullcancellation.OrderStatusResponse;
import com.coxandkings.travel.operations.resource.fullcancellation.OrderStatusUpdateResource;
import com.coxandkings.travel.operations.resource.refund.RefundResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.fullcancellation.*;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.refund.RefundService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.DateTimeUtil;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service("fullCancellation")
public class FullCancellationImpl implements FullCancellationService {

    private static final Logger logger = LogManager.getLogger(FullCancellationImpl.class);

    @Value("${full_cancellation.externet_supplier_back_link}")
    private String externetSupplierBackLink;

    @Value("${full_cancellation.inventory_supplier_back_link}")
    private String inventorySupplierBackLink;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private MDMService mdmService;

    @Autowired
    private Communication communication;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private SupplierUniqueRefRepository supplierUniqueRefRepository;

    @Autowired
    private BEService beService;

    @Autowired
    private FinanceService financeService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier(value = "mDMToken")
    private MDMToken mdmToken;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @Autowired
    private FullCancellationKafkaMessageRepository fullCancellationKafkaMessageRepository;

    @Override
    public void process(KafkaBookingMessage kafkaBookingMessage) throws OperationException {

        logger.info("***Full Cancellation: started Full cancellation process " + kafkaBookingMessage);
        Optional<OpsProduct> opsProductOptional = getOpsProduct(kafkaBookingMessage);
        OpsProduct opsProduct = null;

        saveKafkaMessage(kafkaBookingMessage);

        if (opsProductOptional.isPresent()) {
            opsProduct = opsProductOptional.get();
        } else {
            logger.error("Not able to get booking details booking no:" + kafkaBookingMessage.getBookId() + " Order Id" + kafkaBookingMessage.getOrderNo());
            throw new OperationException("Not able to get booking details booking no:" + kafkaBookingMessage.getBookId() + " Order Id" + kafkaBookingMessage.getOrderNo());
        }
        //create alert for user
        try {
            opsUserAlert(kafkaBookingMessage, "request");
        } catch (Exception e) {
            logger.error("Unable to create full cancellation alert for ops", e);
        }

        //check request is online or offline request
        String supplierType = onlineOrOfflineOrder(opsProduct);
        if (null == supplierType) {
            logger.error("Order booking is not defined is online or offline orderId\" + kafkaBookingMessage.getOrderNo()");
            throw new OperationException("Order booking is not defined is online or offline orderId" + kafkaBookingMessage.getOrderNo());

        } else {
            logger.info("*** Full Cancellation:Supplier type " + supplierType + "***");

            String errorCode = kafkaBookingMessage.getErrorCode();
            if (errorCode != null && errorCode.trim().length() > 0) { //Handle for Offline Supplier or Err'rd scenarios
//            if ("FAILURE".equalsIgnoreCase(kafkaBookingMessage.getStatus())) {
                //check it is booked through inventory
                logger.info("*** Full Cancellation: booking cancellation failed by booking engine handling operation ***");
                if (isBookedInventory(opsProduct)) {
                    logger.info("*** Full Cancellation: Booking is booked through inventory ***");
                    //send mail to supplier saying that cut of date reached can you please cancel this booking
                    inventoryBooking(kafkaBookingMessage, opsProduct);
                } else {
                    //then this is may be online booking which is BE is not able to cancel or this may be offline booking that need to handle
                    offlineBooking(kafkaBookingMessage, opsProduct);
                }
            } else {
                //if it is success then it is online booking or Inventory booking which is not reached to inventory cut of date
                onlineBooking(kafkaBookingMessage, opsProduct);

            }
        }
    }

    @Transactional
    private void saveKafkaMessage(KafkaBookingMessage kafkaBookingMessage) throws OperationException {
        FullCancellationKafkaMessage fullCancellationKafkaMessage = new FullCancellationKafkaMessage();
        FullCancellationIdentity fullCancellationIdentity = new FullCancellationIdentity(kafkaBookingMessage.getBookId(), kafkaBookingMessage.getOrderNo());
        fullCancellationIdentity.setBookId(kafkaBookingMessage.getBookId());
        fullCancellationIdentity.setOrderNo(kafkaBookingMessage.getOrderNo());
        fullCancellationKafkaMessage.setFullCancellationIdentity(fullCancellationIdentity);
        fullCancellationKafkaMessage.setActionType(kafkaBookingMessage.getActionType());
        fullCancellationKafkaMessage.setErrorCode(kafkaBookingMessage.getErrorCode());
        fullCancellationKafkaMessage.setErrorMessage(kafkaBookingMessage.getErrorMessage());
        fullCancellationKafkaMessage.setOperation(kafkaBookingMessage.getOperation());
        try {
            fullCancellationKafkaMessageRepository.saveAndUpdate(fullCancellationKafkaMessage);
        } catch (Exception e) {
            logger.error("Not able to save kafka message ", e);
            throw new OperationException("Not able to save kafka message");
        }
    }

    private void inventoryBooking(KafkaBookingMessage kafkaBookingMessage, OpsProduct opsProduct) throws OperationException {
        String supplierID = opsProduct.getSupplierID();
        String supplierEmail = getSupplierEmail(supplierID);
        if (supplierEmail.isEmpty()) {
            logger.error("Supplier email Address is not specified");
            throw new OperationException("Supplier email Address is not specified");

        } else {
            String supplierEmailLink = generateMailLinkForInventory(kafkaBookingMessage, supplierID);

            //If supplier address exit send mail
            if (communication.sendMailInventorySupplier(supplierEmail, supplierEmailLink, kafkaBookingMessage)) {
                logger.debug("Email sent to supplier" + supplierEmail);
            } else {
                logger.error("Not able to send mail " + supplierEmail + " to supplier for cancellation ");
            }
        }
    }

    private boolean isBookedInventory(OpsProduct opsProduct) {
        boolean expression;
        if ("N".equalsIgnoreCase(opsProduct.getInventory())) {
            expression = false;

        } else {
            expression = true;
        }
        return expression;
    }

    private void onlineBooking(KafkaBookingMessage kafkaBookingMessage, OpsProduct opsProduct) throws OperationException {
        logger.info("*** Full Cancellation: handling online booking flow ***" + kafkaBookingMessage + "***");
        OpsOrderStatus opsOrderStatus = opsProduct.getOrderDetails().getOpsOrderStatus();
        logger.info("***Full Cancellation: orderStatus " + opsOrderStatus);
        if (OpsOrderStatus.XL == opsOrderStatus) {
            OrderStatusUpdateResource orderStatusUpdateResource = new OrderStatusUpdateResource();
            orderStatusUpdateResource.setOrderStatus(OpsOrderStatus.XL);
            orderStatusUpdateResource.setOrderId(kafkaBookingMessage.getOrderNo());
            orderStatusUpdateResource.setBookId(kafkaBookingMessage.getBookId());
            updateOrderStatus(orderStatusUpdateResource);
        } else {
            logger.info("*** Full Cancellation: Order status is not cancelled handling offline flow***" + kafkaBookingMessage + "***");
            offlineBooking(kafkaBookingMessage, opsProduct);
        }
    }

    private void offlineBooking(KafkaBookingMessage kafkaBookingMessage, OpsProduct product) throws OperationException {
        logger.info("*** Full Cancellation: handling offline booking flow ***" + kafkaBookingMessage + "***");
        String supplierID = product.getSupplierID();
        String clientId = getClientId(kafkaBookingMessage);
        try {
            // update order status RXL in BE
            beService.updateOrderStatus(product.getOpsProductCategory(), product.getOpsProductSubCategory(), OpsOrderStatus.RXL, kafkaBookingMessage.getOrderNo());
        } catch (UnsupportedEncodingException e) {
            logger.error("Not able to get token", e);
            throw new OperationException("Not able to get token");
        }

        String supplierEmail = getSupplierEmail(supplierID);

        if (supplierEmail.isEmpty()) {
            logger.error("Supplier email Address is not specified");
            throw new OperationException("Supplier email Address is not specified");

        } else {

            //If supplier address exit send mail
            if (communication.sendMailSupplier(supplierEmail, kafkaBookingMessage, product.getSupplierRefNumber())) {
                logger.debug("Email sent to supplier" + supplierEmail);
            } else {
                logger.error("Not able to send mail " + supplierEmail + " to supplier for cancellation ");
            }

            //create to-do task for ops user
            createToDoTask(product, ToDoFunctionalAreaValues.OPERATIONS, clientId, kafkaBookingMessage);

        }

        if (isExtranetSupplier(supplierID)) {
            logger.info("*** Full Cancellation: Supplier is extranet Supplier " + supplierID + "***");
            //Supplier is extranet
            //create to-do task for supplier user
            createToDoTask(product, ToDoFunctionalAreaValues.EXTERNET_SUPPLIER, clientId, kafkaBookingMessage);
            //updating invoice

            OrderStatusUpdateResource orderStatusUpdateResource = new OrderStatusUpdateResource();
            orderStatusUpdateResource.setBookId(kafkaBookingMessage.getBookId());
            orderStatusUpdateResource.setOrderId(kafkaBookingMessage.getOrderNo());
            orderStatusUpdateResource.setOrderStatus(OpsOrderStatus.XL);
            updateOrderStatus(orderStatusUpdateResource);


        } else {
            logger.info("*** Full Cancellation:  Supplier is not extranet Supplier " + supplierID + "***");
            //If supplier is not extranet supplier
            //send email to supplier with email link
            String supplierEmailLink = generateMailLink(kafkaBookingMessage, supplierID);
            communication.sendMailNotExternetSupplier(supplierEmail, supplierEmailLink, kafkaBookingMessage);

        }
    }

    private String getSupplierEmail(String supplierID) throws OperationException {
        String supplierEmail = null;
        try {
            supplierEmail = jsonObjectProvider.getAttributeValue(getSupplierJson(supplierID), "$.contactInfo.contactDetails.email", String.class);
        } catch (Exception e) {
            logger.error("*** Full Cancellation: Unable to get Supplier Email Address from MDM", e);
            throw new OperationException("Unable to get Supplier Email Address form MDM");
        }
        return supplierEmail;
    }

    private String getClientId(KafkaBookingMessage kafkaBookingMessage) throws OperationException {
        OpsBooking opsBooking = null;

        opsBooking = opsBookingService.getBooking(kafkaBookingMessage.getBookId());

        return opsBooking.getClientID();
    }

    private void opsUserAlert(KafkaBookingMessage kafkaBookingMessage, String alertType) throws OperationException {

        communication.sendAlert(kafkaBookingMessage, alertType);
    }


    /**
     * This method will provide the supplier type
     *
     * @return
     */

    private String onlineOrOfflineOrder(OpsProduct opsProduct) {

        return opsProduct.getOrderDetails().getSupplierType().getSupplierType();

    }

    private String generateMailLink(KafkaBookingMessage kafkaBookingMessage, String supplierID) {

        SupplierUniqueRef supplierUniqueRef = new SupplierUniqueRef();
        if (null != kafkaBookingMessage) {
            supplierUniqueRef.setBookId(kafkaBookingMessage.getBookId());
            supplierUniqueRef.setOrderId(kafkaBookingMessage.getOrderNo());
            supplierUniqueRef.setSupplierId(supplierID);
            SupplierUniqueRef supplierUniqueRef1 = supplierUniqueRefRepository.saveAndUpdate(supplierUniqueRef);
            String supplierLink = externetSupplierBackLink.concat("/").concat(supplierUniqueRef1.getId());
            logger.info("Supplier back link " + supplierLink);
            return supplierLink;

        }
        return null;
    }

    private String generateMailLinkForInventory(KafkaBookingMessage kafkaBookingMessage, String supplierID) {


        SupplierUniqueRef supplierUniqueRef = new SupplierUniqueRef();
        if (null != kafkaBookingMessage) {
            supplierUniqueRef.setBookId(kafkaBookingMessage.getBookId());
            supplierUniqueRef.setOrderId(kafkaBookingMessage.getOrderNo());
            supplierUniqueRef.setSupplierId(supplierID);
            SupplierUniqueRef supplierUniqueRef1 = supplierUniqueRefRepository.saveAndUpdate(supplierUniqueRef);
            return inventorySupplierBackLink.concat("/").concat(supplierUniqueRef1.getId());

        }
        return null;
    }

    private Optional<OpsProduct> getOpsProduct(KafkaBookingMessage kafkaBookingMessage) throws OperationException {
        OpsBooking opsBooking;

        opsBooking = opsBookingService.getBooking(kafkaBookingMessage.getBookId());

        if (opsBooking == null) {
            logger.error("Invalid Booking ReferenceNo");
            throw new OperationException("Invalid Booking referenceNo");
        }


        List<OpsProduct> products = opsBooking.getProducts();

        String orderNo = kafkaBookingMessage.getOrderNo();

        return products.stream().
                filter(p -> p.getOrderID().equals(orderNo)).
                findFirst();
    }

    @Override
    public OrderStatusResponse updateOrderStatus(OrderStatusUpdateResource orderStatusUpdateResource) throws OperationException {
        logger.info("***Full Cancellation updating order status ***" + orderStatusUpdateResource.getOrderStatus());
        FullCancellationIdentity fullCancellationIdentity = new FullCancellationIdentity(orderStatusUpdateResource.getBookId(), orderStatusUpdateResource.getOrderId());
        FullCancellationKafkaMessage fullCancellationKafkaMessage = fullCancellationKafkaMessageRepository.getFullCancellationKafkaMessage(fullCancellationIdentity);

        KafkaBookingMessage kafkaBookingMessage = new KafkaBookingMessage();
        kafkaBookingMessage.setBookId(orderStatusUpdateResource.getBookId());
        kafkaBookingMessage.setOrderNo(orderStatusUpdateResource.getOrderId());


        //send mail to customer
        OpsBooking booking = null;

        booking = opsBookingService.getBooking(orderStatusUpdateResource.getBookId());


        Optional<OpsProduct> opsProductOptional = getOpsProduct(kafkaBookingMessage);
        OpsProduct product = null;
        if (opsProductOptional.isPresent()) {
            product = opsProductOptional.get();
        } else {
            logger.error("Unable to get Order Id:" + kafkaBookingMessage.getOrderNo());
            throw new OperationException("Unable to get order details " + kafkaBookingMessage.getOrderNo());
        }
        try {
            if (null != product) {
                // update order status XL in BE
                beService.updateOrderStatus(product.getOpsProductCategory(), product.getOpsProductSubCategory(), OpsOrderStatus.XL, kafkaBookingMessage.getOrderNo());
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Not able to get token", e);
            throw new OperationException("Not able to get token");
        }
        String clientID = booking.getClientID();
        String clientType = booking.getClientType();
        String clientEmail = null;
        OrderStatusResponse orderStatusResponse = new OrderStatusResponse();
        try {
            clientEmail = clientMasterDataService.getClientEmailId(clientID, MDMClientType.fromString(clientType.toUpperCase()));
        } catch (OperationException e) {
            logger.error("Enable to get client email address", e);
            throw new OperationException("Enable to get client email address");
        }
        //send email to client to info
        if (null != clientEmail) {

            boolean emailStatus = communication.sendMailClient(clientEmail, booking, kafkaBookingMessage);

            if (emailStatus) {

                orderStatusResponse.setStatus("SUCCESS");
                orderStatusResponse.setMesssage("Email sent to client");
            } else {
                orderStatusResponse.setStatus("ERROR");
                orderStatusResponse.setMesssage("Not able to sent email client");

            }
        }

        String errorCode = null;
        if (null != fullCancellationKafkaMessage) {
            fullCancellationKafkaMessage.getErrorCode();
        }

        if (errorCode == null || errorCode.trim().length() == 0) {

            OrderCancellation cancellations = null;
            //Todo:get cancellation charges from BE , Booking engine needs to store cancellation policy
            cancellations = beService.getCancellations(kafkaBookingMessage);
            double cancellationAmount = 0;

            if (null != cancellations) {
                cancellationAmount = cancellations.getCompanyCancelCharges() + cancellations.getSupplierCancelCharges();
            } else {
                logger.error("Not able to get cancellation charge for orderId:" + kafkaBookingMessage.getOrderNo());
                // throw new OperationException("Not able to get cancellation charges for orderId:" + kafkaBookingMessage.getOrderNo());*/
                cancellationAmount =0;
            }
            //get the invoice

            //update finance invoice
            InvoiceDto invoice = financeService.getInvoicesByBookingDetails(kafkaBookingMessage);

            //checking of refund

            if (cancellationAmount < invoice.getTotalCost() && null != product) {
                RefundResource refundResource = new RefundResource();
                refundResource.setClientId(booking.getClientID());
                refundResource.setClientName(invoice.getClient().getClientName());
                refundResource.setClientType(booking.getClientType());
                ProductDetail productDetail = new ProductDetail();
                productDetail.setOrderId(product.getOrderID());
                productDetail.setProductCategory(product.getProductCategory());
                productDetail.setProductName(product.getProductName());
                productDetail.setProductCategorySubType(product.getProductSubCategory());
                refundResource.setProductDetail(productDetail);
                refundResource.setBookingReferenceNo(orderStatusUpdateResource.getBookId());
                refundResource.setRefundType(RefundTypes.REFUND_REDEEMABLE);

                refundResource.setRoeAsInClaim(product.getRoe());
                refundResource.setRoeRequested(product.getRoe());
                refundResource.setDefaultModeOfPayment(booking.getPaymentInfo().get(0).getPaymentMethod());
                refundResource.setRequestedModeOfPayment(booking.getPaymentInfo().get(0).getPaymentMethod());

                refundResource.setReasonForRequest(ReasonForRequest.CANCELLATION);


                double refundAmount = invoice.getTotalCost() - cancellationAmount;
                refundResource.setRefundAmount(BigDecimal.valueOf(refundAmount));
                refundResource.setClaimCurrency(booking.getClientCurrency());
                refundResource.setRefundStatus(RefundStatus.PENDING_WITH_OPS.getStatus());
                try {
                    refundService.add(refundResource);
                } catch (Exception e) {
                    logger.error("***Full Cancellation: Unable to create refund claim" + refundResource);
                }

            }
            financeService.updateInvoice(cancellations, kafkaBookingMessage);
            logger.info("*** Full Cancellation:" + orderStatusResponse);
        }
        return orderStatusResponse;
    }

    @Override
    public OrderStatusResponse updateOrderStatus(String supplierUniqueRefId) throws
            ParseException, OperationException {
        logger.info("***Full Cancellation Supplier Accepted email request for cancellation *****");
        SupplierUniqueRef supplierUniqueRef = supplierUniqueRefRepository.getSupplierUniqueRefById(supplierUniqueRefId);
        if (null == supplierUniqueRef) {
            logger.error("Unable to get supplier unique reference Id details: " + supplierUniqueRefId);
            throw new OperationException("Unable to get supplier unique reference Id details: " + supplierUniqueRefId);
        }
        KafkaBookingMessage kafkaBookingMessage = new KafkaBookingMessage();
        kafkaBookingMessage.setBookId(supplierUniqueRef.getBookId());
        kafkaBookingMessage.setOrderNo(supplierUniqueRef.getOrderId());

        //create alert for ops user supplier confirm booking
        opsUserAlert(kafkaBookingMessage, "supplierConfirm");
        //updating order status

        OrderStatusUpdateResource orderStatusUpdateResource = new OrderStatusUpdateResource();
        orderStatusUpdateResource.setBookId(supplierUniqueRef.getBookId());
        orderStatusUpdateResource.setOrderId(supplierUniqueRef.getOrderId());
        orderStatusUpdateResource.setOrderStatus(OpsOrderStatus.XL);
        return updateOrderStatus(orderStatusUpdateResource);

    }

    @Override
    public OrderStatusResponse updateInventoryStatus(String supplierUniqueRefId) throws
            ParseException, OperationException {

        SupplierUniqueRef supplierUniqueRef = supplierUniqueRefRepository.getSupplierUniqueRefById(supplierUniqueRefId);
        logger.info("***Full Cancellation: updating inventory status " + supplierUniqueRef + "***");
        if (null == supplierUniqueRef) {
            logger.error("Unable to get supplier unique reference Id details: " + supplierUniqueRefId);
            throw new OperationException("Unable to get supplier unique reference Id details: " + supplierUniqueRefId);
        }
        KafkaBookingMessage kafkaBookingMessage = new KafkaBookingMessage();
        kafkaBookingMessage.setBookId(supplierUniqueRef.getBookId());
        kafkaBookingMessage.setOrderNo(supplierUniqueRef.getOrderId());
        //update inventory using BE api to decrease count
        beService.updateInventoryStatus(kafkaBookingMessage);
        return updateOrderStatus(supplierUniqueRefId);

    }

    private void createToDoTask(OpsProduct product, ToDoFunctionalAreaValues toDoFunctionalAreaValues, String
            clientId, KafkaBookingMessage kafkaBookingMessage) throws OperationException {
        //creating to do task for finance user
        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();

        toDoTaskResource.setBookingRefId(kafkaBookingMessage.getBookId());
        toDoTaskResource.setReferenceId(kafkaBookingMessage.getOrderNo()); //OPS DB ClientCommercialClaimNO
        String userID = userService.getSystemUserIdFromMDMToken();
        toDoTaskResource.setCreatedByUserId(userID);
        toDoTaskResource.setProductId(product.getOrderID());
        toDoTaskResource.setClientId(clientId);
        toDoTaskResource.setClientCategoryId("Not sure");
        toDoTaskResource.setClientSubCategoryId("Not Sure");
        toDoTaskResource.setClientTypeId("Not sure");
        toDoTaskResource.setCompanyId("Not Sure");
        toDoTaskResource.setCompanyMarketId("Not Sure");
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.FULL_CANCELLATION.toString());
        toDoTaskResource.setTaskFunctionalAreaId(toDoFunctionalAreaValues.getValue()); // OPERATIONS or FINANCE
        toDoTaskResource.setTaskStatusId("Assigned"); //ASSIGN as per my assumption
        toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue()); // Dummy value HIGH *
        toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());

        toDoTaskResource.setSuggestedActions("");//sent the rest end point for now

        ZonedDateTime travelDate = getTravelDate(kafkaBookingMessage);
        ZonedDateTime kpiDate = getKpi(clientId);
        if (null != travelDate && travelDate.isBefore(kpiDate)) {
            kpiDate = travelDate;
        }

        toDoTaskResource.setDueOnDate(kpiDate);


        try {
            logger.info("Creating todo task" + toDoTaskResource);
            toDoTaskService.save(toDoTaskResource);
        } catch (Exception e) {
            logger.error("Unable to create finance toDo Task", e);

        }
    }

    private String getSupplierJson(String supplierID) throws OperationException {
        String supplierInfo;
        try {
            supplierInfo = mdmService.getSupplierInfo(supplierID);
        } catch (OperationException e) {

            logger.error("Not able to get supplier information", e);
            throw new OperationException("Not able to get supplier information");

        }
        if (supplierInfo == null || supplierInfo.trim().length() == 0) {
            logger.error("Supplier not found " + supplierID);
            throw new OperationException("Supplier not found " + supplierID);

        }
        return supplierInfo;
    }

    /**
     * This method will provide supplier is extranet or not
     *
     * @param supplierNo
     * @return
     * @throws OperationException
     */
    private boolean isExtranetSupplier(String supplierNo) throws OperationException {

        return (Boolean) jsonObjectProvider.getChildObject(getSupplierJson(supplierNo), "$.extranetAccess", Boolean.class);


    }

    /**
     * GetKPI() will return the kpi cut of date.
     * It will take current date and add day and hours in that.
     *
     * @param clientId
     * @return ZonedDateTime
     * @throws OperationException
     */
    private ZonedDateTime getKpi(String clientId) throws OperationException {

        // ClientKPIResource clientKPIResource = mdmService.getKPIDate(clientId);
        //  String equation = clientKPIResource.getKpiMeasure().getMeasure();
      /*  int days = Integer.parseInt(clientKPIResource.getKpiMeasure().getAchievingPeriod().getValue());
        String[] time = clientKPIResource.getKpiMeasure().getAchievingPeriod().getTime().split(":");
        long hour = Long.parseLong(time[0]);
        long minutes = Long.parseLong(time[1]);*/
        ZonedDateTime currentTime = ZonedDateTime.now();
        // ZonedDateTime kpiDateTime = currentTime.plusDays(days).plusMinutes(minutes).plusHours(hour);

       /* if ("Equal To".equalsIgnoreCase(equation)) {

        } else if ("Greater Than".equalsIgnoreCase(equation) || "Less Than".equalsIgnoreCase(equation)) {

        } else if ("Greater Than Equal".equalsIgnoreCase(equation) || "Less Than Equal".equalsIgnoreCase(equation)) {

        }*/

        return DateTimeUtil.formatBEDateTimeZone(currentTime.toString());


    }

    private ZonedDateTime getTravelDate(KafkaBookingMessage kafkaBookingMessage) throws OperationException {
        Optional<OpsProduct> opsProductOptional = getOpsProduct(kafkaBookingMessage);
        OpsProduct opsProduct = null;
        if (opsProductOptional.isPresent()) {
            opsProduct = opsProductOptional.get();


            ZonedDateTime travelDate = null;
            if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {

                List<OpsRoom> rooms = opsProduct.getOrderDetails().getHotelDetails().getRooms();
                Collections.sort(rooms, (r1, r2) -> {
                    try {
                        return DateTimeUtil.formatBEDateTime(r1.getCheckIn()).compareTo(DateTimeUtil.formatBEDateTime(r2.getCheckIn()));
                    } catch (ParseException e) {
                        logger.error("Unable to parse the date", e);

                    }
                    return 0;
                });
                String checkInDate = rooms.get(0).getCheckIn();
                travelDate = DateTimeUtil.formatBEDateTimeZone(checkInDate);
            } else if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {

                List<OpsFlightSegment> flightSegments = opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment();
                Collections.sort(flightSegments, (f1, f2) -> f1.getDepartureDateZDT().compareTo(f2.getDepartureDateZDT()));
                travelDate = flightSegments.get(0).getDepartureDateZDT();
            }
            return travelDate;
        }

        //if we don't found any date
        return DateTimeUtil.formatBEDateTimeZone(ZonedDateTime.now().toString());

    }
}
