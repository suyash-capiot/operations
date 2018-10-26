package com.coxandkings.travel.operations.service.beconsumer;

import com.coxandkings.travel.ext.model.be.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.adapter.OpsBookingAdapter;
import com.coxandkings.travel.operations.utils.xml.XMLUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

@Service
public class BeToOpsBookingConverter {

    private static final char KEYSEPARATOR = '|';
    private static final Logger logger = LogManager.getLogger(BeToOpsBookingConverter.class);
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OpsBookingAdapter opsBookingAdapter;
    @Value(value = "${booking_engine.get.booking}")
    private String getBookingUrl;

    private static void addAccoSupplierClientCommTotalComm(JSONObject orderDetailsJson) {
        Map<String, JSONObject> suppCommTotalsMap = new HashMap<String, JSONObject>();
        Map<String, JSONObject> supptaxBrkUpTotalsMap = new HashMap<String, JSONObject>();
        Map<String, JSONObject> roomPrctaxBrkUpTotalsMap = new HashMap<String, JSONObject>();
        Map<String, JSONObject> clientCommTotalsMap = new HashMap<String, JSONObject>();
        Map<String, JSONObject> companyTaxTotalsMap = new HashMap<String, JSONObject>();
        Map<String, JSONObject> discountTotalsMap = new HashMap<String, JSONObject>();
        JSONArray suppCommTotalsJsonArr = new JSONArray();
        JSONArray clientEntityTotalCommArr = new JSONArray();
        JSONObject roomObjectJson = orderDetailsJson.getJSONObject("hotelDetails");


        BigDecimal totalroomSuppInfoAmt = new BigDecimal(0);
        BigDecimal totalroomPriceAmt = new BigDecimal(0);
        BigDecimal totalroomTaxAmt = new BigDecimal(0);
        BigDecimal totalroomSuppTaxAmt = new BigDecimal(0);
        JSONObject totalRoomSuppPriceInfo = new JSONObject();
        JSONObject totalpriceInfo = new JSONObject();
        JSONObject companyTaxesTotal = new JSONObject();
        JSONObject discountsTotal = new JSONObject();

        for (Object roomConfig : roomObjectJson.getJSONArray("rooms")) {//roomConfig length

            //Calculate total roomSuppPriceInfo
            JSONObject roomSuppInfo = ((JSONObject) roomConfig).getJSONObject("supplierPriceInfo");
            BigDecimal[] suppRoomTotals = getTotalTaxesV2(roomSuppInfo, totalRoomSuppPriceInfo, supptaxBrkUpTotalsMap, totalroomSuppInfoAmt, totalroomSuppTaxAmt);
            totalroomSuppInfoAmt = suppRoomTotals[0];
            totalroomSuppTaxAmt = suppRoomTotals[1];

            //Calculate total roomPriceInfo
            JSONObject roompriceInfo = ((JSONObject) roomConfig).getJSONObject("totalPriceInfo");
            BigDecimal[] roomTotals = getTotalTaxesV2(roompriceInfo, totalpriceInfo, roomPrctaxBrkUpTotalsMap, totalroomPriceAmt, totalroomTaxAmt);
            totalroomPriceAmt = roomTotals[0];
            totalroomTaxAmt = roomTotals[1];

            // Calculate total company taxes
            JSONObject companyTaxesObj = roompriceInfo.optJSONObject("companyTaxes");
            if (companyTaxesObj != null) {
                getTotalCompanyTaxes(companyTaxesObj, totalpriceInfo, companyTaxesTotal, companyTaxTotalsMap);
            }

            // Calculate total discounts
            JSONObject discountsObj = roompriceInfo.optJSONObject("discounts");
            if (discountsObj != null) {
                getTotalDiscounts(discountsObj, totalpriceInfo, discountsTotal, discountTotalsMap);
            }

            //ADD SUPPLIER COMMERCIALS
            JSONArray suppCommJsonArr = ((JSONObject) roomConfig).optJSONArray("supplierCommercials");

            // If no supplier commercials have been defined in BRMS, the JSONArray for suppCommJsonArr will be null.
            // In this case, log a message and proceed with other calculations.
            if (suppCommJsonArr == null) {
                logger.warn("No supplier commercials found");
            } else {
                for (int j = 0; j < suppCommJsonArr.length(); j++) {
                    JSONObject suppCommJson = suppCommJsonArr.getJSONObject(j);
                    String suppCommName = suppCommJson.getString("commercialName");
                    JSONObject suppCommTotalsJson = null;
                    if (suppCommTotalsMap.containsKey(suppCommName)) {
                        suppCommTotalsJson = suppCommTotalsMap.get(suppCommName);
                        suppCommTotalsJson.put("commercialAmount", suppCommTotalsJson.getBigDecimal("commercialAmount").add(suppCommJson.getBigDecimal("commercialAmount")));
                    } else {
                        suppCommTotalsJson = new JSONObject();
                        suppCommTotalsJson.put("commercialName", suppCommName);
                        suppCommTotalsJson.put("commercialCurrency", suppCommJson.optString("commercialCurrency"));
                        suppCommTotalsJson.put("commercialType", suppCommJson.getString("commercialType"));
                        suppCommTotalsJson.put("commercialAmount", suppCommJson.getBigDecimal("commercialAmount"));
                        suppCommTotalsMap.put(suppCommName, suppCommTotalsJson);
                    }
                }

            }

					/*Iterator<Entry<String, JSONObject>> suppCommTotalsIter = suppCommTotalsMap.entrySet().iterator();
					while (suppCommTotalsIter.hasNext()) {
						suppCommTotalsJsonArr.put(suppCommTotalsIter.next().getValue());
					}*/


            //ADDING CLIENTCOMMTOTAL
            JSONArray clientCommJsonArr = ((JSONObject) roomConfig).optJSONArray("clientCommercials");
            if (clientCommJsonArr == null) {
                logger.warn("No client commercials found");
            } else {
                //for each entity in clientcomm add a new object in clientEntityTotalCommercials Array
                for (int j = 0; j < clientCommJsonArr.length(); j++) {
                    JSONObject clientCommJson = clientCommJsonArr.getJSONObject(j);
                    String entityName = clientCommJson.getString("entityName");
                    JSONArray clntCommArr = clientCommJson.getJSONArray("clientCommercials");
                    JSONObject clientCommTotalsJson = null;


                    //Add the commercial type inside the markUpCommercialDetails obj into clientCommTotalsMap and calculate total of each entity
                    for (int k = 0; k < clntCommArr.length(); k++) {
                        JSONObject clntCommJsonObj = clntCommArr.getJSONObject(k);
                        String clientCommName = clntCommArr.getJSONObject(k).getString("commercialName");

                        if (clientCommTotalsMap.containsKey(clientCommName.concat(entityName))) {
                            clientCommTotalsJson = clientCommTotalsMap.get(clientCommName.concat(entityName));
                            clientCommTotalsJson.put("commercialAmount", clientCommTotalsJson.getBigDecimal("commercialAmount").add(clntCommJsonObj.getBigDecimal("commercialAmount")));
                        } else {
                            clientCommTotalsJson = new JSONObject();
                            clientCommTotalsJson.put("commercialName", clientCommName);
                            clientCommTotalsJson.put("commercialType", clntCommJsonObj.getString("commercialType"));
                            clientCommTotalsJson.put("commercialCurrency", clntCommJsonObj.optString("commercialCurrency"));
                            clientCommTotalsJson.put("commercialAmount", clntCommJsonObj.getBigDecimal("commercialAmount"));
                            clientCommTotalsMap.put(clientCommName.concat(entityName), clientCommTotalsJson);
                        }

                        //

                    }
                    JSONArray clientCommTotalsJsonArr = new JSONArray();
                    Iterator<Entry<String, JSONObject>> clientCommTotalsIter = clientCommTotalsMap.entrySet().iterator();
                    while (clientCommTotalsIter.hasNext()) {
                        clientCommTotalsJsonArr.put(clientCommTotalsIter.next().getValue());
                    }
                    if (!clientCommTotalsMap.containsKey(entityName)) {
                        JSONObject clientEntityTotalJson = new JSONObject();
                        clientEntityTotalJson.put("clientID", clientCommJson.getString("clientID"));
                        clientEntityTotalJson.put("parentClientID", clientCommJson.getString("parentClientID"));
                        clientEntityTotalJson.put("commercialEntityType", clientCommJson.getString("commercialEntityType"));
                        clientEntityTotalJson.put("commercialEntityID", clientCommJson.getString("commercialEntityID"));
                        clientCommTotalsMap.put(entityName, clientEntityTotalJson);
                        clientEntityTotalJson.put("clientCommercialsTotal", clientCommTotalsJsonArr);
                        clientEntityTotalCommArr.put(clientEntityTotalJson);
                    }


                }
            }
            //final total calculation at order level
					/*roomObjectJson.put(JSON_PROP_SUPPCOMMTOTALS, suppCommTotalsJsonArr);
					roomObjectJson.put(JSON_PROP_CLIENTENTITYTOTALCOMMS, clientEntityTotalCommArr);
					roomObjectJson.put(JSON_PROP_SUPPBOOKPRICE, totalRoomSuppPriceInfo);
					roomObjectJson.put(JSON_PROP_BOOKPRICE, totalpriceInfo);*/


        }
        Iterator<Entry<String, JSONObject>> suppCommTotalsIter = suppCommTotalsMap.entrySet().iterator();
        while (suppCommTotalsIter.hasNext()) {
            suppCommTotalsJsonArr.put(suppCommTotalsIter.next().getValue());
        }

        orderDetailsJson.put("orderSupplierCommercials", suppCommTotalsJsonArr);
        orderDetailsJson.put("orderClientCommercials", clientEntityTotalCommArr);
        orderDetailsJson.put("orderSupplierPriceInfo", totalRoomSuppPriceInfo);
        orderDetailsJson.put("orderTotalPriceInfo", totalpriceInfo);


    }

