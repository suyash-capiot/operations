package com.coxandkings.travel.operations.service.merge.impl;

import com.coxandkings.travel.ext.model.be.Booking;
import com.coxandkings.travel.ext.model.be.HotelDetails;
import com.coxandkings.travel.ext.model.be.Product;
import com.coxandkings.travel.ext.model.be.Room;
import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.enums.merge.MergeTypeValues;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsAccommodationPaxInfo;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsPaxRoomClientCommercial;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import com.coxandkings.travel.operations.model.core.OpsRoomSuppCommercial;
import com.coxandkings.travel.operations.model.merge.AccommodationBookProduct;
import com.coxandkings.travel.operations.model.merge.BookProduct;
import com.coxandkings.travel.operations.model.merge.Merge;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.repository.merge.MergeRepository;
import com.coxandkings.travel.operations.resource.amendentitycommercial.MarginDetails;
import com.coxandkings.travel.operations.resource.changesuppliername.request.acco.RoomInfo;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentReferenceResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.EmailUsingBodyAndDocumentsResource;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateResource;
import com.coxandkings.travel.operations.resource.merge.AccommodationMergeResource;
import com.coxandkings.travel.operations.resource.merge.AccomodationMergeGroupResource;
import com.coxandkings.travel.operations.resource.merge.BookIdProductResource;
import com.coxandkings.travel.operations.resource.merge.MergeGroupResource;
import com.coxandkings.travel.operations.resource.merge.MergeList;
import com.coxandkings.travel.operations.resource.merge.MergeResource;
import com.coxandkings.travel.operations.resource.merge.SupplierPriceResource;
import com.coxandkings.travel.operations.schedular.MergeJob;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.changesuppliername.ChangeSupplierNameService;
import com.coxandkings.travel.operations.service.commercialstatements.impl.RestUtilities;
import com.coxandkings.travel.operations.service.commercialstatements.impl.RestUtilities.QueryParams;
import com.coxandkings.travel.operations.service.commercialstatements.impl.Utils;
import com.coxandkings.travel.operations.service.merge.MergeService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.quartz.TriggerBuilder.newTrigger;


@Service
public class MergeServiceImpl implements MergeService {

	private static Logger logger = Logger.getLogger(MergeService.class);

	@Autowired
	ChangeSupplierNameService changeSupplierNameService;

	@Autowired
	private MergeRepository mergeRepository;

	@Autowired
	private OpsBookingService opsBookingService;

	@Autowired
	@Qualifier("GenericScheduler")
	private Scheduler scheduler;

	@Value(value = "${ops.email.sendEmailWithTemplate}")
	private String emailUrl;

	@Value("${merge-bookings.document.upload}")
	private String docsUrl;

	@Value("${ops.email.sendEmailUsingBodyAndDocuments}")
	private String sendEmailUsingBodyAndDocuments;

	@Value("${ops.email.fromEmailAddress}")
	private String fromEmailAddress;

	@Value("${merge-bookings.file}")
	private String generalName;

	@Value("${merge-bookings.acco.get-merge-list}")
	private String accoMergeListUrl;

	@Autowired
	private RestUtilities rstUtils;

	@Value("${merge-bookings.acco.getRates}")
	private String accoRatesUrl;
	
	@Value("${merge-bookings.acco.applycommercials}")
	private String bookEngApplyComm;

	@Value("${merge-bookings.acco.getAdvancedDef}")
	private String accoRateAdvncDefUrl;
	
	@Autowired
	private  MDMRestUtils mdmRestUtils;


	private JobDetail getJobDetail(Map<String, String> jobData, Class jobClass) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mergeService", this);

