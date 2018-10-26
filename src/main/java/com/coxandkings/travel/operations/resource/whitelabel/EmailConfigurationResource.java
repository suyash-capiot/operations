package com.coxandkings.travel.operations.resource.whitelabel;

import com.coxandkings.travel.operations.resource.BaseResource;

public class EmailConfigurationResource extends BaseResource {

    private String functionId;

    private String emailFrom;

    private String emailToCC;

    private String emailToBCC;

    //TODO: Figure out the source of server settings id
    private String serverSettingId;

    private String whiteLabelId;


    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailToCC() {
        return emailToCC;
    }

    public void setEmailToCC(String emailToCC) {
        this.emailToCC = emailToCC;
    }

    public String getEmailToBCC() {
        return emailToBCC;
    }

    public void setEmailToBCC(String emailToBCC) {
        this.emailToBCC = emailToBCC;
    }

    public String getServerSettingId() {
        return serverSettingId;
    }

    public void setServerSettingId(String serverSettingId) {
        this.serverSettingId = serverSettingId;
    }

    public String getWhiteLabelId() {
        return whiteLabelId;
    }

    public void setWhiteLabelId(String whiteLabelId) {
        this.whiteLabelId = whiteLabelId;
    }
}
