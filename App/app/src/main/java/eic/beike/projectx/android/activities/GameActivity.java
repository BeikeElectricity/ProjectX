package eic.beike.projectx.android.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.*;
import android.widget.ImageButton;
import android.widget.TextView;

import eic.beike.projectx.R;
import eic.beike.projectx.android.activities.highScore.HighscoreActivity;
import eic.beike.projectx.android.dialogs.MessageDialog;
import eic.beike.projectx.android.event.GameEventHandler;
import eic.beike.projectx.android.event.IGameEventTrigger;
import eic.beike.projectx.android.event.GameEventTrigger;
import eic.beike.projectx.model.GameModel;
import eic.beike.projectx.model.IGameModel;
import eic.beike.projectx.network.busdata.SimpleBusCollector;
import eic.beike.projectx.network.projectXServer.Database;
import eic.beike.projectx.network.projectXServer.IDatabase;
import eic.beike.projectx.util.Constants;

/**
 * The GameActivity initiates the game, sends user input to the model and
 * lets the model update the view. This class also registers the score and
 * switch activity once the round is over. This is something of a god object
 * but Android feels geared towards fat activities.
 *
 * @author Mikael
 * @author Adam
 * @author Alex
 * @author Simon
 */
public class GameActivity extends Activity
        implements MessageDialog.MessageDialogListener
{

    /**
     * A database object to register the score. We would like
     * to update the Database class to follow the singleton pattern
     * in the same way as the BusCollector does.
     */
    private IDatabase db = new Database();

    /**
     *  Used to pass a task to onDialogDismiss.
     *  In android dialogs are asynchronous but we
     *  want to wait before we record a score and switch activity.
     */
    private AsyncTask<Void,Void,Boolean> postDialogTask;


    /**
     * The model used to decide what should be run
     */
    private IGameModel gameModel;

    /**
     * Our fancy animations.
     */
    private Animation bumpButton;
    private Animation fadeAnimation;

    /**
     * This holds the ids of the buttons in the activity so that
     * we don't need to look them up every time.
     */
    private int gridButton[][] = new int[3][3];


    /**********************************************************************
     * Methods dealing with the life cycle
     **********************************************************************/

    /**
     *  Setup animations and choose layout, this is done once in the activity life cycle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Score", Thread.currentThread().getName() + ":onCreate");
        setContentView(R.layout.activity_game);

        //Get the ids of the grid buttons
        gridButton[0][0] = R.id.topLeft;
        gridButton[0][1] = R.id.topMiddle;
        gridButton[0][2] = R.id.topRight;
        gridButton[1][0] = R.id.middleLeft;
        gridButton[1][1] = R.id.middleMiddle;
        gridButton[1][2] = R.id.middleRight;
        gridButton[2][0] = R.id.bottomLeft;
        gridButton[2][1] = R.id.bottomMiddle;
        gridButton[2][2] = R.id.bottomRight;

        //Initiate bump animation.
        bumpButton = new ScaleAnimation(1f, 1.2f, 1f, 1.2f, 50f, 50f);
        bumpButton.setDuration(250);
        bumpButton.setRepeatMode(Animation.REVERSE);
        bumpButton.setRepeatCount(1);
        bumpButton.setInterpolator(new DecelerateInterpolator());

        //Initiate fade animation.
        fadeAnimation = new AlphaAnimation(1,0);
        fadeAnimation.setDuration(250);
        bumpButton.setRepeatMode(Animation.REVERSE);
        bumpButton.setRepeatCount(1);
        bumpButton.setInterpolator(new DecelerateInterpolator());
    }

    /**
     * Every time this view is displayed a new game is generated.
     */
    @Override
    protected void onStart(){
        super.onStart();

        GameEventHandler handler  = new GameEventHandler(Looper.getMainLooper(), this);
        IGameEventTrigger triggers = new GameEventTrigger(handler);
        gameModel = new GameModel(triggers);
    }

    /**
     * When the game is no longer visible the round is aborted.
     */
    @Override
    protected void onStop(){
        super.onStop();
        
        if(gameModel != null){
            gameModel.abortRound();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Display a dialog with the score and record that score after the dialog is dismissed.
     * The score is recorded on a separate thread and when it's done it starts the HighScoreActivity.
     *
     * @param score the score that should be recorded.
     */
    public void endRound(final int score) {
        // Display a dialog of achieved score.
        MessageDialog msg = new MessageDialog();
        msg.setMessage("Your score was " + Integer.toString(score)+ "!");
        msg.show(getFragmentManager(), "show_round_score");

        SharedPreferences settings = getSharedPreferences(Constants.SETTINGS_FILE, 0);

        final String id = settings.getString(Constants.ID_FIELD, "");

        //Create a task that should be run when the dialog is dismissed.
        postDialogTask = new AsyncTask<Void, Void, Boolean>() {
           // Register the score on a background thread and then switch activity.
            @Override
            protected Boolean doInBackground(Void... v) {
                try {
                    db.recordScore(id, score, System.currentTimeMillis(),
                                   SimpleBusCollector.getInstance().getVinNumber());
                } catch (Exception e) {
                    Log.d("GameActivity","Error ending round: "+e.getMessage());
                    return true;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean error) {
                if (error) {
                    gameModel.triggerError("Could not reach the internet.");
                }
                Intent intentBusWaiting = new Intent(getApplicationContext(), HighscoreActivity.class);
                startActivity(intentBusWaiting);
            }
        };
    }


    /**********************************************************************
     *                  Methods used for user event listening
     **********************************************************************/

    /**
     * Called when the  bonus button is pressed. Delegates the press to the model.
     */
    public void onBonusButtonClick(View view) {
        if (view.getId() == R.id.claimBonus) {
            view.startAnimation(bumpButton);
            gameModel.claimFactor();
        }
    }

    /**
     * Called when a grid button is pressed. Gets the position an delegates to the model.
     */
    public void onGridButtonClick(View view) {
        //Loop through the grid buttons to see which one was clicked
        for (int r = 0; r < gridButton.length; r++) {
            for (int c = 0; c < gridButton[r].length; c++) {
                if (view.getId() == gridButton[r][c]) {
                    gameModel.pressButton(r, c);
                    return;
                }
            }
        }
        //Couldn't find matching button. This is an error!
        Log.e(this.getClass().getSimpleName(), "Unknown grid button pressed!");
    }

    /***********************************************************************
     *                  Methods used to let the model update the UI
     ***********************************************************************/


    /**
     * Sets the score multiplier factor.
     */
    public void updateFactor(double latestFactor) {
        TextView last = (TextView) findViewById(R.id.Factor);
        String percent = String.valueOf(latestFactor);
        int endChar = percent.length() < 4 ?
                percent.length()
                : 4;
        last.setText(percent.substring(0, endChar));
    }

    /**
     * Sets the score in view.
     */
    public void updateScore(int latestScore) {
        TextView last = (TextView) findViewById(R.id.totalScore);
        last.setText(String.valueOf(latestScore));
    }


    /**
     * Sets the color of the button in the specified row and column.
     */

    public void updateButton(int row, int column, int colour) {
        if(row < gridButton.length && column < gridButton[row].length) {
            ImageButton button = (ImageButton) findViewById(gridButton[row][column]);
            button.startAnimation(fadeAnimation);

            button.setImageResource(colour);
        } else {
            //Input was not in the correct form, log error.
            Log.e(getClass().getSimpleName(), "Invalid argument when replacing buttons!");
        }
    }

    /**
     * Updates view with the swapped buttons
     *
     * @param row1, Row of the first button to swap
     * @param row2, Row if the second button to swap
     * @param column1, Column of the first button to swap
     * @param column2, Column of the second button to swap
     */
    public void swapButtons(int row1, int row2, int column1, int column2) {
        ImageButton button1 = (ImageButton) findViewById(gridButton[row1][column1]);
        ImageButton button2 = (ImageButton) findViewById(gridButton[row2][column2]);

        Drawable color1 = button1.getDrawable();
        Drawable color2= button2.getDrawable();

        button1.startAnimation(fadeAnimation);
        button1.setImageDrawable(color2);

        button2.startAnimation(fadeAnimation);
        button2.setImageDrawable(color1);
    }

    /**
     * Highlights the specified grid button.
     *
     * @param row, Row of the selected button
     * @param column, Column of the selected button
     */
    public void selectButton(int row, int column) {
        //Check for valid position and change alpha.
        if ((0 <= row && row < gridButton.length) &&
                (0 <= column && column < gridButton[0].length)) {
            ImageButton button = (ImageButton) findViewById(gridButton[row][column]);
            button.setAlpha(.7f);
            button.setSelected(true);
        } else {
            Log.e(getClass().getSimpleName(), "Invalid argument when selecting button!");
        }
    }

    /**
     * Deselect the specified button so that it is no longer highlighted.
     *
     * @param row, Row of the button to deselect
     * @param column, Column of the button to deselect
     */
    public void deselectButton(int row, int column) {
        //Check for valid position and change alpha.
        if ((0 <= row && row < gridButton.length) &&
                (0 <= column && column < gridButton[0].length)) {
            ImageButton button = (ImageButton) findViewById(gridButton[row][column]);
            button.setAlpha(1f);
            button.setSelected(false);
        } else {
            Log.e(getClass().getSimpleName(), "Invalid argument when deselecting button!");
        }
    }


    /**
     * Displays an errorDialog with a specified message
     */
    public void showErrorDialog(String message) {
        MessageDialog dialog = new MessageDialog();
        dialog.setMessage(message);
        dialog.show(getFragmentManager(), "bus_data_unavailable");
    }




    /**
     * Used to run optional task when the dialog is dismissed, i.e. user pressed beside the dialog or on the
     * ok button.
     *
     * @param dialog The triggering dialog.
     */
    @Override
    public void onDialogDismiss(DialogFragment dialog) {
        if (postDialogTask != null){
            postDialogTask.execute();
            postDialogTask = null;
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) { /* Unused. */ }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) { /* Unused */ }
}
