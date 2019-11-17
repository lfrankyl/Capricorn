 package de.franky.l.capricorn;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

 public class Cpc_Dlg_NuPi_WLAN_RefVal extends DialogPreference
{

    private int  iwlanData;
    private NumberPicker PickwlanData;
    private final int  iwlanDataDefVal;
    
    private NumberPicker PickwlanUnit;

    public Cpc_Dlg_NuPi_WLAN_RefVal(Context context, AttributeSet attrs) 
    {
        super(context, attrs);

        // Log.d("Cpc_Dlg_NuPi_WLAN_RefVal","Cpc_Dlg_NuPi_WLAN_RefVal");
        String sDef_Val = context.getString(R.string.pref_wlanRefVal_Default);
        iwlanDataDefVal = Cpc_Utils.SaveParseInt(sDef_Val, 0);
        
        setDialogLayoutResource(R.layout.cpc_nupi_data_val);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

    }
    
 
    @Override
    protected void onDialogClosed(boolean positiveResult) {

        // Log.d("onDialogClosed",String.valueOf(PickwlanData.getValue()));
        PickwlanData.clearFocus();
        if (positiveResult) 
        {
            long lwlanValueByte = Cpc_Utils.lCalcDataValToByte(PickwlanData.getValue(),PickwlanUnit.getValue());

        	persistLong(lwlanValueByte);
            this.callChangeListener(lwlanValueByte);
            
        }
    }
    
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) 
    {
        if (!restorePersistedValue)
        {
            persistLong(iwlanDataDefVal);
        }
    }


    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) 
    {
        // Log.d("Cpc_Dlg_NuPi_MobData_RevVal","onGetDefaultValue");
        return a.getInteger(index,  iwlanDataDefVal);
    }
    
    
    @Override
    protected View onCreateDialogView() 
    {
        String[] sUnitValues;
        sUnitValues = Cpc_Application.getContext().getResources().getStringArray(R.array.pref_DataUnits);

        int iWlanDataMax = 9999;
        int iWlanDataMin = 0;

        int iWlanUnitMax = sUnitValues.length-1;
        int iWlanUnitMin = 0;
        long lPersistedLong;
        int iWlanPickerValueNeu;
        int iwlanPickerUnitNeu;

        // Log.d("Cpc_Dlg_NuPi_MobData_RevVal","onCreateDialogView");

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cpc_nupi_data_val, null);

        TextView mTV = view.findViewById(R.id.tv_nupi_refval_explanation);
        mTV.setText(Cpc_Application.getContext().getResources().getString(R.string.pref_wlanRefVal_Explanation));

        PickwlanData = view.findViewById(R.id.nupi_refval_value);

        // Initialize state
        PickwlanData.setMaxValue(iWlanDataMax);
        PickwlanData.setMinValue(iWlanDataMin);
        
        // Log.d("onCreateDialogView",String.valueOf(iwlanDataDefVal));
        lPersistedLong = this.getPersistedLong( (long)iwlanDataDefVal);
        iWlanPickerValueNeu = Cpc_Utils.iCalcDataVal(lPersistedLong);
        iwlanPickerUnitNeu  = Cpc_Utils.iCalcDataUnitIndex(lPersistedLong);

        //Log.d("onCreateDialogView",String.valueOf(iWlanPickerValueNeu));
        PickwlanData.setValue(iWlanPickerValueNeu);
        PickwlanData.setWrapSelectorWheel(true);

        PickwlanUnit = view.findViewById(R.id.nupi_refval_unit);

        // Initialize state
        PickwlanUnit.setMaxValue(iWlanUnitMax);
        PickwlanUnit.setMinValue(iWlanUnitMin);
        PickwlanUnit.setValue(iwlanPickerUnitNeu);
        PickwlanUnit.setWrapSelectorWheel(true);  // Works only on SKD >=16,
        PickwlanUnit.setDisplayedValues(sUnitValues);          

        return view;
    }


    //  This code copied from android's settings guide.
    private static class SavedState extends BaseSavedState {
        // Member that holds the setting's value
        // Change this data type to match the type saved by your Preference
        int value;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            // Get the current preference's value
            value = source.readInt();  // Change this to read the appropriate data type
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            // Write the preference's value
            dest.writeInt(value);  // Change this to write the appropriate data type
        }

        // Standard creator object using an instance of this class
//        public static final Parcelable.Creator<SavedState> CREATOR =
//                new Parcelable.Creator<SavedState>() {
//
//            public SavedState createFromParcel(Parcel in) {
//                return new SavedState(in);
//            }
//
//            public SavedState[] newArray(int size) {
//                return new SavedState[size];
//            }
//        };
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) {
            // No need to save instance state since it's persistent, use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current setting value
        myState.value = iwlanData;
        return myState;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // Set this Preference's widget to reflect the restored state
        PickwlanData.setValue(myState.value);
    }
}
