package com.parse.starter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.contextualundo.ContextualUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.contextualundo.ContextualUndoAdapter.DeleteItemCallback;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.starter.SimpleGestureFilter.SimpleGestureListener;

public class MainActivity extends Activity implements OnItemClickListener, SimpleGestureListener, DeleteItemCallback {
	private SimpleGestureFilter detector;
	DataHandler data = new DataHandler();
	ArrayList<Transakcja> transakcje = data.pobierzListe();
		
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		detector = new SimpleGestureFilter(this,this);
		ListView list = (ListView) findViewById(R.id.list);
        list.setClickable(true);
				
		ParseAnalytics.trackAppOpened(getIntent());
				
		
		Log.i("size", Integer.toString(transakcje.size()));
		
		
		ListAdapter adapter = new ListAdapter(this, transakcje);
        //list.setAdapter(adapter);
        //list.setOnItemClickListener(this);
        
        ContextualUndoAdapter adapterCUA = new ContextualUndoAdapter(adapter, R.layout.undo_row, R.id.undo_row_undobutton, this);
        adapterCUA.setAbsListView(list);
        list.setAdapter(adapterCUA);
        //adapterCUA.setDeleteItemCallback(this);
        
		
        
        
        
        
		/*		
		//data.dodaj("Buty", -25.55, "odzie¿");
		//data.dodaj("Wyp³ata", 825.55, "Z pracy");
		//data.dodaj("Tankowanie BP", -105.55, "Paliwo");
		*/
	}

	public void odswiezListe() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Transakcja");
		query.fromLocalDatastore();
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> listaTransakcji, ParseException e) {
		    	if (e == null) {
		    	    for (int i = 0; i < listaTransakcji.size(); i++) {
		    	    	Transakcja c = new Transakcja(listaTransakcji.get(i).getString("stworzony"), 
		    	    			listaTransakcji.get(i).getString("nazwa"), 
		    	    			listaTransakcji.get(i).getDouble("koszt"));
		    	    	transakcje.add(c); 
		    	    }		    	    
		    	} else {
		    		Log.e("B³¹d", "Error: " + e.getMessage());
		    	}
		    } 
		});		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO klikniecie elementu
	}
	
	@Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
         this.detector.onTouchEvent(me);
       return super.dispatchTouchEvent(me);
    }
    @Override
    public void onSwipe(int direction) {
    	switch (direction) {
    		//case SimpleGestureFilter.SWIPE_RIGHT : break;
    		//case SimpleGestureFilter.SWIPE_LEFT : break;
    		case SimpleGestureFilter.SWIPE_DOWN : Intent intent = new Intent(getBaseContext(), DodanieTransakcji.class);
    			startActivity(intent);
    			overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
                break;
    		//case SimpleGestureFilter.SWIPE_UP : break;      
      	}       
     }
      
     @Override
     public void onDoubleTap() {
        
     }

	@Override
	public void deleteItem(int position) {
		Log.i("size", Integer.toString(transakcje.size()));
		String stworzony = transakcje.get(position).getStworzony();
		data.usun(stworzony);
		transakcje = data.pobierzListe();
	}
	
}
