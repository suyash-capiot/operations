package com.coxandkings.travel.operations.model.prodreview;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "review_form_submitted")
public class ReviewFormSubmitted {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "booking_reference_number")
    private String bookingReferenceNumber;

    @Column(name = "not_booked_with_us")
    private boolean notBookedWithUs;

    @OneToOne(cascade = CascadeType.ALL)
    private RatingAnswer overallRating;// total rating

    @Column(name = "title_of_your_review")
    private String titleOfYourReview;

    @Column(name = "your_review")
    private String yourReview; //comment

    @Column(name = "aggregate_rating")
    private double aggregate;
/*
    @OneToMany(cascade = CascadeType.ALL)
    private List<Question> questions;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SubQuestion> otherQuestions;*/

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private QuestionRating ratingAnswer;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ProductRelatedQuestionAnswer> productRelatedQuestionAnswerList;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private StyleQuestion styleQuestion;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private ProductReview productreview;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingReferenceNumber() {
        return bookingReferenceNumber;
    }

    public void setBookingReferenceNumber(String bookingReferenceNumber) {
        this.bookingReferenceNumber = bookingReferenceNumber;
    }

    public boolean isNotBookedWithUs() {
        return notBookedWithUs;
    }

    public void setNotBookedWithUs(boolean notBookedWithUs) {
        this.notBookedWithUs = notBookedWithUs;
    }

    public RatingAnswer getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(RatingAnswer overallRating) {
        this.overallRating = overallRating;
    }

    public String getTitleOfYourReview() {
        return titleOfYourReview;
    }

    public void setTitleOfYourReview(String titleOfYourReview) {
        this.titleOfYourReview = titleOfYourReview;
    }

    public String getYourReview() {
        return yourReview;
    }

    public void setYourReview(String yourReview) {
        this.yourReview = yourReview;
    }

    public double getAggregate() {
        return aggregate;
    }

    public void setAggregate(double aggregate) {
        this.aggregate = aggregate;
    }

    public QuestionRating getRatingAnswer() {
        return ratingAnswer;
    }

    public void setRatingAnswer(QuestionRating ratingAnswer) {
        this.ratingAnswer = ratingAnswer;
    }

    public List<ProductRelatedQuestionAnswer> getProductRelatedQuestionAnswerList() {
        return productRelatedQuestionAnswerList;
    }

    public void setProductRelatedQuestionAnswerList(List<ProductRelatedQuestionAnswer> productRelatedQuestionAnswerList) {
        this.productRelatedQuestionAnswerList = productRelatedQuestionAnswerList;
    }

    public StyleQuestion getStyleQuestion() {
        return styleQuestion;
    }

    public void setStyleQuestion(StyleQuestion styleQuestion) {
        this.styleQuestion = styleQuestion;
    }

    public ProductReview getProductreview() {
        return productreview;
    }

    public void setProductreview(ProductReview productreview) {
        this.productreview = productreview;
    }

    public ReviewFormSubmitted(String id, String bookingReferenceNumber, boolean notBookedWithUs, RatingAnswer overallRating, String titleOfYourReview, String yourReview, double aggregate, QuestionRating ratingAnswer, List<ProductRelatedQuestionAnswer> productRelatedQuestionAnswerList, StyleQuestion styleQuestion, ProductReview productreview) {
        this.id = id;
        this.bookingReferenceNumber = bookingReferenceNumber;
        this.notBookedWithUs = notBookedWithUs;
        this.overallRating = overallRating;
        this.titleOfYourReview = titleOfYourReview;
        this.yourReview = yourReview;
        this.aggregate = aggregate;
        this.ratingAnswer = ratingAnswer;
        this.productRelatedQuestionAnswerList = productRelatedQuestionAnswerList;
        this.styleQuestion = styleQuestion;
        this.productreview = productreview;
    }

    public ReviewFormSubmitted() {
    }
}
