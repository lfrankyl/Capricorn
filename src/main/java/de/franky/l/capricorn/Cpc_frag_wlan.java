package de.franky.l.capricorn;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static de.franky.l.capricorn.Cpc_Utils.CpcPref;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Cpc_frag_wlan.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Cpc_frag_wlan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cpc_frag_wlan extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    private String mParam3;
    private String mParam4;

    private View Wlan_View;
    private ViewGroup Lout_Container;

    private OnFragmentInteractionListener mListener;

    public Cpc_frag_wlan()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cpc_frag_wlan.
     */
    // TODO: Rename and change types and number of parameters
    public static Cpc_frag_wlan newInstance(String param1, String param2) {
        Cpc_frag_wlan fragment = new Cpc_frag_wlan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM3, param1);
        args.putString(ARG_PARAM4, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("Cpc_frag_wlan","onCreate");
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public void onStart ()
    {
        super.onStart();
//        Log.d("Cpc_frag_wlan","onStart");
        FillWlanPage();
        Cpc_Utils.changeTextSize(Lout_Container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Wlan_View = inflater.inflate(R.layout.cpc_frag_wlan, container, false);
        Lout_Container = Wlan_View.findViewById(R.id.cpc_main) ;
        return Wlan_View;
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
    private void FillWlanPage()
    {
        final long lMobileReceivedBytes = TrafficStats.getMobileRxBytes();
        final long lMobileSendBytes = TrafficStats.getMobileTxBytes();
        final long lWLANSendBytes =  TrafficStats.getTotalTxBytes() - lMobileSendBytes;
        final long lWLANReceivedBytes =  TrafficStats.getTotalRxBytes() - lMobileReceivedBytes;

        Cpc_Utils_Data_Val.JustGetTheValues(Cpc_Application.getContext());

        SetTextViewText(R.id.tV_W_V1,Cpc_Utils.MakeOutString((double) lWLANReceivedBytes + lWLANSendBytes) + Cpc_Utils.CalcUnit((double) lWLANReceivedBytes + lWLANSendBytes));
        SetTextViewText(R.id.tV_W_V2,Cpc_Utils.MakeOutString((double) lWLANSendBytes) + Cpc_Utils.CalcUnit((double) lWLANSendBytes));
        SetTextViewText(R.id.tV_W_V3,Cpc_Utils.MakeOutString((double) lWLANReceivedBytes) + Cpc_Utils.CalcUnit((double) lWLANReceivedBytes));
        SetTextViewText(R.id.tV_W_V5,Cpc_Utils.CurVal.DispData[6].getValue() + Cpc_Utils.CurVal.DispData[6].getUnit());
        String    sWLANDate = CpcPref.getString(Cpc_Application.getContext().getString(R.string.pref_wlanStartDate_Key),Cpc_Application.getContext().getString(R.string.DontKnow)); 	// WLAN-Datum  aus sharedpreference
        SetTextViewText(R.id.tV_W_V6,sWLANDate);

    }
    void SetTextViewText(int iTV, String sText)
    {
        TextView TV = Wlan_View.findViewById(iTV);
        TV.setText(sText);
    }

}
