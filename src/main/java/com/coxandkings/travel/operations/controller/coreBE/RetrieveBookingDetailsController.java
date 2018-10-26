package com.coxandkings.travel.operations.controller.coreBE;


import com.coxandkings.travel.ext.model.be.Booking;
import com.coxandkings.travel.operations.criteria.booking.becriteria.BookingSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.helper.booking.payment.AccountSummary;
import com.coxandkings.travel.operations.model.accountsummary.BookingPaymentAdviseInfo;
import com.coxandkings.travel.operations.model.accountsummary.InvoiceBasicInfo;
import com.coxandkings.travel.operations.model.core.OpsActionItemDetails;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsUniqueProductSubCategory;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.booking.AssignStaffResource;
import com.coxandkings.travel.operations.resource.outbound.be.BookingOverview;
import com.coxandkings.travel.operations.resource.searchviewfilter.BookingSearchResponseItem;
import com.coxandkings.travel.operations.service.accountsummary.AccountSummaryService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.adapter.OpsBookingAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/bookingService")
public class RetrieveBookingDetailsController {

    private static Logger logger = LogManager.getLogger(RetrieveBookingDetailsController.class);

    @Value(value = "${booking_engine.base_url}")
    private String beBaseUrl;

    @Autowired
    OpsBookingAdapter opsBookingAdapter;

    @Value(value = "${server.port}")
    private String serverPort;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private AccountSummaryService accountSummaryService;

    @GetMapping("/v1/getBooking/{bookingRefId}")
    public HttpEntity<OpsBooking> getBooking(@PathVariable String bookingRefId) throws OperationException, ParseException {
        logger.info("-- RetrieveBookingDetailsController : GET  : /getBooking --");
        logger.info("Parameters for above call : " + "(key  : bookingRefId, value : " + bookingRefId + " )");
        Long startTime = System.currentTimeMillis();
        OpsBooking toReturn = opsBookingService.getBooking(bookingRefId);
        Long lastTime = System.currentTimeMillis();
        logger.info("Controller - Time required to ops getBooking:" + (lastTime - startTime));
        logger.info("-- Exit retrieveBookingDetailsController : GET  : /getBooking --");
        return new ResponseEntity<>(toReturn, HttpStatus.OK);

    }

    @GetMapping("/v1/getUniqueProductSubCategories/{bookingId}")
    public ResponseEntity<List<OpsUniqueProductSubCategory>> getUniqueProductSubCategories(@PathVariable String bookingId)
            throws OperationException {
        try {
            logger.info("*** Entering getUniqueProductSubCategories() method ***");
            List<OpsUniqueProductSubCategory> opsProductSubCategories =
                    opsBookingService.getUniqueProductSubCategories(bookingId);
            logger.info("*** Exit getUniqueProductSubCategories() method ***");
            return new ResponseEntity<>(opsProductSubCategories, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11201);
        }
    }


    @GetMapping("/v1/getOrdersBySubCategory/{bookingId}")
    public ResponseEntity<List<OpsProduct>> getOrdersBySubCategory(@PathVariable String bookingId,
                                                                   @RequestParam String subCategory) throws OperationException {
        try {
            logger.info("Entering method: \n" + "getOrdersBySubCategory() " + " Booking ID: " + bookingId + " Sub Category: " + subCategory);
            List<OpsProduct> opsProductPerCategory =
                    opsBookingService.getProductsBySubCategory(subCategory, bookingId);
            return new ResponseEntity<>(opsProductPerCategory, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11202);
        }
    }

    @GetMapping("/v1/getBookingOverview")
    public BookingOverview getBookingOverview(@RequestParam(value = "bookingRefId") String bookingRefId) throws OperationException {
        logger.info("-- RetrieveBookingDetailsController : GET  : /getBookingOverview --");
        logger.info("Parameters for above call : " + "(key  : bookingRefId, value : " + bookingRefId + " )");
        BookingOverview aBookOverview = null;
        OpsBooking booking = opsBookingService.getBooking(bookingRefId);
        aBookOverview = opsBookingService.getBookingOverview(booking);
        return aBookOverview;
    }


    @GetMapping("/v1/getProduct")
    public OpsProduct getProduct(@RequestParam(name = "bookingRefId") String bookingRefId,
                                 @RequestParam(name = "productId") String productId) throws OperationException {

        logger.info("-- RetrieveBookingDetailsController : GET  : /getBookingOverview --");
        logger.info("Parameters for above call : " + "(key  : bookingRefId, value : " + bookingRefId + " )" + ", (key : productId, value : " + productId + ")");
        OpsProduct opsProduct = opsBookingService.getProduct(bookingRefId, productId);
        return opsProduct;

    }

