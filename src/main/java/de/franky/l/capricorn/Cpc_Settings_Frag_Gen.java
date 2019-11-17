package de.franky.l.capricorn;


import android.net.TrafficStats;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;

import java.util.Locale;

import static de.franky.l.capricorn.Cpc_Utils.CpcPref;

public class Cpc_Settings_Frag_Gen extends PreferenceFragment   
{
    private String sLocalSetting;
	String[] sBckgrdOptions = Cpc_Application.getContext().getResources().getStringArray(R.array.pref_Background);
	String[] sWdgtFontCol = Cpc_Application.getContext().getResources().getStringArray(R.array.pref_WidgetFontColor);


	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
		// Log.d("Cpc_SettingsFragment","On create");
        addPreferencesFromResource(R.xml.cpc_setting_frag_gen);
        
		String sCurrentSummText;
		// Landeseinstellung des Geraetes abrufen
		sLocalSetting = Locale.getDefault().getDisplayName();
     
		// Einstellungen fuer Aktualisierungsintervall
		ListPreference myPrefIntervall = (ListPreference) findPreference(getString(R.string.pref_Intervall_Key));
		sCurrentSummText = Cpc_Utils.MakeOutString(Double.parseDouble(myPrefIntervall.getValue())/1000) + getString(R.string.blankChr) + getString(R.string.pref_Intervall_Unit);
		myPrefIntervall.setSummary(sCurrentSummText);
		myPrefIntervall.setOnPreferenceChangeListener(new OnPreferenceChangeListener() 
		{
		    @Override
		    public boolean onPreferenceChange(Preference preference, Object newValue) 
		    {
				String sNewSummText;
				// Log.d("onPreferenceChange",getString(R.string.pref_Key_Intervall));
				sNewSummText = Cpc_Utils.MakeOutString(Double.parseDouble((String) newValue)/1000) + getString(R.string.blankChr) +getString(R.string.pref_Intervall_Unit);
		        preference.setSummary(sNewSummText);
		        return true;
		    }
		}
		);
		
		// Einstellungen fuer Textgroesse Widget
		ListPreference myPrefTextSize = (ListPreference) findPreference(getString(R.string.pref_TextSize_Key));
		sCurrentSummText = Cpc_Utils.MakeOutString(Integer.parseInt(myPrefTextSize.getValue())) + getString(R.string.blankChr) + getString(R.string.pref_TextSize_Unit);
		myPrefTextSize.setSummary(sCurrentSummText);
		myPrefTextSize.setOnPreferenceChangeListener(new OnPreferenceChangeListener() 
		{
		    @Override
		    public boolean onPreferenceChange(Preference preference, Object newValue) 
		    {
				String sNewSummText;
				// Log.d("onPreferenceChange",getString(R.string.pref_Key_Intervall));
				sNewSummText = Cpc_Utils.MakeOutString(Integer.parseInt((String) newValue)) + getString(R.string.blankChr) + getString(R.string.pref_TextSize_Unit);
		        preference.setSummary(sNewSummText);
		        return true;
		    }
		}
		);
		// Einstellungen fuer Textgroesse App
		ListPreference myPrefAppTextSize = (ListPreference) findPreference(getString(R.string.pref_TextSizeApp_Key));
		sCurrentSummText = Cpc_Utils.MakeOutString(Integer.parseInt(myPrefAppTextSize.getValue())) + getString(R.string.blankChr) + getString(R.string.pref_TextSize_Unit);
		myPrefAppTextSize.setSummary(sCurrentSummText);
		myPrefAppTextSize.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
			 {
				 @Override
				 public boolean onPreferenceChange(Preference preference, Object newValue)
				 {
					 String sNewSummText;
					 // Log.d("onPreferenceChange",getString(R.string.pref_Key_Intervall));
					 sNewSummText = Cpc_Utils.MakeOutString(Integer.parseInt((String) newValue)) + getString(R.string.blankChr) + getString(R.string.pref_TextSize_Unit);
					 preference.setSummary(sNewSummText);
					 return true;
				 }
			 }
		);

		// Einstellungen fuer Hintergrund
		ListPreference myPrefBackground = (ListPreference) findPreference(getString(R.string.pref_Background_Key));
		int iCurVal = Integer.parseInt(myPrefBackground.getValue());
		if (iCurVal >= sBckgrdOptions.length)
		{
			iCurVal = Integer.parseInt(Cpc_Application.getContext().getResources().getString(R.string.pref_Background_Default));
		}
		sCurrentSummText = sBckgrdOptions[iCurVal];

		myPrefBackground.setSummary(sCurrentSummText);
		myPrefBackground.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
														{
															@Override
															public boolean onPreferenceChange(Preference preference, Object newValue)
															{
																String sNewSummText;
																// Log.d("onPreferenceChange",getString(R.string.pref_Key_Intervall));
																sNewSummText = sBckgrdOptions[Integer.parseInt((String) newValue)];
																preference.setSummary(sNewSummText);
																return true;
															}
														}
		);

		// Einstellungen fuer Widget Schriftfarbe
		ListPreference myPrefWidgetFontColor = (ListPreference) findPreference(getString(R.string.pref_WdgtFontcolor_Key));
		sCurrentSummText = sWdgtFontCol[Integer.parseInt(myPrefWidgetFontColor.getValue())];
		myPrefWidgetFontColor.setSummary(sCurrentSummText);
		myPrefWidgetFontColor.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
													   {
														   @Override
														   public boolean onPreferenceChange(Preference preference, Object newValue)
														   {
															   String sNewSummText;
															   // Log.d("onPreferenceChange",getString(R.string.pref_Key_Intervall));
															   sNewSummText = sWdgtFontCol[Integer.parseInt((String) newValue)];
															   preference.setSummary(sNewSummText);
															   return true;
														   }
													   }
		);

		// Einstellungen fuer Numberpicker Transparenz des Widgethintergrundds
		Preference myNumberPickerTransp     = findPreference(getString(R.string.pref_NuPi_BckgrdTransp_Key));
		//Log.d("myNumberPickerTransp",	 myNumberPickerTransp.getKey());
		long lCurrentSummText = CpcPref.getLong(R.string.pref_NuPi_BckgrdTransp_Key,R.string.pref_BckgrdTransp_Default);
		sCurrentSummText = Cpc_Utils.MakeOutString(lCurrentSummText)+" " + Cpc_Application.getContext().getResources().getString(R.string.pref_BckgrdTransp_Unit);
		myNumberPickerTransp.setSummary(sCurrentSummText);
		myNumberPickerTransp.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener()
														  {

															  @Override
															  public boolean onPreferenceChange(Preference preference, Object newValue)
															  {
																  String sNewSummText;
																  sNewSummText =  newValue + Cpc_Application.getContext().getResources().getString(R.string.pref_BckgrdTransp_Unit);
																  preference.setSummary(sNewSummText);
																  return true;
															  }
														  }
		);
	}
	
	@Override
	public void onStart ()
	{
		super.onStart();
		// Log.d("onStart","onStart");
		String sNewSummText;
		ListPreference myPrefLang = (ListPreference) findPreference(getString(R.string.pref_Language_Key));
		sNewSummText = getString(R.string.pref_Language_Sum) + " " +sLocalSetting ;
		
		myPrefLang.setSummary(sNewSummText);
	}
	
	
} // End of class Cpc_SettingsFragment





