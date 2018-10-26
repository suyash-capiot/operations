package com.coxandkings.travel.operations.repository.todo.impl;

import com.coxandkings.travel.ext.model.finance.invoice.Client;
import com.coxandkings.travel.operations.criteria.todo.ToDoCriteria;
import com.coxandkings.travel.operations.enums.prodreview.ClientType;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.enums.user.UserType;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.model.todo.ToDoTaskDetails;
import com.coxandkings.travel.operations.repository.todo.ToDoTaskRepository;
import com.coxandkings.travel.operations.resource.todo.ToDoResponse;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Transactional
class ToDoTaskRepositoryImpl extends SimpleJpaRepository<ToDoTask, String> implements ToDoTaskRepository {

    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String schemaName;

    private EntityManager entityManager;

    public ToDoTaskRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(ToDoTask.class, em);
        entityManager = em;
    }

    private Predicate[] getPredicates(ToDoCriteria toDoCriteria, CriteriaBuilder criteriaBuilder, Root<ToDoTask> root, Boolean checkEmptyOrNull) {
        List<Predicate> predicates = new ArrayList<>();
        String[] ids = toDoCriteria.getIds();
        if (ids != null && ids.length > 0) {
            predicates.add(root.get("id").in(ids));
        }

        String[] excludeIds = toDoCriteria.getExcludeIds();
        if (excludeIds != null && excludeIds.length > 0) {
            predicates.add(criteriaBuilder.not(root.get("id").in(ids)));
        }

        String referenceId = toDoCriteria.getReferenceId();
        if (!StringUtils.isEmpty(referenceId)) {
            predicates.add(criteriaBuilder.equal(root.get("referenceId"), referenceId));
        }

        String bookingRefId = toDoCriteria.getBookingRefId();
        if (!StringUtils.isEmpty(bookingRefId)) {
            predicates.add(criteriaBuilder.equal(root.get("bookingRefId"), bookingRefId));
        }

        String productId = toDoCriteria.getProductId();
        if (!StringUtils.isEmpty(productId)) {
            predicates.add(criteriaBuilder.equal(root.get("productId"), productId));
        } else if (checkEmptyOrNull) {
            // predicates.add(criteriaBuilder.equal(root.get("productId"), ""));
        }

        String clientCategoryId = toDoCriteria.getClientCategoryId();
        if (!StringUtils.isEmpty(clientCategoryId)) {
            predicates.add(criteriaBuilder.equal(root.get("clientCategoryId"), clientCategoryId));
        }

        String clientSubCategoryId = toDoCriteria.getClientSubCategoryId();
        if (!StringUtils.isEmpty(clientSubCategoryId)) {
            predicates.add(criteriaBuilder.equal(root.get("clientSubCategoryId"), clientSubCategoryId));
        }

        String clientTypeId = toDoCriteria.getClientTypeId();
        if (!StringUtils.isEmpty(clientTypeId)) {
            predicates.add(criteriaBuilder.equal(root.get("clientTypeId"), toDoCriteria.getClientTypeId()));
        } else if (checkEmptyOrNull) {
//            predicates.add(criteriaBuilder.isEmpty(root.get("clientTypeId")));
        }

        String companyId = toDoCriteria.getCompanyId();
        if (!StringUtils.isEmpty(companyId)) {
            predicates.add(criteriaBuilder.equal(root.get("companyId"), companyId));
        } else if (checkEmptyOrNull) {
//            predicates.add(criteriaBuilder.isEmpty(root.get("companyId")));
        }

        String companyMarketId = toDoCriteria.getCompanyMarketId();
        if (!StringUtils.isEmpty(companyMarketId)) {
            predicates.add(criteriaBuilder.equal(root.get("companyMarketId"), companyMarketId));
        }

        ToDoTaskNameValues taskName = ToDoTaskNameValues.fromString(toDoCriteria.getTaskNameId());
        if (!StringUtils.isEmpty(taskName)) {
//            Join<ToDoTask, ToDoTaskName> toDoTaskDetails = root.join("taskName");
            predicates.add(criteriaBuilder.equal(root.get("taskName"), taskName));
        }

        ToDoTaskTypeValues taskTypeId = ToDoTaskTypeValues.fromString(toDoCriteria.getTaskTypeId());
        if (!StringUtils.isEmpty(taskTypeId)) {
            predicates.add(criteriaBuilder.equal(root.get("taskType"), taskTypeId));
        } else{
            predicates.add(criteriaBuilder.equal(root.get("taskType"), ToDoTaskTypeValues.MAIN));
        }
//        else {
//            predicates.add(criteriaBuilder.equal(root.get("taskType"), ToDoTaskTypeValues.MAIN));
//        }

        String taskSubTypeId = toDoCriteria.getTaskSubTypeId();
        if (!StringUtils.isEmpty(taskSubTypeId)) {
            predicates.add(criteriaBuilder.equal(root.get("taskSubType"), ToDoTaskSubTypeValues.fromName(taskSubTypeId)));
        }


        ToDoFunctionalAreaValues functionalAreaId = ToDoFunctionalAreaValues.fromString(toDoCriteria.getTaskFunctionalAreaId());
        if (!StringUtils.isEmpty(functionalAreaId)) {
            predicates.add(criteriaBuilder.equal(root.get("taskFunctionalArea"), functionalAreaId));
        }

        List<ToDoTaskStatusValues> toDoTaskStatusValues = new ArrayList<>();
        if (toDoCriteria.getTaskStatusIds() != null) {
            for (String statusValue : toDoCriteria.getTaskStatusIds()) {
                toDoTaskStatusValues.add(ToDoTaskStatusValues.fromString(statusValue));
            }
            if (!toDoTaskStatusValues.isEmpty()) {
                predicates.add(root.get("taskStatus").in(toDoTaskStatusValues));
            }
        }

        Boolean overdue = toDoCriteria.getOverdue();
        if (overdue != null) {
            predicates.add(criteriaBuilder.equal(root.get("overdue"), overdue));
        }

        String priorityId = toDoCriteria.getTaskPriorityId();
        if (!StringUtils.isEmpty(priorityId)) {
            ToDoTaskPriorityValues aPriorityValue = ToDoTaskPriorityValues.fromString(priorityId);
            predicates.add(criteriaBuilder.equal(root.get("taskPriority"), aPriorityValue));
        }

        String productCategory = toDoCriteria.getProductCategory();
        if (!StringUtils.isEmpty(productCategory)) {
            predicates.add(criteriaBuilder.equal(root.get("productCategory"), productCategory));
        }

        String productSubCategory = toDoCriteria.getProductSubCategory();
        if (!StringUtils.isEmpty(productSubCategory)) {
            predicates.add(criteriaBuilder.equal(root.get("productSubCategory"), productSubCategory));
        }

        /**
         * Based on Task Area (my tasks or task assigned by me or task assigned to me)
         * For Approver user:
         * My Tasks: Show all tasks which has no file handlers
         * Task assigned by me: Show tasks where assigned by is Approver user
         * Task assigned to me: Show tasks where file handler or secondary file handler is Approver user
         *
         * For Ops user (or Finance user):
         * My Tasks = Task Assigned to me: Show tasks where file handler or secondary file handler is Ops user
         * Task assigned by me: Show tasks where assigned by is Ops user
         */
        String taskArea = toDoCriteria.getTaskArea();
        if (taskArea == null) {
            taskArea = "assigned_to_me";
        }

        UserType anUserType = toDoCriteria.getUserType();
        if (anUserType == null) {
            anUserType = UserType.OPS_USER;
        }

        String[] fileHandlerIDs = toDoCriteria.getFileHandlerIds();

        switch (anUserType) {
            case OPS_PRODUCT_USER:
            case OPS_MARKETING_USER:
            case OPS_SUPPLIER_EXTRANET:
            case OPS_USER: {
                if (taskArea.equalsIgnoreCase("my_tasks")) {
                    if (fileHandlerIDs != null && fileHandlerIDs.length > 0 &&
                            !StringUtils.isEmpty(toDoCriteria.getAssignedBy())) {
                        predicates.add(criteriaBuilder.or(
                                root.get("fileHandlerId").in(toDoCriteria.getFileHandlerIds()),
                                criteriaBuilder.like(root.get("assignedBy"), toDoCriteria.getAssignedBy())));
                    }
                } else if (taskArea.equalsIgnoreCase("assigned_to_me")) {

                    if (fileHandlerIDs != null && fileHandlerIDs.length > 0) {
                        predicates.add(root.get("fileHandlerId").in(fileHandlerIDs));
                    }
                    String secondaryFileHandlerId = toDoCriteria.getSecondaryFileHandlerId();
                    if (!StringUtils.isEmpty(secondaryFileHandlerId)) {
                        predicates.add(criteriaBuilder.equal(root.get("secondaryFileHandlerId"), secondaryFileHandlerId));
                    }

                } else if (taskArea.equalsIgnoreCase("assigned_by_me")) {
                    String assignedBy = toDoCriteria.getAssignedBy();
                    predicates.add(root.get("assignedBy").in(assignedBy));
                }
            }
            break;

            case OPS_EMAIL_MONITOR_USER:
            case OPS_APPROVER_USER: {
                if (taskArea.equalsIgnoreCase("my_tasks")) {
                    if (fileHandlerIDs != null && fileHandlerIDs.length > 0
                            && !StringUtils.isEmpty(toDoCriteria.getAssignedBy())) {
                        predicates.add(criteriaBuilder.or(
                                root.get("fileHandlerId").in(toDoCriteria.getFileHandlerIds()),
                                criteriaBuilder.like(root.get("assignedBy"), toDoCriteria.getAssignedBy()),
                                root.get("fileHandlerId").isNull()));
                    }
                } else if (taskArea.equalsIgnoreCase("assigned_to_me")) {
                    if (fileHandlerIDs != null && fileHandlerIDs.length > 0) {
                        predicates.add(root.get("fileHandlerId").in(fileHandlerIDs));
                    }

                    String secondaryFileHandlerId = toDoCriteria.getSecondaryFileHandlerId();
                    if (!StringUtils.isEmpty(secondaryFileHandlerId)) {
                        predicates.add(criteriaBuilder.equal(root.get("secondaryFileHandlerId"), secondaryFileHandlerId));
                    }
                } else if (taskArea.equalsIgnoreCase("assigned_by_me")) {
                    String assignedBy = toDoCriteria.getAssignedBy();
                    predicates.add(root.get("assignedBy").in(assignedBy));
                }
            }
            break;
        }

        /*if( taskArea.equalsIgnoreCase( "my_tasks" ))    {
            predicates.add( criteriaBuilder.isNull( root.get( "fileHandlerId" ) ));
        }
        else if( taskArea.equalsIgnoreCase( "assigned_to_me" )) {
            String fileHandlerId = toDoCriteria.getFileHandlerId();
            if(!StringUtils.isEmpty(fileHandlerId)) {
                predicates.add(criteriaBuilder.equal(root.get("fileHandlerId"), (fileHandlerId)));
            }

            String secondaryFileHandlerId = toDoCriteria.getSecondaryFileHandlerId();
            if (!StringUtils.isEmpty(secondaryFileHandlerId)) {
                predicates.add(criteriaBuilder.equal(root.get("secondaryFileHandlerId"), secondaryFileHandlerId));
            }
        }
        else if( taskArea.equalsIgnoreCase( "assigned_by_me" )) {
            String assignedBy = toDoCriteria.getAssignedBy();
            if (!StringUtils.isEmpty(assignedBy)) {
                predicates.add(criteriaBuilder.equal(root.get("assignedBy"), assignedBy));
            }
        }*/

        /*String[] fileHandlerIds = toDoCriteria.getFileHandlerIds();
        if (fileHandlerIds != null && fileHandlerIds.length != 0) {
            predicates.add(root.get("fileHandlerId").in(fileHandlerIds));
        } else {
            String fileHandlerId = toDoCriteria.getFileHandlerId();
            if(!StringUtils.isEmpty(fileHandlerId)) {
                predicates.add(criteriaBuilder.equal(root.get("fileHandlerId"), (fileHandlerId)));
            }
        }

        String secondaryFileHandlerId = toDoCriteria.getSecondaryFileHandlerId();
        if (!StringUtils.isEmpty(secondaryFileHandlerId)) {
            predicates.add(criteriaBuilder.equal(root.get("secondaryFileHandlerId"), secondaryFileHandlerId));
        }*/

        String suggestedActions = toDoCriteria.getSuggestedActions();
        if (!StringUtils.isEmpty(suggestedActions)) {
            predicates.add(criteriaBuilder.equal(root.get("suggestedActions"), suggestedActions));
        }

        String mainTaskStatusTriggerId = toDoCriteria.getMainTaskStatusTriggerId();
        if (!StringUtils.isEmpty(mainTaskStatusTriggerId)) {
            predicates.add(criteriaBuilder.equal(root.get("mainTaskStatusTriggerId"), mainTaskStatusTriggerId));
        }

        String mainTaskId = toDoCriteria.getMainTaskId();
        if (!StringUtils.isEmpty(mainTaskId)) {
            Join<ToDoTask, ToDoTask> parentToDoTask = root.join("parentToDoTask");
            predicates.add(criteriaBuilder.equal(parentToDoTask.get("id"), mainTaskId));
        }


        String lockedBy = toDoCriteria.getLockedBy();
        if (!StringUtils.isEmpty(lockedBy)) {
            Join<ToDoTask, ToDoTaskDetails> toDoTaskDetails = root.join("toDoTaskDetails");
            predicates.add(criteriaBuilder.equal(toDoTaskDetails.get("lockedBy"), lockedBy));
        }

        if (!CollectionUtils.isEmpty(predicates)) {
            return predicates.toArray(new Predicate[0]);
        }

        return new Predicate[0];
    }

    @Override
    public ToDoResponse getByCriteria(ToDoCriteria toDoCriteria, Boolean checkEmptyOrNull) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ToDoTask> taskCriteriaQuery = criteriaBuilder.createQuery(ToDoTask.class);

        Root<ToDoTask> root = taskCriteriaQuery.from(ToDoTask.class);

        Predicate[] predicates = getPredicates(toDoCriteria, criteriaBuilder, root, checkEmptyOrNull);

        if (predicates.length > 0) {
            taskCriteriaQuery.where(predicates);
        }

        String sortCriteria = toDoCriteria.getSortCriteria();
        if (sortCriteria != null) {
            taskCriteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortCriteria)));
            if (toDoCriteria.getDescending() != null && toDoCriteria.getDescending()) {
                taskCriteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortCriteria)));
            }
        }


        TypedQuery<ToDoTask> typedQuery = entityManager.createQuery(taskCriteriaQuery);

        if (toDoCriteria.getPageNumber() == null)
            toDoCriteria.setPageNumber(1);

        if (toDoCriteria.getPageSize() == null)
            toDoCriteria.setPageSize(10);

        typedQuery.setFirstResult((toDoCriteria.getPageNumber() - 1) * toDoCriteria.getPageSize());
        typedQuery.setMaxResults(toDoCriteria.getPageSize());

        Long count = getCountByCriteria(toDoCriteria);
        List<ToDoTask> tasks = typedQuery.getResultList();

        ToDoResponse toDoResponse = new ToDoResponse();
        toDoResponse.setContent(tasks);
        toDoResponse.setCount(count);
        Integer page = (int) Math.ceil((double) count / toDoCriteria.getPageSize());
        toDoResponse.setLast(page == toDoCriteria.getPageNumber());
        toDoResponse.setTotalPages(page);
        toDoResponse.setPageNumber(toDoCriteria.getPageNumber());
        toDoResponse.setPageSize(toDoCriteria.getPageSize());

        return toDoResponse;
    }

    @Override
    public Long getCountByCriteria(ToDoCriteria toDoCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> taskCriteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<ToDoTask> root = taskCriteriaQuery.from(ToDoTask.class);

        taskCriteriaQuery.select(criteriaBuilder.count(root));

        Predicate[] predicates = getPredicates(toDoCriteria, criteriaBuilder, root, false);

        if (predicates.length > 0) {
            taskCriteriaQuery.where(predicates);
        }

        Long count = entityManager.createQuery(taskCriteriaQuery).getSingleResult();
        return count;
    }

    @Override
    public void deleteById(String id) {
        delete(id);
        flush();
    }

    @Override
    public List<String> getAllFileHandlers() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> taskCriteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<ToDoTask> root = taskCriteriaQuery.from(ToDoTask.class);

        taskCriteriaQuery.select(root.get("fileHandlerId"));
        taskCriteriaQuery.distinct(true);

        return entityManager.createQuery(taskCriteriaQuery).getResultList();
    }

    @Override
    public ToDoTask getById(String taskId) {
        return this.findOne(taskId);
    }

    @Override
    public void remove(String id) {
        delete(id);
        flush();
    }

    @Override
    public Boolean isPresent(String id) {
        return exists(id);
    }

    @Override
    @Transactional
    public ToDoTask saveOrUpdate(ToDoTask task) {
        return saveAndFlush(task);
    }

    public ToDoResponse getAll() {
        ToDoCriteria toDoCriteria = new ToDoCriteria();
        toDoCriteria.setTaskTypeId("Main task");

        return getByCriteria(toDoCriteria, false);
    }

    @Override
    public List<HashMap> reassignUnreadTask() {
        Session session = entityManager.unwrap(Session.class);

        String sql = "UPDATE " + schemaName + ".to_do_task as TBL_TODOTASK " +
                "SET file_handler_id = " +
                "CASE WHEN TBL_TODOTASK.secondary_file_handler_id = null THEN NULL " +
                "ELSE TBL_TODOTASK.secondary_file_handler_id END," +
                "secondary_file_handler_id = null " +
                "FROM " + schemaName + ".to_do_task INNER JOIN " + schemaName + ".to_do_task_details " +
                "as TBL_TODOTASKDETAILS ON to_do_task.to_do_task_details_id =  TBL_TODOTASKDETAILS.id " +
                "WHERE TBL_TODOTASKDETAILS.read = false " +
                "and TBL_TODOTASK.alert_notified = false " +
                "and TBL_TODOTASK.due_on <= CURRENT_TIMESTAMP RETURNING TBL_TODOTASK.id";

        SQLQuery query = session.createSQLQuery(sql);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List<HashMap> results = query.list();

        return results;
    }
}
