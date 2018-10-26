package com.coxandkings.travel.operations.service.qcmanagement.impl;

import com.coxandkings.travel.operations.enums.amendclientcommercials.BEInboundOperation;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEOperationId;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEServiceUri;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsBookingStatus;
import com.coxandkings.travel.operations.model.core.OpsOrderStatus;
import com.coxandkings.travel.operations.service.qcmanagement.QcBookingStatusService;
import com.coxandkings.travel.operations.utils.BookingEngineElasticData;
import com.coxandkings.travel.operations.utils.xml.XMLUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

@Service
public class QcBookingStatusServiceImpl implements QcBookingStatusService {
    @Autowired
    private BookingEngineElasticData bookingEngineElasticData;

    public Boolean doQcCheckOpsBookingStatusWithSI(OpsBooking opsBooking) {
        Boolean qcValue = false;
        Integer siBookingStatus = getSIBookingStatus(opsBooking);
        if (siBookingStatus != null) {
            if (siBookingStatus == 0 && opsBooking.getStatus().getBookingStatus().equals(OpsBookingStatus.RQ.getBookingStatus())) {
                qcValue = true;
            } else if (siBookingStatus == 1 && opsBooking.getStatus().getBookingStatus().equals(OpsBookingStatus.CNF.getBookingStatus())) {
                qcValue = true;
            } else if (siBookingStatus == -1 && opsBooking.getStatus().getBookingStatus().equals(OpsBookingStatus.RQ.getBookingStatus())) {
                qcValue = true;
            }
        } else
            qcValue = false;

        return qcValue;
    }

