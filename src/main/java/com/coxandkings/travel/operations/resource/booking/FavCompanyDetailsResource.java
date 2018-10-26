package com.coxandkings.travel.operations.resource.booking;

import com.coxandkings.travel.operations.resource.BaseResource;

public class FavCompanyDetailsResource extends BaseResource {

    private String favId;
    private String groupOfCompaniesId;
    private String groupCompanyId;
    private String companyId;
    private String companyMarketId;
    private String sbuId;
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

    public String getGroupNameId() {
        return groupCompanyId;
    }

    public void setGroupNameId(String groupCompanyId) {
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
