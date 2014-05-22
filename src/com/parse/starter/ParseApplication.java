package com.parse.starter;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.enableLocalDatastore(this);
		Parse.initialize(this, "8kc9yBbdSm0gGn7gowvZJ1Ri8pKnJOpwReIXiwgU", "C1lRm3qvtpWSN5NCQXGMIl0eVAo6APHH52K9Jbqg");

		//ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		//defaultACL.setPublicReadAccess(true);
		
		//ParseACL.setDefaultACL(defaultACL, true);
	}

}
