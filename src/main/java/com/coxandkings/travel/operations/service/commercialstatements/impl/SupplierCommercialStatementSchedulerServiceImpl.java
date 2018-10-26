package com.coxandkings.travel.operations.service.commercialstatements.impl;

import com.coxandkings.travel.operations.criteria.booking.becriteria.BookingDetailsFilter;
import com.coxandkings.travel.operations.criteria.booking.becriteria.BookingSearchCriteria;
import com.coxandkings.travel.operations.criteria.booking.becriteria.ProductDetailsFilter;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialHeads;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementFor;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialType;
import com.coxandkings.travel.operations.enums.commercialStatements.SettlementStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.CommercialStatementDetails;
import com.coxandkings.travel.operations.model.commercialstatements.OrderCommercialDetails;
import com.coxandkings.travel.operations.model.commercialstatements.PassengerDetails;
import com.coxandkings.travel.operations.model.commercialstatements.SupplierCommercialStatement;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.repository.commercialstatements.SupplierCommercialStatementRepo;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementGeneration;
import com.coxandkings.travel.operations.resource.commercialStatement.MdmSupplierId;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.scheduler.*;
import com.coxandkings.travel.operations.resource.searchviewfilter.BookingSearchResponseItem;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.commercialstatements.SupplierCommercialStatementSchedulerService;
import com.coxandkings.travel.operations.service.commercialstatements.impl.brmsProcessors.BaseCommercialsProcessor;
import com.coxandkings.travel.operations.service.commercialstatements.impl.brmsProcessors.CommercialProcessorUtils;
import com.coxandkings.travel.operations.service.commercialstatements.impl.brmsProcessors.OrderWiseCommercialDetails;
import com.coxandkings.travel.operations.service.commercialstatements.impl.brmsProcessors.OrderWiseCommercialDetails.Commercials;
import com.coxandkings.travel.operations.service.commercialstatements.impl.settlementTerms.SupplierSettlementTerms;
import com.coxandkings.travel.operations.service.commercialstatements.impl.settlementTerms.SupplierSettlementTerms.SupplierSettlementDetails;
import com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing.SlabbingTree;
import com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing.SlabbingUtils;
import com.coxandkings.travel.operations.service.qcmanagement.QcUtilService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.scheduler.FortnightlySettlementSchedulerUtil;
import com.coxandkings.travel.operations.utils.scheduler.HalfYearlySettlementSchedulerUtil;
import com.coxandkings.travel.operations.utils.scheduler.WeeklySettlementDateswiseSchedulerUtil;
import com.coxandkings.travel.operations.utils.scheduler.WeeklySettlementDaywiseSchedulerUtil;
import com.coxandkings.travel.operations.utils.supplierBillPassing.DateConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;


