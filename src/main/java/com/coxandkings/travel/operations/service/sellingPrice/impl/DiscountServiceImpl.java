package com.coxandkings.travel.operations.service.sellingPrice.impl;

import com.coxandkings.travel.ext.model.be.Booking;
import com.coxandkings.travel.ext.model.be.OrderTotalPriceInfo;
import com.coxandkings.travel.ext.model.be.Product;
import com.coxandkings.travel.operations.beconsumer.BEConstants;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;
import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.enums.sellingPrice.ApprovalStatus;
import com.coxandkings.travel.operations.enums.sellingPrice.DiscountType;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ProductDetail;
import com.coxandkings.travel.operations.model.sellingPrice.Discount;
import com.coxandkings.travel.operations.model.sellingPrice.SellingPriceApprovalComponent;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.model.todo.ToDoTaskGeneratedType;
import com.coxandkings.travel.operations.repository.sellingPrice.DiscountRepository;
import com.coxandkings.travel.operations.repository.todo.ToDoTaskRepository;
import com.coxandkings.travel.operations.resource.amendentitycommercial.MarginDetails;
import com.coxandkings.travel.operations.resource.refund.RefundResource;
import com.coxandkings.travel.operations.resource.sellingPrice.*;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.refund.RefundService;
import com.coxandkings.travel.operations.service.sellingPrice.DiscountService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.MarginCalculatorUtil;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.adapter.OpsBookingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    ToDoTaskService toDoTaskService;

    @Autowired
    ToDoTaskRepository toDoTaskRepository;

    @Autowired
    OpsBookingService opsBookingService;

    @Autowired
    OpsBookingAdapter opsBookingAdapter;

    @Autowired
    RefundService refundService;

    @Autowired
    private UserService userService;

    @Value(value = "${booking_engine.update.air.order-price}")
    private String updateAirOrderTotalPrice;

    @Value(value = "${booking_engine.update.acco.order-price}")
    private String updateAccoOrderTotalPrice;

    @Autowired
    MarginCalculatorUtil marginCalculatorUtil;

    public OpsBooking setApplicability(OpsBooking opsBooking) {
        opsBooking.setProducts(opsBooking.getProducts().stream().map(opsProduct -> checkApplicability(opsProduct)).collect(Collectors.toList()));
        return opsBooking;
    }

    private OpsProduct checkApplicability(OpsProduct opsProduct) {
        //TODO: write logic here to decide whether opsProduct is applicable for discount on selling price or not.
        return opsProduct;
    }

    private SellingPrice validateDiscount(DiscountResource discountResource) throws OperationException, IOException {
        if (StringUtils.isEmpty(discountResource.getBookingRefId()) ||
                StringUtils.isEmpty(discountResource.getOrderId())) {
            throw new OperationException("Please provide booking reference id, product id and room id");
        }

        if (discountResource.getAmount() == null) {
            throw new OperationException("Discount type cannot be null");
        }

//        if(StringUtils.isEmpty(discountResource.getClientCurrencyCode())) {
//            throw new OperationException("Client currency cannot be null");
//        }

        Boolean discountType = discountResource.getAmount();
        if (discountType == null) {
            throw new OperationException("Invalid discount type");
        }

        OpsBooking aOpsBooking = opsBookingService.getBooking(discountResource.getBookingRefId());

        OpsProduct opsProduct = opsBookingService.getOpsProduct(aOpsBooking , discountResource.getOrderId());
        String productCategory = opsProduct.getProductCategory();
        String productSubCatefgory = opsProduct.getProductSubCategory();

        OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(productCategory);
        OpsProductSubCategory opsProductSubCategory;
        if (opsProductCategory != null) {
            opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory, productSubCatefgory);
            if (opsProductSubCategory == null) {
                throw new OperationException("Invalid product sub category");
            }
        } else {
            throw new OperationException("Invalid product category");
        }

        if (discountResource.getAmount()) {
            if (discountResource.getDiscountAmount() == null) {
                throw new OperationException("Discount amount cannot be null when discount type is amount");
            }

            BigDecimal totalPrice = getTotalPrice(opsProduct, opsProductCategory, opsProductSubCategory);
            if (totalPrice != null) {
                if (discountResource.getDiscountAmount().compareTo(totalPrice) > 0) {
                    throw new OperationException("Discount amount cannot exceed total price");
                }
            }
        } else {
            if (discountResource.getDiscountPercentage() == null) {
                throw new OperationException("Discount percentahge cannot be null when discount type is percentage");
            }

            if (discountResource.getDiscountPercentage() > 100) {
                throw new OperationException("Discount percentage cannot be greater than 100");
            }

            if (discountResource.getSellingPriceComponent() == null || discountResource.getSellingPriceComponent().isEmpty()) {
                throw new OperationException("At least one selling price component should be selected to apply discount in percentage");
            }
        }

        return calculateSellingPrice(discountResource, opsProduct, opsProductCategory, opsProductSubCategory, discountType);
    }

    private BigDecimal getTotalPrice(OpsProduct opsProduct, OpsProductCategory opsProductCategory, OpsProductSubCategory opsProductSubCategory) {
        switch (opsProductCategory) {
            case PRODUCT_CATEGORY_ACCOMMODATION: {
                switch (opsProductSubCategory) {
                    case PRODUCT_SUB_CATEGORY_HOTELS: {
                        return new BigDecimal(opsProduct.getOrderDetails().getHotelDetails().getOpsAccommodationTotalPriceInfo().getTotalPrice());
                    }
                }
            }
            break;
            case PRODUCT_CATEGORY_TRANSPORTATION: {
                switch (opsProductSubCategory) {
                    case PRODUCT_SUB_CATEGORY_FLIGHT: {
                        return new BigDecimal(opsProduct.getOrderDetails().getFlightDetails().getTotalPriceInfo().getTotalPrice());
                    }
                }
            }
        }
        return null;
    }

    private SellingPrice calculateSellingPrice(DiscountResource discountResource, OpsProduct opsProduct, OpsProductCategory productCategory, OpsProductSubCategory productSubCategory, Boolean discountType) throws OperationException, IOException {
        SellingPriceTable sellingPriceTable = new SellingPriceTable();

        SellingPrice originalSellingPrice;
        ProductInformation productInformation = new ProductInformation(discountResource.getBookingRefId(),
                discountResource.getOrderId());
        originalSellingPrice = getOriginalSellingPrice(productInformation);

        List<SellingPriceComponent> sellingPriceComponents = originalSellingPrice.getSellingPriceComponents();
        if (discountType) {
            Optional<SellingPriceComponent> optionalSellingPriceComponent = sellingPriceComponents.stream()
                    .filter(t -> t.getPriceComponentCode().equalsIgnoreCase("Discount"))
                    .findFirst();
            SellingPriceComponent sellingPriceComponent;
            sellingPriceComponent = optionalSellingPriceComponent.orElseGet(SellingPriceComponent::new);
            sellingPriceComponent.setAmount(discountResource.getDiscountAmount());

            optionalSellingPriceComponent = sellingPriceComponents.stream()
                    .filter(t -> t.getPriceComponentCode().equalsIgnoreCase("Base Fare"))
                    .findFirst();
            sellingPriceComponent = optionalSellingPriceComponent.orElseGet(SellingPriceComponent::new);
            sellingPriceComponent.setAmount(sellingPriceComponent.getAmount().subtract(discountResource.getDiscountAmount()));
        } else {
            List<SellingPriceComponent> applyDisocuntOnComponents = sellingPriceComponents.stream()
                    .filter(t -> discountResource.getSellingPriceComponent().stream().anyMatch(r -> r.equalsIgnoreCase(t.getPriceComponentCode())))
                    .collect(Collectors.toList());

            BigDecimal totalDiscount = applyDisocuntOnComponents.stream().map(t -> t.getAmount().multiply(new BigDecimal(discountResource.getDiscountPercentage() / 100))).reduce(new BigDecimal(0), BigDecimal::add);

            applyDisocuntOnComponents.forEach(t -> t.setAmount(t.getAmount().subtract(t.getAmount().multiply(new BigDecimal(discountResource.getDiscountPercentage() / 100)))));

            sellingPriceComponents.stream()
                    .filter(t -> t.getPriceComponentCode().equalsIgnoreCase("discount"))
                    .findFirst()
                    .ifPresent(sellingPriceComponent -> sellingPriceComponent.setAmount(totalDiscount));
        }
        BigDecimal addition = sellingPriceComponents.stream().filter(t -> !t.getPriceComponentCode().equalsIgnoreCase("discount")).map(SellingPriceComponent::getAmount).reduce(new BigDecimal(0), BigDecimal::add);
        originalSellingPrice.setTotalAmount(addition);

        switch (productCategory) {
            case PRODUCT_CATEGORY_TRANSPORTATION: {
                switch (productSubCategory) {
                    case PRODUCT_SUB_CATEGORY_FLIGHT: {
                        sellingPriceTable.setTotalSellingPrice(originalSellingPrice.getTotalAmount());
                        sellingPriceTable.setTotalSupplierPrice(new BigDecimal(opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getSupplierPrice()));
                    }
                }
            }
            break;

            case PRODUCT_CATEGORY_ACCOMMODATION: {
                switch (productSubCategory) {
                    case PRODUCT_SUB_CATEGORY_HOTELS: {
                        sellingPriceTable.setTotalSellingPrice(originalSellingPrice.getTotalAmount());
                        sellingPriceTable.setTotalSupplierPrice(new BigDecimal(opsProduct.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getSupplierPrice()));

                    }
                }
            }
        }

        sellingPriceTable.setMargin(sellingPriceTable.getTotalSellingPrice().subtract(sellingPriceTable.getTotalSupplierPrice()));
        originalSellingPrice.setSellingPriceTable(sellingPriceTable);
        return originalSellingPrice;
    }

    @Override
    public SellingPrice estimateRevisedSellingPrice(DiscountResource discountResource) throws OperationException, IOException {
        return validateDiscount(discountResource);
    }

    private Discount saveDiscount(Discount discount) {
        return discountRepository.createDiscount(discount);
    }

    @Override
    public Discount approveDiscount(DiscountResource discountResource) throws OperationException, ParseException, IllegalAccessException, InvocationTargetException, IOException, JSONException {
        String id = discountResource.getId();
        String userId = discountResource.getCreatedByUserId();

        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(userId))
            throw new OperationException("Id or user id should not be null");

        Discount discount = discountRepository.getDiscount(id);
        ApprovalStatus currentStatus = discount.getApprovalStatus();

        switch (currentStatus) {
            case PENDING: {
                discount.setApprovalStatus(ApprovalStatus.APPROVED);
                discount.setApprovalTime(ZonedDateTime.now());
                discount.setApprover(userId);
                discount = saveDiscount(discount);
                successApproval(discount);

                ToDoTask task = toDoTaskService.getById(discount.getApprovalTaskId());
                ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
                toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.COMPLETED.getValue());
                toDoTaskResource.setId(task.getId());
                toDoTaskResource.setCreatedByUserId(discountResource.getCreatedByUserId());
                toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.SELLING_PRICE.getSubTaskType());
                toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
                toDoTaskService.save(toDoTaskResource);
            }
            break;
            case REJECTED: {
                throw new OperationException("A record once rejected cannot be approved");
            }
            case APPROVED: {
                throw new OperationException("The record is already approved");
            }

        }

        return discount;
    }

