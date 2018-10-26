package com.coxandkings.travel.operations.model.whitelabel;

import com.coxandkings.travel.operations.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "white_label_payment_information")
public class PaymentInformationStandardDetail extends BaseModel {

//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private String id;

    @Column(name = "payment_mode")
    private String modeOfPayment;

    @Column(name = "bank_id")
    private String bankId;

    @Column(name = "content")
    private String content;

    //TODO: Figure-out data type to store key file
    // File path to the particular keyFile
    @Column(name = "key_file")
    private String keyFile;

    @Column(name = "mid_name")
    private String midName;

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

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getKeyFile() {
        return keyFile;
    }

    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMidName() {
        return midName;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public WhiteLabel getWhiteLabel() {
        return whiteLabel;
    }

    public void setWhiteLabel(WhiteLabel whiteLabel) {
        this.whiteLabel = whiteLabel;
    }

}
