package com.coxandkings.travel.operations.response;

public class QuestionResponse {


    private String question;
    private SubQuestionResponse subQuestionResponse;


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public SubQuestionResponse getSubQuestionResponse() {
        return subQuestionResponse;
    }

    public void setSubQuestionResponse(SubQuestionResponse subQuestionResponse) {
        this.subQuestionResponse = subQuestionResponse;
    }
}
