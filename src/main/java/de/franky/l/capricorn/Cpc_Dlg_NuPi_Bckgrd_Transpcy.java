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

 public class Cpc_Dlg_NuPi_Bckgrd_Transpcy extends DialogPreference
{

    private int iTranspMax;
    private NumberPicker PickTranspValue;
    private final int iDef_ValMin_Max;


    public Cpc_Dlg_NuPi_Bckgrd_Transpcy(Context context, AttributeSet attrs)
    {
        super(context, attrs);


        // Log.d("Cpc_Dlg_NuPi_MobData_MaxVal","Cpc_Dlg_NuPi_MobData_MaxVal");
        String sDef_Val = context.getString(R.string.pref_BckgrdTransp_Default);
        iDef_ValMin_Max = Cpc_Utils.SaveParseInt(sDef_Val, 0);

        setDialogLayoutResource(R.layout.cpc_nupi_calls_val);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

    }


    @Override
    protected void onDialogClosed(boolean positiveResult)
    {

        // Log.d("onDialogClosed",String.valueOf(PickMaxDataValue.getValue()));
        PickTranspValue.clearFocus();
        if (positiveResult)
        {
        	long lMaxDataValueMin = PickTranspValue.getValue();
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
        int iMaxMinValueMax = 100;
        int iMaxMinValueMin = 0;

        long lPersistedLong;
        int iMaxDataPickerValueNeu;

        // Log.d("Cpc_Dlg_NuPi_MobData_RevVal","onCreateDialogView");

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cpc_nupi_calls_val, null);

        TextView mTV = view.findViewById(R.id.tv_nupi_maxcalls_explanation);
        mTV.setText(Cpc_Application.getContext().getResources().getString(R.string.pref_BckgrdTransp_Expl));
        mTV = view.findViewById(R.id.tv_nupi_maxvalcalls_unit);
        mTV.setText(Cpc_Application.getContext().getResources().getString(R.string.pref_BckgrdTransp_Unit));

        PickTranspValue = view.findViewById(R.id.nupi_Maxvalcalls_value);

        // Initialize state
        PickTranspValue.setMaxValue(iMaxMinValueMax);
        PickTranspValue.setMinValue(iMaxMinValueMin);

        // Log.d("onCreateDialogView",String.valueOf(iDef_ValMin_Max));
        lPersistedLong = this.getPersistedLong( (long) iDef_ValMin_Max);

        iMaxDataPickerValueNeu = (int) lPersistedLong;
        //Log.d("onCreateDialogView",String.valueOf(iMaxDataPickerValueNeu));
        PickTranspValue.setValue(iMaxDataPickerValueNeu);
        PickTranspValue.setWrapSelectorWheel(true);

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
        public static final Creator<SavedState> CREATOR =
                new Creator<Cpc_Dlg_NuPi_Bckgrd_Transpcy.SavedState>() {

            public Cpc_Dlg_NuPi_Bckgrd_Transpcy.SavedState createFromParcel(Parcel in) {
                return new Cpc_Dlg_NuPi_Bckgrd_Transpcy.SavedState(in);
            }

            public Cpc_Dlg_NuPi_Bckgrd_Transpcy.SavedState[] newArray(int size) {
                return new Cpc_Dlg_NuPi_Bckgrd_Transpcy.SavedState[size];
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
        myState.value = iTranspMax;
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
        PickTranspValue.setValue(myState.value);
        // Log.d("onRestoreInstanceState",String.valueOf(myState.value));
    }
}
