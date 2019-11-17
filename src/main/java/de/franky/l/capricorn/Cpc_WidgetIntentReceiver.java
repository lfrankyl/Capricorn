package de.franky.l.capricorn;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import static de.franky.l.capricorn.Cpc_Utils.CpcPref;


public class Cpc_WidgetIntentReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if(intent.getAction().equals(Intent.ACTION_SHUTDOWN))
		{
			Cpc_Utils.ActionsWhenShutDown(context);
			Toast.makeText(context, "Capricorn: Device wird runtergefahren", Toast.LENGTH_SHORT).show();
			Log.d("WidgetIntentReceiver","Device wird runtergefahren");
		}
		if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE))
		{
			if (!Cpc_Utils.CurVal.bIsUserPresentnotified) {
				Cpc_AlarmManager.MakeItManual(context);
				Cpc_Utils.CurVal.bIsUserPresentnotified = true;
			}
			else {
				Cpc_WidgetProviderHelper.Cpc_StartSettings(context);
			}
		}
		if (intent.getAction().equals(Intent.ACTION_USER_PRESENT))
		{
			// Cpc_WidgetProviderHelper.UnRegisterIntentReceiver();
			Cpc_Utils.CurVal.bIsUserPresentnotified = true;
			Cpc_AlarmManager.MakeItManual(context);
		}
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		{
            CpcPref.putLong(context.getString(R.string.pref_MobileMessung_Key), 0);
			CpcPref.putLong(context.getString(R.string.pref_wlanOffset_Key),   0);
			Toast.makeText(context, "Capricorn: Boot completed", Toast.LENGTH_SHORT).show();
		}
		
		Log.i("WidgetIntentReceiver","OnReceive " + intent.getAction());
	}



	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void PrepareAllDisplayAttributes(Context context,RemoteViews remoteViews)  // frueher "Hier Passierts"
	{
		
		// Log.d("FrankyWidgetIntentReceiver","PrepareAllDisplayAttributes");

		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		boolean bScreenIsOnAndInteractive;
  	
		// Log.d("PrepareAllDisplayAttributes pm.isScreenOn",String.valueOf(pm.isScreenOn()));
		if (Build.VERSION.SDK_INT >= (Build.VERSION_CODES.KITKAT_WATCH))
			bScreenIsOnAndInteractive = pm.isInteractive();
		else
			bScreenIsOnAndInteractive = pm.isScreenOn();
		
		if (bScreenIsOnAndInteractive)
		{
			//Log.d("PrepareAllDisplayAttributes ","Screen ist on");
			
			Cpc_WidgetProvider_small.SetDisplayParametersFromOptions(context);			// Set Background & Bitmaps
			Cpc_WidgetProvider_large.SetDisplayParametersFromOptions(context);
			Cpc_WidgetProvider_medium.SetDisplayParametersFromOptions(context);

			Cpc_WidgetProviderHelper.setTextViewText(remoteViews,3);				// Hier mit iWhich = 3 aufrufen damit alle Widgets aktualisiert werden

		}
		else  // Screen is off
		{
			//Log.d("PrepareAllDisplayAttributes pm.isScreenOn",String.valueOf(bScreenIsOnAndInteractive));
			KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
			//Log.d("PrepareAllDisplayAttributes myKM.inKeyguardRestrictedInputMode()",String.valueOf(myKM.inKeyguardRestrictedInputMode()));
			//Log.d("myKM",String.valueOf(myKM.inKeyguardRestrictedInputMode()));
			if(myKM.inKeyguardRestrictedInputMode())				// Wenn Bildschirm locked ist
			{
				Cpc_Utils.StopAlarmmanager(context);				// dann Aktualisierung der Anzeige einstellen
				Cpc_Utils.CurVal.bIsUserPresentnotified = false;
			} 
			// wenn  nicht locked
			// weiter gehts
		}
	}
	
}	
