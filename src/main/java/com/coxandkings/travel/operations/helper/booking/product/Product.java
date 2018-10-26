package com.coxandkings.travel.operations.helper.booking.product;

import com.coxandkings.travel.operations.helper.booking.BookingDetails;
import com.coxandkings.travel.operations.helper.booking.Location;
import com.coxandkings.travel.operations.helper.booking.passengers.Passenger;
import com.coxandkings.travel.operations.helper.booking.payment.Refund;
import com.coxandkings.travel.operations.helper.booking.payment.SupplierPaymentDetails;
import com.coxandkings.travel.operations.helper.booking.product.accommodation.Accommodation;
import com.coxandkings.travel.operations.helper.booking.product.activities.ActivityUI;
import com.coxandkings.travel.operations.helper.booking.product.activities.Cruise;
import com.coxandkings.travel.operations.helper.booking.product.ancillary.Ancillary;
import com.coxandkings.travel.operations.helper.booking.product.comboProducts.ComboProducts;
import com.coxandkings.travel.operations.helper.booking.product.flightNewScreen.FlightNew;
import com.coxandkings.travel.operations.helper.booking.product.forex.Forex;
import com.coxandkings.travel.operations.helper.booking.product.holiday.Holiday;
import com.coxandkings.travel.operations.helper.booking.product.others.insurance.Insurance;
import com.coxandkings.travel.operations.helper.booking.product.others.visa.Visa;
import com.coxandkings.travel.operations.helper.booking.product.transport.bus.Bus;
import com.coxandkings.travel.operations.helper.booking.product.transport.car.Car;
import com.coxandkings.travel.operations.helper.booking.product.transport.flight.Flight;
import com.coxandkings.travel.operations.helper.booking.product.transport.rail.Rail;
import com.coxandkings.travel.operations.helper.booking.product.transport.rail.RailPass;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@JsonPropertyOrder( {"product"} )
@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",visible = true)
@JsonSubTypes(
    {
        @JsonSubTypes.Type(value = Accommodation.class, name = "accommodation"),
        @JsonSubTypes.Type(value = Car.class, name = "carPackage"),
        @JsonSubTypes.Type(value = Car.class, name = "rental"),
        @JsonSubTypes.Type(value = Car.class, name = "outStation"),
        @JsonSubTypes.Type(value = Car.class, name = "interCityTransfer"),
        @JsonSubTypes.Type(value = Bus.class, name = "bus"),
        @JsonSubTypes.Type(value = Rail.class, name = "rail"),
        @JsonSubTypes.Type(value = RailPass.class, name = "railPass"),
        @JsonSubTypes.Type(value = Flight.class, name = "flight"),
        @JsonSubTypes.Type(value = RailPass.class, name = "railpass"),
        @JsonSubTypes.Type(value = Forex.class, name = "forex"),
        @JsonSubTypes.Type(value = Visa.class, name = "visa"),
        @JsonSubTypes.Type(value = Insurance.class, name = "insurance"),
        @JsonSubTypes.Type(value = Cruise.class, name = "cruise"),
        @JsonSubTypes.Type(value = Holiday.class, name = "holiday"),
        @JsonSubTypes.Type(value = ActivityUI.class, name = "activity"),
        @JsonSubTypes.Type(value = Ancillary.class, name = "ancillary"),
        @JsonSubTypes.Type(value = ComboProducts.class,name = "comboProducts"),
        @JsonSubTypes.Type(value = FlightNew.class,name = "reconfirmSupplierForFlights")

    }
)
public abstract class Product implements Serializable {

    private Integer id;
    private Location location;
    private String productId;
    private String productName;
    private String productNameSubTypeId;
    private String productCategoryId;
    private String productFlavourId;
    private String productSubCategoryId;

    private Boolean bookedThroughInventory;
    private Long inventoryCutOffDate;

    private Long startDate;
    private Long endDate;

    @JsonTypeId
    private String type;

    private Long timeLimitExpiryDate;
    //this is even there in passenger level.. need confirmation
    private String inventory;
    private String fileHandler;
    private String airlineId;
    private String credentialName;
    private Long supplierReconfirmationDateTime;
    private Long clientReconfirmationDateTime;

    private Map< String, String > attributes;
    private BookingDetails bookingReference;
    private String bookingRef;
    Set<Passenger> passengers;
    private Status status;
    Set<Refund> refunds;
    Set<Ancillary> ancillaries;