    @GetMapping("/v1/getAccountSummary/{bookingRefId}")
    public AccountSummary getAccountSummary(@PathVariable String bookingRefId) throws OperationException {
        logger.info("-- RetrieveBookingDetailsController : GET  : /getAccountSummary --");
        logger.info("Parameters for above call : " + "(key  : bookingRefId, value : " + bookingRefId + " )");
        AccountSummary bookingAccSummary = new AccountSummary();
        OpsBooking aBooking = opsBookingService.getBooking(bookingRefId);

        try {
            List<InvoiceBasicInfo> invoiceInfoList = accountSummaryService.getInvoiceInfoForBooking(bookingRefId);
            logger.info("Loaded Invoice information from Finance module");
            bookingAccSummary.setInvoiceInfoList(invoiceInfoList);
            try {
                List<BookingPaymentAdviseInfo> paymentAdviseList = accountSummaryService.loadPaymentAdvisesForBooking(aBooking);
                bookingAccSummary.setPaymentAdviseList(paymentAdviseList);
            }catch(Exception e){}

            accountSummaryService.updateAccountSummaryForPayments(bookingAccSummary);
        } catch (Exception e) {
            logger.error("Error occurred in loading Account Summary" + e);
            throw new OperationException(Constants.OPS_ERR_11205);
        }

        /*
        SupplierPaymentDetails supplierPaymentDetails = new SupplierPaymentDetails();
        List<SupplierPayment> supplierPaymentList = new ArrayList<>();
        OpsBooking opsBooking = opsBookingService.getBooking(bookingRefId);

        String urlAccountSummary  ="http://localhost:"+ serverPort + "/zmock/";

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(urlAccountSummary + "getAccountSummary");
        uriComponentsBuilder.queryParam("bookingRefID", bookingRefId);
        System.out.println(uriComponentsBuilder.toUriString());
        AccountSummary accountSummary = null;

        try {
            RestTemplate restTemplate = RestUtils.getTemplate();
            String accountSummaryString = restTemplate.getForObject(uriComponentsBuilder.toUriString(), String.class);
            ObjectMapper objMapper = new ObjectMapper();
            objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            accountSummary = objMapper.readValue(accountSummaryString, AccountSummary.class);


            List<OpsProduct> opsProductList = opsBooking.getProducts();
            for (OpsProduct aOpsProduct : opsProductList) {
                SupplierPayment aSupplierPayment = new SupplierPayment();
                if (aOpsProduct.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
                    aSupplierPayment.setNetPybleToSupplr(aOpsProduct.getOrderDetails().
                            getFlightDetails().getOpsFlightSupplierPriceInfo().getSupplierPrice());
                }
                if (aOpsProduct.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)) {

                    aSupplierPayment.setNetPybleToSupplr(aOpsProduct.getOrderDetails().
                            getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getSupplierPrice());
                }
                aSupplierPayment.setProductType(aOpsProduct.getProductSubCategory());
                aSupplierPayment.setSupplierName(aOpsProduct.getEnamblerSupplierName());
                aSupplierPayment.setSupplierId(aOpsProduct.getSupplierID());
                aSupplierPayment.setOrderId(aOpsProduct.getOrderID());
                supplierPaymentList.add(aSupplierPayment);
            }
            supplierPaymentDetails.setSupplierPayments(supplierPaymentList);
            accountSummary.setSupplierPaymentDetails(supplierPaymentDetails);

        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        return bookingAccSummary;
    }

/*    @GetMapping("/getAccountSummary/{bookingRefId}")
    public void getAccountSummary( @PathVariable String bookingRefId )   {

    }*/

    @GetMapping("/v1/getClientPaymentDetails/{bookingRefId}")
    public AccountSummary getClientPaymentDetails(@PathVariable String bookingRefId) throws OperationException {
        logger.info("-- RetrieveBookingDetailsController : GET  : /getClientPaymentDetails --");
        logger.info("Parameters for above call : " + "(key  : bookingRefId, value : " + bookingRefId + " )");
        AccountSummary bookingAccSummary = accountSummaryService.getClientPaymentDetails(bookingRefId);
        return bookingAccSummary;
    }

    @PostMapping(value = "/v1/getBookingsByCriteria", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OpsBooking>> findBySearchCriteria(@RequestBody BookingSearchCriteria bookingSearchCriteria) throws OperationException {
        try {
            ResponseEntity<List<Booking>> searchResult = null;
            List<Booking> booking2s = null;
            List<OpsBooking> opsBookings = opsBookingService.getBookingByCriteria(bookingSearchCriteria);
            return new ResponseEntity<>(opsBookings, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11206);
        }
    }

    @PostMapping(value = "/v1/searchBookings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookingSearchResponseItem>> searchBookings(@RequestBody BookingSearchCriteria bookingSearchCriteria)
            throws OperationException {
        logger.info("*** Entering searchBookings() method ***");
        List<BookingSearchResponseItem> bookingSearchResponseItemList = opsBookingService.searchBookings(bookingSearchCriteria);
        logger.info("*** Exit searchBookings() method ***");
        return new ResponseEntity<>(bookingSearchResponseItemList, HttpStatus.OK);
    }


    @GetMapping("/v1/actionItems/get/{bookingID}/{orderID}")
    public ResponseEntity<OpsActionItemDetails> getOrderActionItems(@PathVariable String bookingID, @PathVariable String orderID) throws OperationException {
        try {
            logger.info("-- RetrieveBookingDetailsController : GET : /actionItems/get/{productSubCategory} --");
            OpsActionItemDetails opsOrderMenuItems = opsBookingService.getOrderActionItems(bookingID, orderID);
            return new ResponseEntity<OpsActionItemDetails>(opsOrderMenuItems, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_11208);
        }
    }

    @PostMapping(value = "/v1/assignStaff", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResource> assignStaff(@RequestBody AssignStaffResource assignStaffResource) throws OperationException {
        try {
            MessageResource messageResource = opsBookingService.assignStaff(assignStaffResource);
            return new ResponseEntity<MessageResource>(messageResource, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_11209);
        }
    }

    @PostMapping(value = "/v1/autosuggestion/{autoSuggestionType}")
    public ResponseEntity<List<JSONObject>> getAutoSuggestionBasedOnType(InputStream stream,@PathVariable String autoSuggestionType)
    {
        List<JSONObject> supplierRefByAutoSuggest = opsBookingService.getAutoSuggestions(stream,autoSuggestionType);
        return new ResponseEntity<>(supplierRefByAutoSuggest,HttpStatus.OK);
    }

}