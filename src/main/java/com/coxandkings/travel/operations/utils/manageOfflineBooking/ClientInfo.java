package com.coxandkings.travel.operations.utils.manageOfflineBooking;


import org.json.JSONObject;

public class ClientInfo {

    public enum CommercialsEntityType{
        ClientGroup(Constants.MDM_VAL_CLIENTTYPE_GROUP), ClientSpecific(Constants.MDM_VAL_CLIENTTYPE_CLIENT), ClientType(
                Constants.MDM_VAL_CLIENTTYPE_ENTITY);

        private String mCommEntityType;

        private CommercialsEntityType(String type) {
            mCommEntityType = type;
        }

        public String toString() {
            return mCommEntityType;
        }
    }

    private String mClientId, mClientMarket, mParentId, mCommEntityId, mCommEntityMarket;
    private CommercialsEntityType mCommEntityType;

    ClientInfo(JSONObject clientInfoJson) {
        mClientId = clientInfoJson.getString(Constants.JSON_PROP_CLIENTID);
        mClientMarket = clientInfoJson.getString(Constants.JSON_PROP_CLIENTMARKET);
        mParentId = clientInfoJson.optString(Constants.JSON_PROP_PARENTCLIENTID);
        mCommEntityType = CommercialsEntityType.valueOf(clientInfoJson.optString(Constants.JSON_PROP_COMMENTITYTYPE));
        mCommEntityId = clientInfoJson.optString(Constants.JSON_PROP_COMMENTITYID);
        mCommEntityMarket = clientInfoJson.optString(Constants.JSON_PROP_COMMENTITYMARKET);
    }

    ClientInfo() {
    }

    public String getmClientId() {
        return mClientId;
    }

    public void setmClientId(String mClientId) {
        this.mClientId = mClientId;
    }

    public String getmClientMarket() {
        return mClientMarket;
    }

    public void setmClientMarket(String mClientMarket) {
        this.mClientMarket = mClientMarket;
    }

    public String getmParentId() {
        return mParentId;
    }

    public void setmParentId(String mParentId) {
        this.mParentId = mParentId;
    }

    public String getmCommEntityId() {
        return mCommEntityId;
    }

    public void setmCommEntityId(String mCommEntityId) {
        this.mCommEntityId = mCommEntityId;
    }

    public String getmCommEntityMarket() {
        return mCommEntityMarket;
    }

    public void setmCommEntityMarket(String mCommEntityMarket) {
        this.mCommEntityMarket = mCommEntityMarket;
    }

    public CommercialsEntityType getmCommEntityType() {
        return mCommEntityType;
    }

    public void setmCommEntityType(CommercialsEntityType mCommEntityType) {
        this.mCommEntityType = mCommEntityType;
    }

    public boolean hasParent() {
        return (mParentId != null && mParentId.trim().isEmpty() == false);
    }

    public JSONObject toJSON() {
        JSONObject clientInfoJson = new JSONObject();

        clientInfoJson.put(Constants.JSON_PROP_CLIENTID, mClientId);
        clientInfoJson.put(Constants.JSON_PROP_CLIENTMARKET, mClientMarket);
        clientInfoJson.put(Constants.JSON_PROP_PARENTCLIENTID, mParentId);
        clientInfoJson.put(Constants.JSON_PROP_COMMENTITYTYPE, mCommEntityType);
        clientInfoJson.put(Constants.JSON_PROP_COMMENTITYID, mCommEntityId);
        clientInfoJson.put(Constants.JSON_PROP_COMMENTITYMARKET, mCommEntityMarket);

        return clientInfoJson;
    }
}
