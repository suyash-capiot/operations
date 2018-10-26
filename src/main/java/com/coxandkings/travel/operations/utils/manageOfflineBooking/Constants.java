package com.coxandkings.travel.operations.utils.manageOfflineBooking;

public class Constants {

    //offlineBooking Constants starts here
    // BE constants
    public static final String JSON_PROP_CLIENT_TYPE = "clientType";
    public static final String JSON_PROP_PROD_CAT = "productCategory";
    public static final String JSON_PROP_PROD_SUB_CAT = "productSubCategory";

    public static final String MDM_VAL_CLIENTTYPE_GROUP = "Client Group";
    public static final String MDM_VAL_CLIENTTYPE_CLIENT = "Client";
    public static final String MDM_VAL_CLIENTTYPE_ENTITY = "Client Entity";
    public static final String MDM_PROP_LASTUPDATED = "lastUpdated";
    public static final String MDM_PROP_CLIENTPROFILE = "clientProfile";
    public static final String MDM_PROP_CLIENTDETAILS = "clientDetails";
    public static final String MDM_PROP_CLIENTMKT = "clientMarket";
    public static final String MDM_PROP_PARENTASSOC = "parentAssociation";
    public static final String MDM_PROP_ENTITYID = "entityId";
    public static final String MDM_PROP_BUDGETMARGINATTACHEDTO = "budgetMarginAttachedTo";
    public static final String MDM_PROP_ENTITYTYPE = "entityType";
    public static final String MDM_PROP_COMPANYMKT = "companyMarket";
    public static final String MDM_PROP_CLIENTID = "clientId";
    public static final String MDM_PROP_GROUPS = "groups";
    public static final String MDM_PROP_COMMERCIALSID = "commercialsId";
    public static final String MDM_PROP_CLIENTSTRUCT = "clientStructure";
    public static final String MDM_PROP_CLIENTENTITYTYPE = "clientEntityType";
    public static final String MDM_PROP_PRODSUPPATTACHEDTO = "productSupplierAttachedTo";
    public static final String MDM_PROP_PRODSUPP = "productSupplier";
    public static final String MDM_PROP_CLIENTMARKET = "clientMarket";
    public static final String MDM_PROP_LANGUAGE = "language";
    public static final String MDM_PROP_POS = "pointOfSale";
    public static final String MDM_PROP_PRODCATEGS = "productCategories";
    public static final String MDM_PROP_PRODCATEG = "productCategory";
    public static final String MDM_PROP_PRODCATEGSUBTYPES = "productCatSubTypes";
    public static final String MDM_PROP_SUBTYPE = "subType";
    public static final String MDM_PROP_MAPPINGS = "mappings";
    public static final String MDM_PROP_SUPP = "supplier";
    public static final String MDM_PROP_SUPPID = "supplierId";
    public static final String MDM_PROP_SUPPCREDS = "credentials";
    public static final String MDM_PROP_TRANS_CCY = "transactionalCurrency";
    public static final String MDM_PROP_ORGHIERARCHY = "orgHierarchy";
    public static final String MDM_PROP_NAME = "name";
    public static final String MDM_PROP_ADDRESS = "address";
    public static final String MDM_PROP_COUNTRY = "country";
    public static final String MDM_PROP_STATE = "state";
    public static final String MDM_PROP_CLIENTTYPE = "clientType";



    public static final String JSON_PROP_COMPANYNAME = "companyName";
    public static final String JSON_PROP_CREDSNAME = "credentialsName";
    public static final String JSON_PROP_ENTITYTYPE = "entityType";
    public static final String JSON_PROP_CLIENTID = "clientID";
    public static final String JSON_PROP_CLIENTMARKET = "clientMarket";
    public static final String JSON_PROP_PARENTCLIENTID = "parentClientID";
    public static final String JSON_PROP_COMMENTITYTYPE = "commercialEntityType";
    public static final String JSON_PROP_COMMENTITYID = "commercialEntityID";
    public static final String JSON_PROP_COMMENTITYMARKET = "commercialEntityMarket";
    public static final String JSON_PROP_ENTITYNAME = "entityName";
    public static final String JSON_PROP_ENTITYMARKET = "entityMarket";
    public static final String JSON_PROP_PARENTENTITYNAME = "parentEntityName";
    public static final String JSON_PROP_ENTITYDETAILS = "entityDetails";
    public static final String JSON_PROP_CLIENTCOMMENTITYDTLS = "clientCommercialsEntityDetails";
    public static final String JSON_PROP_CCY = "currency";
    public static final String JSON_PROP_COMPANY_MKT = "companyMarket";
    public static final String JSON_PROP_MKT = "market";
    public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";
    public static final String JSON_PROP_CLIENT_DETAILS = "clientDetails";
    public static final String JSON_PROP_PRODUCT_DETAILS = "productDetails";
    public static final String JSON_PROP_TRAVEL_PAX_DETAILS = "travelAndPassengerDetails";
    public static final String JSON_PROP_SUPPLIER_PRICE_DETAILS = "supplierPriceDetails";
    public static final String JSON_PROP_ARR_PAX_DETAILS = "passengerDetails";
    public static final String JSON_PROP_PAX_TYPE = "passengerType";
    public static final String PAX_TYPE_ADT = "ADULT";
    public static final String PAX_TYPE_CHD = "CHILD";
    public static final String PAX_TYPE_INF = "INFANT";
    public static final String JSON_PROP_PAX_DOB = "dob";
    public static final String JSON_PROP_SUPP_ID = "supplierID";
    public static final String JSON_PROP_RESP_HEADER = "responseHeader";
    public static final String JSON_PROP_RESP_BODY = "responseBody";
    public static final String JSON_PROP_SESSION_ID = "sessionID";
    public static final String JSON_PROP_USER_ID = "userID";
    public static final String JSON_PROP_TRAN_ID = "transactionID";
    public static final String JSON_PROP_CLI_CONT = "clientContext";
    //Acco
    public static final String JSON_PROP_ARR_ROOM_DET_SUMM = "roomDetailsSummary";
    public static final String JSON_PROP_CHKIN_DATE = "checkInDate";
    public static final String JSON_PROP_CHKOUT_DATE = "checkOutDate";
    public static final String JSON_PROP_PROD_NAME = "productName";
    public static final String JSON_PROP_COUNTRY = "country";
    public static final String JSON_PROP_CITY = "city";
    //req constants
    public static final String JSON_PROP_CLIENTDTLS = "clientDetails";
    public static final String JSON_PROP_PRODUCTDTLS = "productDetails";
    public static final String JSON_PROP_TRAVEL_PAX_DTLS = "travelAndPassengerDetails";
    public static final String JSON_PROP_PAYMENTDTLS = "paymentDetails";
    //error constants
    public static final String OPS_ERR_100 = "FromCurrency and toCurrency is required";
    public static final String OPS_ERR_101 = "company market is required";
    public static final String OPS_ERR_102 = "supplier currency is required";
    public static final String OPS_ERR_103 = "client type is required";
    public static final String TECHNICAL_ISSUE_SUPP_COMM_REQ_GENERATE = "technical.issue.supp.comm.req.generate";

}