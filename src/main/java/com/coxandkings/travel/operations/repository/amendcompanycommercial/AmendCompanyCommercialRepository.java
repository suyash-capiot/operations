package com.coxandkings.travel.operations.repository.amendcompanycommercial;

import com.coxandkings.travel.operations.model.companycommercial.AmendCompanyCommercial;

import java.util.List;

public interface AmendCompanyCommercialRepository {

	AmendCompanyCommercial saveCommercial(AmendCompanyCommercial amendCompanyCommercial);
	AmendCompanyCommercial getCommercial(String id);

    List<String> getAmendedCommercialHeads(String bookingId);

    List<String> getApprovedCommercialHeads(String bookingId);

}
