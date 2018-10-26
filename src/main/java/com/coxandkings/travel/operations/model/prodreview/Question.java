package com.coxandkings.travel.operations.model.prodreview;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {
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
    private String labelName;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SubQuestion> subQuestions;


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

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public List<SubQuestion> getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(List<SubQuestion> subQuestions) {
        this.subQuestions = subQuestions;
    }

    public Question(String id, String questionName, String labelName, List<SubQuestion> subQuestions) {
        this.id = id;
        this.questionName = questionName;
        this.labelName = labelName;
        this.subQuestions = subQuestions;
    }

    public Question() {
    }
}
