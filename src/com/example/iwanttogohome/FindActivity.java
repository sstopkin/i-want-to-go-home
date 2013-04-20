package com.example.iwanttogohome;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class FindActivity extends Activity {

	TextView t;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find);
		t = new TextView(this);

		t = (TextView) findViewById(R.id.textView1);
//		t.setText(getCitateFromServer());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find, menu);
		return true;
	}

//	public String getCitateFromServer() {
//		String citateRequestString = "http://bus.admomsk.ru/index.php/search";
//		String resultString = new String("");
//		try {
//
//			URLConnection connection = null;
//			URL url = new URL(citateRequestString);
//
//			connection = url.openConnection();
//
//			HttpURLConnection httpConnection = (HttpURLConnection) connection;
//			httpConnection.setRequestMethod("POST");
//
//			httpConnection.setRequestProperty("User-Agent", "MyAndroid/1.6");
//			httpConnection.setRequestProperty("Content-Language", "ru-RU");
//			httpConnection.setRequestProperty("Content-Type",
//					"application/x-www-form-urlencoded");
//
//			httpConnection.setDoOutput(true);
//			httpConnection.setDoInput(true);
//
//			httpConnection.connect();
//
//			// здесь можем писать в поток данные запроса
//			OutputStream os = httpConnection.getOutputStream();
//			String str = "method=postQuote&text=14&format=text&lang=ru";
//			os.write(str.getBytes());
//
//			os.flush();
//			os.close();
//
//			int responseCode = httpConnection.getResponseCode();
//			if (responseCode == HttpURLConnection.HTTP_OK) {
//				InputStream in = httpConnection.getInputStream();
//
//				InputStreamReader isr = new InputStreamReader(in, "UTF-8");
//
//				StringBuffer data = new StringBuffer();
//				int c;
//				while ((c = isr.read()) != -1) {
//					data.append((char) c);
//				}
//
//				resultString = new String(data.toString());
//
//			} else {
//				resultString = "Server does not respond";
//			}
//		} catch (MalformedURLException e) {
//			resultString = "MalformedURLException:" + e.getMessage();
//		} catch (IOException e) {
//			resultString = "IOException:" + e.getMessage();
//		}
//
//		return resultString;
//
//	}

}
