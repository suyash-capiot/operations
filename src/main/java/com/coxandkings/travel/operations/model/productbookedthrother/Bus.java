package com.coxandkings.travel.operations.model.productbookedthrother;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "other_product_info_bus")
public class Bus extends  CommanAttribute
{

    private String serviceProvider;
    private String pickupPointName;
    private String dropoffPointName;

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getPickupPointName() {
        return pickupPointName;
    }

    public void setPickupPointName(String pickupPointName) {
        this.pickupPointName = pickupPointName;
    }

    public String getDropoffPointName() {
        return dropoffPointName;
    }

    public void setDropoffPointName(String dropoffPointName) {
        this.dropoffPointName = dropoffPointName;
    }
}