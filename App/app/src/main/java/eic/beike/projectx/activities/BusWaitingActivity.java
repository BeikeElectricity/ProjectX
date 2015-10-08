package eic.beike.projectx.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eic.beike.projectx.R;
import eic.beike.projectx.busdata.BusCollector;
import eic.beike.projectx.busdata.SimpleBusCollector;
import eic.beike.projectx.util.Constants;

/**
 * Created by Mikael on 2015-10-06.
 */
public class BusWaitingActivity extends Activity {

    private boolean connected = false;
    private BusCollector busCollector;
    private LocationManager locationManager;
    private LocationHandler locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        busCollector = SimpleBusCollector.getInstance();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationHandler();
        setContentView(R.layout.activity_bus_waiting);
    }

    public void onConnect(View v) {
        if(connected){
            Intent intentGame = new Intent(this, GameActivity.class);
            startActivity(intentGame);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            TextView textView = (TextView) findViewById(R.id.bus_waiting_text);
            Button button = (Button) findViewById(R.id.bus_waiting_button);
            textView.setText("Seaching for bus...");
            button.setEnabled(false);
            new ConnectToBusTask().execute();
        }
    }

    /**
     * Inner class to asynchronously connect to bus
     */
    class ConnectToBusTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Location location = null;
            for(int i = 0; location == null && i < 5; i++){
                location = locationListener.getLocation();
                try {
                    Thread.sleep(Constants.ONE_SECOND_IN_MILLI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(location != null) {
                connected = busCollector.determineBus(location);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            TextView textView = (TextView) BusWaitingActivity.this.findViewById(R.id.bus_waiting_text);
            Button button     = (Button) BusWaitingActivity.this.findViewById(R.id.bus_waiting_button);

            if(connected) {
                textView.setText("Bus found.");
                button.setText("Start");
            }
            else{
                textView.setText("Couldn't connect to bus.\nPlease try again.");
            }
            button.setEnabled(true);
        }


    }

    class LocationHandler implements LocationListener{

        private Location location = null;

        public Location getLocation(){
            return location;
        }

        @Override
        public void onLocationChanged(Location location) {
            this.location = location;
            locationManager.removeUpdates(this);
        }

        // Required unused functions
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    }
}
