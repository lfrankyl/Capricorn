package de.franky.l.capricorn;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import static de.franky.l.capricorn.Cpc_Std_Data.pref_AlarmManagerShallStop_Key;
import static de.franky.l.capricorn.Cpc_Utils.CpcPref;

public class Cpc_AlarmManager extends BroadcastReceiver {
	 
	
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		MakeItManual (context);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void MakeItManual(Context context) 
	{
		if (!CpcPref.getBool(pref_AlarmManagerShallStop_Key, false)){

			if (!Cpc_Utils_Data_Val.JustGetTheValues(context))
			{
				Log.d("Cpc_AlarmManager","MakeItManual Problems with JustGetTheValues");
			}
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.capricorn_layout_small);
			//Log.d("Cpc_AlarmManager","Rufe HierPassierts fuer Small");
			Cpc_WidgetIntentReceiver.PrepareAllDisplayAttributes(context, remoteViews);
			Cpc_WidgetProvider_small.setOnClickPendingIntent4AllElements(context, remoteViews);

			RemoteViews remoteViewsMedium = new RemoteViews(context.getPackageName(),R.layout.capricorn_layout_medium);
			//Log.d("Cpc_AlarmManager","Rufe HierPassierts fuer Medium");
			Cpc_WidgetIntentReceiver.PrepareAllDisplayAttributes(context, remoteViewsMedium);
			Cpc_WidgetProvider_medium.setOnClickPendingIntent4AllElements(context, remoteViewsMedium);

			RemoteViews remoteViewsLarge = new RemoteViews(context.getPackageName(),R.layout.capricorn_layout_large);
			//Log.d("Cpc_AlarmManager","Rufe HierPassierts fuer Large");
			Cpc_WidgetIntentReceiver.PrepareAllDisplayAttributes(context, remoteViewsLarge);
			Cpc_WidgetProvider_large.setOnClickPendingIntent4AllElements(context, remoteViewsLarge);

			// Toast.makeText(context, "Fire", Toast.LENGTH_SHORT).show();
			//Log.d("Alarmmanager",intent.toString());
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			boolean bScreenIsOnAndInteractive;

			// Log.d("PrepareAllDisplayAttributes pm.isScreenOn",String.valueOf(pm.isScreenOn()));
			if (Build.VERSION.SDK_INT >= (Build.VERSION_CODES.KITKAT_WATCH))
				bScreenIsOnAndInteractive = pm.isInteractive();
			else
				bScreenIsOnAndInteractive = pm.isScreenOn();

			if (bScreenIsOnAndInteractive)
			{
			    // Log.d("Alarmmanager", "onReceive");
				Cpc_Utils.StartWidgetUpdates();
			}
			else
			{
				Cpc_Utils.CurVal.bIsUserPresentnotified = false;
			}
		}
   }


}