    private static void getTotalDiscounts(JSONObject roomdiscountsObj, JSONObject totalPriceInfoObj, JSONObject discountsTotalObj, Map<String, JSONObject> discountTotalsMap) {
        totalPriceInfoObj.put("discounts", discountsTotalObj);
        discountsTotalObj.put("amount", discountsTotalObj.optBigDecimal("amount", new BigDecimal(0)).add(roomdiscountsObj.getBigDecimal("amount")));
        //discountsTotalObj.put("currencyCode", roomdiscountsObj.getString("currencyCode"));
        JSONArray discountArr = roomdiscountsObj.optJSONArray("discount");
        if (discountArr != null) {
            for (int t = 0; t < discountArr.length(); t++) {
                JSONObject discountBrkUpJson = discountArr.getJSONObject(t);
                String discountType = discountBrkUpJson.getString("discountType");
                JSONObject discountBrkUpTotalsJson = null;
                if (discountTotalsMap.containsKey(discountType)) {
                    discountBrkUpTotalsJson = discountTotalsMap.get(discountType);
                    discountBrkUpTotalsJson.put("amount", discountBrkUpTotalsJson.getBigDecimal("amount").add(discountBrkUpJson.getBigDecimal("amount")));
                } else {
                    discountBrkUpTotalsJson = new JSONObject();
                    discountBrkUpTotalsJson.put("amount", discountBrkUpJson.getBigDecimal("amount"));
                    discountBrkUpTotalsJson.put("discountType", discountBrkUpJson.getString("discountType"));
                    discountBrkUpTotalsJson.put("currencyCode", discountBrkUpJson.getString("currencyCode"));
                    discountTotalsMap.put(discountType, discountBrkUpTotalsJson);
                }

            }

            JSONArray discountTotalArr = new JSONArray();
            Iterator<Entry<String, JSONObject>> discountIter = discountTotalsMap.entrySet().iterator();
            while (discountIter.hasNext()) {
                discountTotalArr.put(discountIter.next().getValue());
            }
            discountsTotalObj.put("discount", discountTotalArr);
        }


    }

