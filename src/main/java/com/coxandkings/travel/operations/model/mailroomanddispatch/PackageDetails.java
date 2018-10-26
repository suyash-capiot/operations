package com.coxandkings.travel.operations.model.mailroomanddispatch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="inbound_package_details")
public class PackageDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="inbound_package_id")
    private String id;
    private String contents;
    private Integer numberOfDocuments;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_inbound")
    private InboundEntry inboundEntry;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Integer getNumberOfDocuments() {
        return numberOfDocuments;
    }

    public void setNumberOfDocuments(Integer numberOfDocuments) {
        this.numberOfDocuments = numberOfDocuments;
    }

    public InboundEntry getInboundEntry() {
        return inboundEntry;
    }

    public void setInboundEntry(InboundEntry inboundEntry) {
        this.inboundEntry = inboundEntry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageDetails that = (PackageDetails) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(contents, that.contents) &&
                Objects.equals(numberOfDocuments, that.numberOfDocuments) &&
                Objects.equals(inboundEntry, that.inboundEntry);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, contents, numberOfDocuments, inboundEntry);
    }

    /*@Override
    public String toString() {
        return "PackageDetails{" +
                "id='" + id + '\'' +
                ", contents='" + contents + '\'' +
                ", numberOfDocuments=" + numberOfDocuments +
                ", inboundEntry=" + inboundEntry +
                '}';
    }*/
}
