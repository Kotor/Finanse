package com.parse.starter;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class DataHandler {	
	public DataHandler(){
		
	}
	
	public void dodaj(String stworzony, String nazwa, double koszt, String zdjecie, String tag) {
		final ParseObject transakcja = new ParseObject("Transakcja");
		transakcja.put("stworzony", stworzony);
		transakcja.put("nazwa", nazwa);
		transakcja.put("koszt", koszt);
		transakcja.put("zdjecie", zdjecie);
		transakcja.put("tag", tag);
		try {
			transakcja.pin();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void usun(String stworzony) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Transakcja");
		query.whereEqualTo("stworzony", stworzony);
		query.fromLocalDatastore();
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> listaTransakcji,
		                     ParseException e) {
		        if (e == null) {
		        	try {
						listaTransakcji.get(0).unpin();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}		        	
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		        
		    }
		});
	}
	
	public void edytuj(String objectID, final String nazwa, final double koszt, final ParseFile rachunek, final String tag) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Transakcja");
		query.whereEqualTo("objectId", objectID);
		query.fromLocalDatastore();
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> listaTransakcji,
		                     ParseException e) {
		        if (e == null) {
		        	ParseObject transakcja = listaTransakcji.get(0);
		        	transakcja.put("nazwa", nazwa);
		        	transakcja.put("koszt", koszt);
		        	transakcja.put("rachunek", rachunek);
		        	transakcja.put("tag", tag);
		        	transakcja.saveInBackground();
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }
		});
	}
	
	public ArrayList<Transakcja> pobierzListe() {
		final ArrayList<Transakcja> transakcje = new ArrayList<Transakcja>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Transakcja");
		query.fromLocalDatastore();
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> listaTransakcji, ParseException e) {
		    	if (e == null) {
		    	    for (int i = 0; i < listaTransakcji.size(); i++) {
		    	    	Transakcja c = new Transakcja(
		    	    			listaTransakcji.get(i).getString("stworzony"), 
		    	    			listaTransakcji.get(i).getString("nazwa"), 
		    	    			listaTransakcji.get(i).getDouble("koszt"),
		    	    			listaTransakcji.get(i).getString("zdjecie"),
		    	    			listaTransakcji.get(i).getString("tag"));
		    	    	transakcje.add(c); 
		    	    }
		    	} else {
		    		Log.e("B³¹d", "Error: " + e.getMessage());
		    	}
		    } 
		});
		return transakcje;	
	}
}


