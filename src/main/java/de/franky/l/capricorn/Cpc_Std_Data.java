package de.franky.l.capricorn;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/*
 * Created by franky on 05.06.2016.
 */
public class Cpc_Std_Data
{
    static final int MY_PERMISSION_READ_CALL_LOG = 4256;
    static final int MY_PERMISSION_READ_SMS      = 8197;
    static final int MY_PERMISSION_BOTH          = 4143;

    static final String ACTION_UPDATE_WIDGET_1 = "de.franky.l.intent.action.UPDATE_WIDGET";

    static final String pref_FirstSetup_Key = "pref_FirstSetup_Key";
    static final String pref_WlanSetup_Key  = "pref_WlanSetup_Key";
    static final String pref_OREO_Message_Key = "pref_OREO_Message_Key";
    static final String pref_BatSaved_Key = "pref_BatSaved_Key";
    static final String pref_MobileMessungSaved_Key = "pref_MobileMessungSaved_Key";
    static final String pref_AlarmManagerShallStop_Key = "pref_AlarmManagerShallStop_Key";

    final int[] iView;
    final int[] iRIdTv;
    final int[] iRIdTvUnit;
    final int[] iRIdIv;

    Cpc_Std_Data()
    {

        iView = new int[Cpc_Utils.CurVal.sViewOptions_Val.length];

        iRIdTv     = new int[Cpc_Utils.CurVal.sViewOptions_Val.length];
        iRIdTv[0]= R.id.tv_1;
        iRIdTv[1]= R.id.tv_2;
        iRIdTv[2]= R.id.tv_3;
        iRIdTv[3]= R.id.tv_4;
        iRIdTv[4]= R.id.tv_5;
        iRIdTv[5]= R.id.tv_6;
        iRIdTv[6]= R.id.tv_7;

        iRIdTvUnit = new int[Cpc_Utils.CurVal.sViewOptions_Val.length];
        iRIdTvUnit[0]= R.id.tv_1_unit;
        iRIdTvUnit[1]= R.id.tv_2_unit;
        iRIdTvUnit[2]= R.id.tv_3_unit;
        iRIdTvUnit[3]= R.id.tv_4_unit;
        iRIdTvUnit[4]= R.id.tv_5_unit;
        iRIdTvUnit[5]= R.id.tv_6_unit;
        iRIdTvUnit[6]= R.id.tv_7_unit;

        iRIdIv = new int[Cpc_Utils.CurVal.sViewOptions_Val.length];
        iRIdIv[0]= R.id.iv_1;
        iRIdIv[1]= R.id.iv_2;
        iRIdIv[2]= R.id.iv_3;
        iRIdIv[3]= R.id.iv_4;
        iRIdIv[4]= R.id.iv_5;
        iRIdIv[5]= R.id.iv_6;
        iRIdIv[6]= R.id.iv_7;
    }

    public void Update_iViews()
    {
        Context myContext = Cpc_Application.getContext();
        SharedPreferences Cpc_Data = PreferenceManager.getDefaultSharedPreferences(myContext);

        iView[0] = Cpc_Utils .SaveParseInt(Cpc_Data.getString(myContext.getString(R.string.pref_View1_Key), Cpc_Utils.CurVal.sViewOptions_Val[6]),5) ;
        iView[1] = Cpc_Utils .SaveParseInt(Cpc_Data.getString(myContext.getString(R.string.pref_View2_Key), Cpc_Utils.CurVal.sViewOptions_Val[5]),5) ;
        iView[2] = Cpc_Utils .SaveParseInt(Cpc_Data.getString(myContext.getString(R.string.pref_View3_Key), Cpc_Utils.CurVal.sViewOptions_Val[2]),5) ;
        iView[3] = Cpc_Utils .SaveParseInt(Cpc_Data.getString(myContext.getString(R.string.pref_View4_Key), Cpc_Utils.CurVal.sViewOptions_Val[1]),5) ;
        iView[4] = Cpc_Utils .SaveParseInt(Cpc_Data.getString(myContext.getString(R.string.pref_View5_Key), Cpc_Utils.CurVal.sViewOptions_Val[3]),5) ;
        iView[5] = Cpc_Utils .SaveParseInt(Cpc_Data.getString(myContext.getString(R.string.pref_View6_Key), Cpc_Utils.CurVal.sViewOptions_Val[7]),5) ;
        iView[6] = Cpc_Utils .SaveParseInt(Cpc_Data.getString(myContext.getString(R.string.pref_View7_Key), Cpc_Utils.CurVal.sViewOptions_Val[4]),5) ;
    }
}
