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
import eic.beike.projectx.network.busdata.BusCollector;
import eic.beike.projectx.network.busdata.SimpleBusCollector;
import eic.beike.projectx.util.Constants;

/**
 * Created by Mikael on 2015-10-06.
 */
public class BusWaitingActivity extends Activity {

    private boolean connected = false;
    private BusCollector busCollector;
    private LocationManager locationManager;
    private LocationHandler locationListener;

    private static final int GAME_ACTIVITY_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        busCollector = SimpleBusCollector.getInstance();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationHandler();
        setContentView(R.layout.activity_bus_waiting);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GAME_ACTIVITY_ID) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    public void onConnect(View v) {
        if(connected){
            Intent intentGame = new Intent(this, GameActivity.class);
            startActivityForResult(intentGame, GAME_ACTIVITY_ID);
            finish();
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            TextView textView = (TextView) findViewById(R.id.bus_waiting_text);
            Button button = (Button) findViewById(R.id.bus_waiting_button);
            textView.setText(R.string.bus_waiting_searching);
            button.setEnabled(false);
            new ConnectToBusTask().execute();
        }
    }

    /**
     * Inner class to asynchronously connect to bus
     */
    class ConnectToBusTask extends AsyncTask<Void, Void, Location> {

        @Override
        protected Location doInBackground(Void... params) {
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
            return location;
        }

        @Override
        protected void onPostExecute(Location location){
            TextView textView = (TextView) BusWaitingActivity.this.findViewById(R.id.bus_waiting_text);
            Button button     = (Button) BusWaitingActivity.this.findViewById(R.id.bus_waiting_button);

            if(connected) {
                textView.setText(R.string.bus_waiting_ready_text);
                button.setText(R.string.but_waiting_ready_button);
            }
            else{
                if (location != null) {
                    textView.setText(R.string.bus_waiting_no_bus);
                } else {
                    textView.setText(R.string.bus_waiting_no_gps);
                }
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
