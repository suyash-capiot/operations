package com.coxandkings.travel.operations.zmock.controller;

import com.coxandkings.travel.ext.model.be.Booking;
import com.coxandkings.travel.ext.model.be.BookingResponseBody;
import com.coxandkings.travel.ext.model.be.PaxInfo;
import com.coxandkings.travel.ext.model.be.Product;
import com.coxandkings.travel.operations.helper.booking.payment.AccountSummary;
import com.coxandkings.travel.operations.helper.booking.payment.ClientPaymentDetails;
import com.coxandkings.travel.operations.helper.booking.payment.SupplierPaymentDetails;
import com.coxandkings.travel.operations.resource.outbound.be.SearchFilterResource;
import com.coxandkings.travel.operations.zmock.resource.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="/zmock")
@CrossOrigin(origins = "*")
public class BookingController2 {
    ObjectMapper mapper = new ObjectMapper();
    TypeReference<List<Booking>> typeReference = new TypeReference<List<Booking>>(){};
    Logger logger = LogManager.getLogger(BookingController2.class);

    @GetMapping(value = "/getBooking/{bookingRefID}", produces = "application/json")
    @ApiOperation(
            value = "Get booking with given booking reference id",
            notes = "",
            response = Booking.class)
    public Booking getBooking(@PathVariable String bookingRefID) throws IOException {
        InputStream inputStream = BookingController2.class.getResourceAsStream("/zmock/json/booking.json");
        List<Booking> BookingDetailsList = mapper.readValue(inputStream,typeReference);
        logger.info("method called /getBooking with BookId "+ bookingRefID);

        return  BookingDetailsList.stream().filter(booking -> booking.getBookingResponseBody().getBookID().equals(bookingRefID)).findFirst().get();
    }

    @GetMapping(value = "/getProduct")
    public Product getProduct(@RequestParam String bookingRefID, @RequestParam String productId) throws IOException {
        Booking Booking = getBooking(bookingRefID);
        BookingResponseBody aResponseBody = Booking.getBookingResponseBody();
        Product product = aResponseBody.getProducts().stream().filter(product1 -> product1.getOrderID().equals(productId)).findFirst().get();
        return product;
    }

