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
	ArrayList<String> foos = new ArrayList<String>();
	
	public void dodaj(String nazwa, double koszt, ParseFile rachunek, String tag) {
		ParseObject transakcja = new ParseObject("Transakcja");
		transakcja.put("nazwa", nazwa);
		transakcja.put("koszt", koszt);
		transakcja.put("rachunek", rachunek);
		transakcja.put("tag", tag);
		Log.i("ObjectID", transakcja.getObjectId());
		transakcja.pinInBackground(null);
	}
	
	public void dodaj(String nazwa, double koszt, String tag) {
		final ParseObject transakcja = new ParseObject("Transakcja");
		Long tsLong = System.currentTimeMillis()/1000;
		transakcja.put("stworzony", tsLong.toString());
		transakcja.put("nazwa", nazwa);
		transakcja.put("koszt", koszt);
		transakcja.put("tag", tag);
		transakcja.pinInBackground(null);
		Log.i("Dodano", "Produkt: " + nazwa + " o: " + tsLong.toString());
	}
	
	public void usun(String objectID) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Transakcja");
		query.whereEqualTo("objectId", objectID);
		query.fromLocalDatastore();
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> listaTransakcji,
		                     ParseException e) {
		        if (e == null) {
		        	listaTransakcji.get(0).unpinInBackground(null);
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
	
	public List<ParseObject> pobierzListe() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Transakcja");
		query.fromLocalDatastore();
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> listaTransakcji,
		                     ParseException e) {
		        if (e == null) {
		        	
		        	for (int i = 0; i < listaTransakcji.size(); i++) {
		        	       String c = listaTransakcji.get(i).getString("nazwa");
		        	       foos.add(c); 
		        	}
		        	
		        	// TODO obserwator
		        	// obserwator.odswiezListe(listaTransakcji);
		        	/*for (ParseObject transakcja : listaTransakcji) {
		        		Log.i("stworzony", transakcja.getString("stworzony"));
		        		Log.i("nazwa", transakcja.getString("nazwa"));
		        		Log.i("koszt", ""+transakcja.getDouble("koszt"));
		        		//Log.i("rachunek", transakcja.getString("rachunek"));
		        		Log.i("tag", transakcja.getString("tag"));
		        	} */
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }
		}
		);
		Log.i("Rozmiar przed returnem", Integer.toString(foos.size()));
		return null;
	}
}
