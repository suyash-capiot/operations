package com.coxandkings.travel.operations.model.prodreview;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "product_review_product_style_question_answer")
public class ProductStyleQuestionAnswer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;
    @Column(name = "question_id")
    private String questionId;

    @Column
    private String answer;

    @ElementCollection
    private List<String> possibleValues;

    @Column
    private String buttonType;

/*    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="productReview.reviewId")
    @JsonIgnore
    private ProductReview productReview;*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
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
