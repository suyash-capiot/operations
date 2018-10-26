package com.coxandkings.travel.operations.resource.prodreview;

import com.coxandkings.travel.operations.enums.prodreview.ClientType;

import java.util.List;

public class ClientMailResource {

    private String bookID;
    private String clientId;
    private ClientType clientType;
    //templateIds
    private List<String> ids;


    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
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

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
