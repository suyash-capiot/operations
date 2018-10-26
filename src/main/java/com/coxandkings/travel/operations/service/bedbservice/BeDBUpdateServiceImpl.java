package com.coxandkings.travel.operations.service.bedbservice;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.service.bedbservice.acco.AccoBeDBUpdateService;
import com.coxandkings.travel.operations.service.bedbservice.air.AirBeDBUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeDBUpdateServiceImpl implements BeDBUpdateService {

    @Autowired
    private AirBeDBUpdateService airBeDBUpdateService;

    @Autowired
    private AccoBeDBUpdateService accoBeDBUpdateService;


    @Override
    public void updateOrderPrice(OpsProduct opsProduct) throws OperationException {
        OpsProductSubCategory opsProductSubCategory = opsProduct.getOpsProductSubCategory();

        switch (opsProductSubCategory) {
            case PRODUCT_SUB_CATEGORY_BUS:
                break;
            case PRODUCT_SUB_CATEGORY_CAR:
                break;
            case PRODUCT_SUB_CATEGORY_EVENTS:
                break;
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                airBeDBUpdateService.updateOrderPrice(opsProduct);
                break;
            case PRODUCT_SUB_CATEGORY_HOTELS:
                accoBeDBUpdateService.updateOrderPrice(opsProduct);
                break;
            case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
                break;
            case PRODUCT_SUB_CATEGORY_RAIL:
                break;
            default:
                break;
        }
    }

}
