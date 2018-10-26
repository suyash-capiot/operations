package com.coxandkings.travel.operations.model.modifiedFileProfitabiliy;

import com.coxandkings.travel.operations.enums.FileProfitability.FileProfTypes;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "File_Profitability_details")
public class FileProfitabilityBooking {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String bookingReferenceNumber;
    private String clientType;
    private String clientName;
    private String clientId;
    private String productName;
    private String orderId;
    @Enumerated(EnumType.STRING)
    private FileProfTypes bookingType;
    private boolean isPassengerwise;
    private boolean isRoomwise;
    private boolean isTranspotation;
    private boolean isAccomodation;
    private ZonedDateTime bookingDateZDT;
    private String productCategory;
    private String productSubCategory;
    private String departureLocation;
    private String destinationLocation;
    private String leadPassName;
    @Embedded
    private PaxBreakDown paxBreakDown;
    @OneToOne(cascade = CascadeType.ALL)
    private BudgetedProfitability budgetedFileProf;
    @OneToOne(cascade = CascadeType.ALL)
    private OperationalProfitability operationalFileProf;
    @OneToOne(cascade = CascadeType.ALL)
    private FinalProfitability finaFileProf;
    @Embedded
    private Variance variance;

    //Added
    private String BU;
    private String SBU;
    private String companyId;
    private String companyName;
    private String companyGroupId;
    private String companyGroupName;
    private String groupOfCompanyId;
    private String groupOfCompanyName;
    private String branchName;


    public String getBU() {
        return BU;
    }

    public void setBU(String BU) {
        this.BU = BU;
    }

    public String getSBU() {
        return SBU;
    }

    public void setSBU(String SBU) {
        this.SBU = SBU;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyGroupId() {
        return companyGroupId;
    }

    public void setCompanyGroupId(String companyGroupId) {
        this.companyGroupId = companyGroupId;
    }

    public String getCompanyGroupName() {
        return companyGroupName;
    }

    public void setCompanyGroupName(String companyGroupName) {
        this.companyGroupName = companyGroupName;
    }

    public String getGroupOfCompanyId() {
        return groupOfCompanyId;
    }

    public void setGroupOfCompanyId(String groupOfCompanyId) {
        this.groupOfCompanyId = groupOfCompanyId;
    }

    public String getGroupOfCompanyName() {
        return groupOfCompanyName;
    }

    public void setGroupOfCompanyName(String groupOfCompanyName) {
        this.groupOfCompanyName = groupOfCompanyName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getLeadPassName() {
        return leadPassName;
    }

    public void setLeadPassName(String leadPassName) {
        this.leadPassName = leadPassName;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean isTranspotation() {
        return isTranspotation;
    }

    public void setTranspotation(boolean transpotation) {
        isTranspotation = transpotation;
    }

    public boolean isAccomodation() {
        return isAccomodation;
    }

    public void setAccomodation(boolean accomodation) {
        isAccomodation = accomodation;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public ZonedDateTime getBookingDateZDT() {
        return bookingDateZDT;
    }

    public void setBookingDateZDT(ZonedDateTime bookingDateZDT) {
        this.bookingDateZDT = bookingDateZDT;
    }

    public boolean isPassengerwise() {
        return isPassengerwise;
    }

    public void setPassengerwise(boolean passengerwise) {
        isPassengerwise = passengerwise;
    }

    public boolean isRoomwise() {
        return isRoomwise;
    }

    public void setRoomwise(boolean roomwise) {
        isRoomwise = roomwise;
    }

    public FileProfTypes getBookingType() {
        return bookingType;
    }

    public void setBookingType(FileProfTypes bookingType) {
        this.bookingType = bookingType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingReferenceNumber() {
        return bookingReferenceNumber;
    }

    public void setBookingReferenceNumber(String bookingReferenceNumber) {
        this.bookingReferenceNumber = bookingReferenceNumber;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public PaxBreakDown getPaxBreakDown() {
        return paxBreakDown;
    }

    public void setPaxBreakDown(PaxBreakDown paxBreakDown) {
        this.paxBreakDown = paxBreakDown;
    }

    public BudgetedProfitability getBudgetedFileProf() {
        return budgetedFileProf;
    }

    public void setBudgetedFileProf(BudgetedProfitability budgetedFileProf) {
        this.budgetedFileProf = budgetedFileProf;
    }

    public OperationalProfitability getOperationalFileProf() {
        return operationalFileProf;
    }

    public void setOperationalFileProf(OperationalProfitability operationalFileProf) {
        this.operationalFileProf = operationalFileProf;
    }

    public FinalProfitability getFinaFileProf() {
        return finaFileProf;
    }

    public void setFinaFileProf(FinalProfitability finaFileProf) {
        this.finaFileProf = finaFileProf;
    }

    public Variance getVariance() {
        return variance;
    }

    public void setVariance(Variance variance) {
        this.variance = variance;
    }
}
