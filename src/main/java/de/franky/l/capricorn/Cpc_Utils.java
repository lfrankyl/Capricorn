package de.franky.l.capricorn;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Cpc_Utils
{

	public static Cpc_SharePref CpcPref = new Cpc_SharePref();
	public static final Cpc_Values CurVal = new Cpc_Values();
	public static final Cpc_Std_Data CpcData = new Cpc_Std_Data();

	public static void ActionsWhenShutDown(Context context)
	{
		StopAlarmmanager(context);

		long lNumberPicker = (long) Cpc_Utils.CurVal.DispData[5].getNumber();
		CpcPref.putLong(context.getString(R.string.pref_MobileMessung_Key), 0);
		CpcPref.putLong(context.getString(R.string.pref_NuPiRef_Mob_Key), lNumberPicker);
		CpcPref.putLong(Cpc_Std_Data.pref_MobileMessungSaved_Key, 0);

		// Log.d("ShutDown Logging pref_NuPiRef_Mob_Key",String.valueOf(lNumberPicker));

		lNumberPicker = (long)Cpc_Utils.CurVal.DispData[6].getNumber();
		CpcPref.putLong(context.getString(R.string.pref_wlanOffset_Key), 0);
		CpcPref.putLong(context.getString(R.string.pref_NuPiRef_Wlan_Key), lNumberPicker);
	}

	public static int SaveParseInt(String sToParseString, int iDef_Val)
	{
		int iValue; 
		try 
		 {
			iValue = Integer.parseInt(sToParseString);
		 } 
		 catch(NumberFormatException nfe) 
		 {
			 iValue = iDef_Val;
		 } 
		return iValue;
	}

	public static String MakeOutString (double NumberToBeCalc )
	{
        // Makes a string based on the data val in bytes. Takes care does not longer than 4 digits
        // and deletes point if its the last digit
        // Input are Bytes, method calcs best fit to value Kbyte, MByte, GByte, TByte
	    String sTmpResult = "n/a";
		 
        // Log.d("MakeOutString vor",String.valueOf(NumberToBeCalc));
        if (NumberToBeCalc >= 0)
        {
            // Log.d("MakeOutString nach",String.valueOf(NumberToBeCalc));
            if 		(NumberToBeCalc >= (double)(1024*1024*1024)*1024)	NumberToBeCalc = NumberToBeCalc / 1024 / 1024 / 1024 / 1024;
            else if (NumberToBeCalc >= (1024*1024*1024))		 		NumberToBeCalc = NumberToBeCalc / 1024 / 1024 / 1024;
            else if	(NumberToBeCalc >= (1024*1024))						NumberToBeCalc = NumberToBeCalc / 1024 / 1024;
            else if	(NumberToBeCalc >= 1024)							NumberToBeCalc = NumberToBeCalc / 1024;

            sTmpResult = String.valueOf(NumberToBeCalc);

            if (sTmpResult.length() > 4 ){
                sTmpResult = sTmpResult.substring(0,4);			// Result is max. 4 chr incl. decimal point
            }
            if (sTmpResult.substring(sTmpResult.length()-1,sTmpResult.length()).equalsIgnoreCase(".")){  // decimal point noe is the last char
                sTmpResult = sTmpResult.substring(0,sTmpResult.length()-1);								// then delete
            }
        }
	    return (sTmpResult);
	
	}

    public static int iCalcDataVal(long lValueAsBytes){
        // Calculates the number which is shown in the numberpickers
        // Must be used together with iCalcDataUnitIndex.
        float fDataValNew = (float) lValueAsBytes;

        while (fDataValNew > 9999){
            fDataValNew = fDataValNew /1024;
        }
        return Math.round(fDataValNew);
    }

    public static int iCalcDataUnitIndex(long lValueAsBytes){
        // Calculates the Index of the unit 0=Byte, 1=KByte, 2=MByte, 3=GByte, 4=TByte
        // based on the number as bytes, which is used in the numberpickers
        // Must be used together which iCalcDataVal

        float fDataValNew = (float) lValueAsBytes;
        int iUnitIndex = 0;

        while (fDataValNew > 9999){
            fDataValNew = fDataValNew /1024;
            iUnitIndex = iUnitIndex +1;
        }
        return iUnitIndex;
    }

	public static String CalcUnit (double NumberToBeCalc )
	{
        // Determines the unit of the Data val shown in the widget and the App.
        // Input are Byte, Method calcs the best fit to unit KByte, MByte, GByte, TByte
        String sSizeUnit;

		if 		(NumberToBeCalc < 0)									sSizeUnit = "";
		else if (NumberToBeCalc >= ((double)(1024*1024*1024)*(1024)))	sSizeUnit = " TB";
		else if (NumberToBeCalc >=  (1024*1024*1024))					sSizeUnit = " GB";
		else if	(NumberToBeCalc >=  (1024*1024))						sSizeUnit = " MB";
		else if	(NumberToBeCalc >=  1024)								sSizeUnit = " KB";
		else															sSizeUnit = " Byte";

		return (sSizeUnit);

	}
	public static int iCalcDataValOld(long lValueAsBytes){
        // Calculates the number which is shown in the numberpickers
        // Must be used together with iCalcDataUnitIndex.
        // works only if values permitted until 1024.
        // If values permitted until 9999 use the new version
		int iDataValNew = 0;

		if 		(lValueAsBytes >= (long)(1024*1024*1024)*(1024))	iDataValNew = (int) lValueAsBytes/1024/1024/1024/1024;
		else if (lValueAsBytes >= (1024*1024*1024))					iDataValNew = (int) lValueAsBytes/1024/1024/1024;
		else if (lValueAsBytes >= (1024*1024))						iDataValNew = (int) lValueAsBytes/1024/1024;
		else if (lValueAsBytes >= (1024))							iDataValNew = (int) lValueAsBytes/1024;

		return iDataValNew;
	}

	public static int iCalcDataUnitIndexOld(long lValueAsBytes){
        // Calculates the Index of the unit 0=Byte, 1=KByte, 2=MByte, 3=GByte, 4=TByte
        // based on the number as bytes, which is used in the numberpickers
        // Must be used together which iCalcDataVal
        // works only if values permitted until 1024.
        // If values permitted until 9999 use the new version
        int iDataUnitNew = 0;
		if 		(lValueAsBytes >= (long)(1024*1024*1024) * (1024))	iDataUnitNew = 4;
		else if (lValueAsBytes >= (1024*1024*1024))					iDataUnitNew = 3;
		else if (lValueAsBytes >= (1024*1024))						iDataUnitNew = 2;
		else if (lValueAsBytes >= (1024))							iDataUnitNew = 1;

		return iDataUnitNew;
	}

	public static long lCalcDataValToByte(long lValue ,  int iUnit) {
	// Calculates the data value into bytes based on the Unit Index
	// Works at any time
	long lValueByte = lValue;
	if (iUnit == 1)
		lValueByte = lValueByte * 1024;
	else if (iUnit == 2)
		lValueByte = lValueByte * 1024 * 1024;
	else if (iUnit == 3)
		lValueByte = lValueByte * 1024 * 1024 * 1024;
	else if (iUnit == 4) lValueByte = lValueByte * 1024 * 1024 * 1024 * 1024;

	return lValueByte;
}


	public static void changeTextSize(ViewGroup root)
	{
		Integer iTextSize = SaveParseInt(CpcPref.getString(R.string.pref_TextSizeApp_Key, R.string.pref_TextSize_Default),12);
		if (root != null) {
			for (int i = 0; i < root.getChildCount(); i++) {
				View v = root.getChildAt(i);
				if (v instanceof TextView) {
					((TextView) v).setTextSize(iTextSize);
				} else if (v instanceof ViewGroup) {
					changeTextSize((ViewGroup) v);
				}
			}
		}
	}

	@SuppressLint("NewApi")
	private static void StartAlarmmanager(Context context)
	{
		AlarmManager AM=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent amstartintent = new Intent(context, Cpc_AlarmManager.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, amstartintent, 0);

		String sIntervall = CpcPref.getString(context.getString(R.string.pref_Intervall_Key), context.getString(R.string.pref_Intervall_Default));
		Integer iIntervall = SaveParseInt(sIntervall,1000);
		Log.i("Cpc_Utils","Alarmmanager start " + sIntervall);
		if (Build.VERSION.SDK_INT >= (Build.VERSION_CODES.KITKAT))
			AM.setExact(AlarmManager.ELAPSED_REALTIME,SystemClock.elapsedRealtime()+iIntervall,pi);
		else
			AM.set(AlarmManager.ELAPSED_REALTIME,SystemClock.elapsedRealtime()+iIntervall,pi);
	}

	public static void StopAlarmmanager(Context context)
	{
		// Log.i("CPC_Utils","Alarmmanager Stop I");
		AlarmManager AM=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent amstartintent = new Intent(context, Cpc_AlarmManager.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, amstartintent, 0);
		Cpc_Utils_CPU.stopCpuMonitoring();
		AM.cancel(pi);
		Log.i("CPC_Utils","Alarmmanager Stop II");
		//Intent intent= new Intent(context, Cpc_BatteryReceiver.class);
		//context.stopService(intent); 
	}

	public static int Cpc_WidgetCount(Context context)
	{
		int iWidgetCount;
		Cpc_Check_WidgetCount(context);
		iWidgetCount = Cpc_Utils.CurVal.NoSmlWdgt + Cpc_Utils.CurVal.NoMedWdgt + Cpc_Utils.CurVal.NoBigWdgt;
	    // Log.d("Number of widgets: ",String.valueOf(iWidgetCount));
	    return iWidgetCount;
	}

	public static void Cpc_Check_WidgetCount(Context context)
	{
		int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context,Cpc_WidgetProvider_small.class));
		Cpc_Utils.CurVal.NoSmlWdgt = ids.length;
		ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context,Cpc_WidgetProvider_medium.class));
		Cpc_Utils.CurVal.NoMedWdgt = ids.length;
		ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context,Cpc_WidgetProvider_large.class));
		Cpc_Utils.CurVal.NoBigWdgt = ids.length;
		// Log.d("Number of widgets: ",String.valueOf(iWidgetCount));
	}
	public static boolean CPU_is_Visible()
	{
		boolean bResult;
		bResult =  0 == Cpc_Utils.CpcData.iView[0] ;
		bResult =  bResult || (0 == Cpc_Utils.CpcData.iView[1]) ;
		bResult =  bResult || (0 == Cpc_Utils.CpcData.iView[2]) ;
		bResult = bResult && (Cpc_Utils.CurVal.NoSmlWdgt > 0 || Cpc_Utils.CurVal.NoMedWdgt > 0 || Cpc_Utils.CurVal.NoBigWdgt > 0);
		if (! bResult ) {
			bResult = bResult || (0 == Cpc_Utils.CpcData.iView[3]);
			bResult = bResult || (0 == Cpc_Utils.CpcData.iView[4]);
			bResult = bResult && (Cpc_Utils.CurVal.NoMedWdgt > 0 || Cpc_Utils.CurVal.NoBigWdgt > 0);
			if (! bResult ) {
				bResult = bResult || (0 == Cpc_Utils.CpcData.iView[5]);
				bResult = bResult || (0 == Cpc_Utils.CpcData.iView[6]);
				bResult = bResult && (Cpc_Utils.CurVal.NoBigWdgt > 0);
			}
		}
		// Log.d("CPU_is_visible",String.valueOf(bResult) );
		return bResult;
	}
	public static void StartWidgetUpdates(){
		if (Cpc_Application.getContext() == null)
		{
			Log.e("StartWidgetUpdates","context = null");
		}
		else {
			Cpc_Utils.CpcData.Update_iViews();					// Position der Elemente im Widget im globalen Arry iView aktualisieren
			if (Cpc_Utils.Cpc_WidgetCount(Cpc_Application.getContext()) > 0) {
				Cpc_Utils.StartAlarmmanager(Cpc_Application.getContext());
			}
			if (Cpc_Utils.CPU_is_Visible())
			{
				if (!Cpc_Utils_CPU.CPU_Monitoring_Runs())
				{
					if (!Cpc_Utils_CPU.startCpuMonitoring())
					{
						Log.e("Cpc_Act_SettingsHeader", "Something wrong with startCpuMonitoring");
					}
				}
			}
			else
			{
				Cpc_Utils_CPU.stopCpuMonitoring();
			}
		}

	}
	public static void FillDispDataArray(int iIndex, double dNumber){
		Cpc_Utils.CurVal.DispData[iIndex].setNumber(dNumber);
		Cpc_Utils.CurVal.DispData[iIndex].setValue(Cpc_Utils.MakeOutString(dNumber));
		Cpc_Utils.CurVal.DispData[iIndex].setUnit(Cpc_Utils.CalcUnit(dNumber));
		//Log.d("FillDispDataArray 0", String.valueOf(iIndex));
	}

	public static void FillDispDataArray(int iIndex, double dNumber, String sNumber, String sUnit){
		Cpc_Utils.CurVal.DispData[iIndex].setNumber(dNumber);
		Cpc_Utils.CurVal.DispData[iIndex].setValue(sNumber);
		Cpc_Utils.CurVal.DispData[iIndex].setUnit(sUnit);
		//Log.d("FillDispDataArray 1", String.valueOf(iIndex));
	}
	static void Show_Oreo_Message_If_Necessary(Context context){
		// Für OREO Geräte muss einmal ein Hinweis kommen dass im Widegt das Verhalten anders geworden ist
		if (!CpcPref.getBool(Cpc_Std_Data.pref_OREO_Message_Key, false))
		{
			if (Build.VERSION.SDK_INT >= (Build.VERSION_CODES.O)) {
				AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(context);
				DialogBuilder.setTitle(R.string.Oreo_Dlg_Message_Title);
				DialogBuilder.setMessage(R.string.Oreo_Dlg_Message_Msg);

				DialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing but close the dialog
						dialog.dismiss();
					}
				});
				AlertDialog alert = DialogBuilder.create();
				alert.show();

			}
			CpcPref.putBool(Cpc_Std_Data.pref_OREO_Message_Key, true);
		}

	}

}