    private String sourceSupplierId;
    private String enablerSupplierId;
    private String createdByUserId;
    private Long createdTime;
    private String lastModifiedByUserId;
    private Long lastModifiedTime;
    private String supplierId;
    private ComboProducts comboProducts;

    private String  productCategoryCode;
    private String productSubCategoryCode;
    private SupplierPaymentDetails supplierPaymentDetails;

    public String getAirlineId() { return airlineId; }

    public void setAirlineId(String airlineId) { this.airlineId = airlineId; }

    public BookingDetails getBookingReference() { return bookingReference; }

    public void setBookingReference(BookingDetails bookingReference) { this.bookingReference = bookingReference; }

    public ComboProducts getComboProducts() { return comboProducts; }

    public void setComboProducts(ComboProducts comboProducts) { this.comboProducts = comboProducts; }

    public String getProductSubCategoryCode() {
        return productSubCategoryCode;
    }

    public void setProductSubCategoryCode(String productSubCategoryCode) { this.productSubCategoryCode = productSubCategoryCode; }

    public String getProductCategoryCode() {
        return productCategoryCode;
    }

    public void setProductCategoryCode(String productCategoryCode) {
        this.productCategoryCode = productCategoryCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameSubTypeId() {
        return productNameSubTypeId;
    }

    public void setProductNameSubTypeId(String productNameSubTypeId) { this.productNameSubTypeId = productNameSubTypeId; }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductFlavourId() {
        return productFlavourId;
    }

    public void setProductFlavourId(String productFlavourId) {
        this.productFlavourId = productFlavourId;
    }

    public String getProductSubCategoryId() {
        return productSubCategoryId;
    }

    public void setProductSubCategoryId(String productSubCategoryId) { this.productSubCategoryId = productSubCategoryId; }

    public Boolean getBookedThroughInventory() {
        return bookedThroughInventory;
    }

    public void setBookedThroughInventory(Boolean bookedThroughInventory) { this.bookedThroughInventory = bookedThroughInventory; }

    public Long getInventoryCutOffDate() {
        return inventoryCutOffDate;
    }

    public void setInventoryCutOffDate(Long inventoryCutOffDate) {
        this.inventoryCutOffDate = inventoryCutOffDate;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTimeLimitExpiryDate() {
        return timeLimitExpiryDate;
    }

    public void setTimeLimitExpiryDate(Long timeLimitExpiryDate) {
        this.timeLimitExpiryDate = timeLimitExpiryDate;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getFileHandler() {
        return fileHandler;
    }

    public void setFileHandler(String fileHandler) {
        this.fileHandler = fileHandler;
    }

    public String getCredentialName() {
        return credentialName;
    }

    public void setCredentialName(String credentialName) {
        this.credentialName = credentialName;
    }

    public Long getSupplierReconfirmationDateTime() {
        return supplierReconfirmationDateTime;
    }

    public void setSupplierReconfirmationDateTime(Long supplierReconfirmationDateTime) {
        this.supplierReconfirmationDateTime = supplierReconfirmationDateTime;
    }

    public Long getClientReconfirmationDateTime() {
        return clientReconfirmationDateTime;
    }

    public void setClientReconfirmationDateTime(Long clientReconfirmationDateTime) {
        this.clientReconfirmationDateTime = clientReconfirmationDateTime;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getBookingRef() {
        return bookingRef;
    }

    public void setBookingRef(String bookingRef) {
        this.bookingRef = bookingRef;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Refund> getRefunds() {
        return refunds;
    }

    public void setRefunds(Set<Refund> refunds) {
        this.refunds = refunds;
    }

    public Set<Ancillary> getAncillaries() {
        return ancillaries;
    }

    public void setAncillaries(Set<Ancillary> ancillaries) {
        this.ancillaries = ancillaries;
    }

    public String getSourceSupplierId() {
        return sourceSupplierId;
    }

    public void setSourceSupplierId(String sourceSupplierId) {
        this.sourceSupplierId = sourceSupplierId;
    }

    public String getEnablerSupplierId() {
        return enablerSupplierId;
    }

    public void setEnablerSupplierId(String enablerSupplierId) {
        this.enablerSupplierId = enablerSupplierId;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public SupplierPaymentDetails getSupplierPaymentDetails() {
        return supplierPaymentDetails;
    }

    public void setSupplierPaymentDetails(SupplierPaymentDetails supplierPaymentDetails) {
        this.supplierPaymentDetails = supplierPaymentDetails;
    }
}
