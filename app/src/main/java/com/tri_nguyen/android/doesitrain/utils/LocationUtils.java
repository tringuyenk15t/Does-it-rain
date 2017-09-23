package com.tri_nguyen.android.doesitrain.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Tri Nguyen on 9/15/2017.
 *
 * Ref: https://github.com/Shivakishore14/AndroidTutorial/tree/master/GPStut
 */

public class LocationUtils implements LocationListener {
    private Context mContext;

    public LocationUtils(Context context) {
        this.mContext = context;
    }

    @SuppressWarnings("MissingPermission")
    public Location getLocation(){
        try {
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10 , this);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return location;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}
