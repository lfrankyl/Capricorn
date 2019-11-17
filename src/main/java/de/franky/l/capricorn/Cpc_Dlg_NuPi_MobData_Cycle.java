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

public class Cpc_Dlg_NuPi_MobData_Cycle extends DialogPreference 
{

    private int iMobData;
    private NumberPicker semPick;
    private final int iDef_Val;

    public Cpc_Dlg_NuPi_MobData_Cycle(Context context, AttributeSet attrs) 
    {
        super(context, attrs);

        String sDef_Val = context.getString(R.string.pref_MobileCycle_Default);
        iDef_Val = Cpc_Utils.SaveParseInt(sDef_Val, 0);

        setDialogLayoutResource(R.layout.cpc_nupi_mobdata_cycle);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

    }
    
 
    @Override
    protected void onDialogClosed(boolean positiveResult) {

        semPick.clearFocus();
        if (positiveResult)
        {
            persistInt(semPick.getValue());
            this.callChangeListener(semPick.getValue());
        }
    }
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            iMobData = this.getPersistedInt(iDef_Val);
        } else {
            // Set default state from the XML attribute
        	iMobData = (Integer) defaultValue;
            persistInt(iMobData);
        }
        // Log.d("SetIniatvalue",String.valueOf(iMobData));
    }


    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) 
    {
        return a.getInteger(index,  iDef_Val);
    }
    
    
    @Override
    protected View onCreateDialogView() 
    {

        int max = 31;
        int min = 1;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cpc_nupi_mobdata_cycle, null);

        semPick = view.findViewById(R.id.nupi_cycle);

        // Initialize state
        semPick.setMaxValue(max);
        semPick.setMinValue(min);
        semPick.setValue(this.getPersistedInt(iDef_Val));
        semPick.setWrapSelectorWheel(true);

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
        semPick.setValue(myState.value);
    }
}
