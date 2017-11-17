package com.apackage.app.apackage;

import android.location.Location;
import android.util.Log;

/**
 * Created by kivan on 2017-11-17.
 */
// helper class for last know location
public final class gpsTracker {
    public static Location lastLocation;

    public static Location getLastLocation() {
        return lastLocation;
    }

    public static void setLastLocation(Location lastLocation) {
        Log.d("GpsTracker", String.format("LastLocation: %f, %f", lastLocation.getLatitude(), lastLocation.getLongitude()));
        gpsTracker.lastLocation = lastLocation;
    }
}
