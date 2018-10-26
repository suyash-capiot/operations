package com.coxandkings.travel.operations.repository.thirdPartyVoucher.impl;

import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.ReportGenerationCriteria;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.PaymentStatusToReleaseVoucher;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeStatus;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeUsageType;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.SupplierVoucherCodes;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCode;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCodePK;
import com.coxandkings.travel.operations.repository.thirdPartyVoucher.VoucherCodeRepository;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.SearchVouchersToSend;
import com.coxandkings.travel.operations.resource.thirdpartyVoucher.UploadVouchersResource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Repository("voucherCode")
public class VoucherCodeRepositoryImpl extends SimpleJpaRepository<VoucherCode, VoucherCodePK> implements VoucherCodeRepository {

    private EntityManager entityManager;

    public VoucherCodeRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(VoucherCode.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Map<String,Object> searchForReports(ReportGenerationCriteria reportGenerationCriteria) {
        //List<VoucherCode> list = null;
        int noOfPages = 1;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VoucherCode> criteriaQuery = criteriaBuilder.createQuery(VoucherCode.class);
        Root<VoucherCode> root = criteriaQuery.from(VoucherCode.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        Join<VoucherCode, SupplierVoucherCodes> join = root.join("supplierVoucherCodes");
        if (reportGenerationCriteria.getSupplierName() != null && !(reportGenerationCriteria.getSupplierName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(join.get("supplierName"), reportGenerationCriteria.getSupplierName()));
        }
        if (reportGenerationCriteria.getProductCategoryName() != null && !(reportGenerationCriteria.getProductCategoryName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(join.get("productCategoryName"), reportGenerationCriteria.getProductCategoryName()));
        }
        if (reportGenerationCriteria.getProductSubCategoryName() != null && !(reportGenerationCriteria.getProductSubCategoryName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(join.get("productSubCategoryName"), reportGenerationCriteria.getProductSubCategoryName()));
        }
        if (reportGenerationCriteria.getProductName() != null && !(reportGenerationCriteria.getProductName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(join.get("productName"), reportGenerationCriteria.getProductName()));
        }
        if (reportGenerationCriteria.getVoucherCodeStatus() != null && !(reportGenerationCriteria.getVoucherCodeStatus().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("voucherCodeStatus"), VoucherCodeStatus.getEnum(reportGenerationCriteria.getVoucherCodeStatus())));
        }
        if (reportGenerationCriteria.getVoucherCodeUsageType() != null && !(reportGenerationCriteria.getVoucherCodeUsageType().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("voucherCodeUsageType"), VoucherCodeUsageType.getEnum(reportGenerationCriteria.getVoucherCodeUsageType())));
        }
        if (reportGenerationCriteria.getPaymentStatusToReleaseVoucher() != null && !(reportGenerationCriteria.getPaymentStatusToReleaseVoucher().isEmpty())) {
            predicates.add(criteriaBuilder.equal(join.get("paymentStatusToReleaseVoucher"), PaymentStatusToReleaseVoucher.getEnum(reportGenerationCriteria.getPaymentStatusToReleaseVoucher())));
        }
        if (reportGenerationCriteria.getPaymentStatusOfBooking() != null && !(reportGenerationCriteria.getPaymentStatusOfBooking().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("paymentStatus"), reportGenerationCriteria.getPaymentStatusOfBooking()));
        }
        if (!StringUtils.isEmpty(reportGenerationCriteria.getCompanyId()))
        {
            predicates.add(criteriaBuilder.equal(join.get("companyId"), reportGenerationCriteria.getCompanyId()));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        List<VoucherCode> list = entityManager.createQuery(criteriaQuery).getResultList();
        TypedQuery<VoucherCode> voucherCodeTypedQuery = entityManager.createQuery(criteriaQuery);
        Integer size = reportGenerationCriteria.getPageSize();
        Integer page = reportGenerationCriteria.getPageNo();
        if (size != null && page != null) {
            Integer recordNum = (page - 1) * size;
            voucherCodeTypedQuery.setFirstResult(recordNum);
            voucherCodeTypedQuery.setMaxResults(size);
        }

        if ((size!= null && !list.isEmpty()) && list.size() != 0 && !size.equals(0)) {
            noOfPages = list.size() / size;
            if (!(list.size() % size == 0))
                noOfPages = noOfPages + 1;
        }
        Map map = new HashMap();
        map.put("voucherCodes", voucherCodeTypedQuery.getResultList());
        map.put("noOfPages", noOfPages);
        return map;
    }

    @Override
    public List<String> getDocumentsList(String supplierConfigId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);

        Root<VoucherCode> root = criteriaQuery.from(VoucherCode.class);
        criteriaQuery.select(root.get("voucherDetails"));
        criteriaQuery.distinct(true);
        List<String> result = entityManager.createQuery(criteriaQuery).getResultList();
        result.remove(null);
        List<String> docList = new ArrayList<>();
        for (String vd : result) {
            Map map = getVouchers(supplierConfigId, vd,1,null);
            List<VoucherCode> vc= (List<VoucherCode>) map.get("voucherCodes");
            if (!vc.isEmpty() && vc.get(0).getVoucherDetails() != null) {
                docList.add(vc.get(0).getVoucherDetails());
            }
        }

