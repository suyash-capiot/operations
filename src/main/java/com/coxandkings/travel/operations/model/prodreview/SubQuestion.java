package com.coxandkings.travel.operations.model.prodreview;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subQuestion")
public class SubQuestion {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;
    @Column
    private String questionName;

    @Column
    private String answer;

    @Column
    private String buttonType;

    @ElementCollection
    private List<String> possibleValues;


    public SubQuestion(String id, String questionName, String answer, String buttonType, List<String> possibleValues) {
        this.id = id;
        this.questionName = questionName;
        this.answer = answer;
        this.buttonType = buttonType;
        this.possibleValues = possibleValues;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public List<String> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(List<String> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public SubQuestion() {
    }
}
