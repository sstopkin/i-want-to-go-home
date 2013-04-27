package com.example.iwanttogohome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchForm extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_form);
		
		final EditText searchText = (EditText) findViewById(R.id.editText1);
        final Button button = (Button) findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		Intent intent = new Intent(SearchForm.this,
        				SearchResultList.class);
        		String searchStr = searchText.getText().toString(); 
        		intent.putExtra("searchStr", searchStr);
        		startActivity(intent);
            }
        });
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_form, menu);
		return true;
	}

}
