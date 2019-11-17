package de.franky.l.capricorn;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static android.content.Context.WIFI_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Cpc_frag_netwrk.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Cpc_frag_netwrk#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cpc_frag_netwrk extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View Netwrk_View;
    private ViewGroup Lout_Container;

    private OnFragmentInteractionListener mListener;

    public Cpc_frag_netwrk()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cpc_frag_mobile.
     */
    // TODO: Rename and change types and number of parameters
    public static Cpc_frag_netwrk newInstance(String param1, String param2) {
        Cpc_frag_netwrk fragment = new Cpc_frag_netwrk();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Log.d("Cpc_frag_network","onCreate");
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart ()
    {
        super.onStart();
        // Log.d("Cpc_frag_network","onStart");
        FillNetwrkPage();
        Cpc_Utils.changeTextSize(Lout_Container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        Netwrk_View = inflater.inflate(R.layout.cpc_frag_wlan, container, false);
        Lout_Container =  Netwrk_View.findViewById(R.id.cpc_main) ;
        return Netwrk_View ;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void FillNetwrkPage()
    {
        Context AppContext = Cpc_Application.getContext();

        ImageView mIV = Netwrk_View.findViewById(R.id.iV_W_H);
        mIV.setImageResource(R.drawable.capricorn_frag);
        SetTextViewText(R.id.tV_W_H1,R.string.pref_NetWrk_Caption);

        String sNetWrkType = AppContext.getString(R.string.pref_NetWrk_NoNetWrk);
        String sNetWrkState = AppContext.getString(R.string.pref_NetWrk_NoNetWrkConn);
        String sNetWrkSubType = AppContext.getString(R.string.NotKnown);
        int iNetIpAdressTitle = R.string.pref_NetWrk_ip6;
        String sipAddress = AppContext.getString(R.string.NotKnown);
        String sSSID = AppContext.getString(R.string.NotKnown);
        String sWifiBSSID = AppContext.getString(R.string.NotKnown);
        String sWifiSpeed = AppContext.getString(R.string.NotKnown);
        String sRoaming = AppContext.getString(R.string.pref_Off);
        SetTextViewText(R.id.tV_W_T5,R.string.blankChr);
        SetTextViewText(R.id.tV_W_V5,R.string.blankChr);
        SetTextViewText(R.id.tV_W_H21,R.string.pref_NetWrk_Type);
        SetTextViewText(R.id.tV_W_T1,R.string.pref_NetWrk_Status);
        SetTextViewText(R.id.tV_W_T3,R.string.pref_NetWrk_MobileTyp);
        SetTextViewText(R.id.tV_W_T4,R.string.pref_NetWrk_Rming);
        SetTextViewText(R.id.tV_W_H3,R.string.blankChr);
        SetTextViewText(R.id.tV_W_VH3,R.string.blankChr);

        TextView TV = Netwrk_View.findViewById(R.id.tV_W_H21);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                2f
        );
        TV.setLayoutParams(param);

        TV = Netwrk_View.findViewById(R.id.tV_W_H22);
        param = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                3f
        );
        TV.setLayoutParams(param);

        try
        {
            ConnectivityManager cm = (ConnectivityManager) AppContext.getSystemService(Context.CONNECTIVITY_SERVICE);  // Hole Systemdaten um zu erkennen ob
            NetworkInfo NwI = cm.getActiveNetworkInfo();
            if (NwI != null) {
                sNetWrkSubType = getNetWrkSubTypeName(NwI.getSubtype());
                sNetWrkType = NwI.getTypeName();
                sNetWrkState = NwI.getState().toString();
                if (NwI.isRoaming()) {
                    sRoaming = AppContext.getString(R.string.pref_On);
                }
                WifiManager wifiMgr = (WifiManager) AppContext.getApplicationContext().getSystemService(WIFI_SERVICE);
                DhcpInfo wifiInfo = wifiMgr.getDhcpInfo();//.getConnectionInfo();


                if (NwI.getType()== ConnectivityManager.TYPE_WIFI)
                {
                    int ip = wifiInfo.ipAddress;  //.getIpAddress();
                    sipAddress = String.format("%d.%d.%d.%d", (ip & 0xff),  (ip >> 8 & 0xff),  (ip >> 16 & 0xff),  (ip >> 24 & 0xff));
                    sSSID = wifiMgr.getConnectionInfo().getSSID();
                    sWifiBSSID = wifiMgr.getConnectionInfo().getBSSID();
                    sWifiSpeed = String.valueOf(wifiMgr.getConnectionInfo().getLinkSpeed());
                    iNetIpAdressTitle = R.string.pref_NetWrk_ip4;
                    SetTextViewText(R.id.tV_W_T3,R.string.pref_NetWrk_SSID);

                    SetTextViewText(R.id.tV_W_H3,R.string.pref_NetWrk_LinkSpeed);
                    SetTextViewText(R.id.tV_W_VH3,sWifiSpeed + AppContext.getString(R.string.blankChr) + WifiInfo.LINK_SPEED_UNITS);
                    SetTextViewText(R.id.tV_W_T4,R.string.pref_NetWrk_BSSID);
                    sRoaming = sWifiBSSID;
                    if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                        SetTextViewText(R.id.tV_W_T5, R.string.pref_NetWrk_Frq);
                        SetTextViewText(R.id.tV_W_V5, String.valueOf(wifiMgr.getConnectionInfo().getFrequency())+
                                 AppContext.getString(R.string.blankChr) + WifiInfo.FREQUENCY_UNITS);
                    }

                }
                else
                {
                    sSSID = sNetWrkSubType;
                    sipAddress = getLocalIpAddress();
                }

            }
        }
        catch (Exception e)
        {
            Log.d("Cpc_frag_netwrk","Error in NetworkInfo");
        }


        SetTextViewText(R.id.tV_W_H22,sNetWrkType);
        SetTextViewText(R.id.tV_W_V1,sNetWrkState);

        SetTextViewText(R.id.tV_W_T2,iNetIpAdressTitle);
        SetTextViewText(R.id.tV_W_V2,sipAddress);

        SetTextViewText(R.id.tV_W_V3,sSSID);
        SetTextViewText(R.id.tV_W_V4,sRoaming);

        SetTextViewText(R.id.tV_W_T6,R.string.blankChr);
        SetTextViewText(R.id.tV_W_V6,R.string.blankChr);
    }

    public String longToIp(long ip)
    {
        // Make IPv4 address readable
        StringBuilder result = new StringBuilder(15);

        for (int i = 0; i <4; i++) {
            result.insert(0,Long.toString(ip & 0xff));
            if (i < 3) {
                result.insert(0,'.');
            }
            ip = ip >> 8;
        }
        return result.toString();
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("getLocalIpAddress", ex.toString());
        }
        return null;
    }
    public String getNetWrkSubTypeName(int SubType) {
        switch (SubType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "2G GPRS";
            case TelephonyManager.NETWORK_TYPE_GSM:
                return "2G GSM";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "2G EDGE";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "2G CDMA";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "2G 1xRTT";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G IDEN";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "3G UMTS";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "3G EVDO_0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "3G EVDO_A";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "3G HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "3G HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "3G HSPA";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "3G EVDO_B";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "3G EHRPD";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G HSPAP";
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return "3G TD_SCDMA";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G LTE";
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                return "IWLAN";
            default:
                return "unknown";
        }
    }
    void SetTextViewText(int iTV, int iText)
    {
        TextView TV = Netwrk_View.findViewById(iTV);
        TV.setText(iText);
    }
    void SetTextViewText(int iTV, String sText)
    {
        TextView TV = Netwrk_View.findViewById(iTV);
        TV.setText(sText);
    }


}