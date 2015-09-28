package eic.beike.projectx;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import eic.beike.projectx.activities.MainActivity;
import eic.beike.projectx.activities.MenuActivity;
import eic.beike.projectx.activities.NameSplashActivity;

/**
 *@author Adam Ingmansson
 */
public class MainActivityTest
        extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity activity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @SuppressLint("CommitPrefEdits")
    public void setUp() throws Exception {
        super.setUp();


        // Clear name from settings
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences settings = context.getSharedPreferences(MainActivity.SETTINGS_FILE, 0);
        settings.edit().clear().commit();


    }

    @Override
    public void tearDown() throws Exception {

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

        // Used to prevent stalling
        Instrumentation.ActivityMonitor splashMonitor = getInstrumentation().addMonitor(
                NameSplashActivity.class.getName(), null, true
        );
        activity = getActivity();
        assertNotNull(activity);

        // Clean up
        getInstrumentation().removeMonitor(splashMonitor);

    }


    public void testOnCreateWithoutName() throws Exception {
        Log.d("TestDebug","testOnCreateWithoutName");
        // Used to check if an Activity is started, in this case NameSplashActivity.
        Instrumentation.ActivityMonitor splashMonitor = getInstrumentation().addMonitor(
                NameSplashActivity.class.getName(), null, true
        );

        activity = getActivity();

        // Check that it was started
        assertEquals(1, splashMonitor.getHits());

        getInstrumentation().removeMonitor(splashMonitor);
    }


    public void testOnCreateWithName() throws Exception {
        // Used to check if an Activity is started, in this case MenuActivity.
        Instrumentation.ActivityMonitor menuMonitor = getInstrumentation().addMonitor(
                MenuActivity.class.getName(), null, true
        );

        // Monitor that the other activity is not started
        Instrumentation.ActivityMonitor splashMonitor = getInstrumentation().addMonitor(
                NameSplashActivity.class.getName(), null, true
        );


        // Add the test name
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences settings = context.getSharedPreferences(MainActivity.SETTINGS_FILE, 0);
        settings.edit()
                .putString(MainActivity.NAME_FIELD, "testName")
                .commit();

        activity = getActivity();

        // Check that it was started
        assertEquals(0, splashMonitor.getHits());
        assertEquals(1, menuMonitor.getHits());


        getInstrumentation().removeMonitor(splashMonitor);
        getInstrumentation().removeMonitor(menuMonitor);

    }
}
