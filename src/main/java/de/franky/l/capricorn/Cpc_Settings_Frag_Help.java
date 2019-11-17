package de.franky.l.capricorn;


import android.os.Bundle;
import android.preference.PreferenceFragment;

public class Cpc_Settings_Frag_Help extends PreferenceFragment   
{
    
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
		// Log.d("Cpc_SettingsFragment","On create");
        addPreferencesFromResource(R.xml.cpc_setting_frag_help);
	}
}





