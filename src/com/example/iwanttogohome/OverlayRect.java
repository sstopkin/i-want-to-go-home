package com.example.iwanttogohome;

import java.util.ArrayList;
import java.util.List;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;
import android.R;
import android.content.Context;
import android.util.Pair;

public class OverlayRect extends Overlay {

	OverlayRectItem overlayRectItem;
	Context mContext;
	MapController mMapController;
	RectRender rectRender;

	public OverlayRect(MapController mapController,
			List<Pair<String, String>> table) {
		super(mapController);
		mMapController = mapController;
		mContext = mapController.getContext();
		rectRender = new RectRender();
		setIRender(rectRender);
		overlayRectItem = new OverlayRectItem(new GeoPoint(0, 0), mContext
				.getResources().getDrawable(R.drawable.btn_star));
		for (int i = 0; i < table.size(); i++) {
			double lan = Double.valueOf(table.get(i).first);
			double lon = Double.valueOf(table.get(i).second);
			overlayRectItem.geoPoint.add(new GeoPoint(lon, lan));
		}
		
		addOverlayItem(overlayRectItem);
	}

	@Override
	public List<OverlayItem> prepareDraw() {
		ArrayList<OverlayItem> draw = new ArrayList<OverlayItem>();
		overlayRectItem.screenPoint.clear();
		for (GeoPoint point : overlayRectItem.geoPoint) {
			overlayRectItem.screenPoint.add(mMapController
					.getScreenPoint(point));
		}
		draw.add(overlayRectItem);
		return draw;
	}

}
