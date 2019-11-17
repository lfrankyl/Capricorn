package de.franky.l.capricorn;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import static de.franky.l.capricorn.Cpc_Utils.CpcPref;

public class Cpc_Settings_Free_PhoneNumber extends AppCompatActivity {
    String sNewPhoneNumber;
    //private ViewGroup Layout_Container;
    FloatingActionButton fab_Add;
    FloatingActionButton fab_Delete;
    FloatingActionButton fab_Info;
    TextView tv_Clone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpc_setting_free_phone_number);
        Cpc_Utils.changeTextSize((ViewGroup) findViewById(R.id.ll_freePhoneNo));

        tv_Clone = (TextView) findViewById(R.id.tv_Clone);
        fab_Add = (FloatingActionButton) findViewById(R.id.fab_Add);
        fab_Delete = (FloatingActionButton) findViewById(R.id.fab_Delete);
        fab_Info = (FloatingActionButton) findViewById(R.id.fab_Info);
        fab_Add.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View view) {
                                           showAddItemDialog(Cpc_Settings_Free_PhoneNumber.this,"",null);
                                       }
                                   }
        );
        fab_Delete.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View view) {
                                           DeleteItemDialog(Cpc_Settings_Free_PhoneNumber.this,"",null);
                                       }
                                   }
        );
        fab_Info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                InfoDialog(Cpc_Settings_Free_PhoneNumber.this,"",null);
            }
        });
        getPhoneNumbersFromprefListArrayString();
        setPhoneNumbersToStringAndFillTextViews((ViewGroup) findViewById(R.id.ll_PhoneNo));

    }

    @Override
    public void onStart ()
    {
        super.onStart();
//        Log.d("Cpc_Settings_Free_PhoneNumber","onStart");
        //Cpc_Utils.changeTextSize(Layout_Container);
    }

    @Override
    protected void onPause(){
        // Log.d("Cpc_Settings_Free_PhoneNumber","onPause");
        super.onPause();
        putPhoneNumbersToArrayString((ViewGroup) findViewById(R.id.ll_PhoneNo));
    }

    private void getPhoneNumbersFromprefListArrayString(){
        Cpc_Utils.CurVal.FreePhoneNo = CpcPref.getStringArray(getString(R.string.pref_Calls_FreeNumbArr_Key));
    }

    private void setPhoneNumbersToStringAndFillTextViews(ViewGroup root){
        if(((LinearLayout) root).getChildCount() > 0)
            ((LinearLayout) root).removeAllViews();
        for (int i = 0; i < Cpc_Utils.CurVal.FreePhoneNo.size(); i++) {
            setPhoneNumberInTextView(Cpc_Utils.CurVal.FreePhoneNo.get(i),null,true);
        }
    }
    private void putPhoneNumbersToArrayString(ViewGroup root){
        Cpc_Utils.CurVal.FreePhoneNo.clear();
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView )
            {
                Cpc_Utils.CurVal.FreePhoneNo.add(((TextView) v).getText().toString());
            }
        }
        CpcPref.putStringArray(getString(R.string.pref_Calls_FreeNumbArr_Key),Cpc_Utils.CurVal.FreePhoneNo);
        CpcPref.putInt(getString(R.string.pref_Calls_FreeNumb_Key),Cpc_Utils.CurVal.FreePhoneNo.size());
    }

    private void DeleteItemDialog(Context c, String sText, final TextView vTextView) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(R.string.pref_Calls_FreeNumb_Title_Delete);

        String[] strArray = new String[Cpc_Utils.CurVal.FreePhoneNo.size()];
        final boolean[] checkedItems = new boolean[Cpc_Utils.CurVal.FreePhoneNo.size()];
        for(int i=0 ; i< strArray.length;i++){
            strArray[i] = Cpc_Utils.CurVal.FreePhoneNo.get(i);
            checkedItems[i] = Boolean.FALSE;
        }
        // add a checkbox list
        builder.setMultiChoiceItems(strArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                checkedItems[which] = isChecked;
            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton(R.string.pref_Calls_FreeNumb_Apply, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                for(int i=Cpc_Utils.CurVal.FreePhoneNo.size()-1 ; i>=0 ;i--){
                    if(checkedItems[i]){
                        Cpc_Utils.CurVal.FreePhoneNo.remove(i);
                    }
                }
                ViewGroup llPhoNo =  (ViewGroup) findViewById(R.id.ll_PhoneNo);
                setPhoneNumbersToStringAndFillTextViews(llPhoNo);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAddItemDialog(Context c, String sText, final TextView vTextView) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        taskEditText.setText(sText);
        AlertDialog dialog = new AlertDialog.Builder(c)
            .setTitle(R.string.pref_Calls_FreeNumb_Title)
            .setMessage(R.string.pref_Calls_FreeNumb_Task)
            .setView(taskEditText)
            .setPositiveButton(R.string.pref_Calls_FreeNumb_Apply, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sNewPhoneNumber = String.valueOf(taskEditText.getText());
                    setPhoneNumberInTextView(sNewPhoneNumber, vTextView,false);
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    private void InfoDialog(Context c, String sText, final TextView vTextView) {
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(R.string.pref_Calls_FreeNumb_Title)
                .setMessage(getString(R.string.pref_Calls_FreeNumb_Info1) + "\n\n" +
                            getString(R.string.pref_Calls_FreeNumb_Info2) + "\n\n" +
                            getString(R.string.pref_Calls_FreeNumb_Info3))
                .setPositiveButton(android.R.string.ok, null)
                .create();
        dialog.show();
    }

    private void setPhoneNumberInTextView(final String sNewPhoneNo, TextView tv_PhoneNo, boolean Init){
        LinearLayout ll_PhNo = (LinearLayout) findViewById(R.id.ll_PhoneNo);
        if (tv_PhoneNo == null) {
            tv_PhoneNo = new TextView(Cpc_Settings_Free_PhoneNumber.this);
            tv_PhoneNo.setLayoutParams(tv_Clone.getLayoutParams());
            tv_PhoneNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddItemDialog(Cpc_Settings_Free_PhoneNumber.this, sNewPhoneNo, (TextView) v);
                }
            });
            ll_PhNo.addView(tv_PhoneNo);
            Cpc_Utils.changeTextSize(ll_PhNo);
            if (!Init) {
                Cpc_Utils.CurVal.FreePhoneNo.add(sNewPhoneNo);
            }
        }
        tv_PhoneNo.setText(sNewPhoneNo);
    }
}