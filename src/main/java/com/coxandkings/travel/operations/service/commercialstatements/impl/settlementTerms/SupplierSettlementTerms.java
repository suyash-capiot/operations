package com.coxandkings.travel.operations.service.commercialstatements.impl.settlementTerms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementFor;
import com.coxandkings.travel.operations.service.commercialstatements.impl.RestUtilities.QueryParams;

@Component
public class SupplierSettlementTerms extends SettlementTerms {

	private HashMap<String,List<SupplierSettlementDetails>> periodWiseCommMap = new HashMap<String,List<SupplierSettlementDetails>>();
	private HashMap<String,List<SupplierSettlementDetails>> suppWiseCommMap = new HashMap<String,List<SupplierSettlementDetails>>();

	{
		commStatementFor = CommercialStatementFor.SUPPLIER;
	}

	private void updateSuppSettlementTerms() {
		//LocalDate prevDay = LocalDate.now().minusDays(1);
		JSONArray dataArr = getUpdatedTerms();
		for(int i=0;i<dataArr.length();i++) {
			JSONObject dataJson = dataArr.getJSONObject(i);
			String suppId= dataJson.getString("supplierId");
			List<SupplierSettlementDetails> commHeadLst = suppWiseCommMap.get(suppId);
			boolean deleted = dataJson.getBoolean("deleted");
			if(commHeadLst!=null) {
				for(SupplierSettlementDetails commHead:commHeadLst) {
					periodWiseCommMap.get(commHead.periodicityMapKey).remove(commHead);
				}
				commHeadLst.removeAll(commHeadLst);
			}
			if(!deleted) {
				populateSettlementTerms(dataJson);
				return;
			}
		}
	}

	@Override
	protected void populateSettlementTerms(JSONObject dataJson){
		//add commisionable statndard comm
		try {
			String suppId= dataJson.getString("supplierId");
			String suppName= dataJson.getString("supplierName");
			JSONArray commHeads;
			if(dataJson.has("receivableCommercials")) {
				commHeads = dataJson.getJSONObject("receivableCommercials").optJSONArray("commercialHeads");
				if(commHeads!=null)
					populateSettlementTerms(commHeads,suppId,suppName);
			}
			if(dataJson.has("payableCommercials")) {
				commHeads = dataJson.getJSONObject("payableCommercials").optJSONArray("commercialHeads");
				if(commHeads!=null)
					populateSettlementTerms(commHeads,suppId,suppName);
			}
		}
		catch(Exception e) {
			//log
		}
	}

	private void populateSettlementTerms(JSONArray commHeadArr,String suppId,String suppName) {
		List<SupplierSettlementDetails> commHeadLst;
		for(int i=0;i<commHeadArr.length();i++) {
			JSONObject commJson = commHeadArr.getJSONObject(i);
			List<PeriodicityData> periodicityLst = getPeriodicityData(commJson.getJSONObject("periodicity"));
			for(PeriodicityData periodicityData:periodicityLst) {
				String name=commJson.getString("commercialHead");
				int dashIdx = name.indexOf("-");
				String commName=dashIdx!=-1?name.substring(0, dashIdx).trim():name;
				String commType=dashIdx!=-1?name.substring(dashIdx+1, name.length()).trim():"";//blank should never get populated
				SupplierSettlementDetails commHead= new SupplierSettlementDetails(suppId, suppName,commName,commType, commJson.getString("settlementSchedule"),periodicityData);
				commHeadLst = suppWiseCommMap.get(suppId);
				if(commHeadLst==null) {
					commHeadLst=new  ArrayList<SupplierSettlementDetails>();
					suppWiseCommMap.put(suppId,commHeadLst);
				}
				commHeadLst.add(commHead);
				commHeadLst = periodWiseCommMap.get(commHead.periodicityMapKey);
				if(commHeadLst==null) {
					commHeadLst=new  ArrayList<SupplierSettlementDetails>();
					periodWiseCommMap.put(commHead.periodicityMapKey,commHeadLst);
				}
				commHeadLst.add(commHead);


			}
		}
	}

