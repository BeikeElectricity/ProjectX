package eic.beike.projectx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


/**
 * @author Adam Ingmansson
 */
public class MainActivity extends Activity {

    public static final String SETTINGS_FILE = "settings";

    public static final String NAME_FIELD = "name";
    public static final String ID_FIELD = "id";
    private static final int NEXT_ACTIVITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_splash);

        SharedPreferences settings = getSharedPreferences(SETTINGS_FILE, 0);

        // First time running?
        String name = settings.getString(NAME_FIELD,"");

        if (name == "") {
            startActivityForResult(
                new Intent(this, NameSplashActivity.class),
                MainActivity.NEXT_ACTIVITY
            );
        } else {
            // Start menu

            Intent intent = new Intent(this, MenuActivity.class);

            // Set so there is no history for back button
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivityForResult(intent, MainActivity.NEXT_ACTIVITY);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_CANCELED && requestCode == MainActivity.NEXT_ACTIVITY) {
            finish();
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
