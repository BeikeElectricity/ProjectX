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
 * Created by adam on 2015-09-24.
 */
public class MainActivityTest
        extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity activity;
    EditText textField;
    Button button;

    Context context;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @SuppressLint("CommitPrefEdits")
    public void setUp() throws Exception {
        super.setUp();

        // Clear name from settings
        context = getInstrumentation().getContext();
        SharedPreferences settings = context.getSharedPreferences(MainActivity.SETTINGS_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear().commit();


    }

    public void testActivityExists() throws Exception {
        activity = getActivity();
        assertNotNull(activity);
    }

    public void testOnCreate() throws Exception {

        // Used to check if an Activity is started, in this case NameSplashActivity.
        Instrumentation.ActivityMonitor am =
                getInstrumentation().addMonitor(
                        NameSplashActivity.class.getName(), null, true
                );
        activity = getActivity();

        // Wait for activity
        NameSplashActivity nsa = (NameSplashActivity) am.waitForActivityWithTimeout(10000);
        assertNotNull(nsa);
        // Check that it was started
        assertEquals(1,am.getHits());
    }
}
