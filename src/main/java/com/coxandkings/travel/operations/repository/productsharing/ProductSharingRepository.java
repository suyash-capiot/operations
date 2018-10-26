package com.coxandkings.travel.operations.repository.productsharing;

import com.coxandkings.travel.operations.model.productsharing.ProductSharing;

import java.util.List;

public interface ProductSharingRepository {

    ProductSharing findByBookRefAndOrderNo(String bookRefNo, String orderNo);

    ProductSharing saveOrUpdateProductSharing(ProductSharing productSharing);

    List<ProductSharing> findByStatus(String status);

    List<ProductSharing> findByHash(String hash);

    List<ProductSharing> findByStatus(String status, String fromSerialNumber);

    ProductSharing findByBookRefAndOrderNoAndSerialNo(String bookRefNo, String orderID, String serialNumber,String fromPassengerId,String toSerialNumber);

    ProductSharing findProductSharingByHash(String hash);

     ProductSharing findProductSharingBySecondRef(String hash,boolean isConverted);
}
