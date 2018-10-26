package com.coxandkings.travel.operations.model.merge;

import com.coxandkings.travel.operations.enums.merge.MergeStatusValues;
import com.coxandkings.travel.operations.enums.merge.MergeTypeValues;
import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Merge extends BaseModel {

    @Column(name = "number_of_passengers")
    private Integer paxCount;

    @Column(name = "original_margin")
    private BigDecimal originalMargin;

    @Column(name = "new_margin")
    private BigDecimal newMargin;

    @Enumerated(EnumType.STRING)
    private MergeStatusValues mergeStatus;

    @Enumerated(EnumType.STRING)
    private MergeTypeValues mergeType;

    @Column(name = "is_within_cancellation")
    private Boolean withinCancellation;

    @Column(name = "merge_size")
    private Integer count;

    @Column(name = "supplier_id")
    private String supplierId;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<BookProduct> products;

    public Merge() {
        count = 0;
        paxCount = 0;
        originalMargin = BigDecimal.ZERO;
        newMargin = BigDecimal.ZERO;
        withinCancellation = false;
        products = new HashSet<>();
    }

    public Integer getPaxCount() {
        return paxCount;
    }

    public void setPaxCount(Integer paxCount) {
        this.paxCount = paxCount;
    }

    public BigDecimal getOriginalMargin() {
        return originalMargin;
    }

    public void setOriginalMargin(BigDecimal originalMargin) {
        this.originalMargin = originalMargin;
    }

    public BigDecimal getNewMargin() {
        return newMargin;
    }

    public void setNewMargin(BigDecimal newMargin) {
        this.newMargin = newMargin;
    }

    public MergeStatusValues getMergeStatus() {
        return mergeStatus;
    }

    public void setMergeStatus(MergeStatusValues mergeStatus) {
        this.mergeStatus = mergeStatus;
    }

    public int getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getWithinCancellation() {
        return withinCancellation;
    }

    public void setWithinCancellation(Boolean withinCancellation) {
        this.withinCancellation = withinCancellation;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public MergeTypeValues getMergeType() {
        return mergeType;
    }

    public void setMergeType(MergeTypeValues mergeType) {
        this.mergeType = mergeType;
    }

    public Set<BookProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<BookProduct> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Merge{" +
                "id =" + getId() +
                "paxCount =" + paxCount +
                ", mergeStatus =" + mergeStatus +
                ", mergeType =" + mergeType +
                ", count =" + count +
                ", supplierId ='" + supplierId + '\'' +
                '}';
    }


}
