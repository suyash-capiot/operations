package com.coxandkings.travel.operations.service.merge;

import com.coxandkings.travel.operations.enums.merge.MergeTypeValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.merge.AccommodationBookProduct;
import com.coxandkings.travel.operations.model.merge.Merge;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.merge.MergeGroupResource;
import com.coxandkings.travel.operations.resource.merge.MergeList;
import com.coxandkings.travel.operations.resource.merge.MergeResource;
import com.coxandkings.travel.operations.resource.merge.SupplierPriceResource;

import org.json.JSONArray;
import org.json.JSONException;
import org.quartz.SchedulerException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

public interface MergeService {
    List<Merge> saveMerge(String bookingId) throws OperationException, ParseException, SchedulerException;

    List<Merge> saveMerge(OpsBooking opsBooking) throws OperationException, SchedulerException;

    String getPrice(String id, String priceResource);

    List<SupplierPriceResource> getSupplierRates(String id, Set<AccommodationBookProduct> bookProducts) throws JSONException, IOException, OperationException;
    //
//    Set<Merge> splitMerge(String mergeId, String supplierId) throws OperationException, IOException;
//
    MergeList getPotentialMerges(MergeTypeValues mergeTypeValue) throws OperationException;

    List<MergeGroupResource> getMergeGroup(MergeResource mergeResource) throws OperationException;
    //
//    Merge getMerge(String id) throws OperationException;
//
//    void deleteMerge(String id) throws OperationException;
    EmailResponse sendMail();

    MultipartFile createPdf(String id) throws FileNotFoundException;

    String sendMergeListMail(String id);

	JSONArray getPrice(List<MergeGroupResource> mergeResourceLst) throws OperationException;
}