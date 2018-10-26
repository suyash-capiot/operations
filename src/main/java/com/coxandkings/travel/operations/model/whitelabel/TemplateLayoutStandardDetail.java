package com.coxandkings.travel.operations.model.whitelabel;

import com.coxandkings.travel.operations.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "white_label_template_layout")
public class TemplateLayoutStandardDetail extends BaseModel{

//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private String id;

    @Column(name = "template_layout_id")
    private String templateLayoutId;

    @Column(name = "template_type_id")
    private String templateTypeId;

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

    public String getTemplateLayoutId() {
        return templateLayoutId;
    }

    public void setTemplateLayoutId(String templateLayoutId) {
        this.templateLayoutId = templateLayoutId;
    }

    public String getTemplateTypeId() {
        return templateTypeId;
    }

    public void setTemplateTypeId(String templateTypeId) {
        this.templateTypeId = templateTypeId;
    }
    
    public WhiteLabel getWhiteLabel() {
        return whiteLabel;
    }

    public void setWhiteLabel(WhiteLabel whiteLabel) {
        this.whiteLabel = whiteLabel;
    }

}