    @PostMapping(value = "updateProduct")
    public ResponseEntity updateProduct(@RequestParam String bookID, @RequestParam String orderID, @RequestBody Product product){
        logger.info("method called /updateProduct with bookID as "+ bookID+ "and order ID as "+ orderID + "with product details "+ product.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/getAllBooking", produces = "application/json")
    public List<Booking> getAllBooking() throws IOException {
        InputStream inputStream = BookingController2.class.getResourceAsStream("/zmock/json/booking.json");
        List<Booking> BookingDetailsList = mapper.readValue(inputStream,typeReference);
        logger.info("method called /getAllBooking");
        return BookingDetailsList;
    }
    @PostMapping(path = "/addProductInfo")
    public ResponseEntity getProductDetails(@RequestParam("bookingRefID") String bookingRefID,
                                            @RequestParam("productId") String productId,
                                            @RequestBody Product product){
        logger.info("method called /addProduct info with BookId "+ bookingRefID + " and ProductResource Id "+ productId + " and "+product.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/getOtherProduct")
    public ResponseEntity getOtherProduct(@RequestParam("bookingRefID") String bookingRefID,
                                          @RequestParam("productId") String productId){
        logger.info("method called /getOtherProduct with BookId "+ bookingRefID + " and ProductResource Id "+ productId);
        return new ResponseEntity (HttpStatus.OK);
    }

    @PostMapping(path = "/updateBooking")
    public ResponseEntity updateBooking(@RequestBody Booking Booking){
        logger.info("method called /updateBooking with Booking details "+ Booking.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(path = "/updateBookingList")
    public  ResponseEntity updateBookingList(@RequestBody List<Booking> Bookings){
        logger.info("method called /updateBookingList with booking details as ");
        for(Booking Booking : Bookings){
            logger.info("Booking details "+ Booking.toString());
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(path = "/favSearchFilter")
    public ResponseEntity<List<Booking>> searchViewFilter(@RequestBody SearchFilterResource resource) throws IOException {
        logger.info("method called /favSearchFilter with parameter "+ resource.toString());
        InputStream inputStream = BookingController2.class.getResourceAsStream("/zmock/json/booking.json");
        List<Booking> BookingDetailsList = mapper.readValue(inputStream,typeReference);
        return new ResponseEntity(BookingDetailsList,HttpStatus.OK);
    }

    @GetMapping(path = "/getClientPaymentDetails")
    private ClientPaymentDetails getClientPaymentDetails(@RequestParam String bookingRefID) throws IOException {
        logger.info("method called /getClientPaymentDetails with parameter "+ bookingRefID);
        InputStream inputStream = BookingController2.class.getResourceAsStream("/zmock/json/ClientPaymentDetails.json");
        TypeReference<ClientPaymentDetails> typeReference = new TypeReference<ClientPaymentDetails>(){};
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mapper.registerModule(new JSR310Module());
//        mapper.setDateFormat(simpleDateFormat);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ClientPaymentDetails clientPaymentDetails = mapper.readValue(inputStream, typeReference);
        return clientPaymentDetails;
    }

    @GetMapping("/getSupplierPaymentDetails")
    private SupplierPaymentDetails getSupplierPaymentDetails(@RequestParam String bookingRefID) throws  IOException{
        logger.info("method called /getSupplierPaymentDetails with parameter "+ bookingRefID);
        InputStream inputStream = BookingController2.class.getResourceAsStream("/zmock/json/SupplierPaymentDetails.json");
        TypeReference<SupplierPaymentDetails> typeReference = new TypeReference<SupplierPaymentDetails>(){};
        mapper.registerModule(new JSR310Module());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SupplierPaymentDetails supplierPaymentDetails = mapper.readValue(inputStream, typeReference);
        return supplierPaymentDetails;
    }

    @GetMapping("/getAccountSummary")
    private AccountSummary getAccountSummary(@RequestParam String bookingRefID) throws IOException {
        logger.info("method called /getAccountSummary with parameter "+ bookingRefID);
        InputStream inputStream = BookingController2.class.getResourceAsStream("/zmock/json/accountSummary.json");
        TypeReference<AccountSummary> typeReference = new TypeReference<AccountSummary>(){};
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mapper.registerModule(new JSR310Module());
        mapper.setDateFormat(simpleDateFormat);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        AccountSummary accountSummary = mapper.readValue(inputStream, typeReference);

        return accountSummary;
    }

    @PostMapping("/updateExpiryDate")
    private ResponseEntity updateExpiryDate(@RequestBody UpdateExpiryResource updateExpiryResource){
        logger.info("method called /updateExpiryDate with resource body as  "+ updateExpiryResource.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    private List<Product> getAllAccommodation() throws IOException {
        InputStream inputStream = BookingController2.class.getResourceAsStream("/zmock/json/booking.json");
        List<Booking> BookingDetailsList = mapper.readValue(inputStream,typeReference);
        List<Product> accommodationList = new ArrayList<>();
        for(Booking Booking : BookingDetailsList){
            for(Product prod: Booking.getBookingResponseBody().getProducts()){
                if(prod.getProductCategory().equalsIgnoreCase("Accommodation")){
                    accommodationList.add(prod);
                }
            }
        }
        return accommodationList;
    }

    @GetMapping("/amendPassengerName")
    private ResponseEntity<List<Product>> amendPassengerName(@RequestParam String type) throws IOException {
        logger.info("method called /getSupplierPaymentDetails with parameter type as "+ type +"and status as RQ");
        return new ResponseEntity<List<Product>>(getAllAccommodation(), HttpStatus.OK);
    }

    @PostMapping("/updateFlightDetails")
    private ResponseEntity updateFlightDetails(@RequestBody AirlineResource airlineResource){
        logger.info("method called /updateFlightDetails with request body as "+ airlineResource.toString());
        return new ResponseEntity("Flight details updated successfully!", HttpStatus.OK);
    }

    @GetMapping("/getTimeLimitExpiryCutOff")
    private Integer getTimeLimitExpiryCutOff(@RequestParam String orderID, @RequestParam String supplierID){
        logger.info("method called /getTimeLimitCutOff with orderID as "+ orderID+"asd supplierId as "+ supplierID);
        return 10;
    }

    @PostMapping("/updateBookingStatus")
    private ResponseEntity updateBookingStatus(@RequestBody BookingStatusResource bookingStatusResource){
        logger.info("method called /updateBookingStatus with userId as "+ bookingStatusResource.getUserID() +"with bookID as "+ bookingStatusResource.getBookID()+ "and status as "+ bookingStatusResource.getStatus() );
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/updateDob")
    private ResponseEntity updateDob(@RequestParam DobResource dobResponse){
        logger.info("method called /updateExpiryDate with resource body as  "+ dobResponse.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/updateClientReconfirmationDate")
    private ResponseEntity updateClientReconfirmationDate(@RequestBody ClientReconfirmationResource clientReconfirmationResource){
        logger.info("method called /updateExpiryDate with resource body as  "+ clientReconfirmationResource.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/updateSupplierReconfirmationDate")
    private ResponseEntity updateSupplierReconfirmationDate(@RequestBody SupplierReconfirmationDate supplierReconfirmationDate){
        logger.info("method called /updateSupplierReconfirmationDate with resource body as"+ supplierReconfirmationDate.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/updatePaxInfo")
    private ResponseEntity updatePaxInfo(@RequestBody PaxInfo paxInfo){
        logger.info("method called /updateExpiryDate with resource body as  "+ paxInfo);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/updateStayDates")
    private ResponseEntity updateStayDates(@RequestBody StayDatesResource stayDatesResource){
        logger.info("method called /updateStayDates with resource body as "+ stayDatesResource.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/updateTicketingPCC")
    private ResponseEntity updateTicketingPCC(@RequestBody TicketingPCCResource ticketingPCCResource){
        logger.info("method called /updateTicketingPCC with resource body as "+ ticketingPCCResource.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/changePassengerName")
    private ResponseEntity changePassengerName(@RequestBody PotentialListResource potentialListResource){
        logger.info("method called /updateTicketingPCC with Matched Booking Resource as  and Potential List resource as "+ potentialListResource.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{bookingRefId}/{supplierReferenceId}")
    private PartPaymentResource getPartPayment(@PathVariable String bookingRefId, @PathVariable String  supplierReferenceId){
        logger.info("method called /getPartPayment with booking id as "+ bookingRefId+" and supplierId as "+ supplierReferenceId);
        return new PartPaymentResource();
    }

    @PostMapping("/flight/updateSupplierCancelCharges")
    private ResponseEntity updateFlightSupplierCancellationCharges(@RequestBody FlightSupplierCancellationChargesResource flightSupplierCancellationChargesResource){
        logger.info("method called /updateSupplierCancellationCharges with request body as "+ flightSupplierCancellationChargesResource.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/flight/updateCompanyCancelCharges")
    private ResponseEntity updateFlightCompanyCancelCharges(@RequestBody FlightCompanyCancellationChargesResource flightCompanyCancellationChargesResource) {
        logger.info("method called /updateSupplierCancellationCharges with request body as " + flightCompanyCancellationChargesResource.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/flight/updateSupplierAmendCharges")
    private ResponseEntity updateFlightSupplierAmendCharges(@RequestBody FlightSupplierAmendmentChargesResource flightSupplierAmendmentChargesResource){
        logger.info("method called /updateSupplierAmendCharges"+ flightSupplierAmendmentChargesResource.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/flight/updateCompanyAmendCharges")
    private ResponseEntity updateFlightCompanyAmendCharges(@RequestBody FlightCompanyAmendmentChargesResource flightCompanyAmendmentChargesResource){
        logger.info("method called /updateCompanyAmendCharges with request body as "+ flightCompanyAmendmentChargesResource.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/accommodation/updateSupplierCancelCharges")
    private ResponseEntity updateAccommodationSupplierCancellationCharges(@RequestBody AccoSupplierCancellationChargesResource accoSupplierCancellationChargesResource){
        logger.info("method called /updateSupplierCancellationCharges with request body as "+ accoSupplierCancellationChargesResource.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/accommodation/updateCompanyCancelCharges")
    private ResponseEntity updateAccommodationCompanyCancelCharges(@RequestBody AccoCompanyCancellationChargesResource accoCompanyCancellationChargesResource) {
        logger.info("method called /updateSupplierCancellationCharges with request body as " + accoCompanyCancellationChargesResource.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/accommodation/updateSupplierAmendCharges")
    private ResponseEntity updateAccommodationSupplierAmendCharges(@RequestBody AccoSupplierAmendmentChargesResource accoSupplierAmendmentChargesResource){
        logger.info("method called /updateSupplierAmendCharges"+ accoSupplierAmendmentChargesResource.toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/accommodation/updateCompanyAmendCharges")
    private ResponseEntity updateAccommodationCompanyAmendCharges(@RequestBody AccoCompanyAmendmentChargesResource accoCompanyAmendmentChargesResource){
        logger.info("method called /updateCompanyAmendCharges with request body as "+ accoCompanyAmendmentChargesResource.toString());
        return new ResponseEntity(HttpStatus.OK);
    }
}