package com.coxandkings.travel.operations.resource.prodreview;

import com.coxandkings.travel.operations.enums.prodreview.ClientType;

import java.util.List;

public class MailTemplateResource {

    private String bookID;
    private String clientId;
    private ClientType clientType;

    private List<String> urls;

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }
}
