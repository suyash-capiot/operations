package com.coxandkings.travel.operations.model.modifiedFileProfitabiliy;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "file_protitability_type")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class FileProfitabilityType implements Serializable {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    // private String type;
    @OneToOne(cascade = CascadeType.ALL)
    private ProfSellingPrice profSellingPrice;
    @OneToOne(cascade = CascadeType.ALL)
    private ProfSupplierCostPrice profSupplierCostPrice;
    @Embedded
    private ProfMargin profMargin;


    public ProfSellingPrice getProfSellingPrice() {
        return profSellingPrice;
    }

    public void setProfSellingPrice(ProfSellingPrice profSellingPrice) {
        this.profSellingPrice = profSellingPrice;
    }

    public ProfSupplierCostPrice getProfSupplierCostPrice() {
        return profSupplierCostPrice;
    }

    public void setProfSupplierCostPrice(ProfSupplierCostPrice profSupplierCostPrice) {
        this.profSupplierCostPrice = profSupplierCostPrice;
    }

    public ProfMargin getProfMargin() {
        return profMargin;
    }

    public void setProfMargin(ProfMargin profMargin) {
        this.profMargin = profMargin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
