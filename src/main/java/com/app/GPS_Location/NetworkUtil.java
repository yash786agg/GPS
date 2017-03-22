package com.app.GPS_Location;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by Yash on 22/3/17.
 */
public class NetworkUtil implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener
{
    private AppCompatActivity activity;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private NetworkNotifier informer;

    private static final String TAG = "SignIn_Screen";

    public NetworkUtil(AppCompatActivity activity, NetworkNotifier informer)
    {
        this.activity = activity;
        this.informer = informer;

        buildGoogleApiClient();
    }

    private void  buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>()
        {
            @Override
            public void onResult(LocationSettingsResult result)
            {
                final Status status = result.getStatus();

                switch (status.getStatusCode())
                {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.i(TAG, "LocationSettingsStatusCodes SUCCESS");

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try
                        {
                            Log.i(TAG, "LocationSettingsStatusCodes RESOLUTION_REQUIRED");

                            status.startResolutionForResult(activity, 1000);

                        }
                        catch (IntentSender.SendIntentException e)
                        {
                            Log.i(TAG, "LocationSettingsStatusCodes RESOLUTION_REQUIRED catch");

                            e.printStackTrace();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        Log.i(TAG, "LocationSettingsStatusCodes SETTINGS_CHANGE_UNAVAILABLE");

                        break;
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Log.i(TAG, "onConnected");

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location == null)
        {
           Log.i(TAG, "onConnected if");

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest, this);
        }
        else
        {
            Log.i(TAG, "onConnected else");

            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.i(TAG,"onConnectionSuspended: "+i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Log.i(TAG, "onConnectionFailed: " + connectionResult);

        if (connectionResult.hasResolution())
        {
            try
            {
                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            }
            catch (IntentSender.SendIntentException e)
            {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onLocationChanged(Location location)
    {
        Log.i(TAG,"onLocationChanged: "+location.toString());

        if(location.toString() != null)
        {
            handleNewLocation(location);
        }
        else
        {
            informer.locationFailed("Location service started but location was provided null");
        }
    }

    private void handleNewLocation(Location location)
    {
        Log.i(TAG, "handleNewLocation: "+location.toString());

        informer.locationUpdates(location);

    }

    public void connectGoogleApiClient()
    {
        Log.i(TAG, "MainAcivity connectGoogleApiClient");

        googleApiClient.connect();
    }

    public void disconnectGoogleApiClient()
    {
        Log.i(TAG, "MainAcivity disconnectGoogleApiClient");

        if (googleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

}