    private Integer getSIBookingStatus(OpsBooking opsBooking) {
        Integer accoStatus = null;
        Integer airStatus = null;
        try {
            accoStatus = this.qcCheckForAccBookingStatus(opsBooking);
            airStatus = this.qcCheckForAirBookingStatus(opsBooking);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this.checkAllOrderStatuses(accoStatus, airStatus);
    }

    /**
     * purpose of this method is to check each order status
     * 1)If Both order are Zero means booking status is On Request
     * 2)If Both order are One means booking status is Confirmed
     * 3)If Both order are -1 means booking status is OnRequest
     * 4)if one order status is 0 and another order status is 1 means booking status is OnRequest
     * 5)except these all are on request
     *
     * @param accoStatus
     * @param airStatus
     * @return
     */
    private Integer checkAllOrderStatuses(Integer accoStatus, Integer airStatus) {
        if (accoStatus != null && airStatus != null) {
            if (accoStatus == 0 && airStatus == 0)
                return 0;
            else if (accoStatus == 1 && airStatus == 1)
                return 1;
            else if (accoStatus == -1 && airStatus == -1)
                return -1;
            else
                return 0;
        } else
            return null;
    }

    private Integer qcCheckForAccBookingStatus(OpsBooking opsBooking) throws JSONException {
        Integer orderStatus = null;
        JSONArray accoOrders = new JSONArray();
        Element rootElement = this.consumeSupplierResponseForAcco(opsBooking);
        if (rootElement != null) {
            Element[] resWrapper = XMLUtils.getElementsAtXPath(rootElement, "./accoi:ResponseBody/acco:OTA_HotelResRSWrapper");
            for (Element wrapperElem : resWrapper) {
                JSONObject accoInfo = new JSONObject();
                //validateSuppRes(wrapperElem, resBodyJson);
                String supplierId = XMLUtils.getValueAtXPath(wrapperElem, "./acco:SupplierID");
                //String uniqueId = XMLUtils.getValueAtXPath(wrapperElem, "./ota:OTA_HotelResRS/ota:HotelReservations/ota:HotelReservation/ota:UniqueID[@Type='14']/@ID").toString();
                String status = XMLUtils.getFirstElementAtXPath(wrapperElem, "./ota:OTA_HotelResRS/ota:HotelReservations/ota:HotelReservation").getAttribute("ResStatus");
                if (!StringUtils.isEmpty(supplierId)) {
                    accoInfo.put("supplierId", supplierId);
                    if (!StringUtils.isEmpty(status)) {
                        if (status.equalsIgnoreCase("CONF")) {
                            accoInfo.put("status", OpsOrderStatus.OK.getProductStatus());
                        } else if (status.equalsIgnoreCase("NOTCONF")) {
                            accoInfo.put("status", OpsOrderStatus.REJ.getProductStatus());
                        } else if (status.equalsIgnoreCase("RESERVED")) {
                            accoInfo.put("status", OpsOrderStatus.RQ.getProductStatus());
                        }
                    }
                    accoOrders.put(accoInfo);
                }
            }
            if (accoOrders.length() > 1) {
                Map<String, Integer> statusCountMap = this.getStatusCountForAcco(accoOrders);
                if (statusCountMap.size() > 0) {
                    if (statusCountMap.containsKey(OpsOrderStatus.OK.getProductStatus())
                            && statusCountMap.containsKey(OpsOrderStatus.REJ.getProductStatus())
                            && statusCountMap.containsKey(OpsOrderStatus.RQ.getProductStatus())) {
                        if (accoOrders.length() == statusCountMap.get(OpsOrderStatus.OK.getProductStatus())) {
                            orderStatus = 1;  //all air products are confirmed
                        } else if (accoOrders.length() == statusCountMap.get(OpsOrderStatus.REJ.getProductStatus())) {
                            orderStatus = -1; //all air products are rejected
                        } else if (accoOrders.length() == statusCountMap.get(OpsOrderStatus.RQ.getProductStatus())) {
                            orderStatus = 0; //on request
                        } else if (statusCountMap.get(OpsOrderStatus.OK.getProductStatus()) == statusCountMap.get(OpsOrderStatus.REJ.getProductStatus())
                                && statusCountMap.get(OpsOrderStatus.REJ.getProductStatus()) == statusCountMap.get(OpsOrderStatus.RQ.getProductStatus())) {
                            orderStatus = 0;
                        } else {
                            orderStatus = 0;
                        }
                    }
                }
            } else if (accoOrders.length() == 1) {
                JSONObject jsonObject = (JSONObject) accoOrders.get(0);
                if (jsonObject.getString("status").equalsIgnoreCase(OpsOrderStatus.OK.getProductStatus())) {
                    orderStatus = 1;
                } else if (jsonObject.getString("status").equalsIgnoreCase(OpsOrderStatus.REJ.getProductStatus())) {
                    orderStatus = -1;
                } else if (jsonObject.getString("status").equalsIgnoreCase(OpsOrderStatus.RQ.getProductStatus())) {
                    orderStatus = 0;
                }
            }
        }
        return orderStatus;
    }

    private Map<String, Integer> getStatusCountForAcco(JSONArray orders) throws JSONException {
        Map<String, Integer> countMap = new HashMap<>();
        Integer confCount = 0;
        Integer rejCount = 0;
        Integer onRequestCount = 0;
        for (int i = 0; i < orders.length(); i++) {
            JSONObject jsonObject = (JSONObject) orders.get(i);
            if (jsonObject.getString("status").equalsIgnoreCase(OpsOrderStatus.OK.getProductStatus())) {
                confCount++;
            } else if (jsonObject.getString("status").equalsIgnoreCase(OpsOrderStatus.REJ.getProductStatus())) {
                rejCount++;
            } else if (jsonObject.getString("status").equalsIgnoreCase(OpsOrderStatus.RQ.getProductStatus())) {
                onRequestCount++;
            }
        }
        countMap.put(OpsOrderStatus.REJ.getProductStatus(), rejCount);
        countMap.put(OpsOrderStatus.OK.getProductStatus(), confCount);
        countMap.put(OpsOrderStatus.RQ.getProductStatus(), onRequestCount);
        return countMap;
    }

    private Integer qcCheckForAirBookingStatus(OpsBooking opsBooking) throws JSONException {
        //o means on request
        //-1 rejected
        //1 confirmed
        Integer orderStatus = null;
        JSONArray airOrders = new JSONArray();
        Element rootElement = this.consumeSupplierResponseForAir(opsBooking);
        Element[] resWrapper = XMLUtils.getElementsAtXPath(rootElement, "./airi:ResponseBody/air:OTA_AirBookRSWrapper");
        for (Element wrapperElem : resWrapper) {
            JSONObject airInfo = new JSONObject();
            //validateSuppRes(wrapperElem, resBodyJson);
            String supplierId = XMLUtils.getValueAtXPath(wrapperElem, "./air:SupplierID");
            //if bookingRefId present means gds pnr is generated i.e booking is created
            String bookingRefId = XMLUtils.getValueAtXPath(wrapperElem, "./ota:OTA_AirBookRS/ota:AirReservation/ota:BookingReferenceID[@Type='14']/@ID").toString();
            if (!StringUtils.isEmpty(supplierId)) {
                airInfo.put("supplierId", supplierId);
                if (!StringUtils.isEmpty(bookingRefId)) {
                    airInfo.put("pnr", bookingRefId);
                    airInfo.put("status", OpsOrderStatus.OK.getProductStatus());
                } else if (StringUtils.isEmpty(bookingRefId)) {
                    airInfo.put("status", OpsOrderStatus.REJ.getProductStatus());
                }
                airOrders.put(airInfo);
            }
        }
        if (airOrders.length() > 1) {
            Map<String, Integer> statusCountMap = this.getStatusCountForAir(airOrders);
            if (statusCountMap.size() > 0) {
                if (statusCountMap.containsKey(OpsOrderStatus.OK.getProductStatus())
                        && statusCountMap.containsKey(OpsOrderStatus.REJ.getProductStatus())) {
                    if (airOrders.length() == statusCountMap.get(OpsOrderStatus.OK.getProductStatus())) {
                        orderStatus = 1;  //all air products are confirmed
                    } else if (airOrders.length() == statusCountMap.get(OpsOrderStatus.REJ.getProductStatus())) {
                        orderStatus = -1; //all air products are rejected
                    } else if (statusCountMap.get(OpsOrderStatus.OK.getProductStatus()) == statusCountMap.get(OpsOrderStatus.REJ.getProductStatus())) {
                        orderStatus = 0; //equal
                    } else {
                        orderStatus = 0;
                    }
                }
            }
        } else if (airOrders.length() == 1) {
            JSONObject jsonObject = (JSONObject) airOrders.get(0);
            if (jsonObject.getString("status").equalsIgnoreCase(OpsOrderStatus.OK.getProductStatus())) {
                orderStatus = 1;
            } else if (jsonObject.getString("status").equalsIgnoreCase(OpsOrderStatus.REJ.getProductStatus())) {
                orderStatus = -1;
            }
        }
        return orderStatus;
    }

    private Map<String, Integer> getStatusCountForAir(JSONArray orders) throws JSONException {
        Map<String, Integer> countMap = new HashMap<>();
        Integer confCount = 0;
        Integer rejCount = 0;
        for (int i = 0; i < orders.length(); i++) {
            JSONObject jsonObject = (JSONObject) orders.get(i);
            if (jsonObject.getString("status").equalsIgnoreCase(OpsOrderStatus.OK.getProductStatus())) {
                confCount++;
            } else if (jsonObject.getString("status").equalsIgnoreCase(OpsOrderStatus.REJ.getProductStatus())) {
                rejCount++;
            }
        }
        countMap.put(OpsOrderStatus.REJ.getProductStatus(), rejCount);
        countMap.put(OpsOrderStatus.OK.getProductStatus(), confCount);
        return countMap;
    }

    private Element consumeSupplierResponseForAcco(OpsBooking opsBooking) {
        Element jsonOfSupplierCommercials = null;
        try {
            jsonOfSupplierCommercials = bookingEngineElasticData.getXMLData(
                    opsBooking.getUserID(),
                    opsBooking.getTransactionID(),
                    opsBooking.getSessionID(),
                    BEInboundOperation.BOOK,
                    BEOperationId.SUPPLIER_INTEGRATION_RS,
                    BEServiceUri.HOTEL);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return jsonOfSupplierCommercials;
    }

    private Element consumeSupplierResponseForAir(OpsBooking opsBooking) {
        Element jsonOfSupplierCommercials = null;
        try {
            jsonOfSupplierCommercials = bookingEngineElasticData.getXMLData(
                    opsBooking.getUserID(),
                    opsBooking.getTransactionID(),
                    opsBooking.getSessionID(),
                    BEInboundOperation.BOOK,
                    BEOperationId.SUPPLIER_INTEGRATION_RS,
                    BEServiceUri.FLIGHT);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return jsonOfSupplierCommercials;
    }
}
