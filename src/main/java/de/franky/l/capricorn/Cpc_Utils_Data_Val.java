package de.franky.l.capricorn;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.support.v13.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static de.franky.l.capricorn.Cpc_Utils.CpcPref;


class Cpc_Utils_Data_Val
{
	private static int iDaysOfYear(Context context, int iYear, int iMonth, int iDayOfMonth)
	{
		int iResult = 0;
		try
		{
			GregorianCalendar myCal = new GregorianCalendar();
			myCal.set(iYear, iMonth-1, iDayOfMonth);
//			Log.d("iDaysOfYear", String.valueOf(iYear)+":"+String.valueOf(iMonth)+":"+String.valueOf(iDayOfMonth));
			iResult = myCal.get(Calendar.DAY_OF_YEAR);
//			Log.d("iDaysOfYear", String.valueOf(iResult));
		}
		catch (Exception e) 
		{
			Toast.makeText(context, "iDaysOfYear: Something wrong", Toast.LENGTH_SHORT).show();
	    }
		return iResult;
	}

	private static boolean CalcMonitoringConstraints(Context context)
	{

		// Log.d("Monitoring","Daten zum monitoren der mobilen Daten");
    	//  Log.d("Monitoring debugging","Start -------------------------");
        Boolean bAllesOk 			= true;  // Rueckgabewert der Funktion
        try
        {
	    	int iCorrectionDays = 0;                                // Anzahl Korrekturtage beim Jahreswechsel
			Cpc_Utils.CurVal.iMobileCycleTyp = Cpc_Utils.SaveParseInt(CpcPref.getString(R.string.pref_MobileCycleOption_Key, R.string.pref_MobileCycleOption_Default),0);

			Calendar cal_Today = GregorianCalendar.getInstance();
			//int iTodayYear = cal_Today.get(Calendar.YEAR);
	    	int iTodayDayOfMonth = cal_Today.get(Calendar.DAY_OF_MONTH);   	// Das ist heute bezogen auf Monat 1 - 31
	    	int iTodayMonth = cal_Today.get(Calendar.MONTH);   				// Das ist der heutige Monat 1 - 12
	    	int iTodayDayOfYear = cal_Today.get(Calendar.DAY_OF_YEAR);		// Heute als Tag im Jahre 1 - 36x
			//Date todayDate = cal_Today.getTime();							// Heute als Datum
			/*
			Log.d("CalcMonitoringConstraints","Neu");
			
			Log.d("CurVal.bMobileCycleTyp",String.valueOf(Cpc_Utils.CurVal.bMobileCycleTyp));
			Log.d("iTodayYear", String.valueOf(iTodayYear));
			Log.d("iTodayDayOfMonth", String.valueOf(iTodayDayOfMonth));
			Log.d("iTodayMonth", String.valueOf(iTodayMonth));
			Log.d("iTodayDayOfYear", String.valueOf(iTodayDayOfYear));
			Log.d("todayDate", String.valueOf(todayDate));
			*/
			Calendar cal_change = GregorianCalendar.getInstance();
	    	int iChangeDayOfMonth = CpcPref.getInt(R.string.pref_NuPiCycle_Key, 1 );  	// Tag fuer den Reset der mobilen Daten
	    	cal_change.set(Calendar.DAY_OF_MONTH, iChangeDayOfMonth);
	    	if (Cpc_Utils.CurVal.iMobileCycleTyp > 0)														// wenn 30 oder 28 Tage eingestellt ist
	    	{
	        	if (iTodayDayOfMonth > iChangeDayOfMonth || 												// wenn heutiger Tag im Monat groesser als der Aenderungstag oder
	        	   (iTodayDayOfMonth >= iChangeDayOfMonth) && Cpc_Utils.CurVal.bMobResetLocked())						// der Tag groesser/gleich und die Daten zurueckgesetzt wurden dann ist dann muss es einen Monat spaeter sein.
	        	{
					cal_change = calSetMonthAndYear(cal_change,iTodayMonth);
	        	}
	    	}
	    	else																							// wenn monatlich eingestellt ist
	    	{
	        	if (iTodayDayOfMonth >= iChangeDayOfMonth)													// dann kann der Tag im Monat groesser oder gleich sein
	        	{
					cal_change = calSetMonthAndYear(cal_change,iTodayMonth);
	        	}
	    	}
			int iChangeYear = cal_change.get(Calendar.YEAR);
	    	// int iChangeMonth = cal_change.get(Calendar.MONTH);   				// Das ist der heutige Monat 1 - 12
	    	int iChangeDayOfYear = cal_change.get(Calendar.DAY_OF_YEAR);		// Heute als Tag im Jahre 1 - 36x
			Date changeDate = cal_change.getTime();							// Heute als Datum
			/*
			Log.d("iChangeYear", String.valueOf(iChangeYear));
			Log.d("iChangeDayOfMonth", String.valueOf(iChangeDayOfMonth));
			Log.d("iChangeMonth", String.valueOf(iChangeMonth));
			Log.d("iChangeDayOfYear", String.valueOf(iChangeDayOfYear));
			Log.d("changeDate", String.valueOf(changeDate));
			*/
			Calendar cal_begin = (Calendar) cal_change.clone();
			int[] iMobileCycleOptions = Cpc_Application.getContext().getResources().getIntArray(R.array.mobDataCycleOptions_Values);
            if (Cpc_Utils.CurVal.iMobileCycleTyp > 0)				// wenn 30 oder 28 Tage eingestellt ist
	    	{
	    		cal_begin.roll(Calendar.DAY_OF_YEAR,iMobileCycleOptions[Cpc_Utils.CurVal.iMobileCycleTyp] * -1);
	    	}
	    	else													// wenn monatlich eingestellt ist
	    	{
	    		cal_begin.roll(Calendar.MONTH,-1);
	    		
	    	}
	    	if (cal_begin.get(Calendar.MONTH) > cal_change.get(Calendar.MONTH))
	    	{
	    		cal_begin.roll(Calendar.YEAR,-1);
	    	}
	    	cal_begin.set(Calendar.HOUR, 0);
	    	cal_begin.set(Calendar.MINUTE, 0);
	    	Date beginDate = cal_begin.getTime();
			int iBeginYear = cal_begin.get(Calendar.YEAR);
	    	// int iBeginMonth = cal_begin.get(Calendar.MONTH);
	    	int iBeginDayOfYear = cal_begin.get(Calendar.DAY_OF_YEAR);	
			// int iBeginDayOfMonth = cal_begin.get(Calendar.DAY_OF_MONTH);
			/*
			Log.d("iBeginYear", String.valueOf(iBeginYear));
			Log.d("iBeginDayOfMonth", String.valueOf(iBeginDayOfMonth));
			Log.d("iBeginMonth", String.valueOf(iBeginMonth));
			Log.d("iBeginDayOfYear", String.valueOf(iBeginDayOfYear));
			Log.d("beginDate", String.valueOf(beginDate));
			*/
			if (iBeginYear != iChangeYear)
			{
	    		iCorrectionDays =iDaysOfYear(context, cal_begin.get(Calendar.YEAR), 12, 31) ;
			}
	    	Cpc_Utils.CurVal.DateOfChange = changeDate;
	    	Cpc_Utils.CurVal.iCycleDayOne = iBeginDayOfYear;	// Anfangstag des Zyklus bzg. auf Jahr
	        Cpc_Utils.CurVal.iCycleLength = iChangeDayOfYear + iCorrectionDays - iBeginDayOfYear;			// Zykluslaenge entw. 30T oder Tage entsprchend der Monate
	    	if (iTodayDayOfYear >= iBeginDayOfYear)
			{
				Cpc_Utils.CurVal.iCycleDone = iTodayDayOfYear - Cpc_Utils.CurVal.iCycleDayOne + 1;        // Abgelaufene Tage im aktuellen Zyklus
			}
			else
			{
				Cpc_Utils.CurVal.iCycleDone = iTodayDayOfYear + iCorrectionDays - Cpc_Utils.CurVal.iCycleDayOne + 1;        // Correectiondays nur verwenden, wenn Anfang des Zyklus und Ende des Zyklus in 2 verschiedenen Jahren ist
			}
			Cpc_Utils.CurVal.DateOfBegin  = beginDate;
	    	if (Cpc_Utils.CurVal.iCycleDone > Cpc_Utils.CurVal.iCycleLength)						// Das ist nur ein Workaround
			{																						// wenn das Wechseldatum später als die eingestellte Zykluslänge ist.
				Cpc_Utils.CurVal.iCycleDone = 0;													// Hier müsste eigentlich der Numberpicker das nicht zulassen
			}
			/*
	    	Log.d("iCycleDayOne",Integer.toString(Cpc_Utils.CurVal.iCycleDayOne));
	    	Log.d("iCorrectionDays",Integer.toString(iCorrectionDays));
	    	Log.d("iCycleLength",Integer.toString(Cpc_Utils.CurVal.iCycleLength));
	    	Log.d("iCycleDone",Integer.toString(Cpc_Utils.CurVal.iCycleDone));
			*/
        }
		catch (Exception e) 
		{
			Toast.makeText(context, "CalcMonitoringConstraints: Something wrong", Toast.LENGTH_SHORT).show();
			bAllesOk = false;
	    }
		return bAllesOk;
	}
	private static Calendar calSetMonthAndYear(Calendar calOfChange, int iTodayMonth){
		calOfChange.roll(Calendar.MONTH, 1);												// Kalender um einen Monat nach vorne drehen um Enddatum fuer Zyklus zu bekommen
		if (calOfChange.get(Calendar.MONTH)< iTodayMonth) 										// falls neuer Monat kleiner als aktueller Monat dann
		{
			calOfChange.roll(Calendar.YEAR, 1);			  								// Jahr auch um 1 nach vorne drehen
		}
		return calOfChange;
	}


