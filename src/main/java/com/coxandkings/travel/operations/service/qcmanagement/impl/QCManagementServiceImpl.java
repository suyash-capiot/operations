package com.coxandkings.travel.operations.service.qcmanagement.impl;

import com.coxandkings.travel.ext.model.be.BookingActionConstants;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.qcmanagement.CancelAmendTypes;
import com.coxandkings.travel.operations.enums.qcmanagement.QcStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsSupplierType;
import com.coxandkings.travel.operations.model.qcmanagement.QcStatusInfo;
import com.coxandkings.travel.operations.repository.qcmanagement.QcManagementRepository;
import com.coxandkings.travel.operations.resource.qcmanagement.UpdateQcStatusResource;
import com.coxandkings.travel.operations.service.qcmanagement.*;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QCManagementServiceImpl implements QCManagementService {
    private static final Logger logger = LogManager.getLogger(QCManagementServiceImpl.class);

    @Autowired
    private QcCancellationService qcCancellationService;

    @Autowired
    private QcSupplierPriceDetailsService qcSupplierPriceDetailsService;

    @Autowired
    private QcSupplierCommercialService qcSupplierCommercialService;

    @Autowired
    private QcClientCommercialService qcClientCommercialService;

    @Autowired
    private QcBookingStatusService qcBookingStatusService;

    @Autowired
    private QcManagementRepository qcManagementRepository;

    @Value(value = "${qc_management.update.qcstatus}")
    private String getQcStatusUrl;

    /**
     * Purpose of this method is to perform QC checks on these header
     * 1)Supplier Price Details.
     * 2)Client Commercials
     * 3)supplier Commercials
     * 4)Booking Status
     * 5)Amendments & cancellations
     *
     * @param opsBooking
     * @throws OperationException
     */
    @Override
    public void qcCheck(OpsBooking opsBooking) throws OperationException {
        Map<String, Boolean> qcStatus = new HashMap<>();
        this.updateQcStatus(opsBooking.getBookID(), opsBooking.getUserID(), QcStatus.QC_IN_PROGRESS.getStatus());

        /**
         * 1) Supplier Price Details
         */
        //checking for every product
        if (opsBooking != null) {
            for (OpsProduct opsProduct : opsBooking.getProducts()) {
                //checking product is from online or offline supplier
                String supplierType = onlineOrOfflineOrder(opsProduct);
                if (supplierType == null) {
                    throw new OperationException("Supplier Type Should Not Be Null");
                } else if (OpsSupplierType.SUPPLIER_TYPE_ONLINE.getSupplierType().equalsIgnoreCase(supplierType)) {
                    //checking product sub category
                    if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                        Boolean accoStatus = qcSupplierPriceDetailsService.qcCheckForAccoSupplierPriceDetails(opsBooking, opsProduct);
                        qcStatus.put("AccoSupplierPriceDetails", accoStatus);
                    } else if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                        Boolean airStatus = qcSupplierPriceDetailsService.qcCheckForAirSupplierPriceDetails(opsBooking, opsProduct);
                        qcStatus.put("AirSupplierPriceDetails", airStatus);
                    }
                }
            }
            /**
             * 2) SupplierCommercial
             */
            Boolean qcAirSupplierCommercialStaus = null;
            Boolean qcAccoSupplierCommercialStatus = null;
            try {
                qcAirSupplierCommercialStaus = qcSupplierCommercialService.qcCheckForOnlineAirSupplierCommercial(opsBooking);
                qcAccoSupplierCommercialStatus = qcSupplierCommercialService.qcCheckForOnlineAccoSupplierCommercial(opsBooking);
                qcStatus.put("AirSupplierCommercial", qcAirSupplierCommercialStaus);
                qcStatus.put("AccoSupplierCommercial", qcAccoSupplierCommercialStatus);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /**
             * 3) ClientCommercial
             */
            Boolean qcAirClientCommercialStaus = null;
            Boolean qcAccoClientCommercialStatus = null;
            try {
                qcAirClientCommercialStaus = qcClientCommercialService.qcCheckForAirClientCommercial(opsBooking);
                qcAccoClientCommercialStatus = qcClientCommercialService.qcCheckForAccoClientCommercial(opsBooking);
                qcStatus.put("AirClientCommercial", qcAirClientCommercialStaus);
                qcStatus.put("AccoClientCommercial", qcAccoClientCommercialStatus);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /**
             * 4)Booking Status
             */
            Boolean qcValue = qcBookingStatusService.doQcCheckOpsBookingStatusWithSI(opsBooking);
            qcStatus.put("Bookinng Status", qcValue);

            //updating qc status
            this.checkQcStatus(qcStatus, opsBooking.getBookID(), opsBooking.getUserID());
        }
    }

    public void qcCheckForAmendmentAndCancellation(OpsProduct opsProduct, String operation, String actionType) throws OperationException {
        //todo -call methods based on kafka action type
        //for hotel product
        Map<String, Boolean> qcStatus = new HashMap<>();
        if (opsProduct.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
            CancelAmendTypes cancelAmendTypes = CancelAmendTypes.getCancelAmendTypes(actionType);
            boolean check = false;
            if (BookingActionConstants.JSON_PROP_AMEND.equalsIgnoreCase(operation)) {
                switch (cancelAmendTypes) {
                    case ACCO_AMENDTYPE_UPDATEROOM:
                        break;
                    case ACCO_AMENDTYPE_CHANGEPERIODOFSTAY:
                        break;
                    case ACCO_AMENDTYPE_ADDPAX:
                        break;
                    case ACCO_AMENDTYPE_UPDATEPAX:
                }
            } else if (BookingActionConstants.JSON_PROP_CANCEL.equalsIgnoreCase(operation)) {
                CancelAmendTypes cancel = CancelAmendTypes.getCancelAmendTypes(actionType);
                switch (cancel) {
                    case ACCO_CANCELTYPE_FULLCANCEL:
                        check = qcCancellationService.doQcLevelCheck(opsProduct, CancelAmendTypes.ACCO_CANCELTYPE_FULLCANCEL);
                        break;
                    case ACCO_CANCELTYPE_CANCELROOM:
                        break;
                    case ACCO_CANCELTYPE_CANCELPAX:
                }
            }
        } else if (opsProduct.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
            CancelAmendTypes cancelAmendTypes = CancelAmendTypes.getCancelAmendTypes(actionType);
            if (BookingActionConstants.JSON_PROP_AMEND.equalsIgnoreCase(operation)) {
                switch (cancelAmendTypes) {
                    case AIR_AMENDTYPE_PIS:
                        break;
                    case AIR_AMENDTYPE_REM:
                        break;
                    case AIR_AMENDTYPE_SSR:
                        break;
                    case AIR_AMENDTYPE_UPDATEPAX:

                }
            } else if (BookingActionConstants.JSON_PROP_CANCEL.equalsIgnoreCase(operation)) {
                switch (cancelAmendTypes) {
                    case AIR_CANCELTYPE_CANCELALL:
                        break;
                    case AIR_CANCELTYPE_CANCELJOU:
                        break;
                    case AIR_CANCELTYPE_CANCELPAX:
                        break;
                    case AIR_CANCELTYPE_CANCELSSR:
                        break;
                    case AIR_CANCELTYPE_CANCELODO:

                }
            }
        }
    }

    private void checkQcStatus(Map<String, Boolean> qcStatus, String bookId, String orderId) {
        List<String> qcErrors = this.getAllErrors(qcStatus);
        if (qcErrors.size() == 0) {
            this.updateQcStatus(bookId, orderId, QcStatus.QC_COMPLETED.getStatus());
        } else if (qcErrors.size() > 0) {
            this.updateQcStatus(bookId, orderId, QcStatus.QC_ERROR.getStatus());
        }
    }

    private void updateQcStatus(String bookId, String userId, String qcStatus) {
        UpdateQcStatusResource resource = new UpdateQcStatusResource();
        resource.setBookID(bookId);
        resource.setQCStatus(qcStatus);
        resource.setUserID(userId);

        HttpEntity<UpdateQcStatusResource> requestEntity = new HttpEntity<>(resource);
        ResponseEntity<String> res = null;
        try {
            res = RestUtils.exchange(getQcStatusUrl, HttpMethod.PUT, requestEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error occurred while updating QCStatus in BE" + e);
        }
    }

    private List<String> getAllErrors(Map<String, Boolean> qcStatus) {
        List<String> failedList = new ArrayList<>();
        Set<Map.Entry<String, Boolean>> entries = qcStatus.entrySet();
        for (Map.Entry<String, Boolean> entry : entries) {
            if (entry.getValue() == true) {
            } else {
                failedList.add(entry.getKey());
            }
        }
        return failedList;
    }

    @Override
    public List<QcStatusInfo> getAllQcStatusInfo() {
        return qcManagementRepository.getAllQcStatusInfo();
    }

    private String onlineOrOfflineOrder(OpsProduct opsProduct) {
        return opsProduct.getOrderDetails().getSupplierType().getSupplierType();
    }
}
