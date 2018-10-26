package com.coxandkings.travel.operations.resource.outbound.be;

public class SearchFilterResource {

    private BookingDetailsResource favBookingDetailsResource;
    private ProductDetailsResource favProductDetailsResource;
    private ClientAndPassengerDetailsResource favCliAndPassengerDetailsResource;
    private CompanyDetailsResource favCompanyDetailsResource;
    private Integer size;
    private Integer page;
    private String sortingOrder;
    private String sortingField;



    public String getSortingOrder( ) {
        return sortingOrder;
    }

    public void setSortingOrder( String sortingOrder ) {
        this.sortingOrder = sortingOrder;
    }

    public String getSortingField() {
        return sortingField;
    }

    public void setSortingField(String sortingField) {
        this.sortingField = sortingField;
    }

    public BookingDetailsResource getFavBookingDetailsResource( ) {
        return favBookingDetailsResource;
    }

    public void setFavBookingDetailsResource( BookingDetailsResource favBookingDetailsResource ) {
        this.favBookingDetailsResource = favBookingDetailsResource;
    }

    public ProductDetailsResource getFavProductDetailsResource( ) {
        return favProductDetailsResource;
    }

    public void setFavProductDetailsResource( ProductDetailsResource favProductDetailsResource ) {
        this.favProductDetailsResource = favProductDetailsResource;
    }

    public ClientAndPassengerDetailsResource getFavCliAndPassengerDetailsResource( ) {
        return favCliAndPassengerDetailsResource;
    }

    public void setFavCliAndPassengerDetailsResource( ClientAndPassengerDetailsResource favCliAndPassengerDetailsResource ) {
        this.favCliAndPassengerDetailsResource = favCliAndPassengerDetailsResource;
    }

    public CompanyDetailsResource getFavCompanyDetailsResource( ) {
        return favCompanyDetailsResource;
    }

    public void setFavCompanyDetailsResource( CompanyDetailsResource favCompanyDetailsResource ) {
        this.favCompanyDetailsResource = favCompanyDetailsResource;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
