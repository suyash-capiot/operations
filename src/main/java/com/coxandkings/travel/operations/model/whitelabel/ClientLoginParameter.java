package com.coxandkings.travel.operations.model.whitelabel;

import com.coxandkings.travel.operations.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name= "white_label_client_login_parameters")
public class ClientLoginParameter extends BaseModel {

//    @Id
//    @Column(name= "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private String id;

    @Column(name= "login_parameter")
    private String loginParameter;

    @ManyToOne
    @JoinColumn(name= "white_label_id")
    @JsonIgnore
    private WhiteLabel whiteLabel;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getLoginParameter() {
        return loginParameter;
    }

    public void setLoginParameter(String loginParameter) {
        this.loginParameter = loginParameter;
    }

    public WhiteLabel getWhiteLabel() {
        return whiteLabel;
    }

    public void setWhiteLabel(WhiteLabel whiteLabel) {
        this.whiteLabel = whiteLabel;
    }

}
