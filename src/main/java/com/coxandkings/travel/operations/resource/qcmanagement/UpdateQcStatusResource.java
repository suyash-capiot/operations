package com.coxandkings.travel.operations.resource.qcmanagement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "bookID",
        "QCStatus",
        "userID"
})
public class UpdateQcStatusResource {
    @JsonProperty("bookID")
    private String bookID;
    @JsonProperty("QCStatus")
    private String qCStatus;
    @JsonProperty("userID")
    private String userID;

    @JsonProperty("bookID")
    public String getBookID() {
        return bookID;
    }

    @JsonProperty("bookID")
    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    @JsonProperty("QCStatus")
    public String getQCStatus() {
        return qCStatus;
    }

    @JsonProperty("QCStatus")
    public void setQCStatus(String qCStatus) {
        this.qCStatus = qCStatus;
    }

    @JsonProperty("userID")
    public String getUserID() {
        return userID;
    }

    @JsonProperty("userID")
    public void setUserID(String userID) {
        this.userID = userID;
    }
}