	private static int iCalc_SMS(Context context)
	{
		int iNoSMS = 0;

		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {

			GregorianCalendar myCal = new GregorianCalendar();
			myCal.setTime(Cpc_Utils.CurVal.DateOfBegin);
			myCal.set(Calendar.AM_PM, Calendar.AM);
			myCal.set(Calendar.HOUR, 0);
			myCal.set(Calendar.MINUTE, 0);
			myCal.set(Calendar.SECOND, 0);
			// Log.d("myCal.getTime", String.valueOf(myCal.getTime()));
			try {

				final String[] projection = null;
				final String selection = null;
				final String[] selectionArgs = null;
				final String sortOrder = "DATE DESC";
				final Cursor Cpc_SMS_Cursor = context.getContentResolver().query(Uri.parse("content://sms/sent"), projection, selection, selectionArgs, sortOrder);
				if (Cpc_SMS_Cursor != null) {

					int type = Cpc_SMS_Cursor.getColumnIndex(CallLog.Calls.TYPE);
					int date = Cpc_SMS_Cursor.getColumnIndex(CallLog.Calls.DATE);
					Cpc_Utils.CurVal.bCallsMonitoring = CpcPref.getBool(R.string.pref_CallsMonitoring_Key, false);
					if (Cpc_SMS_Cursor.moveToFirst())
					{
						// Loop through the call log.
						do
						{
							String callDate = Cpc_SMS_Cursor.getString(date);
							Date callDayTime = new Date(Long.valueOf(callDate));
							if ((!callDayTime.before(myCal.getTime())) || !Cpc_Utils.CurVal.bCallsMonitoring || !Cpc_Utils.CurVal.bMobileDataCycle) {
								switch (Cpc_SMS_Cursor.getInt(type)) {
									case CallLog.Calls.OUTGOING_TYPE:
										iNoSMS = iNoSMS +1;
										// Log.d("outgoing zaehlt",String.valueOf(iCalls__Out_Num) + " " + callDayTime + " " + String.valueOf(iCallDurationCalc));
										break;
									case CallLog.Calls.INCOMING_TYPE:
										// Log.d("incoming",String.valueOf(iCalls___In_Num) + " " + callDayTime + " " + iCallDurationCalc);
										break;
								}
							}
						} while (Cpc_SMS_Cursor.moveToNext());

					}
					Cpc_SMS_Cursor.close();
				}

			} catch (Exception e) {
				Log.e("bCalc_SMS", "Exception");
				//e.printStackTrace();
				iNoSMS  = 0;
			}
		}
		return iNoSMS;
	}

