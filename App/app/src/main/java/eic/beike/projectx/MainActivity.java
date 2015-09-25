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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TestDebug",this.getClass().getName()+":onCreate");
        SharedPreferences settings = getSharedPreferences(SETTINGS_FILE, 0);
//        settings.edit().clear().commit();
        // First time running?
        String name = settings.getString(NAME_FIELD,"");
        Log.d("TestDebug",String.format("Got name: '%s'",name));
        if (name == "") {
            Intent intent = new Intent(this, NameSplashActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.main_splash);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // TODO: Start menu activity
        Intent intent = new Intent(this,MenuActivity.class);
        startActivity(intent);
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
