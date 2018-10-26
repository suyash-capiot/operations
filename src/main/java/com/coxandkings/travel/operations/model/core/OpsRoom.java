package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsRoom implements Serializable {

    @JsonProperty("roomSuppPriceInfo")
    private OpsRoomSuppPriceInfo roomSuppPriceInfo;

    @JsonProperty("mealInfo")
    private OpsMealInfo mealInfo;

    @JsonProperty("checkIn")
    private String checkIn;

    @JsonProperty("roomTotalPriceInfo")
    private OpsRoomTotalPriceInfo roomTotalPriceInfo;

    @JsonProperty("roomTypeInfo")
    private OpsRoomTypeInfo roomTypeInfo;

    @JsonProperty("ratePlanInfo")
    private OpsRatePlanInfo ratePlanInfo;

    @JsonProperty("paxInfo")
    private List<OpsAccommodationPaxInfo> paxInfo = new ArrayList<OpsAccommodationPaxInfo>();

    @JsonProperty("roomSuppCommercials")
    private List<OpsRoomSuppCommercial> roomSuppCommercials = new ArrayList<OpsRoomSuppCommercial>();

    @JsonProperty("roomClientCommercials")
    private List<OpsClientEntityCommercial> opsClientEntityCommercial = new ArrayList<OpsClientEntityCommercial>();

    @JsonProperty("checkOut")
    private String checkOut;

    @JsonProperty("roomID")
    private String roomID;

    @JsonProperty("refundableIndicator")
    private Boolean refundableIndicator;

    @JsonProperty("isSharable")
    private boolean isSharable;

    @JsonProperty("supplierRoomIndex")
    private String supplierRoomIndex;

    @JsonProperty("occupancyInfo")
    private List<OpsOccupancyInfo> occupancyInfo;

    private Map<String, Boolean> roomLevelActions = new HashMap<>();

    private String status;

    private final static long serialVersionUID = -6246395885438684169L;

    private List<OpsAmendDetails> amendmentChargesDetails;

    private List<OpsCancDetails> cancellationChargesDetails;

    //policy
    private List<OpsPolicy> policies;

    private List<OpsOfferCodes> offerCodes;

    private List<OpsOfferDetailSet> offerDetailSet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsRoom room = (OpsRoom) o;
        return Objects.equals(roomSuppPriceInfo, room.roomSuppPriceInfo) &&
                Objects.equals(mealInfo, room.mealInfo) &&
                Objects.equals(checkIn, room.checkIn) &&
                Objects.equals(roomTotalPriceInfo, room.roomTotalPriceInfo) &&
                Objects.equals(roomTypeInfo, room.roomTypeInfo) &&
                Objects.equals(ratePlanInfo, room.ratePlanInfo) &&
                Objects.equals(paxInfo, room.paxInfo) &&
                Objects.equals(roomSuppCommercials, room.roomSuppCommercials) &&
                Objects.equals(opsClientEntityCommercial, room.opsClientEntityCommercial) &&
                Objects.equals(checkOut, room.checkOut) &&
                Objects.equals(roomID, room.roomID) &&
                Objects.equals(refundableIndicator, room.refundableIndicator) &&
                Objects.equals(roomLevelActions, room.roomLevelActions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomSuppPriceInfo, mealInfo, checkIn, roomTotalPriceInfo, roomTypeInfo, ratePlanInfo, paxInfo, roomSuppCommercials, opsClientEntityCommercial, checkOut, roomID, refundableIndicator, roomLevelActions);
    }

    public OpsRoomSuppPriceInfo getRoomSuppPriceInfo() {
        return roomSuppPriceInfo;
    }

    public void setRoomSuppPriceInfo(OpsRoomSuppPriceInfo roomSuppPriceInfo) {
        this.roomSuppPriceInfo = roomSuppPriceInfo;
    }

    public List<OpsOfferDetailSet> getOfferDetailSet() {
        return offerDetailSet;
    }

    public void setOfferDetailSet(List<OpsOfferDetailSet> offerDetailSet) {
        this.offerDetailSet = offerDetailSet;
    }

    public List<OpsOfferCodes> getOfferCodes() {
        return offerCodes;
    }

    public void setOfferCodes(List<OpsOfferCodes> offerCodes) {
        this.offerCodes = offerCodes;
    }

    public OpsMealInfo getMealInfo() {
        return mealInfo;
    }

    public void setMealInfo(OpsMealInfo mealInfo) {
        this.mealInfo = mealInfo;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public OpsRoomTotalPriceInfo getRoomTotalPriceInfo() {
        return roomTotalPriceInfo;
    }

    public void setRoomTotalPriceInfo(OpsRoomTotalPriceInfo roomTotalPriceInfo) {
        this.roomTotalPriceInfo = roomTotalPriceInfo;
    }

    public OpsRoomTypeInfo getRoomTypeInfo() {
        return roomTypeInfo;
    }

    public void setRoomTypeInfo(OpsRoomTypeInfo roomTypeInfo) {
        this.roomTypeInfo = roomTypeInfo;
    }

    public OpsRatePlanInfo getRatePlanInfo() {
        return ratePlanInfo;
    }

    public void setRatePlanInfo(OpsRatePlanInfo ratePlanInfo) {
        this.ratePlanInfo = ratePlanInfo;
    }

    public List<OpsAccommodationPaxInfo> getPaxInfo() {
        return paxInfo;
    }

    public void setPaxInfo(List<OpsAccommodationPaxInfo> paxInfo) {
        this.paxInfo = paxInfo;
    }

    public List<OpsRoomSuppCommercial> getRoomSuppCommercials() {
        return roomSuppCommercials;
    }

    public void setRoomSuppCommercials(List<OpsRoomSuppCommercial> roomSuppCommercials) {
        this.roomSuppCommercials = roomSuppCommercials;
    }

    public List<OpsClientEntityCommercial> getOpsClientEntityCommercial() {
        return opsClientEntityCommercial;
    }

    public void setOpsClientEntityCommercial(List<OpsClientEntityCommercial> opsClientEntityCommercial) {
        this.opsClientEntityCommercial = opsClientEntityCommercial;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Boolean getRefundableIndicator() {
        return refundableIndicator;
    }

    public void setRefundableIndicator(Boolean refundableIndicator) {
        this.refundableIndicator = refundableIndicator;
    }

    public boolean isSharable() {
        return isSharable;
    }

    public void setSharable(boolean sharable) {
        isSharable = sharable;
    }

    public String getSupplierRoomIndex() {
        return supplierRoomIndex;
    }

    public void setSupplierRoomIndex(String supplierRoomIndex) {
        this.supplierRoomIndex = supplierRoomIndex;
    }

    public List<OpsOccupancyInfo> getOccupancyInfo() {
        return occupancyInfo;
    }

    public void setOccupancyInfo(List<OpsOccupancyInfo> occupancyInfo) {
        this.occupancyInfo = occupancyInfo;
    }

    public Map<String, Boolean> getRoomLevelActions() {
        return roomLevelActions;
    }

    public void setRoomLevelActions(Map<String, Boolean> roomLevelActions) {
        this.roomLevelActions = roomLevelActions;
    }


    public List<OpsAmendDetails> getAmendmentChargesDetails() {
        return amendmentChargesDetails;
    }

    public void setAmendmentChargesDetails(List<OpsAmendDetails> amendmentChargesDetails) {
        this.amendmentChargesDetails = amendmentChargesDetails;
    }

    public List<OpsCancDetails> getCancellationChargesDetails() {
        return cancellationChargesDetails;
    }

    public void setCancellationChargesDetails(List<OpsCancDetails> cancellationChargesDetails) {
        this.cancellationChargesDetails = cancellationChargesDetails;
    }

    public List<OpsPolicy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<OpsPolicy> policies) {
        this.policies = policies;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
