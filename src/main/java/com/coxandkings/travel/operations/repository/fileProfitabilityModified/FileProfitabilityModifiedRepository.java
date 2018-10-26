package com.coxandkings.travel.operations.repository.fileProfitabilityModified;

import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchCriteria;
import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchReportCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.FileProfitabilityBooking;

import java.util.List;

public interface FileProfitabilityModifiedRepository {
    public FileProfitabilityBooking saveOrUpdateFileProfitability(FileProfitabilityBooking fileProfitabilityBooking);

    public FileProfitabilityBooking getFileProfitabilityByFileId(String id);

    public List<FileProfitabilityBooking> getFileProfBookByCriteria(FileProfSearchCriteria criteria);

    public List<FileProfitabilityBooking> getListOfFileProfsWRTCriteria(FileProfSearchCriteria fileProfBookingCriteria) throws OperationException;

    public void updateAllDBRecs();
    public List<FileProfitabilityBooking> getFileProfsWRTBookingId(FileProfSearchCriteria fileProfBookingCriteria);

    public List<FileProfitabilityBooking> getAutoSuggest(FileProfSearchCriteria fileProfBookingCriteria) throws OperationException;

    List<FileProfitabilityBooking> getListOfFileProfsWRTCriteria(FileProfSearchReportCriteria bookingCriteria);
}
