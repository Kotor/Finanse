package com.parse.starter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends Activity  {
	final ArrayList<Transakcja> transakcje = new ArrayList<Transakcja>();
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ListView list = (ListView) findViewById(R.id.list);
        list.setClickable(true);
				
		ParseAnalytics.trackAppOpened(getIntent());
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Transakcja");
		query.fromLocalDatastore();
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> listaTransakcji, ParseException e) {
		    	if (e == null) {
		    	    for (int i = 0; i < listaTransakcji.size(); i++) {
		    	    	Transakcja c = new Transakcja(listaTransakcji.get(i).getString("nazwa"), listaTransakcji.get(i).getDouble("koszt"));
		    	    	transakcje.add(c); 
		    	    }		    	    
		    	} else {
		    		Log.e("B³¹d", "Error: " + e.getMessage());
		    	}
		    } 
		});
		
		ListAdapter adapter = new ListAdapter(this, transakcje);
        list.setAdapter(adapter);
		
		
		/*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, foos);
		lv.setAdapter(arrayAdapter);*/
		
		
		
		
		/*
		DataHandler data = new DataHandler();
		//data.dodaj("Buty", -25.55, "odzie¿");
		//data.dodaj("Wyp³ata", 825.55, "Z pracy");
		//data.dodaj("Tankowanie BP", -105.55, "Paliwo");
		data.pobierzListe();
		*/
	}
}
