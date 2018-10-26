package com.coxandkings.travel.operations.resource.outbound.be;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BookingDetailsFinalResponse {

    List< BookingDetailsResponse > contents = new ArrayList<>( );
    private Long totalElements;
    private Boolean last;
    private Boolean first;
    private Long totalPages;
    private Long size;
    private Long number;
    private Long numberOfElements;

    public BookingDetailsFinalResponse() {
        contents = new LinkedList<>();
    }

    public List< BookingDetailsResponse > getContents( ) {
        return contents;
    }

    public void setContents( List< BookingDetailsResponse > contents ) {
        this.contents = contents;
    }

    public Long getTotalElements( ) {
        return totalElements;
    }

    public void setTotalElements( Long totalElements ) {
        this.totalElements = totalElements;
    }

    public Boolean getLast( ) {
        return last;
    }

    public void setLast( Boolean last ) {
        this.last = last;
    }

    public Boolean getFirst( ) {
        return first;
    }

    public void setFirst( Boolean first ) {
        this.first = first;
    }

    public Long getTotalPages( ) {
        return totalPages;
    }

    public void setTotalPages( Long totalPages ) {
        this.totalPages = totalPages;
    }

    public Long getSize( ) {
        return size;
    }

    public void setSize( Long size ) {
        this.size = size;
    }

    public Long getNumber( ) {
        return number;
    }

    public void setNumber( Long number ) {
        this.number = number;
    }

    public Long getNumberOfElements( ) {
        return numberOfElements;
    }

    public void setNumberOfElements( Long numberOfElements ) {
        this.numberOfElements = numberOfElements;
    }

}
