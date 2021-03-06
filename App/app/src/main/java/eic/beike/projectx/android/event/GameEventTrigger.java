package eic.beike.projectx.android.event;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import static eic.beike.projectx.android.event.Keys.*;
import static eic.beike.projectx.android.event.Operations.*;

/**
 * Used to trigger/create a new event that is handled in GameEventHandler. It uses Messages to bundle
 * data that is sent. This is done to allow worker threads to update the GUI.
 *
 */
public class GameEventTrigger implements IGameEventTrigger {

    private Handler handler;

    public GameEventTrigger(Handler handler){
        this.handler = handler;
    }


    /**
     * Used to notify the handlers about a new factor
     * @param latestFactor The latest factor the player received
     */
    @Override
    public synchronized void triggerNewFactor(double latestFactor) {
        if (handler == null) {
            return;
        }
        Log.d("Score", Thread.currentThread().getName() + ":triggerNewFactor");
        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString(OPERATION, UPDATEFACTOR);
        data.putDouble(PERCENT, latestFactor);

        msg.setData(data);
        msg.sendToTarget();
    }

    /**
     * Notifies the handlers that the score should be uppdated
     * @param score The player's new score
     */
    @Override
    public synchronized void triggerNewScore(int score) {
        if (handler == null) {
            return;
        }

        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString(OPERATION, UPDATESCORE);
        data.putInt(SCORE, score);

        msg.setData(data);
        msg.sendToTarget();

    }

    /**
     * Notifies the ui that a button should be deselected
     *
     * @param row The buttons row number
     * @param column The buttons column number
     */
    @Override
    public synchronized void triggerDeselectButton(int row,int column) {
        if(handler == null) {
            return;
        }

        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString(OPERATION, DESELECTBUTTON);
        data.putInt(ROW, row);
        data.putInt(COLUMN, column);

        msg.setData(data);
        msg.sendToTarget();
    }

    @Override
    public synchronized void triggerSelectButton(int row, int column) {
        if(handler == null) {
            return;
        }

        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString(OPERATION, SELECTBUTTON);
        data.putInt(ROW, row);
        data.putInt(COLUMN, column);

        msg.setData(data);
        msg.sendToTarget();
    }

    @Override
    public synchronized void triggerSwapButtons(int row, int column, int pressedR, int pressedC) {
        if(handler == null) {
            return;
        }

        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString(OPERATION, SWAPBUTTON);
        data.putInt(ROW_1, pressedR);
        data.putInt(ROW_2, row);
        data.putInt(COLUMN_1, pressedC);
        data.putInt(COLUMN_2, column);

        msg.setData(data);
        msg.sendToTarget();
    }

    @Override
    public synchronized void triggerNewButton(int row, int column, int androidColor) {
        if(handler == null) {
            return;
        }

        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString(OPERATION, UPDATEBOARD);
        data.putInt(ROW, row);
        data.putInt(COLUMN, column);
        data.putInt(COLOR, androidColor);

        msg.setData(data);
        msg.sendToTarget();
    }

    /**
     * Notifies the ui to show an error message.
     * @param errorText The text of the error
     */
    @Override
    public void triggerError(String errorText) {
        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString(OPERATION, EXCEPTION);
        data.putString(ERROR, errorText);
        data.putBoolean(ERROR, true);
        msg.setData(data);
        msg.sendToTarget();
    }

    /**
     * Notifies the ui that the round has ended
     * @param score The final score the user got.
     */
    @Override
    public  synchronized void triggerEndRound(double score){
        if(handler == null) {
            return;
        }

        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString(OPERATION, ENDROUND);
        data.putDouble(SCORE, score);

        msg.setData(data);
        msg.sendToTarget();
    }
}
