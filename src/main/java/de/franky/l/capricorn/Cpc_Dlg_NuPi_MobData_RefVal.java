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

 public class Cpc_Dlg_NuPi_MobData_RefVal extends DialogPreference
{

    private int iMobData;
    private NumberPicker PickMobDataValue;
    private final int iDef_Val;

    private NumberPicker PickMobDataUnit;

    public Cpc_Dlg_NuPi_MobData_RefVal(Context context, AttributeSet attrs) 
    {
        super(context, attrs);

        String sDef_Val = context.getString(R.string.pref_MobileRefVal_Default);
        iDef_Val = Cpc_Utils.SaveParseInt(sDef_Val, 0);
//        Log.d("NuPi_MobData_RefVal",String.valueOf(iDef_Val));

        setDialogLayoutResource(R.layout.cpc_nupi_data_val);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

    }
    
//    @Override
//    protected void onClick()
//    {
//        Log.d("Dlg_MobData_RefVal","clicked");
//    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {

        PickMobDataValue.clearFocus();
//        Log.d("onDialogClosed",String.valueOf(PickMobDataValue.getValue()));
        if (positiveResult)
        {
            long lMobDataValueByte = Cpc_Utils.lCalcDataValToByte(PickMobDataValue.getValue(),PickMobDataUnit.getValue());

        	persistLong(lMobDataValueByte);
            this.callChangeListener(lMobDataValueByte);
            
        }
    }
    
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) 
    {
      //  long lMobDataPersist = 0;
  //  	 Log.d("onSetIniatvalue 1",String.valueOf((Integer)defaultValue));
        if (restorePersistedValue) {
            // Restore existing state
    //         Log.d("onSetIniatvalue 2",String.valueOf(this.getPersistedLong(0)));
        //    lMobDataPersist = this.getPersistedLong((long)iDef_Val);
      //       Log.d("onSetIniatvalue 3",String.valueOf(lMobDataPersist));
        } else {
            // Set default state from the XML attribute
        	iMobData = (Integer) defaultValue;
            persistLong(iMobData);
        }
    }


    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) 
    {
    //     Log.d("NuPi_onGetDefaultValue",String.valueOf(iDef_Val));
    	return a.getInteger(index,  iDef_Val);
    }
    
    
    @Override
    protected View onCreateDialogView() 
    {
        String[] sUnitValues;
        sUnitValues = Cpc_Application.getContext().getResources().getStringArray(R.array.pref_DataUnits);

        int iMobdataDataMax = 9999;
        int iMobdataDataMin = 0;

        int iMobdataUnitMax = sUnitValues.length-1;
        int iMobdataUnitMin = 0;
        long lPersistedLong;
        int iMobdataPickerValueNeu;
        int iMobdataPickerUnitNeu;

    //     Log.d("Cpc_Dlg_NuPi_MobData_RevVal","onCreateDialogView");

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cpc_nupi_data_val, null);

        TextView mTV = view.findViewById(R.id.tv_nupi_refval_explanation);
        mTV.setText(Cpc_Application.getContext().getResources().getString(R.string.pref_MobileRefVal_Explanation));

        PickMobDataValue = view.findViewById(R.id.nupi_refval_value);

        // Initialize state
        PickMobDataValue.setMaxValue(iMobdataDataMax);
        PickMobDataValue.setMinValue(iMobdataDataMin);
        
        // Log.d("onCreateDialogView",String.valueOf(iDef_Val));
        lPersistedLong = this.getPersistedLong( (long)iDef_Val);
        iMobdataPickerValueNeu = Cpc_Utils.iCalcDataVal(lPersistedLong);
        iMobdataPickerUnitNeu  = Cpc_Utils.iCalcDataUnitIndex(lPersistedLong);

    //    Log.d("onCreateDialogView",String.valueOf(iMobdataPickerValueNeu));
        PickMobDataValue.setValue(iMobdataPickerValueNeu);
        PickMobDataValue.setWrapSelectorWheel(true);

        PickMobDataUnit = view.findViewById(R.id.nupi_refval_unit);

        // Initialize state
        PickMobDataUnit.setMaxValue(iMobdataUnitMax);
        PickMobDataUnit.setMinValue(iMobdataUnitMin);
        PickMobDataUnit.setValue(iMobdataPickerUnitNeu);
        PickMobDataUnit.setWrapSelectorWheel(true);  // Works only on SKD >=16,

        PickMobDataUnit.setDisplayedValues(sUnitValues);


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
        myState.value = iMobData;
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
        PickMobDataValue.setValue(myState.value);
    }
}
