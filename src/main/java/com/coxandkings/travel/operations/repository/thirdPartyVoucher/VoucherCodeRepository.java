package com.coxandkings.travel.operations.repository.thirdPartyVoucher;

import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.ReportGenerationCriteria;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCode;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCodePK;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.SearchVouchersToSend;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.UploadVouchersResource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface VoucherCodeRepository {

    Map<String,Object> searchForReports(ReportGenerationCriteria reportGenerationCriteria);

    List<String> getDocumentsList(String supplierConfigId);

    List<VoucherCode> update(List<VoucherCode> voucherCodes);

    List<VoucherCode> getReleaseDates(ZonedDateTime date);

    List<VoucherCode> searchToSendVouchers(SearchVouchersToSend searchVouchersToSend);

    List<VoucherCode> unassignedVouchers(String supplierConfigId);

    VoucherCode getVoucherCode(VoucherCodePK voucherCode);

    Map getVouchers(String supplierConfigId,String voucherFile,Integer pageNo,Integer pageSize);



    public  List<VoucherCode> getVoucherCode(String fileId);

}
