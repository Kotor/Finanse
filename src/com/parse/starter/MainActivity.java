package com.parse.starter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.contextualundo.ContextualUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.contextualundo.ContextualUndoAdapter.DeleteItemCallback;
import com.parse.ParseAnalytics;
import com.parse.starter.SimpleGestureFilter.SimpleGestureListener;

public class MainActivity extends Activity implements OnItemClickListener, SimpleGestureListener, DeleteItemCallback {
	private SimpleGestureFilter detector;
	DataHandler data = new DataHandler();
	public ArrayList<Transakcja> transakcje = data.pobierzListe();
	ListView list;
	ListAdapter adapter;
	ImageView imageView;
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		detector = new SimpleGestureFilter(this,this);
		list = (ListView) findViewById(R.id.list);
        list.setClickable(true);
		ParseAnalytics.trackAppOpened(getIntent());
		
		adapter = new ListAdapter(this, transakcje);
        list.setOnItemClickListener(this);
       
        ContextualUndoAdapter adapterCUA = new ContextualUndoAdapter(adapter, R.layout.undo_row, R.id.undo_row_undobutton, 3000, this);
        adapterCUA.setAbsListView(list);
        list.setAdapter(adapterCUA);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		byte[] zdjecie = transakcje.get(arg2).getZdjecie();
		Bitmap zdjecieBMP = BitmapFactory.decodeByteArray(zdjecie, 0, zdjecie.length);
		
		if (zdjecie.length < 2) {
			Toast.makeText(getApplicationContext(), "Brak zdjêcia", Toast.LENGTH_SHORT).show();
		} else {
			AlertDialog.Builder alertadd = new AlertDialog.Builder(MainActivity.this);
			LayoutInflater factory = LayoutInflater.from(MainActivity.this);
			final View view = factory.inflate(R.layout.sample, null);
			this.imageView = (ImageView) view.findViewById(R.id.zdjecie);
			imageView.setImageBitmap(zdjecieBMP);
			alertadd.setView(view);
			alertadd.show();
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
    		//case SimpleGestureFilter.SWIPE_RIGHT : break;
    		//case SimpleGestureFilter.SWIPE_LEFT : break;
    		case SimpleGestureFilter.SWIPE_DOWN : Intent intent = new Intent(getBaseContext(), DodanieTransakcji.class);
    			startActivityForResult(intent, 1);
    			overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
                break;
    		//case SimpleGestureFilter.SWIPE_UP : break;      
      	}       
     }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent in) {    
    	if(requestCode == 1 && resultCode == 2) {
    		Transakcja tr = new Transakcja(in.getStringExtra("stworzony"), in.getStringExtra("nazwa"), 
    				in.getDoubleExtra("koszt", 0), in.getByteArrayExtra("zdjecie"), in.getStringExtra("tag"));
    		transakcje.add(tr);
    		
    	}
    	adapter.notifyDataSetChanged();
    } 
      
     @Override
     public void onDoubleTap() {
        
     }

	@Override
	public void deleteItem(int position) {
		String stworzony = transakcje.get(position).getStworzony();
		data.usun(stworzony);
		transakcje.remove(position);
		adapter.notifyDataSetChanged();
	}	
}
