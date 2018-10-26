package com.coxandkings.travel.operations.model.whitelabel;

import com.coxandkings.travel.operations.enums.whitelabel.WhiteLabelConfigurationType;

import javax.persistence.*;

@Entity
@Table(name = "white_label_configuration_type_enum_Handler")
public class ConfigurationTypeEnumHandler {
    @Id
    @Column(name="id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column
    private String name;///For Multilingual

    @Column
    @Enumerated(EnumType.STRING)
    private WhiteLabelConfigurationType code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WhiteLabelConfigurationType getCode() {
        return code;
    }

    public void setCode(WhiteLabelConfigurationType code) {
        this.code = code;
    }

}
