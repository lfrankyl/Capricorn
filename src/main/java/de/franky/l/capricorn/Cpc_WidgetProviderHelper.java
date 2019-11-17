package de.franky.l.capricorn;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;

import static de.franky.l.capricorn.Cpc_Utils.CpcPref;
import static de.franky.l.capricorn.Cpc_Utils.CurVal;

class Cpc_WidgetProviderHelper
{
	static Cpc_WidgetIntentReceiver CpcReceiver  ;

	static void SetDisplayParametersFromOptions4All(Context context, int iRemoteViewID, int iWhich)
	{

		try
		{
			Bitmap backGrImage;
			Bitmap foreGrImage;
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), iRemoteViewID);
			int iElements = 2 * iWhich +1;  // Algorythmus für Anzahl der Elemente im Widget: kl=3, med=5, gr=7
			for (int i=0; i<iElements; i++)
			{
				if (Cpc_Utils.CpcData.iView[i] == 4 && CurVal.iBatCharging != 0) // 4 = Position der Batterie-Anzeige im Standard-Array
				{	// Check if phone is charging -> then show in image
					backGrImage = BitmapFactory.decodeResource(context.getResources(), CurVal.DispData[Cpc_Utils.CpcData.iView[i]].getIcon());
					foreGrImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.cpc_bat_charge);
					Bitmap mergedImages = mergeBitmap(foreGrImage, backGrImage);
					remoteViews.setImageViewBitmap(Cpc_Utils.CpcData.iRIdIv[i],mergedImages);
				}
				else
				{
					remoteViews.setImageViewResource(Cpc_Utils.CpcData.iRIdIv[i], CurVal.DispData[Cpc_Utils.CpcData.iView[i]].getIcon());
					//Log.d("Element position "+String.valueOf(i),String.valueOf(Cpc_Utils.CpcData.iView[i]));
				}

			}

			//Versuch Transpaarence im Widget einzustellen, kann gelöscht werden wenn sich keine endgültige Lösung ergibt

			int iBackground = Integer.parseInt(CpcPref.getString(context.getString(R.string.pref_Background_Key),context.getString(R.string.pref_Background_Default)));
			long lTransparence = CpcPref.getLong(R.string.pref_NuPi_BckgrdTransp_Key,R.string.pref_BckgrdTransp_Default);
			String[] sBckCols = context.getResources().getStringArray(R.array.pref_Background_Color);
			lTransparence =  (int) ((float )(100 - lTransparence)/100*255);
			String sCurCol = Integer.toHexString((int) lTransparence);
			if (sCurCol.length() == 1){
				sCurCol = "0" + sCurCol;
			}
			sCurCol = "#" + sCurCol + sBckCols[iBackground];
			switch (iBackground) {
				case 1:
					remoteViews.setInt(R.id.fl_Capricorn, "setBackgroundResource", R.drawable.background_light);
					remoteViews.setInt(R.id.fl_Capricorn, "setBackgroundColor", Color.parseColor(sCurCol));
					break;
				default:
					remoteViews.setInt(R.id.fl_Capricorn, "setBackgroundResource", R.drawable.background_dark);
					remoteViews.setInt(R.id.fl_Capricorn, "setBackgroundColor", Color.parseColor(sCurCol));
			}

			if (iWhich == 1)
			{
				Cpc_WidgetProvider_small.pushWidgetUpdate(context, remoteViews);
			}
			else if(iWhich == 2)
			{
				Cpc_WidgetProvider_medium.pushWidgetUpdate(context, remoteViews);
			}
			else if (iWhich == 3)
			{
				Cpc_WidgetProvider_large.pushWidgetUpdate(context, remoteViews);
			}
			
		}
		catch (Exception Ex)
		{
			//Toast.makeText(Cpc_Application.getContext(), "Capricorn: Waiting for sViewOptions in: SetDisplayParametersFromOptionsAll" , Toast.LENGTH_SHORT).show();
			Log.e("Cpc: Wait4sViewOptions:","SetDisplayParametersFromOptionsAll");
		}

	}

	static int getWidgetTextColor()
	{
		int iWdgtFontCol = Integer.parseInt(CpcPref.getString(Cpc_Application.getContext().getString(R.string.pref_WdgtFontcolor_Key),Cpc_Application.getContext().getString(R.string.pref_WdgtFontcolor_Default))) ;
		int iRGB;
		switch (iWdgtFontCol) {
			case 1:
				iRGB = Color.BLACK;
				break;
			case 2:
				iRGB = Color.RED;
				break;
			case 3:
				iRGB = Color.GREEN;
				break;
			case 4:
				iRGB = Color.BLUE;
				break;
			case 5:
				iRGB = Color.YELLOW;
				break;
			case 6:
				iRGB = Color.CYAN;
				break;
			case 7:
				iRGB = Color.MAGENTA;
				break;
			case 8:
				iRGB = Color.LTGRAY;
				break;
			case 9:
				iRGB = Color.DKGRAY;
				break;
			default:
				iRGB = Color.WHITE;
		}
		return iRGB;
	}
	
	static void setTextViewText (RemoteViews remoteViews, int iWhich)
	{
		Integer iTextSize = Cpc_Utils.SaveParseInt(CpcPref.getString(Cpc_Application.getContext().getString(R.string.pref_TextSize_Key), Cpc_Application.getContext().getString(R.string.pref_TextSize_Default)),12);

		int iElements = 2 * iWhich +1;
		int WdgtFontCol = getWidgetTextColor();
		for (int i=0; i<iElements; i++)
		{
			remoteViews.setTextViewText(Cpc_Utils.CpcData.iRIdTv[i], CurVal.DispData[Cpc_Utils.CpcData.iView[i]].getValue());
			remoteViews.setFloat(Cpc_Utils.CpcData.iRIdTv[i],"setTextSize",iTextSize);
			remoteViews.setInt(Cpc_Utils.CpcData.iRIdTv[i],"setTextColor",WdgtFontCol) ;
			remoteViews.setTextViewText(Cpc_Utils.CpcData.iRIdTvUnit[i], CurVal.DispData[Cpc_Utils.CpcData.iView[i]].getUnit());
			remoteViews.setFloat(Cpc_Utils.CpcData.iRIdTvUnit[i],"setTextSize",iTextSize);
			remoteViews.setInt(Cpc_Utils.CpcData.iRIdTvUnit[i],"setTextColor",WdgtFontCol) ;
			//Log.d("Element position "+String.valueOf(i),String.valueOf(iView[i]));

		}
	}

	static Bitmap mergeBitmap(Bitmap frame, Bitmap img){

		Bitmap bmOverlay = Bitmap.createBitmap(frame.getWidth(), frame.getHeight(), frame.getConfig());
		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(img, 0, 0, null);
		canvas.drawBitmap(frame, new Matrix(), null);

		return bmOverlay;

	}
	static void RegisterIntentReceiver(){
		if (!CurVal.bIsReceiverRegistered) {
			IntentFilter iFilter = new IntentFilter();
			iFilter.addAction(Intent.ACTION_USER_PRESENT);
			iFilter.addAction(Intent.ACTION_SHUTDOWN);
			// iFilter.addAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			CpcReceiver = new Cpc_WidgetIntentReceiver();
			Cpc_Application.getContext().registerReceiver(CpcReceiver, iFilter);
			Log.d("Cpc_WdgtProviderHelper", "RegisterIntentReceiver");
			CurVal.bIsReceiverRegistered = true;
			//Intent intent = new Intent(sIntentAction);
			//intent.setClass(Cpc_Application.getContext(), Cpc_WidgetIntentReceiver.class);
		}
	}
	static void UnRegisterIntentReceiver() {
		if (CurVal.bIsReceiverRegistered) {
			Log.d("Cpc_WdgtProviderHelper", "UnRegisterIntentReceiver");
			Cpc_Application.getContext().unregisterReceiver(CpcReceiver);
			CurVal.bIsReceiverRegistered = false;
		}
	}
	static void Cpc_StartSettings(Context context)
	{
		Intent configIntent = new Intent(context, Cpc_Activity_SettingsHeader.class);
		configIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(configIntent);
	}
	static void Cpc_CheckIfAlarmManagerOrStartSettings(){

	}

}
