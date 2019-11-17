package de.franky.l.capricorn;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franky on 12.02.2017.
 */

public class Cpc_Act_Permission_Helper {

    public static int iPermissionTelStatus = -1;
    public static int iPermissionSMSStatus = -1;


    public static void RequestPermission(Activity myAct)
    {
        String CrLf = System.getProperty("line.separator");
        List<String> permissionsNeeded = new ArrayList<>();
        final Activity myActivity;
        myActivity = myAct;

        final List<String> permissionsList = new ArrayList<>();
        if (addPermission(myAct, permissionsList, Manifest.permission.READ_CALL_LOG))
            permissionsNeeded.add(Cpc_Application.getContext().getResources().getString(R.string.Perm_Calls_Rationale));
        if (addPermission(myAct, permissionsList, Manifest.permission.READ_SMS))
            permissionsNeeded.add(Cpc_Application.getContext().getResources().getString(R.string.Perm_SMS_Rationale));

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + CrLf + CrLf + permissionsNeeded.get(i);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(myAct);
                builder1.setTitle(Cpc_Application.getContext().getResources().getString(R.string.app_name) + Cpc_Application.getContext().getResources().getString(R.string.Perm_Dlg_Title));
                builder1.setMessage(message);
                builder1.setCancelable(true);

                builder1.setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ActivityCompat.requestPermissions(myActivity, permissionsList.toArray(new String[permissionsList.size()]),
                                        Cpc_Std_Data.MY_PERMISSION_BOTH);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else {
                ActivityCompat.requestPermissions(myAct, permissionsList.toArray(new String[permissionsList.size()]),
                        Cpc_Std_Data.MY_PERMISSION_BOTH);
            }
        }
    }

    private static boolean addPermission(Activity MyAct, List<String> permissionsList, String permission) {
        boolean bResult = true;
        if (ActivityCompat.checkSelfPermission(MyAct, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MyAct, permission))
                bResult = false;
        }
        return bResult;
    }

    public static void CheckRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0)
        {
            for (int i = 0; i<grantResults.length; i++  )
            {
                if (permissions[i].equalsIgnoreCase(Manifest.permission.READ_CALL_LOG))
                {
                    iPermissionTelStatus =grantResults[i];
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(Cpc_Application.getContext(), R.string.Perm_Calls_Granted, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(Cpc_Application.getContext(), R.string.Perm_Calls_Denied, Toast.LENGTH_LONG).show();
                    }
                }
                if (permissions[i].equalsIgnoreCase(Manifest.permission.READ_SMS)) {
                    iPermissionSMSStatus = grantResults[i];
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(Cpc_Application.getContext(), R.string.Perm_SMS_Granted, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Cpc_Application.getContext(), R.string.Perm_SMS_Denied, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
