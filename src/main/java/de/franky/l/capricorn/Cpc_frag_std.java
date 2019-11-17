package de.franky.l.capricorn;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Cpc_frag_std.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Cpc_frag_std#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cpc_frag_std extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View Std_View;
    private ViewGroup Lout_Container;

    private OnFragmentInteractionListener mListener;

    public Cpc_frag_std()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cpc_frag_std.
     */
    // TODO: Rename and change types and number of parameters
    public static Cpc_frag_std newInstance(String param1, String param2) {
        Cpc_frag_std fragment = new Cpc_frag_std();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        //Log.d("Cpc_frag_std","newInstance");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("Cpc_frag_std","onCreate");
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
        //  Log.d("Cpc_frag_std","onStart");
        FillStdPage();
        Cpc_Utils.changeTextSize(Lout_Container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        Std_View = inflater.inflate(R.layout.cpc_frag_std , container, false);
        Lout_Container = Std_View.findViewById(R.id.cpc_main) ;
        //    Log.d("Cpc_frag_std","onCreateView");
        return Std_View;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        //      Log.d("Cpc_frag_std","onAttach");
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        Log.d("Cpc_frag_std","onDetach");
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

    private void FillStdPage()
    {
        Context AppContext = Cpc_Application.getContext();

        String[] sViewOptions = AppContext.getResources().getStringArray(R.array.viewoptions);

        Cpc_Utils_Data_Val.JustGetTheValues(AppContext);
        if (!Cpc_Utils_CPU.startCpuMonitoring())
        {
            Log.d("Cpc_frag_std","Something wrong with startCpuMonitoring");
        }

        SetTextViewText(R.id.tV_S_T1,sViewOptions[0] + ": ");
        SetTextViewText(R.id.tV_S_V1,Cpc_Utils.CurVal.DispData[0].getValue()+ Cpc_Utils.CurVal.DispData[0].getUnit());

        SetTextViewText(R.id.tV_S_T2,sViewOptions[4] + ": ");
        SetTextViewText(R.id.tV_S_V2,Cpc_Utils.CurVal.DispData[4].getValue()+ Cpc_Utils.CurVal.DispData[4].getUnit());

        SetTextViewText(R.id.tV_S_T3,sViewOptions[7] + ": ");
        SetTextViewText(R.id.tV_S_V3,Cpc_Utils.CurVal.DispData[7].getValue()+ Cpc_Utils.CurVal.DispData[7].getUnit());

        SetTextViewText(R.id.tV_S_T4,sViewOptions[1] + ": ");
        SetTextViewText(R.id.tV_S_V4,Cpc_Utils.CurVal.DispData[1].getValue()+ Cpc_Utils.CurVal.DispData[1].getUnit());
        SetTextViewText(R.id.tV_S_V4_2,Cpc_Utils.CurVal.DispData[Cpc_Utils.CurVal.sViewOptions_Val.length+0].getValue()+ Cpc_Utils.CurVal.DispData[Cpc_Utils.CurVal.sViewOptions_Val.length+0].getUnit());

        SetTextViewText(R.id.tV_S_T5,sViewOptions[2] + ": ");
        SetTextViewText(R.id.tV_S_V5,Cpc_Utils.CurVal.DispData[2].getValue()+ Cpc_Utils.CurVal.DispData[2].getUnit());
        SetTextViewText(R.id.tV_S_V5_2,Cpc_Utils.CurVal.DispData[Cpc_Utils.CurVal.sViewOptions_Val.length+1].getValue()+ Cpc_Utils.CurVal.DispData[Cpc_Utils.CurVal.sViewOptions_Val.length+1].getUnit());

        SetTextViewText(R.id.tV_S_T6,sViewOptions[3] + ": ");
        SetTextViewText(R.id.tV_S_V6,Cpc_Utils.CurVal.DispData[3].getValue()+ Cpc_Utils.CurVal.DispData[3].getUnit());
        SetTextViewText(R.id.tV_S_V6_2,Cpc_Utils.CurVal.DispData[Cpc_Utils.CurVal.sViewOptions_Val.length+2].getValue()+ Cpc_Utils.CurVal.DispData[Cpc_Utils.CurVal.sViewOptions_Val.length+2].getUnit());

        SetTextViewText(R.id.tV_S_V7,AppContext.getString(R.string.pref_CurVal_LastBoot)+ " " + GetLastBootDate());

        SetTextViewText(R.id.tV_S_SDK_H,AppContext.getString(R.string.pref_CurVal_Title_SDK)+":");

        SetTextViewText(R.id.tV_S_SDK_V,String.valueOf(android.os.Build.VERSION.RELEASE) + "  " +
                String.valueOf(android.os.Build.VERSION.SDK_INT) + "  " +
                String.valueOf(android.os.Build.VERSION.CODENAME)) ;
        SetTextViewText(R.id.textView8,String.valueOf(Build.VERSION.INCREMENTAL));
        SetTextViewText(R.id.textView10,Build.BRAND);
        SetTextViewText(R.id.textView12,Build.MANUFACTURER);
        SetTextViewText(R.id.textView14,Build.MODEL);
        SetTextViewText(R.id.textView16,Build.PRODUCT);
        SetTextViewText(R.id.textView18,Build.BOARD);
        SetTextViewText(R.id.textView20,Build.DISPLAY);
        SetTextViewText(R.id.textView22,Build.SERIAL);

    }

    private String GetLastBootDate()
    {
        Context myContext = Cpc_Application.getContext();
        long lLastBoot = (SystemClock.elapsedRealtime() / 1000);
        GregorianCalendar myCal = new GregorianCalendar();
        String sDate;
        if (lLastBoot <= Integer.MAX_VALUE ) {
            lLastBoot = lLastBoot * -1;
            myCal.add(Calendar.SECOND, (int) lLastBoot);
            if (Build.VERSION.SDK_INT <= (Build.VERSION_CODES.JELLY_BEAN_MR2)) {
                sDate = String.valueOf(android.text.format.DateFormat.format("dd.MM.yyyy ; kk:mm", myCal.getTime()));
            }
            else {
                sDate = String.valueOf(android.text.format.DateFormat.format("dd.MM.yyyy ; HH:mm", myCal.getTime()));

            }
        }
        else
        {
            sDate = myContext.getResources().getString(R.string.cpc_nd);
        }
        return sDate;

    }
    void SetTextViewText(int iTV, String sText)
    {
        TextView TV = Std_View.findViewById(iTV);
        TV.setText(sText);
    }


}
