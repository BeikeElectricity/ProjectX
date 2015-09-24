package eic.beike.projectx;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class NameSplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_init);

        Log.d("MainActivity", this.getClass().getName()+ ": onCrate");

        SharedPreferences settings = getSharedPreferences(MainActivity.SETTINGS_FILE,0);


        // Used to fetch the phone id
        final TelephonyManager tm = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        final String androidId;
        androidId = tm.getDeviceId();

        // Save it
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("id", androidId);

        editor.apply();
    }

    public void onClick(View view) {
        EditText editBox = (EditText) findViewById(R.id.nameInputField);
        String text = editBox.getText().toString();

        SharedPreferences settings = getSharedPreferences(MainActivity.SETTINGS_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(MainActivity.NAME_FIELD,text);

        editor.apply();


        // TODO: Start menu activity
//        Intent intent = new Intent(this, Menu.class);
//        startActivity(intent);
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
