package com.coxandkings.travel.operations.repository.fullcancellation;

import com.coxandkings.travel.operations.model.fullCancellation.FullCancellationIdentity;
import com.coxandkings.travel.operations.model.fullCancellation.FullCancellationKafkaMessage;

public interface FullCancellationKafkaMessageRepository {
        FullCancellationKafkaMessage saveAndUpdate(FullCancellationKafkaMessage fullCancellationKafkaMessage);
        FullCancellationKafkaMessage getFullCancellationKafkaMessage(FullCancellationIdentity fullCancellationIdentity);
}

