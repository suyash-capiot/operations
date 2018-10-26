package com.coxandkings.travel.operations.model.newsupplierfirstbooking;

import com.coxandkings.travel.operations.enums.newsupplierfirstbooking.NewSupplierCommunicationStatus;

import javax.persistence.*;

@Entity
@Table(name = "new_supplier_communication")
public class NewSupplierCommunication {

    @Id
    @Column(name = "bookingID")
    private String bookId;
    @Enumerated(EnumType.STRING)
    private NewSupplierCommunicationStatus status;

    public NewSupplierCommunication() {
    }

    public NewSupplierCommunication(String bookId, NewSupplierCommunicationStatus status) {
        this.bookId = bookId;
        this.status = status;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public NewSupplierCommunicationStatus getStatus() {
        return status;
    }

    public void setStatus(NewSupplierCommunicationStatus status) {
        this.status = status;
    }
}
