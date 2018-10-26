package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "tourCode",
        "subTourCode",
        "brandName",
        "tourStart",
        "tourEnd",
        "tourStartCity",
        "tourEndCity",
        "tourName",
        "travelEndDate",
        "travelStartDate",
        "multiCurrencyBooking",
        "amendDate",
        "roe",
        "destination",
        "country",
        "city",
        "noOfNights",
        "packageType",
        "companyPackageName"
})
public class HolidaysDetails {

    //Package Specific Fields
    @JsonProperty("status")
    private String status;

    @JsonProperty("tourCode")
    private String tourCode;

    @JsonProperty("subTourCode")
    private String subTourCode;

    @JsonProperty("brandName")
    private String brandName;

    @JsonProperty("tourStart")
    private String tourStart;

    @JsonProperty("tourEnd")
    private String tourEnd;

    @JsonProperty("tourStartCity")
    private String tourStartCity;

    @JsonProperty("tourEndCity")
    private String tourEndCity;

    @JsonProperty("tourName")
    private String tourName;

    @JsonProperty("travelEndDate")
    private String travelEndDate;

    @JsonProperty("travelStartDate")
    private String travelStartDate;

    @JsonProperty("multiCurrencyBooking")
    private Boolean isMultiCcyBooking;

    @JsonProperty("amendDate")
    private String amendDate;

    @JsonProperty("roe")
    private String roe;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("country")
    private String country;

    @JsonProperty("city")
    private String city;

    @JsonProperty("noOfNights")
    private String noOfNights;

    @JsonProperty("packageType")
    private String packageType;

    @JsonProperty("companyPackageName")
    private String companyPackageName;

    @JsonProperty("supplierType")
    private String supplierType;

    @JsonProperty("supplierReferenceNumber")
    private String supplierReferenceNumber;

    @JsonProperty("componentPricing")
    private HolidaysComponentPricing componentPricing;

    //Commercial Prices
    @JsonProperty("orderClientCommercials")
    private List<OrderClientCommercial> orderClientCommercials = new ArrayList<OrderClientCommercial>();

    @JsonProperty("orderSupplierCommercials")
    private List<OrderSupplierCommercial> orderSupplierCommercials = new ArrayList<OrderSupplierCommercial>();

    //Supplier Price
    @JsonProperty("orderSupplierPriceInfo")
    private HolidaysOrderPriceInfo orderSupplierPriceInfo;

    //Total Selling Price
    @JsonProperty("orderTotalPriceInfo")
    private HolidaysOrderPriceInfo orderTotalPriceInfo;

    @JsonProperty("accomodationDetails")
    private List<HolidaysAccommodationDetails> accomodationDetails = new ArrayList<HolidaysAccommodationDetails>();

    @JsonProperty("transferDetails")
    private List<HolidaysTransferDetails> transferDetails = new ArrayList<HolidaysTransferDetails>();

    @JsonProperty("insuranceDetails")
    private List<HolidaysInsuranceDetails> insuranceDetails = new ArrayList<HolidaysInsuranceDetails>();

    @JsonProperty("activityDetails")
    private List<HolidaysActivitiesDetails> activityDetails = new ArrayList<HolidaysActivitiesDetails>();

    @JsonProperty("extensionNightsDetails")
    private List<HolidaysExtensionDetails> extensionNightsDetails = new ArrayList<HolidaysExtensionDetails>();

    @JsonProperty("extrasDetails")
    private List<HolidaysExtrasDetails> extrasDetails = new ArrayList<HolidaysExtrasDetails>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTourCode() {
        return tourCode;
    }

    public void setTourCode(String tourCode) {
        this.tourCode = tourCode;
    }

    public String getSubTourCode() {
        return subTourCode;
    }

