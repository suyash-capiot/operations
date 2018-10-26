package com.coxandkings.travel.operations.service.qcmanagement;

import com.coxandkings.travel.operations.enums.qcmanagement.CancelAmendTypes;
import com.coxandkings.travel.operations.model.core.OpsProduct;

public interface QcCancellationService {
    boolean doQcLevelCheck(OpsProduct opsProduct, CancelAmendTypes cancelAmendTypes);
}
