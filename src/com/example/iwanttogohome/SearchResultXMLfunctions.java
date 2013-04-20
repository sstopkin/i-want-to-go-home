package com.example.iwanttogohome;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class SearchResultXMLfunctions {

	public final static Document XMLfromString(String xml){
		
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
	
	/** Returns element value
	  * @param elem element (it is XML tag)
	  * @return Element value otherwise empty String
	  */
	 public final static String getElementValue( Node elem ) {
	     Node kid;
	     if( elem != null){
	         if (elem.hasChildNodes()){
	             for( kid = elem.getFirstChild(); kid != null; kid = kid.getNextSibling() ){
	                 if( kid.getNodeType() == Node.TEXT_NODE  ){
	                     return kid.getNodeValue();
	                 }
	             }
	         }
	     }
	     return "";
	 }
		 
	 public static String getXML(){	 
			String line = null;

			try {
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://p-xr.com/xml");

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

			return line;

	}
	 
	public static int numResults(Document doc){		
		Node results = doc.getDocumentElement();
		int res = -1;
		
		try{
			res = Integer.valueOf(results.getAttributes().getNamedItem("count").getNodeValue());
		}catch(Exception e ){
			res = -1;
		}
		
		return res;
	}

	public static String getValue(Element item, String str) {		
		NodeList n = item.getElementsByTagName(str);		
		return SearchResultXMLfunctions.getElementValue(n.item(0));
	}

	public static String getXMLx() {
			String citateRequestString = "http://bus.admomsk.ru/index.php/search";
			String resultString = new String("");
			try {

				URLConnection connection = null;
				URL url = new URL(citateRequestString);

				connection = url.openConnection();

				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod("POST");

				httpConnection.setRequestProperty("User-Agent", "MyAndroid/1.6");
				httpConnection.setRequestProperty("Content-Language", "ru-RU");
				httpConnection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");

				httpConnection.setDoOutput(true);
				httpConnection.setDoInput(true);

				httpConnection.connect();

				// здесь можем писать в поток данные запроса
				OutputStream os = httpConnection.getOutputStream();
				String str = "method=postQuote&text=14&format=text&lang=ru";
				os.write(str.getBytes());

				os.flush();
				os.close();

				int responseCode = httpConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					InputStream in = httpConnection.getInputStream();

					InputStreamReader isr = new InputStreamReader(in, "UTF-8");

					StringBuffer data = new StringBuffer();
					int c;
					while ((c = isr.read()) != -1) {
						data.append((char) c);
					}

					resultString = new String(data.toString());

				} else {
					resultString = "Server does not respond";
				}
			} catch (MalformedURLException e) {
				resultString = "MalformedURLException:" + e.getMessage();
			} catch (IOException e) {
				resultString = "IOException:" + e.getMessage();
			}

			return resultString;

	}
}
