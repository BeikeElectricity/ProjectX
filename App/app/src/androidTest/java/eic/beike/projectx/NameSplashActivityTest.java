package eic.beike.projectx;

import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import junit.framework.TestCase;

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

    public NameSplashActivityTest() {
        super(NameSplashActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);


        // Clear name from settings
        context = getInstrumentation().getContext();
        settings = context.getSharedPreferences(MainActivity.SETTINGS_FILE, 0);
        Log.d("TestDebug","Settings cleared");
        settings.edit().clear().commit();

        activity = getActivity();

        textField = (EditText) activity.findViewById(R.id.nameInputField);
        button = (Button) activity.findViewById(R.id.nameOkButton);


    }

    /**
     * Tests that the activity starts and the fields and button are present
     * @throws Exception
     */
    public void testActivityExists() throws Exception {
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

        //TODO
//        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(
//                Menu.class.getName(), null, false
//        );


        textField.setText(expected);

        // Click the button (duh)
        button.callOnClick();

        String actualText = textField.getText().toString();

        // Check that the field is filled
        assertEquals(expected, actualText);

        String name = settings.getString(MainActivity.NAME_FIELD,"");

        // Check that the name was actually set in settings.
        assertEquals(expected,name);

        //TODO
        // Check that the menu was started.
//        assertEquals(1, monitor.getHits());
    }
}