    private static void getTotalCompanyTaxes(JSONObject roomCompanytTaxObj, JSONObject totalPriceInfoObj, JSONObject companyTaxesTotalObj, Map<String, JSONObject> companyTaxTotalsMap) {

        totalPriceInfoObj.put("companyTaxes", companyTaxesTotalObj);
        companyTaxesTotalObj.put("amount", companyTaxesTotalObj.optBigDecimal("amount", new BigDecimal(0)).add(roomCompanytTaxObj.getBigDecimal("amount")));
        companyTaxesTotalObj.put("currencyCode", roomCompanytTaxObj.getString("currencyCode"));
        JSONArray companyTaxBreakUpArr = roomCompanytTaxObj.optJSONArray("companyTax");
        if (companyTaxBreakUpArr != null) {
            for (int t = 0; t < companyTaxBreakUpArr.length(); t++) {
                JSONObject taxBrkUpJson = companyTaxBreakUpArr.getJSONObject(t);
                String taxCode = taxBrkUpJson.getString("taxCode");
                JSONObject taxBrkUpTotalsJson = null;
                if (companyTaxTotalsMap.containsKey(taxCode)) {
                    taxBrkUpTotalsJson = companyTaxTotalsMap.get(taxCode);
                    taxBrkUpTotalsJson.put("amount", taxBrkUpTotalsJson.getBigDecimal("amount").add(taxBrkUpJson.getBigDecimal("amount")));
                } else {
                    taxBrkUpTotalsJson = new JSONObject();
                    taxBrkUpTotalsJson.put("amount", taxBrkUpJson.getBigDecimal("amount"));
                    taxBrkUpTotalsJson.put("taxCode", taxBrkUpJson.getString("taxCode"));
                    taxBrkUpTotalsJson.put("currencyCode", taxBrkUpJson.getString("currencyCode"));
                    taxBrkUpTotalsJson.put("taxPercent", taxBrkUpJson.getBigDecimal("taxPercent"));
                    taxBrkUpTotalsJson.put("hsnCode", taxBrkUpJson.getString("hsnCode"));
                    taxBrkUpTotalsJson.put("sacCode", taxBrkUpJson.getString("sacCode"));
                    companyTaxTotalsMap.put(taxCode, taxBrkUpTotalsJson);
                }

            }

            JSONArray companyTotaltaxBrkUpArr = new JSONArray();
            Iterator<Entry<String, JSONObject>> taxIter = companyTaxTotalsMap.entrySet().iterator();
            while (taxIter.hasNext()) {
                companyTotaltaxBrkUpArr.put(taxIter.next().getValue());
            }
            companyTaxesTotalObj.put("companyTax", companyTotaltaxBrkUpArr);
        }

    }

    private static BigDecimal[] getTotalTaxesV2(JSONObject roomSuppInfo, JSONObject totalPriceInfo, Map<String, JSONObject> taxBrkUpTotalsMap, BigDecimal totalInfoAmt, BigDecimal totalTaxAmt) {
        BigDecimal amount = roomSuppInfo.has("supplierPrice") ? roomSuppInfo.getBigDecimal("supplierPrice") : roomSuppInfo.getBigDecimal("totalPrice");
        String priceKey = roomSuppInfo.has("supplierPrice") ? "supplierPrice" : "totalPrice";
        totalInfoAmt = totalInfoAmt.add(amount);
        totalPriceInfo.put(priceKey, totalInfoAmt);
        totalPriceInfo.put("currencyCode", roomSuppInfo.getString("currencyCode"));
        JSONObject totaltaxes = new JSONObject();
        JSONObject taxes = roomSuppInfo.getJSONObject("taxes");
        totalTaxAmt = totalTaxAmt.add(taxes.optBigDecimal("amount", new BigDecimal(0)));
        totaltaxes.put("amount", totalTaxAmt);
        totaltaxes.put("currencyCode", taxes.optString("currencyCode"));
        JSONArray taxBreakUpArr = taxes.optJSONArray("tax");
        if (taxBreakUpArr != null) {
            for (int t = 0; t < taxBreakUpArr.length(); t++) {
                JSONObject taxBrkUpJson = taxBreakUpArr.getJSONObject(t);
                String taxCode = taxBrkUpJson.getString("taxCode");
                JSONObject taxBrkUpTotalsJson = null;
                if (taxBrkUpTotalsMap.containsKey(taxCode)) {
                    taxBrkUpTotalsJson = taxBrkUpTotalsMap.get(taxCode);
                    taxBrkUpTotalsJson.put("amount", taxBrkUpTotalsJson.getBigDecimal("amount").add(taxBrkUpJson.getBigDecimal("amount")));
                } else {
                    taxBrkUpTotalsJson = new JSONObject();
                    taxBrkUpTotalsJson.put("amount", taxBrkUpJson.getBigDecimal("amount"));
                    taxBrkUpTotalsJson.put("taxCode", taxBrkUpJson.getString("taxCode"));
                    taxBrkUpTotalsJson.put("currencyCode", taxBrkUpJson.getString("currencyCode"));
                    taxBrkUpTotalsMap.put(taxCode, taxBrkUpTotalsJson);
                }

            }

            JSONArray TotaltaxBrkUpArr = new JSONArray();
            Iterator<Entry<String, JSONObject>> taxIter = taxBrkUpTotalsMap.entrySet().iterator();
            while (taxIter.hasNext()) {
                TotaltaxBrkUpArr.put(taxIter.next().getValue());
            }
            totaltaxes.put("tax", TotaltaxBrkUpArr);
        }
        totalPriceInfo.put("taxes", totaltaxes);

        return new BigDecimal[]{totalInfoAmt, totalTaxAmt};
    }

