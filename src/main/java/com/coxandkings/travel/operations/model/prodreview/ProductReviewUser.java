package com.coxandkings.travel.operations.model.prodreview;


import com.coxandkings.travel.operations.enums.user.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "product_review_user")
public class ProductReviewUser {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "comment")
    private String comment;

    @Column(name = "complaint_created_flag")
    private boolean complaintCreated = false;

 /*   @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "assign_to_id")
    private AssignTo assignTo;*/

    @Column(name = "user_type_enum")
    @Enumerated(EnumType.ORDINAL)
    private UserType userType;

    @Column
    private Boolean done = false;

    @ManyToOne
    @JsonIgnore
    //@JoinColumns({@JoinColumn(name = "product_review_user_id"), @JoinColumn(name = "product_review_user_version_number")})
    private ProductReview productReview;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isComplaintCreated() {
        return complaintCreated;
    }

    public void setComplaintCreated(boolean complaintCreated) {
        this.complaintCreated = complaintCreated;
    }

 /*   public AssignTo getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(AssignTo assignTo) {
        this.assignTo = assignTo;
    }*/

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public ProductReview getProductReview() {
        return productReview;
    }

    public void setProductReview(ProductReview productReview) {
        this.productReview = productReview;
    }


}
