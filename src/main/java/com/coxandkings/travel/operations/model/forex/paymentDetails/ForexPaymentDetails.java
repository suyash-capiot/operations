package com.coxandkings.travel.operations.model.forex.paymentDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ForexPaymentDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_details_id")
    private List<ForexPayToSupplier> forexPayToSupplierSet;

    @Column(name = "document_reference_id")
    @ElementCollection(targetClass = String.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> documentReferenceIds = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ForexPayToSupplier> getForexPayToSupplierSet() {
        return forexPayToSupplierSet;
    }

    public void setForexPayToSupplierSet(List<ForexPayToSupplier> forexPayToSupplierSet) {
        this.forexPayToSupplierSet = forexPayToSupplierSet;
    }

    public List<String> getDocumentReferenceIds() {
        return documentReferenceIds;
    }

    public void setDocumentReferenceIds(List<String> documentReferenceIds) {
        this.documentReferenceIds = documentReferenceIds;
    }
}
