package com.coxandkings.travel.operations.repository.thirdPartyVoucher;

import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.ReportGenerationCriteria;
import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.VoucherCodeSearchCriteria;
import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.SupplierConfigSearchCriteria;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.SupplierVoucherCodes;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCode;

import java.util.List;
import java.util.Map;

public interface SupplierConfigurationRepository {

    public SupplierVoucherCodes add(SupplierVoucherCodes supplierVoucherCodes);

    public SupplierVoucherCodes get(String id);

    public Map searchByCriteria(VoucherCodeSearchCriteria voucherCodeSearchCriteria);

    public Map<String,Object> reportGeneration(ReportGenerationCriteria reportGenerationCriteria);

    public List<SupplierVoucherCodes> searchToAssignVouchers(SupplierConfigSearchCriteria supplierConfigSearchCriteria);

    public void saveSupplierConfig(SupplierVoucherCodes supplierVoucherCodes);

    public void deleteVoucherCode(String id, VoucherCode voucherCode);
}
