package de.franky.l.capricorn;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

import static de.franky.l.capricorn.Cpc_Utils.SaveParseInt;


/**
 * Created by franky on 03.12.2017.
 */

public class Cpc_SharePref
//public class Cpc_SharePref implements SharedPreferences.OnSharedPreferenceChangeListener
{

    private SharedPreferences Cpc_Pref;
    private SharedPreferences.Editor Cpc_Editor;
    private Context cpc_Ctxt;



//
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
//    {
//        Log.d("onSharedPrefChanged",key);
//    }


    Cpc_SharePref()
    {
        cpc_Ctxt = Cpc_Application.getContext();
        Cpc_Pref = PreferenceManager.getDefaultSharedPreferences(cpc_Ctxt);
        Cpc_Editor = Cpc_Pref.edit();
        //Cpc_Pref.registerOnSharedPreferenceChangeListener(this);
    }


    double getDbl(String sSearch, double dDefault)
    {
        double dResult;
        try
        {
            dResult = java.lang.Double.longBitsToDouble(Cpc_Pref.getLong(sSearch, java.lang.Double.doubleToRawLongBits(dDefault))); // Hole boole Wert aus sharedpreference
        }
        catch  (Exception e)
        {
            dResult = dDefault;													// Falls es kracht (ich weiss zwar nicht warum) setze Defaultwert
        }
        return dResult;
    }

    void putDbl(String sSearch, double dVal)
    {
        try
        {
            Cpc_Editor.putLong(sSearch, java.lang.Double.doubleToRawLongBits(dVal)); 	                // Setze double Wert in sharedpreference
            Cpc_Editor.apply();
        }
        catch  (Exception e)
        {
            Log.e("Cpc_SharePref","putDbl Problem "+sSearch + " : " +String.valueOf(dVal));		// Falls es kracht (ich weiss zwar nicht warum) error loggen
        }
    }

    int getInt_R_ID(int RIdSearch, int RIdDefault)
    {
        int    iResult;
        int    iDef_Val = 0;
        String sDef_Val;

        if (RIdDefault > 0)
        {
            sDef_Val = cpc_Ctxt.getString(RIdDefault);
            iDef_Val = SaveParseInt(sDef_Val, -7654);						// Mache einen Integerwert draus
        }

        try
        {
            iResult = getInt(RIdSearch, iDef_Val );
        }
        catch  (Exception e)
        {
            iResult = iDef_Val;														// Falls es kracht (ich weiss zwar nicht warum) setze Defaultwert
        }
        return iResult;

    }

    int getInt(int iSearch, int iDefault)
    {
        return getInt(cpc_Ctxt.getString(iSearch),iDefault);
    }

    int getInt(String sSearch, int iDefault)
    {
        int iResult;
        try
        {
            iResult = Cpc_Pref.getInt(sSearch, iDefault); 		  		 	    // Hole boole Wert aus sharedpreference
        }
        catch  (Exception e)
        {
            iResult = iDefault;													// Falls es kracht (ich weiss zwar nicht warum) setze Defaultwert
        }
        return iResult;
    }


    void putInt(String sSearch, int iVal)
    {
        try
        {
            Cpc_Editor.putInt(sSearch, iVal); 				 	                // Setze boole Wert in sharedpreference
            Cpc_Editor.apply();
        }
        catch  (Exception e)
        {
            Log.e("Cpc_SharePref","putInt Problem "+sSearch + " : " +String.valueOf(iVal));													// Falls es kracht (ich weiss zwar nicht warum) error loggen
        }
    }

    boolean getBool(int iSearch, boolean bDefault)
    {
        return getBool(cpc_Ctxt.getString(iSearch),bDefault);
    }

    boolean getBool(String sSearch, boolean bDefault)
    {
        boolean bResult;
        try
        {
            bResult = Cpc_Pref.getBoolean(sSearch, bDefault); 				 	// Hole boole Wert aus sharedpreference
        }
        catch  (Exception e)
        {
            bResult = bDefault;													// Falls es kracht (ich weiss zwar nicht warum) setze Defaultwert
        }
        return bResult;
    }
    void putBool(String sSearch, boolean bVal)
    {
        try
        {
            Cpc_Editor.putBoolean(sSearch, bVal); 				 	            // Setze boole Wert in sharedpreference
            Cpc_Editor.apply();
        }
        catch  (Exception e)
        {
            Log.e("Cpc_SharePref","putBool Problem "+sSearch + " : " +String.valueOf(bVal));		// Falls es kracht (ich weiss zwar nicht warum) error loggen
        }
    }
    void putString(String sSearch, String sVal)
    {
        try
        {
            Cpc_Editor.putString(sSearch, sVal); 				 	            // Setze String Wert in sharedpreference
            Cpc_Editor.apply();
        }
        catch  (Exception e)
        {
            Log.e("Cpc_SharePref","putString Problem "+sSearch + " : " +sVal);	// Falls es kracht (ich weiss zwar nicht warum) error loggen
        }
    }
    String getString(String sSearch, String sDefault)
    {
        String sResult;
        try
        {
            sResult = Cpc_Pref.getString(sSearch, sDefault); 				 	// Hole String aus sharedpreference
        }
        catch  (Exception e)
        {
            sResult = sDefault;													// Falls es kracht (ich weiss zwar nicht warum) setze Defaultwert
        }
        return sResult;
    }
    String getString(int iSearch, int iDefault)
    {
        return getString(cpc_Ctxt.getString(iSearch),cpc_Ctxt.getString(iDefault));
    }
    void putLong(String sSearch, long lVal)
    {
        try
        {
            Cpc_Editor.putLong(sSearch, lVal); 				 	                // Setze boole Wert in sharedpreference
            Cpc_Editor.apply();
        }
        catch  (Exception e)
        {
            Log.e("Cpc_SharePref","putLong Problem with: "+sSearch + " : " +String.valueOf(lVal));		// Falls es kracht (ich weiss zwar nicht warum) error loggen
        }
    }

    long getLong(int iR_Search, long lDefault)
    {
        return getLong(cpc_Ctxt.getString(iR_Search),lDefault);
    }

    long getLong(String sSearch, long lDefault)
    {
        long lResult;
        try
        {
            lResult = Cpc_Pref.getLong(sSearch, lDefault); 		  		 	    // Hole boole Wert aus sharedpreference
        }
        catch  (Exception e)
        {
            lResult = lDefault;													// Falls es kracht (ich weiss zwar nicht warum) setze Defaultwert
        }
        return lResult;
    }
    long getLong(int iR_Search, int iRDefault)
    {
        long   lDef_Val = iRDefault;
        if (iRDefault > 0)
        {
            lDef_Val = SaveParseInt(cpc_Ctxt.getString(iRDefault), -7654);						// Mache einen Integerwert draus
        }
        return getLong(iR_Search, lDef_Val);
    }
    public boolean putStringArray(String sSearch, ArrayList<String> array) {
        try
        {
            Cpc_Editor.putInt(sSearch +"_size", array.size());
            for(int i=0 ; i < array.size() ; i++) {
                Cpc_Editor.putString(sSearch + "_" + i, array.get(i));
            }
            Cpc_Editor.apply();
            return true;
        }
        catch  (Exception e)
        {
            Log.e("Cpc_SharePref","putStringArray Problem "+sSearch + " : " +String.valueOf(array));													// Falls es kracht (ich weiss zwar nicht warum) error loggen
            return false;
        }
    }

    public ArrayList<String> getStringArray(String sR_Search) {
        ArrayList<String> sReturn = new ArrayList<String>();
        int size = Cpc_Pref.getInt(sR_Search + "_size", 0);
        for (int i = 0; i < size; i++) {
            sReturn.add( Cpc_Pref.getString(sR_Search + "_" + i, "noNumber"));
        }
        return sReturn;
    }
}
