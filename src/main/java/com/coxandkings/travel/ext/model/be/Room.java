
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "supplierPriceInfo",
        "mealInfo",
        "checkIn",
        "clientCommercials",
        "roomTypeInfo",
        "ratePlanInfo",
        "paxInfo",
        "totalPriceInfo",
        "supplierCommercials",
        "checkOut",
        "roomID",
        "occupancyInfo",
        "status"
})
public class Room implements Serializable {

    @JsonProperty("supplierPriceInfo")
    private SupplierPriceInfo supplierPriceInfo;
    @JsonProperty("mealInfo")
    private MealInfo mealInfo;
    @JsonProperty("checkIn")
    private String checkIn;
    @JsonProperty("clientCommercials")
    private List<ClientEntityCommercial> clientEntityCommercials = new ArrayList<ClientEntityCommercial>();
    @JsonProperty("roomTypeInfo")
    private RoomTypeInfo roomTypeInfo;
    @JsonProperty("ratePlanInfo")
    private RatePlanInfo ratePlanInfo;
    @JsonProperty("paxInfo")
    private List<PaxInfo> paxInfo = new ArrayList<PaxInfo>();
    @JsonProperty("totalPriceInfo")
    private TotalPriceInfo totalPriceInfo;
    @JsonProperty("supplierCommercials")
    private List<SupplierCommercial> supplierCommercials = new ArrayList<SupplierCommercial>();
    @JsonProperty("checkOut")
    private String checkOut;
    @JsonProperty("roomID")
    private String roomID;

    @JsonProperty("occupancyInfo")
    private List<OccupancyInfo> occupancyInfo;

    @JsonProperty("supplierRoomIndex")
    private String supplierRoomIndex;
    @JsonProperty("status")
    private String status;
    private final static long serialVersionUID = 1585856408135559367L;
    @JsonProperty("policies")
    private List<Policy> policies;

    @JsonProperty("offerCodes")
    private List<OfferCodes> offerCodes;

    @JsonProperty("offerDetailsSet")
    private List<OfferDetailsSet> offerDetailsSet;
    /**
     * No args constructor for use in serialization
     */
    public Room() {
    }

    /**
     * @param supplierPriceInfo
     * @param mealInfo
     * @param checkIn
     * @param clientCommercials
     * @param roomTypeInfo
     * @param ratePlanInfo
     * @param paxInfo
     * @param totalPriceInfo
     * @param supplierCommercials
     * @param checkOut
     * @param roomID
     */
    public Room(SupplierPriceInfo supplierPriceInfo, MealInfo mealInfo, String checkIn, List<ClientCommercial> clientCommercials, RoomTypeInfo roomTypeInfo, RatePlanInfo ratePlanInfo, List<PaxInfo> paxInfo, TotalPriceInfo totalPriceInfo, List<SupplierCommercial> supplierCommercials, String checkOut, String roomID) {
        super();
        this.supplierPriceInfo = supplierPriceInfo;
        this.mealInfo = mealInfo;
        this.checkIn = checkIn;
        this.clientEntityCommercials = clientEntityCommercials;
        this.roomTypeInfo = roomTypeInfo;
        this.ratePlanInfo = ratePlanInfo;
        this.paxInfo = paxInfo;
        this.totalPriceInfo = totalPriceInfo;
        this.supplierCommercials = supplierCommercials;
        this.checkOut = checkOut;
        this.roomID = roomID;
    }

    @JsonProperty("supplierPriceInfo")
    public SupplierPriceInfo getSupplierPriceInfo() {
        return supplierPriceInfo;
    }

    @JsonProperty("supplierPriceInfo")
    public void setSupplierPriceInfo(SupplierPriceInfo supplierPriceInfo) {
        this.supplierPriceInfo = supplierPriceInfo;
    }

    @JsonProperty("mealInfo")
    public MealInfo getMealInfo() {
        return mealInfo;
    }

    @JsonProperty("mealInfo")
    public void setMealInfo(MealInfo mealInfo) {
        this.mealInfo = mealInfo;
    }

    @JsonProperty("checkIn")
    public String getCheckIn() {
        return checkIn;
    }

