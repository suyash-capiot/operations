package com.coxandkings.travel.operations.model.supplierbillpassing;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "supplier_invoice_OCR")
public class SupplierInvoiceOCR extends BaseModel {

    private String invoiceNumber;
    private String supplierId;

    @Column(name = "data", columnDefinition = "text", length = 10485760)
    private String data;

    private ZonedDateTime creationDate;

    private Boolean used;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
