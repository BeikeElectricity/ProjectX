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

        if (data.getBoolean("error",false)) {
            game.showErrorDialog();
            game.finish();
        }

        //See what operation to perform.
        String operation = data.getString("operation");
        if (operation.equals(Constants.UPDATESCORE)) {
            updateScore(data);

        } else if (operation.equals(Constants.SELECTBUTTON)) {
            selectButton(data);

        } else if (operation.equals(Constants.DESELECTBUTTON)) {
            deselectButton(data);

        } else if (operation.equals(Constants.UPDATEBOARD)) {
            updateBoard(data);

        } else if (operation.equals(Constants.BONUSBUTTON)) {
            updateBonus(data);
        } else if (operation.equals(Constants.SWOPBUTTON)) {
            swapButton(data);
        } else if( operation.equals(Constants.ENDROUND)){
            endRound(data);
        }

        else {
            if (operation != null) {
                Log.d(getClass().getSimpleName(), "Tried to perform unknown operation!");

            } else {
                Log.d(getClass().getSimpleName(), "Malformed operation message!");

            }
        }
    }

    private void swapButton(Bundle data) {
        int row1 = data.getInt("row1");
        int row2 = data.getInt("row2");
        int column1 = data.getInt("column1");
        int column2 = data.getInt("column2");

        game.swapButtons(row1, row2, column1, column2);
    }

    private void updateBonus(Bundle data) {
        int bonus = data.getInt("bonus");

        game.updateBonus(bonus);
    }

    private void updateScore(Bundle data){
        int totalScore = data.getInt("score");
        int latestScore = data.getInt("latest_score");
        int bonusScore = data.getInt("bonus_score");
        game.updateScore(latestScore, bonusScore, totalScore);
    }

    private void selectButton(Bundle data){
        int row = data.getInt("row");
        int column = data.getInt("column");

        game.selectButton(row, column);
    }

    private void deselectButton(Bundle data){
        int row = data.getInt("row");
        int column = data.getInt("column");
        game.deselectButton(row, column);
    }

    private void updateBoard(Bundle data){
        int row = data.getInt("row");
        int column = data.getInt("column");
        int colour = data.getInt("color");

        game.updateButton(row, column, colour);
    }

    private void endRound(Bundle data){
        int score = data.getInt("score");

        game.endRound(score);
    }
}
