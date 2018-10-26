package com.coxandkings.travel.operations.model.prodreview;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity(name = "question_average_count")
public class QuestionAvgCount {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column
    private String question;
    @Column
    private Double average = 0.0;
    @Column
    private Long count = 0L;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "question_average_count_id")
    private ProductReviewAverage productReviewAverage;


    public QuestionAvgCount() {
        this.id = id;
        this.question = question;
        this.average = average;
        this.count = count;
        this.productReviewAverage = productReviewAverage;
    }

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

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public ProductReviewAverage getProductReviewAverage() {
        return productReviewAverage;
    }

    public void setProductReviewAverage(ProductReviewAverage productReviewAverage) {
        this.productReviewAverage = productReviewAverage;
    }
}