    public void setSubTourCode(String subTourCode) {
        this.subTourCode = subTourCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getTourStart() {
        return tourStart;
    }

    public void setTourStart(String tourStart) {
        this.tourStart = tourStart;
    }

    public String getTourEnd() {
        return tourEnd;
    }

    public void setTourEnd(String tourEnd) {
        this.tourEnd = tourEnd;
    }

    public String getTourStartCity() {
        return tourStartCity;
    }

    public void setTourStartCity(String tourStartCity) {
        this.tourStartCity = tourStartCity;
    }

    public String getTourEndCity() {
        return tourEndCity;
    }

    public void setTourEndCity(String tourEndCity) {
        this.tourEndCity = tourEndCity;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getTravelEndDate() {
        return travelEndDate;
    }

    public void setTravelEndDate(String travelEndDate) {
        this.travelEndDate = travelEndDate;
    }

    public String getTravelStartDate() {
        return travelStartDate;
    }

    public void setTravelStartDate(String travelStartDate) {
        this.travelStartDate = travelStartDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNoOfNights() {
        return noOfNights;
    }

    public void setNoOfNights(String noOfNights) {
        this.noOfNights = noOfNights;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getCompanyPackageName() {
        return companyPackageName;
    }

    public void setCompanyPackageName(String companyPackageName) {
        this.companyPackageName = companyPackageName;
    }

    public List<OrderClientCommercial> getOrderClientCommercials() {
        return orderClientCommercials;
    }

    public void setOrderClientCommercials(List<OrderClientCommercial> orderClientCommercials) {
        this.orderClientCommercials = orderClientCommercials;
    }

    public List<OrderSupplierCommercial> getOrderSupplierCommercials() {
        return orderSupplierCommercials;
    }

    public void setOrderSupplierCommercials(List<OrderSupplierCommercial> orderSupplierCommercials) {
        this.orderSupplierCommercials = orderSupplierCommercials;
    }

    public HolidaysOrderPriceInfo getOrderSupplierPriceInfo() {
        return orderSupplierPriceInfo;
    }

    public void setOrderSupplierPriceInfo(HolidaysOrderPriceInfo orderSupplierPriceInfo) {
        this.orderSupplierPriceInfo = orderSupplierPriceInfo;
    }

    public HolidaysOrderPriceInfo getOrderTotalPriceInfo() {
        return orderTotalPriceInfo;
    }

    public void setOrderTotalPriceInfo(HolidaysOrderPriceInfo orderTotalPriceInfo) {
        this.orderTotalPriceInfo = orderTotalPriceInfo;
    }

    public List<HolidaysAccommodationDetails> getAccomodationDetails() {
        return accomodationDetails;
    }

    public void setAccomodationDetails(List<HolidaysAccommodationDetails> accomodationDetails) {
        this.accomodationDetails = accomodationDetails;
    }

    public List<HolidaysTransferDetails> getTransferDetails() {
        return transferDetails;
    }

    public void setTransferDetails(List<HolidaysTransferDetails> transferDetails) {
        this.transferDetails = transferDetails;
    }

    public List<HolidaysInsuranceDetails> getInsuranceDetails() {
        return insuranceDetails;
    }

    public void setInsuranceDetails(List<HolidaysInsuranceDetails> insuranceDetails) {
        this.insuranceDetails = insuranceDetails;
    }

    public List<HolidaysActivitiesDetails> getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(List<HolidaysActivitiesDetails> activityDetails) {
        this.activityDetails = activityDetails;
    }

    public List<HolidaysExtensionDetails> getExtensionNightsDetails() {
        return extensionNightsDetails;
    }

    public void setExtensionNightsDetails(List<HolidaysExtensionDetails> extensionNightsDetails) {
        this.extensionNightsDetails = extensionNightsDetails;
    }

    public List<HolidaysExtrasDetails> getExtrasDetails() {
        return extrasDetails;
    }

    public void setExtrasDetails(List<HolidaysExtrasDetails> extrasDetails) {
        this.extrasDetails = extrasDetails;
    }

    public String getAmendDate() {
        return amendDate;
    }

    public void setAmendDate(String amendDate) {
        this.amendDate = amendDate;
    }

    public String getRoe() {
        return roe;
    }

    public void setRoe(String roe) {
        this.roe = roe;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public HolidaysComponentPricing getComponentPricing() {
        return componentPricing;
    }

    public void setComponentPricing(HolidaysComponentPricing componentPricing) {
        this.componentPricing = componentPricing;
    }

    public String getSupplierReferenceNumber() {
        return supplierReferenceNumber;
    }

    public void setSupplierReferenceNumber(String supplierReferenceNumber) {
        this.supplierReferenceNumber = supplierReferenceNumber;
    }

    public Boolean getMultiCcyBooking() {
        return isMultiCcyBooking;
    }

    public void setMultiCcyBooking(Boolean multiCcyBooking) {
        isMultiCcyBooking = multiCcyBooking;
    }
}
