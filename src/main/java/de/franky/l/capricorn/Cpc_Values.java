package de.franky.l.capricorn;

import java.util.ArrayList;
import java.util.Date;

public class Cpc_Values 
{
	final String[] sViewOptions;								// Kopie aus Arrays ViewOptions um sie ueberall verfuegbar zu machen
	final String[] sViewOptions_Val;							// Kopie aus Arrays ViewOptions_Val um sie ueberall verfuegbar zu machen
	final int[] iRDrawable;										// Speichert die R-Werte der Icons im Array um per Index zugreifen zu koennen
	boolean bMobileDataCycle;									// Flag ob eigener Zyklus ausgwaehlt wurde
	boolean bMobileMonitoring;									// Flag ob Monitoring mobile Daten ausgewaehlt wurde
	boolean bMobileMonitoringOk;								// Flag ob Datennutzung im Rahmen ist Anzeige ob gruen oder rot
	int     iMobileCycleTyp;									// Flag ob monatlicher, 30Tage oder 28Tage Zyklus 0=monatlich, 1=30 Tage, 2=28 Tage
	boolean bMobileViewTyp;										// Ansicht verbrauchte (0) Daten oder noch uebrige (1) Daten
	double dMobileDayMax;										// Wert der maximal uebertragenden mobilen Daten pro Tag
	double dMobileDayCur;										// Aktueller Durchschnitt der uebertragenen mobilen Daten pro Tag
	double dMobileTodayMax;										// Wert der bis heute verbraucht haette werden koennen
	boolean bCallsMonitoring;									// Flag ob Monitoring Anrufe ausgewaehlt wurde
	boolean bCallsMonitoringOk;									// Flag ob Anrufe im Rahmen ist Anzeige ob gruen oder rot
	int    iCallsNumMissed;							 			// Anzahl der verpassten Anrufe
	int    iCallsNumIncoming;									// Anzahl der ausgehenden Anrufe
	int    iCallsNumOutgoing;									// Anzahl der ankommenden Anrufe
	int    iSMSNumSent;											// Anzahl gesendeter SMS
	double dCallsMinIncoming;									// Summe der Minuten der ankommenden Anrufe
	double dCallsMinOutgoingDayMax;								// Minuten die mann max. pro Tag anrufen kann
	double dCallsMinOutgoingDayCur;                             // Aktueller Durchschnitt der ausgehenden Anrufe pro Tag
	double dCallsMinOutgoingTodayMax;                           // Ausgehende Anrufe die man bis heute haette machen koennen
	boolean bCallsViewTyp;										// Ansicht verbrauchter (0) Anrufe oder noch uebrige (1) Anrufe
	boolean bCallsIntegrateSMS;									// Sollen SMS mit in die Freiminuten mit eingerechnet werden oder nicht
	int    iCycleDayOne;										// Tag 1 des Zyklus bezogen auf Jahr
	int    iCycleLength;										// Laenge des Zyklus entweder 30 Tage oder Laenge des entsprechenden Monats
	int    iCycleDone;										    // Bereits abgelaufene Tage im aktuelle Zyklus 
	Date   DateOfChange;										// Tag an dem ein neuer Abrechnungszeitraum beginnt
	Date   DateOfBegin;											// Tag an dem der aktuelle Abrechnungszeitraum began
	int    NoSmlWdgt;											// Anzahl der small widgets die laufen
	int    NoMedWdgt;											// Anzahl der medium widgets die laufen
	int    NoBigWdgt;											// Anzahl der big widgets die laufen
	int    iBatCharging;										// Merker ob Akku geladen wird oder nicht
	private boolean bMobResetIsLocked = false;					// Flag zu Verriegelung des Resets der mobilen Daten, wenn Zyklus vorbei ist. Sonst passiert das immer so lange der Tag nicht vorbei ist.
	boolean bIsReceiverRegistered;								// Flag um festzustellen ob der Broadcastreceiver "WidgetIntentreceiver" bereits registriert ist
	boolean bIsUserPresentnotified;								// Flag Versuch um zu erkennen ob Intent User_present durchgekommen ist speziell für Oreo

