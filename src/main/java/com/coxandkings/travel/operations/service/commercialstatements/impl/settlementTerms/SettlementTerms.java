package com.coxandkings.travel.operations.service.commercialstatements.impl.settlementTerms;

import java.text.DateFormatSymbols;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementFor;
import com.coxandkings.travel.operations.service.commercialstatements.impl.RestUtilities;
import com.coxandkings.travel.operations.service.commercialstatements.impl.RestUtilities.QueryParams;
import com.coxandkings.travel.operations.service.commercialstatements.impl.Utils;

public abstract class SettlementTerms {
	public static final List<String> months= Arrays.asList(DateFormatSymbols.getInstance().getMonths());//as per mdm data.
	public static final List<String> weekDays= Arrays.asList(DateFormatSymbols.getInstance().getWeekdays());//as per mdm data.
	protected static final short DEFAULT_DAY_MONTH_YEAR=0;
	protected static final char WEEKDAY_PRFX='W';
	protected static final char MONTHDAY_PRFX='M';
	protected static final char MONTHEND_PRFX='E';
	
	@Autowired
	protected RestUtilities mdmUtils;
	
	protected CommercialStatementFor commStatementFor;
	protected String lastUpdatedRcrd="0000-00-00T00:00:00.000Z";
	
	@PostConstruct
	protected void loadSuppSettlementTerms() {
		QueryParams queryParams = mdmUtils.new QueryParams();
		queryParams.sort = "-lastUpdated";
		JSONArray dataArr = getSettlementTerms(queryParams);
		setLastUpdatedValue(dataArr);
		//JSONArray dataArr = Utils.getSettlementTermsFromFile(commStatementFor);
		for(int i=0;i<dataArr.length();i++) {
			populateSettlementTerms(dataArr.getJSONObject(i));
		}
	}
	
	protected abstract void populateSettlementTerms(JSONObject dataJson);
	
	protected JSONArray getUpdatedTerms() {
		QueryParams queryParams = mdmUtils.new QueryParams();
		queryParams.sort = "-lastUpdated";
		JSONObject dateFilter = new JSONObject();
		dateFilter.put("$gt", lastUpdatedRcrd);
		JSONObject filter = new JSONObject();
		filter.put("lastUpdated",dateFilter);
		queryParams.filter=filter.toString();
		
		JSONArray dataArr = getSettlementTerms(queryParams);
		setLastUpdatedValue(dataArr);
		//As mdm on by default provides "not deleted" records,the records which are deleted after last run time needs to be taken into account to
		/*filter.put("deleted", true);//This does work from mdm side
		queryParams.filter=filter.toString();
		List<Object> dataLst = dataArr.toList();
		dataLst.addAll(getSettlementTerms(queryParams).toList());
		dataArr =  new JSONArray(dataLst);
		setLastUpdatedValue(dataArr);*/
		return dataArr;
	}
	
	private JSONArray getSettlementTerms(QueryParams queryParams) {
		switch (commStatementFor) {
		case SUPPLIER:
			return mdmUtils.getSuppSettlementTerms(queryParams);
		case CLIENT:
			return mdmUtils.getClientSettlementTerms(queryParams);
		default:
			return new JSONArray();
		}
	}
	
