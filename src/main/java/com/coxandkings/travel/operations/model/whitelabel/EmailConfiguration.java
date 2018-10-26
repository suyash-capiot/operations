package com.coxandkings.travel.operations.model.whitelabel;

import com.coxandkings.travel.operations.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "white_label_email_configuration")
public class EmailConfiguration extends BaseModel {

//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private String id;

    private String functionId;

    private String emailFrom;

    private String emailToCC;

    private String emailToBCC;

    //TODO: Figure out the source of server settings id
    private String serverSettingId;

    @ManyToOne
    @JoinColumn(name = "white_label_id")
    @JsonIgnore
    private WhiteLabel whiteLabel;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailToCC() {
        return emailToCC;
    }

    public void setEmailToCC(String emailToCC) {
        this.emailToCC = emailToCC;
    }

    public String getEmailToBCC() {
        return emailToBCC;
    }

    public void setEmailToBCC(String emailToBCC) {
        this.emailToBCC = emailToBCC;
    }

    public String getServerSettingId() {
        return serverSettingId;
    }

    public void setServerSettingId(String serverSettingId) {
        this.serverSettingId = serverSettingId;
    }

    public WhiteLabel getWhiteLabel() {
        return whiteLabel;
    }

    public void setWhiteLabel(WhiteLabel whiteLabel) {
        this.whiteLabel = whiteLabel;
    }

}