	final Cpc_DispData[] DispData;
	ArrayList<String> FreePhoneNo;								// Array mit den kostenlosen Telefonnummern


	Cpc_Values()
	{
		NoSmlWdgt = 0;
		NoMedWdgt = 0;
		NoBigWdgt = 0;
		bMobileDataCycle = false;
		bMobileMonitoring = false;
		bMobileMonitoringOk = false;
		iMobileCycleTyp     = 0;                     // monthly = default
		dMobileDayMax = 0;
		dMobileDayCur = 0;
		dMobileTodayMax = 0;
		bCallsMonitoring = false;
		bCallsMonitoringOk = false;
		iCallsNumMissed = 0;
		iCallsNumIncoming = 0;
		iCallsNumOutgoing = 0;
		iCycleDayOne      = 0;
		iCycleLength      = 0;
		iCycleDone		  = 0;
		iBatCharging      = 0;
		bIsReceiverRegistered = false;
		bIsUserPresentnotified = false;
    	sViewOptions = Cpc_Application.getContext().getResources().getStringArray(R.array.viewoptions);
    	sViewOptions_Val = Cpc_Application.getContext().getResources().getStringArray(R.array.viewoptions_Values);
		iRDrawable = new int[sViewOptions_Val.length];
		iRDrawable[0] = R.drawable.cpc_cpu_arm_frag;
		iRDrawable[1] = R.drawable.cpc_ram_frag;
		iRDrawable[2] = R.drawable.cpc_sd_intern_frag;
		iRDrawable[3] = R.drawable.cpc_sd_extern_frag;
		iRDrawable[4] = R.drawable.cpc_bat_100_frag;
		iRDrawable[5] = R.drawable.cpc_mobdata_gen_frag;
		iRDrawable[6] = R.drawable.cpc_wlan_frag;
		iRDrawable[7] = R.drawable.cpc_bright;
		iRDrawable[8] = R.drawable.cpc_phone_gen_frag;

		FreePhoneNo = new ArrayList<String>();

		DispData = new Cpc_DispData[sViewOptions_Val.length +3 ];  // +3 sind die max Werte der Std. Speicher
		for (int i=0; i<DispData.length; i++)
		{
			DispData[i] = new  Cpc_DispData();
			DispData[i].setValue(Cpc_Application.getContext().getResources().getString(R.string.NotAvailable));
			DispData[i].setUnit(Cpc_Application.getContext().getResources().getString(R.string.blankChr));
			DispData[i].setNumber(0);
		}
		DispData[0].setIcon(R.drawable.cpc_cpu_arm);                // CPU
		DispData[1].setIcon(R.drawable.cpc_ram);                    // RAM verfügbar
		DispData[2].setIcon(R.drawable.cpc_sd_intern);              // interne SD-Karte [0] verfügbar
		DispData[3].setIcon(R.drawable.cpc_sd_extern);              // externe SD-Karte [1] verfügbar
		DispData[4].setIcon(R.drawable.cpc_bat_100);                // Akku
		DispData[5].setIcon(R.drawable.cpc_mobdata_gen);            // Mobile Datem generell (ohne grün/rot)
		DispData[6].setIcon(R.drawable.cpc_wlan);                   // WLAN
		DispData[7].setIcon(R.drawable.cpc_bright);                 // Helligkeit
		DispData[8].setIcon(R.drawable.cpc_phone_gen);              // Anrufe generell (ohne grün/rot)
																	// 08/09: RAM insgesamt
																	// 09/10: interne SD-Karte [0] insgesamt
																	// 10/11: externe SD-Karte [1] verfügbar

	}
	
	void bMobResetLocked(boolean bValue)
	{
		bMobResetIsLocked = bValue;
	}
	boolean bMobResetLocked()
	{
		return bMobResetIsLocked;
	}

}
