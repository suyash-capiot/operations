package com.coxandkings.travel.operations.repository.amendclientcommercial;

import com.coxandkings.travel.operations.model.clientcommercial.ClientCommercial;

import java.util.List;

public interface AmendClientCommercialRepository {

    ClientCommercial saveClientCommercialAmendment(ClientCommercial clientCommercial);
    ClientCommercial getClientCommercialAmendment(String id);

    List<String> getAmendedCommercialHeads(String bookingId);

    List<String> getApprovedCommercialHeads(String bookingId);
 
}
