package eic.beike.projectx.activities;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
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

        // Use the asynchronous task to get the top list.
        new TopListFetchTask().execute();

        adapter = new HighscoreAdapter(this, new ArrayList<ScoreEntry>());
        setListAdapter(adapter);
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

    /**
     * Inner class to asynchronously fetch the table.
     */
    class TopListFetchTask extends AsyncTask<String, Void, List<ScoreEntry>> {

        private Exception exception;

        protected List<ScoreEntry> doInBackground(String... urls) {
            try {
                if (db == null) {
                    throw new Exception("No database object");
                }
                List<ScoreEntry> data = db.getTopTen();
                return data;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(List<ScoreEntry> data) {
            if (this.exception != null) {
                Log.d("Score", "Exeption while fetching top list: " + this.exception.getMessage());
                finish();
            } else {
                setData(data);
            }
        }
    }
}
