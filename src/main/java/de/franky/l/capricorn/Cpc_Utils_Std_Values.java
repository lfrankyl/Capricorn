package de.franky.l.capricorn;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import static de.franky.l.capricorn.Cpc_Utils.CpcPref;

class Cpc_Utils_Std_Values
{

	static boolean GetValues_Standard(Context context)
	{

		Boolean bAllesOk 			= true;  // Rueckgabewert der Funktion
		try
		{
			// **********************************************************************************************
			// Make all values for RAM usage
			// *                                                                                            *
			SetRAM2DispData(context, 1); 		// 1 = Available RAM
			SetRAM2DispData(context, 9);		// 9 = Total RAM

			// **********************************************************************************************
			// Make all values for battery level
			// We do not make it anymore with a service but with a sticky intent
			// of BatteryManager
			// Anyway Battery receiver has to be registered first
			// *                                                                                            *
			SetBatLevel2DispData(context,4); 		// 4 = Battery


			// **********************************************************************************************
			// Make all values for CPU usage
			// *                                                                                            *
			SetCPUUsage2DispData(context, 0);		// 0 = CPU


			// **********************************************************************************************
			// Make all values for for storage devices
			// *                                                                                            *
			SetStorageDevices2DispData(context);
			// **********************************************************************************************

			// **********************************************************************************************
			// Make all values for for brightness level
			// *                                                                                            *
			SetBrightness2DispData(context,7);		// 8 = Brightness
			// **********************************************************************************************

		}
		catch (Exception e)
		{
			Toast.makeText(context, "GetValues_Standard: Something wrong", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			bAllesOk = false;
		}
		return bAllesOk;
	}

	private static void SetBrightness2DispData(Context context, int Index)
	{
		int iBrightness = Math.round(Settings.System.getInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,0)/(float) 255*100);
		Cpc_Utils.FillDispDataArray(Index,iBrightness,String.valueOf(iBrightness),"%");

	}

