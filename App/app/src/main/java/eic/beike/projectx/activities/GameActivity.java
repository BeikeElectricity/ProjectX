package eic.beike.projectx.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import eic.beike.projectx.R;
import eic.beike.projectx.handlers.GameHandler;
import eic.beike.projectx.model.GameModel;
import eic.beike.projectx.model.IGameModel;

/**
 * @author Mikael
 * @author Adam
 * @author Alex
 */
public class GameActivity extends Activity {

    private IGameModel gameModel;

    /**********************************************************************
     *                  Methods dealing with the life cycle
     **********************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Score", Thread.currentThread().getName() + ":onCreate");
        Handler h = makeHandler();

        gameModel = new GameModel();
        setContentView(R.layout.activity_game);
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
            gameModel.claimBonus();
        }
    }

    /**
     * Called when a grid button is pressed. Gets the position an delegates to the model.
     */
    public void onGridButtonClick(View view) {
        switch (view.getId()){
            case R.id.topLeft:
                gameModel.pressButton(0,0);
                break;
            case R.id.topMiddle:
                gameModel.pressButton(0,1);
                break;
            case R.id.topRight:
                gameModel.pressButton(0,2);
                break;
            case R.id.middleLeft:
                gameModel.pressButton(1,0);
                break;
            case R.id.middleMiddle:
                gameModel.pressButton(1,1);
                break;
            case R.id.middleRight:
                gameModel.pressButton(1,2);
                break;
            case R.id.bottomLeft:
                gameModel.pressButton(2,0);
                break;
            case R.id.bottomMiddle:
                gameModel.pressButton(2,1);
                break;
            case R.id.bottomRight:
                gameModel.pressButton(2,2);
                break;
            default:
                Log.e(this.getClass().getSimpleName(), "Unknown gridbutton pressed!");
                break;
        }

    }

    /***********************************************************************
     *                  Methods used to update the UI
     ***********************************************************************/


    /**
     * Used to update the score
     *
     * @param totalScore New score that the user got
     */
    public void showScore(int latestScore, int totalScore) {
        Log.d("Score", Thread.currentThread().getName() + ":onNewScore");
        TextView scoreText = (TextView) findViewById(R.id.textScore);
        TextView scoreEventText = (TextView) findViewById(R.id.textScoreEvent);


        scoreText.setText(String.valueOf(totalScore));
        scoreEventText.setText(String.format("Du fick %d poäng", latestScore));
    }


    /*******************************************************************************
     *                  Utility private methods
     *******************************************************************************/


    /**
     * Creates a handler to update the ui
     *
     * @return The handler
     */
    private Handler makeHandler() {

        return new GameHandler(Looper.getMainLooper(), this);
//        new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message msg) {
//                Log.d("Score", Thread.currentThread().getName() + ":handleMessage");
//                Bundle data = msg.getData();
//                int totalScore = data.getInt("score");
//                int latestScore = data.getInt("latest_score");
//                onNewScore(latestScore, totalScore);
//            }
//        };

    }
}