	private static boolean bCalc_Calls(Context context)
	{
		boolean bAllesOk = true;
		int iCalls__Out_Min = 0;
		int iCalls__Out_Num = 0;
		int iCalls___In_Min = 0;
		int iCalls___In_Num = 0;
		int iCalls_Miss_Num = 0;
		double dCallsMinOutgoingUsed;								// Summe der Minuten der verbrauchten abgehenden Anrufe
		double dCallsMinOutgoingLeft;								// Summe der Minuten der uebrigen abgehenden Anrufe

		GregorianCalendar myCal = new GregorianCalendar();
		myCal.setTime(Cpc_Utils.CurVal.DateOfBegin);
		myCal.set(Calendar.AM_PM, Calendar.AM);
		myCal.set(Calendar.HOUR, 0);
		myCal.set(Calendar.MINUTE, 0);
		myCal.set(Calendar.SECOND, 0);
		// Log.d("myCal.getTime", String.valueOf(myCal.getTime()));
		try {

			final String[] projection = null;
			final String selection = null;
			final String[] selectionArgs = null;
			final String sortOrder = "DATE DESC";
			final Cursor Cpc_Cursor = context.getContentResolver().query(Uri.parse("content://call_log/calls"), projection, selection, selectionArgs, sortOrder);
			if (Cpc_Cursor != null) {
                Cpc_Utils.CurVal.FreePhoneNo = CpcPref.getStringArray(context.getString(R.string.pref_Calls_FreeNumbArr_Key));
				int type = Cpc_Cursor.getColumnIndex(CallLog.Calls.TYPE);
				int date = Cpc_Cursor.getColumnIndex(CallLog.Calls.DATE);
				int duration = Cpc_Cursor.getColumnIndex(CallLog.Calls.DURATION);
				int number = Cpc_Cursor.getColumnIndex(CallLog.Calls.NUMBER);
				Cpc_Utils.CurVal.bCallsMonitoring = CpcPref.getBool(R.string.pref_CallsMonitoring_Key, false);
				if (Cpc_Cursor.moveToFirst())
				{
					// Loop through the call log.
					ArrayList<String> sLocalFreePhoneNo = new ArrayList<String>();
					for(int i=0 ; i< Cpc_Utils.CurVal.FreePhoneNo.size();i++){
						sLocalFreePhoneNo.add(Cpc_Utils.CurVal.FreePhoneNo.get(i).replaceAll(" ","").trim());
					}

					do
					{
						String callDate = Cpc_Cursor.getString(date);
						Date callDayTime = new Date(Long.valueOf(callDate));
						String callDuration = Cpc_Cursor.getString(duration);
						String callNumber = Cpc_Cursor.getString(number);
						/*
						Log.d("callDayTime", String.valueOf(callDayTime));Log.d("bCallsMonitoring", String.valueOf((! callDayTime.before(myCal.getTime()))));
						Log.d("bCallsMonitoring", String.valueOf( Cpc_Utils.CurVal.bCallsMonitoring == true) );
						Log.d("bCallsMonitoring", String.valueOf( Cpc_Utils.CurVal.bMobileDataCycle == true));
						*/
						int iCallDurationCalc = CallDurationCheck(callDuration);
						// Log.d("Cursor auswerten",String.valueOf((!callDayTime.before(myCal.getTime())) || !Cpc_Utils.CurVal.bCallsMonitoring || !Cpc_Utils.CurVal.bMobileDataCycle));
						if ((!callDayTime.before(myCal.getTime())) || !Cpc_Utils.CurVal.bCallsMonitoring || !Cpc_Utils.CurVal.bMobileDataCycle) {
							switch (Cpc_Cursor.getInt(type)) {
								case CallLog.Calls.OUTGOING_TYPE:
                                    if (! sLocalFreePhoneNo.contains(callNumber)) {
                                        iCalls__Out_Num = iCalls__Out_Num + 1;
                                        iCalls__Out_Min = iCalls__Out_Min + iCallDurationCalc;
//                                        Log.d("outgoing zaehlt", String.valueOf(iCalls__Out_Num) + " " + callNumber + " " + callDayTime + " " + String.valueOf(iCallDurationCalc));
//                                    }
//                                    else
//                                    {
//                                        Log.d("outgoing zaehlt nicht", String.valueOf(iCalls__Out_Num) + " " + callNumber + " " + callDayTime + " " + String.valueOf(iCallDurationCalc));
                                    }
									break;
								case CallLog.Calls.INCOMING_TYPE:
									iCalls___In_Num = iCalls___In_Num + 1;
									iCalls___In_Min = iCalls___In_Min + iCallDurationCalc;
									// Log.d("incoming",String.valueOf(iCalls___In_Num) + " " + callDayTime + " " + iCallDurationCalc);
									break;
								case CallLog.Calls.MISSED_TYPE:
									iCalls_Miss_Num = iCalls_Miss_Num + 1;
									// Log.d("missed",String.valueOf(iCalls_Miss_Num) + " " + callDayTime + " " + iCallDurationCalc);
									break;
							}
						}
						//else
						//{
						//  Log.d("outgoing zaehlt nicht",String.valueOf(iCalls___In_Num) + " " + callDayTime + " " + iCallDurationCalc);
						//}
					} while (Cpc_Cursor.moveToNext());

				}
			Cpc_Cursor.close();
			}

			double dCallsMaxVal = (double) CpcPref.getLong(R.string.pref_NuPiMax_Calls_Key, R.string.pref_Calls_MaxVal_Default);
			long lCallsRefVal = CpcPref.getLong(R.string.pref_NuPiRef_Calls_Key, R.string.pref_Calls_RefVal_Default);
			// Hier ist kodiert wenn keine Anpassung verlangt wird.
			if (CalculationWithoutCorrection()) {
				lCallsRefVal = 0;
			}

			dCallsMinOutgoingUsed = dConvertSec2MinAndRound((double) (iCalls__Out_Min + lCallsRefVal *60 )); // Sekunden umrechnen in Minuten und auf eine Nachkommastelle runden
			if (Cpc_Utils.CurVal.bCallsIntegrateSMS)
			{
				Cpc_Utils.CurVal.iSMSNumSent = iCalc_SMS(context);
				dCallsMinOutgoingUsed = dCallsMinOutgoingUsed + Cpc_Utils.CurVal.iSMSNumSent;  // wenn der Vertrag Minuten und SMS kombiniert, dann diese zu den verbrauchten Minuten dazuzählen
			}

			Cpc_Utils.CurVal.dCallsMinIncoming = dConvertSec2MinAndRound((double) iCalls___In_Min);		// Sekunden umrechnen in Minuten und auf eine Stelle nach dem Komma runden

			Cpc_Utils.CurVal.iCallsNumIncoming = iCalls___In_Num;
			Cpc_Utils.CurVal.iCallsNumOutgoing = iCalls__Out_Num;
			Cpc_Utils.CurVal.iCallsNumMissed = iCalls_Miss_Num;

			Cpc_Utils.CurVal.dCallsMinOutgoingDayMax = dCallsMaxVal / Cpc_Utils.CurVal.iCycleLength;                      // Erlaubte Anrufe / Tag
			Cpc_Utils.CurVal.dCallsMinOutgoingDayCur = dCallsMinOutgoingUsed / Cpc_Utils.CurVal.iCycleDone; // Aktuell ausgehende Anrufe / Tag
			if (lCallsRefVal > 0) {
				Cpc_Utils.CurVal.iCallsNumOutgoing = Cpc_Utils.CurVal.iCallsNumOutgoing + 1; // Wenn Referenzwert angegeben dann ein Anruf addieren
			}
			Cpc_Utils.CurVal.dCallsMinOutgoingTodayMax = dCallsMaxVal / Cpc_Utils.CurVal.iCycleLength * Cpc_Utils.CurVal.iCycleDone;      // Max. moegliche Anrufe bis heute wenn linear

			dCallsMinOutgoingLeft = dCallsMaxVal - dCallsMinOutgoingUsed;
			if (dCallsMinOutgoingLeft > 0)  // uebrigen Freiminuten immer negativ anzeigen
			{
				dCallsMinOutgoingLeft = dCallsMinOutgoingLeft * -1;
			}
			Cpc_Utils.CurVal.bCallsMonitoringOk = dCallsMinOutgoingUsed / Cpc_Utils.CurVal.iCycleDone <= dCallsMaxVal / Cpc_Utils.CurVal.iCycleLength;
			Cpc_Utils.CurVal.bCallsViewTyp = CpcPref.getBool(R.string.pref_Calls_View_Key, false);

			if (Cpc_Utils.CurVal.bCallsViewTyp && Cpc_Utils.CurVal.bCallsMonitoring) {
				Cpc_Utils.FillDispDataArray(8,dCallsMinOutgoingLeft, String.valueOf(dCallsMinOutgoingLeft),"Min");
			} else {
				Cpc_Utils.FillDispDataArray(8,dCallsMinOutgoingLeft, String.valueOf(dCallsMinOutgoingUsed),"Min");
			}

			if (Cpc_Utils.CurVal.bCallsMonitoring && Cpc_Utils.CurVal.bMobileDataCycle)
			{
				if (Cpc_Utils.CurVal.bCallsMonitoringOk)
				{
					Cpc_Utils.CurVal.DispData[8].setIcon(R.drawable.cpc_phone_green);
				}
				else
				{
					Cpc_Utils.CurVal.DispData[8].setIcon(R.drawable.cpc_phone_red);
				}
			}
			else
			{
				Cpc_Utils.CurVal.DispData[8].setIcon(R.drawable.cpc_phone_gen);
			}

		/*
		Log.d("bCalc_Calls iCycleDone",Integer.toString(Cpc_Utils.CurVal.iCycleDone));
		Log.d("bCalc_Calls iCycleLength",Integer.toString(Cpc_Utils.CurVal.iCycleLength));
		Log.d("bCalc_Calls dCallsMax",String.valueOf(dCallsMaxVal));
		Log.d("bCalc_Calls CurVal.dCallsMinOutgoingUsed",String.valueOf(Cpc_Utils.CurVal.dCallsMinOutgoingUsed));
		Log.d("bCalc_Calls CurVal.dCallsMinOutgoingLeft",String.valueOf(Cpc_Utils.CurVal.dCallsMinOutgoingLeft));
		Log.d("bCalc_Calls CurVal.dCallsMinOutgoingDayMax",String.valueOf(Cpc_Utils.CurVal.dCallsMinOutgoingDayMax));
		Log.d("bCalc_Calls dCallsMinOutgoingDayCur",String.valueOf(Cpc_Utils.CurVal.dCallsMinOutgoingDayCur));
		Log.d("bCalc_Calls dCallsMinOutgoingTodayMax",String.valueOf(Cpc_Utils.CurVal.dCallsMinOutgoingTodayMax));
		Log.d("bCalc_Calls iCycleDayOne",Integer.toString(Cpc_Utils.CurVal.iCycleDayOne));
		Log.d("bCalc_Calls bCallsViewTyp", Boolean.toString(Cpc_Utils.CurVal.bCallsViewTyp));
		*/
		} catch (Exception e) {
			Log.e("bCalc_Calls", "Exception");
			//e.printStackTrace();
			bAllesOk = false;
		}
	return bAllesOk;
	}

