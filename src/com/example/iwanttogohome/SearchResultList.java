package com.example.iwanttogohome;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SearchResultList extends ListActivity {
	String searchStr = "searchstr";
	String xml = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		setContentView(R.layout.activity_search_result_list_xml);

		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		if (extras != null) {
			searchStr = extras.getString("searchStr");
			if (!(searchStr == "")) {
				xml = SearchResultListXMLfunctions.getXML(searchStr);
			}
		}

		Document doc = SearchResultListXMLfunctions.XMLfromString(xml);

		int numResults = SearchResultListXMLfunctions.numResults(doc);

		if ((numResults <= 0)) {
			Toast.makeText(SearchResultList.this, "xml<=0", Toast.LENGTH_LONG)
					.show();
			finish();
		}

		NodeList nodes = doc.getElementsByTagName("result");

		for (int i = 0; i < nodes.getLength(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();

			Element e = (Element) nodes.item(i);
			map.put("id", SearchResultListXMLfunctions.getValue(e, "id"));
			map.put("type", SearchResultListXMLfunctions.getValue(e, "type"));
			map.put("Number",
					"Маршрут: "
							+ SearchResultListXMLfunctions
									.getValue(e, "number"));
			mylist.add(map);
		}

		ListAdapter adapter = new SimpleAdapter(this, mylist,
				R.layout.activity_search_result_list, new String[] { "Number",
						"type" }, new int[] { R.id.item_title,
						R.id.item_subtitle });

		setListAdapter(adapter);

		final ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) lv
						.getItemAtPosition(position);

				Intent intent = new Intent(SearchResultList.this,
						MainActivity.class);
				String busPointArray = getBusListById(o.get("id").replace("\"",
						"")); // maybe do not need .replace

				String pathPointArray = getPathListByBusId(o.get("id").replace(
						"\"", "")); // maybe do not need .replace
				intent.putExtra("busPointArray", busPointArray);
				intent.putExtra("pathPointArray", pathPointArray);
				startActivity(intent);

			}
		});

	}

	public String getPathListByBusId(String id) {
		String response = null;
		try {
			HttpClient client = new DefaultHttpClient();
			String getURL = "http://bus.admomsk.ru/index.php/getroute/routecol_geo/"
					+ id + "/undefined/";
			HttpGet get = new HttpGet(getURL);
			HttpResponse responseGet = client.execute(get);
			HttpEntity resEntityGet = responseGet.getEntity();
			if (resEntityGet != null) {
				response = EntityUtils.toString(resEntityGet);
				Log.i("GET RESPONSE", response);
			}
		} catch (UnsupportedEncodingException e) {
			response = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		} catch (MalformedURLException e) {
			response = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		} catch (IOException e) {
			response = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		}
		return response;
	}

	public String getBusListById(String id) {

		String response = null;
		try {
			HttpClient client = new DefaultHttpClient();
			String getURL = "http://bus.admomsk.ru/index.php/getroute/getbus/"
					+ id + "/";
			HttpGet get = new HttpGet(getURL);
			HttpResponse responseGet = client.execute(get);
			HttpEntity resEntityGet = responseGet.getEntity();
			if (resEntityGet != null) {
				// do something with the response
				response = EntityUtils.toString(resEntityGet);
				Log.i("GET RESPONSE", response);
			}
		} catch (UnsupportedEncodingException e) {
			response = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		} catch (MalformedURLException e) {
			response = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		} catch (IOException e) {
			response = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		}
		return response;
	}
}