import java.math.BigDecimal;
import java.net.URI;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SupplierCommercialStatementSchedulerServiceImpl implements SupplierCommercialStatementSchedulerService,Constants {

    @Autowired
    private SupplierCommercialStatementRepo supplierCommercialStatementRepo;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private AlertService alertService;

    @Value("${finance-commercial-statements-settelement.supplier_step_one}")
    private String financeSupplierCommercialStatementStepOne;

    @Value(value = "${commercial-statements.supplier_id}")
    private String getSupplierSettlement_id;

    @Value(value = "${commercial-statements.getSupplierSettlementTerm}")
    private String getSupplierSettlementDetails;

    @Value(value = "${commercial-statements.getSupplierCommercials}")
    private String getSupplierCommercial;

    @Value(value = "${finance-commercial-statements-settelement.get_invoice}")
    private String getInvoiceDetails;

    @Value(value = "${commercial_statements.supplier_alert}")
    private String supplierAlert;

    @Value(value = "${mdm.get_supplier_ids}")
    private String getSupplierIds;

    @Value(value = "${commercial_statements_calculations.supplier}")
    private String supplierBrmsCalculationUrl;

    @Autowired
    private MDMToken mdmToken;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private QcUtilService qcUtilService;

    private Logger logger = LogManager.getLogger(SupplierCommercialStatementSchedulerServiceImpl.class);

    @Autowired
    private SupplierSettlementTerms suppSettlementTerms;
    
    @Autowired
    private SlabbingUtils slabbingUtils;
    
    @Autowired
    private BaseCommercialsProcessor baseCommPrcsr;

    
    private Map<String,List<OrderWiseCommercialDetails>> mergeBooking(List<OrderWiseCommercialDetails> ordrLst) {
    	Map<String,List<OrderWiseCommercialDetails>> mergedDetails = new HashMap<String,List<OrderWiseCommercialDetails>>();
    	for(OrderWiseCommercialDetails ordr:ordrLst) {
    		String key = getStatementDetailsListKey(ordr);
    		List<OrderWiseCommercialDetails> statementDetailsList = mergedDetails.get(key);
    		if(statementDetailsList == null || statementDetailsList.size()<1 )
    		{
    			statementDetailsList = new ArrayList<OrderWiseCommercialDetails>();
    			statementDetailsList.add(ordr);
    			mergedDetails.put(key, statementDetailsList);
    		}
    		else
    		{
    			statementDetailsList.add(ordr);
    		}
    	}
		return mergedDetails;
	}

	private String getStatementDetailsListKey(OrderWiseCommercialDetails ordr) {
		return String.format("%s%c%s%c%s%c%s%c%s", ordr.getProdCateg(), KEYSEPARATOR,ordr.getProdSubCateg(),KEYSEPARATOR,ordr.getProductName(),KEYSEPARATOR,ordr.getCurrency(),KEYSEPARATOR,ordr.getMarket());
	}
	
    @Override
    public void scheduler() {
        try {
        	List<SupplierSettlementDetails> suppCommLst = suppSettlementTerms.getAllCommForStatementGeneration();
        	SupplierCommercialStatement suppCommStatement;
        	BookingSearchCriteria bkngSrchCriteria = new BookingSearchCriteria();
        	BookingDetailsFilter bkngFilter;
    		ProductDetailsFilter prdctFilter;
    		String suppId;
        	for(SupplierSettlementDetails commHead:suppCommLst) {
        		suppId = commHead.getSuppId();
        		bkngFilter = new BookingDetailsFilter();
        		prdctFilter = new ProductDetailsFilter();
        		String fromDateStr = commHead.getFromDate().toLocalDateTime().toString();
        		String toDateStr = commHead.getToDate().toLocalDateTime().toString();
        		if("Booking Date".equals(commHead.getSettlementScheduleType())) {
        			bkngFilter.setBookingFromDate(fromDateStr);
        			bkngFilter.setBookingToDate(toDateStr);
        		}
        		else if("Travel Date".equals(commHead.getSettlementScheduleType())) {
        			prdctFilter.setTravelFromDate(fromDateStr);
        			prdctFilter.setTravelToDate(toDateStr);
        		}
        		else {
        			//log
        			continue;
        		}
        		prdctFilter.setSupplierName(suppId);
        		bkngSrchCriteria.setBookingBasedFilter(bkngFilter);
        		bkngSrchCriteria.setProductBasedFilter(prdctFilter);
        		bkngSrchCriteria.setPage(1);
        		bkngSrchCriteria.setSize(Integer.MAX_VALUE);//set to int max value
        		
        		try {
        				JSONArray bkngArr = Utils.getBookingByCriteriaSlabWise(bkngSrchCriteria);
        			//JSONArray bkngArr = Utils.getBookingFromFile();
        			if(bkngArr==null || bkngArr.length()==0)
        				continue;
        			SlabbingTree slabTree = slabbingUtils.constructSlabData(bkngArr);
        			CommercialProcessorUtils utils = new CommercialProcessorUtils();
        			utils.setSettlementDetails(commHead);
        			utils.setSlabData(slabTree);
    				List<OrderWiseCommercialDetails> ordrLst =baseCommPrcsr.process(bkngArr,utils);
    				Map<String,List<OrderWiseCommercialDetails>> mergedorderDetails = mergeBooking(ordrLst);
    				Set<String> statementIds = new HashSet<>();
    				List<OrderWiseCommercialDetails> tempSd;
    				Iterator<Map.Entry<String, List<OrderWiseCommercialDetails>>> sdEntryIter = mergedorderDetails.entrySet().iterator();
    				while (sdEntryIter.hasNext()) {
    					Map.Entry<String, List<OrderWiseCommercialDetails>> sdEntry = sdEntryIter.next();
    					OrderWiseCommercialDetails ordr = sdEntry.getValue().get(0);	
    					suppCommStatement = new SupplierCommercialStatement();
    					suppCommStatement.setCommercialStatementFor(CommercialStatementFor.SUPPLIER.getName());
    					suppCommStatement.setCreatedDate(ZonedDateTime.now());
    					suppCommStatement.setSupplierOrClientId(suppId);
    		    		suppCommStatement.setSupplierOrClientName(commHead.getSuppName());
    		    		suppCommStatement.setCommercialHead(commHead.getCommercialHeadName());
    		    		suppCommStatement.setCommercialType(commHead.getCommercialHeadType());
    		    		if("Booking Date".equals(commHead.getSettlementScheduleType())) {
    		    			suppCommStatement.setBookingPeriodFrom(ZonedDateTime.parse(commHead.getFromDate().toOffsetDateTime().toString()));
    		    			suppCommStatement.setBookingPeriodTo(ZonedDateTime.parse(commHead.getToDate().toOffsetDateTime().toString()));
    		    		}
    		    		else if("Travel Date".equals(commHead.getSettlementScheduleType())) {
    		    			suppCommStatement.setTravelDateFrom(ZonedDateTime.parse(commHead.getFromDate().toOffsetDateTime().toString()));
    		    			suppCommStatement.setTravelDateTo(ZonedDateTime.parse(commHead.getToDate().toOffsetDateTime().toString()));
    		    		}
    		    		suppCommStatement.setSettlementDueDate(commHead.getSettlementDueDate());
    		    		suppCommStatement.setProductCategory(ordr.getProdCateg());
    		    		suppCommStatement.setProductCategorySubType(ordr.getProdSubCateg());
    		    		suppCommStatement.setProductName(ordr.getProductName());
    		    		suppCommStatement.setCompanyMarket(ordr.getMarket());
    		    		suppCommStatement.setCurrency(ordr.getCurrency());
    		    		suppCommStatement.setSettlementStatus(SettlementStatus.UNSETTLED.getValue());
    		    		OrderWiseCommercialDetails tempSdObj;
    		    		tempSd = new ArrayList<OrderWiseCommercialDetails>();
    		    		Commercials comm = null;
    		    		for(int z=0;z<sdEntry.getValue().size();z++)
    		    		{ 
    		    			
    		    			tempSdObj = sdEntry.getValue().get(z);
    		    			Commercials tempCommObj = tempSdObj.getCommercial(CommercialHeads.forMDMString(commHead.getCommercialHeadName()), commHead.getCommercialHeadType());
    		    			if(tempCommObj==null) {
    		    				continue;
    		    			}
    		    			tempSd.add(tempSdObj);
    		    			if(comm==null) {
    		    				comm = tempCommObj;
    		    				continue;
    		    			}
    		    			comm.setCommercialAmount(comm.getCommercialAmount().add(tempCommObj.getCommercialAmount()));;
    		    		}
    		    		if(comm==null)
    		    			continue;
    		    		if("Receivable".equals(comm.getCommercialType())) {
    		    		suppCommStatement.setTotalReceivable(comm.getCommercialAmount());
    	    			suppCommStatement.setBalanceReceivable(comm.getCommercialAmount());
    	    			suppCommStatement.setTotalReceived(BigDecimal.ZERO);;
    	    			suppCommStatement.setTotalPaid(BigDecimal.ZERO);
    	    			suppCommStatement.setTotalPayable(BigDecimal.ZERO);
    	    			suppCommStatement.setBalancePayable(BigDecimal.ZERO);
    		    		}
    		    		else
    		    		{
    		    			suppCommStatement.setTotalReceivable(BigDecimal.ZERO);
    		    			suppCommStatement.setBalanceReceivable(BigDecimal.ZERO);
    		    			suppCommStatement.setTotalReceived(BigDecimal.ZERO);;
    		    			suppCommStatement.setTotalPaid(BigDecimal.ZERO);
    		    			suppCommStatement.setTotalPayable(comm.getCommercialAmount());
    		    			suppCommStatement.setBalancePayable(comm.getCommercialAmount());
    		    		}
    		    		String statementName = suppCommStatement.getCommercialHead().concat("-").concat(comm.getCommercialType().concat("-").concat(fromDateStr.substring(0, fromDateStr.indexOf("T")).concat(" to ").concat(toDateStr.substring(0, toDateStr.indexOf("T")))));
    		    		suppCommStatement.setStatementName(statementName);
    		    		/*Handle This*/Set<CommercialStatementDetails> commDetails = suppCommStatement.getCommercialStatementDetails();
    		    		if(commDetails==null || commDetails.size()<1) {
    		    			commDetails = new HashSet<CommercialStatementDetails>();
    		    		}
    		    		Iterator<OrderWiseCommercialDetails> suppDetailsListIterator = tempSd.iterator();
    		    		while(suppDetailsListIterator.hasNext())
    		    		{
    		    		tempSdObj = suppDetailsListIterator.next();
    		    		CommercialStatementDetails commDetail=null;
    		    		Iterator<CommercialStatementDetails> itr= commDetails.iterator();
    		    		CommercialStatementDetails temp;
    					while(itr.hasNext()) {
    		    			temp=itr.next();
    		    			if(temp.getBookingRefNum()==tempSdObj.getBookid()) {
    		    				commDetail=temp;
    		    				break;
    		    			}
    		    		}
    					if(commDetail==null) {
    						commDetail = new CommercialStatementDetails();
    						commDetail.setBookingRefNum(tempSdObj.getBookid());
    						commDetail.setBookingDate(ZonedDateTime.parse(tempSdObj.getBookingDate()));
    						commDetail.setCommercialStatement(suppCommStatement);
    						commDetails.add(commDetail);
    					}
    					/*Handle This*/
    					Set<OrderCommercialDetails> orderSet = commDetail.getOrderCommercialDetails();
    					if(orderSet==null) {
    						orderSet = new HashSet<OrderCommercialDetails>();
    		    		}
    					OrderCommercialDetails order = new OrderCommercialDetails();
    					order.setCommercialValue(comm.getCommercialAmount());
    					for(PassengerDetails paxData:tempSdObj.getPaxDetails()) {
    						paxData.setOrderCommercialDetails(order);
    					}
    					/*Handle This*/	order.setPassengerDetailsSet(tempSdObj.getPaxDetails());
    					order.setNumberOfPassengers(tempSdObj.getPaxDetails().size());
    					orderSet.add(order);
    					order.setCommercialStatementDetails(commDetail);
    					commDetail.setOrderCommercialDetails(orderSet);/*Handle This*/
    		    		}
    					suppCommStatement.setCommercialStatementDetails(commDetails);
    		    		SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatementRepo.add(suppCommStatement);
    		    		statementIds.add(supplierCommercialStatement.getStatementId());
    				
    				 try {
    	                 HttpHeaders httpHeaders = new HttpHeaders();
    	                 httpHeaders.set("Authorization", mdmToken.getToken());
    	                 JSONObject entity = new JSONObject();
    	                 entity.put("commercialStatementIds", statementIds);
    	                 HttpEntity httpEntity = new HttpEntity(entity, httpHeaders);
    	                 ResponseEntity<String> result=RestUtils.getTemplate().exchange(financeSupplierCommercialStatementStepOne, HttpMethod.POST, httpEntity, String.class);
    	                 System.out.println("result = " + result);
    	             } catch (Exception e) {
    	                 logger.debug("Error in calling finance supplier commercial statements api after generation");
    	             }
    			} 
        		} catch (Exception e) {
    				continue;
    			}
        		
        	}
        
        } catch (Exception e) {
            logger.debug("Exception occured in commercial statements scheduler");
        }
    }

    private void schedulerStatementCreation(JSONObject settlementTerm, CommercialStatementGeneration commercialStatementGeneration,JSONObject fixed,JSONObject slab) {
        try {
            JSONObject periodicityDetails = settlementTerm.getJSONObject("periodicity");
            String settlementSchedule = settlementTerm.getString("settlementSchedule");
            commercialStatementGeneration.setSettlementSchedule(settlementSchedule);
            String repeatFreq = periodicityDetails.getString("repeatFreq");

            switch (repeatFreq.toLowerCase()) {
                case "monthly":
                    Boolean shouldRuntoday=false;
                    int settlementDueDay = periodicityDetails.getJSONObject("monthly").getInt("settlementDueDay");
                    ZonedDateTime todayDate = ZonedDateTime.now();
                    ZonedDateTime lastOfMonth = todayDate.with(TemporalAdjusters.lastDayOfMonth());
                    if (todayDate.compareTo(lastOfMonth)==0) shouldRuntoday=true;
                    if (shouldRuntoday) {
                        commercialStatementGeneration.setStatementPeriodFromDate(ZonedDateTime.now().minusMonths(1));
                        commercialStatementGeneration.setStatementPeriodToDate(ZonedDateTime.now());
                        commercialStatementGeneration.setSettlementDueDate(ZonedDateTime.now().plusMonths(1).withDayOfMonth(settlementDueDay));
                        create(commercialStatementGeneration,fixed,slab);
                    }
                    break;

                case "weekly":
                    JSONObject weekly = periodicityDetails.getJSONObject("weekly");
                    String weeklySchedule = weekly.getString("weeklySchedule");
                    switch (weeklySchedule.toLowerCase()) {
                        case "day":
                            DayOfWeek fromDay = DayOfWeek.valueOf(weekly.getJSONObject("day").getString("fromDay").toUpperCase());
                            DayOfWeek toDay = DayOfWeek.valueOf(weekly.getJSONObject("day").getString("toDay").toUpperCase());
                            DayOfWeek weeklySettlementDueDay = DayOfWeek.valueOf(weekly.getJSONObject("day").getString("settlementDueDay").toUpperCase());
                            SettlementScheduleResponse settlementScheduleResponse = WeeklySettlementDaywiseSchedulerUtil.computeWeeklyDaywiseSettlementSchedule(fromDay, toDay, weeklySettlementDueDay);
                            if (!settlementScheduleResponse.isShouldRunToday()) break;
                            commercialStatementGeneration.setSettlementDueDate(settlementScheduleResponse.getSettlementDueDay().atStartOfDay(ZoneId.systemDefault()));
                            commercialStatementGeneration.setStatementPeriodFromDate(settlementScheduleResponse.getStartDate().atStartOfDay(ZoneId.systemDefault()));
                            commercialStatementGeneration.setStatementPeriodToDate(settlementScheduleResponse.getEndDate().atStartOfDay(ZoneId.systemDefault()));
                            create(commercialStatementGeneration,fixed,slab);
                            break;

                        case "dates":
                            JSONArray dates = weekly.getJSONArray("dates");
                            List<WeeklyDateswisePeriodicityInput> weeklyDateswisePeriodicityInputs = new ArrayList<>();
                            for (Object object : dates) {
                                JSONObject datesObject = (JSONObject) object;
                                LocalDate fromDate = LocalDate.of(datesObject.getInt("fromDate"), Month.valueOf(datesObject.getString("fromMonth").toUpperCase()), datesObject.getInt("toDate"));
                                LocalDate toDate = LocalDate.of(datesObject.getInt("fromDate"), Month.valueOf(datesObject.getString("fromMonth").toUpperCase()), datesObject.getInt("toDate"));
                                LocalDate weeklySettlementDueDate = LocalDate.of(datesObject.getInt("settlementDueDate"), Month.valueOf(datesObject.getString("settlementDueMonth").toUpperCase()), datesObject.getInt("settlementDueYear"));

                                WeeklyDateswisePeriodicityInput weeklyDateswisePeriodicityInput = new WeeklyDateswisePeriodicityInput();
                                weeklyDateswisePeriodicityInput.setFromDate(fromDate);
                                weeklyDateswisePeriodicityInput.setToDate(toDate);
                                weeklyDateswisePeriodicityInput.setSettlementDate(weeklySettlementDueDate);
                                weeklyDateswisePeriodicityInputs.add(weeklyDateswisePeriodicityInput);
                            }
                            SettlementScheduleResponse weeklySettlementSchedulerResponse = WeeklySettlementDateswiseSchedulerUtil.canExecuteDateswiseSettlement(weeklyDateswisePeriodicityInputs);
                            if (!weeklySettlementSchedulerResponse.isShouldRunToday()) break;
                            commercialStatementGeneration.setStatementPeriodToDate(weeklySettlementSchedulerResponse.getEndDate().atStartOfDay(ZoneId.systemDefault()));
                            commercialStatementGeneration.setStatementPeriodFromDate(weeklySettlementSchedulerResponse.getStartDate().atStartOfDay(ZoneId.systemDefault()));
                            commercialStatementGeneration.setSettlementDueDate(weeklySettlementSchedulerResponse.getSettlementDueDay().atStartOfDay(ZoneId.systemDefault()));
                            create(commercialStatementGeneration,fixed,slab);
                            break;
                    }
                    break;

                case "end of validity":
                    Integer numberOfDays = periodicityDetails.getJSONObject("endOfValidity").getInt("numberOfDays");
                    //TODO need to know what to do exactly
                    commercialStatementGeneration.setSettlementDueDate(ZonedDateTime.now().plusDays(numberOfDays));
                    commercialStatementGeneration.setStatementPeriodFromDate(ZonedDateTime.now().minusMonths(numberOfDays));
                    commercialStatementGeneration.setStatementPeriodToDate(ZonedDateTime.now());
                    create(commercialStatementGeneration,fixed,slab);
                    break;

                case "fortnightly":
                    JSONArray fortNightly = periodicityDetails.getJSONArray("fortnightly");
                    FortnightlyPeriodicityInput fortnightlyPeriodicityInput = new FortnightlyPeriodicityInput();
                    for (Object object : fortNightly) {
                        JSONObject fortNightJson = (JSONObject) object;
                        Integer settlementDueDate = fortNightJson.getInt("settleDueDay");
                        String fortNight = fortNightJson.getString("fortnight");
                        if (fortNight.toLowerCase().contains("first")) {
                            fortnightlyPeriodicityInput.setFirstFortnightSettlementDueDay(settlementDueDate);
                            if (fortNightJson.getString("settlementDueMonth").toLowerCase().equalsIgnoreCase("current"))
                                fortnightlyPeriodicityInput.setSettleFirstFornightByCurrentMonth(true);
                            else fortnightlyPeriodicityInput.setSettleFirstFornightByCurrentMonth(false);
                        } else {
                            fortnightlyPeriodicityInput.setSecondFortnightSettlementDueDay(settlementDueDate);
                            if (fortNightJson.getString("settlementDueMonth").toLowerCase().equalsIgnoreCase("current"))
                                fortnightlyPeriodicityInput.setSettleSecondFornightByCurrentMonth(true);
                            else fortnightlyPeriodicityInput.setSettleSecondFornightByCurrentMonth(false);
                        }
                    }
                    FortnightSettlementScheduleResponse settlementScheduleResponse = FortnightlySettlementSchedulerUtil.computeFortnightSettlementSchedule(fortnightlyPeriodicityInput);
                    if (settlementScheduleResponse.getFirstFortnightScheduleResponse() != null) {
                        if (settlementScheduleResponse.getFirstFortnightScheduleResponse().isShouldRunToday()) {
                            SettlementScheduleResponse firstFortNightSchedulerResponse = settlementScheduleResponse.getFirstFortnightScheduleResponse();
                            CommercialStatementGeneration firstFortnightCommercialStatementGeneration = new CommercialStatementGeneration();
                            CopyUtils.copy(commercialStatementGeneration, firstFortnightCommercialStatementGeneration);
                            firstFortnightCommercialStatementGeneration.setSettlementDueDate(firstFortNightSchedulerResponse.getSettlementDueDay().atStartOfDay(ZoneId.systemDefault()));
                            firstFortnightCommercialStatementGeneration.setStatementPeriodFromDate(firstFortNightSchedulerResponse.getStartDate().atStartOfDay(ZoneId.systemDefault()));
                            firstFortnightCommercialStatementGeneration.setStatementPeriodToDate(firstFortNightSchedulerResponse.getEndDate().atStartOfDay(ZoneId.systemDefault()));
                            create(firstFortnightCommercialStatementGeneration,fixed,slab);
                        }
                    }
                    if (settlementScheduleResponse.getSecondFortnightScheduleResponse() != null) {
                        if (settlementScheduleResponse.getSecondFortnightScheduleResponse().isShouldRunToday()) {
                            CommercialStatementGeneration secondFortnightCommercialStatementGeneration = new CommercialStatementGeneration();
                            CopyUtils.copy(commercialStatementGeneration, secondFortnightCommercialStatementGeneration);
                            SettlementScheduleResponse secondFortNightSchedulerResponse = settlementScheduleResponse.getSecondFortnightScheduleResponse();
                            secondFortnightCommercialStatementGeneration.setSettlementDueDate(secondFortNightSchedulerResponse.getSettlementDueDay().atStartOfDay(ZoneId.systemDefault()));
                            secondFortnightCommercialStatementGeneration.setStatementPeriodFromDate(secondFortNightSchedulerResponse.getStartDate().atStartOfDay(ZoneId.systemDefault()));
                            secondFortnightCommercialStatementGeneration.setStatementPeriodToDate(secondFortNightSchedulerResponse.getEndDate().atStartOfDay(ZoneId.systemDefault()));
                            create(secondFortnightCommercialStatementGeneration,fixed,slab);
                        }
                    }
                    break;

                case "half yearly":
                    JSONArray halfYearly = periodicityDetails.getJSONArray("halfYearly");
                    HalfYearlyPeriodicityInput halfYearlyPeriodicityInput = new HalfYearlyPeriodicityInput();
                    for (Object object : halfYearly) {
                        JSONObject halfYearlyJson = (JSONObject) object;
                        if (halfYearlyJson.getString("halfYearly").contains("1")) {
                            HalfYearlyPeriodicity firstHalfYearlyPeriodicity = new HalfYearlyPeriodicity();
                            firstHalfYearlyPeriodicity.setStartDate(getStartDate(halfYearlyJson));
                            firstHalfYearlyPeriodicity.setEndDate(getEndDate(halfYearlyJson));
                            firstHalfYearlyPeriodicity.setSettlementDueDate(getSettlementDueDate(halfYearlyJson));
                            halfYearlyPeriodicityInput.setFirstHalfYearlyPeriodicity(firstHalfYearlyPeriodicity);
                            if (HalfYearlySettlementSchedulerUtil.computeForHalfYearlySettelemetSchelule(getEndDate(halfYearlyJson))) {
                                commercialStatementGeneration.setStatementPeriodFromDate(firstHalfYearlyPeriodicity.getStartDate().atStartOfDay(ZoneId.systemDefault()));
                                commercialStatementGeneration.setStatementPeriodToDate(firstHalfYearlyPeriodicity.getEndDate().atStartOfDay(ZoneId.systemDefault()));
                                commercialStatementGeneration.setSettlementDueDate(firstHalfYearlyPeriodicity.getSettlementDueDate().atStartOfDay(ZoneId.systemDefault()));
                                create(commercialStatementGeneration,fixed,slab);
                            }

                        } else if (halfYearlyJson.getString("halfYearly").contains("2")) {
                            HalfYearlyPeriodicity secondHalfYearlyPeriodicity = new HalfYearlyPeriodicity();
                            secondHalfYearlyPeriodicity.setStartDate(getStartDate(halfYearlyJson));
                            secondHalfYearlyPeriodicity.setEndDate(getEndDate(halfYearlyJson));
                            secondHalfYearlyPeriodicity.setSettlementDueDate(getSettlementDueDate(halfYearlyJson));
                            halfYearlyPeriodicityInput.setSecondHalfYearlyPeriodicity(secondHalfYearlyPeriodicity);
                            if (HalfYearlySettlementSchedulerUtil.computeForHalfYearlySettelemetSchelule(getEndDate(halfYearlyJson))) {
                                commercialStatementGeneration.setStatementPeriodFromDate(secondHalfYearlyPeriodicity.getStartDate().atStartOfDay(ZoneId.systemDefault()));
                                commercialStatementGeneration.setStatementPeriodToDate(secondHalfYearlyPeriodicity.getEndDate().atStartOfDay(ZoneId.systemDefault()));
                                commercialStatementGeneration.setSettlementDueDate(secondHalfYearlyPeriodicity.getSettlementDueDate().atStartOfDay(ZoneId.systemDefault()));
                                create(commercialStatementGeneration,fixed,slab);
                            }
                        }
                    }

                default:
                    break;
            }

        } catch (Exception e) {
            logger.debug("Exception occured in commercial statements scheduler");
        }
    }

    public void create(CommercialStatementGeneration commercialStatementGeneration, JSONObject fixed, JSONObject slab) throws OperationException {
        BookingSearchCriteria bookingSearchCriteria = new BookingSearchCriteria();
        BookingDetailsFilter bookingDetailsFilter = new BookingDetailsFilter();
        ProductDetailsFilter productDetailsFilter = new ProductDetailsFilter();

        if (commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("Booking Date")) {
            bookingDetailsFilter.setBookingFromDate(DateConverter.zonedDateTimeToString(commercialStatementGeneration.getStatementPeriodFromDate()));
            bookingDetailsFilter.setBookingToDate(DateConverter.zonedDateTimeToString(commercialStatementGeneration.getStatementPeriodToDate()));
        }else if (commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("Travel Date") || commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("Before Travel Date") || commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("On Travel Date")){
            productDetailsFilter.setTravelFromDate(DateConverter.zonedDateTimeToString(commercialStatementGeneration.getStatementPeriodFromDate()));
            productDetailsFilter.setTravelToDate(DateConverter.zonedDateTimeToString(commercialStatementGeneration.getStatementPeriodToDate()));
        }

        productDetailsFilter.setProductCategoryId(commercialStatementGeneration.getProductCategory());
        productDetailsFilter.setProductCategorySubTypeId(commercialStatementGeneration.getProductSubCategory());
        productDetailsFilter.setSupplierName(commercialStatementGeneration.getSupplierId());

        bookingSearchCriteria.setBookingBasedFilter(bookingDetailsFilter);
        bookingSearchCriteria.setProductBasedFilter(productDetailsFilter);
        bookingSearchCriteria.setPage(1);
        bookingSearchCriteria.setSize(10000000);

        List<BookingSearchResponseItem> bookingSearchResponseItems = opsBookingService.searchBookings(bookingSearchCriteria);

        SupplierCommercialStatement supplierCommercialStatement = new SupplierCommercialStatement();
        CommercialStatementDetails commercialStatementDetails = null;
        Set<CommercialStatementDetails> commercialStatementDetailsSet = new HashSet<>();
        String companyId = null;

        for (BookingSearchResponseItem bookingSearchResponseItem : bookingSearchResponseItems) {
            try {
                if (companyId == null)
                    if (!StringUtils.isEmpty(bookingSearchResponseItem.getCompany()))
                        companyId = bookingSearchResponseItem.getCompany();

                OpsBooking opsBooking = opsBookingService.getBooking(bookingSearchResponseItem.getBookID());
                if (opsBooking == null) continue;
                commercialStatementDetails = new CommercialStatementDetails();
                commercialStatementDetails.setBookingRefNum(opsBooking.getBookID());
                commercialStatementDetails.setBookingDate(opsBooking.getBookingDateZDT());
                Set<OrderCommercialDetails> orderCommercialDetailsSet = new HashSet<>();

                for (OpsProduct opsProduct : opsBooking.getProducts()) {
                    if (opsProduct.getProductCategory().equalsIgnoreCase(commercialStatementGeneration.getProductCategory()) && opsProduct.getProductSubCategory().equalsIgnoreCase(commercialStatementGeneration.getProductSubCategory()) && opsProduct.getSupplierID().equalsIgnoreCase(commercialStatementGeneration.getSupplierId()))
                        orderCommercialDetailsSet = generateCommercialStatementDetails(opsProduct, orderCommercialDetailsSet);
                }
                if (orderCommercialDetailsSet.size() != 0) {
                    commercialStatementDetails.setOrderCommercialDetails(orderCommercialDetailsSet);
                    for (OrderCommercialDetails orderCommercialDetails : orderCommercialDetailsSet)
                        orderCommercialDetails.setCommercialStatementDetails(commercialStatementDetails);
                    commercialStatementDetailsSet.add(commercialStatementDetails);
                }
            } catch (Exception e) {
                logger.error("error in getting commercial details form BE");
            }
        }

        if (!commercialStatementDetailsSet.isEmpty()) {
            supplierCommercialStatement.setCompanyId(companyId);
            supplierCommercialStatement = setCommercialStatementDetails(supplierCommercialStatement, commercialStatementGeneration, commercialStatementDetailsSet, fixed,slab);
            try {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.set("Authorization", mdmToken.getToken());
                Set<String> statementIds = new HashSet<>();
                statementIds.add(supplierCommercialStatement.getStatementId());
                Map<String, Object> entity = new HashMap<>();
                entity.put("commercialStatementIds", statementIds);
                HttpEntity httpEntity = new HttpEntity(entity, httpHeaders);
                ResponseEntity<String> result=RestUtils.getTemplate().exchange(financeSupplierCommercialStatementStepOne, HttpMethod.POST, httpEntity, String.class);
                System.out.println("result = " + result);
            } catch (Exception e) {
                logger.debug("Error in calling finance supplier commercial statements api after generation");
            }
        }
    }


    private SupplierCommercialStatement setCommercialStatementDetails(SupplierCommercialStatement supplierCommercialStatement, CommercialStatementGeneration commercialStatementGeneration, Set<CommercialStatementDetails> commercialStatementDetailsSet,JSONObject fixed,JSONObject slab) throws OperationException {
        supplierCommercialStatement.setCommercialHead(commercialStatementGeneration.getCommercialHead());
        supplierCommercialStatement.setCommercialType(commercialStatementGeneration.getCommercialtype());
        if (!StringUtils.isEmpty(commercialStatementGeneration.getCurrency()))
            supplierCommercialStatement.setCurrency(commercialStatementGeneration.getCurrency());
        else supplierCommercialStatement.setCurrency("INR");

        if (commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("Booking Date")) {
            supplierCommercialStatement.setBookingPeriodFrom(commercialStatementGeneration.getStatementPeriodFromDate());
            supplierCommercialStatement.setBookingPeriodTo(commercialStatementGeneration.getStatementPeriodToDate());
        }else if (commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("Travel Date") || commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("Before Travel Date") || commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("On Travel Date")) {
            supplierCommercialStatement.setTravelDateTo(commercialStatementGeneration.getStatementPeriodToDate());
            supplierCommercialStatement.setTravelDateFrom(commercialStatementGeneration.getStatementPeriodFromDate());
        }
        supplierCommercialStatement.setSettlementDueDate(commercialStatementGeneration.getSettlementDueDate());
        supplierCommercialStatement.setProductCategory(commercialStatementGeneration.getProductCategory());
        supplierCommercialStatement.setProductCategorySubType(commercialStatementGeneration.getProductSubCategory());
        if (!StringUtils.isEmpty(commercialStatementGeneration.getProductName()))
        supplierCommercialStatement.setProductName(commercialStatementGeneration.getProductName());

        supplierCommercialStatement.setSupplierOrClientId(commercialStatementGeneration.getSupplierId());
        supplierCommercialStatement.setCommercialStatementFor(CommercialStatementFor.SUPPLIER.getName());
        supplierCommercialStatement.setSupplierOrClientName(commercialStatementGeneration.getSupplierOrClientName());
        supplierCommercialStatement.setCompanyMarket(commercialStatementGeneration.getCompanyMarket());
        supplierCommercialStatement.setTotalServiceTax(BigDecimal.ZERO);
        supplierCommercialStatement.setSupplierOrClientCommercialId(commercialStatementGeneration.getCommercialId());

        /*for (CommercialStatementDetails commercialStatementDetails : commercialStatementDetailsSet)
            for (OrderCommercialDetails orderCommercialDetails : commercialStatementDetails.getOrderCommercialDetails())
                totalAmount = totalAmount.add(orderCommercialDetails.getCommercialValue());*/

        BigDecimal totalAmount=callBrmsCalculator(fixed,slab,commercialStatementGeneration,supplierCommercialStatement);
        if (supplierCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.RECEIVABLE.getValue())) {
            if (!totalAmount.equals(BigDecimal.ZERO))
                supplierCommercialStatement.setTotalReceivable(totalAmount);
            else supplierCommercialStatement.setTotalReceivable(new BigDecimal(300));
            supplierCommercialStatement.setTotalReceived(BigDecimal.ZERO);
            supplierCommercialStatement.setBalanceReceivable(supplierCommercialStatement.getTotalReceivable().subtract(supplierCommercialStatement.getTotalReceived()));
        } else {
            if (!totalAmount.equals(BigDecimal.ZERO))
                supplierCommercialStatement.setTotalPayable(totalAmount);
            else supplierCommercialStatement.setTotalPayable(new BigDecimal(300));
            supplierCommercialStatement.setTotalPaid(BigDecimal.ZERO);
            supplierCommercialStatement.setBalancePayable(supplierCommercialStatement.getTotalPayable().subtract(supplierCommercialStatement.getTotalPaid()));
        }

        for (CommercialStatementDetails commercialStatementDetails : commercialStatementDetailsSet)
            commercialStatementDetails.setCommercialStatement(supplierCommercialStatement);
        supplierCommercialStatement.setCommercialStatementDetails(commercialStatementDetailsSet);

        supplierCommercialStatement.setStatementName("CNK-" + System.currentTimeMillis());
        supplierCommercialStatement.setSettlementStatus(SettlementStatus.UNSETTLED.getValue());
        supplierCommercialStatement.setCreatedDate(ZonedDateTime.now());
        supplierCommercialStatement = supplierCommercialStatementRepo.add(supplierCommercialStatement);
        InlineMessageResource inlineMessageResource = new InlineMessageResource();
        inlineMessageResource.setNotificationType("System");
        inlineMessageResource.setAlertName(supplierAlert);
        ConcurrentHashMap input = new ConcurrentHashMap();
        input.put("statementId", supplierCommercialStatement.getStatementId());
        inlineMessageResource.setDynamicVariables(input);
        alertService.sendInlineMessageAlert(inlineMessageResource);
        return supplierCommercialStatement;
    }

    private Set<OrderCommercialDetails> generateCommercialStatementDetails(OpsProduct opsProduct, Set<OrderCommercialDetails> orderCommercialDetailsSet) {

        Set<PassengerDetails> passengerDetailsSet = new HashSet<>();
        OrderCommercialDetails orderCommercialDetails = new OrderCommercialDetails();

        switch (opsProduct.getProductSubCategory()) {
            case "Flight":
                for (OpsFlightPaxInfo opsFlightPaxInfo : opsProduct.getOrderDetails().getFlightDetails().getPaxInfo()) {
                    PassengerDetails passengerDetails = new PassengerDetails();
                    passengerDetails.setPassengerName(opsFlightPaxInfo.getFirstName());
                    passengerDetails.setPassengerType(opsFlightPaxInfo.getPaxType());
                    passengerDetailsSet.add(passengerDetails);
                }
                orderCommercialDetails.setSupplierOrClientCostPrice(new BigDecimal(opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getSupplierPrice()));
                break;

            case "Hotel":
               /* if (!StringUtils.isEmpty(commercialStatementGeneration.getProductName())) {
                    if (!opsProduct.getOrderDetails().getHotelDetails().getHotelName().equalsIgnoreCase(commercialStatementGeneration.getProductName()))
                        return null;
                }*/
                for (OpsRoom opsRoom : opsProduct.getOrderDetails().getHotelDetails().getRooms()) {
                    for (OpsAccommodationPaxInfo opsAccommodationPaxInfo : opsRoom.getPaxInfo()) {
                        PassengerDetails passengerDetails = new PassengerDetails();
                        passengerDetails.setPassengerType(opsAccommodationPaxInfo.getPaxType());
                        passengerDetails.setPassengerName(opsAccommodationPaxInfo.getFirstName());
                        passengerDetailsSet.add(passengerDetails);
                    }
                }
                orderCommercialDetails.setSupplierOrClientCostPrice(new BigDecimal(opsProduct.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getSupplierPrice()));
                break;
        }

        for (PassengerDetails passengerDetails : passengerDetailsSet)
            passengerDetails.setOrderCommercialDetails(orderCommercialDetails);

        orderCommercialDetails.setOrderId(opsProduct.getOrderID());
        for (OpsOrderSupplierCommercial opsOrderSupplierCommercial : opsProduct.getOrderDetails().getSupplierCommercials()) {
           // if (opsOrderSupplierCommercial.getCommercialType().equalsIgnoreCase(commercialStatementGeneration.getCommercialtype()) && opsOrderSupplierCommercial.getCommercialName().equalsIgnoreCase(commercialStatementGeneration.getCommercialHead())) {
                if (StringUtils.isEmpty(orderCommercialDetails.getCommercialValue()))
                    orderCommercialDetails.setCommercialValue(new BigDecimal(opsOrderSupplierCommercial.getCommercialAmount()));
                else
                    orderCommercialDetails.setCommercialValue(orderCommercialDetails.getCommercialValue().add(new BigDecimal(opsOrderSupplierCommercial.getCommercialAmount())));
            //}
        }

        if (orderCommercialDetails.getCommercialValue()==null) orderCommercialDetails.setCommercialValue(BigDecimal.ZERO);
        orderCommercialDetails.setServiceTax(BigDecimal.ZERO);
        orderCommercialDetails.setNumberOfPassengers(passengerDetailsSet.size());
        orderCommercialDetails.setPassengerDetailsSet(passengerDetailsSet);

        orderCommercialDetailsSet.add(orderCommercialDetails);
        return orderCommercialDetailsSet;
    }

    private LocalDate getStartDate(JSONObject halfYearlyJson) {
        int year = halfYearlyJson.getInt("fromYear");
        String monthStr = halfYearlyJson.getString("fromMonth");
        Month month = Month.valueOf(monthStr.toLowerCase());
        return LocalDate.of(year, month, 1);
    }

    private LocalDate getEndDate(JSONObject halfYearlyJson) {
        int year = halfYearlyJson.getInt("toYear");
        String monthStr = halfYearlyJson.getString("toMonth");
        Month month = Month.valueOf(monthStr.toLowerCase());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month.getValue());
        calendar.set(Calendar.YEAR, year);
        int lastDay = calendar.getActualMaximum(Calendar.DATE);
        return LocalDate.of(year, month, lastDay);
    }

    private LocalDate getSettlementDueDate(JSONObject halfYearlyJson) {
        int year = halfYearlyJson.getInt("settlementDueYear");
        String monthStr = halfYearlyJson.getString("settlementDueMonth");
        int day = halfYearlyJson.getInt("settlementDueDate");
        Month month = Month.valueOf(monthStr.toLowerCase());
        return LocalDate.of(year, month, day);
    }

    private BigDecimal callBrmsCalculator(JSONObject fixed, JSONObject slab, CommercialStatementGeneration commercialStatementGeneration,SupplierCommercialStatement supplierCommercialStatement) {

        BigDecimal totalAmount = BigDecimal.ZERO;
        if (fixed != null) {
            BigDecimal amount=BigDecimal.ZERO;
            if (fixed.has("isAmount") && fixed.getBoolean("isAmount")) {
                BigDecimal valueAmount = fixed.getBigDecimal("valueAmount");
                supplierCommercialStatement.setAmount(true);
                supplierCommercialStatement.setAmountValue(valueAmount);
                amount = amount.add(valueAmount);
            }
            if (fixed.has("isPercentage") && fixed.getBoolean("isPercentage")) {
                JSONObject percentageDetails = fixed.getJSONArray("percentageDetails").getJSONObject(0);
                BigDecimal valuePercentage = percentageDetails.getBigDecimal("valuePercentage");
                String farePriceComponents = percentageDetails.getString("farePriceComponents");
                supplierCommercialStatement.setPercentage(true);
                supplierCommercialStatement.setPercentageValue(valuePercentage);
            }
            JSONObject commercialCalculationDetails=new JSONObject();
            JSONObject commercialDetails = new JSONObject();
            JSONObject otherFees=new JSONObject();
            JSONObject fee = new JSONObject();
            String commercialName=commercialStatementGeneration.getAppliedOnCommercialHead();
            fee.put("commercialName", commercialName);
            fee.put("commercialAmount",amount );
            JSONArray feeDetails=new JSONArray();
            feeDetails.put(0,fee);
            otherFees.put("feeDetails",feeDetails);
            commercialDetails.put("otherFees", otherFees);
            commercialCalculationDetails.put(commercialStatementGeneration.getCommercialHead(),commercialDetails);
            totalAmount=totalAmount.add(internalBrmsCall(commercialCalculationDetails,commercialStatementGeneration));
        }

        else if (slab != null) {
            BigDecimal amount = BigDecimal.ZERO;
            String slabType = slab.getString("slabType");
            if (slab.has("isAmount") && slab.getBoolean("isAmount")) {
                BigDecimal valueAmount = slab.getBigDecimal("valueAmount");
                supplierCommercialStatement.setAmount(true);
                supplierCommercialStatement.setAmountValue(valueAmount);
                amount = amount.add(valueAmount);
            }
            if (slab.has("isPercentage") && slab.getBoolean("isPercentage")) {
                JSONObject percentageDetails = slab.getJSONArray("percentageDetails").getJSONObject(0);
                BigDecimal valuePercentage = percentageDetails.getBigDecimal("valuePercentage");
                String farePriceComponents = percentageDetails.getString("farePriceComponents");
                supplierCommercialStatement.setPercentage(true);
                supplierCommercialStatement.setPercentageValue(valuePercentage);
            }
            JSONObject commercialCalculationDetails = new JSONObject();
            JSONObject commercialDetails = new JSONObject();
            JSONArray slabDetails = new JSONArray();
            JSONObject feeDetails = new JSONObject();
            feeDetails.put("slabType", slabType);
            feeDetails.put("slabTypeValue", supplierCommercialStatement.getCommercialStatementDetails().size());
            slabDetails.put(0, feeDetails);
            commercialDetails.put("slabDetails", slabDetails);
            commercialCalculationDetails.put(commercialStatementGeneration.getCommercialHead(), commercialDetails);
            totalAmount = totalAmount.add(internalBrmsCall(commercialCalculationDetails, commercialStatementGeneration));
        }
        return totalAmount;
    }

    private BigDecimal internalBrmsCall(JSONObject commercialDetails,CommercialStatementGeneration commercialStatementGeneration){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("lookup", "suppliersettlementsession");
            JSONArray commands = new JSONArray();
            commands.put(0, jsonObject);
            JSONObject data = new JSONObject();
            JSONObject insert = new JSONObject();
            insert.put("out-identifier", "Root");
            insert.put("return-object", true);
            insert.put("entry-point", "DEFAULT");
            JSONObject object = new JSONObject();
            JSONObject root = new JSONObject();
            JSONObject header = new JSONObject();
            header.put("operationName", "Search");
            JSONArray businessRuleIntake = new JSONArray();
            JSONObject commonElements = new JSONObject();
            commonElements.put("supplier", commercialStatementGeneration.getSupplierId());
            commonElements.put("productCategorySubType", commercialStatementGeneration.getProductSubCategory());
            commonElements.put("supplierMarket", commercialStatementGeneration.getCompanyMarket());
            commonElements.put("contractValidity",LocalDateTime.now().withNano(0));
            commercialDetails.put("commonElements",commonElements);
            businessRuleIntake.put(0, commercialDetails);
            root.put("header", header);
            root.put("businessRuleIntake", businessRuleIntake);
            object.put("cnk.acco_commercialscalculationengine.suppliersettlementrules.Root", root);
            insert.put("object", object);
            data.put("insert", insert);
            commands.put(0, data);
            jsonObject.put("commands", commands);
            ResponseEntity<String> response = restUtils.postForEntity(supplierBrmsCalculationUrl, qcUtilService.createHeader(jsonObject.toString()), String.class);
            JSONObject responseJson = new JSONObject(response.getBody());
            JSONObject businessRuleResponse = responseJson.getJSONObject("result").getJSONObject("execution-results").getJSONArray("results").getJSONObject(0).getJSONObject("value").getJSONObject("cnk.acco_commercialscalculationengine.suppliersettlementrules.Root").getJSONArray("businessRuleIntake").getJSONObject(0);
            JSONObject responseJSONObject = businessRuleResponse.getJSONObject(commercialStatementGeneration.getCommercialHead());
            Integer commercialAmount = responseJSONObject.getInt("commercialAmount");
            return new BigDecimal(commercialAmount);
        }catch (Exception e){
            return BigDecimal.ZERO;
        }
    }
}
