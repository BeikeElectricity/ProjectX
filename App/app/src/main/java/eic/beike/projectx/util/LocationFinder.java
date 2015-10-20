package eic.beike.projectx.util;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * @author adam
 */
public class LocationFinder implements LocationListener {

    private LocationManager locationManager;

    public static final Location position = new Location("");

    public LocationFinder(LocationManager lm) {

        Log.d("LocationFinder", "Initial location: " + position.toString());

        locationManager = lm;

        Log.d("LocationFinder", "Setting up listener");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }


    public void stop() {
        locationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            position.set(location);
            Log.d("LocationFinder", "New location: " + position.toString());
        }
    }

    // Required unused functions
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}

    public static Location getLocation() {
        return position;
    }

    public static double getLongitude() {
        return position.getLongitude();
    }

    public static double getLatitude() {
        return position.getLatitude();
    }
}

