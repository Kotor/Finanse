package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.starter.SimpleGestureFilter.SimpleGestureListener;

public class DodanieTransakcji extends Activity implements SimpleGestureListener {
	private SimpleGestureFilter detector;
	private static final int CAMERA_REQUEST = 1888; 
	boolean wydatek;
	ParseFile zdjecie;
	ImageView aparat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dodanie_transakcji);
		detector = new SimpleGestureFilter(this,this);
				
		final EditText nazwa, koszt, tag;
		final TextView wydatekTV, przychodTV;
		Switch przelacznik;
		final Button dodaj;
		        
		nazwa = (EditText) findViewById(R.id.nazwa);
		koszt = (EditText) findViewById(R.id.koszt);
		tag = (EditText) findViewById(R.id.tag);
		wydatekTV = (TextView) findViewById(R.id.wydatek);
		przychodTV = (TextView) findViewById(R.id.przychod);
		aparat = (ImageView) findViewById(R.id.aparat);
		przelacznik = (Switch) findViewById(R.id.przelacznik);
		dodaj = (Button) findViewById(R.id.dodaj);
		        
		aparat.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		        startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});
		
		przelacznik.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					wydatek = true;
					wydatekTV.setTextColor(getBaseContext().getResources().getColor(R.color.disable_text));
					przychodTV.setTextColor(getBaseContext().getResources().getColor(R.color.text));
			    } else {
			    	wydatek = false;
			    	wydatekTV.setTextColor(getBaseContext().getResources().getColor(R.color.text));
					przychodTV.setTextColor(getBaseContext().getResources().getColor(R.color.disable_text));
			    }
			}
		});
		
		dodaj.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String nazwaTxt = nazwa.getText().toString();
				String kosztTxt = koszt.getText().toString();
				String tagTxt = tag.getText().toString();
				if (!wydatek) {
					String min = "-";
					String kosztTemp = min.concat(kosztTxt);
					kosztTxt = kosztTemp;					
				}
				if (tagTxt.isEmpty()) {
					tagTxt = "Inne";
				}
				if (nazwaTxt.isEmpty() || koszt.getText().toString().isEmpty() || tagTxt.isEmpty()) {
					Toast msg = Toast.makeText(getBaseContext(), "Nie podano wszystkich danych.", Toast.LENGTH_SHORT);
			        msg.show();
				} else {
					DataHandler data = new DataHandler();
					double kosztDouble = Double.parseDouble(kosztTxt);
					Long tsLong = System.currentTimeMillis()/1000;
					String stworzony = tsLong.toString();
					if (zdjecie != null) {
						data.dodaj(stworzony, nazwaTxt, kosztDouble, zdjecie, tagTxt);
					} else {
						data.dodaj(stworzony, nazwaTxt, kosztDouble, tagTxt);
					}
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
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) { 
        	aparat.setColorFilter(Color.GREEN);
            Bitmap zdj = (Bitmap) data.getExtras().get("data"); 
            //zdjecie = data.getExtras().get("data"); 
        }  
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
