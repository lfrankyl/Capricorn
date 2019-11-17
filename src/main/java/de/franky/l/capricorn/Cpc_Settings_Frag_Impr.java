package de.franky.l.capricorn;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Cpc_Settings_Frag_Impr extends PreferenceFragment   
{
    // Todo: Opensource Lizenzen schreiben und anzeigen
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
		// Log.d("cpc_setting_frag_impr","On create");
        addPreferencesFromResource(R.xml.cpc_setting_frag_impr);
        String sOsInfo;
		Context myContext = Cpc_Application.getContext();
		long lLastUpdate;
		String sLastUpdate = "11.11.11 ; 12:12";
		String sInfo="x.y";
		if (myContext != null)
		{
			try
			{
				sInfo = myContext.getPackageManager().getPackageInfo(myContext.getPackageName(), 0).versionName;
//			    ApplicationInfo ai = myContext.getPackageManager().getApplicationInfo(myContext.getPackageName(), 0);
//			    ZipFile zf = new ZipFile(ai.sourceDir);
//			    ZipEntry ze = zf.getEntry("classes.dex");
//			    lLastUpdate = ze.getTime();
//			    sLastUpdate = new SimpleDateFormat("dd.MM.yyyy' ;   'HH:mm",Locale.getDefault()).format(lLastUpdate);
				sLastUpdate = new SimpleDateFormat("dd.MM.yyyy' ;   'HH:mm",Locale.getDefault()).format(BuildConfig.TIMESTAMP);
//			    zf.close();
			}
			catch (Exception e)
			{
				Toast.makeText(myContext, "Exception @ Capricorn_SettingsFagment", Toast.LENGTH_SHORT).show();
			}
			
		}
		sOsInfo = "  " + sInfo + " ;   " + sLastUpdate;
		EditTextPreference myPref = (EditTextPreference) findPreference(getString(R.string.pref_Info_Key));
		String sCurrentSummText = getString(R.string.pref_Info_Title) + sOsInfo ;
		myPref.setSummary(sCurrentSummText);

		// Log.d("sOsInfo",sNewSummText);
	}
	
} // End of class Cpc_SettingsFragment





