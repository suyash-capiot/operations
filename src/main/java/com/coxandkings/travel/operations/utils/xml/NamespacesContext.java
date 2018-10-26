package com.coxandkings.travel.operations.utils.xml;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;



public class NamespacesContext implements NamespaceContext {
	
	private static Map<String, String> nsLookup = new HashMap<String, String>();
	
	static {
		Properties properties=new Properties();
		try {
			properties.load(NamespacesContext.class.getClassLoader().getResourceAsStream("NamespacesContext.properties"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Failed To laod NamespacesContext");
			e1.printStackTrace();
			
		}
		nsLookup=properties.entrySet().stream().collect(
			    Collectors.toMap(
			         e -> e.getKey().toString(),
			         e -> e.getValue().toString()
			    )
			);
	}

	public static String getNsURI(String prefix) {
		return nsLookup.get(prefix);
	}

	@Override
	public String getNamespaceURI(String prefix) {
		if (prefix == null) {
			throw new IllegalArgumentException();
		}
		
		if (XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
			return XMLConstants.NULL_NS_URI;
		}
		
		if (XMLConstants.XML_NS_PREFIX.equals(prefix)) {
			return XMLConstants.XML_NS_URI;
		}
		
		if (XMLConstants.XMLNS_ATTRIBUTE.equals(prefix)) {
			return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
		}
		
		return getNsURI(prefix);
	}

	@Override
	public String getPrefix(String namespaceURI) {
		if (namespaceURI == null) {
			throw new IllegalArgumentException();
		}
		
		if (XMLConstants.NULL_NS_URI.equals(namespaceURI)) {
			return XMLConstants.DEFAULT_NS_PREFIX;
		}
		
		if (XMLConstants.XML_NS_URI.equals(namespaceURI)) {
			return XMLConstants.XML_NS_PREFIX;
		}
		
		if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
			return XMLConstants.XMLNS_ATTRIBUTE;
		}
		
		Iterator<Entry<String, String>> entryIter = nsLookup.entrySet().iterator();
		while (entryIter.hasNext()) {
			Entry<String, String> entry = entryIter.next();
			if (entry.getValue().equals(namespaceURI)) {
				return entry.getKey();
			}
		}
		
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Iterator getPrefixes(String namespaceURI) {
		if (namespaceURI == null) {
			throw new IllegalArgumentException();
		}
		
		ArrayList<String> prefixes = new ArrayList<String>();

		if (XMLConstants.XML_NS_URI.equals(namespaceURI)) {
			prefixes.add(XMLConstants.XML_NS_PREFIX);
		}
		
		if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
			prefixes.add(XMLConstants.XMLNS_ATTRIBUTE);
		}

		Iterator<Entry<String, String>> entryIter = nsLookup.entrySet().iterator();
		while (entryIter.hasNext()) {
			Entry<String, String> entry = entryIter.next();
			if (entry.getValue().equals(namespaceURI)) {
				prefixes.add(entry.getKey());
			}
		}

		return prefixes.iterator();
	}

}
