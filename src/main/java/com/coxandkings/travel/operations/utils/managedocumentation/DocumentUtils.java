package com.coxandkings.travel.operations.utils.managedocumentation;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.model.core.OpsAccommodationPaxInfo;
import com.coxandkings.travel.operations.model.core.OpsFlightPaxInfo;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.managedocumentation.Document;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class DocumentUtils {

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Value(value = "${manage_documentation.be.acco_rooms_pax_info}")
    private String accoRoomsPaxInfo;

    @Value(value = "${manage_documentation.be.air_pax_info}")
    private String airPaxInfo;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Logger logger = LogManager.getLogger(DocumentUtils.class);

    public Document getPaxInfo(OpsProduct product) {
        Document document = new Document();
        if (product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)) {
            List<OpsAccommodationPaxInfo> paxInfoList = null;
            try {
                paxInfoList = jsonObjectProvider.getChildrenCollection(objectMapper.writeValueAsString(product), accoRoomsPaxInfo, OpsAccommodationPaxInfo.class);
            } catch (Exception ex) {
                logger.info("Issue in serializing the object");
            }
            if (paxInfoList != null && paxInfoList.size() >= 1) {
                OpsAccommodationPaxInfo paxInfo = paxInfoList.stream().filter(opsAccommodationPaxInfo -> opsAccommodationPaxInfo.getLeadPax()).findFirst().get();
                document.setPaxId(paxInfo.getPaxID());
                StringBuilder paxName = new StringBuilder();
                paxName.append(paxInfo.getFirstName());
                        /*if (!StringUtils.isEmpty(paxInfo.getMiddleName()))
                            paxName.append(" ").append(paxInfo.getMiddleName());*/
                if (!StringUtils.isEmpty(paxInfo.getLastName()))
                    paxName.append(" ").append(paxInfo.getLastName());
                document.setPassengerName(paxName.toString());
            }
        } else if (product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
            List<OpsFlightPaxInfo> paxInfoList = null;
            try {
                paxInfoList = jsonObjectProvider.getChildrenCollection(objectMapper.writeValueAsString(product), airPaxInfo, OpsFlightPaxInfo.class);
            } catch (Exception ex) {
                logger.info("Issue in serializing the object");
            }
            if (paxInfoList != null && paxInfoList.size() >= 1) {
                OpsFlightPaxInfo paxInfo = paxInfoList.stream().filter(opsFlightPaxInfo -> opsFlightPaxInfo.getLeadPax()).findFirst().get();
                document.setPaxId(paxInfo.getPassengerID());
                StringBuilder paxName = new StringBuilder();
                paxName.append(paxInfo.getFirstName());
                        /*if (!StringUtils.isEmpty(paxInfo.getMiddleName()))
                            paxName.append(" ").append(paxInfo.getMiddleName());*/
                if (!StringUtils.isEmpty(paxInfo.getLastName()))
                    paxName.append(" ").append(paxInfo.getLastName());
                document.setPassengerName(paxName.toString());
            }
        }
        return document;
    }
}
