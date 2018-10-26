package com.coxandkings.travel.operations.model.qcmanagement;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "qc_status_info")
public class QcStatusInfo {

    @Id
    private String bookId;
    private String qcStatus;
    @ElementCollection(targetClass=String.class)
    @MapKeyColumn(name="qcRemarks")
    private Map<String,String> remarks;
    private Integer percentage;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getQcStatus() {
        return qcStatus;
    }

    public void setQcStatus(String qcStatus) {
        this.qcStatus = qcStatus;
    }

    public Map<String, String> getRemarks() {
        return remarks;
    }

    public void setRemarks(Map<String, String> remarks) {
        this.remarks = remarks;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "QcStatusInfo{" +
                "bookId='" + bookId + '\'' +
                ", qcStatus='" + qcStatus + '\'' +
                ", remarks=" + remarks +
                ", percentage=" + percentage +
                '}';
    }
}


