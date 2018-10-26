package com.coxandkings.travel.operations.resource.whitelabel;


import com.coxandkings.travel.operations.resource.BaseResource;

public class MarketingSetupParameterResource extends BaseResource {

    private String marketingSetUpName;

    private String content;

    private String filePath;

    private String whiteLabelId;

    public String getMarketingSetUpName() {
        return marketingSetUpName;
    }

    public void setMarketingSetUpName(String marketingSetUpName) {
        this.marketingSetUpName = marketingSetUpName;
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
