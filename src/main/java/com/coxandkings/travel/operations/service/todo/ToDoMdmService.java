package com.coxandkings.travel.operations.service.todo;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.todo.ToDoClientResource;
import com.coxandkings.travel.operations.resource.todo.ToDoCompanyMarketResource;
import com.coxandkings.travel.operations.resource.todo.ToDoCompanyResource;
import com.coxandkings.travel.operations.resource.todo.ToDoUserResource;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ToDoMdmService {
//    List<ToDoCompanyResource> getCompanies(String groupCompanyId) throws JSONException, IOException, OperationException;
    List<ToDoCompanyMarketResource> getCompanyMarkets(String companyId) throws JSONException, OperationException, IOException;
    List<ToDoUserResource> assign() throws IOException, OperationException;

    ToDoCompanyResource getCompanyById(String companyId) throws JSONException, OperationException, IOException;

    ToDoClientResource getClientById(String clientType, String clientId,
                                     String clientCategory, String clientSubCategory)throws OperationException;

    ToDoClientResource getClientById(String clientType, String clientId) throws OperationException;

    ToDoCompanyMarketResource getCompanyMarketById(String companyMarketId);

    ToDoUserResource getUserById(String assignedBy) throws JSONException, OperationException, IOException;

    List<ToDoCompanyResource> getCompanies(List<ToDoCompanyResource> toDoCompanyResources) throws JSONException, OperationException, IOException;

    List<ToDoClientResource> getClients(List<ToDoClientResource> toDoClientResources) throws JSONException, OperationException, IOException;

    List<ToDoUserResource> getUsers(List<ToDoUserResource> toDoUserResources) throws JSONException, OperationException, IOException;

    Map<String,ToDoUserResource> getUsersMap(List<ToDoUserResource> toDoUserResources) throws JSONException, OperationException, IOException;

    List<ToDoUserResource> assign(String functionalArea);

    List<ToDoCompanyResource> getCompanies(String userId) throws JSONException, OperationException, IOException;

    List<ToDoUserResource> getSubordinates(String userId) throws JSONException, OperationException, IOException;

    List<ToDoUserResource> getFileHandlersForSearch();

    String getProductById(String id);
}
