package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.criteria.jbpm.InstanceBookMappingCriteria;
import com.coxandkings.travel.operations.enums.Status;
import com.coxandkings.travel.operations.enums.WorkFlow;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.helper.workflow.*;
import com.coxandkings.travel.operations.helper.workflow.Process;
import com.coxandkings.travel.operations.model.jbpm.InstanceBookMapping;
import com.coxandkings.travel.operations.resource.alert.WorkFlowResource;
import com.coxandkings.travel.operations.resource.jbpm.InstanceBookMappingResource;
import com.coxandkings.travel.operations.service.jbpm.InstanceBookMappingService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static com.coxandkings.travel.operations.utils.Constants.*;

public class WorkFlowUtils {

    //TODO: needs to remove this block after user managment integration
    private static ClientHttpRequestInterceptor interceptor = new BasicAuthorizationInterceptor("admin", "admin");

    private static String getFinalUrl(String url, String... args) {
        MessageFormat format = new MessageFormat(url);
        StringBuffer result = new StringBuffer();
        format.format(args, result, null);
        return result.toString();
    }

    public static Long startFlow(String containerUrl,
                                 String processDefUrl,
                                 String flowStartUrl,
                                 String containerName,
                                 WorkFlow workFlow,
                                 Map<String, Object> map) {
        String deployedContainer = getLatestDeployedContainer(containerUrl, containerName);
        processDefUrl = getFinalUrl(processDefUrl, deployedContainer);
        String processDefId = getProcessDefId(processDefUrl, workFlow);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        flowStartUrl = getFinalUrl(flowStartUrl, deployedContainer, processDefId);

        HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(map, headers);
        ResponseEntity<Long> longResponseEntity = RestUtils.postForEntity(flowStartUrl, request, Long.class, interceptor);
        return longResponseEntity.getBody();
    }

    private static InstanceBookMapping getProcInstance(WorkFlowResource workFlowResource,
                                                       InstanceBookMappingService mappingService) throws OperationException {
        InstanceBookMappingCriteria criteria = new InstanceBookMappingCriteria();
        criteria.setBookingRefIds(workFlowResource.getBookingRefId());
        criteria.setWorkFlow(workFlowResource.getWorkFlow());

        if(!(StringUtils.isEmpty(workFlowResource.getUserTaskName())))
        criteria.setUserTaskName(workFlowResource.getUserTaskName());

        if(!(StringUtils.isEmpty(workFlowResource.getEntityRefId())))
        criteria.setEntityRefId(workFlowResource.getEntityRefId());

        List<InstanceBookMapping> instanceBookMappings = mappingService.getByCriteria(criteria);
        if(CollectionUtils.isEmpty(instanceBookMappings)
                || instanceBookMappings.size() > 1) {
            throw new OperationException(ER01);
        }
        InstanceBookMapping next = instanceBookMappings.iterator().next();
        next.setStatus(Status.DELETED);
        InstanceBookMappingResource resource = new InstanceBookMappingResource();
        CopyUtils.copy(next, resource);
        mappingService.save(resource);
        return next;
    }

    public static void resumeFlow(String instanceUrl,
                                    String claimUrl,
                                    String startUrl,
                                    String completeUrl,
                                   WorkFlowResource workFlowResource,
                                    InstanceBookMappingService mappingService,
                                    Map<String, Object> bodyMap) throws OperationException {
        ClientHttpRequestInterceptor basicInterceptor = new BasicAuthorizationInterceptor("krisv", "krisv");

        InstanceBookMapping instance = getProcInstance(workFlowResource, mappingService);
        instanceUrl = getFinalUrl(instanceUrl, instance.getInstanceId());
        ProcessInstance response = RestUtils.getForObject(instanceUrl, ProcessInstance.class, interceptor);

        if(response == null
                || CollectionUtils.isEmpty(response.getActiveUserTasks().getTaskSummaries())
                || response.getActiveUserTasks().getTaskSummaries().size() > 1) {
            throw new OperationException(ER01);
        }
        TaskSummary taskSummary = response.getActiveUserTasks().getTaskSummaries().iterator().next();

        String taskId = taskSummary.getTaskId();
        String containerId = taskSummary.getContainerId();
        String status = taskSummary.getTaskStatus();
        if(status.equalsIgnoreCase(READY)) {
            claimUrl = getFinalUrl(claimUrl, containerId, taskId);
            RestUtils.put(claimUrl, null, interceptor);
        }

        if(status.equalsIgnoreCase(RESERVED)) {
            startUrl = getFinalUrl(startUrl, containerId, taskId);
            RestUtils.put(startUrl, null, basicInterceptor);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(bodyMap, headers);

        completeUrl = getFinalUrl(completeUrl, containerId, taskId);
        RestUtils.put(completeUrl, request, basicInterceptor);
        mappingService.remove(instance.getId());
    }

    public static String getLatestDeployedContainer(String url, String containerName) {
        Response response = RestUtils.getForObject(url, Response.class, interceptor);
        Comparator<String> comparator = getStringComparator();
        TreeSet<String> kieContainerSet = new TreeSet<>(comparator);

        for(KieContainer kieContainer : response.getKieContainers().getKieContainer()) {
            String containerId = kieContainer.getContainerId();
            if(containerId.startsWith(containerName)) {
                kieContainerSet.add(containerId);
            }
        }

        return kieContainerSet.iterator().next();
    }

    public static String getProcessDefId(String url, WorkFlow workFlow) {
        UriComponentsBuilder uriComponentsBuilder=UriComponentsBuilder.fromUriString(url).queryParam("pageSize",100);
        ProcessDefinitions response = RestUtils.getForObject(uriComponentsBuilder.toUriString(), ProcessDefinitions.class,interceptor);
        Comparator<String> comparator = getStringComparator();
        TreeSet<String> kieContainerSet = new TreeSet<>(comparator);

        for(Process process : response.getProcesses()) {
            if(process.getProcessName().equalsIgnoreCase(workFlow.getWorkFlowName())) {
                kieContainerSet.add(process.getProcessId());
            }
        }

        return kieContainerSet.iterator().next();
    }

    // using this comparator to get workflow deployment ids in descending order
    private static Comparator<String> getStringComparator() {
        return new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int len1 = o1.length();
                    int len2 = o2.length();
                    int lim = Math.min(len1, len2);
                    char v1[] = o1.toCharArray();
                    char v2[] = o2.toCharArray();

                    int k = 0;
                    while (k < lim) {
                        char c1 = v1[k];
                        char c2 = v2[k];
                        if (c1 != c2) {
                            return c2 - c1;
                        }
                        k++;
                    }
                    return len2 - len1;
                }
            };
    }
}
