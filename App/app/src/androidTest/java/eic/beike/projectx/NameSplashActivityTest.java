package eic.beike.projectx;

import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import junit.framework.TestCase;

/**
 * Created by adam on 2015-09-24.
 */
public class NameSplashActivityTest extends ActivityInstrumentationTestCase2<NameSplashActivity> {

    NameSplashActivity activity;
    EditText textField;
    Button button;

    Context context;

    public NameSplashActivityTest() {
        super(NameSplashActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();



        // Clear name from settings
        context = getInstrumentation().getContext();
        SharedPreferences settings = context.getSharedPreferences(MainActivity.SETTINGS_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear().commit();

        activity = getActivity();

        textField = (EditText) activity.findViewById(R.id.nameInputField);
        button = (Button) activity.findViewById(R.id.nameOkButton);


    }


    public void testActivityExists() throws Exception {
        assertNotNull(activity);

        assertNotNull(textField);

        assertNotNull(button);
    }

    public void testOnCreate() throws Exception {

    }

    public void testOnClick() throws Exception {
        String expected = "testName";

        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(
                Menu.class.getName(),null, false
        );

        // Send string input value
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                textField.requestFocus();
            }
        });

        // Fill in the name
        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync(expected);
        getInstrumentation().waitForIdleSync();


        // Click the button (duh)
        TouchUtils.clickView(this, button);

        String actualText = textField.getText().toString();

        // Check that the field is filled
        assertEquals(expected, actualText);

        SharedPreferences settings = context.getSharedPreferences(MainActivity.SETTINGS_FILE,0);
        String name = settings.getString(MainActivity.NAME_FIELD,"");

        // Check that the name was actually set in settings.
        assertEquals(expected,name);

        // Check that the menu was started.
        assertEquals(1, monitor.getHits());
    }
}