	protected List<PeriodicityData> getPeriodicityData(JSONObject periodicityJson) {
		List<PeriodicityData> periodicityLst = new ArrayList<PeriodicityData>();
		try{
		String repeatFreq = toCamelCase(periodicityJson.getString("repeatFreq"));
		Object periodObject = periodicityJson.get(repeatFreq);
		//PeriodicityData periodicityData = new PeriodicityData(repeatFreq);
		PeriodicityData periodicityData = new PeriodicityData();
		switch(repeatFreq) {
		case "weekly":{
			if("day".equals(((JSONObject)periodObject).getString("weeklySchedule").toLowerCase())) {
				JSONObject dayJson = ((JSONObject)periodObject).getJSONObject("day");
				periodicityData.setFromDate(dayJson.get("fromDay"), null, null);
				periodicityData.setToDate(dayJson.get("toDay"), null, null);
				periodicityData.setSettlementDate(dayJson.get("settlementDueDay"), null, null);
				addPeriodicityData(periodicityLst,periodicityData);
			}
			else {
				JSONArray dateJsonArr = ((JSONObject)periodObject).getJSONArray("dates");
				for(int i=0;i<dateJsonArr.length();i++) {
					JSONObject dateJson=(JSONObject) dateJsonArr.get(i);
					periodicityData.setFromDate(dateJson.get("fromDate"), dateJson.get("fromMonth"), dateJson.opt("fromYear"));
					periodicityData.setToDate(dateJson.get("toDate"), dateJson.get("toMonth"), dateJson.opt("toYear"));
					periodicityData.setSettlementDate(dateJson.get("settlementDueDate"), dateJson.get("settlementDueMonth"), dateJson.opt("settlementDueYear"));
					addPeriodicityData(periodicityLst,periodicityData);
				}
			}
			break;

		}
		case "monthly":{
			periodicityData.setFromDate(1, null, null);
			periodicityData.toDate.dayPrefx=MONTHEND_PRFX;
			periodicityData.setSettlementDate(((JSONObject)periodObject).get("settlementDueDay"), null, null);
			periodicityData.settlementDueDate.isNextMonth=true;
			addPeriodicityData(periodicityLst,periodicityData);
			break;
		}
		case "quaterly":{
			for(int i=0;i<((JSONArray)periodObject).length();i++) {
				JSONObject quaterJson=(JSONObject) ((JSONArray)periodObject).get(i);
				periodicityData.setFromDate(1, quaterJson.get("fromMonth"), quaterJson.opt("fromYear"));
				periodicityData.setToDate(null, quaterJson.get("toMonth"), quaterJson.opt("toYear"));
				periodicityData.toDate.dayPrefx=MONTHEND_PRFX;
				periodicityData.setSettlementDate(quaterJson.get("settlementDueDate"), quaterJson.get("settlementDueMonth"), quaterJson.opt("settlementDueYear"));
				addPeriodicityData(periodicityLst,periodicityData);
			}
			break;
		}
		case "fortnightly":{
			for(int i=0;i<((JSONArray)periodObject).length();i++) {
				JSONObject fortNghtJson=(JSONObject) ((JSONArray)periodObject).get(i);
				String fortNghtVal=fortNghtJson.optString("fortnight","");
				if(fortNghtVal.toLowerCase().contains("first")) {
					periodicityData.setFromDate(1,null, null);
					periodicityData.setToDate(15,null,null);
				}
				else if(fortNghtVal.toLowerCase().contains("second")) {
					periodicityData.setFromDate(16,null, null);
					periodicityData.toDate.dayPrefx=MONTHEND_PRFX;
				}
				else
					continue;
				periodicityData.setSettlementDate(fortNghtJson.get("settleDueDay"), null,null);
				periodicityData.settlementDueDate.isNextMonth="Next".equals(fortNghtJson.optString("settlementDueMonth"))?true:false;
				addPeriodicityData(periodicityLst,periodicityData);
			}
			break;
		}
		case "halfYearly":{
			for(int i=0;i<((JSONArray)periodObject).length();i++) {
				JSONObject hlfYrlyJson=(JSONObject) ((JSONArray)periodObject).get(i);
				periodicityData.setFromDate(1, hlfYrlyJson.get("fromMonth"), hlfYrlyJson.opt("fromYear"));
				periodicityData.setToDate(null, hlfYrlyJson.get("toMonth"), hlfYrlyJson.opt("toYear"));
				periodicityData.toDate.dayPrefx=MONTHEND_PRFX;
				periodicityData.setSettlementDate(hlfYrlyJson.get("settlementDueDate"), hlfYrlyJson.get("settlementDueMonth"), hlfYrlyJson.opt("settlementDueYear"));
				addPeriodicityData(periodicityLst,periodicityData);
			}
			break;
		}
		case "yearly":{
			JSONObject dateJson = (JSONObject)periodObject; 
			periodicityData.setFromDate(dateJson.has("fromDate")?dateJson.get("fromDate"):1, dateJson.get("fromMonth"), dateJson.opt("fromYear"));
			periodicityData.setToDate(dateJson.opt("toDate"), dateJson.get("toMonth"), dateJson.opt("toYear"));
			if(!dateJson.has("toDate"))
				periodicityData.toDate.dayPrefx=MONTHEND_PRFX;
			periodicityData.setSettlementDate(dateJson.get("settlementDueDate"), dateJson.get("settlementDueMonth"), dateJson.opt("settlementDueYear"));
			addPeriodicityData(periodicityLst,periodicityData);
			break;
		}
		}
		}
		catch(Exception e) {
			//log
		}
		return periodicityLst;

	}
	
