package com.parse.starter;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.contextualundo.ContextualUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.contextualundo.ContextualUndoAdapter.DeleteItemCallback;
import com.parse.ParseAnalytics;

public class MainActivity extends Activity implements OnItemClickListener, DeleteItemCallback {
	DataHandler data = new DataHandler();
	public ArrayList<Transakcja> transakcje = data.pobierzListe();
	ListView list;
	ScrollView scroll;
	ListAdapter adapter;
	ImageView imageView;
	protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	boolean jestWydatek;
	ImageView aparat;
	String filePath = "";
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		list = (ListView) findViewById(R.id.list);
        list.setClickable(true);
        scroll = (ScrollView) findViewById(R.id.scrollView);
        DisplayMetrics metrics = getBaseContext().getResources().getDisplayMetrics();
        final int height = metrics.heightPixels;
        RelativeLayout relDodaj = (RelativeLayout) findViewById(R.id.relativeDodaj);
        RelativeLayout relLista = (RelativeLayout) findViewById(R.id.relativeLista);
        relDodaj.getLayoutParams().height = height;
        relLista.getLayoutParams().height = height;
        
		ParseAnalytics.trackAppOpened(getIntent());
		
		adapter = new ListAdapter(this, transakcje);
        list.setOnItemClickListener(this);
        ContextualUndoAdapter adapterCUA = new ContextualUndoAdapter(adapter, R.layout.undo_row, R.id.undo_row_undobutton, 3000, this);
        adapterCUA.setAbsListView(list);
        list.setAdapter(adapterCUA);
        
        final EditText nazwa, koszt, tag;
		final Button dodaj, wydatek, przychod;
		        
		nazwa = (EditText) findViewById(R.id.nazwa);
		koszt = (EditText) findViewById(R.id.koszt);
		tag = (EditText) findViewById(R.id.tag);
		aparat = (ImageView) findViewById(R.id.aparat);
		wydatek = (Button) findViewById(R.id.wydatek);
		przychod = (Button) findViewById(R.id.przychod);
		dodaj = (Button) findViewById(R.id.dodaj);
		
		scroll.post(new Runnable() {

	        @Override
	        public void run() {
	            scroll.scrollTo(0, height);
	        }
	    });
		     
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String zdjeciePath = transakcje.get(arg2).getZdjecie();				
		
		
		if (zdjeciePath.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Brak zdjêcia", Toast.LENGTH_SHORT).show();
		} else {
			File image = new File(Environment.getExternalStorageDirectory(), zdjeciePath);
			Uri uriSavedImage = Uri.fromFile(image);
			String sPath = uriSavedImage.toString().substring(8);
			Bitmap bmp = BitmapFactory.decodeFile(sPath);			
			AlertDialog.Builder alertadd = new AlertDialog.Builder(MainActivity.this);
			LayoutInflater factory = LayoutInflater.from(MainActivity.this);
			final View view = factory.inflate(R.layout.sample, null);
			this.imageView = (ImageView) view.findViewById(R.id.zdjecie);		
			imageView.setImageBitmap(bmp);	
			imageView.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					Log.i("klik", "yes");
				}
			});
			alertadd.setView(view);
			
			alertadd.show();
			
		}
	}
    
    protected void onActivityResult(int requestCode, int resultCode, Intent in) {    
    	if(requestCode == 1 && resultCode == 2) {
    		Transakcja tr = new Transakcja(in.getStringExtra("stworzony"), in.getStringExtra("nazwa"), 
    				in.getDoubleExtra("koszt", 0), in.getStringExtra("zdjecie"), in.getStringExtra("tag"));
    		transakcje.add(tr);
    		
    	}
    	adapter.notifyDataSetChanged();
    }

     @Override
     public void deleteItem(int position) {
    	 String stworzony = transakcje.get(position).getStworzony();
    	 data.usun(stworzony);
    	 transakcje.remove(position);
    	 adapter.notifyDataSetChanged();
	}	
}
