package com.coxandkings.travel.operations.utils.changeSupplier;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserContext {

    Logger logger = LogManager.getLogger(UserContext.class);

    @Autowired
    private static MDMRestUtils mdmRestUtils;

    private Map<String, List<String>> UserContext(JSONObject reqHdr) throws Exception {
        Map<String, List<String>> mProdSuppMap = new HashMap<String, List<String>>();

        JSONObject clientContextJson = reqHdr.getJSONObject("clientContext");
        String clientID = clientContextJson.getString("clientID");
        String clientLang = clientContextJson.getString("clientLanguage");
        String clientMkt = clientContextJson.getString("clientMarket");
        String clientType = clientContextJson.getString("clientType");
        String pointOfSale = clientContextJson.getString("pointOfSale");
        String userID = reqHdr.optString("userID");


        String clientEntityProdSuppDoc = getProductSuppliersForClient(clientContextJson, clientContextJson.optString("clientID"));
        // TODO: Should not have to check the same condition (clientEntityProdSuppDoc == null) multiple times.
        if (clientEntityProdSuppDoc == null) {
            throw new Exception("No product supplier mapping was found for client entity");
        }

        JSONObject prodCategs = new JSONObject(clientEntityProdSuppDoc);

        JSONArray prodCatDocs = prodCategs.getJSONArray("productCategories");
        if (prodCatDocs == null) {
            throw new Exception("No product categories defined in product supplier mapping for client entity");
        }

        for (int i=0;i<prodCatDocs.length();i++) {
            JSONObject prodCatDoc = prodCatDocs.getJSONObject(i);

            String prodCategory = prodCatDoc.getString("productCategory");
            JSONArray prodSubCatDocs = prodCatDoc.getJSONArray("productCatSubTypes");
            if (prodSubCatDocs == null || prodSubCatDocs.length() == 0) {
                continue;
            }

            for (int j=0;j<prodSubCatDocs.length();j++){

                JSONObject prodSubCatDoc = prodSubCatDocs.getJSONObject(j);

                String prodSubCategory = prodSubCatDoc.getString("subType");

                String prod = prodCategory.concat("|").concat(prodSubCategory);
                JSONArray mappingDocs = prodSubCatDoc.getJSONArray("mappings");
                if (mappingDocs == null || mappingDocs.length() == 0) {
                    continue;
                }

                List<String> prodSupps = new ArrayList<String>();
                List<String> dupCheckList = new ArrayList<String>();
                for (int k=0;k<mappingDocs.length();k++) {

                    JSONObject mappingDoc = mappingDocs.getJSONObject(k);

                    JSONObject suppDoc = mappingDoc.getJSONObject("supplier");
                    String suppID = suppDoc.getString("supplierId");

                    List<String> creds = (List<String>) suppDoc.get("credentials");
                    if (creds == null || creds.size() == 0) {
                        continue;
                    }

                    for (String cred : creds) {
                        String dupCheckKey = String.format("%s|%s", suppID, cred);
                        if (dupCheckList.contains(dupCheckKey)) {
                            continue;
                        }

                        dupCheckList.add(dupCheckKey);
                        String suppCredsDoc = getSupplierCredentialsConfig(suppID, cred);
                        if ( suppCredsDoc == null) {
                            logger.warn(String.format("Supplier credentials %s for supplier %s could not be retrieved from MDM", cred, suppID));
                            continue;
                        }
                        prodSupps.add(suppCredsDoc);
                    }
                }
                mProdSuppMap.put(prod, prodSupps);
            }
        }

        return mProdSuppMap;
    }



    private static String getProductSuppliersForClient(JSONObject clientContextJson, String clientID) throws OperationException {
        String clientEntityProdSuppDoc = null;
        String clientLanguage = clientContextJson.getString("clientLanguage");
        String clientMarket = clientContextJson.getString("clientMarket");
        String clientType = clientContextJson.getString("clientType");
        String pointOfSale = clientContextJson.getString("pointOfSale");

        if (clientID!=null && !clientID.isEmpty()) {
            clientEntityProdSuppDoc = getProductSuppliersConfig(clientID, clientMarket);
            if (clientEntityProdSuppDoc != null) {
                return clientEntityProdSuppDoc;
            }

            // Check if there is a Client Group associated with this Client ID
            // Reference CKIL_323230 (2.2.3/BR16): As per BRD, when tiers are configured for a
            // master agent, the corresponding client groups should be automatically created.
            // Therefore, following code should handle client groups as well as tiers.
            clientEntityProdSuppDoc = getClientGroupProductSuppliers(clientID, clientMarket);
            if (clientEntityProdSuppDoc != null) {
                return clientEntityProdSuppDoc;
            }
        }

        return getClientTypeProductSuppliers(clientType, clientMarket, clientLanguage, pointOfSale);
    }

    private static String getProductSuppliersConfig(String entityId, String market) throws OperationException{

        String url = "http://10.25.6.103:10051/client-config/v1/enable-disable-product-supplier?filter=%s";

        JSONObject filters = new JSONObject();
        filters.put("deleted", false);
        filters.put("productSupplierAttachedTo".concat(".").concat("entityId"), entityId);
        filters.put("productSupplierAttachedTo".concat(".").concat("companyMarket"), market);

        return mdmRestUtils.getResponseJSON(String.format(url,filters.toString()));
    }

    private static String getClientGroupProductSuppliers(String clientID, String clientMarket) throws OperationException {
        //MongoCollection<Document> ac2gColl = MDMConfig.getCollection(MDM_COLL_ASSOCCLIENTTOGROUP);

        String url = "http://10.25.6.103:10080/client-dashboard/v1/associate-client-to-group?filter=%s";

        JSONObject exists = new JSONObject();
        exists.put("$exists",true);

        JSONObject filters = new JSONObject();
        filters.put("deleted", false);
        filters.put("clientId", clientID);
        filters.put("groups".concat(".").concat("productSupplier"), exists);

        // Reference CKIL_323230 (2.2.2/BR09): For a client, there could be multiple client group mappings.
        // In such case, the mapping with latest timestamp would be used.
        String latestUpdatedDocument = mdmRestUtils.getResponseJSON(String.format(url,filters.toString()));
        if (latestUpdatedDocument == null) {
            return null;
        }

        JSONObject latestDoc = new JSONObject(latestUpdatedDocument);

        String clientGroupID = latestDoc.getJSONObject("groups").getString("productSupplier");
        return getProductSuppliersConfig(clientGroupID, clientMarket);
    }

    private static String getClientTypeProductSuppliers(String clientType, String clientMarket, String clientLanguage, String pointOfSale) throws OperationException {
        String clientTypeEntity = getClientTypeEntity(clientType, clientMarket, clientLanguage, pointOfSale);
        return (clientTypeEntity != null && clientTypeEntity.isEmpty() == false) ? getProductSuppliersConfig(clientTypeEntity, clientMarket) : null;
    }

    private static String getClientTypeEntity(String clientType, String clientMarket, String clientLanguage, String pointOfSale) throws OperationException {

        JSONObject doc = new JSONObject(getClientTypeEntityDoc(clientType, clientMarket, clientLanguage, pointOfSale));
        return doc.getString("_id");
    }

    private static String getClientTypeEntityDoc(String clientType, String clientMarket, String clientLanguage, String pointOfSale) throws OperationException{

        String url = "http://10.25.6.103:10050/client/v1/ClientType?filter=%s";

        JSONObject filters = new JSONObject();
        filters.put("deleted", false);
        filters.put("clientStructure".concat(".").concat("clientEntityType"), clientType);
        filters.put("clientStructure".concat(".").concat("clientMarket"), clientMarket);
        filters.put("clientStructure".concat(".").concat("language"), clientLanguage);
        filters.put("clientStructure".concat(".").concat("pointOfSale"), pointOfSale);

        return mdmRestUtils.getResponseJSON(String.format(url,filters.toString()));
    }

    public static String getSupplierCredentialsConfig(String suppID, String cred) throws OperationException{
        // MongoCollection<Document> scColl =
        // MDMConfig.getCollection(MDM_COLL_SUPPCREDS);

        String url = "http://10.25.6.103:10001/supplier-config/v1/suppliersCredential?filter=%s";

        JSONObject filters = new JSONObject();
        filters.put("deleted", false);
//        filters.put("_id", cred);
        filters.put("supplier".concat(".").concat("supplierId"), suppID);

        return mdmRestUtils.getResponseJSON(String.format(url, filters));
    }

}
