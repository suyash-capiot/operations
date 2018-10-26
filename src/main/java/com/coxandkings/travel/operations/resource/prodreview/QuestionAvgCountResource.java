package com.coxandkings.travel.operations.resource.prodreview;

import java.math.BigInteger;

public class QuestionAvgCountResource {

    private String id;
    private String question;
    private String average;
    private BigInteger count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }
}
