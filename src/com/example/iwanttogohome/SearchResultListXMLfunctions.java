package com.example.iwanttogohome;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SearchResultListXMLfunctions {

	public final static Document XMLfromString(String xml) {

		Document doc = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}

		return doc;

	}

	/**
	 * Returns element value
	 * 
	 * @param elem
	 *            element (it is XML tag)
	 * @return Element value otherwise empty String
	 */
	public final static String getElementValue(Node elem) {
		Node kid;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (kid = elem.getFirstChild(); kid != null; kid = kid
						.getNextSibling()) {
					if (kid.getNodeType() == Node.TEXT_NODE) {
						return kid.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	public static String getXML(String searchStr) {
		String line = null;

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://bus.admomsk.ru/index.php/search");
			// http://p-xr.com/xml
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
/*    		searchStr="\""+searchStr+"\"";*/
			nameValuePairs.add(new BasicNameValuePair("text", searchStr));
			// nameValuePairs.add(new BasicNameValuePair("sessionid", "234"));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			line = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		} catch (MalformedURLException e) {
			line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		} catch (IOException e) {
			line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		}

		String[] found = line.split("\"");

		/*
		 * <results count="6"> <result> <id>1</id> <name>Mark</name>
		 * <score>6958</score> </result>
		 */
		line = "";
		int j = 0;
		for (int i = 1; i < found.length; i++) {
			if ((i >= 5) && (found[i - 5].matches("bus n trans"))) {
				String[] busNumber = found[i].split("</a>");
				line += "<result> <id>" + found[i - 3] + "</id> "
						+ "<type>автобус</type>" + "<number>"
						+ busNumber[0].substring(1) + "</number>" + "</result>";
				j++;
			}
			if ((i >= 5) && (found[i - 5].matches("tram n trans"))) {
				String[] busNumber = found[i].split("</a>");
				line += "<result> <id>" + found[i - 3] + "</id> "
						+ "<type>трамвай</type>" + "<number>"
						+ busNumber[0].substring(1) + "</number>" + "</result>";
				j++;
			}
			if ((i >= 5) && (found[i - 5].matches("trol n trans"))) {
				String[] busNumber = found[i].split("</a>");
				line += "<result> <id>" + found[i - 3] + "</id> "
						+ "<type>троллейбус</type>" + "<number>"
						+ busNumber[0].substring(1) + "</number>" + "</result>";
				j++;
			}
			if ((i >= 5) && (found[i - 5].matches("taxi n trans"))) {
				String[] busNumber = found[i].split("</a>");
				line += "<result> <id>" + found[i - 3] + "</id> "
						+ "<type>маршрутное такси</type>" + "<number>"
						+ busNumber[0].substring(1) + "</number>" + "</result>";
				j++;
			}
		}
		line += "</results>";
		return "<results count=\"" + j + "\">" + line;
	}

	public static int numResults(Document doc) {
		Node results = doc.getDocumentElement();
		int res = -1;

		try {
			res = Integer.valueOf(results.getAttributes().getNamedItem("count")
					.getNodeValue());
		} catch (Exception e) {
			res = -1;
		}

		return res;
	}

	public static String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return SearchResultListXMLfunctions.getElementValue(n.item(0));
	}

}