    @JsonProperty("checkIn")
    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public List<ClientEntityCommercial> getClientEntityCommercials() {
        return clientEntityCommercials;
    }

    public void setClientEntityCommercials(List<ClientEntityCommercial> clientEntityCommercials) {
        this.clientEntityCommercials = clientEntityCommercials;
    }

    public List<OfferCodes> getOfferCodes() {
        return offerCodes;
    }

    public void setOfferCodes(List<OfferCodes> offerCodes) {
        this.offerCodes = offerCodes;
    }

    @JsonProperty("roomTypeInfo")
    public RoomTypeInfo getRoomTypeInfo() {
        return roomTypeInfo;
    }

    @JsonProperty("roomTypeInfo")
    public void setRoomTypeInfo(RoomTypeInfo roomTypeInfo) {
        this.roomTypeInfo = roomTypeInfo;
    }

    @JsonProperty("ratePlanInfo")
    public RatePlanInfo getRatePlanInfo() {
        return ratePlanInfo;
    }

    @JsonProperty("ratePlanInfo")
    public void setRatePlanInfo(RatePlanInfo ratePlanInfo) {
        this.ratePlanInfo = ratePlanInfo;
    }

    @JsonProperty("paxInfo")
    public List<PaxInfo> getPaxInfo() {
        return paxInfo;
    }

    @JsonProperty("paxInfo")
    public void setPaxInfo(List<PaxInfo> paxInfo) {
        this.paxInfo = paxInfo;
    }

    @JsonProperty("totalPriceInfo")
    public TotalPriceInfo getTotalPriceInfo() {
        return totalPriceInfo;
    }

    @JsonProperty("totalPriceInfo")
    public void setTotalPriceInfo(TotalPriceInfo totalPriceInfo) {
        this.totalPriceInfo = totalPriceInfo;
    }

    @JsonProperty("supplierCommercials")
    public List<SupplierCommercial> getSupplierCommercials() {
        return supplierCommercials;
    }

    @JsonProperty("supplierCommercials")
    public void setSupplierCommercials(List<SupplierCommercial> supplierCommercials) {
        this.supplierCommercials = supplierCommercials;
    }

    @JsonProperty("checkOut")
    public String getCheckOut() {
        return checkOut;
    }

    @JsonProperty("checkOut")
    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    @JsonProperty("roomID")
    public String getRoomID() {
        return roomID;
    }

    @JsonProperty("roomID")
    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public List<OccupancyInfo> getOccupancyInfo() {
        return occupancyInfo;
    }

    public void setOccupancyInfo(List<OccupancyInfo> occupancyInfo) {
        this.occupancyInfo = occupancyInfo;
    }

    public String getSupplierRoomIndex() {
        return supplierRoomIndex;
    }

