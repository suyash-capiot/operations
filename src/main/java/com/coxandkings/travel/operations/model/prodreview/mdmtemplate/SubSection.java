package com.coxandkings.travel.operations.model.prodreview.mdmtemplate;

import javax.validation.constraints.NotNull;
import java.util.List;


public class SubSection {


    private String subSectionId;

    @NotNull
    private String subSectionName;

    private List<Columns> columns;

    public String getSubSectionName() {
        return subSectionName;
    }

    public void setSubSectionName(String subSectionName) {
        this.subSectionName = subSectionName;
    }

    public String getSubSectionId() {
        return subSectionId;
    }

    public void setSubSectionId(String subSectionId) {
        this.subSectionId = subSectionId;
    }

    public List<Columns> getColumns() {
        return columns;
    }

    public void setColumns(List<Columns> columns) {
        this.columns = columns;
    }
}
