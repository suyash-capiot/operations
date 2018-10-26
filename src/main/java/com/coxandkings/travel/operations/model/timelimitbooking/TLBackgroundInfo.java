package com.coxandkings.travel.operations.model.timelimitbooking;

import com.coxandkings.travel.operations.enums.timelimit.ApprovalStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "client_info_details")
public class TLBackgroundInfo implements Serializable{

    @Id
    @Column(name="bookId")
    private String bookId;
    @Column(name = "clientId")
    private String clientId;
    @Column(name = "total_Revenue_For_Period")
    private String totalRevenueForPeriod;
    @Column(name = "total_Gross_Profit")
    private String totalGrossProfit;
    @Column(name = "total_Outstanding_WithAgeing")
    private String totalOutstandingWithAgeing;
    @Column(name = "number_Of_Bookings_For_Period")
    private Integer numberOfBookingsForPeriod;
    @Column(name="approval_status")
    private ApprovalStatus status;

    public TLBackgroundInfo() {
    }

    public TLBackgroundInfo(String clientId, String totalRevenueForPeriod, String totalGrossProfit, String totalOutstandingWithAgeing, Integer numberOfBookingsForPeriod) {
        this.clientId = clientId;
        this.totalRevenueForPeriod = totalRevenueForPeriod;
        this.totalGrossProfit = totalGrossProfit;
        this.totalOutstandingWithAgeing = totalOutstandingWithAgeing;
        this.numberOfBookingsForPeriod = numberOfBookingsForPeriod;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTotalRevenueForPeriod() {
        return totalRevenueForPeriod;
    }

    public void setTotalRevenueForPeriod(String totalRevenueForPeriod) {
        this.totalRevenueForPeriod = totalRevenueForPeriod;
    }

    public String getTotalGrossProfit() {
        return totalGrossProfit;
    }

    public void setTotalGrossProfit(String totalGrossProfit) {
        this.totalGrossProfit = totalGrossProfit;
    }

    public String getTotalOutstandingWithAgeing() {
        return totalOutstandingWithAgeing;
    }

    public void setTotalOutstandingWithAgeing(String totalOutstandingWithAgeing) {
        this.totalOutstandingWithAgeing = totalOutstandingWithAgeing;
    }

    public Integer getNumberOfBookingsForPeriod() {
        return numberOfBookingsForPeriod;
    }

    public void setNumberOfBookingsForPeriod(Integer numberOfBookingsForPeriod) {
        this.numberOfBookingsForPeriod = numberOfBookingsForPeriod;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }
}