	protected class PeriodicityData{
		protected DateDetails fromDate = new DateDetails();
		protected DateDetails toDate=new DateDetails();;
		protected DateDetails settlementDueDate=new DateDetails();
		//private String periodType;
		protected boolean isDataCorrupt;

		/*PeriodicityData() {
		}

		PeriodicityData(String periodtype) {
			this.periodType = periodtype;
		}*/

		protected void setFromDate(Object day,Object month,Object year) {
			setDate(fromDate, day, month, year);
		}

		protected void setToDate(Object day,Object month,Object year) {
			setDate(toDate, day, month, year);
		}

		protected void setSettlementDate(Object day,Object month,Object year) {
			setDate(settlementDueDate, day, month, year);
		}

		private void setDate(DateDetails dateDetails,Object day,Object month,Object year) {
			if(day!=null) {
				try{
					//As mdm provides inconsistent date value. eg in case of quaterly day is number but value is string
					day = Integer.valueOf((String) day);
				}
				catch(Exception e){}
				if(day instanceof String) {
					short dayIdx=getDayIdx((String) day);
					if(dayIdx==-1) {
						isDataCorrupt=true;
						return;
					}
					dateDetails.day = dayIdx;
					dateDetails.dayPrefx = WEEKDAY_PRFX;
				}
				else {
					dateDetails.day = Short.valueOf(day.toString());
					dateDetails.dayPrefx = MONTHDAY_PRFX;
				}
			}
			if(month!=null && month instanceof String) {
				short monthIdx = getMonthIdx((String) month);
				if(monthIdx==-1) {
					isDataCorrupt=true;
					return;
				}
				dateDetails.month = monthIdx;
			}
			if(year!=null)
				dateDetails.year=Short.valueOf(year.toString());

		}

	}

	protected class DateDetails{
		protected short day=DEFAULT_DAY_MONTH_YEAR;
		protected short month=DEFAULT_DAY_MONTH_YEAR;
		protected short year=DEFAULT_DAY_MONTH_YEAR;
		protected char dayPrefx;
		protected boolean isNextMonth;
	}
	
	private void addPeriodicityData(List<PeriodicityData> periodicityLst , PeriodicityData periodicityData) {
		if(!periodicityData.isDataCorrupt)
			periodicityLst.add(periodicityData);
	}
	
	protected String getPeriodicityMapKey(PeriodicityData periodicityData) {
		return String.format("%c%s|%s|%s", periodicityData.toDate.dayPrefx,periodicityData.toDate.day,periodicityData.toDate.month,periodicityData.toDate.year);
	}

	private void setLastUpdatedValue(JSONArray dataArr) {
		if(dataArr.length()>0) {
			String lastUpdated = dataArr.getJSONObject(0).getString("lastUpdated");
			if(lastUpdatedRcrd.compareTo(lastUpdated)<0)
				lastUpdatedRcrd=lastUpdated;
		}
	}
	
	public String toCamelCase(String string) {
		if(string==null || string.isEmpty())
			return string;
		String strArr[] = string.split(" ");
		String rslt = String.format("%c%s",Character.toLowerCase(strArr[0].charAt(0)),strArr[0].substring(1));
		for(int i =1;i<strArr.length;i++)
			rslt=rslt.concat(String.format("%c%s",Character.toUpperCase(strArr[i].charAt(0)),strArr[i].substring(1)));
		return rslt;
		
	}
	