    private void updateOrderIds(OpsBooking opsBooking, OpsBooking updatedOpsBooking, String orderId) {

        OpsOrderDetails opsOrder = opsBooking.getProducts().parallelStream().filter(order -> order.getOrderID().equals(orderId)).findAny().get().getOrderDetails();
        OpsOrderDetails updatedOpsOrder = updatedOpsBooking.getProducts().parallelStream().filter(order -> order.getOrderID().equals(orderId)).findAny().get().getOrderDetails();

        //updating order level supplier comm ids
        Map<String, OpsOrderSupplierCommercial> supplierCommercialMap = new HashMap<String, OpsOrderSupplierCommercial>();
        for (OpsOrderSupplierCommercial supplierCommercial : opsOrder.getSupplierCommercials()) {
            supplierCommercialMap.put(String.format("%s_%s",supplierCommercial.getCommercialName(),supplierCommercial.getSupplierID()), supplierCommercial);
        }

        for (OpsOrderSupplierCommercial supplierCommercial : updatedOpsOrder.getSupplierCommercials()) {
        	String commercialKey=String.format("%s_%s",supplierCommercial.getCommercialName(),supplierCommercial.getSupplierID());
        	OpsOrderSupplierCommercial originalSupplierCommercial=supplierCommercialMap.getOrDefault(commercialKey, null);
        	if(originalSupplierCommercial==null) {
        		//TODO log exception, Revised Commercials contain additional commercial which was not applied during booking
        		continue;
        	}

            supplierCommercial.setSuppCommId(originalSupplierCommercial.getSuppCommId());
            supplierCommercial.setEligible(true);
            supplierCommercialMap.remove(commercialKey);
        }

        //updating order level client comm ids
        Map<String, OpsOrderClientCommercial> clientCommercialMap = new HashMap<String, OpsOrderClientCommercial>();

        for (OpsOrderClientCommercial clientCommercial : opsOrder.getClientCommercials()) {
            clientCommercialMap.put(String.format("%s|%s", clientCommercial.getCommercialName(), clientCommercial.getCommercialEntityID()), clientCommercial);
        }

        for (OpsOrderClientCommercial clientCommercial : updatedOpsOrder.getClientCommercials()) {
        	String commercialKey=String.format("%s|%s", clientCommercial.getCommercialName(), clientCommercial.getCommercialEntityID());
        	OpsOrderClientCommercial originalCommercial=clientCommercialMap.getOrDefault(commercialKey,null);
        	if(originalCommercial==null) {
        		//TODO log exception, Revised Commercials contain additional commercial which was not applied during booking
        		continue;
        	}
            clientCommercial.setClientCommId(originalCommercial.getClientCommId());
            clientCommercial.setEligible(true);
            clientCommercialMap.remove(commercialKey);
           
        }
        
        //adding inelgible commercials
        
        supplierCommercialMap.forEach((key,value) -> {
        	value.setEligible(false);
        	updatedOpsOrder.getSupplierCommercials().add(value);
        	
        });
        
        clientCommercialMap.forEach((key,value) -> {
        	value.setEligible(false);
        	updatedOpsOrder.getClientCommercials().add(value);
        	
        });

    }

    public OpsBooking updateAccoOrder(JSONObject beRepriceRq, JSONObject beResponseJson, String bookId, String orderId, String roomId) throws OperationException {

        OrderDetails beOrder = null;
        JSONObject beOrderDetailsJson = null;
        OpsBooking updatedOpsBooking = null;

        Booking beBooking = getBEBooking(bookId);
        OpsBooking opsBooking = getOpsBookingFromBEBooking(beBooking);
        Optional<OpsProduct> opsProductOpt = opsBooking.getProducts().parallelStream().filter(orders -> orders.getOrderID().equals(orderId)).findAny();
        if (!opsProductOpt.isPresent()) {
            throw new OperationException(Constants.ORDER_NOT_FOUND, orderId, bookId);
        }

        Optional<OpsRoom> opsRoomOpt = opsProductOpt.get().getOrderDetails().getHotelDetails().getRooms().parallelStream().filter(room -> room.getRoomID().equals(roomId)).findAny();
        if (!opsRoomOpt.isPresent()) {
            throw new OperationException(Constants.ROOM_NOT_FOUND, roomId);
        }

        JSONObject updatedRoomJson = getRoomFromBEResponse(beRepriceRq, beResponseJson, opsProductOpt.get(), opsRoomOpt.get());
        logger.info("Updated Room with Amended Commercials" + updatedRoomJson.toString());

        beOrder = beBooking.getBookingResponseBody().getProducts().parallelStream().filter(product -> product.getOrderID().equals(orderId)).findAny().get()
                .getOrderDetails();
        Room beRoom = beOrder.getHotelDetails().getRooms().parallelStream().filter(room -> room.getRoomID().equals(roomId)).findAny().get();
        updateBERoom(beRoom, updatedRoomJson);
        try {
            beOrderDetailsJson = new JSONObject(objectMapper.writeValueAsString(beOrder));
        } catch (JSONException | JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.ORDER_UPDATE_FAILED);
        }
        logger.info("Updated Room In Existing Order" + beOrderDetailsJson.toString());
        addAccoSupplierClientCommTotalComm(beOrderDetailsJson);
        logger.info("Updated Order Level Totals" + beOrderDetailsJson.toString());
        updateAccoBEOrder(beOrder, beOrderDetailsJson);
        try {
            logger.info("Updated Booking : " + objectMapper.writeValueAsString(beBooking));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        updatedOpsBooking = getOpsBookingFromBEBooking(beBooking);
        updateOrderIds(opsBooking, updatedOpsBooking, orderId);
        return updatedOpsBooking;
    }

    public OpsBooking updateAirOrder(JSONObject bERepriceRq, JSONObject beResponseJson, String bookId,
                                     String orderId, String paxType) throws OperationException {


        OrderDetails BEOrder = null;
        JSONObject BEOrderDetailsJson = null;
        OpsBooking updatedOpsBooking = null;


        Booking BEBooking = getBEBooking(bookId);
        OpsBooking opsBooking = getOpsBookingFromBEBooking(BEBooking);
        Optional<OpsProduct> opsProductOpt = opsBooking.getProducts().parallelStream().filter(orders -> orders.getOrderID().equals(orderId)).findAny();
        if (!opsProductOpt.isPresent()) {
            throw new OperationException(Constants.ORDER_NOT_FOUND, orderId, bookId);
        }

        BEOrderDetailsJson = getOrderFromBEResponse(beResponseJson, opsProductOpt.get());
        logger.info("Updated Priced Itenerary from Booking Engine" + BEOrderDetailsJson);
        BEOrder = BEBooking.getBookingResponseBody().getProducts().parallelStream().filter(product -> product.getOrderID().equals(orderId)).findAny().get()
                .getOrderDetails();
        updateAirBEOrder(BEOrder, BEOrderDetailsJson);
        updatedOpsBooking = getOpsBookingFromBEBooking(BEBooking);
        updateOrderIds(opsBooking, updatedOpsBooking, orderId);
        return updatedOpsBooking;
    }

