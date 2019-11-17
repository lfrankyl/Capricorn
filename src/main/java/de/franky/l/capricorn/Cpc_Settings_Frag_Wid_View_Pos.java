package de.franky.l.capricorn;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.util.Log;

public class Cpc_Settings_Frag_Wid_View_Pos extends PreferenceFragment   
{
    
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
		// Log.d("Cpc_SettingsFragment","On create");
        addPreferencesFromResource(R.xml.cpc_setting_frag_wid_view_pos);
		SetWidgetViewPosition(R.string.pref_View1_Key);
		SetWidgetViewPosition(R.string.pref_View2_Key);
		SetWidgetViewPosition(R.string.pref_View3_Key);
		SetWidgetViewPosition(R.string.pref_View4_Key);
		SetWidgetViewPosition(R.string.pref_View5_Key);
		SetWidgetViewPosition(R.string.pref_View6_Key);
		SetWidgetViewPosition(R.string.pref_View7_Key);
	}
	
	private ListPreference SetWidgetViewPosition(int PrefKey)
	{
		ListPreference LiPref = (ListPreference) findPreference(getString(PrefKey));
		String sCurrentSummText = Cpc_Utils.CurVal.sViewOptions[Integer.parseInt(LiPref.getValue())] ;
		LiPref.setSummary(sCurrentSummText);
		LiPref.setIcon(Cpc_Utils.CurVal.iRDrawable[Integer.parseInt(LiPref.getValue())]);
		LiPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() 
		{
		    @Override
		    public boolean onPreferenceChange(Preference preference, Object newValue) 
		    {
				String sNewSummText;
				int iNewValue = Integer.parseInt((String)newValue);
				sNewSummText = Cpc_Utils.CurVal.sViewOptions[iNewValue] ;
				Log.d("onPreferenceChange",sNewSummText);
		        preference.setSummary(sNewSummText);
		        preference.setIcon(Cpc_Utils.CurVal.iRDrawable[iNewValue]);
		        return true;
		    }
		}
		);
		return LiPref;
	}
	@Override
	public void onStop()
	{
		super.onStop();
		Cpc_Utils.CpcData.Update_iViews();					// Position der Elemente im Widget im globalen Arry iView aktualisieren
	}


} // End of class Cpc_Settings_Frag_Wid_View_Pos





