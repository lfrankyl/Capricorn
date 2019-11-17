package de.franky.l.capricorn;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import java.util.GregorianCalendar;

import static de.franky.l.capricorn.Cpc_Utils.CpcPref;

public class Cpc_Application extends Application
{
	private static Context sContext;

	@Override
    public void onCreate() 
	{
        super.onCreate();
        sContext = getApplicationContext();
        if (CpcPref.getBool(R.string.cpc_First_Run ,true)){
            Log.d("Cpc_Application", "First run = true");
            GregorianCalendar myCal = new GregorianCalendar();
            String sDate = String.valueOf(android.text.format.DateFormat.format("dd.MM.yyyy", myCal.getTime()));
            CpcPref.putString(getString(R.string.pref_wlanStartDate_Key), sDate);  // store wlan start date as first start of the app as initial value
            CpcPref.putBool(Cpc_Std_Data.pref_FirstSetup_Key, false);  // Remember if settings were opened
            CpcPref.putBool(Cpc_Std_Data.pref_OREO_Message_Key, false);  // Remember if OREO message was already shown

            CpcPref.putBool(getString(R.string.cpc_First_Run),false);
        }
        Log.i("Cpc_Application", Cpc_Std_Data.pref_FirstSetup_Key + " -> false");
        Cpc_Utils.CpcData.Update_iViews();
        Cpc_WidgetProviderHelper.RegisterIntentReceiver();
    }
	public static Context getContext()
	{
        return sContext;
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) 
	{
        super.onConfigurationChanged(newConfig);

        // create intent to update all instances of the widget
        // Intent intent = new Intent(AlarmManager., null, this, Cpc_AlarmManager.class);
        Log.d("Cpc_Application", "onConfigurationChanged");
        Cpc_WidgetProvider_small.SetDisplayParametersFromOptions(this);
        Cpc_WidgetProvider_small.SetTextAfterOrientationChange(this);
        Cpc_WidgetProvider_medium.SetDisplayParametersFromOptions(this);
        Cpc_WidgetProvider_medium.SetTextAfterOrientationChange(this);
        Cpc_WidgetProvider_large.SetDisplayParametersFromOptions(this);
        Cpc_WidgetProvider_large.SetTextAfterOrientationChange(this);
    }

}
