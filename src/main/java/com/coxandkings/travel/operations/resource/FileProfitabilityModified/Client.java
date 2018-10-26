package com.coxandkings.travel.operations.resource.FileProfitabilityModified;

public class Client {
    private String _id;
    private ClientProfile clientProfile;
    //private ClientDetails clientDetails;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ClientProfile getClientProfile() {
        return clientProfile;
    }

    public void setClientProfile(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
    }

    @Override
    public String toString() {
        return "Client{" +
                "_id='" + _id + '\'' +
                ", clientProfile=" + clientProfile +
                '}';
    }
}
