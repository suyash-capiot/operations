package com.coxandkings.travel.operations.controller.prodreview;

import com.coxandkings.travel.operations.criteria.prodreview.ProductReviewCriteria;
import com.coxandkings.travel.operations.enums.prodreview.CustomerResponseFlg;
import com.coxandkings.travel.operations.enums.prodreview.ProdReviewStatus;
import com.coxandkings.travel.operations.enums.user.UserType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prodreview.ProductReview;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewAverage;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewByUser;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewTemplateReference;
import com.coxandkings.travel.operations.resource.prodreview.*;
import com.coxandkings.travel.operations.response.prodreview.ProductReviewResposneClient;
import com.coxandkings.travel.operations.response.prodreview.ReviewResponse;
import com.coxandkings.travel.operations.service.prodreview.ProductReviewAverageService;
import com.coxandkings.travel.operations.service.prodreview.ProductReviewService;
import com.coxandkings.travel.operations.service.prodreview.TemplateReferenceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.OperationsException;
import java.util.*;

@RestController
@RequestMapping("/productReviews")
@CrossOrigin(origins = "*")
public class ProductReviewController {
    @Autowired
    private ProductReviewService productReviewService;

    @Autowired
    private TemplateReferenceService templateReferenceService;

    @Autowired
    private ProductReviewAverageService productReviewAverageService;

    private static final Logger logger = LogManager.getLogger(ProductReviewController.class);

