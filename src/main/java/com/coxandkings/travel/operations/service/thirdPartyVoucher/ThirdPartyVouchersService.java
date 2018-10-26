package com.coxandkings.travel.operations.service.thirdPartyVoucher;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.SupplierVoucherCodes;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCode;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.FileAttachmentResource;

import java.util.List;

public interface ThirdPartyVouchersService {

    public List<SupplierVoucherCodes> searchToAssignVouchers(OpsProduct opsProduct);

    public List<VoucherCode> vouchersAvailable(String supplierConfigId);

    public String assignVouchersToBooking(SupplierVoucherCodes supplierVoucherCodes, OpsProduct opsProduct, List<String> voucherIds) throws OperationException;

    public EmailResponse sendEmailToSupplier(String bookId, String orderId, String productName, String templateId, String noOfVouchers) throws OperationException;

    public EmailResponse sendEmailToClient(OpsBooking opsBooking, String orderId, FileAttachmentResource fileAttachmentResource, String templateId, String productName) throws OperationException;

    public void getThirdPartyVoucher();

    public void updateVouchersWithReleaseDate(List<VoucherCode> voucherCodes);


}
