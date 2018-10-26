package com.coxandkings.travel.operations.service.remarks.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.helper.remarks.RoleDeserializer;
import com.coxandkings.travel.operations.helper.remarks.UserDeserializer;
import com.coxandkings.travel.operations.resource.remarks.RoleBasicInfo;
import com.coxandkings.travel.operations.resource.remarks.RoleMDMFilter;
import com.coxandkings.travel.operations.resource.remarks.UserBasicInfo;
import com.coxandkings.travel.operations.resource.remarks.UserMDMFilter;
import com.coxandkings.travel.operations.service.remarks.MDMUserService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Service
public class MDMUserServiceImpl implements MDMUserService {

    @Autowired
    MDMRestUtils mdmRestUtils;

    @Autowired
    JsonObjectProvider jsonObjectProvider;

    @Value(value = "${mdm.common.get.users}")
    private String toGetUserUrl;

    @Value(value = "${mdm.common.get.filter-based-users}")
    private String getFilterBasedUsers;

    @Value(value = "${mdm.common.get.filter-based-roles}")
    private String getFilterBasedRoles;

    /**
     * Purpose of this method to get All the Users
     * from MDM
     * @return
     */
    @Override
    public List<UserBasicInfo> getAllUsers() {
        String users = null;
        List<UserBasicInfo> userList = null;
        String childJson = null;

        try {
            users = mdmRestUtils.exchange(toGetUserUrl, HttpMethod.GET, String.class);
        } catch (OperationException e) {
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode treeNode = null;
        try {
            treeNode = mapper.readTree(users).at("/data");

            childJson = treeNode.toString();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(UserBasicInfo.class, new UserDeserializer());
            mapper.registerModule(module);
            userList = mapper.readValue(childJson, new TypeReference<List<UserBasicInfo>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userList;
    }

    /**
     * purpose of this method is to get the list of ops user
     * by passing roleName
     * @param roleName
     * @return
     */
    @Override
    public List<UserBasicInfo> getOpsUsersUsingRole(String roleName) {
        String users = null;
        List<UserBasicInfo> userList = null;
        String childJson = null;
        ResponseEntity<String> responseMDM = null;

        try {
            responseMDM = mdmRestUtils.exchange(this.createUrl(roleName), HttpMethod.GET, null, String.class);
        } catch (OperationException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode treeNode = null;
        try {
            treeNode = mapper.readTree(responseMDM.getBody()).at("/data");

            childJson = treeNode.toString();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(UserBasicInfo.class, new UserDeserializer());
            mapper.registerModule(module);
            userList = mapper.readValue(childJson, new TypeReference<List<UserBasicInfo>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }
    private URI createUrl(String roleName) {
        String filterString = null;
        UserMDMFilter userMDMFilter = new UserMDMFilter();
        userMDMFilter.setUserDetailsCompaniesRoleName(roleName);

        ObjectMapper aMapper = new ObjectMapper();
        try {
            filterString = aMapper.writeValueAsString(userMDMFilter);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return UriComponentsBuilder.fromUriString(getFilterBasedUsers + filterString + "&sort='_id'").build().encode().toUri();
    }

    /**
     * Purpose of this method is to get All the Roles
     * based on search parameter.
     * @param roleName
     * @return
     */
    @Override
    public List<RoleBasicInfo> searchRoles(StringBuilder roleName) {
        String roles = null;
        String filterString = null;
        List<RoleBasicInfo> roleList = null;
        String childJson = null;

        RoleMDMFilter roleMDMFilter = new RoleMDMFilter();
        roleName.insert(0,'/').insert(roleName.length(),"/");
        roleMDMFilter.setRoleName(roleName.toString());
        ObjectMapper aMapper = new ObjectMapper();

        //preparing Json
        try {
            filterString = aMapper.writeValueAsString(roleMDMFilter);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //preparing URL
        URI uri = UriComponentsBuilder.fromUriString(getFilterBasedRoles + filterString).build().encode().toUri();

        try {
            roles = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
        } catch (OperationException e) {
            e.printStackTrace();
        }

        //removing data from string to deserialize proper
        JsonNode treeNode = null;
        try {
            treeNode = aMapper.readTree(roles).at("/data");
            childJson = treeNode.toString();

            SimpleModule module = new SimpleModule();
            module.addDeserializer(RoleBasicInfo.class, new RoleDeserializer());
            aMapper.registerModule(module);
            roleList = aMapper.readValue(childJson, new TypeReference<List<RoleBasicInfo>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return roleList;
    }
}