		jobData.forEach(jobDataMap::put);
		return JobBuilder.newJob().ofType(jobClass).storeDurably().setJobData(jobDataMap).build();
	}

	private JobDetail getMergeJobDetail(Map<String, String> jobData) {
		return getJobDetail(jobData, MergeJob.class);
	}

	private void scheduleSplitMergeDetail(String mergeId, String supplierId, Long timeInSeconds) throws SchedulerException {
		Map<String, String> jobData = new HashMap<>();
		jobData.put("mergeId", mergeId);
		jobData.put("supplierId", supplierId);
		scheduleJob(jobData, timeInSeconds);
	}

	private Trigger getTrigger(JobDetail jobDetail, Long timeInSeconds) {
		final long ONE_SECOND_IN_MILLIS = 1000;
		return newTrigger().forJob(jobDetail).startAt(new Date(System.currentTimeMillis() + timeInSeconds * ONE_SECOND_IN_MILLIS)).build();
	}

	private void scheduleJob(Map<String, String> jobData, Long timeInSeconds) throws SchedulerException {
		JobDetail jobDetail = getMergeJobDetail(jobData);
		Trigger trigger = getTrigger(jobDetail, timeInSeconds);
		scheduler.scheduleJob(jobDetail, trigger);
	}



	//executed after kafka booking notification
	public List<Merge> saveMerge(OpsBooking opsBooking) throws OperationException, SchedulerException {
		Set<Merge> merges = new TreeSet<>((a,b) -> a.getId().compareToIgnoreCase(b.getId()));

		if(!bookingEligible(opsBooking)) {
			return null;
		}

		List<OpsProduct> opsProducts = opsBooking.getProducts();

		for(OpsProduct opsProduct: opsProducts) {
			OpsProductCategory productCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
			OpsProductSubCategory productSubCategory = OpsProductSubCategory.getProductSubCategory(productCategory, opsProduct.getProductSubCategory());

			// I have got a product which can be a possible merge product. But first, I should know that the product is eligible for merge
			// Checking if product is eligible for merge

			if(!mergeEligible(productCategory, productSubCategory)) {
				continue;
			}
			Merge currentMerge = new Merge();
			//Assuming every booking to be outside cancellation period
			//TODO: fix the assumption above
			currentMerge.setWithinCancellation(isCancellable(opsProduct,productSubCategory));
			currentMerge.setSupplierId(null);
			currentMerge.setMergeType(getMergeType(productSubCategory));
			List<OpsRoom> opsRooms = opsProduct.getOrderDetails().getHotelDetails().getRooms();

			Set<OpsRoom> opsRoomSet = new TreeSet<>(this::compareRooms);
			opsRoomSet.addAll(opsRooms);

			for(OpsRoom opsRoom: opsRoomSet) {
				List<OpsRoom> matchingRooms = getRoomsWithCriteria(opsRooms, opsRoom, this::compareRooms);

				AccommodationBookProduct accommodationBookProduct = new AccommodationBookProduct();

				accommodationBookProduct.setCheckInDate(opsRoom.getCheckIn());
				accommodationBookProduct.setCheckOutDate(opsRoom.getCheckOut());
				accommodationBookProduct.setRoomCategory(opsRoom.getRoomTypeInfo().getRoomCategoryID());
				accommodationBookProduct.setRoomType(opsRoom.getRoomTypeInfo().getRoomTypeCode());
				accommodationBookProduct.setBookingReferenceId(opsBooking.getBookID());
				accommodationBookProduct.setProductId(opsProduct.getOrderID());
				accommodationBookProduct.setOldSupplierRef(opsProduct.getSupplierID());
				currentMerge.setPaxCount(currentMerge.getPaxCount() + matchingRooms.stream().mapToInt(t -> t.getPaxInfo().size()).sum());
				currentMerge.getProducts().add(accommodationBookProduct);

				merges.add(matchAndMerge(currentMerge));
			}
		}






		return new ArrayList<>(merges);
	}

	private boolean bookingEligible(OpsBooking opsBooking) {
		// TODO Add paid, confirmed and offline filter
		return true;
	}

	private MergeTypeValues getMergeType(OpsProductSubCategory productSubCategory) {
		MergeTypeValues mergeType=null;
		switch(productSubCategory) {
		case PRODUCT_SUB_CATEGORY_BUS:
			break;
		case PRODUCT_SUB_CATEGORY_CAR:
			break;
		case PRODUCT_SUB_CATEGORY_EVENTS:
			break;
		case PRODUCT_SUB_CATEGORY_FLIGHT:
			break;
		case PRODUCT_SUB_CATEGORY_HOLIDAYS:
			break;
		case PRODUCT_SUB_CATEGORY_HOTELS:
			mergeType=MergeTypeValues.ACCOMMODATION;
			break;
		case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
			break;
		case PRODUCT_SUB_CATEGORY_RAIL:
			break;
		default:
			break;

		}
		return mergeType;
	}

	private Boolean isCancellable(OpsProduct opsProduct, OpsProductSubCategory productSubCategory) {
		boolean cancellable=false;
		switch (productSubCategory) {
		case PRODUCT_SUB_CATEGORY_BUS:
			break;
		case PRODUCT_SUB_CATEGORY_CAR:
			break;
		case PRODUCT_SUB_CATEGORY_EVENTS:
			break;
		case PRODUCT_SUB_CATEGORY_FLIGHT:
			break;
		case PRODUCT_SUB_CATEGORY_HOLIDAYS:
			break;
		case PRODUCT_SUB_CATEGORY_HOTELS:
			cancellable=true;
			break;
		case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
			break;
		case PRODUCT_SUB_CATEGORY_RAIL:
			break;
		default:
			break;
		}
		return cancellable;
	}

	private int compareRooms(OpsRoom room1, OpsRoom room2) {
		if(room1.getCheckIn().equalsIgnoreCase(room2.getCheckIn()) &&
				room1.getRoomTypeInfo().getRoomTypeCode().equalsIgnoreCase(room2.getRoomTypeInfo().getRoomTypeCode()) &&
				room1.getRoomTypeInfo().getRoomCategoryID().equalsIgnoreCase(room2.getRoomTypeInfo().getRoomCategoryID()))
			return 0;
		else
			return 1;
	}


	private List<OpsRoom> getRoomsWithCriteria(List<OpsRoom> opsRooms, OpsRoom opsRoom, Comparator<OpsRoom> opsRoomComparator) {
		List<OpsRoom> matchingRooms = new ArrayList<>();
		for(OpsRoom tempRoom: opsRooms) {
			Integer compareValue = opsRoomComparator.compare(opsRoom, tempRoom);
			if(compareValue == 0) {
				matchingRooms.add(tempRoom);
			}
		}

		return matchingRooms;
	}

	private Merge matchAndMerge(Merge currentMerge) throws OperationException {
		List<Merge> match = mergeRepository.getPotentialAccommodationMerge(currentMerge);

		if(match.size() > 1) {
			throw new OperationException("Fetal error!!!");
		}


		if(match.isEmpty()) {
			currentMerge.setCount(1);
			return mergeRepository.saveOrUpdate(currentMerge);
		} else {
			Merge matchMerge = match.iterator().next();
			matchMerge.setCount(matchMerge.getCount() + currentMerge.getCount());
			matchMerge.setPaxCount(matchMerge.getPaxCount() + currentMerge.getPaxCount());
			matchMerge.getProducts().addAll(currentMerge.getProducts());
			return mergeRepository.saveOrUpdate(matchMerge);
		}
	}

	private boolean mergeEligible(OpsProductCategory productCategory, OpsProductSubCategory productSubCategory) {
		switch (productCategory) {
		case PRODUCT_CATEGORY_ACCOMMODATION:{
			switch (productSubCategory) {
			case PRODUCT_SUB_CATEGORY_HOTELS: {
				return true;
			}
			}
		}
		}
		return false;
	}

	// called from UI
	public List<Merge> saveMerge(String  id) throws OperationException, ParseException, SchedulerException {
		if(id == null) {
			logger.error("A Booking2 without id cannot be processed");
			throw new OperationException("A Booking2 without id cannot be processed");
		}

		OpsBooking opsBooking = opsBookingService.getBooking(id);
		return saveMerge(opsBooking);
	}

	@Override
	public MergeList getPotentialMerges(MergeTypeValues mergeTypeValue) throws OperationException {
		MergeList merges;

		switch(mergeTypeValue) {
		case ACCOMMODATION:
			merges=getAccoMergeList();

			//merges = mergeRepository.getPotentialMerges();
			break;
			//            case HOLIDAY_SET_PACKAGE:
			//                merges = holidaySetPackageMergeRepository.getPotentialMerges();
			//                break;
			//            case SHARED_TRANSFER:
			//                merges = sharedTransferMergeRepository.getPotentialMerges();
			//                break;
			//            case ACTIVITY_WITH_SHARED_TRANSFER:
			//                merges = activityWithSharedTransferMergeRepository.getPotentialMerges();
			//                break;
			//            case ACTIVITY_WITHOUT_TRANSFER:
			//                merges = activityWithoutTransferMergeRepository.getPotentialMerges();
			//                break;
		default:
			throw new OperationException("Merge type with this value does not exist!");
		}
		return merges;
	}

	@Override
	public List<MergeGroupResource> getMergeGroup(MergeResource mergeResource) throws OperationException {
		List<MergeGroupResource> mergeGroupResources = new ArrayList<MergeGroupResource>();
		if (mergeResource instanceof AccommodationMergeResource) {
			AccommodationMergeResource accoMergeResource=(AccommodationMergeResource)mergeResource;
			for(BookIdProductResource booking:accoMergeResource.getBookingGroup()) {
				OpsRoom opsRoom=booking.getProduct().getOrderDetails().getHotelDetails().getRooms().stream().filter(room-> room.getRoomID().equals(booking.getRoomId())).findAny().get();
				AccomodationMergeGroupResource mergeGroupResource=new AccomodationMergeGroupResource();
				mergeGroupResource.setBookingRefNumber(booking.getBookingRefId());
				mergeGroupResource.setOrderId(booking.getProduct().getOrderID());
				mergeGroupResource.setRoomId(opsRoom.getRoomID());
				mergeGroupResource.setCheckInDate(accoMergeResource.getCheckInDate());
				mergeGroupResource.setCheckOutDate(accoMergeResource.getCheckOutDate());
				mergeGroupResource.setHotelName(accoMergeResource.getHotelName());
				PaxDetails paxDetails = new PaxDetails(opsRoom);
				mergeGroupResource.setPaxCount(paxDetails.count);
				mergeGroupResource.setRoomCount(accoMergeResource.getRoomCount());
				mergeGroupResource.setLeadPassengerName(paxDetails.getString());
				mergeGroupResource.setMargin(getMargin(opsRoom));
				mergeGroupResource.setRoomCategory(accoMergeResource.getRoomCategory());
				mergeGroupResource.setRoomType(accoMergeResource.getRoomType());
				mergeGroupResource.setSupplierName(booking.getProduct().getEnamblerSupplierName());
				mergeGroupResource.setSupplierId(booking.getProduct().getSupplierID());
				mergeGroupResource.setMergeType(MergeTypeValues.ACCOMMODATION);
				mergeGroupResources.add(mergeGroupResource);
			}
		}
		return mergeGroupResources;
	}

	public JSONArray getPrice(List<MergeGroupResource> mergeGroupResources) throws OperationException {
		JSONArray resultArr = new JSONArray();
		if(mergeGroupResources==null || mergeGroupResources.isEmpty()) {
			return resultArr;
		}
		MergeGroupResource tempMrgRsrc = mergeGroupResources.get(0);
		Set<String> suppIdSet = new HashSet<String>();
		int totalPaxCount=0;
		for(MergeGroupResource mergeGrpRsrc:mergeGroupResources) {
			suppIdSet.add(mergeGrpRsrc.getSupplierId());
			totalPaxCount+=mergeGrpRsrc.getPaxCount();
			
			/*BookingSearchCriteria bkngSrchCriteria = new BookingSearchCriteria();
			BookingDetailsFilter bkngFilter = new BookingDetailsFilter();
			bkngFilter.setBookingRefId(mergeGrpRsrc.getBookingRefNumber());
			bkngSrchCriteria.setPage(1);
			bkngSrchCriteria.setSize(1);
			bkngSrchCriteria.setBookingBasedFilter(bkngFilter);
			JSONObject bookJson = (JSONObject) Utils.getBookingByCriteriaSlabWise(bkngSrchCriteria).get(0);*/
		}
		if(tempMrgRsrc instanceof AccomodationMergeGroupResource) {
			AccomodationMergeGroupResource accoMergeRsrc = (AccomodationMergeGroupResource)tempMrgRsrc;
			QueryParams queryParams = rstUtils.new QueryParams();
			JSONObject filter = new JSONObject();
			filter.put("defineRates.status", "Active");
			filter.put("deactivate", false);
			//TODO:check validity too
			filter.put("productName",accoMergeRsrc.getHotelName());
			filter.put("supplierId",new JSONObject().put("$in", new JSONArray(suppIdSet)));
			queryParams.filter =filter.toString();
			JSONArray accoRateArr = rstUtils.getMDMData(accoRatesUrl, queryParams);
			//JSONArray accoRateArr = Utils.getAccoRateFromFile();
			JSONObject accoRate;
			
			/******************************************/
			//Map<MergeGroupResource,JSONObject> opsReqMap = new HashMap<MergeGroupResource,JSONObject>();
			//for(MergeGroupResource mergeGrpRsrc:mergeGroupResources) {
				//AccomodationMergeGroupResource tempAccoMergeRsrc = (AccomodationMergeGroupResource)mergeGrpRsrc;
				Booking bookingObject = opsBookingService.getRawBooking(accoMergeRsrc.getBookingRefNumber());
				Product productObject=null;
				Room roomObject = null;
				for(Product prod:bookingObject.getBookingResponseBody().getProducts()) {
					if(prod.getOrderID().equals(accoMergeRsrc.getOrderId())) {
						for(Room room:prod.getOrderDetails().getHotelDetails().getRooms()) {
							if(room.getRoomID().equals(accoMergeRsrc.getRoomId())) {
								roomObject = room;
								break;
							}
						}
						productObject=prod;
						break;
					}
				}
				//handle null conditions here
				JSONObject opsReq = new JSONObject();
				opsReq.put("commercialsOperation", "Booking");
				opsReq.put("requestHeader", new JSONObject(bookingObject.getBookingResponseHeader()));
				JSONObject reqBody = new JSONObject();
				opsReq.put("requestBody", reqBody);
				JSONObject resBody = new JSONObject();
				opsReq.put("responseBody", resBody);
				
				JSONObject accoInfoReq = new JSONObject();
				accoInfoReq.put("checkIn", accoMergeRsrc.getCheckInDate());
				accoInfoReq.put("checkOut", accoMergeRsrc.getCheckOutDate());
				reqBody.append("accommodationInfo", accoInfoReq);
				
				JSONObject roomInfo = new JSONObject();
				
				HotelDetails hotelObject = productObject.getOrderDetails().getHotelDetails();
				JSONObject hotelInfo = new JSONObject();
				hotelInfo.put("hotelCode", hotelObject.getHotelCode());
				hotelInfo.put("hotelName", hotelObject.getHotelName());
				
				JSONObject mealInfo = new JSONObject();
				mealInfo.put("mealName", roomObject.getMealInfo().getMealName());
				mealInfo.put("mealCode", roomObject.getMealInfo().getMealID());
				
				JSONObject roomTypeInfo = new JSONObject(roomObject.getRoomTypeInfo());
				roomTypeInfo.put("roomCategoryCode", roomObject.getRoomTypeInfo().getRoomCategoryID());
				
				roomInfo.put("hotelInfo", hotelInfo);
				roomInfo.put("ratePlanInfo", new JSONObject(roomObject.getRatePlanInfo()).put("ratePlanName", roomObject.getRatePlanInfo().getRatePlanname()));
				roomInfo.put("mealInfo", mealInfo);
				roomInfo.put("roomTypeInfo", roomTypeInfo);
				JSONObject accoInfo = new JSONObject();
				JSONObject roomStay = new JSONObject();
				roomStay.put("roomInfo", roomInfo);
				roomStay.put("nightlyPriceInfo", new JSONArray());
				accoInfo.append("roomStay", roomStay);
				resBody.append("accommodationInfo", accoInfo);
				
				//opsReqMap.put(mergeGrpRsrc, opsReq);
			//}
			/******************************************/
			
			for(int i=0;i<accoRateArr.length();i++) {
				accoRate = (JSONObject) accoRateArr.get(i);
				JSONArray multiRateArr = (JSONArray) accoRate.getJSONObject("defineRates").remove("addDefineRates");//TODo:do null check
				if(multiRateArr==null)
					continue;
				JSONObject parentRate;
				String curr = ((JSONObject) accoRate).getJSONObject("defineRates").optString("currency", "");
				for(int j=0;j<multiRateArr.length();j++) {
					parentRate = (JSONObject) multiRateArr.get(j);
					if(!("Active".equals(parentRate.getString("status"))))
						continue;
					String travelFrom = parentRate.optString("travelFrom");
					String travelTo = parentRate.optString("travelTo");
					if(!(travelFrom!=null && travelTo!=null && 
							accoMergeRsrc.getCheckInDate().equals(travelFrom.substring(0,travelFrom.indexOf("T")==-1?travelFrom.length():travelFrom.indexOf("T"))) && 
							accoMergeRsrc.getCheckOutDate().equals(travelTo.substring(0,travelFrom.indexOf("T")==-1?travelFrom.length():travelFrom.indexOf("T"))))) {
						continue;
					}
					JSONArray roomWiseRateArr = (JSONArray) parentRate.remove("childRecordScreen1");//TODO: and do same for childScreen2
					if(roomWiseRateArr==null)
						continue;
					JSONObject roomRate;
					for(int k=0;k<roomWiseRateArr.length();k++) {
						roomRate = (JSONObject) roomWiseRateArr.get(k);
						if(!("Active".equals(roomRate.getString("status"))))
							continue;
						if(!(accoMergeRsrc.getRoomCategory().equals(roomRate.optString("roomCategory"))))
							continue;
						JSONArray roomTypeArr = roomRate.getJSONArray("roomTypeDetails");
						JSONObject roomRequired=null;
						for(Object roomType:roomTypeArr) {
							if(accoMergeRsrc.getRoomType().equals(((JSONObject) roomType).optString("roomType")) && ((JSONObject)roomType).has("value")) {
								roomRequired = (JSONObject) roomType;
								break;
							}
						}
						if(roomRequired==null)
							continue;
						JSONObject advncdDef = roomRate.optJSONObject("advanceDefinition");
						if(advncdDef!=null) {
							String advncDefId = advncdDef.optString("advanceDefinitionId");
							if(advncDefId!=null && !advncDefId.isEmpty()) {
								filter = new JSONObject();
								JSONObject data = (JSONObject) rstUtils.getMDMData(String.format(accoRateAdvncDefUrl, advncDefId), null,false);
								if(data!=null) {
									JSONObject advDef =  roomRate.optJSONObject("advanceDefinition");
									if(advDef!=null) {
										int minPax=data.optInt("minimumNumberOfPax", -1);
										int maxPax=data.optInt("maximumNumberOfPax", -1);
										if(minPax==-1 || minPax<=totalPaxCount)
											if(maxPax==-1 || maxPax>=totalPaxCount)
												advncdDef=data;
									}
								}
								
							}
						}
						roomRate.put("advanceDefinition", advncdDef);
						BigDecimal newNetSupplierCost = roomRequired.getBigDecimal("value");
						BigDecimal newSupplierCommercialPayables = new BigDecimal(0);
						BigDecimal newSupplierCommercialsReveivables = new BigDecimal(0);
						JSONArray suppCommJsonArr=null;
						/*************************************/
						//for(String supp:suppIdSet) {
							roomStay.put("supplierRef", accoRate.getString("supplierId"));
							roomStay.put("accommodationSubType", accoRate.getString("productCategorySubType"));
							accoInfoReq.put("accommodationSubType", accoRate.getString("productCategorySubType"));
							JSONObject totalPriceJson = new JSONObject();
							totalPriceJson.put("amount", newNetSupplierCost);
							totalPriceJson.put("taxes", new JSONObject());
							totalPriceJson.put("currencyCode", curr);
							roomStay.put("totalPriceInfo", totalPriceJson);
							String commResponse=null;
							try {
								HttpHeaders httpHeaders = new HttpHeaders();
					            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
					            HttpEntity<String> httpEntity = new HttpEntity<String>(opsReq.toString(), httpHeaders);
								commResponse = RestUtils.postForEntity(bookEngApplyComm,httpEntity, String.class).getBody();
								suppCommJsonArr = ((JSONObject) ((JSONObject) new JSONObject(commResponse).getJSONObject("responseBody").getJSONArray("accommodationInfo").get(0)).getJSONArray("roomStay").get(0)).getJSONArray("supplierCommercials");
							} catch (Exception e) {
							}
							if(suppCommJsonArr!=null) {
								for(Object suppCommJson:suppCommJsonArr) {
									if("Receivable".equals(((JSONObject)suppCommJson).getString("commercialType"))) {
										newSupplierCommercialsReveivables.add(((JSONObject)suppCommJson).getBigDecimal("commercialAmount"));
									}
									else {
										newSupplierCommercialPayables.add(((JSONObject)suppCommJson).getBigDecimal("commercialAmount"));
									}
								}
								newNetSupplierCost = newNetSupplierCost.add(newSupplierCommercialPayables).subtract(newSupplierCommercialsReveivables);
							}
							
						//}
						JSONObject totalmarginDetails = new JSONObject();
						JSONArray marginDetailsArr = new JSONArray();
						MarginDetails totalOldMargin = new MarginDetails();
						MarginDetails totalNewMargin = new MarginDetails();
						for(MergeGroupResource mergeGrpRsrc:mergeGroupResources) {	
							MarginDetails oldMargin = mergeGrpRsrc.getMargin();
							MarginDetails newMargin = new MarginDetails();
							newMargin.setNetSupplierCost(newNetSupplierCost);
							newMargin.setNetSellingPrice(oldMargin.getNetSellingPrice());
							newMargin.setSupplierCommercialPayables(newSupplierCommercialPayables);
							newMargin.setSupplierCommercialsReveivables(newSupplierCommercialsReveivables);
							newMargin.setClientCommercialPayables(oldMargin.getClientCommercialPayables());
							newMargin.setClientCommercialsReveivables(oldMargin.getClientCommercialsReveivables());
							newMargin.setCurrencyCode(curr);
							newMargin.setNetMargin(newMargin.getNetSellingPrice().subtract(newMargin.getNetSupplierCost()));
							
							totalOldMargin.setNetSupplierCost(totalOldMargin.getNetSupplierCost().add(oldMargin.getNetSupplierCost()));
							totalOldMargin.setNetSellingPrice(totalOldMargin.getNetSellingPrice().add(oldMargin.getNetSellingPrice()));
							totalOldMargin.setSupplierCommercialPayables(totalOldMargin.getSupplierCommercialPayables().add(oldMargin.getSupplierCommercialPayables()));
							totalOldMargin.setSupplierCommercialsReveivables(totalOldMargin.getSupplierCommercialsReveivables().add(oldMargin.getSupplierCommercialsReveivables()));
							totalOldMargin.setClientCommercialPayables(totalOldMargin.getClientCommercialPayables().add(oldMargin.getClientCommercialPayables()));
							totalOldMargin.setClientCommercialsReveivables(totalOldMargin.getClientCommercialsReveivables().add(oldMargin.getClientCommercialsReveivables()));
							totalOldMargin.setCurrencyCode(curr);
							totalOldMargin.setNetMargin(totalOldMargin.getNetSellingPrice().subtract(totalOldMargin.getNetSupplierCost()));
							
							totalNewMargin.setNetSupplierCost(totalNewMargin.getNetSupplierCost().add(newMargin.getNetSupplierCost()));
							totalNewMargin.setNetSellingPrice(totalNewMargin.getNetSellingPrice().add(newMargin.getNetSellingPrice()));
							totalNewMargin.setSupplierCommercialPayables(totalNewMargin.getSupplierCommercialPayables().add(newMargin.getSupplierCommercialPayables()));
							totalNewMargin.setSupplierCommercialsReveivables(totalNewMargin.getSupplierCommercialsReveivables().add(newMargin.getSupplierCommercialsReveivables()));
							totalNewMargin.setClientCommercialPayables(totalNewMargin.getClientCommercialPayables().add(newMargin.getClientCommercialPayables()));
							totalNewMargin.setClientCommercialsReveivables(totalNewMargin.getClientCommercialsReveivables().add(newMargin.getClientCommercialsReveivables()));
							totalNewMargin.setCurrencyCode(curr);
							totalNewMargin.setNetMargin(totalNewMargin.getNetSellingPrice().subtract(totalNewMargin.getNetSupplierCost()));
							
							JSONObject marginDetails = new JSONObject();
							marginDetails.put("bookId", mergeGrpRsrc.getBookingRefNumber());
							marginDetails.put("leadPax", mergeGrpRsrc.getLeadPassengerName());
							marginDetails.put("oldMargin", new JSONObject(oldMargin));
							marginDetails.put("newMargin", new JSONObject(newMargin));
							marginDetailsArr.put(marginDetails);
						}
						totalmarginDetails.put("marginDetails", marginDetailsArr);
						totalmarginDetails.put("roomType", accoMergeRsrc.getRoomType());
						totalmarginDetails.put("totalOldMargin", new JSONObject(totalOldMargin));
						totalmarginDetails.put("totalNewMargin", new JSONObject(totalNewMargin));
						totalmarginDetails.put("mergeId", String.format("MER%d * %d", System.currentTimeMillis()/100,totalPaxCount));
						/*************************************/
						JSONObject accoRateClone = new JSONObject(accoRate.toString());
						JSONObject parentRateClone = new JSONObject(parentRate.toString());
						parentRateClone.put("childRecord", roomRate);
						accoRateClone.getJSONObject("defineRates").put("addDefineRates",parentRateClone);
						accoRateClone.put("totalMarginDetails",totalmarginDetails);
						resultArr.put(accoRateClone);
					}

				}
			}
		}
		return resultArr;
	}
	
	private MarginDetails getMargin(OpsRoom opsRoom) {
		MarginDetails marginDetails = new MarginDetails();
		BigDecimal netMargin = null;
		BigDecimal netSupplierCost = null;
		BigDecimal netSellingPrice = null;
		BigDecimal supplierCost = null;
		BigDecimal supplierCommercialPayables = new BigDecimal(0);
		BigDecimal supplierCommercialsReveivables = new BigDecimal(0);
		BigDecimal clientCommercialPayables = new BigDecimal(0);
		BigDecimal clientCommercialsReveivables = new BigDecimal(0);
		String supplierCurrency = "";
		String clientCurrency = "";

		supplierCost = new BigDecimal(opsRoom.getRoomSuppPriceInfo().getRoomSupplierPrice());
		supplierCurrency = opsRoom.getRoomSuppPriceInfo().getCurrencyCode();
		clientCurrency = opsRoom.getRoomTotalPriceInfo().getCurrencyCode();


		for (OpsRoomSuppCommercial supplierCommercial : opsRoom.getRoomSuppCommercials()) {

			if (supplierCommercial.getCommercialType().equals("Receivable"))
				supplierCommercialsReveivables = supplierCommercialsReveivables
				.add(new BigDecimal(supplierCommercial.getCommercialAmount()));
			else if (supplierCommercial.getCommercialType().equals("Payable"))
				supplierCommercialPayables = supplierCommercialPayables
				.add(new BigDecimal(supplierCommercial.getCommercialAmount()));

		}
		// TODO company flag not present at room level
		if (opsRoom.getOpsClientEntityCommercial().size() > 0) {
			for (OpsPaxRoomClientCommercial clientCommercial : opsRoom.getOpsClientEntityCommercial().get(0)
					.getOpsPaxRoomClientCommercial()) {
				if (clientCommercial.getCommercialType().equals("Receivable"))
					clientCommercialsReveivables = clientCommercialsReveivables
					.add(new BigDecimal(clientCommercial.getCommercialAmount()));
				else if (clientCommercial.getCommercialType().equals("Payable"))
					clientCommercialPayables = clientCommercialPayables
					.add(new BigDecimal(clientCommercial.getCommercialAmount()));
			}
		}

		netSupplierCost = supplierCost.add(supplierCommercialPayables).subtract(supplierCommercialsReveivables);
		netSellingPrice = supplierCost.add(clientCommercialsReveivables).subtract(clientCommercialPayables);
		netMargin = netSellingPrice.subtract(netSupplierCost);

		//BigDecimal roe=RestUtils.getForObject("http://10.24.2.248:8080/booking_engine/getRoe/{supplierCurrency}/{clientCurrency}", BigDecimal.class, supplierCurrency,clientCurrency);
		//System.out.println("ROE Fetched "+roe);
		//TODO check if ROE is required to change everything to client currency
		marginDetails.setCurrencyCode(supplierCurrency);
		marginDetails.setNetMargin(netMargin);
		marginDetails.setNetSellingPrice(netSellingPrice);
		marginDetails.setNetSupplierCost(netSupplierCost);
		marginDetails.setClientCommercialPayables(clientCommercialPayables);
		marginDetails.setClientCommercialsReveivables(clientCommercialsReveivables);
		marginDetails.setSupplierCommercialPayables(supplierCommercialPayables);
		marginDetails.setSupplierCommercialsReveivables(supplierCommercialsReveivables);
		return marginDetails;
	}

	@Deprecated
	private String getLeadPaxName(OpsRoom opsRoom) throws OperationException {
		int count=0;
		OpsAccommodationPaxInfo leadPax=null;
		for(OpsAccommodationPaxInfo paxInfo: opsRoom.getPaxInfo()) {
			if(paxInfo.getLeadPax()) {
				leadPax=paxInfo;
			}
			count++;
		}
		if(leadPax==null) {
			throw new OperationException(Constants.LEAD_PAX_NOT_FOUND,opsRoom.getRoomID());
		}

		return String.format("%s %s %s x %d", leadPax.getTitle(),leadPax.getFirstName(),leadPax.getLastName(),count);
	}

	class PaxDetails{
		private OpsAccommodationPaxInfo leadPax=null;
		private int count;
		private String roomId;

		PaxDetails(OpsRoom opsRoom) {
			for(OpsAccommodationPaxInfo paxInfo: opsRoom.getPaxInfo()) {
				if(paxInfo.getLeadPax()) {
					leadPax=paxInfo;
				}
				count++;
			}
			this.roomId = opsRoom.getRoomID();
		}

		public String getString() throws OperationException{
			if(leadPax==null) {
				throw new OperationException(Constants.LEAD_PAX_NOT_FOUND,roomId);
			}
			return String.format("%s %s %s x %d", leadPax.getTitle(),leadPax.getFirstName(),leadPax.getLastName(),count);
		}
	}


	private MergeList getAccoMergeList() throws OperationException {
		ResponseEntity<List<AccommodationMergeResource>> response = null;
		try {

			response=RestUtils.exchange(accoMergeListUrl,HttpMethod.GET,null, new ParameterizedTypeReference<List<AccommodationMergeResource>>() {});

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new OperationException(Constants.ERROR_LISTING_MERGE_BOOKINGS);
		}
		List<AccommodationMergeResource> mergeResourceList=response.getBody();
		List<AccommodationMergeResource> cancellableResourceList=new ArrayList<AccommodationMergeResource>();
		List<AccommodationMergeResource> nonCancellableResourceList=new ArrayList<AccommodationMergeResource>();
		MergeList mergeList=new MergeList(cancellableResourceList, nonCancellableResourceList);
		for(AccommodationMergeResource mergeResource:mergeResourceList) {

			List<String> book_order_room_ids=Arrays.asList(mergeResource.getBook_order_room_id().split("\\s*,\\s*"));
			AccommodationMergeResource cancellableResource=null;
			AccommodationMergeResource nonCancellableResource=null;
			Map<String,AccommodationMergeResource> supplierBookingGroupMap = new HashMap<String, AccommodationMergeResource>();
			for(String book_order_room_id:book_order_room_ids) {
				BookIdProductResource bookingGroup=new BookIdProductResource();
				String[] ids=book_order_room_id.split("\\|");
				String bookingId=ids[0];
				String orderId=ids[1];
				String roomId=ids[2];
				OpsBooking opsBooking;
				OpsProduct opsProduct;
				try {

					opsBooking=opsBookingService.getBooking(bookingId);
					opsProduct=opsBookingService.getOpsProduct(opsBooking, orderId);
				}
				catch(Exception e) {
					e.printStackTrace();
					continue;
				}

				bookingGroup.setBookingRefId(bookingId);
				bookingGroup.setProduct(opsProduct);
				bookingGroup.setRoomId(roomId);
				String supplierId=opsProduct.getSupplierID();

				if(isRoomWithinCancellationPeriod(opsProduct,roomId)) {




					//grouping bookings from same supplier
					if(supplierBookingGroupMap.containsKey(supplierId)) {
						cancellableResource=supplierBookingGroupMap.get(supplierId);
						cancellableResource.getBookingGroup().add(bookingGroup);
					}
					else {
						cancellableResource=new AccommodationMergeResource();
						cancellableResource.setSupplierName(opsProduct.getEnamblerSupplierName());
						cancellableResource.setSupplierId(supplierId);
						cancellableResource.setBookingGroup(new ArrayList<BookIdProductResource>());
						cancellableResource.getBookingGroup().add(bookingGroup);
						cancellableResource.setWithinCancellation(true);
						cancellableResource.setCheckInDate(mergeResource.getCheckInDate());
						cancellableResource.setCheckOutDate(mergeResource.getCheckOutDate());
						cancellableResource.setHotelName(mergeResource.getHotelName());
						cancellableResource.setRoomCategory(mergeResource.getRoomCategory());
						cancellableResource.setRoomType(mergeResource.getRoomType());
						cancellableResource.setMergeType(MergeTypeValues.ACCOMMODATION);
						supplierBookingGroupMap.put(supplierId, cancellableResource);
					}

				}



				else {
					if(nonCancellableResource==null) {
						nonCancellableResource=new AccommodationMergeResource();
						nonCancellableResource.setBookingGroup(new ArrayList<BookIdProductResource>());
						nonCancellableResource.setWithinCancellation(false);
						nonCancellableResource.setCheckInDate(mergeResource.getCheckInDate());
						nonCancellableResource.setCheckOutDate(mergeResource.getCheckOutDate());
						nonCancellableResource.setHotelName(mergeResource.getHotelName());
						nonCancellableResource.setRoomCategory(mergeResource.getRoomCategory());
						nonCancellableResource.setRoomType(mergeResource.getRoomType());
						nonCancellableResource.setMergeType(MergeTypeValues.ACCOMMODATION);
					}
					nonCancellableResource.getBookingGroup().add(bookingGroup);

				}

			}





			if(supplierBookingGroupMap.size()>0) {

				Iterator<AccommodationMergeResource> supplierBookingGroupItr = supplierBookingGroupMap.values().iterator();
				while (supplierBookingGroupItr.hasNext()) {
					AccommodationMergeResource supplierBookingGroup = supplierBookingGroupItr.next();
					supplierBookingGroup.setRoomCount(supplierBookingGroup.getBookingGroup().size());
					supplierBookingGroup.setBookingCount(cancellableResource.getBookingGroup().stream().map(bookingGroup ->bookingGroup.getBookingRefId()).distinct().collect(Collectors.toList()).size());
					if(supplierBookingGroup.getBookingCount()==1)
						continue;
					cancellableResourceList.add(supplierBookingGroup);
				}


			}

			if(nonCancellableResource!=null) {
				nonCancellableResource.setRoomCount(nonCancellableResource.getBookingGroup().size());
				nonCancellableResource.setBookingCount(nonCancellableResource.getBookingGroup().stream().map(bookingGroup ->bookingGroup.getBookingRefId()).distinct().collect(Collectors.toList()).size());
				if(nonCancellableResource.getBookingCount()==1)
					continue;
				nonCancellableResourceList.add(nonCancellableResource);
			}

		}


		return mergeList;
	}

	private boolean isRoomWithinCancellationPeriod(OpsProduct opsProduct, String roomId) {
		// TODO Check for Cancellation Period Here
		return true;
	}

	@Override
	public String getPrice(String id, String priceResource) {
		//        TODO :
		//         * Calculate Total pax count for given list of products.
		//         * Get the list of suppliers for those products
		//         * Call MDM API to get available rate records, for those suppliers and with the given pax count
		//        "http://10.25.6.26:8080/booking_engine/AccoService/v1/search";

		//TODO: Uncomment later
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root;
		try {
			root = objectMapper.readTree(priceResource);

			JSONObject requestHeader = getRequestHeader();
			JSONObject requestBody = new JSONObject();
			JSONObject body = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject accommodationInfoElement = new JSONObject();
			accommodationInfoElement.put("accommodationSubType", root.at("/rateFor").asText());
			accommodationInfoElement.put("supplierRef", root.at("/supplierRef").asText());
			accommodationInfoElement.put("countryCode", root.at("/countryCode").asText());
			accommodationInfoElement.put("cityCode", root.at("/cityCode").asText());
			accommodationInfoElement.put("hotelCode", root.at("/roomInfo/hotelInfo/hotelCode"));
			accommodationInfoElement.put("checkIn", root.at("/checkIn").asText());
			accommodationInfoElement.put("checkOut", root.at("/checkOut").asText());
			accommodationInfoElement.put("paxNationality", "");
			JsonNode roomInfoElement = root.at("/roomInfo");

			JSONObject roomConfiguration = new JSONObject();
			roomConfiguration.put("roomInfo", new JSONObject(objectMapper.readValue(roomInfoElement.toString(), RoomInfo.class)));
			roomConfiguration.put("adultCount", root.at("/adultCount").asInt());
			roomConfiguration.put("childAges", new ArrayList());

			JSONArray roomConfig = new JSONArray();
			roomConfig.put(roomConfiguration);
			accommodationInfoElement.put("roomConfig", roomConfig);

			JSONArray accommodationInfo = new JSONArray();
			accommodationInfo.put(accommodationInfoElement);
			requestBody.put("accommodationInfo", accommodationInfo);
			body.put("requestHeader", requestHeader);
			body.put("requestBody", requestBody);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity httpEntity = new HttpEntity(body.toString(), httpHeaders);
			RestTemplate restTemplate = RestUtils.getTemplate();

			ResponseEntity<String> responseEntity = restTemplate.exchange("http://10.25.6.26:8080/booking_engine/AccoService/v1/price", HttpMethod.POST, httpEntity, String.class);

			System.out.println(responseEntity.getBody());

			Merge merge = mergeRepository.getById(id);
			BookProduct bookProduct = merge.getProducts().iterator().next();
			changeSupplierNameService.getRatesForAcco(bookProduct.getBookingReferenceId(), bookProduct.getProductId(), bookProduct.getOldSupplierRef());
			//            System.out.println(accoRateResponseResource);
		} catch (IOException | JSONException | OperationException | ParseException e) {
			e.printStackTrace();
		}
		//        File file = new File("D:\\PriceRs.json");
		//        try {
		//            FileInputStream fileInputStream = new FileInputStream(file);
		//            byte[] fileBytes = new byte[(int) file.length()];
		//            try {
		//                fileInputStream.read(fileBytes);
		//            } catch (IOException e) {
		//                e.printStackTrace();
		//            }
		//            String str = new String(fileBytes);
		//            ObjectMapper objectMapper = new ObjectMapper();
		//            try {
		//                AccoRateResponseResource accoRateResponseResource = objectMapper.readValue(str, AccoRateResponseResource.class);
		//
		//
		//            } catch (IOException e) {
		//                e.printStackTrace();
		//            }
		//            System.out.println(str);
		//        } catch (FileNotFoundException e) {
		//            e.printStackTrace();
		//        }
		return null;
	}

	public EmailResponse sendMail() {
		URI url = UriComponentsBuilder.fromUriString(emailUrl).build().encode().toUri();
		HttpEntity<EmailUsingTemplateResource> httpEntity = new HttpEntity<>(getEmail());
		RestTemplate restTemplate = RestUtils.getTemplate();
		ResponseEntity<EmailResponse> emailResponse = restTemplate.exchange(url, HttpMethod.POST, httpEntity, EmailResponse.class);
		return emailResponse.getBody();
	}

	private EmailUsingTemplateResource getEmail() {
		EmailUsingTemplateResource emailUsingTemplateResource = new EmailUsingTemplateResource();
		emailUsingTemplateResource.setSubject("Merge booking: Supplier negotiation");
		emailUsingTemplateResource.setFromMail("smtp.aws@coxandkings.com");
		emailUsingTemplateResource.setToMail(Collections.singletonList("nikhil.gore@coxandkings.com"));
		emailUsingTemplateResource.setPriority(EmailPriority.HIGH);
		TemplateInfo templateInfo = new TemplateInfo();
		templateInfo.setIsActive(true);
		templateInfo.setGroupOfCompanies("");
		templateInfo.setGroupCompany("");
		templateInfo.setCompanyName("Ezeego");
		templateInfo.setSubBusinessUnit("");
		templateInfo.setClientType("B2B");
		templateInfo.setMarket("India");
		templateInfo.setSource("");
		templateInfo.setProductCategory("Accommodation");
		templateInfo.setProductCategorySubType("Hotel");
		templateInfo.setProcess("Operations");
		templateInfo.setFunction("Merge Booking");
		templateInfo.setScenario("");
		templateInfo.setRule1("");
		templateInfo.setRule2("");
		templateInfo.setRule3("");
		templateInfo.setCommunicationType("External");
		templateInfo.setCommunicateTo("");
		templateInfo.setIncomingCommunicationType("");
		templateInfo.setDestination("");
		templateInfo.setBrochure("");
		templateInfo.setTour("");
		emailUsingTemplateResource.setTemplateInfo(templateInfo);

		Map<String,String> dynamicvariables = new HashMap<>();
		dynamicvariables.put("First Name","Aavish"); // TODO: based on bookingId,orderid fetch client details extract first name from that.
		dynamicvariables.put("Booking_Ref_Id","123456");

		emailUsingTemplateResource.setDynamicVariables(dynamicvariables);

		return emailUsingTemplateResource;
	}

	public MultipartFile createPdf(String id) throws FileNotFoundException {
		MultipartFile multipartFile = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		FileOutputStream fileOutputStream = new FileOutputStream(generalName);
		Merge merge = mergeRepository.getById(id);
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, byteArrayOutputStream);
			document.setPageSize(PageSize.A4);
			document.newPage();
			document.open();
			Font fontbold = FontFactory.getFont("Times-Roman", 12, Font.BOLD);
			Font headingFont = FontFactory.getFont("Cambria (Headings)", 10, Font.BOLD);
			Font normalFont = FontFactory.getFont("Calibri (Body)", 9);
			Paragraph heading = new Paragraph("Merge booking details", fontbold);
			heading.setAlignment(Element.ALIGN_CENTER);
			document.add(heading);
			PdfPTable table = new PdfPTable(7);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);

			String[] columnHeads = {"Booking id", "Order id", "Room category", "Room type", "Check in date", "Check out date", "Pax Count"};
			Arrays.stream(columnHeads).map(a -> new Paragraph(a, headingFont)).map(PdfPCell::new).forEach(table::addCell);

			for(BookProduct bookProduct: merge.getProducts()) {
				AccommodationBookProduct accommodationBookProduct = (AccommodationBookProduct) bookProduct;
				List<String> columns = new ArrayList<String>();
				columns.add(accommodationBookProduct.getBookingReferenceId());
				columns.add(accommodationBookProduct.getProductId());
				columns.add(accommodationBookProduct.getRoomCategory());
				columns.add(accommodationBookProduct.getRoomType());
				columns.add(accommodationBookProduct.getCheckInDate());
				columns.add(accommodationBookProduct.getCheckOutDate());
				columns.add(merge.getPaxCount().toString());

				columns.stream().map(a -> new Paragraph(a, normalFont)).map(PdfPCell::new).forEach(table::addCell);
			}
			document.add(table);

			document.close();
			byte bytes[] = byteArrayOutputStream.toByteArray();

			fileOutputStream.write(bytes);
			fileOutputStream.close();
			multipartFile = new MockMultipartFile (generalName, generalName, MediaType.APPLICATION_PDF_VALUE, bytes);
			return multipartFile;
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}

		return multipartFile;
	}

	public String sendMergeListMail(String id) {
		try {
			MultipartFile document  = createPdf(id);
			System.out.println(document);

			LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
			FileSystemResource value = null;
			value = new FileSystemResource(new File(document.getOriginalFilename()));
			params.add("file",value );
			params.add("type", "pdf");
			params.add("name","MergeBookingNegotiation");
			params.add("category","category");

			HttpHeaders headers = new HttpHeaders();

			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity =
					new HttpEntity<>(params, headers);

			ResponseEntity<DocumentReferenceResource> responseEntity = RestUtils.exchange(docsUrl , HttpMethod.POST, requestEntity, DocumentReferenceResource.class);
			DocumentReferenceResource body = responseEntity.getBody();
			System.out.println(body.getId());
			EmailResponse emailResponse = sendAnEmail(body.getId());
			System.out.println(emailResponse.getMesssage());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	private EmailResponse sendAnEmail(String documentId) throws DocumentException {
		URI url = UriComponentsBuilder.fromUriString(sendEmailUsingBodyAndDocuments).build().encode().toUri();
		HttpEntity<EmailUsingBodyAndDocumentsResource> httpEntity = new HttpEntity<>(getBodyAndDocs(documentId));
		RestTemplate restTemplate = RestUtils.getTemplate();
		ResponseEntity<EmailResponse> emailResponse = restTemplate.exchange(url, HttpMethod.POST,httpEntity,EmailResponse.class);
		return emailResponse.getBody();
	}

	private EmailUsingBodyAndDocumentsResource getBodyAndDocs(String documentId) throws DocumentException {

		EmailUsingBodyAndDocumentsResource emailUsingBodyAndDocumentsResource = new EmailUsingBodyAndDocumentsResource();
		emailUsingBodyAndDocumentsResource.setFromMail(fromEmailAddress);
		//TODO: getSupplier name and fetch MDM to get email address of that supplier
		emailUsingBodyAndDocumentsResource.setToMail(Collections.singletonList("nikhil.gore@coxandkings.com"));
		emailUsingBodyAndDocumentsResource.setSubject("Merge booking: Supplier negotiation");
		emailUsingBodyAndDocumentsResource.setPriority(EmailPriority.HIGH);
		emailUsingBodyAndDocumentsResource.setBody("Please find details attached");
		emailUsingBodyAndDocumentsResource.setDocumentReferenceIDs(Collections.singletonList(documentId));
		emailUsingBodyAndDocumentsResource.setFileAttachments(null);
		//emailUsingBodyAndDocumentsResource.setEmailTagResource(null);

		return emailUsingBodyAndDocumentsResource;
	}


	@Override
	public List<SupplierPriceResource> getSupplierRates(String id, Set<AccommodationBookProduct> bookProducts) throws JSONException, IOException, OperationException {
		List<SupplierPriceResource> toReturn = new ArrayList<>();

		Merge merge = mergeRepository.getById(id);
		JSONArray roomConfigs = new JSONArray();

		for(BookProduct bookProduct: bookProducts) {
			AccommodationBookProduct accommodationBookProduct = (AccommodationBookProduct) bookProduct;

			JSONObject roomConfigElement = new JSONObject();
			//            roomConfigElement.put("roomTypeCode", accommodationBookProduct.getRoomType());
			//            roomConfigElement.put("roomCategoryCode", accommodationBookProduct.getRoomCategory());
			OpsProduct opsProduct = opsBookingService.getProduct(bookProduct.getBookingReferenceId(), bookProduct.getProductId());
			for(OpsRoom opsRoom: opsProduct.getOrderDetails().getHotelDetails().getRooms()) {
				roomConfigElement.put("adultCount", opsRoom.getPaxInfo().size());
				roomConfigElement.put("childAges", new ArrayList());

				roomConfigs.put(roomConfigElement);
				roomConfigElement = new JSONObject();
			}

		}

		BookProduct bookProduct = bookProducts.iterator().next();
		OpsProduct opsProduct = opsBookingService.getProduct(bookProduct.getBookingReferenceId(), bookProduct.getProductId());

		JSONObject requestBody = new JSONObject();
		requestBody.put("countryCode", opsProduct.getOrderDetails().getHotelDetails().getCountryCode());
		requestBody.put("cityCode", opsProduct.getOrderDetails().getHotelDetails().getCityCode());
		requestBody.put("hotelCode", opsProduct.getOrderDetails().getHotelDetails().getHotelCode());
		requestBody.put("checkIn", opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckIn());
		requestBody.put("checkOut", opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckOut());
		requestBody.put("paxNationality", "");
		requestBody.put("roomConfig", roomConfigs);
		JSONArray accoSubTypes = new JSONArray();
		accoSubTypes.put("Hotel");
		requestBody.put("accommodationSubTypes", accoSubTypes);
		JSONObject body = new JSONObject();
		body.put("requestHeader", getRequestHeader());
		body.put("requestBody", requestBody);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity httpEntity = new HttpEntity(body.toString(), httpHeaders);
		RestTemplate restTemplate = RestUtils.getTemplate();
		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange("http://10.24.2.248:8080/booking_engine/AccoService/v1/search", HttpMethod.POST, httpEntity, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode  root = objectMapper.readTree(responseEntity.getBody());
			JsonNode accommodationInfo = root.at("/responseBody/accommodationInfo");

			accommodationInfo.elements().forEachRemaining(tempNode -> {
				JsonNode roomStay = tempNode.at("/roomStay");
				roomStay.elements().forEachRemaining(roomStayNode -> {
					SupplierPriceResource supplierPriceResource = new SupplierPriceResource();

					JsonNode totalPriceInfo = roomStayNode.at("/totalPriceInfo");
					JsonNode roomInfoNode = roomStayNode.at("/roomInfo");

					BigDecimal totalPrice = totalPriceInfo.at("/amount").decimalValue();
					String supplierRef = roomStayNode.at("/supplierRef").asText();
					String roomCategoryName = roomStayNode.at("/roomInfo/roomTypeInfo/roomCategoryName").asText();
					RoomInfo roomInfo = objectMapper.convertValue(roomInfoNode, RoomInfo.class);
					Integer adultCount =  merge.getPaxCount();
					String checkIn = opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckIn();
					String checkOut = opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckOut();
					String accommodationSubType = roomStayNode.at("/accommodationSubType").asText();

					supplierPriceResource.setAccommodationSubType(accommodationSubType);
					supplierPriceResource.setTotalPrice(totalPrice);
					supplierPriceResource.setSupplierRef(supplierRef);
					supplierPriceResource.setRoomCategoryName(roomCategoryName);
					supplierPriceResource.setRoomInfo(roomInfo);
					supplierPriceResource.setAdultCount(adultCount);
					supplierPriceResource.setCheckIn(checkIn);
					supplierPriceResource.setCheckOut(checkOut);
					supplierPriceResource.setCityCode(opsProduct.getOrderDetails().getHotelDetails().getCityCode());
					supplierPriceResource.setCountryCode(opsProduct.getOrderDetails().getHotelDetails().getCountryCode());

					toReturn.add(supplierPriceResource);
				});
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		return toReturn;
	}

	private JSONObject getRequestHeader() throws JSONException {
		JSONObject clientContext = new JSONObject();
		clientContext.put("clientID", "12345");
		clientContext.put("clientType", "B2C");
		clientContext.put("clientCurrency", "INR");
		clientContext.put("company", "BookEngCNKIndia");
		clientContext.put("clientMarket", "India");
		clientContext.put("clientLanguage", "English");
		clientContext.put("pointOfSale", "http://www.ezeego1.in");
		JSONObject requestHeader = new JSONObject();
		requestHeader.put("userID", "WEM-search");
		Double random = Math.random() * 1000000;
		requestHeader.put("sessionID", "BE-20171215-" + random.intValue());
		requestHeader.put("transactionID", "BE-20171215-2903");
		requestHeader.put("clientContext", clientContext);
		return requestHeader;
	}



}
