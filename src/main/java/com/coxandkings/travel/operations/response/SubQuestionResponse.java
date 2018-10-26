package com.coxandkings.travel.operations.response;

import java.util.List;

public class SubQuestionResponse {

    private String labelName;
    private List<SubQuestionDetails> subQuestionDetails;

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public List<SubQuestionDetails> getSubQuestionDetails() {
        return subQuestionDetails;
    }

    public void setSubQuestionDetails(List<SubQuestionDetails> subQuestionDetails) {
        this.subQuestionDetails = subQuestionDetails;
    }
}
