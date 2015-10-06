package eic.beike.projectx.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import eic.beike.projectx.R;
import eic.beike.projectx.network.projectXServer.Database;
import eic.beike.projectx.network.projectXServer.IDatabase;
import eic.beike.projectx.util.HighscoreAdapter;
import eic.beike.projectx.util.ScoreEntry;


/**
 * @author Adam
 */
public class HighscoreActivity extends ListActivity {

    HighscoreAdapter adapter;
    IDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        db = new Database();

        List<ScoreEntry> data = getData();

        adapter = new HighscoreAdapter(this, data);
        setListAdapter(adapter);
    }

    /**
     * Fetches the current highscore from the database
     * @return Array of ScoreEntry that contains the current highscore
     */
    private List<ScoreEntry> getData() {
        return db.getTopTen();
    }

    public void setData(List<ScoreEntry> data) {
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
