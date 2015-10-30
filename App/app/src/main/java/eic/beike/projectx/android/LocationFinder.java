package eic.beike.projectx.android;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Keeps track of the gps position of the phone. We start listening
 * as soon as the app starts and let any class that's interested get
 * the android Location. The reason we do this is because we had trouble
 * getting a gps fix fast enough so we give the location finder a head
 * start.
 *
 * @author adam
 */
public class LocationFinder implements LocationListener {

    private LocationManager locationManager;

    private static final Location position = new Location("");

    public LocationFinder(LocationManager lm) {

        Log.d("LocationFinder", "Initial location: " + position.toString());

        locationManager = lm;

        Log.d("LocationFinder", "Setting up listener");
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0, 1,this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
    }


    public void stop() {
        locationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null && location.getAccuracy() < 500) {
            //location wasn't null and we're 68% certain that it's correct to 500 meters.
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

