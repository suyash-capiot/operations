package com.coxandkings.travel.operations.model.merge;

import com.coxandkings.travel.operations.enums.merge.MergeTypeValues;

import javax.persistence.*;

@Entity
@Table(name = "merge_type")
public class MergeType {
    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", unique = true, nullable = false)
    private MergeTypeValues code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MergeTypeValues getCode() {
        return code;
    }

    public void setCode(MergeTypeValues code) {
        this.code = code;
    }
}