	private static double dConvertSec2MinAndRound(double dSeconds){		// Sekunden umrechnen in Minuten und auf eine Nachkommastelle runden
		return Math.rint(((double) dSeconds ) / 60 * 10.0) / 10.0;
	}

	private static int CallDurationCheck (String sDurSec)
	{
		int iCallDurationCalc = Cpc_Utils.SaveParseInt(sDurSec,0);
		// Log.d("iCallDurationCalc", String.valueOf(iCallDurationCalc));
		// Log.d("iCallDurationCalc/60", String.valueOf((float)iCallDurationCalc/60));
		// Log.d("float iCallDurationCalc", String.valueOf(Math.floor((float)iCallDurationCalc/60)));
		try
		{
			if ((float) iCallDurationCalc / 60 > Math.floor((float)iCallDurationCalc/60))
			{
				iCallDurationCalc = (int) ((float)iCallDurationCalc/60 +1)  *60;
			}
		}
		catch (Exception e)
		{
			iCallDurationCalc = -1;
		}
		// Log.d("iCallDurationCalc danach", String.valueOf((float)iCallDurationCalc));
		return iCallDurationCalc;

	}
	
	private static boolean GetValues_WLAN(Context context, long lwlanMessung, long lMobileMessung)
	{
        Boolean bAllesOk 		= true;  // Rueckgabewert der Funktion
		long lwlanOffset;
		long lNuPi_wlan_RefVal;
		
		try
		{
			lNuPi_wlan_RefVal = CpcPref.getLong(R.string.pref_NuPiRef_Wlan_Key, R.string.pref_wlanRefVal_Default);
			lwlanOffset       = CpcPref.getLong(R.string.pref_wlanOffset_Key, 0);
/*
			 Log.d("JustGetTheValues  iNuPi_wlan_RefVal",String.valueOf(lNuPi_wlan_RefVal));
			 Log.d("JustGetTheValues  lwlanOffset",String.valueOf(lwlanOffset));
			 Log.d("JustGetTheValues  lwlanMessung",String.valueOf(lwlanMessung));
			 Log.d("JustGetTheValues  CurVal.dWlan",String.valueOf(lNuPi_wlan_RefVal +  lwlanMessung - lwlanOffset));
*/
			if (lNuPi_wlan_RefVal != 1)																					// wenn wlan Numberpickerwert <> 1Byte
			{
				Cpc_Utils.FillDispDataArray(6,lNuPi_wlan_RefVal +  lwlanMessung - lwlanOffset);
				if (Cpc_Utils.CurVal.DispData[6].getNumber() < 0)																				    // wenn Wert negativ dann in Lollipop
				{
					Cpc_Utils.FillDispDataArray(6,Cpc_Utils.CurVal.DispData[6].getNumber() + lMobileMessung);
				}
			}
			else																										// fuer Wert = 1 Byte (Sonderfall)
			{
				Cpc_Utils.FillDispDataArray(6,lwlanMessung);
			}
		}
		catch (Exception e) 
		{
			Toast.makeText(context, "GetValues_WLAN: Something wrong", Toast.LENGTH_SHORT).show();
			bAllesOk = false;
	    }
		return bAllesOk;
		
	}


