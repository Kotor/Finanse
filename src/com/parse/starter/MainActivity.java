package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;

import com.parse.ParseAnalytics;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ParseAnalytics.trackAppOpened(getIntent());
		
		DataHandler data = new DataHandler();
		//data.dodaj("Buty", -25.55, "odzie¿");
		//data.dodaj("Wyp³ata", 825.55, "Z pracy");
		//data.dodaj("Tankowanie BP", -105.55, "Paliwo");
		data.pobierzListe();
		
		
	}
}
