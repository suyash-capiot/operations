package com.coxandkings.travel.operations.resource.outbound.be;

public class CompanyDetailsResource {
//modified
    private String groupOfCompaniesId;
    private String groupCompanyId;
    private String companyId;
    private String companyMarketId;
    private String sbuId;
    private String buId;
    private String companyName;

    public String getCompanyName( ) {
        return companyName;
    }

    public void setCompanyName( String companyName ) {
        this.companyName = companyName;
    }

    public String getGroupOfCompaniesId( ) {
        return groupOfCompaniesId;
    }

    public void setGroupOfCompaniesId( String groupOfCompaniesId ) {
        this.groupOfCompaniesId = groupOfCompaniesId;
    }

    public String getGroupCompanyId( ) {
        return groupCompanyId;
    }

    public void setGroupCompanyId( String groupCompanyId ) {
        this.groupCompanyId = groupCompanyId;
    }

    public String getCompanyId( ) {
        return companyId;
    }

    public void setCompanyId( String companyId ) {
        this.companyId = companyId;
    }

    public String getCompanyMarketId( ) {
        return companyMarketId;
    }

    public void setCompanyMarketId( String companyMarketId ) {
        this.companyMarketId = companyMarketId;
    }

    public String getSbuId( ) {
        return sbuId;
    }

    public void setSbuId( String sbuId ) {
        this.sbuId = sbuId;
    }

    public String getBuId( ) {
        return buId;
    }

    public void setBuId( String buId ) {
        this.buId = buId;
    }
}