	public static boolean JustGetTheValues(Context context)
	{
		
        Boolean bAllesOk;  // Rueckgabewert der Funktion
		bAllesOk 		 = Cpc_Utils_Std_Values.GetValues_Standard(context);
		String sMobileLeft ="Simone";											// Anzeigewert fuer noch uebrige mobile Daten
		double dMobileUsed = 574;											// Wert fuer verbrauchte mobile Daten
		double dMobileLeft = 978;											// Wert fuer noch uebrige mobile Daten
		// Log.d("Standard", String.valueOf(bAllesOk));
        // **********************************************************************************************
        // Make all values for mobile data
		//
		//
		//
		long lNuPi_Mobile_RefVal;
		long lMobileOffset;
		long lMobileMessung;
		long lwlanMessung;

		lNuPi_Mobile_RefVal 			 = CpcPref.getLong(R.string.pref_NuPiRef_Mob_Key, R.string.pref_MobileRefVal_Default);
		if (lNuPi_Mobile_RefVal < 0)
		{
			lNuPi_Mobile_RefVal = lNuPi_Mobile_RefVal * -1;
		}
		lMobileOffset       			 = CpcPref.getLong(R.string.pref_MobileMessung_Key, 0);
		Cpc_Utils.CurVal.iMobileCycleTyp = Integer.parseInt(CpcPref.getString(R.string.pref_MobileCycleOption_Key, R.string.pref_MobileCycleOption_Default));

		// Log.d("JustGetTheValues pref_WlanSetup_Key",String.valueOf(Cpc_Std_Data.getBoolean(context.getString(R.string.pref_WlanSetup_Key), false)));
		// Log.d("JustGetTheValues pref_FirstSetup_Key",String.valueOf(Cpc_Std_Data.getBoolean(context.getString(R.string.pref_FirstSetup_Key), false)));
		lMobileMessung = TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes();     			// Mobile Daten aus System Daten holen
		// Log.d("lMobileMessung",String.valueOf(lMobileMessung));
		if (lMobileMessung==0)       			// keine mobilen Daten gemessen werden z.B. enn Lollopop und wlan aktiv oder Mobile Daten komplett aus
		{
			lMobileMessung = CpcPref.getLong(Cpc_Std_Data.pref_MobileMessungSaved_Key,0);				// Deshalb Wert aus Speicher holen und anzeigen
			// Log.d("WLAN and Lollipop lMobileMessung",String.valueOf(lMobileMessung));
		}
		else																									// wenn kein wlan oder kein Lollipop
		{
			//lMobileMessung = TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes();     			// Mobile Daten aus System Daten holen
			// Die App wurde gerade frisch installiert oder jmd hat die App-Daten geloescht
			if (!CpcPref.getBool(Cpc_Std_Data.pref_FirstSetup_Key, false))
			{
				// Das erste Setup wurde per wlan gemacht
				if (CpcPref.getBool(Cpc_Std_Data.pref_WlanSetup_Key, false))
				{
					if (lMobileMessung > 0)
					{
					   CpcPref.putBool(Cpc_Std_Data.pref_FirstSetup_Key, true);						//Flag setzen dass richtige mobile Daten gemessen wurden
					   lMobileOffset = lMobileMessung;
						CpcPref.putLong(context.getString(R.string.pref_MobileMessung_Key), lMobileMessung);
					}
				}
			}
			CpcPref.putLong(Cpc_Std_Data.pref_MobileMessungSaved_Key, lMobileMessung);			// in Cpc Speicher ablegen falls oben
			// Log.d("Mobile or not Lollipop lMobileMessung",String.valueOf(lMobileMessung));
		}
		lwlanMessung = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes() - lMobileMessung; 		// Die Wlan Daten sind immer die Gesamtdaten minus der Mobilendaten, die ggf aus Cpc-Speicher kommen
		//Log.d("JustGetTheValues  lwlanMessung",String.valueOf(lwlanMessung));
		//Log.d("JustGetTheValues  lMobileMessung",String.valueOf(lMobileMessung));

		bAllesOk = GetValues_WLAN(context,lwlanMessung,lMobileMessung);
		//Log.d("WLAN", String.valueOf(bAllesOk));

		String[] sMobileCycleOptions = context.getResources().getStringArray(R.array.mobDataCycleOptions_Values);   // was bzgl. Mobile Daten eingestellt ist
		/*
		 Log.d("JustGetTheValues  iNuPi_Mobile_RefVal",String.valueOf(lNuPi_Mobile_RefVal));
		 Log.d("JustGetTheValues  lMobileOffset",String.valueOf(lMobileOffset));

		 Log.d("JustGetTheValues getMobileRxBytes", String.valueOf(TrafficStats.getMobileRxBytes()));
		 Log.d("JustGetTheValues getMobileTxBytes", String.valueOf(TrafficStats.getMobileTxBytes()));
		 Log.d("JustGetTheValues getTotalRxBytes", String.valueOf(TrafficStats.getTotalRxBytes()));
		 Log.d("JustGetTheValues getTotalRxBytes", String.valueOf(TrafficStats.getTotalTxBytes()));
        */
		Cpc_Utils.CurVal.iCycleDayOne = 0;

		// Hier ist kodiert wenn keine Anpassung verlangt wird.	
		if (CalculationWithoutCorrection())
		{
			dMobileUsed = lMobileMessung;
			Cpc_Utils.CurVal.DispData[5].setIcon(R.drawable.cpc_mobdata_gen);
		}

		// Hier ist kodiert wenn Referenzwert verlangt ist und sonst keine weitere Korrektur
		if (CalculationWithReferenceValue())
		{
			dMobileUsed = lNuPi_Mobile_RefVal +  lMobileMessung - lMobileOffset;
			Cpc_Utils.CurVal.DispData[5].setIcon(R.drawable.cpc_mobdata_gen);
	    }
		
		// Hier muss das kodiert werden, wenn ein Zyklus verlangt ist
        if (CalculationWithBillingPeriod())
        {
            // Log.d("Zyklus", Cpc_Std_Data.getString(context.getString(R.string.pref_MobileCycleOption_Key), context.getString(R.string.pref_MobileCycleOption_Default)));
	        Cpc_Utils.CurVal.bMobileDataCycle  =  true;
        	Calendar myCal = Calendar.getInstance();
            int iCurDay = myCal.get(Calendar.DAY_OF_MONTH);  // current day of month
    		int iNuPi_Mobile_Cycle;
    		
    		iNuPi_Mobile_Cycle = CpcPref.getInt_R_ID(R.string.pref_NuPiCycle_Key, R.string.pref_MobileCycle_Default);
    		//Log.d("Heute ist der Tag",String.valueOf(iCurDay == iNuPi_Mobile_Cycle));
			//Log.d("Wir haben es schon gemacht: ",String.valueOf(Cpc_Utils.CurVal.bMobResetLocked()));
    		if(iCurDay == iNuPi_Mobile_Cycle)						// Jetzt ist der Tag an dem die mobilen Daten zurueckgesetzt werden muessen
            {
            	if(!Cpc_Utils.CurVal.bMobResetLocked())				// Falls wir das noch nicht gemacht haben tun wir das, sonst nicht.
            	{
            		Log.d("I machs",String.valueOf(iNuPi_Mobile_Cycle));
	            	Cpc_Utils.CurVal.bMobResetLocked(true);					// verriegeln das das nur einmal gemacht wird
	            	dMobileUsed = 0;
	            	lNuPi_Mobile_RefVal = 0;
	            	CpcPref.putLong(context.getString(R.string.pref_MobileMessung_Key), lMobileMessung);
					CpcPref.putLong(context.getString(R.string.pref_NuPiRef_Mob_Key), (long) dMobileUsed);

					CpcPref.putLong(context.getString(R.string.pref_NuPiRef_Calls_Key), 0);
					switch (Cpc_Utils.CurVal.iMobileCycleTyp)
					{
						case 1:
							// wenn 30 Tage Zyklus verlangt ist
							myCal.roll(Calendar.DAY_OF_YEAR, 30);
							break;
						case 2:
							// wenn 28 Tage Zyklus verlangt ist
							myCal.roll(Calendar.DAY_OF_YEAR, 28);
							break;
						default:
							// wenn monatlicher Zyklus verlangt ist
							myCal.roll(Calendar.MONTH, 1);
							break;
					}
					CpcPref.putInt(context.getString(R.string.pref_NuPiCycle_Key), myCal.get(Calendar.DAY_OF_MONTH));
	            	// Log.d("MobReset",String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", myCal.getTime())));
	            	// Log.d("lMobileMessung",String.valueOf(lMobileMessung));
            	}
            	
            }
            else
            {
        		//Log.d("I machs ned",String.valueOf(iNuPi_Mobile_Cycle));
        		Cpc_Utils.CurVal.bMobResetLocked(false);
            	
            }
			dMobileUsed = lNuPi_Mobile_RefVal +  lMobileMessung - lMobileOffset;
			/*
			Log.d("iCurday",String.valueOf(iCurDay));
			Log.d("iNuPi_Mobile_RefVal)", String.valueOf(lNuPi_Mobile_RefVal));
			Log.d("lMobileMessung", String.valueOf(lMobileMessung));
			Log.d("lMobileOffset", String.valueOf(lMobileOffset));
			Log.d("bMobResetLocked", String.valueOf(Cpc_Utils.CurVal.bMobResetLocked()));
			*/
        }
        
    	bAllesOk = CalcMonitoringConstraints(context);
		// Log.d("CalcMonitoringConstraints", String.valueOf(bAllesOk));

        Cpc_Utils.CurVal.bMobileMonitoring = CpcPref.getBool(R.string.pref_MobileMonitoring_Key, false);
    	// Log.d("Monitoring ?",String.valueOf(CurVal.bMobileMonitoring));
        if (Cpc_Utils.CurVal.bMobileMonitoring) 							// Ueberwachung der mobilen Daten ist eingestellt
        {
        	double dMobMax = CpcPref.getLong(R.string.pref_NuPiMax_Mob_Key,R.string.pref_MobileMaxVal_Default); // Maxwert in Byte
        	
        	Cpc_Utils.CurVal.dMobileDayMax = dMobMax/Cpc_Utils.CurVal.iCycleLength;         				// Erlaubte Byte / Tag 
        	Cpc_Utils.CurVal.dMobileDayCur = dMobileUsed/Cpc_Utils.CurVal.iCycleDone;   		// Aktuell verbrauchte Byte / Tag
        	Cpc_Utils.CurVal.dMobileTodayMax = dMobMax/Cpc_Utils.CurVal.iCycleLength * Cpc_Utils.CurVal.iCycleDone; 		// Max. moegliche Menge bis heute wenn linear
        	dMobileLeft = dMobMax - dMobileUsed;
        	if (dMobileLeft < 0)	// uebrige Daten erstmal immer positiv machen damit MakeOutString funktioniert
        		
        	{
        		dMobileLeft = dMobileLeft * -1;
        	}
    		sMobileLeft = "-"+Cpc_Utils.MakeOutString(dMobileLeft); // dann uebrige Daten immer negativ anzeigen

			Cpc_Utils.CurVal.bMobileMonitoringOk = dMobileUsed / Cpc_Utils.CurVal.iCycleDone <= dMobMax / Cpc_Utils.CurVal.iCycleLength;

			/*
			Log.d("Monitoring CurVal.iCycleDone",Integer.toString(Cpc_Utils.CurVal.iCycleDone));
			Log.d("Monitoring CurVal.iCycleLength",Integer.toString(Cpc_Utils.CurVal.iCycleLength));
			Log.d("Monitoring CurVal.iCycleDayOne",Integer.toString(Cpc_Utils.CurVal.iCycleDayOne));

			Log.d("Logging MobData pref_NuPiRef_Mob_Key (dMobileUsed)",String.valueOf(dMobileUsed));
			Log.d("Logging MobData pref_MobileOffset_Key (lMobileMessung)",String.valueOf(lMobileMessung));
			Log.d("Monitoring dMobMax",String.valueOf(dMobMax));
			Log.d("Monitoring dMobMax/iCycleLength",String.valueOf(dMobMax/Cpc_Utils.CurVal.iCycleLength));
			Log.d("Monitoring CurValUsed.dMobile/CurVal.iCycleDone",String.valueOf(dMobileUsed/Cpc_Utils.CurVal.iCycleDone));

			Log.d("Monitoring debugging","Ende -------------------------");
			*/

        }
		CpcPref.putLong(context.getString(R.string.pref_MobileMessung_Key), lMobileMessung);
		CpcPref.putLong(context.getString(R.string.pref_NuPiRef_Mob_Key), (long) dMobileUsed);

		CpcPref.putLong(context.getString(R.string.pref_wlanOffset_Key), lwlanMessung);
		CpcPref.putLong(context.getString(R.string.pref_NuPiRef_Wlan_Key), (long) Cpc_Utils.CurVal.DispData[6].getNumber());

		if (Cpc_Utils.CurVal.bMobileMonitoring && Cpc_Utils.CurVal.bMobileDataCycle)
		{
			if (Cpc_Utils.CurVal.bMobileMonitoringOk)
			{
				Cpc_Utils.CurVal.DispData[5].setIcon(R.drawable.cpc_mobdata_green);
			}
			else
			{
				Cpc_Utils.CurVal.DispData[5].setIcon(R.drawable.cpc_mobdata_red);
			}
		}
		else
		{
			Cpc_Utils.CurVal.DispData[5].setIcon(R.drawable.cpc_mobdata_gen);
		}

		Cpc_Utils.CurVal.bMobileViewTyp = CpcPref.getBool(R.string.pref_MobileView_Key, false);
    	if (Cpc_Utils.CurVal.bMobileViewTyp && Cpc_Utils.CurVal.bMobileMonitoring)
    	{
			Cpc_Utils.FillDispDataArray(5,dMobileLeft,sMobileLeft,Cpc_Utils.CalcUnit(dMobileLeft));
    	}
		else
		{
			Cpc_Utils.FillDispDataArray(5,dMobileUsed);
		}

 		//  Log.d("JustGetTheValues lMobileNumberPicker",String.valueOf(lNuPi_Mobile_RefVal));
 		//  Log.d("JustGetTheValues Messung",String.valueOf(lMobileMessung));
 		//  Log.d("JustGetTheValues CurVal.dMobileUsed",String.valueOf(dMobileUsed));
 		//  Log.d("JustGetTheValues CurVal.sMobile",CurVal.sMobile);

		// Log.d("IntentReceiver", "Just get the values");

        //bAllesOk = bCalc_Calls(context);
        // Log.d("bCalc_Calls", String.valueOf(bAllesOk));

    	return(bAllesOk);
	}