    public void setSupplierRoomIndex(String supplierRoomIndex) {
        this.supplierRoomIndex = supplierRoomIndex;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OfferDetailsSet> getOfferDetailsSet() {
        return offerDetailsSet;
    }

    public void setOfferDetailsSet(List<OfferDetailsSet> offerDetailsSet) {
        this.offerDetailsSet = offerDetailsSet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Room.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("supplierPriceInfo");
        sb.append('=');
        sb.append(((this.supplierPriceInfo == null) ? "<null>" : this.supplierPriceInfo));
        sb.append(',');
        sb.append("mealInfo");
        sb.append('=');
        sb.append(((this.mealInfo == null) ? "<null>" : this.mealInfo));
        sb.append(',');
        sb.append("checkIn");
        sb.append('=');
        sb.append(((this.checkIn == null) ? "<null>" : this.checkIn));
        sb.append(',');
        sb.append("clientCommercials");
        sb.append('=');
        sb.append(((this.clientEntityCommercials == null) ? "<null>" : this.clientEntityCommercials));
        sb.append(',');
        sb.append("roomTypeInfo");
        sb.append('=');
        sb.append(((this.roomTypeInfo == null) ? "<null>" : this.roomTypeInfo));
        sb.append(',');
        sb.append("ratePlanInfo");
        sb.append('=');
        sb.append(((this.ratePlanInfo == null) ? "<null>" : this.ratePlanInfo));
        sb.append(',');
        sb.append("paxInfo");
        sb.append('=');
        sb.append(((this.paxInfo == null) ? "<null>" : this.paxInfo));
        sb.append(',');
        sb.append("totalPriceInfo");
        sb.append('=');
        sb.append(((this.totalPriceInfo == null) ? "<null>" : this.totalPriceInfo));
        sb.append(',');
        sb.append("supplierCommercials");
        sb.append('=');
        sb.append(((this.supplierCommercials == null) ? "<null>" : this.supplierCommercials));
        sb.append(',');
        sb.append("checkOut");
        sb.append('=');
        sb.append(((this.checkOut == null) ? "<null>" : this.checkOut));
        sb.append(',');
        sb.append("policies");
        sb.append('=');
        sb.append(((this.policies == null) ? "<null>" : this.policies));
        sb.append(',');
        sb.append("roomID");
        sb.append('=');
        sb.append(((this.roomID == null) ? "<null>" : this.roomID));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.supplierPriceInfo == null) ? 0 : this.supplierPriceInfo.hashCode()));
        result = ((result * 31) + ((this.mealInfo == null) ? 0 : this.mealInfo.hashCode()));
        result = ((result * 31) + ((this.checkIn == null) ? 0 : this.checkIn.hashCode()));
        result = ((result * 31) + ((this.clientEntityCommercials == null) ? 0 : this.clientEntityCommercials.hashCode()));
        result = ((result * 31) + ((this.roomTypeInfo == null) ? 0 : this.roomTypeInfo.hashCode()));
        result = ((result * 31) + ((this.ratePlanInfo == null) ? 0 : this.ratePlanInfo.hashCode()));
        result = ((result * 31) + ((this.paxInfo == null) ? 0 : this.paxInfo.hashCode()));
        result = ((result * 31) + ((this.totalPriceInfo == null) ? 0 : this.totalPriceInfo.hashCode()));
        result = ((result * 31) + ((this.supplierCommercials == null) ? 0 : this.supplierCommercials.hashCode()));
        result = ((result * 31) + ((this.checkOut == null) ? 0 : this.checkOut.hashCode()));
        result = ((result * 31) + ((this.roomID == null) ? 0 : this.roomID.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Room) == false) {
            return false;
        }
        Room rhs = ((Room) other);
        return ((((((((((((this.supplierPriceInfo == rhs.supplierPriceInfo) || ((this.supplierPriceInfo != null) && this.supplierPriceInfo.equals(rhs.supplierPriceInfo))) && ((this.mealInfo == rhs.mealInfo) || ((this.mealInfo != null) && this.mealInfo.equals(rhs.mealInfo)))) && ((this.checkIn == rhs.checkIn) || ((this.checkIn != null) && this.checkIn.equals(rhs.checkIn)))) && ((this.clientEntityCommercials == rhs.clientEntityCommercials) || ((this.clientEntityCommercials != null) && this.clientEntityCommercials.equals(rhs.clientEntityCommercials)))) && ((this.roomTypeInfo == rhs.roomTypeInfo) || ((this.roomTypeInfo != null) && this.roomTypeInfo.equals(rhs.roomTypeInfo)))) && ((this.ratePlanInfo == rhs.ratePlanInfo) || ((this.ratePlanInfo != null) && this.ratePlanInfo.equals(rhs.ratePlanInfo)))) && ((this.paxInfo == rhs.paxInfo) || ((this.paxInfo != null) && this.paxInfo.equals(rhs.paxInfo)))) && ((this.totalPriceInfo == rhs.totalPriceInfo) || ((this.totalPriceInfo != null) && this.totalPriceInfo.equals(rhs.totalPriceInfo)))) && ((this.supplierCommercials == rhs.supplierCommercials) || ((this.supplierCommercials != null) && this.supplierCommercials.equals(rhs.supplierCommercials)))) && ((this.checkOut == rhs.checkOut) || ((this.checkOut != null) && this.checkOut.equals(rhs.checkOut)))) && ((this.roomID == rhs.roomID) || ((this.roomID != null) && this.roomID.equals(rhs.roomID))));
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }
}
