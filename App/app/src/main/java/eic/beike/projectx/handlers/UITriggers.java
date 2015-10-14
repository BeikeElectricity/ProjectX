package eic.beike.projectx.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import eic.beike.projectx.util.Constants;

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
    public synchronized void triggerNewScore(int latestScore, int totalscore) {
        if (handler == null) {
            return;
        }
        Log.d("Score", Thread.currentThread().getName() + ":triggerNewScore");
        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString("operation", Constants.UPDATESCORE);
        data.putInt("score", totalscore);
        data.putInt("latest_score", latestScore);

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

        data.putString("operation", Constants.BONUSBUTTON);
        data.putInt("bonus", bonus);

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

        data.putString("operation", Constants.DESELECTBUTTON);
        data.putInt("row", row);
        data.putInt("column", column);

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

        data.putString("operation", Constants.SELECTBUTTON);
        data.putInt("row", row);
        data.putInt("column", column);

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

        data.putString("operation", Constants.SWOPBUTTON);
        data.putInt("row1", pressedR);
        data.putInt("row2", row);
        data.putInt("column1", pressedC);
        data.putInt("column2", column);

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

        data.putString("operation", Constants.UPDATEBOARD);
        data.putInt("row", row);
        data.putInt("column", column);
        data.putInt("color", androidColor);

        msg.setData(data);
        msg.sendToTarget();
    }

    @Override
    public void triggerError(String errorText) {
        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString("exception", errorText);
        data.putBoolean("error", true);

        msg.setData(data);
        msg.sendToTarget();
    }
}