	static boolean IsADayWithinDaysRange(Integer iDay2Consider, Integer iRange)
	{
    	// Log.d("IsADayWithin30Days","Start -------------------------");
    	Calendar myTempCal = Calendar.getInstance();							// Wir lassen uns vom System ein Kalender-Objekt geben
    	int iTodayYear = myTempCal.get(Calendar.DAY_OF_YEAR);   				// Das ist heute bezogen aufs Jahr
		boolean bResult = false;												// Ergebnis falsch als Default
		int iDay2ConsiderYear = 0;												// Das ist der Wechseltag bezogen aufs Jahr Default = 0
    	int iTodayMonth = myTempCal.get(Calendar.DAY_OF_MONTH);   				// Das ist heute bezogen auf Monat
    	if (iTodayMonth > iDay2Consider)										// wenn heute groesser ist als der fragliche Tag dann ist es der nächste Monat
    	{
			if (myTempCal.get(Calendar.MONTH) == Calendar.DECEMBER )			// wenn diesen Monat = Dezember dann müssen wir den Jahreswechsel berücksichtigen
			{																    // erst Gesamttage des Jahres bestimmen
																				// dann das Wechseldatum dazurechnen
				iDay2ConsiderYear = iDaysOfYear(Cpc_Application.getContext(),myTempCal.get(Calendar.YEAR),12,31)+iDay2Consider ;
			}
    	}
    	else
		{
			myTempCal.set(Calendar.DAY_OF_MONTH,iDay2Consider  );
			iDay2ConsiderYear = myTempCal.get(Calendar.DAY_OF_YEAR);
		}
    	if (iDay2ConsiderYear - iTodayYear < iRange)							// jetzt nur checken ob die Differenz der Jahrestage innerhalb des Ranges sind
		{
			bResult = true;														// wenn ja dann Egebnis wahr
		}
		return bResult ;
	}

