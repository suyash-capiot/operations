package com.coxandkings.travel.operations.model.reconfirmation.supplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class SupplierReconfirmationFilter {

    @JsonProperty( "productDetails.configurationFor" )
    private String configurationFor;

    @JsonProperty( "productDetails.productCategory" )
    private String productCategory;

    @JsonProperty( "productDetails.productCatSubtype" )
    private String productCatSubtype;

    @JsonProperty( "productDetails.productId" )
    private String productId;

    @JsonProperty( "productDetails.productNameSubType" )
    private String productNameSubType;

    @JsonProperty( "productDetails.productFlavor" )
    private String productFlavor;

    @JsonProperty( "supplierReconfirmation.supplierDetails.supplierName" )
    private String supplierName;

    @JsonProperty( "supplierReconfirmation.supplierDetails.supplierId" )
    private String supplierId;

    public void setConfigurationFor( String configurationFor ) {
        this.configurationFor = configurationFor;
    }

    public void setProductCategory( String productCategory ) {
        this.productCategory = productCategory;
    }

    public void setProductCatSubtype( String productCatSubtype ) {
        this.productCatSubtype = productCatSubtype;
    }

    public void setProductId( String productId ) {
        this.productId = productId;
    }

    public void setProductNameSubType( String productNameSubType ) {
        this.productNameSubType = productNameSubType;
    }

    public void setProductFlavor( String productFlavor ) {
        this.productFlavor = productFlavor;
    }

    public String getConfigurationFor( ) {
        return configurationFor;
    }

    public String getProductCategory( ) {
        return productCategory;
    }

    public String getProductCatSubtype( ) {
        return productCatSubtype;
    }

    public String getProductId( ) {
        return productId;
    }

    public String getProductNameSubType( ) {
        return productNameSubType;
    }

    public String getProductFlavor( ) {
        return productFlavor;
    }

    public String getSupplierId( ) {
        return supplierId;
    }

    public void setSupplierId( String supplierId ) {
        this.supplierId = supplierId;
    }

    public String getSupplierName( ) {
        return supplierName;
    }

    public void setSupplierName( String supplierName ) {
        this.supplierName = supplierName;
    }

    //    public static void main( String[] args ) throws Exception {
//
//        SupplierReconfirmationFilter mdmFilter = new SupplierReconfirmationFilter( );
//        mdmFilter.setProductCategory( "Transportation" );
//        mdmFilter.setProductCatSubtype( "" );
//        ObjectMapper aMapper = new ObjectMapper( );
//        System.out.println( "Output is:\n" + aMapper.writeValueAsString( mdmFilter ) );
//    }

    public  String getUrl( SupplierReconfirmationFilter supplierReconfirmationFilter ) {
        String url = null;
        try {
            ObjectMapper aMapper = new ObjectMapper( );
            url = aMapper.writeValueAsString( supplierReconfirmationFilter );
            return url;
        } catch ( Exception e ) {
            e.printStackTrace( );
            return url;
        }
    }
}

