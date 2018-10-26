package com.coxandkings.travel.operations.repository.qcmanagement;

import com.coxandkings.travel.operations.model.qcmanagement.QcStatusInfo;

import java.util.List;

public interface QcManagementRepository {
    public QcStatusInfo saveOrUpdate(QcStatusInfo qcStatusInfo);
    public List<QcStatusInfo> getAllQcStatusInfo();
}
