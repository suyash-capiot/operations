package com.coxandkings.travel.operations.service.productbookedthrother;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.productbookedthrother.Attribute;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.productbookedthrother.ProductBookedThrOtherResource;

import java.io.IOException;
import java.text.ParseException;

public interface ProductBookedThrOtherService {

  public ProductBookedThrOtherResource getProduct(String bookingRefId,String orderId,String productCategorySubTypeValue) throws Exception;

  public Attribute saveProduct(ProductBookedThrOtherResource productBookedThrOtherResource) throws Exception;

  public Attribute updateProduct(ProductBookedThrOtherResource productBookedThrOtherResource) throws OperationException;

  public EmailResponse sendAnEmailToSupplier(ProductBookedThrOtherResource productBookedThrOtherResource) throws Exception;

    public EmailResponse sendAnEmailToClientOrCustomer(String bookingRefId, String orderId, String productCategorySubTypeValue) throws IOException, ParseException, OperationException;

    public Attribute updateProductByClient(ProductBookedThrOtherResource productBookedThrOtherResource) throws OperationException;

    public boolean isDetailSent(String productCategorySubTypeValue, String id);


}
