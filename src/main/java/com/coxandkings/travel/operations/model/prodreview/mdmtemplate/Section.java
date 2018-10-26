package com.coxandkings.travel.operations.model.prodreview.mdmtemplate;

import javax.validation.constraints.NotNull;
import java.util.List;


public class Section {

    private String sectionId;

    @NotNull
    private String sectionName;

    private Integer position;

    private List<SubSection> subSections;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public List<SubSection> getSubSections() {
        return subSections;
    }

    public void setSubSections(List<SubSection> subSections) {
        this.subSections = subSections;
    }
}
