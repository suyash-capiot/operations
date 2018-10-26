package com.coxandkings.travel.operations.model.mailroomanddispatch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="dispatch_contact_details")
public class ContentDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="dispatch_contact_id")
    private String id;
    private String contents;
    private Integer numberOfDocuments;
    private String remarks;
    @ManyToOne
    @JsonIgnore
    private OutboundDispatch outboundDispatch;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public OutboundDispatch getOutboundDispatch() {
        return outboundDispatch;
    }

    public void setOutboundDispatch(OutboundDispatch outboundDispatch) {
        this.outboundDispatch = outboundDispatch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentDetails that = (ContentDetails) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(contents, that.contents) &&
                Objects.equals(numberOfDocuments, that.numberOfDocuments) &&
                Objects.equals(remarks, that.remarks) &&
                Objects.equals(outboundDispatch, that.outboundDispatch);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, contents, numberOfDocuments, remarks,outboundDispatch);
    }

    @Override
    public String toString() {
        return "ContentDetails{" +
                "id='" + id + '\'' +
                ", contents='" + contents + '\'' +
                ", numberOfDocuments=" + numberOfDocuments +
                ", remarks='" + remarks + '\'' +
               ", outboundDispatch=" + outboundDispatch +
                '}';
    }
}
