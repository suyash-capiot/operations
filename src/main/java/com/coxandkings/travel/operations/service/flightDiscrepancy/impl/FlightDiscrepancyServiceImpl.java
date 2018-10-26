package com.coxandkings.travel.operations.service.flightDiscrepancy.impl;

import com.coxandkings.travel.operations.enums.flightDiscrepancy.DiscrepancyStatus;
import com.coxandkings.travel.operations.enums.flightDiscrepancy.DiscrepancyType;
import com.coxandkings.travel.operations.enums.flightDiscrepancy.FilterByName;
import com.coxandkings.travel.operations.enums.flightDiscrepancy.TransactionType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.flightDiscrepancy.FlightDiscrepancyRecord;
import com.coxandkings.travel.operations.resource.flightDiscrepancy.FlightDiscrepancyRecordUpdate;
import com.coxandkings.travel.operations.resource.flightDiscrepancy.FlightDiscrepancySearchCriteria;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.flightDiscrepancy.FlightDiscrepancyService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Service
public class FlightDiscrepancyServiceImpl implements FlightDiscrepancyService {

    @Value(value = "${flight_discrepancy.search}")
    private String search;

    @Value(value = "${flight_discrepancy.get}")
    private String get;

    @Autowired
    private MDMToken mdmToken;

    @Value(value = "${flight_discrepancy.update_status}")
    private String update;

    @Value(value = "${flight_discrepancy.get_source_airLines}")
    private String getSourceAirLines;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;

    @Autowired
    private JsonObjectProvider<FlightDiscrepancyRecord> jsonObjectProvider;

    private Logger logger=Logger.getLogger(FlightDiscrepancyServiceImpl.class);

    @Override
    public Map search(FlightDiscrepancySearchCriteria flightDiscrepancySearchCriteria) throws OperationException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(search);
        builder=generateBuilder(builder,flightDiscrepancySearchCriteria);
        URI  uri = builder.build().encode().toUri();
        ResponseEntity<String> response  = null;
        try{
            response = restUtils.exchange(uri, HttpMethod.GET, restUtils.getHttpEntity(), String.class);
        }catch (Exception e){
            logger.debug("Error in getting flight discrepancies");
            throw new OperationException(e.getLocalizedMessage());
        }

        List<FlightDiscrepancyRecord> flightDiscrepancyRecords = jsonObjectProvider.getChildrenCollection(response.getBody(), "$.content", FlightDiscrepancyRecord.class);
        if (flightDiscrepancyRecords.size() == 0) {
            Map<String, Object> entity = new HashMap<>();
            entity.put("content", flightDiscrepancyRecords);
            return entity;
        }

