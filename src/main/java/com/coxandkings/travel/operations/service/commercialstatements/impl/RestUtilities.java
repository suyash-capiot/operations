package com.coxandkings.travel.operations.service.commercialstatements.impl;

import java.math.BigDecimal;
import java.net.URI;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.coxandkings.travel.operations.utils.MDMRestUtils;

@Service
public class RestUtilities {
	
	public static final String QUERY_PARAM_FILTER="filter";
	public static final String QUERY_PARAM_PAGE="page";
	public static final String QUERY_PARAM_COUNT="count";
	public static final String QUERY_PARAM_SORT="sort";

	@Value(value = "${mdm.getSlabTypes}")
	private  String getSlabTypes;
	
	@Value(value = "${commercial-statements.supplier-settlement-url}")
	private String getSuppSettlTerms;
	
	@Value(value = "${commercial-statements.client-settlement-url}")
	private String getClntSettlTerms;
	
	@Value(value = "${daily_roe}")
	protected String bookingEngRoe;
	
	@Autowired
	private  MDMRestUtils mdmRestUtils;
	
	@Autowired
	private com.coxandkings.travel.operations.utils.RestUtils restUtils;
	
	public JSONArray getMDMSlabTypes(String prdctCateg,String prdctCategSubType){
		JSONObject filterJson = new JSONObject();
		filterJson.put("data.productCategory", prdctCateg);
		filterJson.put("data.productCategorySubType", prdctCategSubType);
		filterJson.put("deleted", false);

		QueryParams query = new QueryParams();
		query.filter=filterJson.toString();
		
		return getMDMData(getSlabTypes, query);
	}
	
	public JSONArray getSuppSettlementTerms(QueryParams queryParams) {
		return getMDMData(getSuppSettlTerms, queryParams);
	}
	
	public JSONArray getClientSettlementTerms(QueryParams queryParams) {
		return getMDMData(getClntSettlTerms, queryParams);
	}
	
	public Object getMDMData(String baseURL,QueryParams queryParams,boolean isArr) {
		if(isArr)
			return getMDMData(baseURL, queryParams);
		URI uri = getURI(baseURL, queryParams);
		if(uri==null)
			return null;
		String resJsonStr = getMDMRes(uri);
		if(resJsonStr==null || resJsonStr.isEmpty())
			return null;
		return new JSONObject(resJsonStr);
		
	}
	
	public URI getURI(String baseURL,QueryParams queryParams) {
		if(baseURL==null || baseURL.isEmpty())
			return null;
		UriComponentsBuilder bldr = UriComponentsBuilder.fromUriString(baseURL);
		if(queryParams!=null) {
			if(queryParams.filter!=null)
				bldr.queryParam(QUERY_PARAM_FILTER, queryParams.filter);
			if(queryParams.page>0 && queryParams.count>0) {
				bldr.queryParam(QUERY_PARAM_PAGE, queryParams.page);
				bldr.queryParam(QUERY_PARAM_COUNT, queryParams.count);
			}
			if(queryParams.sort!=null)
				bldr.queryParam(QUERY_PARAM_SORT, queryParams.sort);
		}
		return bldr.build().encode().toUri();
	}
	
	public JSONArray getMDMData(String baseURL,QueryParams queryParams) {
		URI uri = getURI(baseURL, queryParams);
		if(uri==null)
			return new JSONArray();
		return getDataArray(uri);
	}
	
	public String getMDMRes(URI uri) {
		String resJsonStr = "";
        try {
        	resJsonStr=mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class).getBody();
        	return resJsonStr;
        }
        catch (Exception e) {
        	//log
        }
        return resJsonStr;
	}
	
	public JSONArray getDataArray(URI uri) {
		JSONArray resultArr = new JSONArray();
		String resJsonStr = getMDMRes(uri);
		if(resJsonStr==null || resJsonStr.isEmpty())
			return resultArr;
		resultArr = new JSONObject(resJsonStr).optJSONArray("data");
		return resultArr==null?new JSONArray():resultArr;
	}
	
	public class QueryParams {
		public String filter;
		public int page;
		public int count;
		public String sort;
	}
	
	public BigDecimal getInRequestedCurrency(String fromCurrency, String toCurrency,String market) {
		URI uri=UriComponentsBuilder.fromUriString(String.format(bookingEngRoe,fromCurrency,toCurrency,market)).build().encode().toUri();
		try {
			return new BigDecimal(mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class).getBody());
		} catch (Exception e) {
			return new BigDecimal(1);

		} 

	}
	
}
