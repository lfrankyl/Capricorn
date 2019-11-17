package de.franky.l.capricorn;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import static de.franky.l.capricorn.Cpc_Std_Data.pref_AlarmManagerShallStop_Key;
import static de.franky.l.capricorn.Cpc_Utils.CpcPref;

public class Cpc_Activity_HelpHeader extends PreferenceActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback
{

@Override
protected void onCreate(Bundle savedInstanceState)
{
    // Log.d("Cpc_Activity_SettingsHeader","onCreate vor super");
    super.onCreate(savedInstanceState);
    setTitle(R.string.pref_Help_Title);
    try
    {
        Intent launchIntent = getIntent();
        launchIntent.putExtras(new Bundle());
        // Log.d("Cpc_Activity_SettingsHeader launchIntent",launchIntent.toString());
        // Log.d("Cpc_Activity_SettingsHeader EXTRA_APPWIDGET_ID",AppWidgetManager.EXTRA_APPWIDGET_ID);
        // Log.d("Cpc_Activity_SettingsHeader INVALID_APPWIDGET_ID",Integer.toString(AppWidgetManager.INVALID_APPWIDGET_ID));
    }
    catch (Exception e)
    {
        e.printStackTrace();
        // Log.d("Cpc_Activity_SettingsHeader","Exception e hat zugeschlagen");
        Toast.makeText(Cpc_Application.getContext(), "Exception @ Cpc_Activity_SettingsHeader", Toast.LENGTH_LONG).show();

    }
    //Cpc_Act_Permission_Helper.RequestPermission(this);
    // get the appWidgetId of the appWidget being configured
    Intent launchIntent = getIntent();
    Bundle extras = launchIntent.getExtras();
    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

    // set the result for cancel first
    // if the user cancels, then the appWidget
    // should not appear
    getFragmentManager().beginTransaction().replace(android.R.id.content, new Cpc_Settings_Frag_Help()).commit();
    Intent okResultValue = new Intent();
    okResultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    setResult(RESULT_OK, okResultValue);
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        Cpc_Act_Permission_Helper.CheckRequestPermissionsResult(requestCode,permissions ,grantResults );
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CpcPref.putBool(pref_AlarmManagerShallStop_Key,true);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        CpcPref.putBool(pref_AlarmManagerShallStop_Key,false);
        ContinueAfterClosingSettingsDialog();
    }
    private static void ContinueAfterClosingSettingsDialog()
    {
        Cpc_Utils.StartWidgetUpdates();
    }
}