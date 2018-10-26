package com.coxandkings.travel.operations.repository.fileProfitabilityModified.Impl;

import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchCriteria;
import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchReportCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.FileProfitabilityBooking;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.PaxBreakDown;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.FinalSupplierLiability;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalServiceOrder;
import com.coxandkings.travel.operations.repository.fileProfitabilityModified.FileProfitabilityModifiedRepository;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository("fileProfitabilityRepoImpl")
@org.springframework.transaction.annotation.Transactional(readOnly = false)
public class FileProfModifiedRepoImpl extends SimpleJpaRepository<FileProfitabilityBooking, String> implements FileProfitabilityModifiedRepository {

    private static final Logger logger = Logger.getLogger(FileProfModifiedRepoImpl.class);

    private EntityManager entityManager;

    public FileProfModifiedRepoImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(FileProfitabilityBooking.class, em);
        entityManager = em;
    }

    @Autowired
    private UserService userService;

    @Override
    public FileProfitabilityBooking saveOrUpdateFileProfitability(FileProfitabilityBooking fileProfitabilityBooking) {
        return this.saveAndFlush(fileProfitabilityBooking);
    }

    @Override
    public FileProfitabilityBooking getFileProfitabilityByFileId(String id) {

        return this.findOne(id);
    }


    @Override
    public List<FileProfitabilityBooking> getFileProfBookByCriteria(FileProfSearchCriteria criteria) {
        List<FileProfitabilityBooking> fileProfitabilityBooking = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileProfitabilityBooking> criteriaQuery = criteriaBuilder.createQuery(FileProfitabilityBooking.class);
        Root<FileProfitabilityBooking> root = criteriaQuery.from(FileProfitabilityBooking.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getFileProfTypes() != null) {
            predicates.add(criteriaBuilder.equal(root.get("bookingType"), criteria.getFileProfTypes()));
        }
        if (criteria.getBookingRefNumber() != null) {
            predicates.add(criteriaBuilder.equal(root.get("bookingReferenceNumber"), criteria.getBookingRefNumber()));
        }

        if (criteria.getIsPassengerwise()) {
            predicates.add(criteriaBuilder.equal(root.get("isPassengerwise"), criteria.getIsPassengerwise()));
        }
        if (criteria.getIsTransportation()) {
            predicates.add(criteriaBuilder.equal(root.get("isTranspotation"), criteria.getIsTransportation()));
        }

        if (criteria.getIsAccomodation()) {
            predicates.add(criteriaBuilder.equal(root.get("isAccomodation"), criteria.getIsAccomodation()));
        }
        if (criteria.isRoomwise()) {
            predicates.add(criteriaBuilder.equal(root.get("isRoomwise"), criteria.isRoomwise()));
        }

        if (criteria.getOrderId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("orderId"), criteria.getOrderId()));
        }

        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        try {
            List<FileProfitabilityBooking> fileProfitabilityBookings = entityManager.createQuery(criteriaQuery).getResultList();
            return fileProfitabilityBooking;
        } catch (NoResultException e) {
            return fileProfitabilityBooking;
        }
    }

    @Override
    public List<FileProfitabilityBooking> getListOfFileProfsWRTCriteria(FileProfSearchCriteria fileProfBookingCriteria) throws OperationException{
        try {
            List<FileProfitabilityBooking> fileProfitabilityBookingList = null;
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<FileProfitabilityBooking> criteriaQuery = criteriaBuilder.createQuery(FileProfitabilityBooking.class);
            Root<FileProfitabilityBooking> root = criteriaQuery.from(FileProfitabilityBooking.class);
            criteriaQuery.select(root);

            List<Predicate> predicates = new ArrayList<>();

            predicates = getPredicateForCompany(root, criteriaBuilder);

            if (fileProfBookingCriteria.getDestinationLocation() != null) {
                predicates.add(criteriaBuilder.equal(root.get("destinationLocation"), fileProfBookingCriteria.getDestinationLocation()));
            }
            if (fileProfBookingCriteria.getDepartureLocation() != null) {
                predicates.add(criteriaBuilder.equal(root.get("departureLocation"), fileProfBookingCriteria.getDepartureLocation()));
            }

            if (fileProfBookingCriteria.getTravelDateFrom() != null && fileProfBookingCriteria.getTravelDateTo() != null) {
                predicates.add(criteriaBuilder.between(root.get("bookingDateZDT"), fileProfBookingCriteria.getTravelDateFrom(), fileProfBookingCriteria.getTravelDateTo()));
            }
            if (fileProfBookingCriteria.getIsTransportation()) {
                predicates.add(criteriaBuilder.equal(root.get("isTranspotation"), fileProfBookingCriteria.getIsTransportation()));
            }

            if (fileProfBookingCriteria.getIsAccomodation()) {
                predicates.add(criteriaBuilder.equal(root.get("isAccomodation"), fileProfBookingCriteria.getIsAccomodation()));
            }
            if (fileProfBookingCriteria.getIsPassengerwise()) {
                predicates.add(criteriaBuilder.equal(root.get("isPassengerwise"), fileProfBookingCriteria.getIsPassengerwise()));
            }
            if (fileProfBookingCriteria.isRoomwise()) {
                predicates.add(criteriaBuilder.equal(root.get("isRoomwise"), fileProfBookingCriteria.isRoomwise()));
            }

            if (fileProfBookingCriteria.getProdcutCategory() != null) {
                predicates.add(criteriaBuilder.equal(root.get("productCategory"), fileProfBookingCriteria.getProdcutCategory()));
            }

            if (fileProfBookingCriteria.getProductSubCategory() != null) {
                predicates.add(criteriaBuilder.equal(root.get("productSubCategory"), fileProfBookingCriteria.getProductSubCategory()));
            }

            getPredsForSearch(fileProfBookingCriteria, criteriaBuilder, root, predicates);

            if (fileProfBookingCriteria.getOrderId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("orderId"), fileProfBookingCriteria.getOrderId()));
            }
            if (!predicates.isEmpty()) {
                criteriaQuery.where(predicates.toArray(new Predicate[0]));
            }

            fileProfitabilityBookingList = entityManager.createQuery(criteriaQuery).getResultList();

            return fileProfitabilityBookingList;
        }catch(Exception e)
        {
            logger.error("Exception occurred in Class: FileProfModifiedRepoImpl Method: getListOfFileProfsWRTCriteria");
            throw new OperationException();
        }
    }

    private void getPredsForAutoSugg(FileProfSearchCriteria fileProfBookingCriteria, CriteriaBuilder criteriaBuilder, Root<FileProfitabilityBooking> root, List<Predicate> predicates) throws OperationException {
        try {
            if (fileProfBookingCriteria.getProductName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + fileProfBookingCriteria.getProductName().trim().toLowerCase() + "%"));
            }
            if (fileProfBookingCriteria.getBookingRefNumber() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("bookingReferenceNumber")), "%" + fileProfBookingCriteria.getBookingRefNumber().trim().toLowerCase() + "%"));
            }
            if (fileProfBookingCriteria.getLeadPassengerName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("paxBreakDown").get("leadPassengerName")), "%" + fileProfBookingCriteria.getLeadPassengerName().trim().toLowerCase() + "%"));
            }
        }catch(Exception e)
        {
            logger.error("Exception occurred in Class: FileProfModifiedRepoImpl Method: getPredsForAutoSugg");
            throw new OperationException(Constants.OPS_ERR_20100);
        }
    }

    private void getPredsForSearch(FileProfSearchCriteria fileProfBookingCriteria, CriteriaBuilder criteriaBuilder, Root<FileProfitabilityBooking> root, List<Predicate> predicates) throws OperationException {
        try {
            if (fileProfBookingCriteria.getProductName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("productName"), fileProfBookingCriteria.getProductName()));
            }
            if (fileProfBookingCriteria.getBookingRefNumber() != null) {
                predicates.add(criteriaBuilder.equal(root.get("bookingReferenceNumber"), fileProfBookingCriteria.getBookingRefNumber()));
            }
            if (fileProfBookingCriteria.getLeadPassengerName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("paxBreakDown").get("leadPassengerName"), fileProfBookingCriteria.getLeadPassengerName()));
            }
        }catch(Exception e)
        {
            throw new OperationException(Constants.OPS_ERR_20100);
        }
    }

    @Override
    public List<FileProfitabilityBooking> getListOfFileProfsWRTCriteria(FileProfSearchReportCriteria fileProfBookingCriteria) {
        List<FileProfitabilityBooking> fileProfitabilityBookingList = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileProfitabilityBooking> criteriaQuery = criteriaBuilder.createQuery(FileProfitabilityBooking.class);
        Root<FileProfitabilityBooking> root = criteriaQuery.from(FileProfitabilityBooking.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        predicates = getPredicateForCompany(root, criteriaBuilder);


        if (fileProfBookingCriteria.getDestinationLocation() != null) {
            predicates.add(criteriaBuilder.equal(root.get("destinationLocation"), fileProfBookingCriteria.getDestinationLocation()));
        }
        if (fileProfBookingCriteria.getDepartureLocation() != null) {
            predicates.add(criteriaBuilder.equal(root.get("departureLocation"), fileProfBookingCriteria.getDepartureLocation()));
        }

        if (fileProfBookingCriteria.getTravelDateFrom() != null && fileProfBookingCriteria.getTravelDateTo() != null) {
            predicates.add(criteriaBuilder.between(root.get("bookingDateZDT"), fileProfBookingCriteria.getTravelDateFrom(), fileProfBookingCriteria.getTravelDateTo()));
        }
        if (fileProfBookingCriteria.isTransportation()) {
            predicates.add(criteriaBuilder.equal(root.get("isTranspotation"), fileProfBookingCriteria.isTransportation()));
        }

        if (fileProfBookingCriteria.isAccomodation()) {
            predicates.add(criteriaBuilder.equal(root.get("isAccomodation"), fileProfBookingCriteria.isAccomodation()));
        }
        if (fileProfBookingCriteria.isPassengerwise()) {
            predicates.add(criteriaBuilder.equal(root.get("isPassengerwise"), fileProfBookingCriteria.isPassengerwise()));
        }
        if (fileProfBookingCriteria.isRoomwise()) {
            predicates.add(criteriaBuilder.equal(root.get("isRoomwise"), fileProfBookingCriteria.isRoomwise()));
        }

        if (fileProfBookingCriteria.getProdcutCategory() != null) {
            predicates.add(criteriaBuilder.equal(root.get("productCategory"), fileProfBookingCriteria.getProdcutCategory()));
        }

        if (fileProfBookingCriteria.getProductSubCategory() != null) {
            predicates.add(criteriaBuilder.equal(root.get("productSubCategory"), fileProfBookingCriteria.getProductSubCategory()));
        }

        getPredsForSearch(fileProfBookingCriteria, criteriaBuilder, root, predicates);

        if (fileProfBookingCriteria.getOrderId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("orderId"), fileProfBookingCriteria.getOrderId()));
        }

        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));

        }
        fileProfitabilityBookingList = entityManager.createQuery(criteriaQuery).getResultList();

        return fileProfitabilityBookingList;
    }

    private void getPredsForSearch(FileProfSearchReportCriteria fileProfBookingCriteria, CriteriaBuilder criteriaBuilder, Root<FileProfitabilityBooking> root, List<Predicate> predicates) {

        if (fileProfBookingCriteria.getProductName() != null) {
            predicates.add(criteriaBuilder.equal(root.get("productName"), fileProfBookingCriteria.getProductName()));
        }
        if (fileProfBookingCriteria.getBookingRefNumber() != null) {
            predicates.add(criteriaBuilder.equal(root.get("bookingReferenceNumber"), fileProfBookingCriteria.getBookingRefNumber()));
        }
        if (fileProfBookingCriteria.getLeadPassengerName() != null) {
            predicates.add(criteriaBuilder.equal(root.get("paxBreakDown").get("leadPassengerName"), fileProfBookingCriteria.getLeadPassengerName()));
        }
    }

    @Override
    public List<FileProfitabilityBooking> getAutoSuggest(FileProfSearchCriteria fileProfBookingCriteria) throws OperationException{
        List<FileProfitabilityBooking> fileProfitabilityBookingList = null;
        try {

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<FileProfitabilityBooking> criteriaQuery = criteriaBuilder.createQuery(FileProfitabilityBooking.class);
            Root<FileProfitabilityBooking> root = criteriaQuery.from(FileProfitabilityBooking.class);
            criteriaQuery.select(root);

            List<Predicate> predicates = new ArrayList<>();

            predicates = getPredicateForCompany(root, criteriaBuilder);//Company predicates

            if (fileProfBookingCriteria.getTravelDateFrom() != null && fileProfBookingCriteria.getTravelDateTo() != null) {
                predicates.add(criteriaBuilder.between(root.get("bookingDateZDT"), fileProfBookingCriteria.getTravelDateFrom(), fileProfBookingCriteria.getTravelDateTo()));
            }

            getPredsForAutoSugg(fileProfBookingCriteria, criteriaBuilder, root, predicates);//Auto suggest value predicates

            if (!predicates.isEmpty()) {
                criteriaQuery.where(predicates.toArray(new Predicate[0]));
            }

            fileProfitabilityBookingList = entityManager.createQuery(criteriaQuery).getResultList();
        } catch(OperationException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Exception occurred in Class: FileProfModifiedRepoImpl Method: getAutoSuggest");
            throw new OperationException(Constants.OPS_ERR_20100);
        }
        return fileProfitabilityBookingList;
    }

    @Override
    public void updateAllDBRecs() {

        List<FileProfitabilityBooking> fileProfs = this.findAll();
        for (FileProfitabilityBooking fpb : fileProfs) {
            if (fpb.getPaxBreakDown() != null) {
                PaxBreakDown paxBreakDown = fpb.getPaxBreakDown();
                if (paxBreakDown.getLeadPassengerName() != null && !paxBreakDown.getLeadPassengerName().isEmpty()) {
                    fpb.setLeadPassName(paxBreakDown.getLeadPassengerName());
                    this.saveAndFlush(fpb);
                }
            }
        }
    }

    @Override
    public List<FileProfitabilityBooking> getFileProfsWRTBookingId(FileProfSearchCriteria fileProfBookingCriteria) {
        List<FileProfitabilityBooking> fileProfitabilityBookingList = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileProfitabilityBooking> criteriaQuery = criteriaBuilder.createQuery(FileProfitabilityBooking.class);
        Root<FileProfitabilityBooking> root = criteriaQuery.from(FileProfitabilityBooking.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        predicates = getPredicateForCompany(root, criteriaBuilder);

        if (fileProfBookingCriteria.getTravelDateFrom() != null && fileProfBookingCriteria.getTravelDateTo() != null) {
            predicates.add(criteriaBuilder.between(root.get("bookingDateZDT"), fileProfBookingCriteria.getTravelDateFrom(), fileProfBookingCriteria.getTravelDateTo()));
        } else {
            if (fileProfBookingCriteria.getTravelDateFrom() != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("bookingDateZDT"), fileProfBookingCriteria.getTravelDateFrom()));
            else
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("bookingDateZDT"), fileProfBookingCriteria.getTravelDateTo()));
        }

        if (fileProfBookingCriteria.getBookingRefNumber() != null) {
            predicates.add(criteriaBuilder.equal(root.get("bookingReferenceNumber"), fileProfBookingCriteria.getBookingRefNumber()));
        }

        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        fileProfitabilityBookingList = entityManager.createQuery(criteriaQuery).getResultList();

        if (fileProfitabilityBookingList == null || fileProfitabilityBookingList.size() == 0) {

            fileProfitabilityBookingList = getListOfFPsBetweenDates(fileProfBookingCriteria);
        }

        return fileProfitabilityBookingList;

    }

    public List<FileProfitabilityBooking> getListOfFPsBetweenDates(FileProfSearchCriteria fileProfBookingCriteria) {
        List<FileProfitabilityBooking> fileProfitabilityBookingList = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileProfitabilityBooking> criteriaQuery = criteriaBuilder.createQuery(FileProfitabilityBooking.class);
        Root<FileProfitabilityBooking> root = criteriaQuery.from(FileProfitabilityBooking.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (fileProfBookingCriteria.getTravelDateFrom() != null && fileProfBookingCriteria.getTravelDateTo() != null) {
            predicates.add(criteriaBuilder.between(root.get("bookingDateZDT"), fileProfBookingCriteria.getTravelDateFrom(), fileProfBookingCriteria.getTravelDateTo()));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        fileProfitabilityBookingList = entityManager.createQuery(criteriaQuery).getResultList();
        return fileProfitabilityBookingList;
    }

    private List<Predicate> getPredicateForCompany(Root<FileProfitabilityBooking> root, CriteriaBuilder criteriaBuilder) {

       /* if (!StringUtils.isEmpty(loggedInUser.getBU()))
            predicateList.add(criteriaBuilder.equal(root.get("BU"), loggedInUser.getBU()));

        if (!StringUtils.isEmpty(loggedInUser.getSBU()))
            predicateList.add(criteriaBuilder.equal(root.get("SBU"), loggedInUser.getSBU()));*/     List<Predicate> predicateList = new ArrayList<>();
        OpsUser loggedInUser = userService.getLoggedInUser();


        if (!StringUtils.isEmpty(loggedInUser.getCompanyId()))
            predicateList.add(criteriaBuilder.equal(root.get("companyId"), loggedInUser.getCompanyId()));

       /* if (!StringUtils.isEmpty(loggedInUser.getCompanyName()))
            predicateList.add(criteriaBuilder.equal(root.get("companyName"), loggedInUser.getCompanyName()));

        if (!StringUtils.isEmpty(loggedInUser.getCompanyGroupId()))
            predicateList.add(criteriaBuilder.equal(root.get("companyGroupId"), loggedInUser.getCompanyGroupId()));

        if (!StringUtils.isEmpty(loggedInUser.getCompanyGroupName()))
            predicateList.add(criteriaBuilder.equal(root.get("companyGroupName"), loggedInUser.getCompanyGroupName()));

        if (!StringUtils.isEmpty(loggedInUser.getGroupOfCompanyId()))
            predicateList.add(criteriaBuilder.equal(root.get("groupOfCompanyId"), loggedInUser.getGroupOfCompanyId()));

        if (!StringUtils.isEmpty(loggedInUser.getGroupOfCompanyName()))
            predicateList.add(criteriaBuilder.equal(root.get("groupOfCompanyName"), loggedInUser.getGroupOfCompanyName()));*/

      /*  if (!StringUtils.isEmpty(loggedInUser.getBranchName()))
            predicateList.add(criteriaBuilder.equal(root.get("branchName"), loggedInUser.getBranchName()));*/

        return predicateList;

    }


}

