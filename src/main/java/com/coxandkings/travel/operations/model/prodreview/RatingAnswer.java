package com.coxandkings.travel.operations.model.prodreview;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "product_review_rating_answer")
public class RatingAnswer {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "question_id")
    private String question;

    @Column
    private String answer;

    @ElementCollection
    private List<String> possibleValues;

    @Column
    private String buttonType;



    /*@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="productreview.reviewId")
    @JsonIgnore
    private ProductReview productreview;*/

    public RatingAnswer(String id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public RatingAnswer() {
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(List<String> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }
}
