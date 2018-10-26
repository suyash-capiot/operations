package com.coxandkings.travel.operations.resource.whitelabel;

import com.coxandkings.travel.operations.resource.BaseResource;

public class ColorSchemeResource extends BaseResource {

    private String colorId;

    private String cssElementId;

    private String whiteLabelId;

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getCssElementId() {
        return cssElementId;
    }

    public void setCssElementId(String cssElementId) {
        this.cssElementId = cssElementId;
    }

    public String getWhiteLabelId() {
        return whiteLabelId;
    }

    public void setWhiteLabelId(String whiteLabelId) {
        this.whiteLabelId = whiteLabelId;
    }
}
