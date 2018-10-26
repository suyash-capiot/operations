package com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.coxandkings.travel.operations.service.commercialstatements.impl.Priority;

public class SlabbingTree {

	private HashMap<String, SlabbingTree> childMap = new HashMap<String, SlabbingTree>();
	private HashMap<String, HashMap<String, Slab>> slabMap;//This should be initialized for only leaf nodes
	private int mDepth;
	private static final String DEFAULT_KEY="ALL";
	public static final String GET_PREFIX = "get";
	public static final String SET_PREFIX = "set";
	
	static {
		SlabPropsConfig.loadConfig();
	}
	
	public SlabbingTree() {
		this(0);
	}
	
	private SlabbingTree(int depth) {
		this.mDepth = depth;
		initializeTree();
	}
	
	private void initializeTree() {
		if(!isLeafNode()) {
			this.childMap.put(DEFAULT_KEY, new SlabbingTree(mDepth+1));
		}
	}
	
	private void initializeSlabMap() {
		this.slabMap = new HashMap<String, HashMap<String, Slab>>();
	}
	
	private boolean isLeafNode() {
		return (mDepth == SlabPropsConfig.fieldArr.length-1 || SlabPropsConfig.fieldArr.length==0);
	}

	public static class GetterSetter{
    	private Method getter;
		private Method setter;
		
		public Method getGetter() {
			return getter;
		}
		public void setGetter(Method getter) {
			this.getter = getter;
		}
		public Method getSetter() {
			return setter;
		}
		public void setSetter(Method setter) {
			this.setter = setter;
		}
    }
	
	private static class SlabPropsConfig{
		private static Field[] fieldArr = sortPriorityWise(SlabProperties.class.getDeclaredFields());//TODO:sort this by priority
	    private static HashMap<String,GetterSetter> mthdMap = new HashMap<String,GetterSetter>();
	    
	    private static void loadConfig()  {
			Method[] propMethods = SlabProperties.class.getDeclaredMethods();
			for(Field propField:fieldArr) {
				String propName = propField.getName();
				for(Method propMethod:propMethods) {
					GetterSetter get_setMapper = mthdMap.get(propName);
					if(get_setMapper==null) {
						get_setMapper = new GetterSetter();
						mthdMap.put(propName, get_setMapper);
					}
					String mthdNameLwr = propMethod.getName().toLowerCase();
					String propNameLwr = propName.toLowerCase();
					if(GET_PREFIX.concat(propNameLwr).equals(mthdNameLwr))
						get_setMapper.setGetter(propMethod);
					else if(SET_PREFIX.concat(propNameLwr).equals(mthdNameLwr))
						get_setMapper.setSetter(propMethod);
				}
			}
			
		}
	    
	    private static Field[] sortPriorityWise(Field[] dataArr) {
	    	//TODO:change to insertion sort
	    	Field temp;
	    	for(int i=0;i<dataArr.length;i++) {
	    		for(int j=1;j<(dataArr.length-i);j++)
	    		 {
	    			Field curr1 = dataArr[j-1];
					Priority priority1 = (Priority) curr1.getAnnotation(Priority.class);
					int value1=priority1!=null?priority1.value():Integer.MAX_VALUE;
	    			Field curr2 = dataArr[j];
	    			Priority priority2 = (Priority) curr2.getAnnotation(Priority.class);
	    			int value2=priority2!=null?priority2.value():Integer.MAX_VALUE;
	    		  if(value1 > value2)	
	    		  {
	    			  temp = dataArr[j-1];
	    			  dataArr[j-1] = dataArr[j];
	    			  dataArr[j] = temp;
	    		  }
	    		
	    	  }
	    	}
			return dataArr;
		}
	
	    static GetterSetter getGetterSetter(String fieldName) {
	    	return mthdMap.containsKey(fieldName)?mthdMap.get(fieldName):new GetterSetter();
	    }
	    
	    static GetterSetter getGetterSetter(Field field) {
	    	return getGetterSetter(field.getName());
	    }
	}
	
	private String getGetterValue(SlabProperties slabProps) {
		Method propGetter = SlabPropsConfig.getGetterSetter(SlabPropsConfig.fieldArr[mDepth]).getGetter();
		String propVal=null;
		if(propGetter!=null)
			try {
				propVal = (String) propGetter.invoke(slabProps);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				//log here
			}
		return propVal==null || propVal.isEmpty()?DEFAULT_KEY:propVal;
	}
	
