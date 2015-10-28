package eic.beike.projectx.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import eic.beike.projectx.R;
import eic.beike.projectx.android.LocationFinder;
import eic.beike.projectx.util.Constants;


/**
 * @author Adam Ingmansson
 * This is the first activity to start. It checks if you have chosen a nickname before
 * if not it starts nameSplashActivity else starts menu
 */
public class MainActivity extends Activity {

    public static final int NEXT_ACTIVITY = 0;

    private LocationFinder locationFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_splash);

        SharedPreferences settings = getSharedPreferences(Constants.SETTINGS_FILE, 0);

        String name = settings.getString(Constants.NAME_FIELD, "");


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationFinder = new LocationFinder(lm);

        // First time running?
        if (name.equals("")) {

            // Start the name picker activity
            startActivityForResult(
                    new Intent(this, NameSplashActivity.class),
                    MainActivity.NEXT_ACTIVITY
            );
        } else {

            // Start the menu
            Intent intent = new Intent(this, MenuActivity.class);

            // Set so there is no history for back button
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Off we go
            startActivityForResult(intent, MainActivity.NEXT_ACTIVITY);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        locationFinder.stop();

        Log.d("LocationFinder", "Removing listener");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles the result from any activity started by this.
     *
     * @param requestCode The code identifying which request
     * @param resultCode The result code of that request
     * @param data The intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_CANCELED) {
            finish();
        }
    }


}
