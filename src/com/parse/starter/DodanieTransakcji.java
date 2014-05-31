package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.starter.SimpleGestureFilter.SimpleGestureListener;

public class DodanieTransakcji extends Activity implements SimpleGestureListener {
	private SimpleGestureFilter detector;
	boolean wydatek;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dodanie_transakcji);
		detector = new SimpleGestureFilter(this,this);
		
		// Get a reference to the AutoCompleteTextView in the layout
		final AutoCompleteTextView tag = (AutoCompleteTextView) findViewById(R.id.tag);
		// Get the string array
		String[] tagi = getResources().getStringArray(R.array.tagi);
		// Create the adapter and set it to the AutoCompleteTextView 
		ArrayAdapter<String> adapter = 
		        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tagi);
		tag.setAdapter(adapter);
		
		
		final EditText nazwa, koszt;
		ImageView aparat;
		Switch przelacznik;
		final Button dodaj;
		        
		nazwa = (EditText) findViewById(R.id.nazwa);
		koszt = (EditText) findViewById(R.id.koszt);
		aparat = (ImageView) findViewById(R.id.aparat);
		przelacznik = (Switch) findViewById(R.id.przelacznik);
		dodaj = (Button) findViewById(R.id.dodaj);
		        
		aparat.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Toast msg = Toast.makeText(getBaseContext(),"aparat", Toast.LENGTH_LONG);
		        msg.show();
			}
		});
		
		przelacznik.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					wydatek = true;
			    } else {
			    	wydatek = false;
			    }
			}
		});
		
		dodaj.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String nazwaTxt = nazwa.getText().toString();
				String kosztTxt = koszt.getText().toString();
				if (!wydatek) {
					String min = "-";
					String kosztTemp = min.concat(kosztTxt);
					kosztTxt = kosztTemp;
				}
				String tagTxt = tag.getText().toString();
				if (nazwaTxt.isEmpty() || koszt.getText().toString().isEmpty() || tagTxt.isEmpty()) {
					Toast msg = Toast.makeText(getBaseContext(), "Nie podano wszystkich danych.", Toast.LENGTH_SHORT);
			        msg.show();
				} else {
					DataHandler data = new DataHandler();
					double kosztDouble = Double.parseDouble(kosztTxt);
					Long tsLong = System.currentTimeMillis()/1000;
					String stworzony = tsLong.toString();
					data.dodaj(stworzony, nazwaTxt, kosztDouble, tagTxt);
					Log.i("Dodano transakcje", nazwaTxt);
					Intent intent = new Intent();
					intent.putExtra("stworzony", stworzony);
					intent.putExtra("nazwa", nazwaTxt);
					intent.putExtra("koszt", kosztDouble);
					setResult(2, intent);
					finish();
					overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
				}
		    }
		});
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
    		// case SimpleGestureFilter.SWIPE_RIGHT : break;
    		// case SimpleGestureFilter.SWIPE_LEFT : break;
    		// case SimpleGestureFilter.SWIPE_DOWN : break;
    		case SimpleGestureFilter.SWIPE_UP : super.onBackPressed();
    			overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    			break;      
      	}
     }
      
     @Override
     public void onDoubleTap() {
        
     }
	
}
