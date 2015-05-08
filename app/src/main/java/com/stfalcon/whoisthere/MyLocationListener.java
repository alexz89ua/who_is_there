package com.stfalcon.whoisthere;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

class MyLocationListener implements LocationListener {
    static String provider;
    static Location imHere;

    public static void SetUpLocationListener(Context context) {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();
        /*if (provider=="qwe")*/
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                3000,
                1,
                locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                3000,
                1,
                locationListener);
        imHere = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (imHere == null) {
            imHere = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

    }

    @Override
    public void onLocationChanged(Location loc) {
        imHere = loc;
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}