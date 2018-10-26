package com.coxandkings.travel.ext.model.finance.cancelinvoice;

import java.util.ArrayList;
import java.util.List;

public class EmailRequestEntity {
    List<String> invoiceNumber = new ArrayList<>();

    public List<String> getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(List<String> invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
