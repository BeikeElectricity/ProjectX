package eic.beike.projectx;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    public static final String SETTINGS_FILE = "settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(SETTINGS_FILE,0);

        // First time running?
        if (settings.getString("nick","") == "") {
            setContentView(R.layout.main_init);

            // Used to fetch the phone id
            final TelephonyManager tm = (TelephonyManager) getBaseContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);

            final String androidId;
            androidId = tm.getDeviceId();

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("id",androidId);

            editor.commit();
        } else {
            setContentView(R.layout.main_splash);

        }

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
}
