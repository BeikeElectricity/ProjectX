package eic.beike.projectx;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.jar.Attributes;

/**
 *@author Adam Ingmansson
 */
public class MainActivityTest
        extends ActivityInstrumentationTestCase2<MainActivity> {


    Context context;
    SharedPreferences settings;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @SuppressLint("CommitPrefEdits")
    public void setUp() throws Exception {
        super.setUp();

        // Clear name from settings
        context = getInstrumentation().getContext();
        settings = context.getSharedPreferences(MainActivity.SETTINGS_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear().commit();


    }

    public void testActivityExists() throws Exception {
        MainActivity activity = getActivity();
        assertNotNull(activity);
    }

    public void testOnCreateWithName() throws Exception {
        Log.d("TestDebug","testOnCreate");
        // Used to check if an Activity is started, in this case NameSplashActivity.
        Instrumentation.ActivityMonitor monitor =
                getInstrumentation().addMonitor(
                        MenuActivity.class.getName(), null, true
                );

        Log.d("TestDebug","Activity start");
        getActivity();

        // Wait for activity
        Log.d("TestDebug", "Before wait");
        NameSplashActivity nsa = (NameSplashActivity) monitor.waitForActivityWithTimeout(5000);
        Log.d("TestDebug", "After wait");
        assertNotNull(nsa);
        // Check that it was started
        assertEquals(1, monitor.getHits());
    }

    public void testOnCreateWithoutName() throws Exception {
        Log.d("TestDebug","testOnCreate");
        // Used to check if an Activity is started, in this case NameSplashActivity.
        Instrumentation.ActivityMonitor monitor =
                getInstrumentation().addMonitor(
                        NameSplashActivity.class.getName(), null, true
                );

        Log.d("TestDebug","Activity start");
        getActivity();

        // Wait for activity
        Log.d("TestDebug","Before wait");
        NameSplashActivity nsa = (NameSplashActivity) monitor.waitForActivityWithTimeout(5000);
        Log.d("TestDebug", "After wait");
        assertNotNull(nsa);
        // Check that it was started
        assertEquals(1, monitor.getHits());
    }
}
