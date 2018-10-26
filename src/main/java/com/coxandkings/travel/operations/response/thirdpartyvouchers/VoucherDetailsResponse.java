package com.coxandkings.travel.operations.response.thirdpartyvouchers;

import java.util.List;

public class VoucherDetailsResponse {

    private List<String> deactivatedCodes;
    private VoucherCodeResponse voucherCodeResponse;

    public List<String> getDeactivatedCodes() {
        return deactivatedCodes;
    }

    public void setDeactivatedCodes(List<String> deactivatedCodes) {
        this.deactivatedCodes = deactivatedCodes;
    }

    public VoucherCodeResponse getVoucherCodeResponse() {
        return voucherCodeResponse;
    }

    public void setVoucherCodeResponse(VoucherCodeResponse voucherCodeResponse) {
        this.voucherCodeResponse = voucherCodeResponse;
    }
}
