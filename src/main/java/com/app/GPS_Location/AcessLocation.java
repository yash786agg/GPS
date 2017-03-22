package com.app.GPS_Location;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import com.app.GPS.R;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Yash on 23/2/17.
 */

public class AcessLocation
{
    @TargetApi(Build.VERSION_CODES.M)
    public static void getLocation(final Activity mActivity, final int PERMISSION_REQUEST_CODE)
    {
            List<String> permissionsNeeded = new ArrayList<>();

            List<String> permissionsList = new ArrayList<>();

            if (!addPermission(mActivity,permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
                permissionsNeeded.add(mActivity.getResources().getString(R.string.GPS));


            if (permissionsList.size() > 0)
            {
                mActivity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), PERMISSION_REQUEST_CODE);
            }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static boolean addPermission(Context context, List<String> permissionsList, String permission)
    {
        if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsList.add(permission);

            return false;
        }
        return true;
    }

}
