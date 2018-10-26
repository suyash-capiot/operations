package com.coxandkings.travel.operations.controller.changesuppliername;

import com.coxandkings.travel.operations.consumer.factory.AppConfig;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskSubTypeValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.changesuppliername.DiscountOnSupplierPrice;
import com.coxandkings.travel.operations.model.changesuppliername.SupplementOnSupplierPrice;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.resource.changesuppliername.ChangeSupplierPriceApprovalResource;
import com.coxandkings.travel.operations.resource.changesuppliername.ChangedSupplierPriceResource;
import com.coxandkings.travel.operations.resource.changesuppliername.SupplierResource;
import com.coxandkings.travel.operations.resource.changesuppliername.cms.CmsSupplierResource;
import com.coxandkings.travel.operations.resource.changesuppliername.request.UtilityResource;
import com.coxandkings.travel.operations.resource.changesuppliername.request.accoV2.CSCreateBookingResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.changesuppliername.ChangeSupplierNameService;
import com.coxandkings.travel.operations.service.changesuppliername.ChangeSupplierNameServiceV2;
import com.coxandkings.travel.operations.service.changesuppliername.ChangeSupplierPriceService;
import com.coxandkings.travel.operations.service.changesuppliername.impl.ChangeSupplierNameServiceImplV2;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.changeSupplier.CachingConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/changeSupplierName")
@CrossOrigin("*")
public class ChangeSupplierNameControllerV2 {

    private static final String cmsCacheKey = "CMS";

    @Autowired
    private ChangeSupplierNameServiceV2 changeSupplierNameService;
    @Autowired
    @Qualifier("changeSupplierPriceServiceImpl")
    private ChangeSupplierPriceService changeSupplierPriceService;

    @Autowired
    private OpsBookingService opsBookingService;

    @PostMapping("/v2/createBooking")
    public ResponseEntity<Object> createBooking(@RequestBody CSCreateBookingResource csCreateBookingResource) throws OperationException {
        try {
            return new ResponseEntity<>(changeSupplierNameService.createBooking(csCreateBookingResource), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/v2/{bookID}")
    public ResponseEntity<OpsBooking> getBooking(@PathVariable(name = "bookID", required = true) String bookID) throws OperationException {
        try {
            OpsBooking opsBooking = opsBookingService.getBooking(bookID);

            return new ResponseEntity<OpsBooking>(opsBooking, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10912);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() throws OperationException {
        try {
            String opsBooking = changeSupplierNameService.getCMSDefinedRates("ABHICREDIT108","8a9d917b64f4abbf0164f5041724000f","");
            return new ResponseEntity<String>(opsBooking, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10912);
        }
    }

    @PostMapping("/cmsDefinedRates")
    public ResponseEntity<Object> getDefinedRates(@RequestBody CSCreateBookingResource csCreateBookingResource) throws OperationException {
        try {
            String opsBooking = changeSupplierNameService.getCMSDefinedRates(csCreateBookingResource.getBookID(), csCreateBookingResource.getOrderID(), csCreateBookingResource.getNewSuppID());
            return new ResponseEntity<Object>(opsBooking, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cmsSuppliers")
    public ResponseEntity<Object> getCMSSupplierNames(@RequestBody UtilityResource utilityResource) throws OperationException {
        try {

            List<CmsSupplierResource> list = changeSupplierNameService.getSupplierElements(cmsCacheKey);
            return new ResponseEntity<Object>(changeSupplierNameService.getCMS(list, utilityResource.getName()), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