//    private Refund refundProcess(OpsBooking opsBooking, OpsProduct opsProduct, String roomId, String passengerId, ReasonForRequest reasonForRequest, BigDecimal ammount) {
//        RefundResource refundResource = new RefundResource();
//        try {
//            refundResource.setBookingReferenceNo(opsBooking.getBookID());
//            refundResource.setClientId(opsBooking.getClientID());
//            refundResource.setClientType(opsBooking.getClientType());
//            refundResource.setClaimCurrency(opsBooking.getClientCurrency());
//
//            ProductDetail productDetail = new ProductDetail();
//            productDetail.setOrderId(opsProduct.getOrderID());
//            productDetail.setProductCategory(opsProduct.getProductCategory());
//            productDetail.setProductCategorySubType(opsProduct.getProductSubCategory());
//
//            refundResource.setProductDetail(productDetail);
//            List<OpsPaymentInfo> paymentInfo = opsBooking.getPaymentInfo(); //todo: need to think
//            refundResource.setDefaultModeOfPayment(paymentInfo.get(0).getPaymentMethod());
//            refundResource.setClaimCurrency(paymentInfo.get(0).getAmountCurrency());
//            refundResource.setReasonForRequest(reasonForRequest);
//            refundResource.setRefundAmount(ammount); // todo: get it from add pax and full cancellation api
//            refundResource.setRefundType(RefundTypes.REFUND_AMOUNT);
//            UsernamePasswordAuthenticationToken userPwdAuth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//            OpsUser loggedInUser = (OpsUser) userPwdAuth.getPrincipal();
//            String userID = loggedInUser.getUserID();
//            refundResource.setCreatedByUserId(userID);
//
//            Refund response = refundService.add(refundResource);
//
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.info(" public Refund refundProcess(OpsBooking opsBooking, OpsProduct opsProduct, String roomId, String passengerId, ReasonForRequest reasonForRequest, BigDecimal ammount)  , exception raised : " + e);
//        }
//        return null;
//    }

    @Override
    public Discount rejectDiscount(DiscountResource discountResource) throws OperationException, ParseException, IllegalAccessException, InvocationTargetException, IOException, JSONException {
        String id = discountResource.getId();
        String userId = discountResource.getCreatedByUserId();

        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(userId))
            throw new OperationException("Id or user id should not be null");

        Discount discount = discountRepository.getDiscount(id);
        ApprovalStatus currentStatus = discount.getApprovalStatus();
        switch (currentStatus) {
            case PENDING: {
                discount.setApprovalStatus(ApprovalStatus.REJECTED);
                discount.setApprovalTime(ZonedDateTime.now());
                discount.setApprover(userId);
                discount = saveDiscount(discount);

                ToDoTask task = toDoTaskService.getById(discount.getApprovalTaskId());

                //TODO: create alert because rejected
                //Alert should have task.getRemark() as message and it should be sent to task.getFileHandlerId()

                ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
                toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.FAILED.getValue());
                toDoTaskResource.setId(task.getId());
                toDoTaskResource.setCreatedByUserId(discountResource.getCreatedByUserId());
                toDoTaskService.save(toDoTaskResource);

                return discount;
            }

            case APPROVED: {
                throw new OperationException("A record once approved cannot be rejected");
            }
        }
        return null;
    }

    @Override
    public Discount getDiscountById(String discountId) {
        return discountRepository.getDiscount(discountId);
    }

    @Override
    public SellingPriceApprovalComponent getApprovalScreenDetails(DiscountResource discountResource) throws OperationException, IOException {
        if (StringUtils.isEmpty(discountResource.getId())) {
            throw new OperationException("Discount id cannot be null");
        }

        Discount discount = discountRepository.getDiscount(discountResource.getId());

        SellingPrice originalSellingPrice = getOriginalSellingPrice(new ProductInformation(discount.getBookingRefId(), discount.getOrderId()));
        SellingPrice revisedSellingPrice = estimateRevisedSellingPrice(getDiscountResource(discount));

        SellingPriceApprovalComponent sellingPriceApprovalComponent = new SellingPriceApprovalComponent();
        sellingPriceApprovalComponent.setDiscount(discount);
        sellingPriceApprovalComponent.setOriginalSellingPrice(originalSellingPrice);
        sellingPriceApprovalComponent.setEstimatedSellingPrice(revisedSellingPrice);

        return sellingPriceApprovalComponent;
    }

    @Override
    public List<String> sellingPriceComponents(ProductInformation productInformation) throws OperationException, IOException {
        if (StringUtils.isEmpty(productInformation.getBookingRefId()) || StringUtils.isEmpty(productInformation.getOrderId())) {
            throw new OperationException("Booking ref id or product id is missing");
        }
        OpsProduct opsProduct = opsBookingService.getProduct(productInformation.getBookingRefId(), productInformation.getOrderId());

        if (StringUtils.isEmpty(opsProduct.getProductCategory()) && StringUtils.isEmpty(opsProduct.getProductSubCategory())) {
            throw new OperationException("AbstractProductFactory category or subcategory cannot be null");
        }


        OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
        OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory, opsProduct.getProductSubCategory());

        List<String> sellingPriceComponents = new ArrayList<>();
        if (opsProductCategory != null) {
            switch (opsProductCategory) {
                case PRODUCT_CATEGORY_ACCOMMODATION: {
                    if (opsProductSubCategory != null) {
                        switch (opsProductSubCategory) {
                            case PRODUCT_SUB_CATEGORY_HOTELS: {
                                OpsHotelDetails opsHotelDetails = opsProduct.getOrderDetails().getHotelDetails();
                                OpsAccommodationTotalPriceInfo opsAccommodationTotalPriceInfo = opsHotelDetails.getOpsAccommodationTotalPriceInfo();

                                if (!StringUtils.isEmpty(opsAccommodationTotalPriceInfo.getTotalPrice())) {
                                    sellingPriceComponents.add("Base fare");
                                }

                                OpsTaxes opsTaxes = opsAccommodationTotalPriceInfo.getOpsTaxes();
                                List<OpsTax> opsTaxList = opsTaxes.getTax();

                                if (opsTaxList == null
                                        || opsTaxList.isEmpty()
                                        || opsTaxList.stream().allMatch(opsTax -> opsTax.getAmount() == 0)) {
                                    if (opsTaxes.getAmount() != null){
                                        if(opsTaxes.getAmount()!=0)
                                            sellingPriceComponents.add("Taxes");
                                    }

                                } else {
                                    sellingPriceComponents.addAll(opsTaxList.stream().filter(opsTax -> opsTax.getAmount() != 0).map(OpsTax::getTaxCode).collect(Collectors.toList()));
                                }

                                return sellingPriceComponents;
                            }
                        }
                    }
                }
                break;
                case PRODUCT_CATEGORY_TRANSPORTATION: {
                    if (opsProductSubCategory != null) {
                        switch (opsProductSubCategory) {
                            case PRODUCT_SUB_CATEGORY_FLIGHT: {
                                OpsFlightDetails opsFlightDetails = opsProduct.getOrderDetails().getFlightDetails();
                                OpsFlightTotalPriceInfo opsFlightTotalPriceInfo = opsFlightDetails.getTotalPriceInfo();

                                OpsBaseFare opsBaseFare = opsFlightTotalPriceInfo.getBaseFare();
                                OpsFees opsFees = opsFlightTotalPriceInfo.getFees();
                                OpsTaxes opsTaxes = opsFlightTotalPriceInfo.getTaxes();
                                OpsReceivables opsReceivables = opsFlightTotalPriceInfo.getReceivables();

                                if (opsBaseFare != null && opsBaseFare.getAmount() != null)
                                    sellingPriceComponents.add("Base fare");

                                List<OpsFee> opsFeeList = opsFees.getFee();
                                if (opsFeeList == null
                                        || opsFeeList.isEmpty()
                                        || opsFeeList.stream().allMatch(opsFee -> opsFee.getAmount() == 0)) {
                                    sellingPriceComponents.add("Fees");
                                } else {
                                    List<String> feePriceComponents = opsFeeList.stream().filter(opsFee -> opsFee.getAmount() != 0).map(OpsFee::getFeeCode).collect(Collectors.toList());
                                    sellingPriceComponents.addAll(feePriceComponents);
                                }

                                List<OpsTax> opsTaxList = opsTaxes.getTax();
                                if (opsTaxList == null
                                        || opsTaxList.isEmpty()
                                        || opsTaxList.stream().allMatch(opsTax -> opsTax.getAmount() == 0)) {
                                    if(opsTaxes.getAmount()!=null){
                                        if (opsTaxes.getAmount() != 0)
                                            sellingPriceComponents.add("Taxes");
                                    }
                                } else {
                                    List<String> taxPriceComponents = opsTaxList.stream().filter(opsTax -> opsTax.getAmount() != 0).map(OpsTax::getTaxCode).collect(Collectors.toList());
                                    sellingPriceComponents.addAll(taxPriceComponents);
                                }
                                if(opsReceivables!=null){
                                    List<OpsReceivable> opsReceivableList = opsReceivables.getReceivable();
                                    if (opsReceivableList == null
                                            || opsReceivableList.isEmpty()
                                            || opsReceivableList.stream().allMatch(opsReceivable -> opsReceivable.getAmount() == 0)) {
                                        if (opsReceivables.getAmount() != 0)
                                            sellingPriceComponents.add("Receivables");
                                    } else {
                                        List<String> receivableComponents = opsReceivableList.stream().filter(opsReceivable -> opsReceivable.getAmount() != 0).map(OpsReceivable::getCode).collect(Collectors.toList());
                                        sellingPriceComponents.addAll(receivableComponents);
                                    }
                                }


                                return sellingPriceComponents;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private DiscountResource getDiscountResource(Discount discount) {
        DiscountResource discountResource = new DiscountResource();
        discountResource.setId(discount.getId());
        discountResource.setBookingRefId(discount.getBookingRefId());
        discountResource.setOrderId(discount.getOrderId());
        discountResource.setClientCurrencyCode(discount.getClientCurrencyCode());

        switch (discount.getDiscountType()) {
            case AMOUNT: {
                discountResource.setAmount(true);
                discountResource.setDiscountAmount(discount.getDiscountAmount());
            }
            break;
            case PERCENTAGE: {
                discountResource.setAmount(false);
                discountResource.setDiscountPercentage(discount.getDiscountPercentage());
                discountResource.setSellingPriceComponent(discount.getSellingPriceComponent());
            }
        }

        discountResource.setCreatedByUserId(discount.getCreatedByUserId());

        return discountResource;
    }

    @Override
    public Discount createDiscount(DiscountResource discountResource) throws OperationException, ParseException, InvocationTargetException, IllegalAccessException, IOException, JSONException {
        return saveDiscount(discountResource);
    }

    private Discount saveDiscount(DiscountResource discountResource) throws OperationException, ParseException, InvocationTargetException, IllegalAccessException, IOException, JSONException {
        Discount discount;
        discount = getDiscount(discountResource);
        setDefaults(discount, discountResource);
        if (discountResource.getAmount()) {
            discount.setDiscountType(DiscountType.AMOUNT);
        } else {
            discount.setDiscountType(DiscountType.PERCENTAGE);
        }
        discount = discountRepository.createDiscount(discount);

        ToDoTask toDoTask = createApprovalTask(discount);
        discount.setApprovalTaskId(toDoTask.getId());
        discount = discountRepository.createDiscount(discount);
        return discount;
    }

    private Discount getDiscount(DiscountResource discountResource) {
        Discount discount = new Discount();

        discount.setBookingRefId(discountResource.getBookingRefId());
        discount.setOrderId(discountResource.getOrderId());

        if (discountResource.getAmount()) {
            discount.setDiscountAmount(discountResource.getDiscountAmount());
            discount.setClientCurrencyCode(discountResource.getClientCurrencyCode());
        } else {
            discount.setDiscountPercentage(discountResource.getDiscountPercentage());
            discount.setSellingPriceComponent(discountResource.getSellingPriceComponent());
            discount.setClientCurrencyCode(discountResource.getClientCurrencyCode());
        }

        return discount;
    }

    private void setDefaults(Discount discount, DiscountResource discountResource) {
        discount.setCreatedByUserId(discountResource.getCreatedByUserId());
        discount.setApprovalStatus(ApprovalStatus.PENDING);
        discount.setCreatedTime(System.currentTimeMillis());
    }

    private ToDoTask createApprovalTask(Discount discount) throws OperationException, ParseException, InvocationTargetException, IllegalAccessException, IOException, JSONException {

        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
        toDoTaskResource.setTaskSubTypeId("SELLING_PRICE");
        toDoTaskResource.setTaskTypeId("Main task");
        toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.MEDIUM.getValue());
        ;
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
//        toDoTaskResource.setDueOn(1522147354000L);
        toDoTaskResource.setProductId(discount.getOrderId());
        toDoTaskResource.setBookingRefId(discount.getBookingRefId());
        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskResource.setReferenceId(discount.getId());
        return toDoTaskService.save(toDoTaskResource);
    }

    @Override
    public Discount successApproval(Discount discount) throws ParseException, OperationException, IOException {
        //TODO
        Booking rawBooking = opsBookingService.getRawBooking(discount.getBookingRefId());

        Optional<Product> aOptionalProduct
                = rawBooking.getBookingResponseBody().getProducts().stream().filter(aProduct
                -> aProduct.getOrderID().equals(discount.getOrderId())).findFirst();

        if (aOptionalProduct.isPresent()) {
            Product aProduct = aOptionalProduct.get();

            OrderTotalPriceInfo orderTotalPriceInfo = aProduct.getOrderDetails().getOrderTotalPriceInfo();
            SellingPrice revisedSellingPrice = estimateRevisedSellingPrice(getDiscountResource(discount));

            BigDecimal roundedTotalPrice = revisedSellingPrice.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN);
            //TODO : Updating only the total price now,
            //TODO: remaining details to be updated after discussion with business
            orderTotalPriceInfo.setTotalPrice(roundedTotalPrice.toString());

            JSONObject updatePrice = new JSONObject();

            try {
                updatePrice.put(BEConstants.JSON_PROP_ORDERID, aProduct.getOrderID());
                ObjectWriter ow = new ObjectMapper().writer();

                JSONArray orderClientCommercialJSON = new JSONArray(ow.writeValueAsString(aProduct.getOrderDetails()
                        .getOrderClientCommercials()));
                updatePrice.put(BEConstants.JSON_PROP_ORDER_CLIENTCOMMS, orderClientCommercialJSON);

                aProduct.getOrderDetails().getOrderSupplierCommercials().stream().filter(aSupplierComm ->
                        aSupplierComm.getSuppCommId() == null).forEach(aSupplierComm -> aSupplierComm.setSuppCommId(""));
                JSONArray orderSupplierCommercialJSON = new JSONArray(ow.writeValueAsString(aProduct.getOrderDetails()
                        .getOrderSupplierCommercials()));
                updatePrice.put(BEConstants.JSON_PROP_ORDER_SUPPCOMMS, orderSupplierCommercialJSON);

                JSONObject orderSupplierPriceInfoJSON = new JSONObject(ow.writeValueAsString(aProduct.getOrderDetails()
                        .getOrderSupplierPriceInfo()));
                updatePrice.put(BEConstants.JSON_PROP_ORDER_SUPPLIERPRICEINFO, orderSupplierPriceInfoJSON);


                JSONObject totalPriceJSON = new JSONObject(ow.writeValueAsString(orderTotalPriceInfo));
                updatePrice.put(BEConstants.JSON_PROP_ORDER_TOTALPRICEINFO, totalPriceJSON);

                updatePrice.put(BEConstants.JSON_PROP_USERID, userService.getLoggedInUserId());

                if (aProduct.getProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                    try {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity httpEntity = new HttpEntity(updatePrice.toString(), headers);
                        RestTemplate restTemplate = RestUtils.getTemplate();
                        restTemplate.exchange(updateAirOrderTotalPrice,
                                HttpMethod.PUT, httpEntity, String.class);
                    } catch (RestClientException e) {
                        throw new OperationException(String.format("Unable to update TotalPrice for orderId <%s> ",
                                aProduct.getOrderID()));
                    }
                }

                if (aProduct.getProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {

                    JSONArray orderRoomsInfoJSON = new JSONArray(ow.writeValueAsString(aProduct.getOrderDetails()
                            .getHotelDetails().getRooms()));

                    updatePrice.put(BEConstants.JSON_PROP_ACCO_ROOMS, orderRoomsInfoJSON);

                    try {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity httpEntity = new HttpEntity(updatePrice.toString(), headers);
                        RestTemplate restTemplate = RestUtils.getTemplate();
                        restTemplate.exchange(updateAccoOrderTotalPrice,
                                HttpMethod.PUT, httpEntity, String.class);
                    } catch (RestClientException e) {
                        throw new OperationException(String.format("Unable to update TotalPrice for orderId <%s> ",
                                aProduct.getOrderID()));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //TODO: KPI due date failing
            String creditDebitMemoId = createCreditNote(discount);
        }

//        TODO: If update is success,
//        sellingPriceRepository.deleteDiscount(discount.getId());
        return discount;
    }

    private String createCreditNote(Discount discount) throws ParseException, OperationException, IOException {
        //TODO: call finance to create credit note
        //TODO: finance will return id
        OpsBooking opsBooking = opsBookingService.getBooking(discount.getBookingRefId());
        OpsProduct opsProduct = opsBookingService.getProduct(discount.getBookingRefId(), discount.getOrderId());
        RefundResource refundResource = new RefundResource();
        //Todo: need to remove
        refundResource.setRefundType(RefundTypes.REFUND_REDEEMABLE);

        refundResource.setBookingReferenceNo(discount.getBookingRefId());//m
        refundResource.setClaimCurrency(discount.getClientCurrencyCode());
        refundResource.setClientId(opsBooking.getClientID());//m
        refundResource.setClientName(opsBooking.getClientType());
        refundResource.setClientType(opsBooking.getClientType());//m or o
        refundResource.setDefaultModeOfPayment("DD");//mode in which Booking2 is done in case of refund type is refund amount then refund will given back using thi mode of payment
//        refundResource.setNetAmountPayable();

        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductCategory(opsProduct.getProductCategory());
        productDetail.setProductCategorySubType(opsProduct.getProductSubCategory());
        productDetail.setOrderId(discount.getOrderId());

        refundResource.setProductDetail(productDetail);
        refundResource.setReasonForRequest(ReasonForRequest.AMEND_SELLING_PRICE);
        refundResource.setRefundAmount(discount.getDiscountAmount());//refund amount after reduction

        refundResource.setRefundStatus("PENDING WITH OPS");
        refundResource.setClientId(opsBooking.getClientID());
        refundResource.setRoeAsInClaim(opsProduct.getRoe());//booking roe
        refundResource.setCreatedByUserId(userService.getLoggedInUserId());

        return refundService.add(refundResource).getCreditNoteNo();
    }

    @Override
    public List<? extends Discount> getAllRecords(String bookingRefId, String productId, String productCategory, String productSubCategory) throws OperationException {
        OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(productCategory);
        if (opsProductCategory == null) {
            throw new OperationException("Invalid product category");
        }

        OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory, productSubCategory);
        if (opsProductSubCategory == null) {
            throw new OperationException("Invalid product sub-category");
        }

        switch (opsProductCategory) {
            case PRODUCT_CATEGORY_ACCOMMODATION: {
                switch (opsProductSubCategory) {
                    case PRODUCT_SUB_CATEGORY_HOTELS: {
                        return discountRepository.getAllDiscounts(bookingRefId, productId);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public SellingPrice getOriginalSellingPrice(ProductInformation productInformation) throws OperationException, IOException {
        List<SellingPriceComponent> sellingPriceComponents = new ArrayList<>();
        OpsBooking opsBooking = opsBookingService.getBooking(productInformation.getBookingRefId());
        OpsProduct opsProduct = opsBookingService.getProduct(productInformation.getBookingRefId(), productInformation.getOrderId());

        if (productInformation.getBookingRefId() == null
                || productInformation.getOrderId() == null) {
            throw new OperationException("Booking2 ref id, order id, product category, product sub category should not be null.");
        }

        OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
        if (opsProductCategory == null) {
            throw new OperationException("Invalid product category");
        }

        OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory, opsProduct.getProductSubCategory());
        if (opsProductSubCategory == null) {
            throw new OperationException("Invalid product subcategory");
        }

        SellingPrice sellingPrice = new SellingPrice();
        SellingPriceTable sellingPriceTable = new SellingPriceTable();
        MarginDetails marginDetails = marginCalculatorUtil.calculateMargin(opsBooking,opsProduct);
        switch (opsProductCategory) {
            case PRODUCT_CATEGORY_ACCOMMODATION: {

                switch (opsProductSubCategory) {
                    case PRODUCT_SUB_CATEGORY_HOTELS: {
                        OpsHotelDetails opsHotelDetails = opsProduct.getOrderDetails().getHotelDetails();
                        OpsAccommodationTotalPriceInfo opsAccommodationTotalPriceInfo = opsHotelDetails.getOpsAccommodationTotalPriceInfo();

                        if (!StringUtils.isEmpty(opsAccommodationTotalPriceInfo.getTotalPrice())) {
                            sellingPriceComponents.add(new SellingPriceComponent(new BigDecimal(opsAccommodationTotalPriceInfo.getTotalPrice()), "Base fare"));
                        }

                        OpsTaxes opsTaxes = opsAccommodationTotalPriceInfo.getOpsTaxes();
                        List<OpsTax> opsTaxList = opsTaxes.getTax();

                        if (opsTaxList == null
                                || opsTaxList.isEmpty()
                                || opsTaxList.stream().allMatch(opsTax -> opsTax.getAmount() == 0)) {
                            if(opsTaxes.getAmount()!=null){
                                if (opsTaxes.getAmount() != 0)
                                    sellingPriceComponents.add(new SellingPriceComponent(
                                            new BigDecimal(opsTaxes.getAmount()), "Taxes"));
                            }

                        } else {
                            sellingPriceComponents.addAll(opsTaxList.stream().filter(opsTax -> opsTax.getAmount() != 0)
                                    .map(opsTax -> new SellingPriceComponent(new BigDecimal(opsTax.getAmount()),
                                            opsTax.getTaxCode())).collect(Collectors.toList()));
                        }

                        sellingPriceTable.setTotalSellingPrice(new BigDecimal(opsAccommodationTotalPriceInfo.getTotalPrice()));
                        sellingPriceTable.setTotalSupplierPrice(new BigDecimal(opsHotelDetails.getOpsAccoOrderSupplierPriceInfo().getSupplierPrice()));
                        sellingPriceTable.setMargin(marginDetails.getNetMargin());
//                        sellingPriceTable.setMargin(sellingPriceTable.getTotalSellingPrice().subtract(sellingPriceTable.getTotalSupplierPrice()));
                    }
                }
            }
            break;

            case PRODUCT_CATEGORY_TRANSPORTATION: {
                switch (opsProductSubCategory) {
                    case PRODUCT_SUB_CATEGORY_FLIGHT: {
                        OpsFlightDetails opsFlightDetails = opsProduct.getOrderDetails().getFlightDetails();
                        OpsFlightTotalPriceInfo opsFlightTotalPriceInfo = opsFlightDetails.getTotalPriceInfo();

                        OpsBaseFare opsBaseFare = opsFlightTotalPriceInfo.getBaseFare();
                        OpsFees opsFees = opsFlightTotalPriceInfo.getFees();
                        OpsTaxes opsTaxes = opsFlightTotalPriceInfo.getTaxes();
                        OpsReceivables opsReceivables = opsFlightTotalPriceInfo.getReceivables();

                        if (opsBaseFare != null && opsBaseFare.getAmount() != null)
                            sellingPriceComponents.add(new SellingPriceComponent(new BigDecimal(opsBaseFare.getAmount()), "Base fare"));

                        List<OpsFee> opsFeeList = opsFees.getFee();
                        if (opsFeeList == null
                                || opsFeeList.isEmpty()
                                || opsFeeList.stream().allMatch(opsFee -> opsFee.getAmount() == 0)) {
                            sellingPriceComponents.add(new SellingPriceComponent(new BigDecimal(opsFees.getTotal()), "Fees"));
                        } else {
                            List<SellingPriceComponent> feePriceComponents = opsFeeList.stream().filter(
                                    opsFee -> opsFee.getAmount() != 0).map(opsFee -> new SellingPriceComponent(
                                    new BigDecimal(opsFee.getAmount()), opsFee.getFeeCode())).collect(Collectors.toList());
                            sellingPriceComponents.addAll(feePriceComponents);
                        }

                        List<OpsTax> opsTaxList = opsTaxes.getTax();
                        if (opsTaxList == null
                                || opsTaxList.isEmpty()
                                || opsTaxList.stream().allMatch(opsTax -> opsTax.getAmount() == 0)) {
                            if(opsTaxes.getAmount()!=null){
                                if (opsTaxes.getAmount() != 0)
                                    sellingPriceComponents.add(new SellingPriceComponent(new BigDecimal(opsTaxes.getAmount()), "Taxes"));
                            }

                        } else {
                            List<SellingPriceComponent> taxPriceComponents = opsTaxList.stream().filter(opsTax -> opsTax.getAmount() != 0).
                                    map(opsTax -> new SellingPriceComponent(new BigDecimal(opsTax.getAmount()), opsTax.getTaxCode())).collect(Collectors.toList());
                            sellingPriceComponents.addAll(taxPriceComponents);
                        }

                        if(opsReceivables!=null){
                            List<OpsReceivable> opsReceivableList = opsReceivables.getReceivable();
                            if (opsReceivableList == null
                                    || opsReceivableList.isEmpty()
                                    || opsReceivableList.stream().allMatch(opsReceivable -> opsReceivable.getAmount() == 0)) {
                                if (opsReceivables.getAmount() != 0)
                                    sellingPriceComponents.add(new SellingPriceComponent(new BigDecimal(opsReceivables.getAmount()), "Receivables"));
                            } else {
                                List<SellingPriceComponent> receivablePriceCompoenent = opsReceivableList.stream().filter(opsReceivable
                                        -> opsReceivable.getAmount() != 0).map(opsReceivable -> new SellingPriceComponent
                                        (new BigDecimal(opsReceivable.getAmount()), opsReceivable.getCode())).collect(Collectors.toList());
                                sellingPriceComponents.addAll(receivablePriceCompoenent);
                            }
                        }


                        sellingPriceTable.setTotalSellingPrice(new BigDecimal(opsFlightTotalPriceInfo.getTotalPrice()));
                        sellingPriceTable.setTotalSupplierPrice(new BigDecimal(opsFlightDetails.getOpsFlightSupplierPriceInfo().getSupplierPrice()));
                        sellingPriceTable.setMargin(marginDetails.getNetMargin());
//                        sellingPriceTable.setMargin(sellingPriceTable.getTotalSellingPrice().subtract(sellingPriceTable.getTotalSupplierPrice()));
                    }
                }
            }
        }
        sellingPriceComponents.add(new SellingPriceComponent(new BigDecimal(0), "Discount"));

        sellingPrice.setSellingPriceComponents(sellingPriceComponents);
        sellingPrice.setTotalAmount(sellingPrice.getSellingPriceComponents().stream().filter(t ->
                !t.getPriceComponentCode().equalsIgnoreCase("discount"))
                .map(SellingPriceComponent::getAmount).reduce(new BigDecimal(0), BigDecimal::add));
        sellingPrice.setSellingPriceTable(sellingPriceTable);

        return sellingPrice;
    }

}