        Map<String, Object> entity = new HashMap<>();
        entity = setResponse(response.getBody());
        entity.put("content", flightDiscrepancyRecords);
        return entity;
    }

    public <T> T exchange(String url, HttpMethod httpMethod, Class<T> responseType, Object... args) throws OperationException {
        HttpEntity<?> httpEntity = getHttpEntity();
        RestTemplate template = restUtils.getTemplate();
        ResponseEntity<T> exchange = template.exchange(url, httpMethod, httpEntity, responseType, args);
        T body = exchange.getBody();
        return body;
    }

    private HttpEntity getHttpEntity() throws OperationException {
        RestTemplate template = restUtils.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        OpsUser loggedInUser = userService.getLoggedInUser();
        String token = loggedInUser.getToken();
//        logger.info("In MDMRestUtils.exchange() method --> Is token null?" + (token == null));
        headers.add("Authorization", token);
        return new HttpEntity(headers);
    }


    @Override
    public Map update(List<FlightDiscrepancyRecordUpdate> flightDiscrepancyRecordUpdates) throws OperationException {
        if (flightDiscrepancyRecordUpdates == null || flightDiscrepancyRecordUpdates.isEmpty())
            throw new OperationException(Constants.ER704);
        for(FlightDiscrepancyRecordUpdate flightDiscrepancyRecordUpdate:flightDiscrepancyRecordUpdates){
            if (StringUtils.isEmpty(flightDiscrepancyRecordUpdate.getId()))
                throw new OperationException(Constants.ER441);
            if (StringUtils.isEmpty(flightDiscrepancyRecordUpdate.getDiscrepancyStatus()))
                throw new OperationException(Constants.ER695);
            FlightDiscrepancyRecord flightDiscrepancyRecord=getById(flightDiscrepancyRecordUpdate.getId());
            if (!flightDiscrepancyRecord.getDiscrepancyStatus().isEmpty())
                throw new OperationException("Some of the selected records status is already updated");

            if (flightDiscrepancyRecordUpdate.getDiscrepancyStatus().equalsIgnoreCase(DiscrepancyStatus.REJECTED.getValue())) {
                if (StringUtils.isEmpty(flightDiscrepancyRecordUpdate.getBspDisputeId()))
                    throw new OperationException(Constants.ER696);
            } else
                if (flightDiscrepancyRecordUpdate.getDiscrepancyStatus().equalsIgnoreCase(DiscrepancyStatus.ACCEPTED.getValue())){
                if (!StringUtils.isEmpty(flightDiscrepancyRecordUpdate.getBspDisputeId()))
                    throw new OperationException(Constants.ER694);
                if (flightDiscrepancyRecord.getTravelErpTransactionDTO().getTicketFare().compareTo(flightDiscrepancyRecord.getReportTransactionDTO().getTicketFare())!=0)
                    throw new OperationException(Constants.ER698);
            }
            else
                throw new OperationException(Constants.ER695);
        }

        for (FlightDiscrepancyRecordUpdate flightDiscrepancyRecordUpdate:flightDiscrepancyRecordUpdates){
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(update);
            builder.queryParam("transactionRecordId", flightDiscrepancyRecordUpdate.getId());
            builder.queryParam("discrepancyStatus", flightDiscrepancyRecordUpdate.getDiscrepancyStatus());
            if(!StringUtils.isEmpty(flightDiscrepancyRecordUpdate.getBspDisputeId()))
                builder.queryParam("bspDisputeId", flightDiscrepancyRecordUpdate.getBspDisputeId());
            System.out.println("builder.toUriString() = " + builder.toUriString());
            try {
                mdmRestUtils.exchange(builder.toUriString(), HttpMethod.POST, Map.class);
            }catch (Exception e){
                logger.debug("Error in updating flight discrepancies");
                throw new OperationException(e.getLocalizedMessage());
            }
        }
        Map<String,String> entity=new HashMap<>();
        entity.put("message","successfully updated");
        return entity;
    }

    @Override
    public Set<String> filterByName(String filterName) throws OperationException {
        if (StringUtils.isEmpty(filterName)) throw new OperationException(Constants.ER700);
        if (Arrays.stream(FilterByName.values()).noneMatch(filterByName -> filterByName.getValue().equalsIgnoreCase(filterName)))
            throw new OperationException(Constants.ER701);
        Set<String> list = null;
        try {
            list = mdmRestUtils.exchange(getSourceAirLines + filterName, HttpMethod.GET, Set.class);
        } catch (Exception e) {
            logger.debug("Error in getting getting source airlines");
            throw new OperationException("Error in getting details");
        }
        list.removeAll(Arrays.asList(null, ""));

        return list;
    }

    private Map<String,Object> setResponse(String response){
        Map<String, Object> entity = new HashMap<>();
        entity.put("last", jsonObjectProvider.getChildObject(response, "$.last", Boolean.class));
        entity.put("totalElements", jsonObjectProvider.getChildObject(response, "$.totalElements", Integer.class));
        entity.put("totalPages", jsonObjectProvider.getChildObject(response, "$.totalPages", Integer.class));
        entity.put("size", jsonObjectProvider.getChildObject(response, "$.size", Integer.class));
        entity.put("sort", jsonObjectProvider.getAttributeValue(response, "$.sort", String.class));
        entity.put("number", jsonObjectProvider.getChildObject(response, "$.number", Integer.class));
        entity.put("numberOfElements", jsonObjectProvider.getChildObject(response, "$.numberOfElements", Integer.class));
        entity.put("first", jsonObjectProvider.getChildObject(response, "$.first", Boolean.class));
        return entity;
    }

    private UriComponentsBuilder generateBuilder(UriComponentsBuilder builder,FlightDiscrepancySearchCriteria flightDiscrepancySearchCriteria) throws OperationException {
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getDateTo()))
            builder.queryParam("dateTo", flightDiscrepancySearchCriteria.getDateTo());
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getDateFrom()))
            builder.queryParam("dateFrom", (flightDiscrepancySearchCriteria.getDateFrom()));
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getBookingRefNum()))
            builder.queryParam("bookingRefNum", flightDiscrepancySearchCriteria.getBookingRefNum());
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getSupplierName()))
            builder.queryParam("supplierName", flightDiscrepancySearchCriteria.getSupplierName());
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getTransactionType())) {
            if (Arrays.stream(TransactionType.values()).noneMatch(transactionType -> transactionType.getValue().equalsIgnoreCase(flightDiscrepancySearchCriteria.getTransactionType())))
                throw new OperationException(Constants.ER702);
            builder.queryParam("transactionType", flightDiscrepancySearchCriteria.getTransactionType());
        }
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getAirlineCode()))
            builder.queryParam("airlineCode", flightDiscrepancySearchCriteria.getAirlineCode());
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getType())){
            if (Arrays.stream(DiscrepancyType.values()).noneMatch(discrepancyType -> discrepancyType.getValue().equalsIgnoreCase(flightDiscrepancySearchCriteria.getType())))
                throw new OperationException(Constants.ER703);
            builder.queryParam("type", flightDiscrepancySearchCriteria.getType());
        }
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getCompany()))
            builder.queryParam("company", flightDiscrepancySearchCriteria.getCompany());
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getIata()))
            builder.queryParam("iata", flightDiscrepancySearchCriteria.getIata());
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getBspReferenceNumber()))
            builder.queryParam("bspReferenceNumber", flightDiscrepancySearchCriteria.getBspReferenceNumber());
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getDiscrepancyRecordId()))
            builder.queryParam("id", flightDiscrepancySearchCriteria.getDiscrepancyRecordId());
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getPage()))
            builder.queryParam("page", flightDiscrepancySearchCriteria.getPage()-1);
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getSize()))
            builder.queryParam("size", flightDiscrepancySearchCriteria.getSize());
        if (!StringUtils.isEmpty(flightDiscrepancySearchCriteria.getFilterByName())){
            if (flightDiscrepancySearchCriteria.getFilterByValues().size()==0) throw new OperationException(Constants.ER700);
            if (Arrays.stream(FilterByName.values()).noneMatch(filterByName -> filterByName.getValue().equalsIgnoreCase(flightDiscrepancySearchCriteria.getFilterByName())))
                throw new OperationException(Constants.ER701);
            builder.queryParam("filterByName",flightDiscrepancySearchCriteria.getFilterByName());
            builder.queryParam("filterByValues",String.join(",", flightDiscrepancySearchCriteria.getFilterByValues()));
        }
        return builder;
    }


    public List<String> getDiscrepancyTypes(){
        List<String> discrepancyTypes=new ArrayList<>();
        Arrays.stream(DiscrepancyType.values()).forEach(discrepancyType -> discrepancyTypes.add(discrepancyType.getValue()));
        return discrepancyTypes;
    }

    public List<String> getDiscrepancyStatusList(){
        List<String> discrepancyStatusList=new ArrayList<>();
        Arrays.stream(DiscrepancyStatus.values()).forEach(discrepancyStatus -> discrepancyStatusList.add(discrepancyStatus.getValue()));
        return discrepancyStatusList;
    }

    public List<String> getTransactionTypes(){
        List<String> transactionTypes=new ArrayList<>();
        Arrays.stream(TransactionType.values()).forEach(transactionType -> transactionTypes.add(transactionType.getValue()));
        return transactionTypes;
    }

    public List<String> getFilterByNames(){
        List<String> filterByNames=new ArrayList<>();
        Arrays.stream(FilterByName.values()).forEach(filterByName -> {
            if (filterByName!=FilterByName.IATA)
                filterByNames.add(filterByName.getValue());
        });
        return filterByNames;
    }

    @Override
    public List<Object> getIata() throws OperationException {

        ResponseEntity<Object[]> response = null;
        try {
            response = restUtils.exchange(getSourceAirLines + FilterByName.IATA.getValue(),HttpMethod.GET,restUtils.getHttpEntity(),Object[].class);
        } catch (Exception e) {
            logger.debug("Error in getting iata number");
            throw new OperationException("Error in getting details");
        }
        return Arrays.asList(response.getBody());
    }

    @Override
    public  FlightDiscrepancyRecord getById(String id) throws OperationException {
        FlightDiscrepancySearchCriteria flightDiscrepancySearchCriteria=new FlightDiscrepancySearchCriteria();
        flightDiscrepancySearchCriteria.setDiscrepancyRecordId(id);
        List<FlightDiscrepancyRecord> flightDiscrepancyRecords= (List<FlightDiscrepancyRecord>) search(flightDiscrepancySearchCriteria).get("content");
        if (flightDiscrepancyRecords.isEmpty()) throw new OperationException(Constants.ER01);
        return flightDiscrepancyRecords.get(0);
    }

}

