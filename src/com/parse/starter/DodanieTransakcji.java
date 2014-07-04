package com.parse.starter;


import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.starter.SimpleGestureFilter.SimpleGestureListener;

public class DodanieTransakcji extends Activity implements SimpleGestureListener {
	protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	private SimpleGestureFilter detector;
	boolean jestWydatek;
	ImageView aparat;
	String filePath = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dodanie_transakcji);
		detector = new SimpleGestureFilter(this,this);
				
		final EditText nazwa, koszt, tag;
		final Button dodaj, wydatek, przychod;
		        
		nazwa = (EditText) findViewById(R.id.nazwa);
		koszt = (EditText) findViewById(R.id.koszt);
		tag = (EditText) findViewById(R.id.tag);
		aparat = (ImageView) findViewById(R.id.aparat);
		wydatek = (Button) findViewById(R.id.wydatek);
		przychod = (Button) findViewById(R.id.przychod);
		dodaj = (Button) findViewById(R.id.dodaj);
		     
		aparat.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				Long tsLong = System.currentTimeMillis()/1000;
				String timeStamp = tsLong.toString();
				
				//folder stuff
				File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Finanse");
				imagesFolder.mkdirs();

				filePath = "/Finanse/QR_" + timeStamp + ".png" ;
				File image = new File(imagesFolder, "QR_" + timeStamp + ".png");
				Uri uriSavedImage = Uri.fromFile(image);
				Log.i("sciezka zapis", uriSavedImage.toString());
				
				imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
				startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
		
		wydatek.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				wydatek.setTextColor(getResources().getColor(R.color.wydatek));
				przychod.setTextColor(getResources().getColor(R.color.disable_text));
				jestWydatek = false;
		    }
		});
		
		przychod.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				przychod.setTextColor(getResources().getColor(R.color.przychod));
				wydatek.setTextColor(getResources().getColor(R.color.disable_text));
				jestWydatek = true;
			}
		});
		
		dodaj.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String nazwaTxt = nazwa.getText().toString();
				String kosztTxt = koszt.getText().toString();
				String tagTxt = tag.getText().toString();
				if (!jestWydatek) {
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
					data.dodaj(stworzony, nazwaTxt, kosztDouble, filePath, tagTxt);
					Intent intent = new Intent();
					intent.putExtra("stworzony", stworzony);
					intent.putExtra("nazwa", nazwaTxt);
					intent.putExtra("koszt", kosztDouble);
					intent.putExtra("zdjecie", filePath);
					intent.putExtra("tag", tagTxt);
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
