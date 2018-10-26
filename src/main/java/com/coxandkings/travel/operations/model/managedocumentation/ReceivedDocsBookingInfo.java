package com.coxandkings.travel.operations.model.managedocumentation;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
public class ReceivedDocsBookingInfo {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String bookId;
    private String orderId;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> documentSettingIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Set<String> getDocumentSettingIds() {
        return documentSettingIds;
    }

    public void setDocumentSettingIds(Set<String> documentSettingIds) {
        this.documentSettingIds = documentSettingIds;
    }
}
