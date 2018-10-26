package com.coxandkings.travel.operations.model.mailroomanddispatch;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="dispatch_bar_code_")
public class BarCode {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="bar_code_id")
    private String id;
    private String consigneeName;
    private String consigneeLocation;
    private String companyName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeLocation() {
        return consigneeLocation;
    }

    public void setConsigneeLocation(String consigneeLocation) {
        this.consigneeLocation = consigneeLocation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "BarCode{" +
                "id='" + id + '\'' +
                ", consigneeName='" + consigneeName + '\'' +
                ", consigneeLocation='" + consigneeLocation + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
