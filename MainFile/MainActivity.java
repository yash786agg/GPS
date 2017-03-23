package com.app.Current_Location;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NetworkNotifier
{
    private int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private NetworkUtil networkUtilObj;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            networkUtilObj = new NetworkUtil(this,this);

            networkUtilObj.connectGoogleApiClient();
        }
        else
        {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                networkUtilObj = new NetworkUtil(this,this);

                networkUtilObj.connectGoogleApiClient();
            }
            else
            {
                int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;
                AcessLocation.getLocation(MainActivity.this, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        }
    }

    @Override
    public void locationUpdates(Location location)
    {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        Toast.makeText(MainActivity.this, "Latitude: " + currentLatitude + "" + " Longitude: " + currentLongitude + "", Toast.LENGTH_LONG).show();

    }


    @Override
    public void locationFailed(String message)
    { }

    @Override
    protected void onPause()
    {
        super.onPause();

        if(networkUtilObj != null)
        {
            networkUtilObj.disconnectGoogleApiClient();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 2:
            {
                Map<String, Integer> perms = new HashMap<>();

                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    networkUtilObj = new NetworkUtil(this, this);

                    networkUtilObj.connectGoogleApiClient();
                }

                if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                {
                    boolean should = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
                    if (should)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
                        builder.setTitle(R.string.permission_denied);
                        builder.setMessage(R.string.permission_access_fine_location);
                        builder.setPositiveButton(R.string.i_am_sure, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton(R.string.re_try, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }
                        });
                        builder.show();

                    }
                }

                break;
            }

            case 1:
            {
                Map<String, Integer> perms = new HashMap<>();

                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    networkUtilObj = new NetworkUtil(this, this);

                    networkUtilObj.connectGoogleApiClient();
                }

                break;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}
