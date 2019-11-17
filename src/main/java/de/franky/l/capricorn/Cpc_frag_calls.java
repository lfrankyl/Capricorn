package de.franky.l.capricorn;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static de.franky.l.capricorn.Cpc_Utils.CpcPref;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Cpc_frag_calls.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Cpc_frag_calls#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cpc_frag_calls extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View Mobile_View;
    private ViewGroup Layout_Container;

    private OnFragmentInteractionListener mListener;

    public Cpc_frag_calls()
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
    public static Cpc_frag_calls newInstance(String param1, String param2) {
        Cpc_frag_calls fragment = new Cpc_frag_calls();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("Cpc_frag_calls","onCreate");
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
//        Log.d("Cpc_frag_calls","onStart");
        FillCallsPage();
        Cpc_Utils.changeTextSize(Layout_Container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        Mobile_View = inflater.inflate(R.layout.cpc_frag_mobile, container, false);
        Layout_Container = Mobile_View.findViewById(R.id.cpc_main) ;
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

    private void FillCallsPage()
    {
        Context AppContext = Cpc_Application.getContext();
        Cpc_Utils_Data_Val.JustGetTheValues(AppContext);

        SetTextViewText(R.id.tV_M_H1,R.string.pref_CurVal_CallDataInfo1);

        SetTextViewText(R.id.tV_C_V1,R.string.cpc_nd);
        String[] sMobileCycleOptions = Cpc_Application.getContext().getResources().getStringArray(R.array.mobDataCycleOptions);
        if(Cpc_Utils.CurVal.bMobileDataCycle) // wenn Abrechnungszeitraum eingestellt ist
        {
            SetTextViewText(R.id.tV_C_V1,sMobileCycleOptions[Cpc_Utils.CurVal.iMobileCycleTyp]);
//            if (Cpc_Utils.CurVal.iMobileCycleTyp == 1)
//            {
//                SetTextViewText(R.id.tV_C_V1,Integer.toString(Cpc_Utils.CurVal.iCycleLength) + " " + AppContext.getResources().getString(R.string.pref_Cur_Bill_Period_Title3));
//            }
//            else
//            {
//                SetTextViewText(R.id.tV_C_V1,R.string.pref_Cur_Bill_Period_Title5); //brauch mer den noch???
//            }
            SetTextViewText(R.id.tV_C_V2,"("+AppContext.getString(R.string.pref_Cur_Bill_Period_Title2) + " " + Integer.toString(Cpc_Utils.CurVal.iCycleLength - Cpc_Utils.CurVal.iCycleDone) +  " " + AppContext.getString(R.string.pref_Cur_Bill_Period_Title3)+")");
            SetTextViewText(R.id.tV_C_V3,String.valueOf(android.text.format.DateFormat.format("dd.MM.yyyy", Cpc_Utils.CurVal.DateOfChange)));
            long lCallsMax = CpcPref.getLong(R.string.pref_NuPiMax_Calls_Key,R.string.pref_Calls_MaxVal_Default); // Maxwert in Byte
            SetTextViewText(R.id.tV_M_H2,AppContext.getString(R.string.pref_Calls_MaxVal) + ": "+ String.valueOf(lCallsMax)+ " " + AppContext.getString(R.string.pref_Calls_RefVal_Unit));
        }

        SetTextViewText(R.id.tV_M_H3,R.string.pref_CurVal_CallDataInfo2);

        ImageView mIV = Mobile_View.findViewById(R.id.iV_M2);
        mIV.setImageResource(R.drawable.cpc_phone_gen_frag);

        SetTextViewText(R.id.tV_M_T1,R.string.pref_CurVal_MobileMax);
        SetTextViewText(R.id.tV_M_V1,Cpc_Utils.MakeOutString(Cpc_Utils.CurVal.dCallsMinOutgoingTodayMax) +  " " + AppContext.getResources().getString(R.string.pref_CurVal_Call_Unit));

        SetTextViewText(R.id.tV_M_T2,R.string.pref_CurVal_MobileCur);
        SetTextViewText(R.id.tV_M_V2,Cpc_Utils.CurVal.DispData[7].getValue() + " " + AppContext.getResources().getString(R.string.pref_CurVal_Call_Unit) + " / " + String.valueOf(Cpc_Utils.CurVal.iCallsNumOutgoing) + " " + AppContext.getResources().getString(R.string.pref_CurVal_CallDataInfo1));

        SetTextViewText(R.id.tV_M_T3,R.string.pref_CurVal_MobileMeanMax);
        SetTextViewText(R.id.tV_M_V3,Cpc_Utils.MakeOutString(Cpc_Utils.CurVal.dCallsMinOutgoingDayMax) + " " + AppContext.getResources().getString(R.string.pref_CurVal_Call_Unit));

        SetTextViewText(R.id.tV_M_T4,R.string.pref_CurVal_MobileMeanCur);
        SetTextViewText(R.id.tV_M_V4,Cpc_Utils.MakeOutString(Cpc_Utils.CurVal.dCallsMinOutgoingDayCur) + " " + AppContext.getResources().getString(R.string.pref_CurVal_Call_Unit));

        SetTextViewText(R.id.tV_M_H4,R.string.blankChr);

        SetTextViewText(R.id.tV_M_T5,R.string.pref_CurVal_CallDataInfo3);
        if (Cpc_Utils.CurVal.dCallsMinIncoming < 10)
        {
            SetTextViewText(R.id.tV_M_V5,"0" + Cpc_Utils.MakeOutString(Cpc_Utils.CurVal.dCallsMinIncoming) + " " + AppContext.getResources().getString(R.string.pref_CurVal_Call_Unit) + " / "+ String.valueOf(Cpc_Utils.CurVal.iCallsNumIncoming) + " " + AppContext.getResources().getString(R.string.pref_CurVal_CallDataInfo1));
        }
        else
        {
            SetTextViewText(R.id.tV_M_V5,Cpc_Utils.MakeOutString(Cpc_Utils.CurVal.dCallsMinIncoming) + " " + AppContext.getResources().getString(R.string.pref_CurVal_Call_Unit) + " / "+ String.valueOf(Cpc_Utils.CurVal.iCallsNumIncoming) + " " + AppContext.getResources().getString(R.string.pref_CurVal_CallDataInfo1));
        }
        SetTextViewText(R.id.tV_M_T6,R.string.pref_CurVal_CallDataInfo4);
        SetTextViewText(R.id.tV_M_V6,String.valueOf(Cpc_Utils.CurVal.iCallsNumMissed) + " " + AppContext.getResources().getString(R.string.pref_CurVal_CallDataInfo1));

        SetTextViewText(R.id.tV_M_H5,R.string.blankChr);

        SetTextViewText(R.id.tV_M_T7,R.string.blankChr);

        SetTextViewText(R.id.tV_M_V7,R.string.blankChr);

        if (Cpc_Utils.CurVal.bCallsIntegrateSMS) {
            SetTextViewText(R.id.tV_M_T8,R.string.pref_CurVal_CallDataInfo5);
            SetTextViewText(R.id.tV_M_V8,String.valueOf(Cpc_Utils.CurVal.iSMSNumSent));
        }
        else
        {
            SetTextViewText(R.id.tV_M_T8,R.string.blankChr);
            SetTextViewText(R.id.tV_M_V8,R.string.blankChr);
        }
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