package eic.beike.projectx.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.*;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import android.widget.Toast;
import eic.beike.projectx.R;
import eic.beike.projectx.handlers.GameHandler;
import eic.beike.projectx.handlers.ITriggers;
import eic.beike.projectx.handlers.UITriggers;
import eic.beike.projectx.model.GameModel;
import eic.beike.projectx.model.IGameModel;
import eic.beike.projectx.network.busdata.SimpleBusCollector;
import eic.beike.projectx.network.projectXServer.Database;
import eic.beike.projectx.network.projectXServer.IDatabase;

/**
 * @author Mikael
 * @author Adam
 * @author Alex
 * @author Simon
 */
public class GameActivity extends Activity {

    private IDatabase db = new Database();

    /**
     * The model used to decide what should be run
     */
    private IGameModel gameModel;

    private Animation bumpButton;
    private Animation fadeAnimation;

    private int gridButton[][] = new int[3][3];


    /**********************************************************************
     * Methods dealing with the life cycle
     **********************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Score", Thread.currentThread().getName() + ":onCreate");

        //TODO: Decide how to create.
        GameHandler handler  = new GameHandler(Looper.getMainLooper(), this);
        ITriggers triggers = new UITriggers(handler);
        gameModel = new GameModel(triggers);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Record score in database and switch to the high score activity. This is done in a background
     * thread since we need to make a network operation.
     *
     * @param score the score that should be recorded.
     */
    public void endRound(int score) {
        Toast.makeText(getApplicationContext(), "The round is over!", Toast.LENGTH_LONG).show();

        //Register the score on a background thread and then switch activity.
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... scores) {
                boolean success = db.recordScore("alex",10,System.currentTimeMillis(),"Ericsson$100020");
                //TODO: Get correct id, the ids need to registered in the db from the name splash activity.
                db.recordScore("alex", scores[0], System.currentTimeMillis(),
                               SimpleBusCollector.getInstance().getVinNumber());
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                Intent intentBusWaiting = new Intent(getApplicationContext(), HighscoreActivity.class);
                startActivity(intentBusWaiting);
            }
        }.execute(score);
    }


    /**********************************************************************
     *                  Methods used for event listening
     **********************************************************************/

    /**
     * Called when the claimBonus button is pressed. Delegates the press to the model.
     */
    public void onBonusButtonClick(View view) {
        if (view.getId() == R.id.claimBonus) {
            view.startAnimation(bumpButton);
            gameModel.claimBonus();
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
        return;
    }

    /***********************************************************************
     *                  Methods used to update the UI
     ***********************************************************************/


    /**
     * Used to update the score
     */
    public void updateScore(int latestScore, int bonusScore, int totalScore) {
        TextView total = (TextView) findViewById(R.id.totalScore);
        TextView last = (TextView) findViewById(R.id.lastScore);
        Button bonus = (Button) findViewById(R.id.claimBonus);

        total.setText(String.valueOf(totalScore));
        last.setText(String.valueOf(latestScore));
        bonus.setText(String.valueOf(bonusScore));
    }

    public void updateBonus(int bonusScore) {
        Button bonus = (Button) findViewById(R.id.claimBonus);

        bonus.setText(String.valueOf(bonusScore));
    }


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

    public void swapButtons(int row1, int row2, int column1, int column2) {
        ImageButton button1 = (ImageButton) findViewById(gridButton[row1][column1]);
        ImageButton button2 = (ImageButton) findViewById(gridButton[row2][column2]);

        Drawable color1 = (Drawable) button1.getDrawable();
        Drawable color2= (Drawable) button2.getDrawable();

        button1.startAnimation(fadeAnimation);
        button1.setImageDrawable(color2);

        button2.startAnimation(fadeAnimation);
        button2.setImageDrawable(color1);
    }

    /**
     * Highlights the specified grid button.
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
}
