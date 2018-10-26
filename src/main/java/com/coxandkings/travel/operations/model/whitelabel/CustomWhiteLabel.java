package com.coxandkings.travel.operations.model.whitelabel;

import com.coxandkings.travel.operations.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "CustomWhiteLabel")
public class CustomWhiteLabel extends BaseModel{

//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private String id;

    @Column(name = "custom_Text")
    private String customText;

    @OneToOne
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

    public String getCustomText() {
        return customText;
    }

    public void setCustomText(String customText) {
        this.customText = customText;
    }

    public WhiteLabel getWhiteLabel() {
        return whiteLabel;
    }

    public void setWhiteLabel(WhiteLabel whiteLabel) {
        this.whiteLabel = whiteLabel;
    }
}