	private static boolean CheckDataCalculation (int WhatToCheck)
	{
		boolean Result = false;
		Context context = Cpc_Application.getContext();
		String[] sMobileOptions = context.getResources().getStringArray(R.array.mobDataOptions_Values);				// Hole Definitionen um zu checken
	    
		if (CpcPref.getString(R.string.pref_MobileOptions_Key, R.string.pref_MobileOptions_Default).equals(sMobileOptions[WhatToCheck]))
		{
			Result = true;
		}

		return Result;
	}
	private static boolean CalculationWithoutCorrection ()
	{
		boolean Result;
		Result = CheckDataCalculation(0);
		return Result;
	}
	private static boolean CalculationWithReferenceValue ()
	{
		boolean Result;
		Result = CheckDataCalculation(1);
		return Result;
	}
	private static boolean CalculationWithBillingPeriod ()
	{
		boolean Result;
		Result = CheckDataCalculation(2);
		return Result;
	}
	
	static boolean IsWLANandLollipop()
	{
		return  IsWifi() &&  (Build.VERSION.SDK_INT >= (Build.VERSION_CODES.LOLLIPOP));       		// Wenn Lollopop und wlan aktiv dann keine Werte fuer mobile Daten
	}

	private static boolean IsWifi()
	{
		return getNetworkType() == ConnectivityManager.TYPE_WIFI;     // WiFi eingeschaltet ist
	}

	private static boolean isNetworkOff(){
		return getNetworkType() == -1;
	}
	private static int getNetworkType(){
		ConnectivityManager cm = (ConnectivityManager) Cpc_Application.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);  // Hole Systemdaten um zu erkennen ob
		int iReturn = -1;
		NetworkInfo NwI = cm.getActiveNetworkInfo();
		if (NwI != null)
		{
			iReturn = NwI.getType();
		}
		return iReturn;
	}
} 	// End of class Cpc_Utils_Data_Val

