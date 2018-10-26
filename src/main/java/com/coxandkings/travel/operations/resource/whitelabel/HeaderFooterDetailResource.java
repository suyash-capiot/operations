package com.coxandkings.travel.operations.resource.whitelabel;

import com.coxandkings.travel.operations.resource.BaseResource;

public class HeaderFooterDetailResource extends BaseResource {

    private String elementId;
    private String controlTypeId;
    private String content;
    private String filePath;
    private String whiteLabelId;

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getControlTypeId() {
        return controlTypeId;
    }

    public void setControlTypeId(String controlTypeId) {
        this.controlTypeId = controlTypeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getWhiteLabelId() {
        return whiteLabelId;
    }

    public void setWhiteLabelId(String whiteLabelId) {
        this.whiteLabelId = whiteLabelId;
    }
}
