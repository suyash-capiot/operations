package com.coxandkings.travel.operations.repository.manageproductupdates;

import com.coxandkings.travel.operations.enums.todo.ToDoFunctionalAreaValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.todo.ToDoSubType;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.resource.todo.ToDoFunctionalArea;
import com.coxandkings.travel.operations.resource.todo.ToDoStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ManageProductUpdatesToDoTaskRepositoryImpl extends SimpleJpaRepository<ToDoTask, String> implements ManageProductUpdatesToDoTaskRepository {

    private EntityManager entityManager;

    public ManageProductUpdatesToDoTaskRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em )   {
        super( ToDoTask.class, em);
        entityManager=em;
    }

    @Override
    public List<String> getToDoTasksBySubType( ToDoSubType subType, ToDoStatus status ) throws OperationException {

        List<String> resultingToDoTasks = null;

        CriteriaBuilder criteriaBuilder =   entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery   =   criteriaBuilder.createQuery( String.class);
        Root<ToDoTask> root=criteriaQuery.from( ToDoTask.class );
        criteriaQuery.select(root.get( "referenceId" ));

        List<Predicate> predicates = new ArrayList<>();

        if(!StringUtils.isEmpty( subType )) {
            Join<ToDoTask, ToDoSubType> toDoTaskDetails = root.join("taskSubType" );
            predicates.add( criteriaBuilder.equal( toDoTaskDetails.get("id"), subType.getId() ));
        }

        if(!StringUtils.isEmpty( status )) {
            Join<ToDoTask, ToDoStatus> toDoTaskDetails = root.join("taskStatus" );
            predicates.add( criteriaBuilder.notEqual( toDoTaskDetails.get("id"), status.getId() ));
        }

        ToDoFunctionalAreaValues functionalAreaId = ToDoFunctionalAreaValues.OPERATIONS;
        Join<ToDoTask, ToDoFunctionalArea> toDoTaskDetails = root.join("taskFunctionalArea");
        predicates.add(criteriaBuilder.equal(toDoTaskDetails.get("id"), functionalAreaId));

        if(!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        resultingToDoTasks = entityManager.createQuery( criteriaQuery ).getResultList();

        return resultingToDoTasks;
    }
}
