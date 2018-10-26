package com.coxandkings.travel.operations.service.fullcancellation.impl;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsOrderStatus;
import com.coxandkings.travel.operations.resource.fullcancellation.OrderCancellation;
import com.coxandkings.travel.operations.resource.fullcancellation.OrderStatusResource;
import com.coxandkings.travel.operations.resource.fullcancellation.ProductsResource;
import com.coxandkings.travel.operations.resource.user.MdmUserInfo;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.fullcancellation.BEService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BEServiceImpl implements BEService {
    private static final Logger logger = LogManager.getLogger(BEServiceImpl.class);

    @Value("${full_cancellation.booking_engine_db_services.cancellation_charge}")
    private String cancellationCharages;

    @Value("${full_cancellation.booking_engine_db_services.acco_status}")
    private String accoUpdateOrderStatusUrl;

    @Value("${full_cancellation.booking_engine_db_services.air_status}")
    private String airUpdateOrderStatusUrl;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier( value = "mDMToken" )
    private MDMToken mdmToken;

    @Override
    public OrderCancellation getCancellations(KafkaBookingMessage kafkaBookingMessage) throws OperationException {
        List<ProductsResource> list = new ArrayList<>();

        String responseJSON = mdmRestUtils.getResponseJSON(cancellationCharages.concat(kafkaBookingMessage.getBookId()));

        ProductsResource[] products = (ProductsResource[]) jsonObjectProvider.getChildObject(responseJSON, "$.products.*", ProductsResource[].class);
        for (ProductsResource pResource : products) {
            list.add(pResource);
        }

        ProductsResource productsResourceData = null;
        try {
            for (ProductsResource productResource : list) {
                if (null != productResource.getOrderID()) {
                    if (productResource.getOrderID().equalsIgnoreCase(kafkaBookingMessage.getOrderNo())) {
                        productsResourceData = productResource;
                    }
                }
            }
        } catch (NullPointerException ne) {
            logger.error(ne);
        }

        if (productsResourceData != null) {
            return productsResourceData.getOrderCancellations().get(0);
        }
        return null;

    }


    /**
     * This method will update order status in BE
     *
     * @param productCategory provide product category based on this need to update order
     * @param orderStatus
     * @param orderId
     * @throws OperationException
     */
    @Override
    public void updateOrderStatus(OpsProductCategory productCategory, OpsProductSubCategory opsProductSubCategory, OpsOrderStatus orderStatus, String orderId) throws OperationException, UnsupportedEncodingException {
        OrderStatusResource orderStatusResource = new OrderStatusResource();
        //Todo: need to check this is correct way og getting token

        String userID = null;
        try {
            MdmUserInfo mdmUser = userService.createUserDetailsFromToken(mdmToken.getToken());
            OpsUser aOpsUser = userService.getOpsUser(mdmUser);
            userID = aOpsUser.getUserID();
        } catch (Exception e) {
            logger.error(e);
            userID = "ops";
        }

        orderStatusResource.setUserID(userID);
        orderStatusResource.setOrderID(orderId);
        orderStatusResource.setStatus(orderStatus.getProductStatus());
        String url = null;
        if (productCategory == OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION) {
            url = accoUpdateOrderStatusUrl;
        } else if (productCategory == OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION && opsProductSubCategory == OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT) {
            url = airUpdateOrderStatusUrl;
        }

        try {
            RestUtils.put(url, orderStatusResource, String.class);
        } catch (Exception e) {
            logger.error("Unable to update booking status for order:" + orderId, e);
            throw new OperationException("Unable to update booking status for order:" + orderId);
        }

        logger.info("*** Full Cancellation: Updated status for Cancelled in Booking Engine ***");

    }

    @Override
    public void updateInventoryStatus(KafkaBookingMessage kafkaBookingMessage) {
        logger.info("**** updateing in Inventory");
    }
}
