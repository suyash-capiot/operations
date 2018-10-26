package com.coxandkings.travel.operations.model.whitelabel;

import com.coxandkings.travel.operations.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "white_label_header_detail")
public class HeaderDetail extends BaseModel{

//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private String id;

    @Column(name = "element_id" )
    private String elementId;

    @Column(name = "control_type_id")
    private String controlTypeId;

    @Column(name = "content")
    private String content;

    @Column(name = "filePath")
    private String filePath;

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

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getControlTypeId() {
        return controlTypeId;
    }

    public void setControlTypeId(String controlTypeId) {
        this.controlTypeId = controlTypeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public WhiteLabel getWhiteLabel() {
        return whiteLabel;
    }

    public void setWhiteLabel(WhiteLabel whiteLabel) {
        this.whiteLabel = whiteLabel;
    }

}
