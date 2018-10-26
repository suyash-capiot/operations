
package com.coxandkings.travel.operations.model.commercialstatements;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "supplier_commercial_statement")
public class SupplierCommercialStatement extends BaseCommercialDetails {

    @ManyToOne
    private CommercialStatementsBillPassing commercialStatementsBillPassing;

    public CommercialStatementsBillPassing getCommercialStatementsBillPassing() {
        return commercialStatementsBillPassing;
    }

    public void setCommercialStatementsBillPassing(CommercialStatementsBillPassing commercialStatementsBillPassing) {
        this.commercialStatementsBillPassing = commercialStatementsBillPassing;
    }
}

