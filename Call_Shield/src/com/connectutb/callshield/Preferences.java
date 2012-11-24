package com.connectutb.callshield;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class Preferences extends PreferenceActivity{
	/* Load the preferences */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

	}
    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preferences_headers, target);
    }
    
    /**
     * This fragment shows the preferences for the first header.
     */
    public static class Prefs1Fragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}