	public Collection<Slab> get(SlabProperties slabProps){
		String propVal = getGetterValue(slabProps);
		if(isLeafNode()) {
			HashMap<String, Slab> dataset = this.slabMap.get(propVal);
			return dataset==null?new ArrayList<Slab>():dataset.values();
		}
		SlabbingTree childTree = this.childMap.get(propVal);
		return childTree==null?new ArrayList<Slab>():childTree.get(slabProps);
	}
	
	public void put(SlabProperties slabProps, String slabType, BigDecimal slabValue) {
		String propVal = getGetterValue(slabProps);
		if(isLeafNode()) //If leaf Node add slab data to current value and all. 
		{
			addToSlab(propVal, slabType, slabValue);
			if(!propVal.equals(DEFAULT_KEY)) {
				addToSlab(DEFAULT_KEY, slabType, slabValue);
			}
			return;
		}
		SlabbingTree childTree = this.childMap.get(propVal);
		if(childTree==null) {
			childTree = new SlabbingTree(mDepth+1);
			this.childMap.put(propVal, childTree);
		}
		childTree.put(slabProps, slabType, slabValue);
		if(!propVal.equals(DEFAULT_KEY))
			this.childMap.get(DEFAULT_KEY).put(slabProps, slabType, slabValue);
	}
	
	private void addToSlab(String propVal,String slabType, BigDecimal slabValue) {
		if(this.slabMap == null) {
			initializeSlabMap();
		}
		HashMap<String, Slab> propWiseSlabMap = this.slabMap.get(propVal);
		if(propWiseSlabMap==null) {
			propWiseSlabMap = new HashMap<String,Slab>();
			this.slabMap.put(propVal, propWiseSlabMap);
		}
		Slab slabData = propWiseSlabMap.get(slabType);
		if(slabData==null) {
			slabData = new Slab(slabType, slabValue);
			propWiseSlabMap.put(slabType, slabData);
		}
		else
			slabData.updateSlabValue(slabValue);
	}
	
	public class Slab{
		private String slabType;
		private BigDecimal slabValue = new BigDecimal(0);
		
		Slab(String slabType,BigDecimal slabValue) {
			this.slabType = slabType;
			updateSlabValue(slabValue);
		}
		
		void updateSlabValue(BigDecimal slabValue) {
			if(slabValue!=null)
				this.slabValue = this.slabValue.add(slabValue);
		}

		public String getSlabType() {
			return this.slabType;
		}
		
		public BigDecimal getSlabValue() {
			return this.slabValue;
		}
		
	}
	
	public static void main(String args[]) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		SlabProperties slabProps = new SlabProperties();
		slabProps.setProductCateg("Acco");
		SlabbingTree tree= new SlabbingTree();
		tree.put(slabProps, "abc", new BigDecimal(0));
		tree.put(slabProps, "abc", new BigDecimal(1));
		tree.get(slabProps).forEach(slab->System.out.println(String.format(" {type:%s value:%s} ", slab.getSlabType(),slab.getSlabValue())));
		slabProps.setProductCategSubType("Hotel");
		tree.put(slabProps, "abc", new BigDecimal(1));
		//Collection<Slab> slabLst = tree.get(slabProps);
		tree.get(slabProps).forEach(slab->System.out.println(String.format(" {type:%s value:%s} ", slab.getSlabType(),slab.getSlabValue())));
		slabProps.setProductCategSubType("");
		tree.get(slabProps).forEach(slab->System.out.println(String.format(" {type:%s value:%s} ", slab.getSlabType(),slab.getSlabValue())));
		slabProps.setProductCateg("");
		tree.get(slabProps).forEach(slab->System.out.println(String.format(" {type:%s value:%s} ", slab.getSlabType(),slab.getSlabValue())));
		slabProps.setProductCateg("Air");
		tree.put(slabProps, "abc", new BigDecimal(5));
		slabProps.setProductCateg("");
		tree.get(slabProps).forEach(slab->System.out.println(String.format(" {type:%s value:%s} ", slab.getSlabType(),slab.getSlabValue())));
	}
	
}
