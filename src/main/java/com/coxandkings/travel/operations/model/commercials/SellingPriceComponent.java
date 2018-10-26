package com.coxandkings.travel.operations.model.commercials;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "SELLING_PRICE_COMPONENT")
public class SellingPriceComponent implements Serializable {
    //TODO : Speak to MDM team to Add more Supplier price components in DB

    @Id
    @Column(name = "selling_price_component")
    private String sellingPriceComponent;

    public SellingPriceComponent() {
    }

    public SellingPriceComponent(String sellingPriceComponent) {
        this.sellingPriceComponent = sellingPriceComponent;
    }

    public String getSellingPriceComponent() {
        return sellingPriceComponent;
    }

    public void setSellingPriceComponent(String sellingPriceComponent) {
        this.sellingPriceComponent = sellingPriceComponent;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sellingPriceComponent == null) ? 0 : sellingPriceComponent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SellingPriceComponent other = (SellingPriceComponent) obj;
		if (sellingPriceComponent == null) {
			if (other.sellingPriceComponent != null)
				return false;
		} else if (!sellingPriceComponent.equals(other.sellingPriceComponent))
			return false;
		return true;
	}
    
}
