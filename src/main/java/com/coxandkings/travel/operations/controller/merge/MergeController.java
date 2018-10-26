package com.coxandkings.travel.operations.controller.merge;

import com.coxandkings.travel.operations.enums.merge.MergeTypeValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.merge.AccommodationBookProduct;
import com.coxandkings.travel.operations.model.merge.Merge;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.merge.MergeGroupResource;
import com.coxandkings.travel.operations.resource.merge.MergeList;
import com.coxandkings.travel.operations.resource.merge.MergeResource;
import com.coxandkings.travel.operations.resource.merge.SupplierPriceResource;
import com.coxandkings.travel.operations.service.changesuppliername.ChangeSupplierNameService;
import com.coxandkings.travel.operations.service.merge.MergeService;

import org.json.JSONArray;
import org.json.JSONException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/mergeBooking")
@CrossOrigin(origins = "*")
public class MergeController {

    @Autowired
    MergeService mergeService;

    @Autowired
    ChangeSupplierNameService changeSupplierNameOrPriceService;

    @PostMapping("/v1/merge")
    public ResponseEntity<List<Merge>> merge(@RequestBody String bookingRefId) throws OperationException, ParseException, SchedulerException {
        List<Merge> merges = mergeService.saveMerge(bookingRefId);
        return new ResponseEntity<>(merges, HttpStatus.OK);
    }

    @PostMapping("/v1/getSupplierRates")
    public ResponseEntity<List<SupplierPriceResource>> getSupplierRates(@RequestParam(name = "id") String id, @RequestBody Set<AccommodationBookProduct> bookProducts) throws JSONException, OperationException {
        List<SupplierPriceResource> supplierRates = null;
        try {
            supplierRates = mergeService.getSupplierRates(id, bookProducts);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(supplierRates, HttpStatus.OK);
    }

    @PostMapping("/v1/getPriceOld")
    @Deprecated
    public String getPriceOld(@RequestParam(name = "id") String id, @RequestBody String priceRequest) {
        return mergeService.getPrice(id, priceRequest);
    }
    
    @PostMapping("/v1/getPrice")
    public JSONArray getPrice(@RequestBody List<MergeGroupResource> mergeResourceLst) throws OperationException {
        return mergeService.getPrice(mergeResourceLst);
    }


//    @PostMapping("/splitMerge")
//    public ResponseEntity<Set<Merge>> splitMerge(@RequestParam(name = "mergeId") String mergeId,
//                                                 @RequestParam(name = "supplierId") String supplierId) throws OperationException, IOException {
//        Set<Merge> merges = mergeService.splitMerge(mergeId, supplierId);
//        return new ResponseEntity<>(merges, HttpStatus.OK);
//    }

    @GetMapping("/v1/getAll")
    public ResponseEntity<MergeList> getPotentialMerges(@RequestParam(name = "mergeType")MergeTypeValues mergeTypeValues) throws OperationException {
    	MergeList merges = mergeService.getPotentialMerges(mergeTypeValues);
        return new ResponseEntity<>(merges, HttpStatus.OK);
    }
    
    @PostMapping("/v1/get")
    public List<MergeGroupResource> getMergeGroup(@RequestBody MergeResource mergeResource) throws OperationException {
         return mergeService.getMergeGroup(mergeResource);
    }

    @GetMapping("/v1/sendMail")
    public ResponseEntity<EmailResponse> getPotentialMerges() throws OperationException {
        EmailResponse emailResponse = mergeService.sendMail();
        return new ResponseEntity<>(emailResponse, HttpStatus.OK);
    }

    @GetMapping("/v1/createPdf")
    public ResponseEntity<String> createPdf(@RequestParam(name = "id") String id) throws FileNotFoundException {
        mergeService.sendMergeListMail(id);
        return new ResponseEntity<>("Nikhil", HttpStatus.OK);
    }

//    @GetMapping("/getMerge/{id}")
//    public ResponseEntity<Merge> getMerge(@PathVariable(name = "id") String id) throws OperationException {
//        Merge merge = mergeService.getMerge(id);
//        return new ResponseEntity<>(merge, HttpStatus.OK);
//    }
}
