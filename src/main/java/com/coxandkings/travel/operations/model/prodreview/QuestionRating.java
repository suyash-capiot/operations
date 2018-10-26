package com.coxandkings.travel.operations.model.prodreview;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "question_rating")
public class QuestionRating {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column
    private String question;

    @Column
    private String labelName;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<RatingAnswer> subQuestions;

    public QuestionRating() {
        this.id = id;
        this.question = question;
        this.labelName = labelName;
        this.subQuestions = subQuestions;
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

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public List<RatingAnswer> getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(List<RatingAnswer> subQuestions) {
        this.subQuestions = subQuestions;
    }
}
