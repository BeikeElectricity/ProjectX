package eic.beike.projectx.android.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import eic.beike.projectx.util.Constants;

import static eic.beike.projectx.android.handlers.Keys.*;
import static eic.beike.projectx.android.handlers.Operations.*;

/**
 * Used to update the game activity from any thread.
 */
public class UITriggers implements ITriggers {

    private Handler handler;

    public UITriggers( Handler handler){
        this.handler = handler;
    }


    /**
     * Used to notify the handlers about a new score
     * @param latestScore The latest score the player received
     */
    @Override
    public synchronized void triggerNewScore(double latestScore) {
        if (handler == null) {
            return;
        }
        Log.d("Score", Thread.currentThread().getName() + ":triggerNewScore");
        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString(OPERATION, UPDATESCORE);
        data.putDouble(PERCENT, latestScore);

        msg.setData(data);
        msg.sendToTarget();
    }
    @Override
    public synchronized void triggerNewBonus(int bonus) {
        if (handler == null) {
            return;
        }

        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString(OPERATION, BONUSBUTTON);
        data.putInt(BONUS, bonus);

        msg.setData(data);
        msg.sendToTarget();

    }
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

        data.putString(OPERATION, SWOPBUTTON);
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
