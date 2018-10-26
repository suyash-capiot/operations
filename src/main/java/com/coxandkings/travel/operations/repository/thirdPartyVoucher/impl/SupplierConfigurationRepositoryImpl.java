package com.coxandkings.travel.operations.repository.thirdPartyVoucher.impl;

import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.ReportGenerationCriteria;
import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.VoucherCodeSearchCriteria;
import com.coxandkings.travel.operations.criteria.thirdPartyVoucher.SupplierConfigSearchCriteria;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.PaymentStatusToReleaseVoucher;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.SupplierVoucherCodes;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCode;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCodePK;
import com.coxandkings.travel.operations.repository.thirdPartyVoucher.SupplierConfigurationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Transactional
@Repository("supplierConfiguration")
public class SupplierConfigurationRepositoryImpl extends SimpleJpaRepository<SupplierVoucherCodes, String> implements SupplierConfigurationRepository {
    private EntityManager entityManager;

    public SupplierConfigurationRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(SupplierVoucherCodes.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public SupplierVoucherCodes add(SupplierVoucherCodes supplierVoucherCodes) {
        return this.saveAndFlush(supplierVoucherCodes);
    }

    @Override
    public SupplierVoucherCodes get(String id) {
        return this.findOne(id);
    }

    @Override
    public Map searchByCriteria(VoucherCodeSearchCriteria voucherCodeSearchCriteria) {
        int noOfPages = 1;
        Map map = new HashMap();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplierVoucherCodes> criteriaQuery = criteriaBuilder.createQuery(SupplierVoucherCodes.class);
        Root<SupplierVoucherCodes> root = criteriaQuery.from(SupplierVoucherCodes.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        SupplierConfigSearchCriteria supplierConfigSearchCriteria = voucherCodeSearchCriteria.getFilter();
        if (!StringUtils.isEmpty(supplierConfigSearchCriteria.getCompanyId())) {
            predicates.add(criteriaBuilder.equal(root.get("companyId"), supplierConfigSearchCriteria.getCompanyId()));
        }
        if (supplierConfigSearchCriteria.getSupplierName() != null && !(supplierConfigSearchCriteria.getSupplierName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("supplierName"), supplierConfigSearchCriteria.getSupplierName()));
        }
        if (supplierConfigSearchCriteria.getProductName() != null && !(supplierConfigSearchCriteria.getProductName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("productName"), supplierConfigSearchCriteria.getProductName()));
        }
        if (supplierConfigSearchCriteria.getProductCategoryName() != null && !(supplierConfigSearchCriteria.getProductCategoryName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("productCategoryName"), supplierConfigSearchCriteria.getProductCategoryName()));
        }
        if (supplierConfigSearchCriteria.getProductSubCategoryName() != null && !(supplierConfigSearchCriteria.getProductSubCategoryName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("productSubCategoryName"), supplierConfigSearchCriteria.getProductSubCategoryName()));
        }
        if (!StringUtils.isEmpty(supplierConfigSearchCriteria.getWorkFlowId()))
        {
            predicates.add(criteriaBuilder.equal(root.get("workflowId"), supplierConfigSearchCriteria.getWorkFlowId()));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        List<SupplierVoucherCodes> list = entityManager.createQuery(criteriaQuery).getResultList();

        TypedQuery<SupplierVoucherCodes> supplierConfigurationTypedQuery = entityManager.createQuery(criteriaQuery);
        if (list!=null && list.size()>0)
        {
            Integer size = voucherCodeSearchCriteria.getCount();
            Integer page = voucherCodeSearchCriteria.getPage();
            Integer firstRecordIndex = new Integer(0);
            if (!StringUtils.isEmpty(size) && !StringUtils.isEmpty(page)) {
                firstRecordIndex = (page - 1) * size;
            }
            else {
                size = new Integer(10);
                page = new Integer(1);
            }
            supplierConfigurationTypedQuery.setFirstResult(firstRecordIndex);
            supplierConfigurationTypedQuery.setMaxResults(size);
            noOfPages = list.size() / size;
            if (!(list.size() % size == 0))
                noOfPages = noOfPages + 1;

            map.put("data", supplierConfigurationTypedQuery.getResultList());
            map.put("size",size);
            map.put("noOfPages", noOfPages);
            map.put("page",page);
            map.put("totalCount",list.size());
        }
        else {
            map.put("data", supplierConfigurationTypedQuery.getResultList());
            map.put("size",voucherCodeSearchCriteria.getCount());
            map.put("noOfPages", noOfPages);
            map.put("page",voucherCodeSearchCriteria.getPage());
            map.put("totalCount",list.size());
        }




        return map;
    }

    @Override
    public Map<String,Object> reportGeneration(ReportGenerationCriteria reportGenerationCriteria) {
        List<SupplierVoucherCodes> list = null;
        Integer noOfPages=1;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplierVoucherCodes> criteriaQuery = criteriaBuilder.createQuery(SupplierVoucherCodes.class);
        Root<SupplierVoucherCodes> root = criteriaQuery.from(SupplierVoucherCodes.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (reportGenerationCriteria.getSupplierName() != null && !(reportGenerationCriteria.getSupplierName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("supplierName"), reportGenerationCriteria.getSupplierName()));
        }
        if (reportGenerationCriteria.getProductCategoryName() != null && !(reportGenerationCriteria.getProductCategoryName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("productCategoryName"), reportGenerationCriteria.getProductCategoryName()));
        }
        if (reportGenerationCriteria.getProductSubCategoryName() != null && !(reportGenerationCriteria.getProductSubCategoryName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("productSubCategoryName"), reportGenerationCriteria.getProductSubCategoryName()));
        }
        if (reportGenerationCriteria.getProductName() != null && !(reportGenerationCriteria.getProductName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("productName"), reportGenerationCriteria.getProductName()));
        }
        if (reportGenerationCriteria.getPaymentStatusToReleaseVoucher() != null && !(reportGenerationCriteria.getPaymentStatusToReleaseVoucher().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("paymentStatusToReleaseVoucher"), PaymentStatusToReleaseVoucher.getEnum(reportGenerationCriteria.getPaymentStatusToReleaseVoucher())));
        }
        if (!StringUtils.isEmpty(reportGenerationCriteria.getCompanyId()))
        {
            predicates.add(criteriaBuilder.equal(root.get("companyId"), reportGenerationCriteria.getCompanyId()));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }

        list = entityManager.createQuery(criteriaQuery).getResultList();
        TypedQuery<SupplierVoucherCodes> supplierVoucherCodesTypedQuery= entityManager.createQuery(criteriaQuery);
        Integer size = reportGenerationCriteria.getPageSize();
        Integer page = reportGenerationCriteria.getPageNo();
        if (size != null && page != null) {
            Integer recordNum = (page - 1) * size;
            supplierVoucherCodesTypedQuery.setFirstResult(recordNum);
            supplierVoucherCodesTypedQuery.setMaxResults(size);
        }

        if ((size!= null && !list.isEmpty()) && list.size() != 0 && !size.equals(0)) {
            noOfPages = list.size() / size;
            if (!(list.size() % size == 0))
                noOfPages = noOfPages + 1;
        }
        Map map = new HashMap();
        map.put("voucherCodes", supplierVoucherCodesTypedQuery.getResultList());
        map.put("noOfPages", noOfPages);
        return map;
    }

    @Override
    public List<SupplierVoucherCodes> searchToAssignVouchers(SupplierConfigSearchCriteria supplierConfigSearchCriteria) {
        List<SupplierVoucherCodes> list = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplierVoucherCodes> criteriaQuery = criteriaBuilder.createQuery(SupplierVoucherCodes.class);
        Root<SupplierVoucherCodes> root = criteriaQuery.from(SupplierVoucherCodes.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (supplierConfigSearchCriteria.getSupplierName() != null && !(StringUtils.isEmpty(supplierConfigSearchCriteria.getSupplierName()))) {
            predicates.add(criteriaBuilder.equal(root.get("supplierId"), supplierConfigSearchCriteria.getSupplierName()));
        }
        if (supplierConfigSearchCriteria.getProductName() != null && !(StringUtils.isEmpty(supplierConfigSearchCriteria.getProductName()))) {
            predicates.add(criteriaBuilder.equal(root.get("productName"), supplierConfigSearchCriteria.getProductName()));
        }
        if (supplierConfigSearchCriteria.getProductCategoryName() != null && !(StringUtils.isEmpty(supplierConfigSearchCriteria.getProductCategoryName()))) {
            predicates.add(criteriaBuilder.equal(root.get("productCategoryName"), supplierConfigSearchCriteria.getProductCategoryName()));
        }
        if (supplierConfigSearchCriteria.getProductSubCategoryName() != null && !(StringUtils.isEmpty(supplierConfigSearchCriteria.getProductSubCategoryName()))) {
            predicates.add(criteriaBuilder.equal(root.get("productSubCategoryName"), supplierConfigSearchCriteria.getProductSubCategoryName()));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        try {
            list = entityManager.createQuery(criteriaQuery).getResultList();
            return list;
        } catch (NoResultException e) {
            return list;
        }
    }

    @Override
    public void saveSupplierConfig(SupplierVoucherCodes supplierVoucherCodes) throws EntityExistsException {
        try {
            entityManager.persist(supplierVoucherCodes);
            entityManager.flush();
            entityManager.clear();
        } catch (EntityExistsException e) {
            throw new EntityExistsException(e);
        }

    }

    @Override
    public void deleteVoucherCode(String id, VoucherCode voucherCode)
    {
        SupplierVoucherCodes supplierVoucherCodes1 = entityManager.find(SupplierVoucherCodes.class, id);
        supplierVoucherCodes1.getVoucherCodes().remove(voucherCode);
        entityManager.merge(supplierVoucherCodes1);

    }


}
