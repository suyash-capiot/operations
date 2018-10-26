package com.coxandkings.travel.operations.model.driver;

import com.coxandkings.travel.operations.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "VEHICLE_CATEGORY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VehicleCategory extends BaseModel {

    @Column
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