	public short getMonthIdx(String month) {
		short monthIdx = (short) months.indexOf(String.format("%s%s", Character.toUpperCase(month.charAt(0)),((String)month).substring(1).toLowerCase()));
		return (short) (monthIdx==-1?monthIdx:monthIdx+1);
	}

	public short getDayIdx(String weekDay) {
		return (short) weekDays.indexOf(String.format("%s%s", Character.toUpperCase(weekDay.charAt(0)),((String)weekDay).substring(1).toLowerCase()));
		//return (short) (weekDayIdx==-1?weekDayIdx:weekDayIdx+1);
	}
	
	public class SettlementDetails{
		private String name;
		private String type;
		private PeriodicityData periodicityData;
		private String settlementSchedule;
		protected String periodicityMapKey;
		private ZonedDateTime fromDate;
		private ZonedDateTime toDate;
		private ZonedDateTime settlementDueDate;

		SettlementDetails(String commName,String commType,String settlementSchedule,PeriodicityData periodicityData) {
			this.name=commName;
			this.type=commType;//blank should never get populated
			this.periodicityData=periodicityData;
			this.settlementSchedule=settlementSchedule;
			this.periodicityMapKey=getPeriodicityMapKey(periodicityData);
		}

		public String getCommercialHeadName(){
			return name;
		}

		public String getCommercialHeadType(){
			return type;
		}

		public String getSettlementScheduleType(){
			return settlementSchedule;
		}
		
		public CommercialStatementFor getCommercialFor() {
			return commStatementFor;
		}

		public ZonedDateTime getFromDate() {
			if(fromDate==null) {
				fromDate=getDate(periodicityData.fromDate).withHour(0).withMinute(0).withSecond(0).withNano(0);
				if(periodicityData.fromDate.dayPrefx==WEEKDAY_PRFX) {
					if(Math.abs(fromDate.toLocalDate().compareTo(getToDate().toLocalDate()))==6)
						fromDate=fromDate.minusDays(1);
					else
						fromDate=fromDate.minusWeeks(1);
				}
			}
			return fromDate;
		}

		public ZonedDateTime getToDate() {
			if(toDate==null)
				toDate=getDate(periodicityData.toDate).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
			return toDate;
		}

		public ZonedDateTime getSettlementDueDate() {
			if(settlementDueDate==null) {
				settlementDueDate=getDate(periodicityData.settlementDueDate);
				if(periodicityData.settlementDueDate.dayPrefx==WEEKDAY_PRFX) {
					if(settlementDueDate.toLocalDate().compareTo(getToDate().toLocalDate())<0)
						settlementDueDate=settlementDueDate.plusWeeks(1);
				}
			}
			return settlementDueDate;
		}
		
		private ZonedDateTime getDate(DateDetails dateDtls) {
			ZonedDateTime dateTime =ZonedDateTime.now();
			if(dateDtls.year!=DEFAULT_DAY_MONTH_YEAR)
				dateTime=dateTime.withYear(dateDtls.year);
			if(dateDtls.month!=DEFAULT_DAY_MONTH_YEAR)
				dateTime=dateTime.withMonth(dateDtls.month);
			if(dateDtls.isNextMonth)
				dateTime=dateTime.plusMonths(1);
			if(dateDtls.dayPrefx==MONTHDAY_PRFX && dateDtls.day!=DEFAULT_DAY_MONTH_YEAR)
				dateTime=dateTime.withDayOfMonth(dateDtls.day);
			else if(dateDtls.dayPrefx==MONTHEND_PRFX)
				dateTime=dateTime.withDayOfMonth(dateTime.getMonth().maxLength());
			else if(dateDtls.dayPrefx==WEEKDAY_PRFX) {
				int dayIdx = getDayIdx(dateTime.getDayOfWeek().toString());
				if(dateDtls.day<dayIdx)
					dateTime=dateTime.minusDays(dayIdx-dateDtls.day);
				else if(dateDtls.day>dayIdx)
					dateTime=dateTime.plusDays(dateDtls.day-dayIdx);
			}
			return dateTime;
		}

	}
}
