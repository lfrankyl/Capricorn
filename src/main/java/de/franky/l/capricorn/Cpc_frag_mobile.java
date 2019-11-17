package de.franky.l.capricorn;

import android.content.Context;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static de.franky.l.capricorn.Cpc_Utils.CpcPref;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Cpc_frag_mobile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Cpc_frag_mobile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cpc_frag_mobile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View Mobile_View;
    private ViewGroup Lout_Container;

    private OnFragmentInteractionListener mListener;

    public Cpc_frag_mobile()
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
    public static Cpc_frag_mobile newInstance(String param1, String param2) {
        Cpc_frag_mobile fragment = new Cpc_frag_mobile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("Cpc_frag_mobile","onCreate");
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
//        Log.d("Cpc_frag_mobile","onStart");
        FillMobilePage();
        Cpc_Utils.changeTextSize(Lout_Container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        Mobile_View = inflater.inflate(R.layout.cpc_frag_mobile, container, false);
        Lout_Container =  Mobile_View.findViewById(R.id.cpc_main) ;
        return Mobile_View ;
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

    private void FillMobilePage()
    {
        final long lMobileReceivedBytes = TrafficStats.getMobileRxBytes();
        final long lMobileSendBytes = TrafficStats.getMobileTxBytes();
        String[] sMobileCycleOptions = Cpc_Application.getContext().getResources().getStringArray(R.array.mobDataCycleOptions);

        Context AppContext = Cpc_Application.getContext();
        Cpc_Utils_Data_Val.JustGetTheValues(AppContext);

        SetTextViewText(R.id.tV_C_V1,R.string.cpc_nd);

        if(Cpc_Utils.CurVal.bMobileDataCycle) // wenn Abrechnungszeitraum eingestellt ist
        {
            SetTextViewText(R.id.tV_C_V1,sMobileCycleOptions[Cpc_Utils.CurVal.iMobileCycleTyp]);

            SetTextViewText(R.id.tV_C_V2,"("+AppContext.getString(R.string.pref_Cur_Bill_Period_Title2) + " " + Integer.toString(Cpc_Utils.CurVal.iCycleLength - Cpc_Utils.CurVal.iCycleDone) +  " " + AppContext.getString(R.string.pref_Cur_Bill_Period_Title3)+")");
            SetTextViewText(R.id.tV_C_V3,String.valueOf(android.text.format.DateFormat.format("dd.MM.yyyy", Cpc_Utils.CurVal.DateOfChange)));
            double dMobMax = CpcPref.getLong(R.string.pref_NuPiMax_Mob_Key,R.string.pref_MobileMaxVal_Default); // Maxwert in Byte
            SetTextViewText(R.id.tV_M_H2,AppContext.getString(R.string.pref_MobileMaxVal) + ": "+ Cpc_Utils.MakeOutString(dMobMax) + Cpc_Utils.CalcUnit(dMobMax));
        }
        SetTextViewText(R.id.tV_M_V1,Cpc_Utils.MakeOutString(Cpc_Utils.CurVal.dMobileTodayMax) + Cpc_Utils.CalcUnit(Cpc_Utils.CurVal.dMobileTodayMax));
        SetTextViewText(R.id.tV_M_V2,Cpc_Utils.CurVal.DispData[5].getValue() + Cpc_Utils.CurVal.DispData[5].getUnit());

        SetTextViewText(R.id.tV_M_V3,Cpc_Utils.MakeOutString(Cpc_Utils.CurVal.dMobileDayMax) + Cpc_Utils.CalcUnit(Cpc_Utils.CurVal.dMobileDayMax));
        SetTextViewText(R.id.tV_M_V4,Cpc_Utils.MakeOutString(Cpc_Utils.CurVal.dMobileDayCur) + Cpc_Utils.CalcUnit(Cpc_Utils.CurVal.dMobileDayCur));
        SetTextViewText(R.id.tV_M_V5,Cpc_Utils.MakeOutString((double) lMobileReceivedBytes + lMobileSendBytes) + Cpc_Utils.CalcUnit((double) lMobileReceivedBytes + lMobileSendBytes));

        SetTextViewText(R.id.tV_M_V6,Cpc_Utils.MakeOutString((double) lMobileReceivedBytes) + Cpc_Utils.CalcUnit((double) lMobileReceivedBytes));
        SetTextViewText(R.id.tV_M_V7,Cpc_Utils.MakeOutString((double) lMobileSendBytes) + Cpc_Utils.CalcUnit((double) lMobileSendBytes));

        SetTextViewText(R.id.tV_M_T8,R.string.blankChr);
        SetTextViewText(R.id.tV_M_V8,R.string.blankChr);
        SetTextViewText(R.id.tV_M_T9,R.string.blankChr);
        SetTextViewText(R.id.tV_M_V9,R.string.blankChr);
    }

    void SetTextViewText(int iTV, int iText)
    {
        TextView TV = Mobile_View.findViewById(iTV);
        TV.setText(iText);
    }
    void SetTextViewText(int iTV, String sText)
    {
        TextView TV = Mobile_View.findViewById(iTV);
        TV.setText(sText);
    }

}