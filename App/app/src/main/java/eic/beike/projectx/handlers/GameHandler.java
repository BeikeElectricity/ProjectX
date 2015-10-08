package eic.beike.projectx.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import eic.beike.projectx.activities.GameActivity;
import eic.beike.projectx.util.Constants;

/**
 * @author Adam
 * @author Alex
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

        //See what operation to perform.
        String operation = data.getString("operation");
        switch (operation) {
            case Constants.UPDATESCORE:
                updateScore(data);
                break;
            case Constants.SELECTBUTTON:
                selectButton(data);
                break;
            case Constants.DESELECTBUTTON:
                deselectButton(data);
                break;
            case Constants.UPDATEBOARD:
                updateBoard(data);
                break;
            default:
                if(operation != null) {
                    Log.d(getClass().getSimpleName(), "Tried to perform unknown operation!");
                    break;
                } else {
                    Log.d(getClass().getSimpleName(), "Malformed operation message!");
                    break;
                }
        }
    }


    private void updateScore(Bundle data){
        //TODO: Add sanity checks!
        int totalScore = data.getInt("score");
        int latestScore = data.getInt("latest_score");
        int bonusScore = data.getInt("bonus_score");
        game.updateScore(latestScore, bonusScore, totalScore);
    }

    private void selectButton(Bundle data){
        //TODO: Implement this stub!
    }

    private void deselectButton(Bundle data){
        //TODO: Implement this stub!
    }

    private void updateBoard(Bundle data){
        //TODO: Implement this stub!
    }
}
