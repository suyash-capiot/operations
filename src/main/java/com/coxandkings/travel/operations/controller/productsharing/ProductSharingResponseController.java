package com.coxandkings.travel.operations.controller.productsharing;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.productsharing.ProductSharingStatus;
import com.coxandkings.travel.operations.service.productsharing.ProductSharingService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;


@RestController
@CrossOrigin(value = "*")
@RequestMapping("clientResponse/operations/productSharing/clientResponse")
public class ProductSharingResponseController {


    @Autowired
    private ProductSharingService productSharingService;


    /**
     * @param ID
     * @return
     * @throws OperationException
     */
    @GetMapping(value = "/v1/accept")
    public ResponseEntity<?> acceptProductSharing(@RequestParam("ID") String ID) throws OperationException {
        String productSharing = null;
        try {
            productSharing = this.productSharingService.checkClientResponse(ProductSharingStatus.ACCEPT,ID);
            return new ResponseEntity(productSharing, OK);
        } catch (OperationException e) {
            e.printStackTrace();
            String message = this.productSharingService.getMessage(e.getErrorCode(), Locale.ENGLISH);
            return new ResponseEntity(productSharingService.convertToHtml(message, "red"), BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            String message = this.productSharingService.getMessage(Constants.ER908, Locale.ENGLISH);
            return new ResponseEntity(productSharingService.convertToHtml(message, "blue"), BAD_REQUEST);
        }

    }

    /**
     * @param ID
     * @return
     * @throws OperationException
     */
    @GetMapping(value = "/v1/reject")
    public ResponseEntity<?> rejectProductSharing(@RequestParam("ID") String ID) throws OperationException {
        String productSharing = null;
        try {
            productSharing = this.productSharingService.checkClientResponse(ProductSharingStatus.REJECT,ID);
            return new ResponseEntity(productSharing, OK);
        } catch (OperationException e) {
            e.printStackTrace();
            String message = this.productSharingService.getMessage(e.getErrorCode(), Locale.ENGLISH);
            return new ResponseEntity(productSharingService.convertToHtml(message, "red"), BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            String message = this.productSharingService.getMessage(Constants.ER908, Locale.ENGLISH);
            return new ResponseEntity(productSharingService.convertToHtml(message, "blue"), BAD_REQUEST);
        }

    }


}





