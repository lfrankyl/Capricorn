package de.franky.l.capricorn;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static de.franky.l.capricorn.Cpc_Utils.CpcPref;

public class Cpc_Settings_Frag_Cost_Rel_Data extends PreferenceFragment
		implements SharedPreferences.OnSharedPreferenceChangeListener

{
	private SharedPreferences DefSharedPref;


	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
		// Log.d("Cpc_Settings_Frag_Cost_Rel_Data","On create");
        addPreferencesFromResource(R.xml.cpc_setting_frag_cost_rel_data);

		DefSharedPref = PreferenceManager.getDefaultSharedPreferences(Cpc_Application.getContext());

		Preference myNumberPickerPref     = findPreference(getString(R.string.pref_NuPiRef_Mob_Key));        // muss hier definiert werden sonst kennt die Listboxpreference den nicht
		Preference myNumberPickerCycle    = findPreference(getString(R.string.pref_NuPiCycle_Key));          // 				"
		Preference myNumberPickerMaxVal   = findPreference(getString(R.string.pref_NuPiMax_Mob_Key));        // 				"
//		Preference myNumberPickerMaxCalls = findPreference(getString(R.string.pref_NuPiMax_Calls_Key));      // 				"
//		Preference myNumberPickerRefValCalls = findPreference(getString(R.string.pref_NuPiRef_Calls_Key));   // 				"
//		Preference free_PhoneNumber       = findPreference(getString(R.string.pref_Calls_FreeNumb_Key));// 				"


		// Einstellungen fuer Zyklus Typ
		String[] sMobileCycleOptions = Cpc_Application.getContext().getResources().getStringArray(R.array.mobDataCycleOptions);
		ListPreference myPrefCycleType = (ListPreference) findPreference(getString(R.string.pref_MobileCycleOption_Key));
		String sCurrentSummText = getString(R.string.pref_MobileCycleOption_Sum) + getString(R.string.blankChr) + sMobileCycleOptions[Integer.parseInt(myPrefCycleType.getValue())] ;
		myPrefCycleType.setSummary(sCurrentSummText);
		myPrefCycleType.setOnPreferenceChangeListener(new OnPreferenceChangeListener() 
		{
		    @Override
		    public boolean onPreferenceChange(Preference preference, Object newValue) 
		    {
				String[] sMobileCycleOptions = Cpc_Application.getContext().getResources().getStringArray(R.array.mobDataCycleOptions);
				int iNewValue = Integer.parseInt((String)newValue);
				Cpc_Utils.CurVal.iMobileCycleTyp = iNewValue;
				// Log.d("onPChg bMobileCycleTyp",String.valueOf(iNewValue));
		    	String sNewSummText;
				// Log.d("onPreferenceChange",getString(R.string.pref_Key_viewLeft));
				sNewSummText = getString(R.string.pref_MobileCycleOption_Sum) + getString(R.string.blankChr) + sMobileCycleOptions[iNewValue] ;
		        preference.setSummary(sNewSummText);
		        return true;
		    }
		}
		);

		 
		 // Einstellungen fuer Numberpicker Referenzwert
		 // Log.d("NumberPickerPref",	 myNumberPickerPref.getKey());

         long lCurrentSummText = CpcPref.getLong(R.string.pref_NuPiRef_Mob_Key,R.string.pref_MobileRefVal_Default);
         sCurrentSummText = Cpc_Utils.MakeOutString(lCurrentSummText);
		 sCurrentSummText = sCurrentSummText + " " + Cpc_Utils.CalcUnit(lCurrentSummText);
		 myNumberPickerPref.setSummary(sCurrentSummText);
		 myNumberPickerPref.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() 
	        {
	
	            @Override
	            public boolean onPreferenceChange(Preference preference, Object newValue) 
	            {
	            	String sNewSummText;
	                long lMobileNumberPicker = CpcPref.getLong(R.string.pref_NuPiRef_Mob_Key,R.string.pref_MobileRefVal_Default);
	    	      	sNewSummText = String.valueOf(Cpc_Utils.MakeOutString(lMobileNumberPicker)) + " " + Cpc_Utils.CalcUnit(lMobileNumberPicker );
			        preference.setSummary(sNewSummText);
					CpcPref.putBool(Cpc_Std_Data.pref_WlanSetup_Key, Cpc_Utils_Data_Val.IsWLANandLollipop());
			        if (Cpc_Utils_Data_Val.IsWLANandLollipop())
			        {
			            Log.i("pref_NuPiRef_Mob_Key", "pref_WlanSetup_Key -> true");
			        }
			        else
			        {
		                long lMessung = TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes();
						CpcPref.putLong(getString(R.string.pref_MobileMessung_Key), lMessung);
			            //Log.d("pref_NuPiRef_Mob_Key", "pref_WlanSetup_Key -> false " + String.valueOf(lMessung));
			        }
	                return true;
	            }
	        }
		 );
	
         // Einstellungen fuer Numberpicker fuer eigenen Zyklus
      	 GregorianCalendar myCal = new GregorianCalendar();
		 int iCurrentSummText = CpcPref.getInt_R_ID( R.string.pref_NuPiCycle_Key, R.string.pref_MobileCycle_Default);
	     int iCurrentMonth = myCal.get(Calendar.MONTH);      // Aktuellen Monat merken
	     // Log.d("NuPi original I",String.valueOf(iCurrentSummText));
		 if (Cpc_Utils.CurVal.iMobileCycleTyp > 0)					// Wenn 30 oder 28-Tage-Zyklus
		 {
	         if ( (iCurrentSummText < myCal.get(Calendar.DAY_OF_MONTH)) || 
	        	  (iCurrentSummText <= myCal.get(Calendar.DAY_OF_MONTH)) && Cpc_Utils.CurVal.bMobResetLocked() )
	         {
	             myCal.roll(Calendar.MONTH, 1);
        		 if (myCal.get(Calendar.MONTH)< iCurrentMonth) // falls neuer Monat kleiner als aktueller Monat dann 
        		 {
        			myCal.roll(Calendar.YEAR, 1);			  // Jahr auch um 1 nach vorne drehen
        		 }
	         }
		 }
		 else													// wenn monatlicher Zyklus
		 {
	         if (iCurrentSummText <= myCal.get(Calendar.DAY_OF_MONTH))
	         {
	             myCal.roll(Calendar.MONTH, 1);
        		 if (myCal.get(Calendar.MONTH)< iCurrentMonth) // falls neuer Monat kleiner als aktueller Monat dann 
        		 {
        			myCal.roll(Calendar.YEAR, 1);			  // Jahr auch um 1 nach vorne drehen
        		 }
	         }
		 }
         myCal.set(Calendar.DAY_OF_MONTH, iCurrentSummText);

         String sDate = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", myCal.getTime()));
         // Log.d("NuPi original II",sDate);
		 sCurrentSummText = getString(R.string.pref_MobileCycle_Sum1) + getString(R.string.blankChr) + sDate;
		 myNumberPickerCycle.setSummary(sCurrentSummText);
		 myNumberPickerCycle.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener()
	        {
	
	            @Override
	            public boolean onPreferenceChange(Preference preference, Object newValue) 
	            {
	            	String sNewSummText;
	            	GregorianCalendar myCal = new GregorianCalendar();
	                String sDate;
	       		 	int iMobileNumberPicker = CpcPref.getInt_R_ID(R.string.pref_NuPiCycle_Key, R.string.pref_MobileCycle_Default);
	       	     	int iCurrentMonth = myCal.get(Calendar.MONTH);      // Aktuellen Monat merken
	                if ((myCal.get(Calendar.DAY_OF_MONTH) > iMobileNumberPicker) || 
	                	(myCal.get(Calendar.DAY_OF_MONTH) >= iMobileNumberPicker) && Cpc_Utils.CurVal.bMobResetLocked())
	                {
	                	myCal.roll(Calendar.MONTH, 1);
						if (myCal.get(Calendar.MONTH)< iCurrentMonth) // falls neuer Monat kleiner als aktueller Monat dann 
						{
							myCal.roll(Calendar.YEAR, 1);			  // Jahr auch um 1 nach vorne drehen
						}
	                }
					// Log.d("NuPi changed iMobileNumberPicker",String.valueOf(iMobileNumberPicker));
                	myCal.set(Calendar.DAY_OF_MONTH, iMobileNumberPicker);
	                sDate = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", myCal.getTime()));
					// Log.d("NuPi changed sDate",String.valueOf(iMobileNumberPicker));
					sNewSummText = getString(R.string.pref_MobileCycle_Sum1) + getString(R.string.blankChr) + sDate; 
			        preference.setSummary(sNewSummText);
			        //Log.d("NuPi changed sDate",String.valueOf(Cpc_Utils.CurVal.iMobileCycleTyp));

					int[] iMobileCycleOptions = Cpc_Application.getContext().getResources().getIntArray(R.array.mobDataCycleOptions_Values);
					if((Cpc_Utils.CurVal.iMobileCycleTyp > 0) && !Cpc_Utils_Data_Val.IsADayWithinDaysRange(iMobileNumberPicker,iMobileCycleOptions[Cpc_Utils.CurVal.iMobileCycleTyp]))
			        {
				        AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(getActivity());
	
				        DialogBuilder.setTitle(R.string.pref_MobileCycle_XXDays2Long_Title);
				        DialogBuilder.setMessage(Cpc_Application.getContext().getResources().getString(R.string.pref_MobileCycle_XXDays2Long_Message1)
								+ Cpc_Application.getContext().getResources().getString(R.string.blankChr)
								+ String.valueOf(iMobileCycleOptions[Cpc_Utils.CurVal.iMobileCycleTyp])
								+ Cpc_Application.getContext().getResources().getString(R.string.pref_MobileCycle_XXDays2Long_Message2));

				        DialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() 
				        {
				        	public void onClick(DialogInterface dialog, int which) {
				                // Do nothing but close the dialog
				                dialog.dismiss();
				            }
				        });
				        AlertDialog alert = DialogBuilder.create();
						Log.d("NuPi changed sDate","Jetzt sollte der Dialog kommen");
				        alert.show();
			        }
	                return true;
	            }
	        }
		 );
		
			// Einstellungen fuer Anzeige mobile Daten verbrauchte oder noch uebrige
			SwitchPreference myPrefMobViewType = (SwitchPreference) findPreference(getString(R.string.pref_MobileView_Key));
			Cpc_Utils.CurVal.bMobileViewTyp = myPrefMobViewType.isChecked();
			if (myPrefMobViewType.isChecked())
			{
				sCurrentSummText = getString(R.string.pref_MobileView_SumOn) ;
			}
			else
			{
				sCurrentSummText = getString(R.string.pref_MobileView_SumOff) ;
			}
			myPrefMobViewType.setSummary(sCurrentSummText);
			myPrefMobViewType.setOnPreferenceChangeListener(new OnPreferenceChangeListener() 
			{
			    @Override
			    public boolean onPreferenceChange(Preference preference, Object newValue) 
			    {
					String sNewSummText;
			    	if (newValue.equals(true))
					{
			    		sNewSummText = getString(R.string.pref_MobileView_SumOn) ;
						Cpc_Utils.CurVal.bMobileViewTyp = true;

					}
					else
					{
						sNewSummText = getString(R.string.pref_MobileView_SumOff) ;
						Cpc_Utils.CurVal.bMobileViewTyp = false;
					}
					// Log.d("onPreferenceChange",getString(R.string.pref_MobileView_Key));
			        preference.setSummary(sNewSummText);
			        return true;
			    }
			}
			);


		// Einstellungen fuer Mobile Daten monitoren Ja/Nein
		SwitchPreference myPrefMobileMonitoring = (SwitchPreference) findPreference(getString(R.string.pref_MobileMonitoring_Key));
		sCurrentSummText = SetMonitoringResultData(false,myNumberPickerMaxVal,myPrefMobViewType, null, null,myPrefMobileMonitoring.isChecked());
		myPrefMobileMonitoring.setSummary(sCurrentSummText);
		myPrefMobileMonitoring.setOnPreferenceChangeListener(new OnPreferenceChangeListener() 
		{
		    @Override
		    public boolean onPreferenceChange(Preference preference, Object newValue) 
		    {
				Preference myNumberPickerMaxVal= findPreference(getString(R.string.pref_NuPiMax_Mob_Key));
				SwitchPreference myPrefMobViewType = (SwitchPreference) findPreference(getString(R.string.pref_MobileView_Key));
				String sNewSummText = SetMonitoringResultData(false, myNumberPickerMaxVal,myPrefMobViewType, null, null, newValue.equals(true));
				// Log.d("onPreferenceChange",getString(R.string.pref_MobileMonitoring_Key));
		        preference.setSummary(sNewSummText);
		        return true;
		    }
		}
		);


		 // Einstellungen fuer Numberpicker Maxwert mobile Daten
		 // Log.d("NumberPickerMaxVal",	 myNumberPickerMaxVal.getKey());

        lCurrentSummText = CpcPref.getLong( R.string.pref_NuPiMax_Mob_Key, R.string.pref_MobileMaxVal_Default) ;
        sCurrentSummText = Cpc_Utils.MakeOutString( lCurrentSummText);
		// Log.d("pref_Mobile_MaxVal_Sum",	 sCurrentSummText);
		sCurrentSummText = sCurrentSummText + " " + Cpc_Utils.CalcUnit(lCurrentSummText);
		myNumberPickerMaxVal.setSummary(sCurrentSummText);
		myNumberPickerMaxVal.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() 
	        {
	            @Override
	            public boolean onPreferenceChange(Preference preference, Object newValue) 
	            {
					String sNewSummText;
					long lMobileNumberPicker = CpcPref.getLong( R.string.pref_NuPiMax_Mob_Key, R.string.pref_MobileMaxVal_Default);
					sNewSummText = String.valueOf(Cpc_Utils.MakeOutString(lMobileNumberPicker)) + " " + Cpc_Utils.CalcUnit(lMobileNumberPicker);
					// Log.d("pref_Mobile_MaxVal_Sum onPreferenceChange iMobileNumberPicker",	 String.valueOf(dMobileNumberPicker));
					preference.setSummary(sNewSummText);
					return true;
	            }
	        }
		 );
		
		// Einstellungen fuer Datenberechnung: ohne, Referenzwert, Abrechnungszeitraum
		String[] sMobileOptions = Cpc_Application.getContext().getResources().getStringArray(R.array.mobDataOptions);
		ListPreference myPrefMobileOptions = (ListPreference) findPreference(getString(R.string.pref_MobileOptions_Key));
		int iCurValue = Integer.parseInt(myPrefMobileOptions.getValue());
		sCurrentSummText = sMobileOptions[iCurValue] ;
		myPrefMobileOptions.setSummary(sCurrentSummText);

		if (iCurValue == 1) 															// Referenzwert
		{
			// Log.d("pref_Key_mobData_Options on change",sMobileOptions[1]);
			SetOptions(true,false,false);
		}
		else
		{
			if(iCurValue == 2)															// Zyklus
			{
				// Log.d("pref_Key_mobData_Options on change",sMobileOptions[2]);
				SetOptions(true,true,true);

			}
			else																		// ohne Anpassung
			{
				// Log.d("pref_Key_mobData_Options on change",sMobileOptions[0]);
				SetOptions(false,false,false);
			}
		}

		myPrefMobileOptions.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
			  {
				  @Override
				  public boolean onPreferenceChange(Preference preference, Object newValue)
				  {
					  String[] sMobileOptions = Cpc_Application.getContext().getResources().getStringArray(R.array.mobDataOptions);
					  int iNewValue = Integer.parseInt((String)newValue);

					  // Log.d("pref_Key_mobData_Options",(String)newValue);
					  if (iNewValue == 1) 															// Referenzwert
					  {
						  // Log.d("pref_Key_mobData_Options on change",sMobileOptions[1]);
						  SetOptions(true,false,false);
					  }
					  else
					  {
						  if(iNewValue == 2)															// Zyklus
						  {
							  // Log.d("pref_Key_mobData_Options on change",sMobileOptions[2]);
							  SetOptions(true,true,true);


						  }
						  else																		// ohne Anpassung
						  {
							  // Log.d("pref_Key_mobData_Options on change",sMobileOptions[0]);
							  SetOptions(false,false,false);
						  }
					  }
					  String sNewSummText;
					  sNewSummText = sMobileOptions[iNewValue] ;
					  preference.setSummary(sNewSummText);
					  return true;
				  }
			  }
		);

		/* From this position call related code which is invalid currently due to Google policy

		// Einstellungen fuer Anzeige Anrufe verbrauchte oder noch uebrige
		SwitchPreference myPrefCallsViewType = (SwitchPreference) findPreference(getString(R.string.pref_Calls_View_Key));
		Cpc_Utils.CurVal.bCallsViewTyp = myPrefCallsViewType.isChecked();
		if (myPrefCallsViewType.isChecked())
		{
			sCurrentSummText = getString(R.string.pref_Calls_View_SumOn) ;
		}
		else
		{
			sCurrentSummText = getString(R.string.pref_Calls_View_SumOff) ;
		}
		myPrefCallsViewType.setSummary(sCurrentSummText);
		myPrefCallsViewType.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
		    @Override
		    public boolean onPreferenceChange(Preference preference, Object newValue)
		    {
				String sNewSummText;
		    	if (newValue.equals(true))
				{
		    		sNewSummText = getString(R.string.pref_Calls_View_SumOn) ;
					Cpc_Utils.CurVal.bCallsViewTyp = true;
				}
				else
				{
					sNewSummText = getString(R.string.pref_Calls_View_SumOff) ;
					Cpc_Utils.CurVal.bCallsViewTyp = false;
				}
				// Log.d("onPreferenceChange",getString(R.string.pref_Calls_View_Key));
		        preference.setSummary(sNewSummText);
		        return true;
		    }
		}
		);

		// Einstellungen fuer SMS in Freiminuten integrieren oder nicht
		SwitchPreference myPrefCallsIntegrateSMS = (SwitchPreference) findPreference(getString(R.string.pref_CallsIntegrateSMS_Key));
		Cpc_Utils.CurVal.bCallsIntegrateSMS = myPrefCallsIntegrateSMS.isChecked();
		if (myPrefCallsIntegrateSMS.isChecked())
		{
			sCurrentSummText = getString(R.string.pref_CallsIntegrateSMS_SumOn) ;
		}
		else
		{
			sCurrentSummText = getString(R.string.pref_CallsIntegrateSMS_SumOff) ;
		}
		myPrefCallsIntegrateSMS.setSummary(sCurrentSummText);
		myPrefCallsIntegrateSMS.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		  {
			  @Override
			  public boolean onPreferenceChange(Preference preference, Object newValue)
			  {
				  String sNewSummText;
				  if (newValue.equals(true))
				  {
					  sNewSummText = getString(R.string.pref_CallsIntegrateSMS_SumOn) ;
					  Cpc_Utils.CurVal.bCallsIntegrateSMS = true;
				  }
				  else
				  {
					  sNewSummText = getString(R.string.pref_CallsIntegrateSMS_SumOff) ;
					  Cpc_Utils.CurVal.bCallsIntegrateSMS = false;
				  }
				  // Log.d("onPreferenceChange",getString(R.string.pref_CallsIntegrateSMS_Key));
				  preference.setSummary(sNewSummText);
				  return true;
			  }
		  }
		);


		// Einstellungen fuer Anrufe monitoren Ja/Nein
		SwitchPreference myPrefCallsMonitoring = (SwitchPreference) findPreference(getString(R.string.pref_CallsMonitoring_Key));
		sCurrentSummText = SetMonitoringResultData(true, myNumberPickerMaxCalls,myPrefCallsViewType,myPrefCallsIntegrateSMS,free_PhoneNumber,myPrefCallsMonitoring.isChecked());
		myPrefCallsMonitoring.setSummary(sCurrentSummText);
		myPrefCallsMonitoring.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
		    @Override
		    public boolean onPreferenceChange(Preference preference, Object newValue)
		    {
				Preference myNumberPickerCalls = findPreference(getString(R.string.pref_NuPiMax_Calls_Key));
				Preference free_PhoneNumber = findPreference(getString(R.string.pref_Calls_FreeNumb_Key));
				SwitchPreference myPrefCallsViewType = (SwitchPreference) findPreference(getString(R.string.pref_Calls_View_Key));
				SwitchPreference myPrefCallsIntegrateSMS = (SwitchPreference) findPreference(getString(R.string.pref_CallsIntegrateSMS_Key));
				String sNewSummText = SetMonitoringResultData(true, myNumberPickerCalls,myPrefCallsViewType,myPrefCallsIntegrateSMS, free_PhoneNumber,newValue.equals(true));
				// Log.d("onPreferenceChange",getString(R.string.pref_Key_MobileMonitoring));
		        preference.setSummary(sNewSummText);
		        return true;
		    }
		}
		);


		 // Einstellungen fuer Numberpicker Maxwert Freiminuten
		 // Log.d("NumberPickerMaxVal",	 myNumberPickerMaxVal.getKey());

        lCurrentSummText = CpcPref.getLong( R.string.pref_NuPiMax_Calls_Key, R.string.pref_Calls_MaxVal_Default);
        sCurrentSummText = Cpc_Utils.MakeOutString( lCurrentSummText);
		// Log.d("pref_NuPiMaxCalls_Key",	 sCurrentSummText);
		sCurrentSummText = sCurrentSummText + " " + getString(R.string.pref_Calls_MaxVal_Unit);
		myNumberPickerMaxCalls.setSummary(sCurrentSummText);
		myNumberPickerMaxCalls.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener()
	        {
	            @Override
	            public boolean onPreferenceChange(Preference preference, Object newValue)
	            {
					String sNewSummText;
					long lMobileNumberPicker = CpcPref.getLong( R.string.pref_NuPiMax_Calls_Key, R.string.pref_Calls_MaxVal_Default);
					sNewSummText = String.valueOf(Cpc_Utils.MakeOutString(lMobileNumberPicker)) + " " + getString(R.string.pref_Calls_MaxVal_Unit);
					// Log.d("pref_NuPiMaxCalls_Key onPreferenceChange lMobileNumberPicker",	 String.valueOf(lMobileNumberPicker));
					preference.setSummary(sNewSummText);
					return true;
	            }
	        }
		 );

		 // Einstellungen fuer Numberpicker Referenzewert Freiminuten
		 // Log.d("NumberPickerMaxVal",	 myNumberPickerMaxVal.getKey());

		lCurrentSummText = CpcPref.getLong(R.string.pref_NuPiRef_Calls_Key,R.string.pref_Calls_RefVal_Default);
		sCurrentSummText = Cpc_Utils.MakeOutString( lCurrentSummText);
		// Log.d("pref_NuPiRefCalls_Key",	 sCurrentSummText);
		sCurrentSummText = sCurrentSummText + " " + getString(R.string.pref_Calls_RefVal_Unit);
		myNumberPickerRefValCalls.setSummary(sCurrentSummText);
		myNumberPickerRefValCalls.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener()
	        {
	            @Override
	            public boolean onPreferenceChange(Preference preference, Object newValue)
	            {
					String sNewSummText;
					long lMobileNumberPicker = CpcPref.getLong(R.string.pref_NuPiRef_Calls_Key,R.string.pref_Calls_RefVal_Default);
					sNewSummText = String.valueOf(Cpc_Utils.MakeOutString(lMobileNumberPicker)) + " " + getString(R.string.pref_Calls_RefVal_Unit);
					// Log.d("pref_NuPiRefCalls_Key onPreferenceChange iMobileNumberPicker",	 String.valueOf(lMobileNumberPicker));
					preference.setSummary(sNewSummText);
					return true;
	            }
	        }
		 );

		// Einstellungen fuer kostenlose Telefonnummern
		// Log.d("pref_Calls_FreeNumb_Sum",	 myNumberPickerMaxVal.getKey());
		// Muss als letztes initialisiert werden, sonst machen andere Preferences das wieder kaputt

		lCurrentSummText = CpcPref.getInt(R.string.pref_Calls_FreeNumb_Key,0);
		sCurrentSummText = String.valueOf(lCurrentSummText);
		Log.d("free_PhoneNumber",	 sCurrentSummText);
		sCurrentSummText = sCurrentSummText + " " + getString(R.string.pref_Calls_FreeNumb_Sum);
		free_PhoneNumber.setSummary(sCurrentSummText);





		 */

	}

	@Override
	public void onResume() {
		super.onResume();
		DefSharedPref.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		DefSharedPref.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		DefSharedPref.unregisterOnSharedPreferenceChangeListener(this);
	}
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		try	{
			Preference free_PhoneNumber       = findPreference(Cpc_Application.getContext().getResources().getString(R.string.pref_Calls_FreeNumb_Key));
			// Log.d("onSharedPreferenceChanged",	 key);
			if (key.equals(Cpc_Application.getContext().getResources().getString(R.string.pref_Calls_FreeNumb_Key))|| key.equals(getString(R.string.pref_Calls_FreeNumbArr_Key )))
			{
				int iCurrentSummText = CpcPref.getInt(R.string.pref_Calls_FreeNumb_Key,R.string.pref_Calls_FreeNumb_Default);
				String sCurrentSummText = String.valueOf(iCurrentSummText);
				// Log.d("free_PhoneNumber changed II",	 sCurrentSummText);
				sCurrentSummText = sCurrentSummText + " " + getString(R.string.pref_Calls_FreeNumb_Sum);
				free_PhoneNumber.setSummary(sCurrentSummText);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(Cpc_Application.getContext(), "Exception @ onSharedPreferenceChanged", Toast.LENGTH_LONG).show();

		}

	}

	private void SetOptions(boolean bReference, boolean bCategory, boolean bMonitoring)
	{
    	Preference myNuPiRef_Mob  = findPreference(getString(R.string.pref_NuPiRef_Mob_Key));
		Preference myNuPiMax_Mob= findPreference(getString(R.string.pref_NuPiMax_Mob_Key)); 
    	
		Preference myNuPiRef_Calls  = findPreference(getString(R.string.pref_NuPiRef_Calls_Key));
		Preference myNuPiMax_Calls= findPreference(getString(R.string.pref_NuPiMax_Calls_Key)); 

		Preference myNuPiCycle = findPreference(getString(R.string.pref_NuPiCycle_Key)); 
		
    	Preference myMobileCycleOption = findPreference(getString(R.string.pref_MobileCycleOption_Key));
		SwitchPreference myMobileMonitoring  = (SwitchPreference) findPreference(getString(R.string.pref_MobileMonitoring_Key));
		Preference myMobileView        = findPreference(getString(R.string.pref_MobileView_Key));
		
		//SwitchPreference myCalls_Monitoring  = (SwitchPreference) findPreference(getString(R.string.pref_CallsMonitoring_Key));
		//Preference myCalls_Integrating_SMS = findPreference(getString(R.string.pref_CallsIntegrateSMS_Key));
		//Preference myCalls_View        = findPreference(getString(R.string.pref_Calls_View_Key));
		//Preference myCalls_FreePhoneNo = findPreference(getString(R.string.pref_Calls_FreeNumb_Key));

		SetPreferenceStatus(myNuPiCycle, bCategory);
		SetPreferenceStatus(myMobileCycleOption, bCategory);

		SetPreferenceStatus(myNuPiRef_Mob, bReference);

		//SetPreferenceStatus(myNuPiRef_Calls, bReference);

		SetPreferenceStatus(myMobileMonitoring, bMonitoring);
		SetPreferenceStatus(myNuPiMax_Mob, bMonitoring && myMobileMonitoring.isChecked());
		SetPreferenceStatus(myMobileView, bMonitoring && myMobileMonitoring.isChecked());

		//SetPreferenceStatus(myCalls_Monitoring, bMonitoring);
		//SetPreferenceStatus(myNuPiMax_Calls, bMonitoring && myCalls_Monitoring.isChecked());
		//SetPreferenceStatus(myCalls_View, bMonitoring && myCalls_Monitoring.isChecked());
		//SetPreferenceStatus(myCalls_Integrating_SMS, bMonitoring && myCalls_Monitoring.isChecked());
		//SetPreferenceStatus(myCalls_FreePhoneNo,bMonitoring && myCalls_Monitoring.isChecked());

		Cpc_Utils.CurVal.bMobileDataCycle = bReference && bCategory && bMonitoring;
	}
	
	private void SetPreferenceStatus (Preference myPref, boolean Status )
	{
		myPref.setEnabled(Status);
		myPref.setSelectable(Status);
	}
	
	private String SetMonitoringResultData(boolean bCalls, Preference preference1, Preference preference2, Preference preference3, Preference preference4,boolean bStatus )
	{
		String sResult = getString(R.string.pref_Monitoring_SumOff);
		try
		{
			SetPreferenceStatus(preference1, bStatus);
			SetPreferenceStatus(preference2, bStatus);
			if (preference3 != null)
				SetPreferenceStatus(preference3, bStatus);
			if (preference4 != null)
				SetPreferenceStatus(preference4, bStatus);
			if (bStatus == Boolean.TRUE)
			{
				sResult = getString(R.string.pref_Monitoring_SumOn) ;
			}
		}
		catch (Exception e)
		{
			sResult = "Shit MonitoringResultData";
		}
		return sResult;
	}

} // End of class Cpc_Settings_Frag_Cost_Rel_Data





