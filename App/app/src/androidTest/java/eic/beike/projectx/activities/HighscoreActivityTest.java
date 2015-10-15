package eic.beike.projectx.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Adapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import eic.beike.projectx.util.Constants;
import eic.beike.projectx.util.HighscoreAdapter;
import eic.beike.projectx.util.ScoreEntry;

/**
 * @author Adam
 */
public class HighscoreActivityTest extends ActivityInstrumentationTestCase2<HighscoreActivity> {

    HighscoreActivity activity;
    ArrayList<ScoreEntry> data;

    public HighscoreActivityTest() { super(HighscoreActivity.class); }


    @SuppressLint("CommitPrefEdits")
    public void setUp() throws Exception {
        super.setUp();

        ScoreEntry[] arr = new ScoreEntry[] {
                new ScoreEntry("Name1","1"),
                new ScoreEntry("Name2","2"),
                new ScoreEntry("Name3","3"),
                new ScoreEntry("Name4","4"),
                new ScoreEntry("Name5","5")
        };
        data = new ArrayList<ScoreEntry>(Arrays.asList(arr));
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void tearDown() throws Exception {

        if (this.activity != null) {
            this.activity.finish();
        }

        this.activity = null;

        // Clear settings
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences settings = context.getSharedPreferences(Constants.SETTINGS_FILE, 0);
        settings.edit().clear().commit();


        super.tearDown();
    }

    /**
     * Test that the activity can start properly and its the right activity.
     */
    public void testActivityExits() {
        activity = getActivity();
        assertNotNull(activity);
        assertSame(HighscoreActivity.class, activity.getClass());
    }

    /**
     * Tests that the list is being populated
     */
    public void testPopulatedData() {
        activity = getActivity();

        ListView listView = activity.getListView();

        assertNotNull(listView);

        Handler handler = activity.makeHandler();
        Bundle bundle = new Bundle();
        StringBuilder sb = new StringBuilder();

        for (ScoreEntry entry : data) {
            String row = entry.getName() + ":" + entry.getScore();
            sb.append(row).append(",");
        }

        sb.deleteCharAt(sb.lastIndexOf(","));

        bundle.putString("scores", sb.toString());
        bundle.putBoolean("update_data", true);

        Message msg = handler.obtainMessage();
        msg.setData(bundle);

        // Wait so that the original database call gets through
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        msg.sendToTarget();

        // Sleep some to make sure the data is updated.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Adapter adapter = listView.getAdapter();

        assertNotNull(adapter);
        assertSame(adapter.getClass(), HighscoreAdapter.class);
        assertEquals(adapter.getCount(),data.size());
    }

}