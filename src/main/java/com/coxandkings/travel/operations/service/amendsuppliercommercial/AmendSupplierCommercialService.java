package com.coxandkings.travel.operations.service.amendsuppliercommercial;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.amendsuppliercommercial.AmendSupplierCommercial;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.AmendSupplierCommercialApprovalResource;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.AmendSupplierCommercialResource;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.SupplierCommercialPricingDetailResource;

public interface AmendSupplierCommercialService {


    void save(AmendSupplierCommercialResource supplierCommercialResource) throws OperationException;

    void update(AmendSupplierCommercialResource supplierCommercial) throws OperationException;

    AmendSupplierCommercial getAmendSupplierCommercial(String id);

    //String changeApprovalStatus(AmendSupplierCommercialApprovalResource resource) throws OperationException;

    ToDoTask createToDoTask(AmendSupplierCommercial amendSupplierCommercial) throws OperationException;


    AmendSupplierCommercial calculateSupplierCommercialForAcco(AmendSupplierCommercialResource supplierCommercial) throws OperationException;

    AmendSupplierCommercial calculateSupplierCommercialForAir(AmendSupplierCommercialResource supplierCommercial) throws OperationException;

    String approveOrReject(AmendSupplierCommercialApprovalResource resource) throws OperationException;

    SupplierCommercialPricingDetailResource calculateMargin(OpsBooking opsBooking,OpsProduct opsProduct) throws OperationException;
}
