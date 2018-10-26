package com.coxandkings.travel.operations.model.letter;

import com.coxandkings.travel.operations.model.amendsuppliercommercial.JSONUserType;
import com.coxandkings.travel.operations.model.communication.BaseCommunication;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "letter")
@TypeDefs({@TypeDef(name = "StringJsonObject", typeClass = JSONUserType.class)})
public class Letter extends BaseCommunication {

    private String address;
    @Column
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "java.util.ArrayList")})
    private List<String> documentRefIDs;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getDocumentRefIDs() {
        return documentRefIDs;
    }

    public void setDocumentRefIDs(List<String> documentRefIDs) {
        this.documentRefIDs = documentRefIDs;
    }

    @Override
    public String toString() {
        return "Letter{" +
                "address='" + address + '\'' +
                '}';
    }
}
