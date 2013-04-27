package com.example.iwanttogohome;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	MapController mMapController;
	OverlayManager mOverlayManager;
	MapView mapView;
	String busPointArray;
	String pathPointArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();

		setContentView(R.layout.activity_main);

		mapView = (MapView) findViewById(R.id.map);
		mMapController = mapView.getMapController();
		mOverlayManager = mMapController.getOverlayManager();
		// mapView.getMapController().setJamsVisible(false);
		mapView.showBuiltInScreenButtons(true);
		mMapController.setPositionNoAnimationTo(new GeoPoint(54.968884,
				73.357678));
		mMapController.setZoomCurrent(11);

		if (extras != null) {
			busPointArray = extras.getString("busPointArray");
			pathPointArray = extras.getString("pathPointArray");
			if (!(busPointArray == "")) {
				try {
					showPath(pathPointArray);// path
					showObject(busPointArray);// bus
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void showPath(String in) throws ParseException {
		// TODO Auto-generated method stub
		// public static Hashtable lines(String in) throws ParseException {
		List<Pair<String, String>> pairList = new ArrayList<Pair<String, String>>();
		JSONParser parser = new JSONParser();

		Object obj = parser.parse(in);
		JSONObject jsonObj = (JSONObject) obj;

		JSONArray ja = (JSONArray) jsonObj.get("features");
		for (int i = 0; i < ja.size(); i++) {
			JSONObject jaFeature = (JSONObject) ja.get(i);
			// System.out.println(jaFeature.get("type"));
			JSONObject jaGeometry = (JSONObject) jaFeature.get("geometry");
			JSONArray jaGeometries = (JSONArray) jaGeometry.get("geometries");
			for (int j = 0; j < jaGeometries.size(); j++) {
				Object obj1 = parser.parse(jaGeometries.get(i).toString());
				JSONObject jsonObj1 = (JSONObject) obj1;
				JSONArray jsonCoords = (JSONArray) jsonObj1.get("coordinates");
				for (int l = 0; l < jsonCoords.size(); l++) {
					JSONArray jsonCoord = (JSONArray) jsonCoords.get(l);
					String lan = jsonCoord.get(0).toString();
					String lon = jsonCoord.get(1).toString();
					Pair<String, String> coords = new Pair<String, String>(lan,
							lon);
					pairList.add(coords);
				}
			}

		}
		OverlayRect overlayRect = new OverlayRect(mMapController, pairList);// path
		//overlayRect.setPriority((byte)1);
		mOverlayManager.addOverlay(overlayRect);

		ArrayList<ArrayList<String>> listOflists = new ArrayList<ArrayList<String>>();
		JSONArray stations = (JSONArray) jsonObj.get("stations");
		for (int i = 0; i < stations.size(); i++) {
			JSONObject jaFeature = (JSONObject) stations.get(i);
			// System.out.println(jaFeature.get("name"));
			ArrayList<String> singleList = new ArrayList<String>();
			singleList.add((String) jaFeature.get("name").toString());
			JSONArray jsonCoord = (JSONArray) jaFeature.get("coordinates");
			singleList.add(jsonCoord.get(0).toString());
			singleList.add(jsonCoord.get(1).toString());
			listOflists.add(singleList);
		}
		renderBusStops(listOflists);
	
		
	}

	private void renderBusStops(ArrayList<ArrayList<String>> listOflists) {
		// Load required resources
		Resources res = getResources();

		// Create a layer of objects for the map
		Overlay busStopOverlay = new Overlay(mMapController);
		OverlayItem bus = null;
		for (int i = 0; i < listOflists.size(); i++) {
			double lon = Double.valueOf(listOflists.get(i).get(1));
			double lan = Double.valueOf(listOflists.get(i).get(2));
			// Create an object for the layer
			bus = new OverlayItem(new GeoPoint(lan, lon),
					res.getDrawable(R.drawable.star));
			// Create a balloon model for the object
			BalloonItem balloonBus = new BalloonItem(this, bus.getGeoPoint());
			balloonBus.setText("asd");// singleList.indexOf(1)
			// // Add the balloon model to the object
			bus.setBalloonItem(balloonBus);
			bus.setPriority((byte) 1);
			// Add the object to the layer
			busStopOverlay.addOverlayItem(bus);
		}
		mOverlayManager.addOverlay(busStopOverlay);
	}

	public void showObject(String busPointArray) throws ParseException {
		// https://code.google.com/p/json-simple/
		JSONParser parser = new JSONParser();

		Object obj;

		obj = parser.parse(busPointArray);

		JSONObject jsonObj = (JSONObject) obj;
		JSONArray ja = (JSONArray) jsonObj.get("vehicles");

		// Load required resources
		Resources res = getResources();

		// Create a layer of objects for the map
		Overlay overlay = new Overlay(mMapController);
		OverlayItem bus = null;
		for (int i = 0; i < ja.size(); i++) {
			Object obj_int = parser.parse(ja.get(i).toString());
			JSONObject jsonObji = (JSONObject) obj_int;
			JSONArray jaint = (JSONArray) jsonObji.get("coordinates");
			Double lon = Double.parseDouble(jaint.get(0).toString());
			Double lan = Double.parseDouble(jaint.get(1).toString());
			String info = jsonObji.get("info").toString();
			int course_int = Integer
					.parseInt(jsonObji.get("course").toString());

			String busType = "bus";
			String icon_set_name = null;
			if ((course_int >= 0) && (course_int <= 15)) {
				icon_set_name = "ic_" + busType + "_64_0";
			}
			if ((course_int > 15 && course_int <= 45)) {
				icon_set_name = "ic_" + busType + "_64_1";
			}
			if ((course_int > 45 && course_int <= 75)) {
				icon_set_name = "ic_" + busType + "_64_2";
			}
			if ((course_int > 75 && course_int <= 105)) {
				icon_set_name = "ic_" + busType + "_64_3";
			}
			if ((course_int > 105 && course_int <= 135)) {
				icon_set_name = "ic_" + busType + "_64_4";
			}
			if ((course_int > 135 && course_int <= 165)) {
				icon_set_name = "ic_" + busType + "_64_5";
			}
			if ((course_int > 165 && course_int <= 195)) {
				icon_set_name = "ic_" + busType + "_64_6";
			}
			if ((course_int > 195 && course_int <= 225)) {
				icon_set_name = "ic_" + busType + "_64_7";
			}
			if ((course_int > 225 && course_int <= 255)) {
				icon_set_name = "ic_" + busType + "_64_8";
			}
			if ((course_int > 255 && course_int <= 285)) {
				icon_set_name = "ic_" + busType + "_64_9";
			}
			if ((course_int > 285 && course_int <= 315)) {
				icon_set_name = "ic_" + busType + "_64_10";
			}
			if ((course_int > 315 && course_int <= 345)) {
				icon_set_name = "ic_" + busType + "_64_11";
			}
			if ((course_int > 345) && (course_int <= 360)) {
				icon_set_name = "ic_" + busType + "_64_0";
			}
			int path = getResources().getIdentifier(icon_set_name, "drawable",
					"com.example.iwanttogohome");
			// Create an object for the layer
			bus = new OverlayItem(new GeoPoint(lan, lon), res.getDrawable(path));

			// Create a balloon model for the object
			BalloonItem balloonBus = new BalloonItem(this, bus.getGeoPoint());
			balloonBus.setText(info);
			// // Add the balloon model to the object
			bus.setBalloonItem(balloonBus);
			// Add the object to the layer
			bus.setPriority((byte) 1);
			overlay.addOverlayItem(bus);
		}
		mOverlayManager.addOverlay(overlay);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		try {
			switch (item.getItemId()) {
			case R.id.action_settings:
				Toast.makeText(MainActivity.this, "Geen resultaten gevonden",
						Toast.LENGTH_LONG).show();
			case R.id.search:
				Intent intent = new Intent(MainActivity.this, SearchForm.class);
				startActivity(intent);
			default:
				return super.onOptionsItemSelected(item);
			}
		} catch (Exception e) {
			// log(e);
		}
		return false;
	}
}
