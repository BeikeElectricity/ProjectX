package eic.beike.projectx.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import eic.beike.projectx.R;
import eic.beike.projectx.busdata.Sensor;
import eic.beike.projectx.handlers.GameHandler;
import eic.beike.projectx.model.UserEvent;
import eic.beike.projectx.model.GameModel;

/**
 * @author Mikael
 * @author Adam
 */
public class GameActivity extends Activity {

    private GameModel gameModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("Score", Thread.currentThread().getName() + ":onCreate");
        Handler h = makeHandler();

        gameModel = new GameModel();
        gameModel.setHandler(h);
        gameModel.start();
        setContentView(R.layout.activity_game);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameModel.stopLoop();
    }

    /**
     * @param v The input form the "Stop Button" button. Not used, but required
     */
    public void onStopButton(View v) {
        gameModel.onClick(new UserEvent(System.currentTimeMillis(), Sensor.Stop_Pressed));
    }

    /**
     * @param v The input form the "Door Open" button. Not used, but required
     */
    public void onDoorOpen(View v) {
        gameModel.onClick(new UserEvent(System.currentTimeMillis(), Sensor.Opendoor));
    }

    public void onLeft(View v) {

    }

    public void onRight(View v) {

    }

    /**
     * Creates a handler to update the ui
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
    /**
     * Used to update the score
     * @param totalScore New score that the user got
     */
    public void showScore(int latestScore, int totalScore) {
        Log.d("Score", Thread.currentThread().getName() + ":onNewScore");
        TextView scoreText = (TextView) findViewById(R.id.textScore);
        TextView scoreEventText = (TextView) findViewById(R.id.textScoreEvent);

        scoreText.setText(String.valueOf(totalScore));
        scoreEventText.setText(String.format("Du fick %d poäng", latestScore));
    }
}
