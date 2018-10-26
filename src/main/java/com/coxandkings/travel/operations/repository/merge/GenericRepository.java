package com.coxandkings.travel.operations.repository.merge;

import com.coxandkings.travel.operations.exceptions.OperationException;

import java.util.List;

public interface GenericRepository<T, U> {
    T saveOrUpdate(T t);

    void remove(String id);

    T getById(String id);

    List<T> getByCriteria(U u);

    T getMatch(T t) throws OperationException;

    Boolean isPresent(String id);
}
