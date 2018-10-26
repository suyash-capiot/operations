package com.coxandkings.travel.operations.service.productsharing.impl;

import com.coxandkings.travel.ext.model.finance.invoice.InvoiceDto;
import com.coxandkings.travel.ext.model.finance.invoice.InvoiceParticularsDto;
import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;
import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.productsharing.ProductSharing;
import com.coxandkings.travel.operations.model.refund.Refund;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ProductDetail;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.productsharing.ProductSharingRepository;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateResource;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.notification.NotificationResource;
import com.coxandkings.travel.operations.resource.productsharing.*;
import com.coxandkings.travel.operations.resource.refund.RefundResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoCancellationService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.productsharing.AddPaxService;
import com.coxandkings.travel.operations.service.productsharing.FinanceService;
import com.coxandkings.travel.operations.service.productsharing.ProductSharingService;
import com.coxandkings.travel.operations.service.reconfirmation.common.HashGenerator;
import com.coxandkings.travel.operations.service.refund.RefundService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service("productSharingService")
public class ProductSharingServiceImpl implements ProductSharingService {

    private static final Logger logger = LogManager.getLogger(ProductSharingServiceImpl.class);

    @Autowired
    private ProductSharingRepository productSharingRepository;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccoCancellationService accoCancellationService;

    @Autowired
    private HashGenerator hashGenerator;

    @Autowired
    private FinanceService financeService;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AddPaxService addPaxService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private RestUtils restUtils;

    @Value(value = "${product_sharing.mdm.alert_url}")
    private String getAlertUrl;

    @Value(value = "${product_sharing.template.email_subject}")
    private String subjectForProductSharing;

    @Value(value = "${product_sharing.communication.email_url}")
    private String urlForCommunication;

    @Value(value = "${product_sharing.communication.accept.url}")
    private String acceptProductSharingUrl;

    @Value(value = "${product_sharing.communication.reject.url}")
    private String rejectProductSharingUrl;

    @Value(value = "${product_sharing.be.shared_booking_url}")
    private String getSharedBookingsUrl;

    @Value(value = "${product_sharing.be.shared_booking_url}")
    private String amendBookingURL;

    @Value(value = "${product_sharing.template.business_process}")
    private String PRDT_BUSINESS_PROCESS;

    @Value(value = "${product_sharing.template.function}")
    private String PRDT_FUNCTION;

    @Value(value = "${product_sharing.template.scenario}")
    private String PRDT_SCENARIO;

    @Value(value = "${ops.email.fromEmailAddress}")
    private String fromEmailId;

    @Value(value = "${product_sharing.finance.send_invoice}")
    private String sendInvoice;

    @Value(value = "${product_sharing.be.updateIsSharableURL}")
    private String updateIsSharableURL;

    @Value(value = "${product_sharing.mdm.user_alert}")
    private String productSharingAlertName;

