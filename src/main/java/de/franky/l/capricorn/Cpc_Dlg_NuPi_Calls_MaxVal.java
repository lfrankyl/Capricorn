 package de.franky.l.capricorn;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

 public class Cpc_Dlg_NuPi_Calls_MaxVal extends DialogPreference
{

    private int iCallsMax;
    private NumberPicker PickCallsValue;
    private final int iDef_ValMin_Max;


    public Cpc_Dlg_NuPi_Calls_MaxVal(Context context, AttributeSet attrs) 
    {
        super(context, attrs);


        // Log.d("Cpc_Dlg_NuPi_MobData_MaxVal","Cpc_Dlg_NuPi_MobData_MaxVal");
        String sDef_Val = context.getString(R.string.pref_Calls_MaxVal_Default);
        iDef_ValMin_Max = Cpc_Utils.SaveParseInt(sDef_Val, 0);
        
        setDialogLayoutResource(R.layout.cpc_nupi_calls_val);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

    }
    
 
    @Override
    protected void onDialogClosed(boolean positiveResult) 
    {

        // Log.d("onDialogClosed",String.valueOf(PickMaxDataValue.getValue()));
        PickCallsValue.clearFocus();
        if (positiveResult)
        {
        	long lMaxDataValueMin = PickCallsValue.getValue();
        	persistLong(lMaxDataValueMin);
            this.callChangeListener(lMaxDataValueMin);
            
        }
    }
    
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) 
    {
        if (!restorePersistedValue)
        {
            // Set default state from the XML attribute
            persistLong(iDef_ValMin_Max);
        }
    }


    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) 
    {
    	// Log.d("onGetDefaultValue",String.valueOf(iDef_ValMin_Max));
    	return a.getInteger(index,  iDef_ValMin_Max);
    }
    
    
    @Override
    protected View onCreateDialogView() 
    {
        int iMaxMinValueMax = 1000;
        int iMaxMinValueMin = 0;

        long lPersistedLong;
        int iMaxDataPickerValueNeu;

        // Log.d("Cpc_Dlg_NuPi_MobData_RevVal","onCreateDialogView");

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cpc_nupi_calls_val, null);

        TextView mTV = view.findViewById(R.id.tv_nupi_maxcalls_explanation);
        mTV.setText(Cpc_Application.getContext().getResources().getString(R.string.pref_Calls_MaxVal_Expl));

        PickCallsValue = view.findViewById(R.id.nupi_Maxvalcalls_value);

        // Initialize state
        PickCallsValue.setMaxValue(iMaxMinValueMax);
        PickCallsValue.setMinValue(iMaxMinValueMin);
        
        // Log.d("onCreateDialogView",String.valueOf(iDef_ValMin_Max));
        lPersistedLong = this.getPersistedLong( (long) iDef_ValMin_Max);

        iMaxDataPickerValueNeu = (int) lPersistedLong;
        //Log.d("onCreateDialogView",String.valueOf(iMaxDataPickerValueNeu));
        PickCallsValue.setValue(iMaxDataPickerValueNeu);
        PickCallsValue.setWrapSelectorWheel(true);

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
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
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
        myState.value = iCallsMax;
        // Log.d("onSaveInstanceState",String.valueOf(iCallsMax));
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
        PickCallsValue.setValue(myState.value);
        // Log.d("onRestoreInstanceState",String.valueOf(myState.value));
    }
}
