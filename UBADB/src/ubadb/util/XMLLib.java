package ubadb.util;

import java.io.Reader;
import java.io.Writer;

import ubadb.util.exceptions.XMLLibException;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

/**
 * 
 * NOTE: It currently uses XStream Library but in the future it may change with little impact in the code
 * @author 	dcastro
 * @date	12/05/2008
 */
public class XMLLib
{
	private static XStream xStream;
	
	//[start] Constructor
	static
	{
		xStream = new XStream();
		xStream.setMode(XStream.NO_REFERENCES);	//In this mode, no references are added (all objects are copied into the XML)
	}
	//[end]
	
	//[start] toXML
	public static String toXML(Object obj) throws XMLLibException
	{
		try
		{
			return xStream.toXML(obj);
		}
		catch(XStreamException e)
		{
			throw new XMLLibException(e);
		}
	}
	//[end]

	//[start] toXML (to File)
	public static void toXML(Object obj, Writer out) throws XMLLibException
	{
		try
		{
			xStream.toXML(obj,out);
		}
		catch(XStreamException e)
		{
			throw new XMLLibException(e);
		}
	}
	//[end]
	
	//[start] fromXML
	public static Object fromXML(String xmlObject) throws XMLLibException
	{
		try
		{
			return xStream.fromXML(xmlObject);
		}
		catch(XStreamException e)
		{
			throw new XMLLibException(e);
		}
	}
	//[end]
	
	//[start] fromXML (from File)
	public static Object fromXML(Reader in) throws XMLLibException
	{
		try
		{
			return xStream.fromXML(in);
		}
		catch(XStreamException e)
		{
			throw new XMLLibException(e);
		}
	}
	//[end]
}
