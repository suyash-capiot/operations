package com.coxandkings.travel.operations.resource.whitelabel;

import com.coxandkings.travel.operations.resource.BaseResource;

public class ClientLoginParameterResource extends BaseResource {

    private String loginParameter;
    private String whiteLabelId;


    public String getLoginParameter() {
        return loginParameter;
    }

    public void setLoginParameter(String loginParameter) {
        this.loginParameter = loginParameter;
    }


    public String getWhiteLabelId() {
        return whiteLabelId;
    }

    public void setWhiteLabelId(String whiteLabelId) {
        this.whiteLabelId = whiteLabelId;
    }
}