        return docList;
    }

    @Override
    public List<VoucherCode> update(List<VoucherCode> voucherCodes) {
        return this.save(voucherCodes);
    }

    @Override
    public List<VoucherCode> getReleaseDates(ZonedDateTime date) {
        List<VoucherCode> result = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VoucherCode> criteriaQuery = criteriaBuilder.createQuery(VoucherCode.class);
        List<Predicate> predicates = new ArrayList<>();
        Root<VoucherCode> root = criteriaQuery.from(VoucherCode.class);
        criteriaQuery.select(root);

        if (date != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("releaseDate"),date));
            predicates.add(criteriaBuilder.equal(root.get("voucherCodeStatus"),VoucherCodeStatus.ASSIGNED));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        try {
            result = entityManager.createQuery(criteriaQuery).getResultList();
        } catch (NoResultException e) {
            return result;
        }
        return result;
    }

    @Override
    public List<VoucherCode> searchToSendVouchers(SearchVouchersToSend searchVouchersToSend) {
        List<VoucherCode> result = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VoucherCode> criteriaQuery = criteriaBuilder.createQuery(VoucherCode.class);
        List<Predicate> predicates = new ArrayList<>();
        Root<VoucherCode> root = criteriaQuery.from(VoucherCode.class);
        criteriaQuery.select(root);
        Join<VoucherCode, SupplierVoucherCodes> join = root.join("supplierVoucherCodes");
        if (!StringUtils.isEmpty(searchVouchersToSend.getBookId())) {
            predicates.add(criteriaBuilder.equal(root.get("bookId"), searchVouchersToSend.getBookId()));
        }
        if (!StringUtils.isEmpty(searchVouchersToSend.getOrderId())) {
            predicates.add(criteriaBuilder.equal(root.get("orderId"), searchVouchersToSend.getOrderId()));
        }
        if (!StringUtils.isEmpty(searchVouchersToSend.getReleaseDate())) {
            predicates.add(criteriaBuilder.equal(root.get("releaseDate"), searchVouchersToSend.getReleaseDate()));
        }
        if (!StringUtils.isEmpty(searchVouchersToSend.getVoucherCodeStatus())) {
            predicates.add(criteriaBuilder.equal(root.get("voucherCodeStatus"), searchVouchersToSend.getVoucherCodeStatus()));
        }
        if (!StringUtils.isEmpty(searchVouchersToSend.getSupplierConfigId())) {
            predicates.add(criteriaBuilder.equal(join.get("id"), searchVouchersToSend.getSupplierConfigId()));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        try {
            result = entityManager.createQuery(criteriaQuery).getResultList();
        } catch (NoResultException e) {
            return result;
        }
        return result;
    }

    @Override
    public List<VoucherCode> unassignedVouchers(String supplierConfigId) {
        List<VoucherCode> list = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VoucherCode> criteriaQuery = criteriaBuilder.createQuery(VoucherCode.class);
        Root<VoucherCode> root = criteriaQuery.from(VoucherCode.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        Join<VoucherCode, SupplierVoucherCodes> join = root.join("supplierVoucherCodes");
        if (supplierConfigId != null && !(supplierConfigId.isEmpty())) {
            predicates.add(criteriaBuilder.equal(join.get("id"), supplierConfigId));
        }
        predicates.add(criteriaBuilder.equal(root.get("voucherCodeStatus"), VoucherCodeStatus.UNASSIGNED));

        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        try {
            list = entityManager.createQuery(criteriaQuery).getResultList();
        } catch (NoResultException e) {
            return list;
        }
        return list;
    }

    @Override
    public VoucherCode getVoucherCode(VoucherCodePK voucherCodePK) {
        return this.findOne(voucherCodePK);
    }

    @Override
    public Map getVouchers(String supplierConfigId, String voucherFile,Integer page,Integer size) {
        int noOfPages = 1;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VoucherCode> criteriaQuery = criteriaBuilder.createQuery(VoucherCode.class);
        Root<VoucherCode> root = criteriaQuery.from(VoucherCode.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        Join<VoucherCode, SupplierVoucherCodes> join = root.join("supplierVoucherCodes");
        if (supplierConfigId != null && !(supplierConfigId.isEmpty())) {
            predicates.add(criteriaBuilder.equal(join.get("id"), supplierConfigId));
        }
        if (voucherFile!=null && !voucherFile.isEmpty()){
            predicates.add(criteriaBuilder.equal(root.get("voucherDetails"),voucherFile));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        List<VoucherCode> list = entityManager.createQuery(criteriaQuery).getResultList();
        TypedQuery<VoucherCode> voucherCodeTypedQuery = entityManager.createQuery(criteriaQuery);

        if (size != null && page != null) {
            Integer recordNum = (page - 1) * size;
            voucherCodeTypedQuery.setFirstResult(recordNum);
            voucherCodeTypedQuery.setMaxResults(size);
        }

        if ((size!= null && !list.isEmpty()) && list.size() != 0 && !size.equals(0)) {
            noOfPages = list.size() / size;
            if (!(list.size() % size == 0))
                noOfPages = noOfPages + 1;
        }
        Map map = new HashMap();
        map.put("voucherCodes", voucherCodeTypedQuery.getResultList());
        map.put("noOfPages", noOfPages);
        return map;
    }



    public  List<VoucherCode> getVoucherCode(String fileId)
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VoucherCode> criteriaQuery = criteriaBuilder.createQuery(VoucherCode.class);
        Root<VoucherCode> root = criteriaQuery.from(VoucherCode.class);
        criteriaQuery.select(root);
        Predicate predicate = null;
        if (!StringUtils.isEmpty(fileId))
        {
            predicate = criteriaBuilder.equal(root.get("fileId"),fileId);
        }
        criteriaQuery.where(predicate);
        List<VoucherCode> voucherCodeList = entityManager.createQuery(criteriaQuery).getResultList();
        return voucherCodeList;
    }


}
