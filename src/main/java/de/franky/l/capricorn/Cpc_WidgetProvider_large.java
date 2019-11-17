package de.franky.l.capricorn;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;


public class Cpc_WidgetProvider_large extends AppWidgetProvider
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		super.onReceive(context, intent);
//		if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE))
//		{
//			if (!Cpc_Utils.CurVal.bIsUserPresentnotified) {
				Cpc_AlarmManager.MakeItManual(context);
				Cpc_Utils.CurVal.bIsUserPresentnotified = true;
//			}
//			else {
//				Cpc_WidgetProviderHelper.Cpc_StartSettings(context);
//			}
//		}
		Log.d("WidgetProvider large","onReceive " + intent.getAction());
	}

	@Override
	public void onDisabled(Context context)
	{
		super.onDisabled(context);
		Log.i("WidgetProvider large","onDisabled");
		if (Cpc_Utils.Cpc_WidgetCount(context) < 1)
		{
			Cpc_Utils.StopAlarmmanager(context);
		}
	}

	@Override
	public void onDeleted(Context context,int[] appWidgetIDs)
	{
		super.onDeleted(context, appWidgetIDs);
		Log.i("WidgetProvider large","onDeleted");
	}
	
	
	@Override
	public void onEnabled(Context context)
	{
		super.onEnabled(context);

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.capricorn_layout_large);
		Cpc_Utils.Cpc_Check_WidgetCount(context);
		if(!Cpc_Utils_Data_Val.JustGetTheValues(context))
		{
		  Log.d("WidgtProvLargeOnEnabled", "Problem with Cpc_Utils.JustGetTheValues(context)");
		}

		Cpc_WidgetIntentReceiver.PrepareAllDisplayAttributes(context, remoteViews);
		setOnClickPendingIntent4AllElements(context, remoteViews);
		Log.i("WidgetProvider large","onEnabled");
	}
	
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) 
	{
		Log.i("WidgetProvider large","onUpdate" );
		// context.startService(new Intent (context, Cpc_BatteryReceiver.class));
		// Get all ids
		ComponentName thisWidget = new ComponentName(context,Cpc_WidgetProvider_large.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds) {
			// Register an onClickListener
			Intent intent = new Intent(context, Cpc_WidgetIntentReceiver.class);

			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.capricorn_layout_large);
			remoteViews.setOnClickPendingIntent(R.id.fl_Capricorn, pendingIntent);
			Cpc_WidgetProviderHelper.RegisterIntentReceiver();
			//appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
		//RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.capricorn_layout_large);
		//setOnClickPendingIntent4AllElements(context, remoteViews); // Alle Clickevents scharf machen
	}
	
	public static void setOnClickPendingIntent4AllElements(Context myContext, RemoteViews myRemoteViews)
	{
		// Log.d("Widgetprovider large","setOnClickPendingIntent4AllElements");
		myRemoteViews.setOnClickPendingIntent(R.id.fl_Capricorn , buildButtonPendingIntent(myContext));
		pushWidgetUpdate(myContext, myRemoteViews);
		// Log.d("pushWidgetUpdate in","setOnClickPendingIntent4AllElements");

	}

	private static PendingIntent buildButtonPendingIntent(Context context)
	{
//		Intent intent = new Intent(Cpc_Std_Data.ACTION_UPDATE_WIDGET);
		Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		if (Build.VERSION.SDK_INT >= (Build.VERSION_CODES.O)) {
			//intent.setClass(Cpc_Application.getContext(), Cpc_WidgetIntentReceiver.class);
			Cpc_WidgetProviderHelper.RegisterIntentReceiver();
		}
		// Log.d("Widgetprovider large","buildButtonPendingIntent");
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) 
	{
		ComponentName myWidget = new ComponentName(context, Cpc_WidgetProvider_large.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(myWidget, remoteViews);		
	}
	
	public static void SetTextAfterOrientationChange(Context context)
	{
		// Log.d("Widgetprovider large","SetTextAfterOrientationChange");
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.capricorn_layout_large);
		Cpc_WidgetProviderHelper.setTextViewText(remoteViews,3);
	}
	
	public static void SetDisplayParametersFromOptions(Context context)
	{
  	    Cpc_WidgetProviderHelper.SetDisplayParametersFromOptions4All(context, R.layout.capricorn_layout_large, 3);
	}


	
}
