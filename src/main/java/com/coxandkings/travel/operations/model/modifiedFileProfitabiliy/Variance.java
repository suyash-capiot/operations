package com.coxandkings.travel.operations.model.modifiedFileProfitabiliy;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Variance {

    private BigDecimal budgetedVsOperational;
    private BigDecimal budgetedVsFinal;
    private BigDecimal operationalVsFinal;

    public BigDecimal getBudgetedVsOperational() {
        return budgetedVsOperational;
    }

    public void setBudgetedVsOperational(BigDecimal budgetedVsOperational) {
        this.budgetedVsOperational = budgetedVsOperational;
    }

    public BigDecimal getBudgetedVsFinal() {
        return budgetedVsFinal;
    }

    public void setBudgetedVsFinal(BigDecimal budgetedVsFinal) {
        this.budgetedVsFinal = budgetedVsFinal;
    }

    public BigDecimal getOperationalVsFinal() {
        return operationalVsFinal;
    }

    public void setOperationalVsFinal(BigDecimal operationalVsFinal) {
        this.operationalVsFinal = operationalVsFinal;
    }
}
