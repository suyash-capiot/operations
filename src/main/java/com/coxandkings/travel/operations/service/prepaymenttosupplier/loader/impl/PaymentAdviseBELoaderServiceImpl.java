package com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.impl;

import com.coxandkings.travel.operations.enums.prepaymenttosupplier.CommercialType;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsOrderSupplierCommercial;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import com.coxandkings.travel.operations.model.core.OpsRoomSuppCommercial;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviseBELoaderService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentAdviseBELoaderServiceImpl implements PaymentAdviseBELoaderService {


    @Autowired
    private OpsBookingService opsBookingService;


    JsonObjectProvider jsonObjectProvider = new JsonObjectProvider();
    private static Logger logger = LogManager.getLogger(PaymentAdviseBELoaderServiceImpl.class);

    @Override
    public BigDecimal getNetPayableToSupplier(String bookingId, String orderId) throws OperationException {
        BigDecimal sumReceivableAmount = new BigDecimal("0");
        BigDecimal sumPayableAmount = new BigDecimal("0");
        BigDecimal netPayableToSupplier = null;
        OpsProduct opsProduct = opsBookingService.getProduct(bookingId, orderId);
        if (opsProduct != null) {
            OpsProductCategory productCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
            if (productCategory != null) {
                OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(productCategory, opsProduct.getProductSubCategory());
                if (opsProductSubCategory != null) {
                    switch (productCategory) {
                        case PRODUCT_CATEGORY_TRANSPORTATION: {
                            switch (opsProductSubCategory) {
                                case PRODUCT_SUB_CATEGORY_FLIGHT: {
                                    BigDecimal suppPrice = new BigDecimal(opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getSupplierPrice());
                                    List<OpsOrderSupplierCommercial> flightSupplierCommercialList = opsProduct.getOrderDetails().getSupplierCommercials();

                                    for (OpsOrderSupplierCommercial flightSupplierCommercial : flightSupplierCommercialList) {
                                        if (flightSupplierCommercial.getCommercialType().equalsIgnoreCase(String.valueOf(CommercialType.RECEIVABLE))) {
                                            String afterCommercialTotalAmount = flightSupplierCommercial.getCommercialAmount();
                                            sumReceivableAmount = sumReceivableAmount.add(new BigDecimal(afterCommercialTotalAmount));
                                        }
                                        if (flightSupplierCommercial.getCommercialType().equalsIgnoreCase(String.valueOf(CommercialType.PAYABLE))) {
                                            String afterCommercialTotalAmount = flightSupplierCommercial.getCommercialAmount();
                                            sumPayableAmount = sumPayableAmount.add(new BigDecimal(afterCommercialTotalAmount));
                                        }

                                    }
                                    BigDecimal price = (suppPrice.add(sumPayableAmount)).subtract(sumReceivableAmount);
                                    netPayableToSupplier = price;
                                    break;
                                }
                                case PRODUCT_SUB_CATEGORY_HOTELS:
                                    break;
                            }
                        }

                        case PRODUCT_CATEGORY_ACCOMMODATION: {
                            switch (opsProductSubCategory) {
                                case PRODUCT_SUB_CATEGORY_HOTELS: {
                                    BigDecimal suppPrice = new BigDecimal("0");
                                    suppPrice = new BigDecimal(opsProduct.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getSupplierPrice());
                                    List<OpsRoom> opsRooms = opsProduct.getOrderDetails().getHotelDetails().getRooms();

                                    for (OpsRoom opsRoom : opsRooms) {
                                        List<OpsRoomSuppCommercial> roomSuppCommercials = opsRoom.getRoomSuppCommercials();
                                        for (OpsRoomSuppCommercial opsRoomSuppCommercial : roomSuppCommercials) {
                                            if (opsRoomSuppCommercial.getCommercialType().equalsIgnoreCase(String.valueOf(CommercialType.PAYABLE))) {
                                                Double afterCommercialTotalAmount = opsRoomSuppCommercial.getCommercialAmount();
                                                sumPayableAmount = sumPayableAmount.add(new BigDecimal(afterCommercialTotalAmount));
                                            } else if (opsRoomSuppCommercial.getCommercialType().equalsIgnoreCase("Receivable")) {
                                                Double afterCommercialTotalAmount = opsRoomSuppCommercial.getCommercialAmount();
                                                sumReceivableAmount = sumReceivableAmount.add(new BigDecimal(afterCommercialTotalAmount));
                                            }
                                        }
                                    }
                                    BigDecimal price = (suppPrice.add(sumPayableAmount)).subtract(sumReceivableAmount);
                                    netPayableToSupplier = price;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return netPayableToSupplier;
    }

    @Override
    public String getSupplierCurrency(String bookinRefId, String orderId) throws OperationException {
        String currrency = null;
        OpsProduct opsProduct = opsBookingService.getProduct(bookinRefId, orderId);
        if (!StringUtils.isEmpty(opsProduct)) {
            OpsProductCategory productCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
            if (!StringUtils.isEmpty(productCategory)) {
                OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(productCategory, opsProduct.getProductSubCategory());
                if (!StringUtils.isEmpty(opsProductSubCategory)) {
                    switch (productCategory) {

                        case PRODUCT_CATEGORY_TRANSPORTATION: {
                            switch (opsProductSubCategory) {
                                case PRODUCT_SUB_CATEGORY_FLIGHT: {
                                    currrency = opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getCurrencyCode();
                                    break;
                                }

                                case PRODUCT_SUB_CATEGORY_BUS:
                                    break;
                            }
                        }

                        case PRODUCT_CATEGORY_ACTIVITIES: {

                        }

                        case PRODUCT_CATEGORY_ACCOMMODATION: {
                            switch (opsProductSubCategory) {
                                case PRODUCT_SUB_CATEGORY_HOTELS: {
                                    currrency = opsProduct.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getCurrencyCode();
                                    break;
                                }
                            }
                        }

                    }
                }
            }
        }
        return currrency;
    }

}
