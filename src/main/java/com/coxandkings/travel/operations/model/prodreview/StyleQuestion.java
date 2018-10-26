package com.coxandkings.travel.operations.model.prodreview;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "style_question")
public class StyleQuestion {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column
    private String label;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ProductStyleQuestionAnswer> productStyleQuestionAnswerList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ProductStyleQuestionAnswer> getProductStyleQuestionAnswerList() {
        return productStyleQuestionAnswerList;
    }

    public void setProductStyleQuestionAnswerList(List<ProductStyleQuestionAnswer> productStyleQuestionAnswerList) {
        this.productStyleQuestionAnswerList = productStyleQuestionAnswerList;
    }
}
