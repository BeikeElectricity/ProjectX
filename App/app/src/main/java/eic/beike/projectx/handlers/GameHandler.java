package eic.beike.projectx.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import eic.beike.projectx.activities.GameActivity;

/**
 * @author Adam
 */
public class GameHandler extends Handler {

    GameActivity game;

    public GameHandler(Looper looper, GameActivity activity) {
        super(looper);
        game = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        Log.d("Score", Thread.currentThread().getName() + ":handleMessage");

        Bundle data = msg.getData();
        if (data.getBoolean("error",false)) {
            game.showErrorDialog();
            game.finish();
        }
        int totalScore = data.getInt("score");
        int latestScore = data.getInt("latest_score");
        game.showScore(latestScore, totalScore);
    }

}
