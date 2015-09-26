package eic.beike.projectx;

import android.annotation.SuppressLint;
import android.app.Activity;
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

    MainActivity activity;
    Context context;
    SharedPreferences settings;

    Instrumentation.ActivityMonitor splashMonitor;
    Instrumentation.ActivityMonitor menuMonitor;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @SuppressLint("CommitPrefEdits")
    public void setUp() throws Exception {
        super.setUp();
        Log.d("TestDebug", "------------------------------------------------------");
        Log.d("TestDebug", "setUp");


        // Clear name from settings
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences settings = context.getSharedPreferences(MainActivity.SETTINGS_FILE, 0);
        settings.edit().clear().commit();


    }

    @Override
    public void tearDown() throws Exception {
        Log.d("TestDebug", "tearDown");
        Log.d("TestDebug","------------------------------------------------------");

        if (this.activity != null) {
            this.activity.finish();
        }

        this.activity = null;

        // Clear settings
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences settings = context.getSharedPreferences(MainActivity.SETTINGS_FILE, 0);
        settings.edit().clear().commit();


        super.tearDown();
    }

    public void testActivityExists() throws Exception {
        Log.d("TestDebug", "testActivityExists");

        // Used to prevent stalling
        Instrumentation.ActivityMonitor splashMonitor = getInstrumentation().addMonitor(
                NameSplashActivity.class.getName(), null, true
        );
        Log.d("TestDebug", "getActivity");
        activity = getActivity();
        assertNotNull(activity);

        // Clean up
        getInstrumentation().removeMonitor(splashMonitor);

    }


    public void testOnCreateWithoutName() throws Exception {
        Log.d("TestDebug","testOnCreateWithoutName");
//
//         Used to check if an Activity is started, in this case NameSplashActivity.
//        Instrumentation.ActivityMonitor
        Instrumentation.ActivityMonitor splashMonitor = getInstrumentation().addMonitor(
                NameSplashActivity.class.getName(), null, true
        );

        Log.d("TestDebug","Activity start");
//        getActivity();
        activity = getActivity();

        // Wait for activity
        Log.d("TestDebug", "Before wait");
        Activity nsa = splashMonitor.waitForActivityWithTimeout(10000);
        Log.d("TestDebug", "After wait");

        assertNotNull(nsa);
        // Check that it was started
        assertEquals(1, splashMonitor.getHits());

        getInstrumentation().removeMonitor(splashMonitor);
    }


    public void testOnCreateWithName() throws Exception {
        Log.d("TestDebug","testOnCreateWithName");
        // Used to check if an Activity is started, in this case MenuActivity.
        Instrumentation.ActivityMonitor menuMonitor = getInstrumentation().addMonitor(
                MenuActivity.class.getName(), null, true
        );

        Instrumentation.ActivityMonitor splashMonitor = getInstrumentation().addMonitor(
                NameSplashActivity.class.getName(), null, true
        );


        Context context = getInstrumentation().getTargetContext();
        SharedPreferences settings = context.getSharedPreferences(MainActivity.SETTINGS_FILE, 0);
        settings.edit()
                .putString(MainActivity.NAME_FIELD, "testName")
                .commit();

        Log.d("TestDebug","Activity start");
        activity = getActivity();

        // Wait for activity
        Log.d("TestDebug", "Before wait");
        MenuActivity nsa = (MenuActivity) menuMonitor.waitForActivityWithTimeout(10000);
        Log.d("TestDebug", "After wait");

        assertNotNull(nsa);
        // Check that it was started
        assertEquals(0, splashMonitor.getHits());
        assertEquals(1, menuMonitor.getHits());

    }
}
