package com.coxandkings.travel.operations.resource.whitelabel;

import com.coxandkings.travel.operations.resource.BaseResource;

public class CustomWhiteLabelResource extends BaseResource{

    private String customText;
    private String whiteLabelId;

    public String getCustomText() {
        return customText;
    }

    public void setCustomText(String customText) {
        this.customText = customText;
    }

    public String getWhiteLabelId() {
        return whiteLabelId;
    }

    public void setWhiteLabelId(String whiteLabelId) {
        this.whiteLabelId = whiteLabelId;
    }
}
