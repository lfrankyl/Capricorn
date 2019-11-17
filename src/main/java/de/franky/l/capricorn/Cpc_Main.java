package de.franky.l.capricorn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import static de.franky.l.capricorn.Cpc_Std_Data.pref_AlarmManagerShallStop_Key;
import static de.franky.l.capricorn.Cpc_Utils.CpcPref;

public class Cpc_Main extends AppCompatActivity
		//	implements ActivityCompat.OnRequestPermissionsResultCallback
    {
    private ToggleButton btnStd;
    private ToggleButton btnMobile;
    private ToggleButton btnWifi;
    //private ToggleButton btnCalls;
    private ToggleButton btnNetwrk;

    private static final int SETTINGS_RESULT = 1;
    private static final int HELP_RESULT = 2;
    private static final int INFO_RESULT = 3;

    private List<Fragment> Cpc_frag;
    private ViewPager Cpc_pager;

    @Override
    protected void onStart()
    {
        super.onStart();
        Cpc_Utils.Show_Oreo_Message_If_Necessary(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
//        Log.d("Cpc_Main", "onCreate");
        setContentView(R.layout.cpc_main);

        Cpc_frag = getFragments();

        Cpc_PageAdapter pageAdapter = new Cpc_PageAdapter(getSupportFragmentManager(), Cpc_frag);

        Cpc_pager = findViewById(R.id.cpc_viewpager);
        Cpc_pager.setAdapter(pageAdapter);

        Cpc_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                try {
                    switch (position) {
                        case 4:
                            setButtonCheckStatus(false,false,false,false,true);
                            break;
                        case 3:
                            setButtonCheckStatus(false,false,false,true,false);
                            break;
                        case 2:
                            setButtonCheckStatus(false,false,true,false,false);
                            break;
                        case 1:
                            setButtonCheckStatus(false,true,false,false,false);
                            break;
                        default:                            // includes 0 (Standard)
                            setButtonCheckStatus(true,false,false,false,false);
                            break;
                    }
                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), "Cpc_Main btn not exists", Toast.LENGTH_SHORT).show();
                    Log.e("Cpc_Main", "btn not exists");
                }
            }
        });
        //Cpc_Act_Permission_Helper.RequestPermission(this);

    }
    private void setButtonCheckStatus(Boolean Std, Boolean Mobile, Boolean Wifi, Boolean Netwrk, Boolean Calls){
        btnStd.setChecked(Std);
        btnMobile.setChecked(Mobile);
        btnWifi.setChecked(Wifi);
        btnNetwrk.setChecked(Netwrk);
        //btnCalls.setChecked(Calls);
    };

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        Cpc_Act_Permission_Helper.CheckRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
*/

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<>();

        fList.add(Cpc_frag_std.newInstance("Fragment 1","std"));
        fList.add(Cpc_frag_mobile.newInstance("Fragment 1","mobile"));
        fList.add(Cpc_frag_wlan.newInstance("Fragment 1","wlan"));
        fList.add(Cpc_frag_netwrk.newInstance("Fragment 1","netwrk"));
        //fList.add(Cpc_frag_calls.newInstance("Fragment 1","calls"));
        return fList;
    }


    private class Cpc_PageAdapter extends FragmentStatePagerAdapter
    {

        private List<Fragment> cpc_fragments;

        public Cpc_PageAdapter(FragmentManager fm, List<Fragment> fragments)
        {
            super(fm);
            this.cpc_fragments = fragments;
        }
        @Override
        public Fragment getItem(int position)
        {
            return this.cpc_fragments.get(position);
        }

        @Override
        public int getCount()
        {
            return this.cpc_fragments.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            Fragment tFr = (Fragment) super.instantiateItem(container, position);
            Cpc_frag.set(position, tFr);
            return tFr;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate our menu from the resources by using the menu inflater.
        getMenuInflater().inflate(R.menu.cpc_menu, menu);

/*
        btnCalls = findViewById(R.id.btnCalls);
        btnCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Cpc_pager.setCurrentItem(4);
                setButtonCheckStatus(false,false,false,false,true);
            }
        });
*/

        btnNetwrk = findViewById(R.id.btnNetwrk);
        btnNetwrk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Cpc_pager.setCurrentItem(3);
                setButtonCheckStatus(false,false,false,true,false);
            }
        });

        btnWifi = findViewById(R.id.btnWifi);
        btnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Cpc_pager.setCurrentItem(2);
                setButtonCheckStatus(false,false,true,false,false);
            }
        });


        btnMobile = findViewById(R.id.btnMobile);
        btnMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Cpc_pager.setCurrentItem(1);
                setButtonCheckStatus(false,true,false,false,false);
            }
        });

        btnStd = findViewById(R.id.btnStd);
        btnStd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Cpc_pager.setCurrentItem(0);
                setButtonCheckStatus(true,false,false,false,false);
            }
        });


        Button btnUpdate = findViewById(R.id.btn_Update);
        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Cpc_Utils_Data_Val.JustGetTheValues(Cpc_Application.getContext());
                int iSelItem = Cpc_pager.getCurrentItem();
                //Log.d("btnUpdate",String.valueOf(iSelItem) );
                Cpc_pager.setCurrentItem(iSelItem);
                try
                {
                    Fragment mFr = Cpc_frag.get(iSelItem);
                    if (mFr !=null)
                    {
//                        Log.d("btnUpdate","mFr Fragment is visible " + String.valueOf(mFr.isVisible()));
                        if (mFr.isVisible()) {
                            mFr.onStart();
                        }
                    }
//                    else
//                    {
//                        Log.d("btnUpdate","mFr Fragment is null");
//                    }
                }
                catch (Exception e)
                {
                    Log.e("btnUpdate","Fragment does not exist");
                }
            }
        });
        return true;
    }

    // BEGIN_INCLUDE(menu_item_selected)
    /**
     * This method is called when one of the menu items to selected. These items
     * can be on the Action Bar, the overflow menu, or the standard options menu. You
     * should return true if you handle the selection.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_help:
                i = new Intent(getApplicationContext(), Cpc_Activity_HelpHeader.class);
                startActivityForResult(i, HELP_RESULT);
                return true;

            case R.id.action_impressum:
                i = new Intent(getApplicationContext(), Cpc_Activity_InfoHeader.class);
                startActivityForResult(i, INFO_RESULT);
                return true;

            case R.id.action_settings:
                i = new Intent(getApplicationContext(), Cpc_Activity_SettingsHeader.class);
                startActivityForResult(i, SETTINGS_RESULT);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // END_INCLUDE(menu_item_selected)

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
        Cpc_Utils.StartWidgetUpdates();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d("Cpc_Main","onDestroy");
    }
}