	private static void SetRAM2DispData(Context context,int Index)
	{
		double  dRAM;

		try
		{
			if (Index == 1) {
				dRAM = dGetAvaiableRAM(context);
			}
			else
			{
				dRAM = dGetTotalRAM(context);
			}
			//Log.d("SetRAM2DispData ",String.valueOf(dRAM/1024/1024));

			Cpc_Utils.FillDispDataArray(Index, dRAM);
		}
		catch (Exception e)
		{
			Toast.makeText(context, "SetAvailableRAM2DispData: Something wrong", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	private static void SetBatLevel2DispData(Context context,int Index){

		int     iBatteryLevel;

		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = context.getApplicationContext().registerReceiver(null, ifilter);
		int iBatRawLevel = 0;
		int iBatRawScale = -1;

		if (batteryStatus != null)
		{
			iBatRawLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			iBatRawScale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			Cpc_Utils.CurVal.iBatCharging = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
		}
		iBatteryLevel = Math.round(iBatRawLevel / (float)iBatRawScale *100);

		// Log.d("GetValues_Standard Batterylevel", String.valueOf(iBatteryLevel));

		if (iBatteryLevel >= 0)
		{
			CpcPref.putInt(Cpc_Std_Data.pref_BatSaved_Key, iBatteryLevel);				// Bat.-Level speichern falls Bat.-Receiver keinen schluessigen Wert liefert
		}
		else
		{
			iBatteryLevel = CpcPref.getInt(Cpc_Std_Data.pref_BatSaved_Key,-34) ;
			Log.i("GetValues_Standard", "iBatteryLevel Save " + String.valueOf(iBatteryLevel));
		}
		Cpc_Utils.FillDispDataArray(Index,iBatteryLevel,String.valueOf(iBatteryLevel),"%");

		// Set the right battery icon in the widget depending on the battery level
		if 		(iBatteryLevel >= 80)	Cpc_Utils.CurVal.DispData[Index].setIcon(R.drawable.cpc_bat_100);
		else if (iBatteryLevel >= 60)	Cpc_Utils.CurVal.DispData[Index].setIcon(R.drawable.cpc_bat_80);
		else if (iBatteryLevel >= 40)	Cpc_Utils.CurVal.DispData[Index].setIcon(R.drawable.cpc_bat_60);
		else if (iBatteryLevel >= 20)	Cpc_Utils.CurVal.DispData[Index].setIcon(R.drawable.cpc_bat_40);
		else							Cpc_Utils.CurVal.DispData[Index].setIcon(R.drawable.cpc_bat_20);

		//Log.d("SetBatLevel2DispData int",String.valueOf(iBatteryLevel));
		//Log.d("SetBatLevel2DispData String",Cpc_Utils.CurVal.DispData[Index].getValue());
		// **********************************************************************************************

	}
	private static void SetCPUUsage2DispData(Context context,int Index){
		float   fCPUusage 		= Cpc_Utils_CPU.fCPU;
		String  sAvailableCPU 	= String.valueOf(fCPUusage).substring(0, 3);			                    // Hier machen wir bei der CPU Auslastung glatte Zahlen (ohne Nachkomma-Anteil);

		if (sAvailableCPU.substring(sAvailableCPU.length()-1,sAvailableCPU.length()).equalsIgnoreCase(".")) // falls das letzte Zeichen ein Punkt ist
		{
			sAvailableCPU = sAvailableCPU.substring(0,sAvailableCPU.length()-1);                            // dann Punkt loeschen
		}
		if (Build.VERSION.SDK_INT <= (Build.VERSION_CODES.N_MR1)) {
			Cpc_Utils.FillDispDataArray(Index,fCPUusage,sAvailableCPU,"%");
		}
		else // Wenn Android 8 (Oreo) dann funktioniert CPU nicht
		{
			Cpc_Utils.FillDispDataArray(Index,fCPUusage,context.getString(R.string.NotWorking),context.getString(R.string.blankChr));
		}

	}

	private static void SetStorageDevices2DispData(Context context){
		double  dAvailableSDCard1 	= 0;

		Cpc_Utils_CT.Device CpcStorageDevices[] = Cpc_Utils_CT.getDevices(context);							// Hole alle Geraete aus CT Utils Klasse
	        /*
	        for (int i = 0; i < CpcStorageDevices.length; i++)
	        {
	        	Log.d("Cpc_CT_Utils.getDevices"," ");
	        	Log.d("Cpc_CT_Utils.getDevices",String.valueOf(i));
	        	Log.d("Cpc_CT_Utils.getDevices",CpcStorageDevices[i].toString());
	        	Log.d("Cpc_CT_Utils.mRemovable",String.valueOf(CpcStorageDevices[i].mRemovable));
	        	Log.d("Cpc_CT_Utils.mState",String.valueOf(CpcStorageDevices[i].mState));
	        	Log.d("Cpc_CT_Utils.getState",String.valueOf(CpcStorageDevices[i].getState()));
	        	Log.d("Cpc_CT_Utils.isAvailable",String.valueOf(CpcStorageDevices[i].isAvailable()));
	        	Log.d("Cpc_CT_Utils.getFreeSpace",String.valueOf(CpcStorageDevices[i].getFreeSpace()/1024/1024/1024));
	        }
	        */

		Cpc_Utils.FillDispDataArray(2,CpcStorageDevices[0].getFreeSpace());					// 2 = internal SD-Card verfügbar
		// CpcStorageDevices[0]: 0 ist immer die interne SD-Karte
		Cpc_Utils.FillDispDataArray(10,CpcStorageDevices[0].getTotalSpace());					// 10 = internal SD-Card insgesamt

		//Log.d("Cpc_CT_Utils.length", String.valueOf(CpcStorageDevices.length));
		if (CpcStorageDevices.length > 1)															// wenn es eine externe SD-Ka	rte gibt
		{
			dAvailableSDCard1 = CpcStorageDevices[1].getFreeSpace();								// Die zweite [1] ist immer die welchselbare SD-Karte
			Cpc_Utils.FillDispDataArray(11,CpcStorageDevices[1].getTotalSpace()); 				// 11 = removeable SD-Card insgesamt
		}

		if (dAvailableSDCard1 > 0)																	// Wenn bei der ext. SD-Karte was > 0 rauskommt
		{																							// 3 = removeable SD-Card verfügbar
			Cpc_Utils.FillDispDataArray(3,dAvailableSDCard1);									// dann ists der Wert der gespeichert wird
		}
		else                             															// falls der Wert 0 ist
		{
			Cpc_Utils.FillDispDataArray(3,dAvailableSDCard1,"n/a","");			// dann ists sie offensichtlich nicht da, daher n/a speichern
		}
	}

	private static double dGetAvaiableRAM(Context myContext)
	{
		return getMemoryInfo(myContext).availMem;
	}

	private static double dGetTotalRAM(Context myContext)
	{
		return getMemoryInfo(myContext).totalMem;
	}

	private static MemoryInfo getMemoryInfo(Context myContext)
	{
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager)myContext.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		return mi;
	}

}
