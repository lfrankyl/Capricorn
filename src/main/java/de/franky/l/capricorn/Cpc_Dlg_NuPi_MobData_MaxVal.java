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

 public class Cpc_Dlg_NuPi_MobData_MaxVal extends DialogPreference
{

    private int iMobDataMax;
    private NumberPicker PickMaxDataValue;
    private final int iDef_Val_Max;

    private NumberPicker PickMaxDataUnit;

    public Cpc_Dlg_NuPi_MobData_MaxVal(Context context, AttributeSet attrs) 
    {
        super(context, attrs);


        // Log.d("Cpc_Dlg_NuPi_MobData_MaxVal","Cpc_Dlg_NuPi_MobData_MaxVal");
        String sDef_Val = context.getString(R.string.pref_MobileMaxVal_Default);
        iDef_Val_Max = Cpc_Utils.SaveParseInt(sDef_Val, 0);
        
        setDialogLayoutResource(R.layout.cpc_nupi_data_val);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

    }
    
 
    @Override
    protected void onDialogClosed(boolean positiveResult) 
    {

        // Log.d("onDialogClosed",String.valueOf(PickMaxDataValue.getValue()));
        PickMaxDataValue.clearFocus();
        if (positiveResult)
        {
            long lMaxDataValueByte = Cpc_Utils.lCalcDataValToByte(PickMaxDataValue.getValue(),PickMaxDataUnit.getValue());

        	persistLong(lMaxDataValueByte);
            this.callChangeListener(lMaxDataValueByte);
            
        }
    }
    
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) 
    {
        if (!restorePersistedValue)
        {
            persistLong(iDef_Val_Max);
        }
    }


    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) 
    {
    	// Log.d("onGetDefaultValue",String.valueOf(iDef_Val_Max));
    	return a.getInteger(index,  iDef_Val_Max);
    }
    
    
    @Override
    protected View onCreateDialogView() 
    {
        String[] sUnitValues;
        sUnitValues = Cpc_Application.getContext().getResources().getStringArray(R.array.pref_DataUnits);

        int iMaxDataValueMax = 9999;
        int iMaxDataValueMin = 0;

        int iMaxDataUnitMax = sUnitValues.length-1;
        int iMaxDataUnitMin = 0;
        long lPersistedLong;
        int iMaxDataPickerValueNeu;
        int iMaxDataPickerUnitNeu;

        // Log.d("Cpc_Dlg_NuPi_MobData_RevVal","onCreateDialogView");

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cpc_nupi_data_val, null);

        TextView mTV = view.findViewById(R.id.tv_nupi_refval_explanation);
        mTV.setText(Cpc_Application.getContext().getResources().getString(R.string.pref_MobileMaxVal_Expl));

        PickMaxDataValue = view.findViewById(R.id.nupi_refval_value);

        // Initialize state
        PickMaxDataValue.setMaxValue(iMaxDataValueMax);
        PickMaxDataValue.setMinValue(iMaxDataValueMin);
        
        // Log.d("onCreateDialogView",String.valueOf(iDef_Val_Max));
        lPersistedLong = this.getPersistedLong( (long) iDef_Val_Max);
        iMaxDataPickerValueNeu = Cpc_Utils.iCalcDataVal(lPersistedLong);
        iMaxDataPickerUnitNeu  = Cpc_Utils.iCalcDataUnitIndex(lPersistedLong);

        //Log.d("onCreateDialogView",String.valueOf(iMaxDataPickerValueNeu));
        PickMaxDataValue.setValue(iMaxDataPickerValueNeu);
        PickMaxDataValue.setWrapSelectorWheel(true);

        PickMaxDataUnit = view.findViewById(R.id.nupi_refval_unit);

        // Initialize state
        PickMaxDataUnit.setMaxValue(iMaxDataUnitMax);
        PickMaxDataUnit.setMinValue(iMaxDataUnitMin);
        PickMaxDataUnit.setValue(iMaxDataPickerUnitNeu);
        PickMaxDataUnit.setWrapSelectorWheel(true);  // Works only on SKD >=16,

        PickMaxDataUnit.setDisplayedValues(sUnitValues);          

        return view;
    }


    //  This code copied from android's settings guide.
    private static class SavedState extends BaseSavedState 
    {
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
    protected Parcelable onSaveInstanceState() 
    {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) 
        {
            // No need to save instance state since it's persistent, use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current setting value
        myState.value = iMobDataMax;
        // Log.d("onSaveInstanceState",String.valueOf(iMobDataMax));
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
        PickMaxDataValue.setValue(myState.value);
        // Log.d("onRestoreInstanceState",String.valueOf(myState.value));
    }
}
