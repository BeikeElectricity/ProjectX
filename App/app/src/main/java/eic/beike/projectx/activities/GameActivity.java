package eic.beike.projectx.activities;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import eic.beike.projectx.R;
import eic.beike.projectx.handlers.GameHandler;
import eic.beike.projectx.model.GameModel;
import eic.beike.projectx.model.IGameModel;
import eic.beike.projectx.util.Colour;

/**
 * @author Mikael
 * @author Adam
 * @author Alex
 */
public class GameActivity extends Activity {

    /**
     * The model used to decide what should be run
     */
    private IGameModel gameModel;
    private Animation bumpButton;
    private Animation fadeAnimition;
    private int gridButton[][] = new int[3][3];


    /**********************************************************************
     * Methods dealing with the life cycle
     **********************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Score", Thread.currentThread().getName() + ":onCreate");

        //TODO: Decide how to create.
        gameModel = new GameModel();
        gameModel.setHandler(new GameHandler(Looper.getMainLooper(), this));
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
        fadeAnimition = new AlphaAnimation(1,0);
        fadeAnimition.setDuration(250);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        for (int r = 0; r <= gridButton.length; r++) {
            for (int c = 0; c <= gridButton[r].length; c++) {
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

    /**
     * Update the whole button board.
     */
    public void updateGridButtons(Colour[][] colour ) {
        //Walk through the input
        for(int r = 0 ; r < colour.length ; r++) {
            for (int c = 0; c < colour[r].length; c++) {
                if ((r < gridButton.length) && (c < gridButton[r].length)) {
                    //Input is valid, update the button.
                    Button button = (Button) findViewById(gridButton[r][c]);
                    //Do silly blink animation before colour change.
                    button.startAnimation(fadeAnimition);
                    button.setBackgroundColor(colour[r][c].getAndroidColor());
                } else {
                    //Input was not in the correct form, log error.
                    Log.e(getClass().getSimpleName(), "Invalid argument when replacing buttons!");
                }
            }
        }
    }

    public void swopButtons(int row1, int row2, int column1, int column2) {
        Button button1 = (Button) findViewById(gridButton[row1][column1]);
        Button button2 = (Button) findViewById(gridButton[row2][column2]);

        ColorDrawable button1Background = (ColorDrawable) button1.getBackground();
        ColorDrawable button2Background = (ColorDrawable) button2.getBackground();

        button1.startAnimation(fadeAnimition);
        button1.setBackgroundColor(button2Background.getColor());

        button2.startAnimation(fadeAnimition);
        button2.setBackgroundColor(button1Background.getColor());
    }

    /**
     * Highlights the specified grid button.
     */
    public void selectButton(int row, int column) {
        //Check for valid position and change alpha.
        if ((0 <= row && row < gridButton.length) &&
                (0 <= column && column < gridButton[0].length)) {
            Button button = (Button) findViewById(gridButton[row][column]);
            button.setAlpha(.7f);
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
            Button button = (Button) findViewById(gridButton[row][column]);
            button.setAlpha(1);
        } else {
            Log.e(getClass().getSimpleName(), "Invalid argument when deselecting button!");
        }
    }
}
