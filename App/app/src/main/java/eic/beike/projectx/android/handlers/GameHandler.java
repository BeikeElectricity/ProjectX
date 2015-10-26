package eic.beike.projectx.android.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import eic.beike.projectx.android.activities.GameActivity;

import static eic.beike.projectx.android.handlers.Keys.*;

import static eic.beike.projectx.android.handlers.Operations.*;


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
        String operation = data.getString(OPERATION);
        if (operation.equals(UPDATESCORE)) {
            updateScore(data);

        } else if (operation.equals(SELECTBUTTON)) {
            selectButton(data);

        } else if (operation.equals(DESELECTBUTTON)) {
            deselectButton(data);

        } else if (operation.equals(UPDATEBOARD)) {
            updateBoard(data);

        } else if (operation.equals(BONUSBUTTON)) {
            updateBonus(data);
        } else if (operation.equals(SWOPBUTTON)) {
            swapButton(data);
        } else if(operation.equals(ENDROUND)){
            endRound(data);
        } else if(operation.equals(EXCEPTION)) {
            errorException(data);
        }

        else {
            if (operation != null) {
                Log.d(getClass().getSimpleName(), "Tried to perform unknown operation!");

            } else {
                Log.d(getClass().getSimpleName(), "Malformed operation message!");

            }
        }
    }

    private void errorException(Bundle data) {
        if (data.getBoolean(ERROR,false)) {
            game.showErrorDialog(data.getString(ERROR));
            game.finish();
        }
    }

    private void swapButton(Bundle data) {
        int row1 = data.getInt(ROW_1);
        int row2 = data.getInt(ROW_2);
        int column1 = data.getInt(COLUMN_1);
        int column2 = data.getInt(COLUMN_2);

        game.swapButtons(row1, row2, column1, column2);
    }

    private void updateBonus(Bundle data) {
        int bonus = data.getInt(BONUS);

        game.updateBonus(bonus);
    }

    private void updateScore(Bundle data){
        double percent = data.getDouble(PERCENT);
        game.updateScore(percent);
    }

    private void selectButton(Bundle data){
        int row = data.getInt(ROW);
        int column = data.getInt(COLUMN);

        game.selectButton(row, column);
    }

    private void deselectButton(Bundle data){
        int row = data.getInt(ROW);
        int column = data.getInt(COLUMN);
        game.deselectButton(row, column);
    }

    private void updateBoard(Bundle data){
        int row = data.getInt(ROW);
        int column = data.getInt(COLUMN);
        int colour = data.getInt(COLOR);

        game.updateButton(row, column, colour);
    }

    private void endRound(Bundle data){
        int score = (int) data.getDouble(SCORE);

        game.endRound(score);
    }
}
