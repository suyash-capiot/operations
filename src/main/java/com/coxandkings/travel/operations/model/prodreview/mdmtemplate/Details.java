package com.coxandkings.travel.operations.model.prodreview.mdmtemplate;

import java.util.List;


public class Details {

    private String detailsId;

    private List<Section> sections;

    public String getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(String detailsId) {
        this.detailsId = detailsId;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
