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
        Log.d("TestDebug", this.getClass().getName() + ":onCreate");

        setContentView(R.layout.main_splash);

        for (int i = 0;i<3;i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        SharedPreferences settings = getSharedPreferences(SETTINGS_FILE, 0);

//        settings.edit().clear().commit();
        // First time running?
        String name = settings.getString(NAME_FIELD,"");
        Log.d("TestDebug", String.format("Got name: '%s'", name));




        // Start menu activity
        Log.d("TestDebug", "Starting Menu");
        if (name == "") {
            Log.d("TestDebug","No name, starting namesplash");

            startActivity(
                    new Intent(this, NameSplashActivity.class)
            );

            Log.d("TestDebug","Finalize.");
//            try {
//                finalize();
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }

        } else {
            startActivity(
                    new Intent(this, MenuActivity.class)
            );
        }
//        try {
//            finalize();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
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
