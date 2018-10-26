package com.coxandkings.travel.operations.repository.amendmentandpartialcancellation.impl;

import com.coxandkings.travel.operations.enums.amendmentandpartialcancellation.ActionType;
import com.coxandkings.travel.operations.enums.amendmentandpartialcancellation.OpsApprovalStatus;
import com.coxandkings.travel.operations.model.amendmentandpartialcancellation.AmendAndPartCanc;
import com.coxandkings.travel.operations.repository.amendmentandpartialcancellation.CompChgAmendAndPartCancRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CompChgAmendAndPartCancRepositoryImpl extends SimpleJpaRepository<AmendAndPartCanc, Long> implements CompChgAmendAndPartCancRepository {

    private EntityManager entityManager;

    public CompChgAmendAndPartCancRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(AmendAndPartCanc.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public boolean saveCompanyChargesAmendment(List<AmendAndPartCanc> amendAndPartCancList) {
        boolean flag = false;

        for (AmendAndPartCanc amendAndPartCanc : amendAndPartCancList) {
            entityManager.persist(amendAndPartCanc);
            flag = amendAndPartCanc.getId() != null ? true : false;

            // If there is any issue while storing particular AmendAndPartCanc record.
            if (!flag) break;
        }
        return flag;
    }

    @Override
    public int approveRejectCompanyChargesAmendment(String bookingRefId, String approvalStatus, String remark) {
        return entityManager.createQuery(" UPDATE AmendAndPartCanc apc SET apc.approvalUserStatus=:approvalStatus,apc.remark=:remark " +
                " WHERE apc.taskRefID=:taskRefID ")
                .setParameter("approvalStatus", approvalStatus.equalsIgnoreCase(OpsApprovalStatus.APPROVED.toString()) ? OpsApprovalStatus.APPROVED.toString() : OpsApprovalStatus.REJECTED.toString())
                .setParameter("remark", remark).setParameter("taskRefID", bookingRefId).executeUpdate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AmendAndPartCanc> getAmendmentDetailsByTaskRefId(String taskRefID) {
        return entityManager.createQuery(" FROM AmendAndPartCanc apc WHERE apc.taskRefID=:taskRefID AND apc.approvalUserStatus=:approvalStatus ")
                .setParameter("taskRefID", taskRefID).setParameter("approvalStatus", OpsApprovalStatus.APPROVED.toString()).getResultList();
    }

    @Override
    public int updateSupplierResponseToAmendment(String suppResponse, String taskRefID) {
        return entityManager.createQuery(" UPDATE AmendAndPartCanc apc SET apc.approvalUserStatus=:status WHERE apc.taskRefID=:taskRefID ")
                .setParameter("status", suppResponse.equalsIgnoreCase(OpsApprovalStatus.APPROVED.toString()) ? OpsApprovalStatus.APPROVED.toString() : OpsApprovalStatus.REJECTED.toString())
                .setParameter("taskRefID", taskRefID).executeUpdate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AmendAndPartCanc> getSupplierChargesAmendments(String taskRefID) {
        return entityManager.createQuery(" FROM AmendAndPartCanc apc WHERE apc.taskRefID=:taskRefID AND (apc.actionType=:suppChgAmend OR apc.actionType=:suppChgCanc) " +
                " AND apc.approvalUserStatus=:status ")
                .setParameter("taskRefID", taskRefID)
                .setParameter("suppChgAmend", ActionType.SUPPLIER_CHARGES_AMENDMENT.toString())
                .setParameter("suppChgCanc", ActionType.SUPPLIER_CHARGES_CANCELLATION.toString())
                .setParameter("status", OpsApprovalStatus.APPROVED.toString()).getResultList();
    }

    @Override
    public List<AmendAndPartCanc> getAmendAndCancDetailsList(String taskRefId) {
        return entityManager.createQuery(" FROM AmendAndPartCanc apc WHERE apc.taskRefID=:taskRefId ").setParameter("taskRefId", taskRefId).getResultList();
    }
}
