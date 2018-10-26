package com.coxandkings.travel.operations.service.FileProfitability;

import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchCriteria;
import com.coxandkings.travel.operations.enums.FileProfitability.FileProfTypes;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.FileProfitabilityBooking;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FileProfitabilityService {
    List<FileProfitabilityBooking> getListOfFileProfsWRTCriteria(FileProfSearchCriteria fileProfBookingCriteria) throws OperationException;

    void calculationMethodForServiceOrder(OpsProduct opsProduct, FileProfitabilityBooking fromServiceOrder, FileProfTypes fileProfType);

    void opsBookingFromKafka(KafkaBookingMessage kafkaBookingMessage) throws OperationException;

    void updateSupplierFinalProfitability(JSONObject commercialStatementJsn) throws OperationException;
    void updateClientFinalProfitability(JSONObject commercialStatementJsn) throws OperationException;
}