    private OpsBooking getOpsBookingFromBEBooking(Booking BEBooking) throws OperationException {

        OpsBooking opsBooking = null;

        opsBooking = opsBookingAdapter.getOpsBooking(BEBooking);


        return opsBooking;
    }

    private Booking getBEBooking(String bookingId) throws OperationException {
        Booking BEBooking = null;

        String parameterizedGetBooking = getBookingUrl + bookingId;
        String bookingDetails = RestUtils.getForObject(parameterizedGetBooking, String.class);
        if (bookingDetails.contains("BE_ERR_001")) {
            throw new OperationException(Constants.DB_BOOKING_GET_ERROR);
        }
        try {
            BEBooking = objectMapper.readValue(bookingDetails, Booking.class);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.DB_BOOKING_GET_ERROR);
        }


        return BEBooking;
    }

    private void updateAccoBEOrder(OrderDetails BEOrder, JSONObject BEOrderDetailsJson) throws OperationException {

        try {
            BEOrder.setOrderSupplierCommercials(objectMapper.readValue(BEOrderDetailsJson.getJSONArray("orderSupplierCommercials").toString(), new TypeReference<List<OrderSupplierCommercial>>() {
            }));

            JSONArray clientCommArray = new JSONArray();
            JSONArray orderClientCommercialsArr = BEOrderDetailsJson.getJSONArray("orderClientCommercials");
            for (int i = 0; i < orderClientCommercialsArr.length(); i++) {
                JSONObject orderClientCommercial = orderClientCommercialsArr.getJSONObject(i);
                JSONArray entityCommJsonArr = orderClientCommercial.getJSONArray("clientCommercialsTotal");
                for (int j = 0; j < entityCommJsonArr.length(); j++) {
                    JSONObject clientEntityComm = entityCommJsonArr.getJSONObject(j);
                    JSONObject clientCommTotalJson = new JSONObject();

                    clientCommTotalJson.put("commercialName", clientEntityComm.getString("commercialName"));
                    clientCommTotalJson.put("commercialType", clientEntityComm.getString("commercialType"));

                    clientCommTotalJson.put("commercialAmount", clientEntityComm.getBigDecimal("commercialAmount"));
                    clientCommTotalJson.put("commercialCurrency", clientEntityComm.getString("commercialCurrency"));
                    clientCommTotalJson.put("clientID", orderClientCommercial.getString("clientID"));
                    clientCommTotalJson.put("parentClientID", orderClientCommercial.getString("parentClientID"));
                    clientCommTotalJson.put("commercialEntityID", orderClientCommercial.getString("commercialEntityID"));
                    clientCommTotalJson.put("commercialEntityType", orderClientCommercial.getString("commercialEntityType"));
                    clientCommTotalJson.put("companyFlag", i == 0 ? true : false);

                    //clientCommJson.put(JSON_PROP_RECIEPTNUMBER, clientComm.getRecieptNumber());
                    //clientCommJson.put(JSON_PROP_INVOICENUMBER, clientComm.getInVoiceNumber());
                    clientCommArray.put(clientCommTotalJson);
                }

            }
            BEOrder.setOrderClientCommercials(objectMapper.readValue(clientCommArray.toString(), new TypeReference<List<OrderClientCommercial>>() {
            }));
            BEOrder.setOrderSupplierPriceInfo(objectMapper.readValue(BEOrderDetailsJson.getJSONObject("orderSupplierPriceInfo").toString(), OrderSupplierPriceInfo.class));
            BEOrder.setOrderTotalPriceInfo(objectMapper.readValue(BEOrderDetailsJson.getJSONObject("orderTotalPriceInfo").toString(), OrderTotalPriceInfo.class));
        } catch (JSONException | IOException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.ORDER_UPDATE_FAILED);
        }

    }

    private void updateAirBEOrder(OrderDetails BEOrder, JSONObject pricedItineraryJson) throws OperationException {

        try {
            JSONObject supplierItinPricingInfoJson = pricedItineraryJson.getJSONObject("supplierItineraryPricingInfo");
            //Setting Order Supplier Commercials
            BEOrder.setOrderSupplierCommercials(objectMapper.readValue(supplierItinPricingInfoJson.getJSONArray("supplierCommercialsTotals").toString(), new TypeReference<List<OrderSupplierCommercial>>() {
            }));
            // Setting Order Client Commercials
            JSONArray clientCommArray = new JSONArray();
            JSONArray orderClientCommercialsArr = pricedItineraryJson.getJSONArray("clientEntityTotalCommercials");
            for (int i = 0; i < orderClientCommercialsArr.length(); i++) {
                JSONObject orderClientCommercial = orderClientCommercialsArr.getJSONObject(i);
                JSONArray entityCommJsonArr = orderClientCommercial.getJSONArray("clientCommercialsTotal");
                for (int j = 0; j < entityCommJsonArr.length(); j++) {
                    JSONObject clientEntityComm = entityCommJsonArr.getJSONObject(j);
                    JSONObject clientCommTotalJson = new JSONObject();

                    clientCommTotalJson.put("commercialName", clientEntityComm.getString("commercialName"));
                    clientCommTotalJson.put("commercialType", clientEntityComm.getString("commercialType"));

                    clientCommTotalJson.put("commercialAmount", clientEntityComm.getBigDecimal("commercialAmount"));
                    clientCommTotalJson.put("commercialCurrency", clientEntityComm.getString("commercialCurrency"));
                    clientCommTotalJson.put("clientID", orderClientCommercial.getString("clientID"));
                    clientCommTotalJson.put("parentClientID", orderClientCommercial.getString("parentClientID"));
                    clientCommTotalJson.put("commercialEntityID", orderClientCommercial.getString("commercialEntityID"));
                    clientCommTotalJson.put("commercialEntityType", orderClientCommercial.getString("commercialEntityType"));
                    clientCommTotalJson.put("companyFlag", i == 0 ? true : false);

                    //clientCommJson.put(JSON_PROP_RECIEPTNUMBER, clientComm.getRecieptNumber());
                    //clientCommJson.put(JSON_PROP_INVOICENUMBER, clientComm.getInVoiceNumber());
                    clientCommArray.put(clientCommTotalJson);
                }

            }
            BEOrder.setOrderClientCommercials(objectMapper.readValue(clientCommArray.toString(), new TypeReference<List<OrderClientCommercial>>() {
            }));
            //Setting Order Supplier Price Info
            BEOrder.getOrderSupplierPriceInfo().setSupplierPrice(supplierItinPricingInfoJson.getJSONObject("itinTotalFare").getBigDecimal("amount").toString());
            BEOrder.getOrderSupplierPriceInfo().setCurrencyCode(supplierItinPricingInfoJson.getJSONObject("itinTotalFare").getString("currencyCode"));
            BEOrder.getOrderSupplierPriceInfo().setPaxTypeFares(objectMapper.readValue(supplierItinPricingInfoJson.getJSONArray("paxTypeFares").toString(), new TypeReference<List<PaxTypeFare>>() {
            }));

            //Setting Order Total Price Info
            BEOrder.getOrderTotalPriceInfo().setTotalPrice(pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").getBigDecimal("amount").toString());
            BEOrder.getOrderTotalPriceInfo().setCurrencyCode(pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").getString("currencyCode"));
            BEOrder.getOrderTotalPriceInfo().setBaseFare(objectMapper.readValue(pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").getJSONObject("baseFare").toString(),
                    BaseFare.class));
            
            if(pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").has("fees"))
            	BEOrder.getOrderTotalPriceInfo().setFees(objectMapper.readValue(pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").getJSONObject("fees").toString(),
                    Fees.class));
            if(pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").has("taxes"))
            	BEOrder.getOrderTotalPriceInfo().setTaxes(objectMapper.readValue(pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").getJSONObject("taxes").toString(),
                    Taxes.class));
            if (pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").has("receivables")) {
            	BEOrder.getOrderTotalPriceInfo().setReceivables(objectMapper.readValue(pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").getJSONObject("receivables").toString(),
                        Receivables.class));
            }
            else {
            	BEOrder.getOrderTotalPriceInfo().setReceivables(null);
            }
            
            if (pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").has("discounts")) {
            	BEOrder.getOrderTotalPriceInfo().setDiscounts(objectMapper.readValue(pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").getJSONObject("discounts").toString(),
            			Discounts.class));
            }
            else {
            	BEOrder.getOrderTotalPriceInfo().setDiscounts(null);
            }
            
            
            if (pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").has("companyTaxes")) {
            	BEOrder.getOrderTotalPriceInfo().setCompanyTaxes(objectMapper.readValue(pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").getJSONObject("companyTaxes").toString(),
            			CompanyTaxes.class));
            }
            else {
            	BEOrder.getOrderTotalPriceInfo().setCompanyTaxes(null);
            }
            
            
            
            if (pricedItineraryJson.getJSONObject("airItineraryPricingInfo").has("incentives"))
            {
            	BEOrder.getOrderTotalPriceInfo().setIncentives(objectMapper.readValue(pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONObject("incentives").toString(),
                        Incentives.class));
            }
            else {
            	BEOrder.getOrderTotalPriceInfo().setIncentives(null);
            }
            
            	BEOrder.getOrderTotalPriceInfo().setPaxTypeFares(objectMapper.readValue(pricedItineraryJson.getJSONObject("airItineraryPricingInfo").getJSONArray("paxTypeFares").toString(), new TypeReference<List<PaxTypeFare>>() {
            }));

            JSONArray paxTypeClientEntityCommercials = pricedItineraryJson.getJSONArray("clientCommercialItinInfo");
            for (int j = 0; j < paxTypeClientEntityCommercials.length(); j++) {
                JSONObject paxTypeClientEntityCommercial = paxTypeClientEntityCommercials.getJSONObject(j);
                String paxType = paxTypeClientEntityCommercial.getString("paxType");
                PaxTypeFare paxTypeFare = BEOrder.getOrderTotalPriceInfo().getPaxTypeFares().parallelStream().filter(pax -> pax.getPaxType().equals(paxType)).findAny().get();
                paxTypeFare.setClientEntityCommercials(objectMapper.readValue(paxTypeClientEntityCommercial.getJSONArray("clientEntityCommercials").toString(), new TypeReference<List<ClientEntityCommercial>>() {
                }));

            }

        } catch (JSONException | IOException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.ORDER_UPDATE_FAILED);
        }

    }

    private Room updateBERoom(Room BERoom, JSONObject updatedRoomJson) throws OperationException {

        JSONObject supplierTotalPriceInfoJson = updatedRoomJson.getJSONObject("supplierTotalPriceInfo");
        BERoom.getSupplierPriceInfo().setSupplierPrice(supplierTotalPriceInfoJson.getBigDecimal("amount").toString());
        BERoom.getSupplierPriceInfo().setCurrencyCode(supplierTotalPriceInfoJson.getString("currencyCode"));
        JSONObject totalPriceInfo = updatedRoomJson.getJSONObject("totalPriceInfo");
        try {
            BERoom.getSupplierPriceInfo().setTaxes(objectMapper.readValue(supplierTotalPriceInfoJson.getJSONObject("taxes").toString(), Taxes.class));
            BERoom.setClientEntityCommercials(objectMapper.readValue(updatedRoomJson.getJSONArray("clientEntityCommercials").toString(), new TypeReference<List<ClientEntityCommercial>>() {
            }));
            BERoom.getTotalPriceInfo().setTotalPrice(totalPriceInfo.getBigDecimal("amount").toString());
            BERoom.getTotalPriceInfo().setCurrencyCode(totalPriceInfo.getString("currencyCode"));
            BERoom.getTotalPriceInfo().setTaxes(objectMapper.readValue(totalPriceInfo.getJSONObject("taxes").toString(), Taxes.class));
            
            if(totalPriceInfo.has("discounts")) {
            	BERoom.getTotalPriceInfo().setDiscounts(objectMapper.readValue(totalPriceInfo.getJSONObject("discounts").toString(), Discounts.class));
            }
            else {
            	BERoom.getTotalPriceInfo().setDiscounts(null);
            }
            
            if(totalPriceInfo.has("companyTaxes")) {
            	BERoom.getTotalPriceInfo().setCompanyTaxes(objectMapper.readValue(totalPriceInfo.getJSONObject("companyTaxes").toString(), CompanyTaxes.class));
            }
            else {
            	BERoom.getTotalPriceInfo().setCompanyTaxes(null);
            }
            
            if(totalPriceInfo.has("incentives")) {
            	BERoom.getTotalPriceInfo().setIncentives(objectMapper.readValue(totalPriceInfo.getJSONObject("incentives").toString(), Incentives.class));
            }
            else {
            	BERoom.getTotalPriceInfo().setIncentives(null);
            }
            
            
            if(totalPriceInfo.has("receivables")) {
            	BERoom.getTotalPriceInfo().setReceivables(objectMapper.readValue(totalPriceInfo.getJSONObject("receivables").toString(), Receivables.class));
            }
            else {
            	BERoom.getTotalPriceInfo().setReceivables(null);
            }
            
            
            BERoom.setSupplierCommercials(objectMapper.readValue(updatedRoomJson.getJSONArray("supplierCommercials").toString(), new TypeReference<List<SupplierCommercial>>() {
            }));
        } catch (JSONException | IOException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.ORDER_UPDATE_FAILED);
        }
        return BERoom;
    }

    private int getBEAccomodationInfoIndex(JSONObject BERepriceRq, OpsProduct opsOrder, OpsRoom opsRoom) throws OperationException {
        int idx = -1;
        String OpsKey = getOpsKeyForReq(opsOrder, opsRoom, opsOrder.getOrderDetails().getHotelDetails());
        try {
            JSONArray accommodationInfoJsonArr = BERepriceRq.getJSONObject("requestBody").getJSONArray("accommodationInfo");
            for (int i = 0; i < accommodationInfoJsonArr.length(); i++) {
                JSONObject accommodationInfoJson = accommodationInfoJsonArr.getJSONObject(i);
                JSONArray roomConfigJsonArr=accommodationInfoJson.getJSONArray("roomConfig");
                for(int j=0;j< roomConfigJsonArr.length();j++) {
                	JSONObject roomConfigJson=roomConfigJsonArr.getJSONObject(j);
                	accommodationInfoJson.put("supplierRef", roomConfigJson.getString("supplierRef"));
                	String BEKey = getBEKeyForReq(accommodationInfoJson);
                	if (BEKey.equals(OpsKey))
                    idx = i;
                	}
            }
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.ORDER_UPDATE_FAILED);
        }
        return idx;
    }

    private JSONObject getRoomFromBEResponse(JSONObject BERepriceRq, JSONObject beResponseJson, OpsProduct opsOrder, OpsRoom opsRoom) throws OperationException {
        JSONObject roomJson = null;
        int accommodationInfoIdx = getBEAccomodationInfoIndex(BERepriceRq, opsOrder, opsRoom);
        String opsRoomKey = getOpsKeyForRoomStay(opsRoom, opsOrder.getOrderDetails().getHotelDetails());
        try {
            JSONObject accommodationInfoJson = beResponseJson.getJSONObject("responseBody").getJSONArray("accommodationInfo").getJSONObject(accommodationInfoIdx);
            JSONArray roomStayJsonArr = accommodationInfoJson.getJSONArray("roomStay");
            for (int i = 0; i < roomStayJsonArr.length(); i++) {
                JSONObject roomStayJson = roomStayJsonArr.getJSONObject(i);
                String BERoomKey = getBEKeyForRoomStay(roomStayJson.getJSONObject("roomInfo"));
                if (opsRoomKey.equals(BERoomKey)) {
                    roomJson = roomStayJson;
                    return roomJson;
                }
            }

        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.ROOMSTAY_NOT_FOUND);
        }
        throw new OperationException(Constants.ROOMSTAY_NOT_FOUND);
    }

    private JSONObject getOrderFromBEResponse(JSONObject beResponseJson, OpsProduct opsOrder) throws OperationException {
        JSONObject pricedItineraryJson = null;
        JSONArray pricedItineraryJsonArr = beResponseJson.getJSONObject("responseBody").getJSONArray("pricedItinerary");
        String OpsKey = getOpsKeyForPricedItinerary(opsOrder);
        for (int i = 0; i < pricedItineraryJsonArr.length(); i++) {
            pricedItineraryJson = pricedItineraryJsonArr.getJSONObject(i);
            if (OpsKey.equals(getBEKeyForPricedItinerary(pricedItineraryJson)))
                return pricedItineraryJson;
        }
        throw new OperationException(Constants.PRICEDITINERARY_NOT_FOUND);
    }

    private String getBEKeyForReq(JSONObject subReqJson) {
        String requestKey = null;
        try {
            requestKey = String.format("%s%c%s%c%s%c%s%c%s", subReqJson.getString("supplierRef"), KEYSEPARATOR,
                    subReqJson.getString("countryCode"), KEYSEPARATOR, subReqJson.getString("cityCode"), KEYSEPARATOR,
                    subReqJson.getString("checkIn"), KEYSEPARATOR, subReqJson.getString("checkOut"));
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return requestKey;
    }

    public String getBEKeyForRoomStay(JSONObject roomInfoJson) {
        //TODO:should supplier ref be present or indexes/uuid should be used?
        //TODO:add req params here
        String roomKey = null;
        try {
            roomKey = String.format("%c%s%c%s%c%s%c%s%c%s", KEYSEPARATOR, roomInfoJson.getJSONObject("hotelInfo").getString("hotelCode"), KEYSEPARATOR,
                    roomInfoJson.getJSONObject("roomTypeInfo").getString("roomTypeCode"), KEYSEPARATOR, roomInfoJson.getJSONObject("roomTypeInfo").getString("roomCategoryCode"),
                    KEYSEPARATOR, roomInfoJson.getJSONObject("ratePlanInfo").getString("ratePlanCode"), KEYSEPARATOR, roomInfoJson.getJSONObject("roomTypeInfo").getString("roomRef"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return roomKey;
    }

    private String getOpsKeyForReq(OpsProduct opsOrder, OpsRoom opsRoom, OpsHotelDetails opsHotel) {
        String requestKey = null;
        requestKey = String.format("%s%c%s%c%s%c%s%c%s", opsOrder.getSupplierID(), KEYSEPARATOR,
                opsHotel.getCountryCode(), KEYSEPARATOR, opsHotel.getCityCode(), KEYSEPARATOR,
                opsRoom.getCheckIn(), KEYSEPARATOR, opsRoom.getCheckOut());
        return requestKey;
    }

    public String getOpsKeyForRoomStay(OpsRoom opsRoom, OpsHotelDetails opsHotel) {
        // TODO:should supplier ref be present or indexes/uuid should be used?
        // TODO:add req params here
        String roomKey = null;

        roomKey = String.format("%c%s%c%s%c%s%c%s%c%s", KEYSEPARATOR, opsHotel.getHotelCode(), KEYSEPARATOR,
                opsRoom.getRoomTypeInfo().getRoomTypeCode(), KEYSEPARATOR,
                opsRoom.getRoomTypeInfo().getRoomCategoryID(), KEYSEPARATOR,
                opsRoom.getRatePlanInfo().getRatePlanCode(), KEYSEPARATOR, opsRoom.getRoomTypeInfo().getRoomRef());

        return roomKey;
    }

    public String getBEKeyForPricedItinerary(JSONObject pricedItinJson) {
        StringBuilder strBldr = new StringBuilder(pricedItinJson.optString("supplierRef"));

        JSONObject airItinJson = pricedItinJson.optJSONObject("airItinerary");
        if (airItinJson != null) {
            JSONArray origDestOptsJsonArr = airItinJson.getJSONArray("originDestinationOptions");
            for (int j = 0; j < origDestOptsJsonArr.length(); j++) {
                JSONObject origDestOptJson = origDestOptsJsonArr.getJSONObject(j);
                strBldr.append('[');
                JSONArray flSegsJsonArr = origDestOptJson.optJSONArray("flightSegment");
                if (flSegsJsonArr == null) {
                    break;
                }

                for (int k = 0; k < flSegsJsonArr.length(); k++) {
                    JSONObject flSegJson = flSegsJsonArr.getJSONObject(k);
                    JSONObject opAirlineJson = flSegJson.getJSONObject("operatingAirline");
                    strBldr.append(opAirlineJson.getString("airlineCode").concat(opAirlineJson.getString("flightNumber")).concat("|"));
                }
                strBldr.setLength(strBldr.length() - 1);
                strBldr.append(']');
            }
        }
        return strBldr.toString();
    }

    public String getOpsKeyForPricedItinerary(OpsProduct opsOrder) {
        StringBuilder strBldr = new StringBuilder(opsOrder.getSupplierID());

        List<OpsOriginDestinationOption> originDestinationOptions = opsOrder.getOrderDetails().getFlightDetails().getOriginDestinationOptions();
        if (originDestinationOptions != null) {

            for (int j = 0; j < originDestinationOptions.size(); j++) {
                OpsOriginDestinationOption originDestinationOption = originDestinationOptions.get(j);
                strBldr.append('[');
                List<OpsFlightSegment> flightSegments = originDestinationOption.getFlightSegment();
                if (flightSegments == null) {
                    break;
                }

                for (int k = 0; k < flightSegments.size(); k++) {
                    OpsFlightSegment flightSegment = flightSegments.get(k);
                    OpsOperatingAirline operatingAirline = flightSegment.getOperatingAirline();
                    strBldr.append(operatingAirline.getAirlineCode().concat(operatingAirline.getFlightNumber()).concat("|"));
                }
                strBldr.setLength(strBldr.length() - 1);
                strBldr.append(']');
            }
        }
        return strBldr.toString();
    }


    public String getKeyFormSupplierPricingResponse(Element airPriceResponse, String supplierId) {

        StringBuilder stringBuilder = null;
        stringBuilder = new StringBuilder(supplierId);
        Element[] pricedItineraries = XMLUtils.getElementsAtXPath(airPriceResponse, "./ota:PricedItineraries");
        if(pricedItineraries!=null) {
            for(Element priceItenaryEle:pricedItineraries) {

                Element[] originDestinationOptions = XMLUtils.getElementsAtXPath(priceItenaryEle, "./ota:PricedItinerary/ota:AirItinerary/ota:OriginDestinationOptions/ota:OriginDestinationOption");
                if (originDestinationOptions != null) {
                    for (Element originDestinationOption : originDestinationOptions) {
                        stringBuilder.append('[');
                        Element[] flightSegments = XMLUtils.getElementsAtXPath(originDestinationOption, "./ota:FlightSegment");
                        for (Element flightSegment : flightSegments) {
                            Element codeAndFlightNumberRes = XMLUtils.getFirstElementAtXPath(flightSegment, "./ota:OperatingAirline");
                            String airlineCode = codeAndFlightNumberRes.getAttribute("Code");
                            String flightNumber = codeAndFlightNumberRes.getAttribute("FlightNumber");
                            stringBuilder.append(airlineCode.concat(flightNumber).concat("|"));

                        }

                        stringBuilder.setLength(stringBuilder.length() - 1);
                        stringBuilder.append(']');
                    }

                }
            }
        }
        return stringBuilder.toString();
    }
		
	    
	
}
