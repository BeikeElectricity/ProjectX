package eic.beike.projectx.activities;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

/**
 * Created by Simon on 2015-10-14.
 */
public class GameActivityTest extends ActivityInstrumentationTestCase2<GameActivity> {

    GameActivity activity;

    public GameActivityTest() {
        super(GameActivity.class);
    }



    public void tearDown() throws Exception {

        if (this.activity != null) {
            this.activity.finish();
        }

        this.activity = null;

        super.tearDown();
    }

    /**
     * Test that the activity can start properly and its the right activity.
     */
    public void testActivityExits() {
        assertNotNull(activity);
        assertSame(GameActivity.class, activity.getClass());
    }

}
