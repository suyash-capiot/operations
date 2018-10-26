package com.coxandkings.travel.operations.model.activitylog;

import com.coxandkings.travel.operations.model.communication.BaseCommunication;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "activitylog")
public class ActivityLog extends BaseCommunication {

    private String concernedPerson;
    @ElementCollection
    private List<String> documentRefIDs;
    private String dueDate;

    public String getConcernedPerson() {
        return concernedPerson;
    }

    public void setConcernedPerson(String concernedPerson) {
        this.concernedPerson = concernedPerson;
    }

    public List<String> getDocumentRefIDs() {
        return documentRefIDs;
    }

    public void setDocumentRefIDs(List<String> documentRefIDs) {
        this.documentRefIDs = documentRefIDs;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}