
package com.coxandkings.travel.operations.model.commercialstatements;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "client_commercial_statement")
public class ClientCommercialStatement extends BaseCommercialDetails {

    private String clientType;
    private String clientCategory;
    private String clientSubCategory;

//    @ManyToMany
//    private Set<ClientPaymentAdvice> clientPaymentAdviceSet;

    @ManyToOne
    private CommercialStatementsBillPassing commercialStatementsBillPassing;

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(String clientCategory) {
        this.clientCategory = clientCategory;
    }

    public String getClientSubCategory() {
        return clientSubCategory;
    }

    public void setClientSubCategory(String clientSubCategory) {
        this.clientSubCategory = clientSubCategory;
    }

    public CommercialStatementsBillPassing getCommercialStatementsBillPassing() {
        return commercialStatementsBillPassing;
    }

    public void setCommercialStatementsBillPassing(CommercialStatementsBillPassing commercialStatementsBillPassing) {
        this.commercialStatementsBillPassing = commercialStatementsBillPassing;
    }

//    public Set<ClientPaymentAdvice> getClientPaymentAdviceSet() {
//        return clientPaymentAdviceSet;
//    }
//
//    public void setClientPaymentAdviceSet(Set<ClientPaymentAdvice> clientPaymentAdviceSet) {
//        this.clientPaymentAdviceSet = clientPaymentAdviceSet;
//    }
}

