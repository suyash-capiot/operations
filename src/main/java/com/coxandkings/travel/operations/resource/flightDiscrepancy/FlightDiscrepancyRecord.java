package com.coxandkings.travel.operations.resource.flightDiscrepancy;

import com.coxandkings.travel.operations.enums.flightDiscrepancy.DiscrepancyType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FlightDiscrepancyRecord {

    private String id;
    private BSPReportTransactionDTO reportTransactionDTO;
    private TravelERPTransactionDTO travelErpTransactionDTO;
    private DiscrepancyType type;
    private String bspReferenceNumber;
    private String discrepancyStatus;
    private String opsRemark;
    private String bspDisputeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BSPReportTransactionDTO getReportTransactionDTO() {
        return reportTransactionDTO;
    }

    public void setReportTransactionDTO(BSPReportTransactionDTO reportTransactionDTO) {
        this.reportTransactionDTO = reportTransactionDTO;
    }

    public TravelERPTransactionDTO getTravelErpTransactionDTO() {
        return travelErpTransactionDTO;
    }

    public void setTravelErpTransactionDTO(TravelERPTransactionDTO travelErpTransactionDTO) {
        this.travelErpTransactionDTO = travelErpTransactionDTO;
    }

    public DiscrepancyType getType() {
        return type;
    }

    public void setType(DiscrepancyType type) {
        this.type = type;
    }

    public String getBspReferenceNumber() {
        return bspReferenceNumber;
    }

    public void setBspReferenceNumber(String bspReferenceNumber) {
        this.bspReferenceNumber = bspReferenceNumber;
    }

    public String getDiscrepancyStatus() {
        return discrepancyStatus;
    }

    public void setDiscrepancyStatus(String discrepancyStatus) {
        this.discrepancyStatus = discrepancyStatus;
    }

    public String getOpsRemark() {
        return opsRemark;
    }

    public void setOpsRemark(String opsRemark) {
        this.opsRemark = opsRemark;
    }

    public String getBspDisputeId() {
        return bspDisputeId;
    }

    public void setBspDisputeId(String bspDisputeId) {
        this.bspDisputeId = bspDisputeId;
    }

}
