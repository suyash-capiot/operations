package com.coxandkings.travel.operations.model.prodreview.mdmtemplate;

import javax.validation.constraints.NotNull;

public class Columns {


    private String columnsId;

    @NotNull
    private String columnName;

    private String dataType;

    private Integer characterDataSize;

    private Integer positionInSection;

    private String labelName;

    private Boolean displayOnScreen;

    private String controlType;

    private Boolean isMandatory;

    private String dropdownSource;

    private Validation validation;

    public String getColumnsId() {
        return columnsId;
    }

    public void setColumnsId(String columnsId) {
        this.columnsId = columnsId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getCharacterDataSize() {
        return characterDataSize;
    }

    public void setCharacterDataSize(Integer characterDataSize) {
        this.characterDataSize = characterDataSize;
    }

    public Integer getPositionInSection() {
        return positionInSection;
    }

    public void setPositionInSection(Integer positionInSection) {
        this.positionInSection = positionInSection;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public Boolean getDisplayOnScreen() {
        return displayOnScreen;
    }

    public void setDisplayOnScreen(Boolean displayOnScreen) {
        this.displayOnScreen = displayOnScreen;
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean mandatory) {
        isMandatory = mandatory;
    }

    public String getDropdownSource() {
        return dropdownSource;
    }

    public void setDropdownSource(String dropdownSource) {
        this.dropdownSource = dropdownSource;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }
}
