package com.coxandkings.travel.operations.model.commercials;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name =  "HOLIDAY_APPLY_COMMERCIAL_ON")
public class ApplyCommercialOn  implements Serializable{

    @Id
    @Column(name = "apply_commercial_on")
    private String applyCommercialOn;

    public ApplyCommercialOn(){

    }

    public ApplyCommercialOn(String applyCommercialOn) {
        this.applyCommercialOn = applyCommercialOn;
    }

    public String getApplyCommercialOn() {
        return applyCommercialOn;
    }

    public void setApplyCommercialOn(String applyCommercialOn) {
        this.applyCommercialOn = applyCommercialOn;
    }
}

