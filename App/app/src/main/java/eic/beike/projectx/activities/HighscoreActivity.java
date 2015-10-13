package eic.beike.projectx.activities;

import android.app.DialogFragment;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import eic.beike.projectx.R;
import eic.beike.projectx.network.projectXServer.Database;
import eic.beike.projectx.network.projectXServer.IDatabase;
import eic.beike.projectx.util.HighscoreAdapter;
import eic.beike.projectx.util.MessageDialog;
import eic.beike.projectx.util.ScoreEntry;


/**
 * @author Adam
 */
public class HighscoreActivity extends ListActivity
        implements MessageDialog.MessageDialogListener
{

    HighscoreAdapter adapter;
    IDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new Database();

        // Use the asynchronous task to get the top list.
        new TopListFetchTask().execute();

        adapter = new HighscoreAdapter(this, new ArrayList<ScoreEntry>());
        setListAdapter(adapter);
    }


    /**
     * Updates the adapter with new data, clearing old.
     * @param data New data for the adapter
     */
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
     * Used to finish the activity of ok is clicked.
     * @param dialog The triggering dialog.
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        finish();
    }

    /**
     * Used to finish the activity if the dialog is dismissed, i.e. user pressed beside the dialog.
     * @param dialog The triggering dialog.
     */
    @Override
    public void onDialogDismiss(DialogFragment dialog) {
        finish();
    }

    /**
     * If the negative (no) button is clicked. Not used.
     * @param dialog Triggering dialog
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) { /* Unused. */ }

    /**
     * Inner class to asynchronously fetch the table.
     */
    class TopListFetchTask extends AsyncTask<String, Void, List<ScoreEntry>> {

        private Exception exception;

        /**
         * Runs in the asynchronous thread
         * @param urls
         * @return The list from the database.
         */
        protected List<ScoreEntry> doInBackground(String... urls) {
            try {

                if (db == null) {
                    throw new Exception("No database object");
                }

                return db.getTopTen();

            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        /**
         * Runs after doInBackground and is run in the ui-thread.
         * @param data The result from doInBackground (The top list)
         */
        protected void onPostExecute(List<ScoreEntry> data) {
            if (this.exception != null) {

                MessageDialog dialog = new MessageDialog();
                dialog.show(getFragmentManager(), "highscore_unavailable");
                Log.d("Score", "Exception while fetching top list: " + this.exception.getMessage());

            } else {
                setData(data);
            }
        }
    }
}
