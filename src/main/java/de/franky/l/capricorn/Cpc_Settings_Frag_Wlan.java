package de.franky.l.capricorn;


import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.GregorianCalendar;

import static de.franky.l.capricorn.Cpc_Utils.CpcPref;

public class Cpc_Settings_Frag_Wlan extends PreferenceFragment   
{

	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
		// Log.d("Cpc_SettingsFragment","On create");
        addPreferencesFromResource(R.xml.cpc_setting_frag_wlan);
        
		String sCurrentSummText;
     
		Preference myNumberPickerWLAN     = findPreference(getString(R.string.pref_NuPiRef_Wlan_Key));    // muss hier definiert werden sonst kennt die Listboxpreference den nicht
		 
		 // Einstellungen fuer Numberpicker wlan Referenzwert
		 // Log.d("NumberPickerWLAN",	 myNumberPickerWLAN.getKey());
         long lCurrentSummText = CpcPref.getLong( R.string.pref_NuPiRef_Wlan_Key, R.string.pref_wlanRefVal_Default);
         sCurrentSummText = Cpc_Utils.MakeOutString( lCurrentSummText);
		 sCurrentSummText = sCurrentSummText + " " + Cpc_Utils.CalcUnit(lCurrentSummText);
 		 EditTextPreference myPrefWlanDate = (EditTextPreference) findPreference(getString(R.string.pref_wlanStartDate_Key));
 		 String sDate = CpcPref.getString(getString(R.string.pref_wlanStartDate_Key), getString(R.string.DefaultText)) ;
 		 myPrefWlanDate.setSummary(sDate);
		 myNumberPickerWLAN.setSummary(sCurrentSummText);
		 myNumberPickerWLAN.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() 
	        {
	
	            @Override
	            public boolean onPreferenceChange(Preference preference, Object newValue) 
	            {
	            	String sNewSummText;
	                long lMessung = TrafficStats.getTotalTxBytes() + TrafficStats.getTotalRxBytes() - TrafficStats.getMobileRxBytes() - TrafficStats.getMobileTxBytes();
              	    GregorianCalendar myCal = new GregorianCalendar();
            		EditTextPreference myPrefWlanDate = (EditTextPreference) findPreference(getString(R.string.pref_wlanStartDate_Key));
                    String sDate = String.valueOf(android.text.format.DateFormat.format("dd.MM.yyyy", myCal.getTime()));
					CpcPref.putLong(getString(R.string.pref_wlanOffset_Key), lMessung);
					CpcPref.putString(getString(R.string.pref_wlanStartDate_Key), sDate);
	                long lWLANNumberPicker = CpcPref.getLong(R.string.pref_NuPiRef_Wlan_Key, R.string.pref_wlanRefVal_Default);
					sNewSummText = String.valueOf(Cpc_Utils.MakeOutString(lWLANNumberPicker)) + " " + Cpc_Utils.CalcUnit(lWLANNumberPicker);
			        preference.setSummary(sNewSummText);
			        myPrefWlanDate.setSummary(sDate);
	                return true;
	            }
	        }
		 );
	}
	
} // End of class Cpc_SettingsFragment





