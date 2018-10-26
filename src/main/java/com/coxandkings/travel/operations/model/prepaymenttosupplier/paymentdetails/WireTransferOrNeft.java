package com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "WireTransferOrNeft")
public class WireTransferOrNeft {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "fromBankName")
    private String fromBankName; //TODO: From Bank master

    @Column(name = "toBankName")
    private String toBankName;

    @Column(name = "accountNumber")
    private String accountNumber;

    @Column(name = "swiftIfscCode")
    private String swiftIfscCode;

    @Column(name = "intermediaryBankSwiftOrIfscCode")
    private String intermediaryBankSwiftOrIfscCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromBankName() {
        return fromBankName;
    }

    public void setFromBankName(String fromBankName) {
        this.fromBankName = fromBankName;
    }

    public String getToBankName() {
        return toBankName;
    }

    public void setToBankName(String toBankName) {
        this.toBankName = toBankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSwiftIfscCode() {
        return swiftIfscCode;
    }

    public void setSwiftIfscCode(String swiftIfscCode) {
        this.swiftIfscCode = swiftIfscCode;
    }

    public String getIntermediaryBankSwiftOrIfscCode() {
        return intermediaryBankSwiftOrIfscCode;
    }

    public void setIntermediaryBankSwiftOrIfscCode(String intermediaryBankSwiftOrIfscCode) {
        this.intermediaryBankSwiftOrIfscCode = intermediaryBankSwiftOrIfscCode;
    }
}