	public List<SupplierSettlementDetails> getAllCommForStatementGeneration(){
		updateSuppSettlementTerms();
		List<SupplierSettlementDetails> resultLst = new ArrayList<SupplierSettlementDetails>();
		LocalDate now = LocalDate.now();
		addCommHeadToLst(resultLst, now,MONTHDAY_PRFX);
		addCommHeadToLst(resultLst, now,WEEKDAY_PRFX);
		addCommHeadToLst(resultLst, now,MONTHEND_PRFX);
		return resultLst;
	}

	private void addCommHeadToLst(List<SupplierSettlementDetails> resultLst,LocalDate date,char dayPfx){
		PeriodicityData tempPeriodicity = new PeriodicityData();
		tempPeriodicity.toDate.dayPrefx=dayPfx;
		switch(dayPfx) {
		case MONTHDAY_PRFX:{
			tempPeriodicity.toDate.day = (short) date.getDayOfMonth();
			break;
		}
		case WEEKDAY_PRFX:{
			tempPeriodicity.toDate.day = getDayIdx(date.getDayOfWeek().toString());
			break;
		}
		case MONTHEND_PRFX:{
			if(date.getDayOfMonth()!=date.lengthOfMonth())
				return;
			break;
		}
		}
		addCommHeadToLst(resultLst, tempPeriodicity);
		tempPeriodicity.toDate.month = (short) date.getMonthValue();
		addCommHeadToLst(resultLst, tempPeriodicity);
		tempPeriodicity.toDate.year = (short) date.getYear();
		addCommHeadToLst(resultLst, tempPeriodicity);
	}

	private void addCommHeadToLst(List<SupplierSettlementDetails> resultLst,PeriodicityData periodicityData) {
		List<SupplierSettlementDetails> tempLst=periodWiseCommMap.get(getPeriodicityMapKey(periodicityData));
		if(tempLst!=null)
			resultLst.addAll(tempLst);
	}

	public class SupplierSettlementDetails extends SettlementDetails{
		private String suppId;
		private String suppName;

		SupplierSettlementDetails(String suppId,String suppName,String commName,String commType,String settlementSchedule,PeriodicityData periodicityData) {
			super(commName,commType,settlementSchedule,periodicityData);
			this.suppId=suppId;
			this.suppName=suppName;
		}

		public String getSuppId(){
			return suppId;
		}

		public String getSuppName(){
			return suppName;
		}

	}

	public static void main(String args[]) throws JSONException, IOException {
		/*String[] arr = DateFormatSymbols.getInstance().getShortMonths();
		for(String month:arr)
			System.out.print(month+" ");
		arr = DateFormatSymbols.getInstance().getWeekdays();
		for(String day:arr)
			System.out.print(day+" ");
		short monthIdx=(short) shortMonths.indexOf("Jan");
		System.out.println(LocalDate.now().getDayOfWeek().name());
		System.out.println(LocalDate.now().minusDays(1).toString());
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		System.out.println(list);
		list.removeAll(list);
		System.out.println(list);*/
		/*SupplierSettlementTerms settleTerms = new SupplierSettlementTerms();
		/*BufferedReader reader = new BufferedReader(new FileReader(new File("D:/Temp/SampleSettlementJson")));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		}*/
		/*JSONArray dataArr=new JSONArray(new String(Files.readAllBytes(Paths.get("D:/Temp/SampleSettlementJson"))));
		for(int i=0;i<dataArr.length();i++) {
			settleTerms.populateSettlementTerms(dataArr.getJSONObject(i));
		}
		List<CommercialHead> lst=settleTerms.getAllCommForStatementGeneration();
		System.out.println(lst.get(0).getFromDate());
		System.out.println(lst.get(0).getToDate());
		System.out.println(lst.get(0).getSettlementDueDate());*/
		/*LocalDate now= LocalDate.now();
		System.out.println(now.getDayOfMonth());
		System.out.println(DayOfWeek.SUNDAY.getValue());
		System.out.println(now.lengthOfMonth());
		ZonedDateTime dateTime =ZonedDateTime.now();
		dateTime.withMonth(1);
		dateTime.withYear(2000);*/
		System.out.println(ZonedDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999));
	}
}
