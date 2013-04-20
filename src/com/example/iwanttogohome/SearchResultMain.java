package com.example.iwanttogohome;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SearchResultMain extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresultxml_listplaceholder);
        
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
      
       
        String xml = SearchResultXMLfunctions.getXMLx();
        Document doc = SearchResultXMLfunctions.XMLfromString(xml);
                
        int numResults = SearchResultXMLfunctions.numResults(doc);
        
        if((numResults <= 0)){
        	Toast.makeText(SearchResultMain.this, "Geen resultaten gevonden", Toast.LENGTH_LONG).show();  
        	finish();
        }
                
		NodeList nodes = doc.getElementsByTagName("result");
					
		for (int i = 0; i < nodes.getLength(); i++) {							
			HashMap<String, String> map = new HashMap<String, String>();	
			
			Element e = (Element)nodes.item(i);
			map.put("id", SearchResultXMLfunctions.getValue(e, "id"));
        	map.put("name", "Naam:" + SearchResultXMLfunctions.getValue(e, "name"));
        	map.put("Score", "Score: " + SearchResultXMLfunctions.getValue(e, "score"));
        	mylist.add(map);			
		}		
       
        ListAdapter adapter = new SimpleAdapter(this, mylist , R.layout.activity_search_result, 
                        new String[] { "name", "Score" }, 
                        new int[] { R.id.item_title, R.id.item_subtitle });
        
        setListAdapter(adapter);
        
        final ListView lv = getListView();
        lv.setTextFilterEnabled(true);	
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
        		@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);	        		
        		Toast.makeText(SearchResultMain.this, "ID '" + o.get("id") + "' was clicked.", Toast.LENGTH_LONG).show(); 

			}
		});
    }
}