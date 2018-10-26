package com.coxandkings.travel.operations.resource.offlinecancellationsystem;

public class OfflineSupplierCredentials {
    private String url;
    private String userName;
    private String password;
    private String form_userName;
    private String form_password;

    public String getForm_userName() {
        return form_userName;
    }

    public void setForm_userName(String form_userName) {
        this.form_userName = form_userName;
    }

    public String getForm_password() {
        return form_password;
    }

    public void setForm_password(String form_password) {
        this.form_password = form_password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
