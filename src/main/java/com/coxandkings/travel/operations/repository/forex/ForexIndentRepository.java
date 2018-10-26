package com.coxandkings.travel.operations.repository.forex;

import com.coxandkings.travel.operations.enums.forex.IndentStatus;
import com.coxandkings.travel.operations.enums.forex.IndentType;
import com.coxandkings.travel.operations.model.forex.ForexBooking;
import com.coxandkings.travel.operations.model.forex.ForexIndent;

import java.util.List;

public interface ForexIndentRepository {

    ForexIndent saveOrUpdate(ForexIndent forexIndent);

    ForexIndent getById(String id);

    List<ForexIndent> getIndentsByForexId(String id);

    List<ForexIndent> getIndentsByRequestId(String id);

    ForexIndent getIndentsByType(String id, IndentType type);

    List<String> getSupplierListForGivenName(String name);

    List<String> getSupplierList();

    Integer getIndentStatusCount(IndentStatus status);

    ForexBooking getForexByIndentId(String indentId);
}
