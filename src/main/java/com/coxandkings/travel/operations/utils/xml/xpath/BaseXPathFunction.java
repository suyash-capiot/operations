package com.coxandkings.travel.operations.utils.xml.xpath;

abstract class BaseXPathFunction implements XPathFunction {
	protected String mFuncName;
	
	public String getName() {
		return mFuncName;
	}
}
