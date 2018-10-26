package com.coxandkings.travel.operations.model.booking;

import com.coxandkings.travel.operations.model.BaseModel;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user_favourite")
@TypeDefs({@TypeDef(name = "StringJsonObject", typeClass = StringJsonUserType.class)})
public class UserFavourite extends BaseModel implements Serializable {

    @Column
    private String favName;

    @Column
    private String userId;

    @Column
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "java.lang.String")})
    private String userFavouriteSearchReq;


    public String getFavName() {
        return favName;
    }

    public void setFavName(String favName) {
        this.favName = favName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFavouriteSearchReq() {
        return userFavouriteSearchReq;
    }

    public void setUserFavouriteSearchReq(String userFavouriteSearchReq) {
        this.userFavouriteSearchReq = userFavouriteSearchReq;
    }
}
