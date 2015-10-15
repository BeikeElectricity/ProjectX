package eic.beike.projectx.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import eic.beike.projectx.R;
import eic.beike.projectx.util.Constants;


/**
 * @author Adam Ingmansson
 */
public class NameSplashActivity extends Activity {

    public static final int MENU_ACTIVITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_init);

        SharedPreferences settings = getSharedPreferences(Constants.SETTINGS_FILE,0);

        // Used to fetch the phone id
        final TelephonyManager tm = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        final String androidId;
        androidId = tm.getDeviceId();

        // Save it
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.ID_FIELD, androidId);

        editor.apply();
    }


    /**
     * Handles clicks form the button in the view
     * @param view Unused (Triggering view)
     */
    public void onClick(View view) {
        EditText editBox = (EditText) findViewById(R.id.nameInputField);
        String text = editBox.getText().toString();

        // Lets save the name in permanent storage
        SharedPreferences settings = getSharedPreferences(Constants.SETTINGS_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(Constants.NAME_FIELD,text);

        editor.apply();


        // Start menu activity
        Intent intent = new Intent(this, MenuActivity.class);

        // Set so there is no history for back button
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, NameSplashActivity.MENU_ACTIVITY);
    }

    /**
     * Handles the result from any activity started by this.
     * Used for stopping this when user exits.
     *
     * @param requestCode The code identifying which request
     * @param resultCode The result code of that request
     * @param data The intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MENU_ACTIVITY) {
            if (resultCode == NameSplashActivity.RESULT_CANCELED) {
                // Bubble the "exit" to next activity (Main)
                setResult(NameSplashActivity.RESULT_CANCELED);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_name_splash, menu);
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
