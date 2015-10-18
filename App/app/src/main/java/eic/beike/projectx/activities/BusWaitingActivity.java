package eic.beike.projectx.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eic.beike.projectx.R;
import eic.beike.projectx.network.busdata.BusCollector;
import eic.beike.projectx.network.busdata.SimpleBusCollector;
import eic.beike.projectx.util.Constants;
import eic.beike.projectx.util.LocationFinder;

/**
 * Created by Mikael on 2015-10-06.
 */
public class BusWaitingActivity extends Activity {

    private boolean connected = false;
    private BusCollector busCollector;

    private static final int GAME_ACTIVITY_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        busCollector = SimpleBusCollector.getInstance();

        setContentView(R.layout.activity_bus_waiting);
        onConnect(null);
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
        if (connected){
            Intent intentGame = new Intent(this, GameActivity.class);
            startActivityForResult(intentGame, GAME_ACTIVITY_ID);
        }
        else{

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
    class ConnectToBusTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(Constants.ONE_SECOND_IN_MILLI);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            connected = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            TextView textView = (TextView) BusWaitingActivity.this.findViewById(R.id.bus_waiting_text);
            Button button     = (Button) BusWaitingActivity.this.findViewById(R.id.bus_waiting_button);

            if (connected) {
                textView.setText(R.string.bus_waiting_ready_text);
                button.setText(R.string.but_waiting_ready_button);
            } else {
                double tmp = LocationFinder.getLatitude() + LocationFinder.getLongitude();
                if (tmp != 0) {
                    textView.setText(R.string.bus_waiting_no_bus);
                } else {
                    textView.setText(R.string.bus_waiting_no_gps);
                }
            }
            button.setEnabled(true);
        }
    }
}
