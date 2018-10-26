package com.coxandkings.travel.operations.model.thirdPartyVouchers;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class VoucherCodePK implements Serializable {

    @Column(name = "PK_ID") //overridden

    private String id;

    @Column(name = "PK_VOUCHER_ID") //overridden

    private String voucherCode;

    public VoucherCodePK() {
    }

    public VoucherCodePK(String id, String voucherCode) {
        this.id = id;
        this.voucherCode = voucherCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VoucherCodePK)) return false;
        VoucherCodePK that = (VoucherCodePK) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(voucherCode, that.voucherCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, voucherCode);
    }
}