    /**
     * @param actionResource
     * @return
     * @throws OperationException
     */
    @Override
    public ProductSharing saveProductSharing(ActionResource actionResource) throws OperationException {
        ProductSharing productSharing = new ProductSharing();
        productSharing.setBookRefNo(actionResource.getToShare().getBookRefID());
        productSharing.setOrderID(actionResource.getToShare().getOrderID());
        productSharing.setStatus(actionResource.getToShare().getStatus());
        productSharing.setFromSerialNumber(actionResource.getToShare().getSerialNumber());
        productSharing.setSecondRef(hashGenerator.getHash());
        productSharing.setConverted(false);

        if (actionResource.getSerialNumberOfFirstPassenger() != null) {
            productSharing.setFromSerialNumber(actionResource.getSerialNumberOfFirstPassenger());
        }
        if (actionResource.getToShare().getSerialNumber() != null) {
            productSharing.setToSerialNumber(actionResource.getToShare().getSerialNumber());
        }
        if (actionResource.getBookRefIDOfFirstPassenger() != null) {
            productSharing.setFromBookRefNo(actionResource.getBookRefIDOfFirstPassenger());
        }
        if (actionResource.getOrderIDOfFirstPassenger() != null) {
            productSharing.setFromOrderID(actionResource.getOrderIDOfFirstPassenger());
        }
        if (actionResource.getPassengerIDOfFirstPassenger() != null) {
            productSharing.setFromPassengerId(actionResource.getPassengerIDOfFirstPassenger());
        }
        if (actionResource.getToShare().getPassengerID() != null) {
            productSharing.setPassengerId(actionResource.getToShare().getPassengerID());
        }
        try {
            productSharing.setLastModifiedTime(System.currentTimeMillis());
            productSharing.setCreatedTime(System.currentTimeMillis());
            productSharing.setCreatedByUserId(this.userService.getLoggedInUserId());
            productSharing.setLastModifiedByUserId(this.userService.getLoggedInUserId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("unable to saveProductSharing , exception raised : " + e);
        }
        emailProcessing(productSharing);
        return productSharing;
    }

    /**
     * @param productSharing
     * @return
     * @throws OperationException
     */
    @Override
    public ProductSharing updateProductSharing(ProductSharing productSharing) throws OperationException {
        try {
            productSharing.setLastModifiedTime(System.currentTimeMillis());
            productSharing.setCreatedByUserId(this.userService.getLoggedInUserId());
            productSharing.setCreatedTime(System.currentTimeMillis());
            productSharing.setLastModifiedByUserId(this.userService.getLoggedInUserId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("public ProductSharing updateProductSharing(ProductSharing productSharing) throws OperationException , exception raised : " + e);
        }
        emailProcessing(productSharing);
        return productSharing;
    }

    /**
     * @param productSharing
     * @return
     * @throws OperationException
     */
    private ProductSharingMainResource emailProcessing(ProductSharing productSharing) throws OperationException {

        OpsBooking fromBookingDetails = this.getBookingById(productSharing.getFromBookRefNo());
        OpsProduct fromProductDetails = this.getProductById(fromBookingDetails, productSharing.getFromOrderID());

        ProductSharingMainResource fromProductSharingMainResource = process(fromBookingDetails, fromProductDetails, productSharing.getFromSerialNumber(), productSharing.getFromPassengerId());

        OpsBooking toBookingDetails = this.getBookingById(productSharing.getBookRefNo());
        OpsProduct toProductDetails = this.getProductById(toBookingDetails, productSharing.getOrderID());
        ProductSharingMainResource toProductSharingMainResource = process(toBookingDetails, toProductDetails, productSharing.getToSerialNumber(), productSharing.getPassengerId());

        fromProductSharingMainResource.setStatus(productSharing.getStatus());
        if (fromProductSharingMainResource != null) {
            ProductSharing andSendEmail = this.searchTemplateAndSendEmail(fromProductSharingMainResource, productSharing, fromBookingDetails, fromProductDetails, toProductSharingMainResource, this.subjectForProductSharing);
            productSharing = productSharingRepository.saveOrUpdateProductSharing(andSendEmail);
            return fromProductSharingMainResource;
        } else {
            return fromProductSharingMainResource;
        }
    }

    /**
     * @param status
     * @return
     */
    @Override
    public List<ProductSharing> findByStatus(String status) {

        return productSharingRepository.findByStatus(status);
    }

    /**
     * @param status
     * @param fromSerialNumber
     * @return
     */
    @Override
    public List<ProductSharing> findByStatusAndFromSerialNumber(String status, String fromSerialNumber) {
        return productSharingRepository.findByStatus(status, fromSerialNumber);
    }

    /**
     * @param bookRefNo
     * @param orderID
     * @param serialNumber
     * @param fromPassengerId
     * @param toSerialNumber
     * @return
     */
    @Override
    public ProductSharing findByBookRefAndOrderNoAndSerialNo(String bookRefNo, String orderID, String serialNumber, String fromPassengerId, String toSerialNumber) {

        return productSharingRepository.findByBookRefAndOrderNoAndSerialNo(bookRefNo, orderID, serialNumber, fromPassengerId, toSerialNumber);
    }

    /**
     * @param rejectedList
     * @param progressList
     * @return
     */
    @Override
    public List<ProductSharingMainResource> removeRejectItemsFromProgress(List<ProductSharing> rejectedList, List<ProductSharingMainResource> progressList) {
        try {
//            this.prettyJSON(progressList);
//            this.prettyJSON(rejectedList);
//            for (ProductSharing productSharing : rejectedList) {
//                ProductSharingMainResource rejectedProductSharingMainResource = new ProductSharingMainResource();
//                rejectedProductSharingMainResource.setBookingReferenceNo(productSharing.getBookRefNo());
//                rejectedProductSharingMainResource.setOrderId(productSharing.getOrderID());
//                rejectedProductSharingMainResource.setSerialNumber(productSharing.getToSerialNumber());
//                rejectedProductSharingMainResource.setPassengerId(productSharing.getPassengerId());
//                this.prettyJSON(rejectedProductSharingMainResource);
//                if (progressList.contains(rejectedProductSharingMainResource)) {
//                    progressList.remove(rejectedProductSharingMainResource);
//                }
//            }
            progressList = remove(rejectedList, progressList);
            return progressList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("public List<ProductSharingMainResource> removeRejectItemsFromProgress(List<ProductSharing> rejectedList, List<ProductSharingMainResource> progressList) , exception raised : " + e);
            return progressList;
        }

    }

    /**
     * @param rejectedList
     * @return
     */
    @Override
    public List<ProductSharingMainResource> getListOfRejected(List<ProductSharing> rejectedList) {

        this.prettyJSON(rejectedList);
        List<ProductSharingMainResource> rejectedListOfPSM = new ArrayList<>();
        try {
            for (ProductSharing productSharing : rejectedList) {
                String bookRefNo1 = productSharing.getBookRefNo();
                OpsBooking opsBooking = this.getBookingById(bookRefNo1);
                String orderID = productSharing.getOrderID();
                String serialNumber = productSharing.getToSerialNumber();
                String passengerId = productSharing.getPassengerId();
                OpsProduct orderDetails = null;
                try {
                    orderDetails = this.getProductById(opsBooking, orderID);
                    ProductSharingMainResource productSharingMainResource = this.process(opsBooking, orderDetails, serialNumber, passengerId);
                    productSharingMainResource.setStatus(ProductSharingStatus.REJECT);
                    productSharingMainResource.setSerialNumber(serialNumber);
                    productSharingMainResource.setBookingReferenceNo(bookRefNo1);
                    productSharingMainResource.setPassengerId(passengerId);
                    productSharingMainResource.setOrderId(orderID);
                    rejectedListOfPSM.add(productSharingMainResource);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("public List<ProductSharingMainResource> getListOfRejected(List<ProductSharing> rejectedList) , exception raised : " + e);
                }
            }
            return rejectedListOfPSM;
        } catch (Exception e) {
            e.printStackTrace();
            return rejectedListOfPSM;
        }

    }

    public List<ProductSharingMainResource> remove(List<ProductSharing> acceptedList, List<ProductSharingMainResource> progressList) {

        for (ProductSharing productSharing : acceptedList) {

            String bookRefNo = productSharing.getBookRefNo();
            String orderID = productSharing.getOrderID();
            String toSerialNumber = productSharing.getToSerialNumber();
            String passengerId = productSharing.getPassengerId();

            for (int i = 0; i < progressList.size(); i++) {
                ProductSharingMainResource productSharingMainResource = progressList.get(i);
                String bookingReferenceNo = productSharingMainResource.getBookingReferenceNo();
                String orderId = productSharingMainResource.getOrderId();
                String serialNumber = productSharingMainResource.getSerialNumber();
                String passengerId1 = productSharingMainResource.getPassengerId();
                if (bookRefNo.equalsIgnoreCase(bookingReferenceNo)
                        && orderID.equalsIgnoreCase(orderId)
                        && toSerialNumber.equalsIgnoreCase(serialNumber)
                        && passengerId.equalsIgnoreCase(passengerId1)) {

                    progressList.remove(i);
                }
            }


        }
        return progressList;

    }


    /**
     * @param acceptedList
     * @param progressList
     * @return
     */
    @Override
    public List<ProductSharingMainResource> removeAcceptedItemsFromProgress(List<ProductSharing> acceptedList, List<ProductSharingMainResource> progressList) {
        try {
//            this.prettyJSON(progressList);
//            this.prettyJSON(acceptedList);
//            for (ProductSharing productSharing : acceptedList) {
//                ProductSharingMainResource rejectedProductSharingMainResource = new ProductSharingMainResource();
//                rejectedProductSharingMainResource.setBookingReferenceNo(productSharing.getBookRefNo());
//                rejectedProductSharingMainResource.setOrderId(productSharing.getOrderID());
//                rejectedProductSharingMainResource.setSerialNumber(productSharing.getToSerialNumber());
//                rejectedProductSharingMainResource.setPassengerId(productSharing.getPassengerId());
//                if (progressList.contains(rejectedProductSharingMainResource)) {
//                    progressList.remove(rejectedProductSharingMainResource);
//                }
//            }
            progressList = remove(acceptedList, progressList);
            return progressList;
        } catch (Exception e) {
            e.printStackTrace();

            logger.info("  public List<ProductSharingMainResource> removeAcceptedItemsFromProgress(List<ProductSharing> acceptedList, List<ProductSharingMainResource> progressList)  , exception raised : " + e);
            return progressList;
        }
    }

    /**
     * @param acceptedList
     * @return
     */
    @Override
    public List<ProductSharingMainResource> getListOfAccepted(List<ProductSharing> acceptedList) {
        List<ProductSharingMainResource> acceptedListOfPSM = new ArrayList<>();
        try {

            this.prettyJSON(acceptedList);
            for (ProductSharing productSharing : acceptedList) {
                String bookRefNo = productSharing.getBookRefNo();
                OpsBooking opsBooking = this.getBookingById(bookRefNo);
                String orderID = productSharing.getOrderID();
                OpsProduct orderDetails = this.getProductById(opsBooking, orderID);
                String serialNumber = productSharing.getToSerialNumber();
                String passengerId = productSharing.getPassengerId();
                ProductSharingMainResource productSharingMainResource = this.process(opsBooking, orderDetails, serialNumber, passengerId);
                productSharingMainResource.setStatus(ProductSharingStatus.ACCEPT);
                productSharingMainResource.setSerialNumber(serialNumber);
                productSharingMainResource.setBookingReferenceNo(bookRefNo);
                productSharingMainResource.setPassengerId(passengerId);
                productSharingMainResource.setOrderId(orderID);
                acceptedListOfPSM.add(productSharingMainResource);
            }
            return acceptedListOfPSM;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("  public List<ProductSharingMainResource> getListOfAccepted(List<ProductSharing> acceptedList), exception raised : " + e);
            return acceptedListOfPSM;
        }
    }

    /**
     * @param opsBooking
     * @param aProduct
     * @param roomId
     * @param serialNo
     * @param passengerId
     * @return
     * @throws OperationException
     */
    @Override
    public ProductSharingMainResource getPaxInfo(OpsBooking opsBooking, OpsProduct aProduct, String roomId, String serialNo, String passengerId) throws OperationException {

        ZonedDateTime checkInZTZ = null;
        ZonedDateTime checkOutZTZ = null;

        ProductSharingMainResource productSharingMainResource = new ProductSharingMainResource();
        productSharingMainResource.setBookingReferenceNo(opsBooking.getBookID());
        productSharingMainResource.setOrderId(aProduct.getOrderID());

        AccommodationDetails accommodationDetails = new AccommodationDetails();
        productSharingMainResource.setBookingReferenceNo(opsBooking.getBookID());
        productSharingMainResource.setStatus(ProductSharingStatus.PROGRESS);
        OpsProductCategory aProductCategory = OpsProductCategory.getProductCategory(aProduct.getProductCategory());
        switch (aProductCategory) {

            case PRODUCT_CATEGORY_TRANSPORTATION: {
                if (aProduct.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                } else {
                    throw new OperationException(Constants.ER312);
                }
            }
            case PRODUCT_CATEGORY_ACCOMMODATION: {
                if (aProduct.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                    String orderID = aProduct.getOrderID();
                    OpsOrderDetails orderDetails = aProduct.getOrderDetails();
                    if (orderDetails.getHotelDetails() != null) {
                        OpsHotelDetails hotelDetails = orderDetails.getHotelDetails();
                        productSharingMainResource.setOrderId(orderID);
                        List<OpsRoom> rooms = hotelDetails.getRooms();
                        for (OpsRoom opsRoom : rooms) {
                            if (roomId.equalsIgnoreCase(opsRoom.getRoomID())) {
                                String checkIn = opsRoom.getCheckIn();
                                String checkOut = opsRoom.getCheckOut();
                                if (checkOut != null) {
                                    checkOutZTZ = DateTimeUtil.formatBEDateTimeZone(checkOut);
                                    String checkInDateInString = checkOutZTZ.toOffsetDateTime().toString();
                                    String[] checkInDateWithoutT = checkInDateInString.split("T");

                                    accommodationDetails.setCheckOutDate(checkInDateWithoutT[0]);
                                }
                                if (checkIn != null) {
                                    checkInZTZ = DateTimeUtil.formatBEDateTimeZone(checkIn);

                                    String checkInDate = checkInZTZ.toOffsetDateTime().toString();
                                    String[] checkInDateWithoutT = checkInDate.split("T");
                                    accommodationDetails.setCheckInDate(checkInDateWithoutT[0]);
                                    accommodationDetails.setNumberOfDays(String.valueOf(Math.abs(checkOutZTZ.getDayOfMonth() - checkInZTZ.getDayOfMonth())));
                                    accommodationDetails.setNumberOfNights(String.valueOf(Math.abs(checkOutZTZ.getDayOfMonth() - checkInZTZ.getDayOfMonth())));
                                }
                                OpsRoomTypeInfo roomTypeInfo = null;
                                if (opsRoom.getRoomTypeInfo() != null) {
                                    roomTypeInfo = opsRoom.getRoomTypeInfo();
                                    if (roomTypeInfo != null) {
                                        String roomTypeName = roomTypeInfo.getRoomTypeName();
                                        accommodationDetails.setRoomCapacity(roomTypeName);
                                        accommodationDetails.setHotelType(roomTypeName);
                                    }
                                }
                                if (opsRoom.getPaxInfo() != null) {
                                    List<OpsAccommodationPaxInfo> groupOfPaxInfo = opsRoom.getPaxInfo();
                                    for (OpsAccommodationPaxInfo paxInfo : groupOfPaxInfo) {
                                        if (paxInfo != null) {
                                            if (paxInfo.getLeadPax()) {
                                                if (paxInfo.getFirstName() != null) {
                                                    productSharingMainResource.setFirstName(paxInfo.getFirstName());
                                                }
                                                if (paxInfo.getPaxID() != null) {
                                                    productSharingMainResource.setPassengerId(paxInfo.getPaxID());
                                                }
                                                if (paxInfo.getLastName() != null) {
                                                    productSharingMainResource.setLastName(paxInfo.getLastName());
                                                }
                                                if (paxInfo.getTitle() != null) {
                                                    productSharingMainResource.setTitle(paxInfo.getTitle());
                                                    if (paxInfo.getTitle().equalsIgnoreCase("Mr")) {
                                                        productSharingMainResource.setGenderPreferenceForSharing("MALE");
                                                    } else {
                                                        productSharingMainResource.setGenderPreferenceForSharing("FEMALE");
                                                    }
                                                } else {
                                                    productSharingMainResource.setTitle("Mr");
                                                    productSharingMainResource.setGenderPreferenceForSharing("MALE");
                                                }
                                                if (paxInfo.getFirstName() != null) {
                                                    productSharingMainResource.setPassengerName(paxInfo.getFirstName());
                                                }
                                                if (paxInfo.getContactDetails() != null) {
                                                    OpsContactDetails contactDetails = paxInfo.getContactDetails().get(0);
                                                    OpsContactInfo contactInfo = contactDetails.getContactInfo();
                                                    if (contactDetails.getEmail() != null) {
                                                        productSharingMainResource.setEmailId(contactDetails.getEmail());
                                                    }
                                                    if (contactInfo != null && contactInfo.getEmail() != null) {
                                                        productSharingMainResource.setEmailId(contactInfo.getEmail());
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                                if (hotelDetails.getHotelName() != null) {
                                    accommodationDetails.setHotelName(hotelDetails.getHotelName());
                                }
                                if (hotelDetails.getCountryName() != null) {
                                    accommodationDetails.setCountry(hotelDetails.getCountryName());
                                }
                                if (aProduct.getProductCategory() != null) {
                                    accommodationDetails.setHotelCategory(aProduct.getProductCategory());
                                }
                                if (aProduct.getProductSubCategory() != null) {
                                    accommodationDetails.setHotelSubCategory(aProduct.getProductSubCategory());
                                }
                                if (opsRoom.getRoomID() != null) {
                                    productSharingMainResource.setSerialNumber(opsRoom.getRoomID());
                                    accommodationDetails.setId(opsRoom.getRoomID());
                                }
                                if (hotelDetails.getCityName() != null) {
                                    accommodationDetails.setCity(hotelDetails.getCityName());
                                }
                            }
                        }
                        productSharingMainResource.setAccommodationDetails(accommodationDetails);
                        return productSharingMainResource;
                    }
                }
            }
            default:
                break;
        }
        return null;
    }

    /**
     * @param bookingId
     * @return
     * @throws OperationException
     */
    @Override
    public OpsBooking getBookingById(String bookingId) throws OperationException {
        OpsBooking bookingDetails = null;
        try {
            bookingDetails = this.opsBookingService.getBooking(bookingId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException(Constants.ER305);
        }
        if (bookingDetails == null) {
            throw new OperationException(Constants.ER318);
        }
        return bookingDetails;
    }

    /**
     * @param bookingDetails
     * @param orderID
     * @return
     * @throws OperationException
     */
    @Override
    public OpsProduct getProductById(OpsBooking bookingDetails, final String orderID) throws OperationException {
        OpsProduct productDetails = null;
        try {
            productDetails = bookingDetails.getProducts().stream().filter(aProduct -> aProduct.getOrderID().equalsIgnoreCase(orderID)).findFirst().get();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("  public OpsProduct getProductById(OpsBooking bookingDetails, final String orderID) throws OperationException , exception raised : " + e);
            throw new OperationException(Constants.ER317);
        }
        if (productDetails == null) {
            logger.info(" public OpsProduct getProductById(OpsBooking bookingDetails, final String orderID) throws OperationException , exception raised : product details==> " + productDetails);
            throw new OperationException(Constants.ER317);
        }
        return productDetails;
    }

    /**
     * @param productSharingMainResource
     * @return
     */
    @Override
    public ProductSharingBookingCriteria convert(ProductSharingMainResource productSharingMainResource) {
        ProductSharingBookingCriteria productSharingBookingCriteria = null;
        try {
            productSharingBookingCriteria = new ProductSharingBookingCriteria();
            productSharingBookingCriteria.setCheckInDate(productSharingMainResource.getAccommodationDetails().getCheckInDate());
            productSharingBookingCriteria.setCheckOutDate(productSharingMainResource.getAccommodationDetails().getCheckOutDate());
            productSharingBookingCriteria.setGender(productSharingMainResource.getGenderPreferenceForSharing());
            productSharingBookingCriteria.setIsSharable(String.valueOf(true));
            productSharingBookingCriteria.setRoomCategory(productSharingMainResource.getAccommodationDetails().getHotelCategory());
            productSharingBookingCriteria.setRoomType(productSharingMainResource.getAccommodationDetails().getHotelType());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(" public ProductSharingBookingCriteria convert(ProductSharingMainResource productSharingMainResource) : something went wrong with conversion");
        }
        return productSharingBookingCriteria;
    }

    /**
     * @param productSharingBookingCriteria
     * @return
     * @throws OperationException
     */
    @Override
    public List<ProductSharingMainResource> getListOfSharedInProgress(ProductSharingBookingCriteria productSharingBookingCriteria) throws OperationException {
        List<ProductSharingMainResource> response = new ArrayList<>();
        List<SharedBooking> getSharedBookings = getSharedBookings(productSharingBookingCriteria);

        try {
            for (SharedBooking sharedBooking : getSharedBookings) {
                ProductSharingMainResource prdtSharingMainResrc = new ProductSharingMainResource();
                AccommodationDetails accoDetails = new AccommodationDetails();
                prdtSharingMainResrc.setBookingReferenceNo(sharedBooking.getBookingRefNo());

                //Acco
                accoDetails.setId(sharedBooking.getRoomId());
                accoDetails.setHotelSubCategory("");
                accoDetails.setHotelCategory("");
                accoDetails.setNumberOfNights(sharedBooking.getNumberOfNights() != null ? sharedBooking.getNumberOfNights() : "0");
                accoDetails.setNumberOfDays(sharedBooking.getNumberOfDays() != null ? sharedBooking.getNumberOfDays() : "0");
                accoDetails.setRoomCapacity(sharedBooking.getRoomCapacity() != null ? sharedBooking.getRoomCapacity() : "");

                accoDetails.setCheckOutDate(sharedBooking.getCheckOutDate());
                accoDetails.setHotelName(sharedBooking.getHotelName());
                accoDetails.setCheckInDate(sharedBooking.getCheckInDate());
                accoDetails.setHotelType(sharedBooking.getLuxuryType() != null ? sharedBooking.getLuxuryType() : "");
                accoDetails.setCity(sharedBooking.getCity());
                accoDetails.setCountry(sharedBooking.getCountry());
                prdtSharingMainResrc.setAccommodationDetails(accoDetails);
                //pax
                prdtSharingMainResrc.setOrderId(sharedBooking.getOrderId());
                prdtSharingMainResrc.setPassengerId(sharedBooking.getPaxId());
                prdtSharingMainResrc.setPassengerName(sharedBooking.getFirstName());
                prdtSharingMainResrc.setFirstName(sharedBooking.getFirstName());
                prdtSharingMainResrc.setLastName(sharedBooking.getLastName());
                prdtSharingMainResrc.setGenderPreferenceForSharing(sharedBooking.getGenderPreference() != null ? sharedBooking.getGenderPreference() : "MALE");
                prdtSharingMainResrc.setEmailId(sharedBooking.getEmail());
                prdtSharingMainResrc.setSerialNumber(sharedBooking.getRoomId());
                if (prdtSharingMainResrc.getGenderPreferenceForSharing().equalsIgnoreCase("MALE")) {
                    prdtSharingMainResrc.setTitle("Mr");
                } else {
                    prdtSharingMainResrc.setTitle("Mrs");
                }
                prdtSharingMainResrc.setStatus(ProductSharingStatus.PROGRESS);
                response.add(prdtSharingMainResrc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("   public List<ProductSharingMainResource> getListOfSharedInProgress(ProductSharingBookingCriteria productSharingBookingCriteria) throws OperationException,  " + e);
        }
        return response;

    }

    /**
     * @param businessProcess
     * @param scenario
     * @param function
     * @return
     */
    private TemplateInfo getTemplateInfo(String businessProcess, String scenario, String function) {
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
     * @param aProduct
     * @param serialNo
     * @param passengerId
     * @return
     * @throws OperationException
     */
    public ProductSharingMainResource process(OpsBooking opsBooking, OpsProduct aProduct, String serialNo, String passengerId) throws OperationException {
        ZonedDateTime checkInZTZ = null;
        ZonedDateTime checkOutZTZ = null;

        ProductSharingMainResource productSharingMainResource = new ProductSharingMainResource();
        productSharingMainResource.setBookingReferenceNo(opsBooking.getBookID());
        productSharingMainResource.setOrderId(aProduct.getOrderID());

        AccommodationDetails accommodationDetails = new AccommodationDetails();
        productSharingMainResource.setBookingReferenceNo(opsBooking.getBookID());
        OpsProductCategory aProductCategory = OpsProductCategory.getProductCategory(aProduct.getProductCategory());
        switch (aProductCategory) {

            case PRODUCT_CATEGORY_TRANSPORTATION: {
                if (aProduct.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                } else {
                    throw new OperationException(Constants.ER312);
                }
            }
            case PRODUCT_CATEGORY_ACCOMMODATION: {
                if (aProduct.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                    String orderID = aProduct.getOrderID();
                    OpsOrderDetails orderDetails = aProduct.getOrderDetails();
                    if (orderDetails.getHotelDetails() != null) {
                        OpsHotelDetails hotelDetails = orderDetails.getHotelDetails();
                        productSharingMainResource.setOrderId(orderID);
                        List<OpsRoom> rooms = hotelDetails.getRooms();
                        for (OpsRoom opsRoom : rooms) {
                            if (serialNo.equalsIgnoreCase(opsRoom.getRoomID())) {
                                String checkIn = opsRoom.getCheckIn();
                                String checkOut = opsRoom.getCheckOut();

                                if (checkOut != null) {
                                    checkOutZTZ = DateTimeUtil.formatBEDateTimeZone(checkOut);
                                    accommodationDetails.setCheckOutDate(checkOutZTZ.toOffsetDateTime().toString());
                                }
                                if (checkIn != null) {
                                    checkInZTZ = DateTimeUtil.formatBEDateTimeZone(checkIn);
                                    accommodationDetails.setCheckInDate(checkInZTZ.toOffsetDateTime().toString());
                                    accommodationDetails.setNumberOfDays(String.valueOf(Math.abs(checkOutZTZ.getDayOfMonth() - checkInZTZ.getDayOfMonth())));
                                    accommodationDetails.setNumberOfNights(String.valueOf(Math.abs(checkOutZTZ.getDayOfMonth() - checkInZTZ.getDayOfMonth())));
                                }
                                OpsRoomTypeInfo roomTypeInfo = null;
                                if (opsRoom.getRoomTypeInfo() != null) {
                                    roomTypeInfo = opsRoom.getRoomTypeInfo();
                                    if (roomTypeInfo != null) {
                                        String roomTypeName = roomTypeInfo.getRoomTypeName();
                                        accommodationDetails.setRoomCapacity(roomTypeName);
                                        accommodationDetails.setHotelType(roomTypeName);
                                    }
                                }
                                if (opsRoom.getPaxInfo() != null) {
                                    List<OpsAccommodationPaxInfo> groupOfPaxInfo = opsRoom.getPaxInfo();
                                    for (OpsAccommodationPaxInfo paxInfo : groupOfPaxInfo) {
                                        if (paxInfo != null /*&& paxInfo.getLeadPax()*/) {
                                            if (paxInfo.getPaxID().equalsIgnoreCase(passengerId)) {
                                                if (paxInfo.getFirstName() != null) {
                                                    productSharingMainResource.setFirstName(paxInfo.getFirstName());
                                                }
                                                if (paxInfo.getPaxID() != null) {
                                                    productSharingMainResource.setPassengerId(paxInfo.getPaxID());
                                                }
                                                if (paxInfo.getLastName() != null) {
                                                    productSharingMainResource.setLastName(paxInfo.getLastName());
                                                }


                                                if (paxInfo.getTitle() != null) {
                                                    productSharingMainResource.setTitle(paxInfo.getTitle());
                                                    if (paxInfo.getTitle().equalsIgnoreCase("Mr")) {
                                                        productSharingMainResource.setGenderPreferenceForSharing("MALE");
                                                    } else {
                                                        productSharingMainResource.setGenderPreferenceForSharing("FEMALE");
                                                    }
                                                } else {
                                                    productSharingMainResource.setTitle("Mr");
                                                    productSharingMainResource.setGenderPreferenceForSharing("MALE");
                                                }
                                                if (paxInfo.getFirstName() != null) {
                                                    productSharingMainResource.setPassengerName(paxInfo.getFirstName());
                                                }
                                                if (paxInfo.getContactDetails() != null) {
                                                    OpsContactDetails contactDetails = paxInfo.getContactDetails().get(0);
                                                    OpsContactInfo contactInfo = contactDetails.getContactInfo();

                                                    if (contactDetails.getEmail() != null) {
                                                        productSharingMainResource.setEmailId(contactDetails.getEmail());
                                                    }
                                                    if (contactInfo != null && contactInfo.getEmail() != null) {
                                                        productSharingMainResource.setEmailId(contactInfo.getEmail());
                                                    }


                                                }
                                            }
                                        }
                                    }
                                }
                                if (hotelDetails.getHotelName() != null) {
                                    accommodationDetails.setHotelName(hotelDetails.getHotelName());
                                }
                                if (hotelDetails.getCountryName() != null) {
                                    accommodationDetails.setCountry(hotelDetails.getCountryName());
                                }
                                if (aProduct.getProductCategory() != null) {
                                    accommodationDetails.setHotelCategory(aProduct.getProductCategory());
                                }
                                if (aProduct.getProductSubCategory() != null) {
                                    accommodationDetails.setHotelSubCategory(aProduct.getProductSubCategory());
                                }
                                if (opsRoom.getRoomID() != null) {
                                    productSharingMainResource.setSerialNumber(opsRoom.getRoomID());
                                    accommodationDetails.setId(opsRoom.getRoomID());
                                }
                                if (hotelDetails.getCityName() != null) {
                                    accommodationDetails.setCity(hotelDetails.getCityName());
                                }
                            }
                        }
                        productSharingMainResource.setAccommodationDetails(accommodationDetails);
                        return productSharingMainResource;
                    }
                }
            }
            default:
                break;
        }
        return null;
    }

    /**
     * @param opsProduct
     * @param opsBooking
     * @param referenceID
     * @param productSharing
     * @return
     */
    @Override
    public ProductSharing createToDoTask(OpsProduct opsProduct, OpsBooking opsBooking, String referenceID, ProductSharing productSharing) {
        ToDoTask toDoDetails = null;
        try {
            ToDoTaskResource toDo = new ToDoTaskResource();
            toDo.setBookingRefId(opsBooking.getBookID());
            try {
                toDo.setCreatedByUserId(this.userService.getLoggedInUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            toDo.setProductId(opsProduct.getOrderID());
            toDo.setReferenceId("453453456453456454");
//            toDo.setClientTypeId(opsBooking.getClientType());
//            toDo.setCompanyId(opsBooking.getCompanyId());
//            toDo.setClientId(opsBooking.getClientID());
            // toDo.setCompanyMarketId(opsBooking.getCompanyId());//todo company market id
            // toDo.setDueOnDate(ZonedDateTime.now());
            toDo.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
            toDo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            toDo.setTaskPriorityId(ToDoTaskPriorityValues.MEDIUM.getValue());
            toDo.setTaskSubTypeId(ToDoTaskSubTypeValues.PRODUCT_SHARING.toString());
            toDo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
            toDo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
            toDoDetails = toDoTaskService.save(toDo);

            if (toDoDetails != null) {
                //  productSharing.setToDoTaskID(toDoDetails.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(" public ProductSharing createToDoTask(OpsProduct opsProduct, OpsBooking opsBooking, String referenceID, ProductSharing productSharing) , exception raised : " + e);
        }
        return productSharing;
    }


    /**
     * @param fromDetails
     * @param productSharing
     * @param opsBooking
     * @param opsProduct
     * @param toDetails
     * @param subject
     * @return
     */
    public ProductSharing searchTemplateAndSendEmail(ProductSharingMainResource fromDetails, ProductSharing productSharing, OpsBooking opsBooking, OpsProduct opsProduct, ProductSharingMainResource toDetails, String subject) {

        try {
            EmailUsingTemplateResource emailUsingTemplateResource = new EmailUsingTemplateResource();
            emailUsingTemplateResource.setCommunicationTagResource(getCommunicationTagResource(opsBooking, opsProduct));
            ArrayList<String> emails = new ArrayList<>();
            if (toDetails.getEmailId() != null && toDetails.getEmailId().isEmpty() == false) {
                emails.add(toDetails.getEmailId());
            }
            productSharing.setHash(hashGenerator.getHash());
            productSharing.setExpiredLink(false);
            emailUsingTemplateResource.setFromMail(fromEmailId);
            emailUsingTemplateResource.setToMail(emails);
            emailUsingTemplateResource.setSubject(subject);
            emailUsingTemplateResource.setPriority(EmailPriority.HIGH);

            emailUsingTemplateResource.setProcess(PRDT_BUSINESS_PROCESS);
            emailUsingTemplateResource.setScenario(PRDT_SCENARIO);
            emailUsingTemplateResource.setFunction(PRDT_FUNCTION);


            emailUsingTemplateResource.setTemplateInfo(getTemplateInfo(PRDT_BUSINESS_PROCESS, PRDT_SCENARIO, PRDT_FUNCTION));
            Map<String, String> dynamicVariables = new HashMap<>();
            dynamicVariables.put("First Name", toDetails.getFirstName());
            dynamicVariables.put("Last Name", toDetails.getLastName());
            dynamicVariables.put("Product Type", opsProduct.getOpsProductCategory().getCategory());
            dynamicVariables.put("Passenger Name", fromDetails.getPassengerName());
            dynamicVariables.put("Order Details", getOrderDetails(fromDetails));
            dynamicVariables.put("Gender", fromDetails.getGenderPreferenceForSharing());
            dynamicVariables.put("ProductSharingRejectUrl", rejectProductSharingUrl + productSharing.getHash());
            dynamicVariables.put("ProductSharingAcceptUrl", acceptProductSharingUrl + productSharing.getHash());
            emailUsingTemplateResource.setDynamicVariables(dynamicVariables);
            productSharing.setSentEmail(true);

            this.sendAnEmail(emailUsingTemplateResource);
            return productSharing;
        } catch (Exception e) {
            e.printStackTrace();
            productSharing.setSentEmail(false);
            logger.info("  public ProductSharing createAndSendEmail(ProductSharingMainResource fromDetails, ProductSharing productSharing, OpsProduct opsProduct, ProductSharingMainResource toDetails, String subject)  , exception raised : " + e);
        }
        return productSharing;

    }

    public static String prettyJSON(Object object) {
        logger.info(" //###  prettyJSON ###");
        String s = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            // System.err.println(s);
            //  logger.info(s);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception " + e.toString());
            return s;
        }
    }

    /**
     * @param productSharingMainResource
     * @return
     */
    private String getOrderDetails(ProductSharingMainResource productSharingMainResource) {
        StringBuilder paxDetails = new StringBuilder();

        AccommodationDetails accommodationDetails = productSharingMainResource.getAccommodationDetails();
        String hotelName = accommodationDetails.getHotelName() != null ? accommodationDetails.getHotelName().concat(", ") : "";
        paxDetails.append(hotelName);

        String city = accommodationDetails.getCity() != null ? accommodationDetails.getCity().concat(", ") : "";
        paxDetails.append(city);

        String country = accommodationDetails.getCountry() != null ? accommodationDetails.getCountry().concat(", ") : "";
        paxDetails.append(country);

        String numberOfDays = accommodationDetails.getNumberOfDays() != null ? accommodationDetails.getNumberOfDays().concat(" Days ").concat(", ") : "";
        paxDetails.append(numberOfDays);

        String numberOfNights = accommodationDetails.getNumberOfNights() != null ? accommodationDetails.getNumberOfNights().concat(" Nights ").concat(", ") : "";
        paxDetails.append(numberOfNights);

        String hotelType = accommodationDetails.getHotelType() != null ? accommodationDetails.getHotelType().concat(", ") : "";
        paxDetails.append(hotelType);

        String hotelCategory = accommodationDetails.getHotelCategory() != null ? accommodationDetails.getHotelCategory().concat(", ") : "";
        paxDetails.append(hotelCategory);

        String hotelSubCategory = accommodationDetails.getHotelSubCategory() != null ? accommodationDetails.getHotelSubCategory().concat(", ") : "";
        paxDetails.append(hotelSubCategory);

        String checkInDate = accommodationDetails.getCheckInDate() != null ? accommodationDetails.getCheckInDate() : "";
        String[] checkInDateWithoutT = checkInDate.split("T");
        String checkinDateInString = checkInDateWithoutT[0].concat(", ");
        paxDetails.append(checkinDateInString);

        String checkOutDate = accommodationDetails.getCheckOutDate() != null ? accommodationDetails.getCheckOutDate() : "";
        String[] checkOutDateWithoutT = checkOutDate.split("T");
        String checkoutDateInString = checkOutDateWithoutT[0].concat(", ");
        paxDetails.append(checkoutDateInString);
        String roomCapacity = accommodationDetails.getRoomCapacity() != null ? accommodationDetails.getRoomCapacity().concat(",") : "";
        return paxDetails.toString();
    }

    /**
     * @param aProductSharing
     * @return
     */
    private ProductSharing convertToFrom(ProductSharing aProductSharing) {
        ProductSharing aProductSharingTo = null;
        try {
            aProductSharingTo = new ProductSharing();

            aProductSharingTo.setFromPassengerId(aProductSharing.getPassengerId());
            aProductSharingTo.setFromSerialNumber(aProductSharing.getToSerialNumber());
            aProductSharingTo.setFromOrderID(aProductSharing.getOrderID());
            aProductSharingTo.setFromBookRefNo(aProductSharing.getBookRefNo());

            aProductSharingTo.setPassengerId(aProductSharing.getFromPassengerId());
            aProductSharingTo.setOrderID(aProductSharing.getFromOrderID());
            aProductSharingTo.setBookRefNo(aProductSharing.getFromBookRefNo());
            aProductSharingTo.setToSerialNumber(aProductSharing.getFromSerialNumber());
            aProductSharingTo.setStatus(ProductSharingStatus.PROGRESS);
            aProductSharingTo.setSentEmail(false);
            aProductSharingTo.setGenderPreferenceForSharing(aProductSharing.getGenderPreferenceForSharing());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("  private ProductSharing convertToFrom(ProductSharing aProductSharing), exception raised : " + e);
        }
        return aProductSharingTo;
    }

    /**
     * @param sharingStatus
     * @param hash
     * @return
     * @throws OperationException
     */
    @Override
    public String checkClientResponse(ProductSharingStatus sharingStatus, String hash) throws OperationException {
        ProductSharing aProductSharing = this.productSharingRepository.findProductSharingByHash(hash);
        ProductSharing aProductSharingTo = null;
        if (aProductSharing != null) {
            aProductSharingTo = productSharingRepository.findProductSharingBySecondRef(aProductSharing.getSecondRef(), true);
            if (aProductSharingTo == null) {
                aProductSharingTo = convertToFrom(aProductSharing);
                aProductSharingTo.setConverted(true);
            }
            if (aProductSharingTo.getStatus().getProductSharingStatus().equalsIgnoreCase(ProductSharingStatus.ACCEPT.getProductSharingStatus())) {
                if (aProductSharingTo.isExpiredLink()) {
                    String message = getMessage(Constants.ER903, Locale.ENGLISH);
                    return convertToHtml(message, "blue");
                }
            }
            if (aProductSharingTo.getStatus().getProductSharingStatus().equalsIgnoreCase(ProductSharingStatus.REJECT.getProductSharingStatus())) {
                if (aProductSharingTo.isExpiredLink()) {
                    String message = getMessage(Constants.ER904, Locale.ENGLISH);
                    return convertToHtml(message, "blue");
                }
            }
            aProductSharing.setExpiredLink(true);
            //newly added
            aProductSharingTo.setStatus(sharingStatus);
            aProductSharingTo.setExpiredLink(true);
            aProductSharingTo = productSharingRepository.saveOrUpdateProductSharing(aProductSharingTo);
            aProductSharing = productSharingRepository.saveOrUpdateProductSharing(aProductSharing);
            if (aProductSharingTo != null) {
                if (sharingStatus.getProductSharingStatus().equalsIgnoreCase(ProductSharingStatus.ACCEPT.getProductSharingStatus())) {
                    String message = getMessage(Constants.ER906, Locale.ENGLISH);
                    try {
                        if (true) {
                            //Initiator
                            OpsBooking fromBookingDetails = this.getBookingById(aProductSharing.getFromBookRefNo());
                            OpsProduct fromProductDetails = this.getProductById(fromBookingDetails, aProductSharing.getFromOrderID());
                            // : amendment single to double
                            boolean statusOfAmendmentProcess = this.amendmentProcess(fromBookingDetails, fromProductDetails, aProductSharing.getFromSerialNumber(), aProductSharing.getFromPassengerId());
                            //joinee
                            OpsBooking aOpsBooking = this.getBookingById(aProductSharing.getBookRefNo());
                            OpsProduct aOpsProduct = this.getProductById(aOpsBooking, aProductSharing.getOrderID());

                            refundProcess(aOpsBooking, aOpsProduct, aProductSharing.getToSerialNumber(),
                                    aProductSharing.getPassengerId(), ReasonForRequest.CANCELLATION, new BigDecimal(0));
                            // : single room will be cancelled ( at supplier level )
                            boolean statusOfCancellationProcess = this.cancellationProcess(aOpsBooking, aOpsProduct, aProductSharing.getToSerialNumber(), aProductSharing.getPassengerId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return convertToHtml(message, "green");
                }
                if (sharingStatus.getProductSharingStatus().equalsIgnoreCase(ProductSharingStatus.REJECT.getProductSharingStatus())) {
                    String message = getMessage(Constants.ER907, Locale.ENGLISH);
                    return convertToHtml(message, "green");
                }
            } else {
                String message = getMessage(Constants.ER908, Locale.ENGLISH);
                return convertToHtml(message, "red");
            }

        } else {
            throw new OperationException(Constants.ER905); // Url has been expired
        }
        String message = getMessage(Constants.ER908, Locale.ENGLISH);
        return convertToHtml(message, "red");
    }

    /**
     * @param opsBooking
     * @param opsProduct
     * @param roomId
     * @param passengerId
     * @return
     */
    private boolean cancellationProcess(OpsBooking opsBooking, OpsProduct opsProduct, String roomId, String passengerId) {
        try {
            Object o = accoCancellationService.processCancellation(opsBooking, opsProduct);
            InvoiceDto invoiceDetails = financeService.getInvoicesByBookingDetails(opsBooking.getBookID(), opsProduct.getOrderID());
            Set<InvoiceParticularsDto> invoiceParticularsDto = invoiceDetails.getInvoiceParticularsDto();
            Set<InvoiceParticularsDto> updated = new HashSet<>();
            for (InvoiceParticularsDto particularsDto : invoiceParticularsDto) {
                particularsDto.setAmendmentCharges(0.0);  //todo get it from booking engine
                particularsDto.setAmendmentPrice(0.0);  //todo get it from booking engine
                updated.add(particularsDto);
            }
            invoiceDetails.setInvoiceParticularsDto(updated);
            InvoiceDto invoiceDto = financeService.cancelInvoiceDetails(invoiceDetails, opsBooking.getBookID());
            sendAnInvoice(invoiceDto.getInvoiceNumber());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("private boolean cancellationProcess(OpsBooking opsBooking, OpsProduct opsProduct, String roomId, String passengerId), exception raised : " + e);
        }
        return false;
    }

    /**
     * @param opsBooking
     * @param opsProduct
     * @param roomId
     * @param passengerId
     * @return
     */
    private boolean amendmentProcess(OpsBooking opsBooking, OpsProduct opsProduct, String roomId, String passengerId) {
        try {
            OpsOrderDetails orderDetails = opsProduct.getOrderDetails();
            OpsHotelDetails hotelDetails = orderDetails.getHotelDetails();
            List<OpsRoom> rooms = hotelDetails.getRooms();
            for (OpsRoom opsRoom : rooms) {
                if (opsRoom.getRoomID().equalsIgnoreCase(roomId)) {
                    List<OpsAccommodationPaxInfo> paxInfo = opsRoom.getPaxInfo();
                    for (OpsAccommodationPaxInfo opsAccommodationPaxInfo : paxInfo) {
                        if (opsAccommodationPaxInfo.getPaxID().equalsIgnoreCase(passengerId)) {
                            try {
                                Object response = addPaxService.addPaxDetails(opsBooking, opsProduct, opsRoom, opsAccommodationPaxInfo);
                            } catch (OperationException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                    break;
                }
            }
            //Amendment of invoice
            InvoiceDto invoiceDetails = financeService.getInvoicesByBookingDetails(opsBooking.getBookID(), opsProduct.getOrderID());
            Set<InvoiceParticularsDto> invoiceParticularsDto = invoiceDetails.getInvoiceParticularsDto();
            Set<InvoiceParticularsDto> updated = new HashSet<>();
            for (InvoiceParticularsDto particularsDto : invoiceParticularsDto) {
                particularsDto.setAmendmentCharges(0.0);  //todo :get it from booking engine
                particularsDto.setAmendmentPrice(0.0);  // todo :get it from booking engine
                updated.add(particularsDto);
            }
            invoiceDetails.setInvoiceParticularsDto(updated);
            InvoiceDto invoiceDto = this.financeService.amendInvoiceDetails(invoiceDetails, invoiceDetails.getInvoiceNumber());
            sendAnInvoice(invoiceDto.getInvoiceNumber());
            return true;
        } catch (Exception e) {
            logger.info("  private boolean amendmentProcess(OpsBooking opsBooking, OpsProduct opsProduct, String roomId, String passengerId)  , exception raised : " + e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param emailUsingTemplateResource
     */
    @Async
    private void sendAnEmail(EmailUsingTemplateResource emailUsingTemplateResource) {
        prettyJSON(emailUsingTemplateResource);
        ResponseEntity<EmailResponse> emailResponse = null;
        try {
            emailResponse = mdmRestUtils.postForEntity(this.urlForCommunication, emailUsingTemplateResource, EmailResponse.class);
            EmailResponse body = emailResponse.getBody();
            if (body.getStatus().equalsIgnoreCase("success")) {
                logger.info(" Email Status success   " + body.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
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
     * @param productSharingBookingCriteria
     * @return
     * @throws OperationException
     */
    @Override
    public List<SharedBooking> getSharedBookings(ProductSharingBookingCriteria productSharingBookingCriteria) throws OperationException {
        productSharingBookingCriteria.setRoomCategory("");
        try {
            ResponseEntity<SharedBooking[]> exchange = restUtils.postForEntity(getSharedBookingsUrl, productSharingBookingCriteria, SharedBooking[].class);
            SharedBooking[] sharedBookings = exchange.getBody();
            return Arrays.asList(sharedBookings);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(" public List<SharedBooking> getSharedBookings(ProductSharingBookingCriteria productSharingBookingCriteria)  , exception raised : " + e);
            throw new OperationException(Constants.ER111);
        }
    }

    /**
     * @param opsBooking
     * @param opsProduct
     * @param roomId
     * @param passengerId
     * @param reasonForRequest
     * @param ammount
     * @return
     */
    //todo:
    private Refund refundProcess(OpsBooking opsBooking, OpsProduct opsProduct, String roomId, String passengerId, ReasonForRequest reasonForRequest, BigDecimal ammount) {

        RefundResource refundResource = new RefundResource();
        try {
            refundResource.setBookingReferenceNo(opsBooking.getBookID());
            refundResource.setClientId(opsBooking.getClientID());
            refundResource.setClientType(opsBooking.getClientType());
            refundResource.setClaimCurrency(opsBooking.getClientCurrency());

            ProductDetail productDetail = new ProductDetail();
            productDetail.setOrderId(opsProduct.getOrderID());
            productDetail.setProductCategory(opsProduct.getProductCategory());
            productDetail.setProductCategorySubType(opsProduct.getProductSubCategory());

            refundResource.setProductDetail(productDetail);
            List<OpsPaymentInfo> paymentInfo = opsBooking.getPaymentInfo(); //: need to think
            refundResource.setDefaultModeOfPayment(paymentInfo.get(0).getPaymentMethod());
            refundResource.setClaimCurrency(paymentInfo.get(0).getAmountCurrency());
            refundResource.setReasonForRequest(reasonForRequest);
            refundResource.setRefundAmount(ammount); // : get it from add pax and full cancellation api
            refundResource.setRefundType(RefundTypes.REFUND_AMOUNT);
            refundResource.setCreatedByUserId(this.userService.getLoggedInUserId());
            Refund response = refundService.add(refundResource);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(" public Refund refundProcess(OpsBooking opsBooking, OpsProduct opsProduct, String roomId, String passengerId, ReasonForRequest reasonForRequest, BigDecimal ammount)  , exception raised : " + e);
        }
        return null;
    }

    /**
     * @param opsRoom
     * @param sharingMemberCount
     */
    public void dividePriceAccordingToOccupancy(OpsRoom opsRoom, int sharingMemberCount) {

        List<OpsOccupancyInfo> occupancyInfo = opsRoom.getOccupancyInfo();
        for (OpsOccupancyInfo opsOccupancyInfo : occupancyInfo) {
            String maxOccupancy = opsOccupancyInfo.getMaxOccupancy();
            String paxType = opsOccupancyInfo.getPaxType();

            if (sharingMemberCount <= ((int) Integer.parseInt(maxOccupancy))) {
                OpsRoomTotalPriceInfo roomTotalPriceInfo = opsRoom.getRoomTotalPriceInfo();
                String currencyCode = roomTotalPriceInfo.getCurrencyCode();

                String roomTotalPrice = roomTotalPriceInfo.getRoomTotalPrice();
            }
        }
        return;
    }

    /**
     * @param opsBooking
     * @param opsProduct
     * @return
     */
    private CommunicationTagResource getCommunicationTagResource(OpsBooking opsBooking, OpsProduct opsProduct) {
        CommunicationTagResource communicationTagResource = new CommunicationTagResource();
        communicationTagResource.setActionType("EMAIL");
        communicationTagResource.setBookId(opsBooking.getBookID());
        communicationTagResource.setOrderId(opsProduct.getOrderID());
        communicationTagResource.setClientId(opsBooking.getClientID());
        communicationTagResource.setCustomerId(opsBooking.getClientID());
        communicationTagResource.setSupplierId(opsProduct.getSupplierID());
        communicationTagResource.setFunction(PRDT_FUNCTION);
        communicationTagResource.setProcess(PRDT_BUSINESS_PROCESS);
        communicationTagResource.setScenario(PRDT_SCENARIO);
        return communicationTagResource;
    }

    /**
     * @param invoiceNumber
     * @return
     */
    private boolean sendAnInvoice(String invoiceNumber) {
        JSONObject request = new JSONObject();
        try {
            request.put("invoiceNumber", invoiceNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResponseEntity<?> exchange = null;
        try {
            exchange = mdmRestUtils.postForEntity(sendInvoice, request, Object.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object body = exchange.getBody();
        if (body != null) {
            return true;
        }
        return false;
    }


    @Autowired
    private AlertService alertService;


    /**
     * @param toDoTask
     * @param passengerName InlineMessageResource inlineMessageResource = new InlineMessageResource();
     *                      inlineMessageResource.setAlertName("ProductSharing");
     *                      inlineMessageResource.setNotificationType("System Event");
     *                      ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
     *                      map.put("PassengerName", passengerName);
     *                      inlineMessageResource.setDynamicVariables(map);
     *                      alertService.sendInlineMessageAlert(inlineMessageResource);
     * @return
     */
    @Override
    public Boolean sendAlertToUser(ToDoTask toDoTask, String passengerName) {
        try {
            InlineMessageResource inlineMessageResource = new InlineMessageResource();
            inlineMessageResource.setAlertName(productSharingAlertName);
            inlineMessageResource.setNotificationType("System");
            ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
            if (passengerName != null) {
                map.put("PassengerName", passengerName);
            }
            if (map != null) {
                inlineMessageResource.setDynamicVariables(map);
            }

            NotificationResource notificationResource = null;
            try {
                notificationResource = alertService.sendInlineMessageAlert(inlineMessageResource);
            } catch (OperationException e) {
                e.printStackTrace();
            }
//           ResponseEntity<NotificationResource> notificationResource = mdmRestUtils.postForEntity(getAlertUrl, inlineMessageResource, NotificationResource.class);
            if (notificationResource != null)
                return true;
            else
                return false;
        } catch (Exception e) {
            logger.info("public Boolean sendAlertToUser(ToDoTask toDoTask, String passengerName)  , exception raised : " + e);
            return false;
        }
    }

    /**
     * @param sharableRequest
     * @return
     */
    @Override
    public boolean updateIsSharableFlag(SharableRequest sharableRequest) {
        try {
            HttpEntity<SharableRequest> httpEntity = new HttpEntity<>(sharableRequest);
            ResponseEntity<?> isSharableUpdateResponse = restUtils.exchange(updateIsSharableURL, HttpMethod.PUT, httpEntity, String.class);
            if (isSharableUpdateResponse.getStatusCode().is2xxSuccessful())
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(" public boolean updateIsSharableFlag(SharableRequest sharableRequest)  , exception raised : " + e);
            return false;
        }
    }
}