    @PostMapping(value = "/v1/save")
    @PutMapping(value = "/v1/saveAndUpdate")
    public ResponseEntity<ProductReview> save(@RequestBody ProductReviewResource productReviewResource) throws OperationException {
        ProductReview productReview = productReviewService.saveAndUpdate(productReviewResource);
        try {
            return new ResponseEntity<>(productReview, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error while saving product review", e);

            throw new OperationException("Error while saving product review");
        }

    }

    /**
     * method for wem product review update
     *
     * @param productReviewResource
     * @return
     * @throws OperationException
     */
    @PutMapping(value = "/v1/productReviewUpdate")
    public ResponseEntity<ProductReview> saveAndUpdate(@RequestBody ProductReviewResource productReviewResource) throws OperationException {

        if (productReviewResource.getId().isEmpty()) {
            throw new OperationException("To update product review review id required");
        }
        ProductReview productReview = productReviewService.saveAndUpdate(productReviewResource);
        try {
            return new ResponseEntity<>(productReview, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error while saving product review");
            e.printStackTrace();
            throw new OperationException("Error while saving product review");
        }

    }

    @RequestMapping(value = "/v1/update", method = {RequestMethod.PUT})
    public ResponseEntity<ProductReview> update(@RequestBody ReviewResponse reviewResponse) throws OperationException {
        ProductReview productReview = productReviewService.updateProductReview(reviewResponse);
        try {
            return new ResponseEntity<>(productReview, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error while updating the product review");
            e.printStackTrace();
            throw new OperationException("Error while updating the product review");
        }

    }

    @GetMapping("/v1/getUIResponse/{id}")
    public ResponseEntity<ReviewResponse> getUIResponse(@PathVariable String id) throws OperationException {
        ReviewResponse reviewResponse = productReviewService.getUIResponse(id);
        try {
            return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("unable to get UI response");
            e.printStackTrace();
            throw new OperationException("Error while getting UI response");
        }
    }
/*
    @PostMapping("/v1/submitReview")
    public ResponseEntity<ProductReview> submitReview(@RequestBody ProductReviewResource productReviewResource) throws OperationException {
        ProductReview productReview = productReviewService.saveAndUpdate(productReviewResource);
        productReview.setProductReviewStatus(ProdReviewStatus.UN_VERIFIED);
        ProductReviewResource productReviewResource1 = new ProductReviewResource();
        CopyUtils.copy(productReview, productReviewResource1);
        productReviewService.saveAndUpdate(productReviewResource1);
        return new ResponseEntity<>(productReview, HttpStatus.OK);

    }*/

    /*@PostMapping("/v1/updateStatus")
    public ResponseEntity updateStatus(@RequestParam String id, @RequestParam ProdReviewStatus prodReviewStatus) throws OperationException {
        ProductReview productReview = productReviewService.updateStatus(id, prodReviewStatus);
        try {
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error while updating status of the product review");
            e.printStackTrace();
            throw new OperationException("Error while updating status of the product review");
        }
    }*/

    @RequestMapping(path = "/v1/getProductReview/{reviewId}", method = RequestMethod.GET)
    public ResponseEntity<ProductReview> getProductReview(@PathVariable String reviewId) throws OperationException {
        ProductReview productReview = productReviewService.getProductReviewById(reviewId);
        if (productReview == null) {
            throw new OperationException("No Product Review Found for review Id " + reviewId);
        }
        return new ResponseEntity<ProductReview>(productReview, HttpStatus.OK);
    }

    @RequestMapping(path = "/v1/search", method = RequestMethod.POST)
    public ResponseEntity<SearchProductReviewResponse> getProductReviewByCriteria(@RequestBody ProductReviewCriteria productReviewCriteria) {

        return new ResponseEntity(productReviewService.getManageProductCriteria(productReviewCriteria), HttpStatus.OK);
    }

    //soft delete
    /*@RequestMapping(value = "/v1/delete/{reviewId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteReview(@PathVariable(name = "reviewId") String reviewId) throws OperationException {
        try {
            return new ResponseEntity<String>(productReviewService.deleteReview(reviewId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to delete the review");
            e.printStackTrace();
            throw new OperationException("Unable to delete the review");
        }
    }*/

    @RequestMapping(value = "/v1/status", method = RequestMethod.PUT)
    public ResponseEntity<String> changeProductReviewStatus(@RequestBody ProductReviewStatusResource productReviewStatusResource) throws OperationException {
        try {
            return new ResponseEntity<String>(productReviewService.changeProductReviewStatus(productReviewStatusResource), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error while updating status");
            e.printStackTrace();
            throw new OperationException("Error while updating product review status");
        }
    }


    @PostMapping("/v1/sendToClient")
    public ResponseEntity sendMailToCustomer(@RequestBody ClientMailResource clientMailResource) throws OperationException {
        return new ResponseEntity(productReviewService.sendMailToCustomer(clientMailResource), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/getTemplateReference/{referenceNo}", method = RequestMethod.GET)
    public ResponseEntity<ProductReviewTemplateReference> getTemplateReferenceById(@PathVariable(name = "referenceNo") String referenceNo) {
        ProductReviewTemplateReference templateReference = templateReferenceService.getTemplateReferenceById(referenceNo);
        return new ResponseEntity<>(templateReference, HttpStatus.OK);
    }

    @PostMapping("/v1/submit")
    public ResponseEntity<Map> submit(@RequestBody CommentResource commentResource) throws OperationException {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("message", productReviewService.submitReview(commentResource));
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("error while submitting the product review");
            e.printStackTrace();
            throw new OperationException("Error while submitting the review");
        }
    }

    @PostMapping(value = "/v1/customerResponse/{reviewId}/{customerAcceptance}")
    public ResponseEntity<String> customerResponse(@PathVariable String reviewId, @PathVariable CustomerResponseFlg customerAcceptance) throws OperationException, OperationsException {
        String message = "";
        ProductReviewStatusResource productReviewStatusResource = new ProductReviewStatusResource();
        productReviewStatusResource.setReviewId(reviewId);
        if (customerAcceptance.equals(CustomerResponseFlg.CONFIRMED)) {
            productReviewStatusResource.setProductReviewStatus(ProdReviewStatus.PUBLISHED);
            message = "Review has been published successfully";
            productReviewService.changeProductReviewStatus(productReviewStatusResource);
        } else if (customerAcceptance.equals(CustomerResponseFlg.REJECTED)) {
            productReviewStatusResource.setProductReviewStatus(ProdReviewStatus.UN_PUBLISHED);
            message = "Review has not been published as customer rejected the changes.";
            productReviewService.changeProductReviewStatus(productReviewStatusResource);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/v1/getProductReviewAverage/{id}")
    private ProductReviewAverage getProductReviewAverage(@PathVariable(name = "id") String id) throws OperationException {
        ProductReviewAverage productReviewAverage = productReviewAverageService.getProductReviewAverage(id);
        if (productReviewAverage == null) {
            throw new OperationException("Review Average Not Found");
        }
        return productReviewAverage;
    }


    @GetMapping("/v1/company")
    private List<String> getCompanyByUser(@RequestParam String userID) {
        return productReviewService.getCompanyByUser(userID);
    }

    @GetMapping("/v1/statusDropdown")
    private List<ProdReviewStatus> getReviewStatusDropdown() {
        return new ArrayList<>(Arrays.asList(ProdReviewStatus.values()));
    }

    @GetMapping("/v1/getUserRole")
    private JSONArray getUserRoles() {
        JSONArray array = new JSONArray();
        JSONObject opsUser = new JSONObject();
        opsUser.put("name", UserType.OPS_USER.getUserType());
        opsUser.put("value", UserType.OPS_USER);
        JSONObject productUser = new JSONObject();
        productUser.put("name", "Product User");
        productUser.put("value", UserType.OPS_PRODUCT_USER);
        JSONObject marketingUser = new JSONObject();
        marketingUser.put("name", UserType.OPS_MARKETING_USER.getUserType());
        marketingUser.put("value", UserType.OPS_MARKETING_USER);
        array.put(opsUser);
        array.put(productUser);
        array.put(marketingUser);
        return array;
    }

    /**
     * This product review is for
     *
     * @return
     */
    @PostMapping("/v1/getProductReviewsByUserId")
    public ResponseEntity<ProductReviewResposneClient> getProductReviewsByUserId(@RequestBody ProductReviewByUser productReviewByUser) {
        ProductReviewResposneClient productReviews = productReviewService.getProductReviewByUserId(productReviewByUser);
        return new ResponseEntity<ProductReviewResposneClient>(productReviews, HttpStatus.OK);
    }


}

