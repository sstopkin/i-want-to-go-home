package com.example.iwanttogohome;

//import ru.mapkittest.R;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.utils.GeoPoint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        final MapView mapView = (MapView) findViewById(R.id.map);
//        mapView.getMapController().setJamsVisible(false);
        mapView.showBuiltInScreenButtons(true);
        mapView.getMapController().setPositionNoAnimationTo(new GeoPoint(54.968884,73.357678));
        mapView.getMapController().setZoomCurrent(11);
        
     
        final Button button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SearchResultMain.class);
//				String url = ((EditText)findViewById(R.id.editText1)).getText().toString();
//				intent.putExtra("url", url);
				startActivity(intent);
			}
		});
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
		   try{
		    switch (item.getItemId()) {
		    case R.id.action_settings:            
		        Intent intent = new Intent(this, MainActivity.class);
		        startActivity(intent);    
		    default:
		        return super.onOptionsItemSelected(item);
		    }
		   }catch(Exception e){
//		      log(e);
		   }
		return false;
		}

}
