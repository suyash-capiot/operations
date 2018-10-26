package com.coxandkings.travel.operations.utils.xml.xpath;

import com.coxandkings.travel.operations.utils.xml.XMLUtils;
import org.w3c.dom.Node;


public class ConcatsequenceFunction extends BaseXPathFunction {

	ConcatsequenceFunction() {
		mFuncName = "concatsequence";
	}
	
	@Override
	public String evaluate(Node[] xpathNodes, String[] funcArgs) {
		StringBuilder strBldr = new StringBuilder();
		for (Node xpathNode : xpathNodes) {
			strBldr.append(XMLUtils.getNodeValue(xpathNode));
			strBldr.append(','); 
		}
		
		if(strBldr.length()>0)
			strBldr.setLength(strBldr.length()-1);
		else
			strBldr.setLength(0);
		return strBldr.toString();
	}
}
