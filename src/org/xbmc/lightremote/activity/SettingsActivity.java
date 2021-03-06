package org.xbmc.lightremote.activity;

import java.util.List;

import org.xbmc.lightremote.Application;
import org.xbmc.lightremote.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;

public class SettingsActivity extends PreferenceActivity {
	protected SharedPreferences mSharedPrefs;

		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Add a button to the header list.
	        if (hasHeaders()) {
	            Button button = new Button(this);
	            button.setText("Some action");
	            setListFooter(button);
	        }
	        
			mSharedPrefs = getSharedPreferences(Application.APP_NAME, 0); 
	    }

	    /**
	     * Populate the activity with the top-level headers.
	     */
	    @Override
	    public void onBuildHeaders(List<Header> target) {
	        loadHeadersFromResource(R.xml.preference_headers, target);
	    }

	    /**
	     * This fragment shows the preferences for the first header.
	     */
	    public static class Prefs1Fragment extends PreferenceFragment implements OnPreferenceChangeListener {
	        @Override
	        public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);
	            
	            // Make sure default values are applied.  In a real app, you would
	            // want this in a shared function that is used to retrieve the
	            // SharedPreferences wherever they are needed.
	            //PreferenceManager.setDefaultValues(getActivity(), R.xml.advanced_preferences, false);

	            // Load the preferences from an XML resource
	            addPreferencesFromResource(R.xml.fragmented_preferences);
	            
	            findPreference("password").setOnPreferenceChangeListener(this);
	        }

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if (preference.getKey().equals("password")) {
					Application.getContext().getSharedPreferences(Application.APP_NAME, 0).edit().putString("password", (String)newValue).commit();
					return true;
				}
				return false;
			}
	    }

	    /**
	     * This fragment contains a second-level set of preference that you
	     * can get to by tapping an item in the first preferences fragment.
	     */
	    public static class Prefs1FragmentInner extends PreferenceFragment {
	        @Override
	        public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);

	            // Can retrieve arguments from preference XML.
	            Log.i("args", "Arguments: " + getArguments());

	            // Load the preferences from an XML resource
	            addPreferencesFromResource(R.xml.fragmented_preferences_inner);
	        }
	    }

	    /**
	     * This fragment shows the preferences for the second header.
	     */
	    public static class Prefs2Fragment extends PreferenceFragment {
	        @Override
	        public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);

	            // Can retrieve arguments from headers XML.
	            Log.i("args", "Arguments: " + getArguments());

	            // Load the preferences from an XML resource
	            addPreferencesFromResource(R.xml.preference_dependencies);
	        }
	    }
	}