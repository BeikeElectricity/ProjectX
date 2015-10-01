package eic.beike.projectx.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;

import eic.beike.projectx.R;
import eic.beike.projectx.util.HighscoreAdapter;
import eic.beike.projectx.util.ScoreEntry;

public class HighscoreActivity extends ListActivity {

    HighscoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<ScoreEntry> data = getData();

        adapter = new HighscoreAdapter(this,data);
        setListAdapter(adapter);
    }

    /**
     * Fetches the current highscore from the database
     * @return Array of ScoreEntry that contains the current highscore
     */
    private ArrayList<ScoreEntry> getData() {

        // TODO: Get data from database
        ScoreEntry[] data = {
                new ScoreEntry("Person 1", "10 000"),
                new ScoreEntry("Person 2", "6"),
                new ScoreEntry("Person 3", "6"),
                new ScoreEntry("Person 4", "6"),
                new ScoreEntry("Person 5", "6"),
                new ScoreEntry("Person 6", "6"),
                new ScoreEntry("Person 8", "6"),
                new ScoreEntry("Person 9", "6"),
                new ScoreEntry("Person 10", "6"),
                new ScoreEntry("You", "iPhone")
        };
        return new ArrayList<ScoreEntry>(Arrays.asList(data));
    }

    public void setData(ArrayList<ScoreEntry> data) {
        adapter.clear();
        for (ScoreEntry se : data) {
            adapter.add(se);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_highscore, menu);
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
