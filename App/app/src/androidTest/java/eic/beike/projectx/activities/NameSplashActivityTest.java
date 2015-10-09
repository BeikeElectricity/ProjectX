package eic.beike.projectx.activities;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import eic.beike.projectx.R;
import eic.beike.projectx.activities.MainActivity;
import eic.beike.projectx.activities.MenuActivity;
import eic.beike.projectx.activities.NameSplashActivity;

/**
 * Test case for NameSplashActivity
 *
 *
 *@author Adam Ingmansson
 */
public class NameSplashActivityTest extends ActivityInstrumentationTestCase2<NameSplashActivity> {

    NameSplashActivity activity;
    EditText textField;
    Button button;

    Context context;
    SharedPreferences settings;

    Instrumentation.ActivityMonitor menuMonitor;

    public NameSplashActivityTest() {
        super(NameSplashActivity.class);
    }



    @SuppressLint("CommitPrefEdits")
    public void setUp() throws Exception {
        super.setUp();

        // Clear name from settings
        context = getInstrumentation().getTargetContext();
        settings = context.getSharedPreferences(MainActivity.SETTINGS_FILE, 0);
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

    /**
     * Tests that the activity starts and the fields and button are present
     * @throws Exception
     */
    public void testActivityExists() throws Exception {
        activity = getActivity();
        textField = (EditText) activity.findViewById(R.id.nameInputField);
        button = (Button) activity.findViewById(R.id.nameOkButton);


        assertNotNull(activity);

        assertNotNull(textField);
        final View decorView = activity.getWindow().getDecorView();

        ViewAsserts.assertOnScreen(decorView, textField);

        assertNotNull(button);

        ViewAsserts.assertOnScreen(decorView, button);

    }

    /**
     *
     * @// FIXME: 2015-09-25
     * @throws Exception
     */
    public void testOnCreate() throws Exception {
        Log.d("TestDebug","testOnCreate");

        activity = getActivity();

        String id = settings.getString(MainActivity.ID_FIELD,"");
        Log.d("TestDebug",String.format("Id '%s' fetched.",id));
        assertNotSame("Fields are the same. Id: '"+id+"'","",id);


    }

    /**
     * Tests that the name is saved when the button is clicked
     * and also that the Menu-activity is started.
     *
     * @throws Exception
     */
    @UiThreadTest
    public void testOnClick() throws Exception {
        String expected = "testName";

        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(
                MenuActivity.class.getName(), null, true
        );

        activity = getActivity();

        textField = (EditText) activity.findViewById(R.id.nameInputField);
        button = (Button) activity.findViewById(R.id.nameOkButton);

        textField.setText(expected);

        // Click the button (duh)
        button.callOnClick();

        String actualText = textField.getText().toString();

        // Check that the field is filled
        assertEquals(expected, actualText);

        String name = settings.getString(MainActivity.NAME_FIELD,"");

        // Check that the name was actually set in settings.
        assertEquals(expected,name);

        // Check that the menu was started.
        assertEquals(1, monitor.getHits());
    }
}
