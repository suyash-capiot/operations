package com.coxandkings.travel.operations.repository.reconfirmation.client;

import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientReconfirmationRepository {

    ClientReconfirmationDetails saveOrUpdateClientReconfirmation(ClientReconfirmationDetails clientReconfirmationDetails);

    ClientReconfirmationDetails findByBookRefAndOrderNo(String bookRefNo, String orderNo);

    List<ClientReconfirmationDetails> getAllClientReconfirmation();

    ClientReconfirmationDetails findByClientReconfirmationId(String reconfirmationID);

    ClientReconfirmationDetails findByHash(String hash);

     void deleteAllReconfirmation();
     void deleteReconfirmation(String id);
}
