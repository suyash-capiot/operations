package com.coxandkings.travel.operations.model.booking;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table
public class FavCompanyDetails extends BaseModel {

    @Column(name = "fav_id")
    private String favId;

    @Column(name = "group_of_companies_id")
    private String groupOfCompaniesId;

    @Column(name = "group_company_id")
    private String groupCompanyId;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "compnay_market_id")
    private String companyMarketId;

    @Column(name = "sbu_id")
    private String sbuId;

    @Column(name = "bu_id")
    private String buId;


    public String getFavId() {
        return favId;
    }

    public void setFavId(String favId) {
        this.favId = favId;
    }

    public String getGroupOfCompaniesId() {
        return groupOfCompaniesId;
    }

    public void setGroupOfCompaniesId(String groupOfCompaniesId) {
        this.groupOfCompaniesId = groupOfCompaniesId;
    }

    public String getGroupCompanyId() {
        return groupCompanyId;
    }

    public void setGroupCompanyId(String groupCompanyId) {
        this.groupCompanyId = groupCompanyId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyMarketId() {
        return companyMarketId;
    }

    public void setCompanyMarketId(String companyMarketId) {
        this.companyMarketId = companyMarketId;
    }

    public String getSbuId() {
        return sbuId;
    }

    public void setSbuId(String sbuId) {
        this.sbuId = sbuId;
    }

    public String getBuId() {
        return buId;
    }

    public void setBuId(String buId) {
        this.buId = buId;
    }
}
