package eic.beike.projectx.model;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eic.beike.projectx.network.busdata.BusCollector;
import eic.beike.projectx.network.busdata.BusData;
import eic.beike.projectx.network.busdata.Sensor;
import eic.beike.projectx.network.busdata.SimpleBusCollector;
import eic.beike.projectx.util.Colour;
import eic.beike.projectx.util.Constants;

/**
 * @author Mikael
 * @author Adam
 */
public class GameModel extends Thread implements IGameModel{

    private BusCollector busCollector;
    private Button[][] buttons;
    private Count count;
    private int pressedC = -1;
    private int pressedR = -1;

    /**
     * Persistent total score
     */
    private int score = 0;

    private int bonus = 0;

    private Handler handler;


    public GameModel(Handler handler){
        super();

        busCollector = new SimpleBusCollector();
        busCollector.chooseBus(BusCollector.TEST_BUSS_VIN_NUMBER);
        this.handler = handler;
        buttons = generateNewButtons();
        count = new Count();
    }

    /**
     * Used to notify the handlers about a new score
     * @param latestScore The latest score the player received
     */
    protected synchronized void triggerNewScore(int latestScore) {
        if (handler == null) {
            return;
        }
        Log.d("Score", Thread.currentThread().getName() + ":triggerNewScore");
        this.score += latestScore;
        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString("operation", Constants.UPDATESCORE);
        data.putInt("score", this.score);
        data.putInt("latest_score", latestScore);
        data.putInt("bonus_score", 0);

        msg.setData(data);
        msg.sendToTarget();
    }

    private synchronized void triggerNewBonus(int bonus) {
        if (handler == null) {
            return;
        }

        this.bonus += bonus;
        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString("operation", Constants.BONUSBUTTON);
        data.putInt("bonus", this.bonus);

        msg.setData(data);
        msg.sendToTarget();

    }

    private synchronized void triggerDeselectButton(int row,int column) {
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

    private synchronized void triggerSelectButton(int row, int column) {
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

    private synchronized void triggerSwopButtons(int row, int column) {
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

    private synchronized void triggerNewButton(int row, int column, int androidColor) {
        if(handler == null) {
            return;
        }

        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putString("operation", Constants.UPDATEBOARD);
        data.putInt("row", row);
        data.putInt("column", column);
        data.putInt("colour", androidColor);

        msg.setData(data);
        msg.sendToTarget();
    }



    @Override
    public void claimBonus() {
        Long currentTime = System.currentTimeMillis();
        BusData data = busCollector.getBusData(currentTime, Sensor.Stop_Pressed);
        count.count(currentTime, data.timestamp);
    }

    @Override
    public void pressButton(int row, int column) {
        if(pressedR < 0 && pressedC < 0) {
            pressedR = row;
            pressedC = column;
            triggerSelectButton(row,column);
        } else if(isSame(row, column)) {
            triggerDeselectButton(row, column);
            pressedR = -1;
            pressedC = -1;
        } else if (isNeighbour(row, column)) {
            //Valid swap, swap and deselect and remember to update ui
            triggerSwopButtons(row, column);
            swapButtons(row, column);
            triggerDeselectButton(pressedR, pressedC);
            pressedR = -1;
            pressedC = -1;
            count.sum(buttons);
        } else {
            // Clicked button far away, select it and deselect prev selected.
            triggerDeselectButton(pressedR,pressedC);
            pressedR = row;
            pressedC = column;
            triggerSelectButton(pressedR, pressedC);
        }
    }

    private boolean isSame(int row, int column) {
       return pressedR == row && pressedC == column;
    }

    private void swapButtons(int row, int column) {
        Button temp = buttons[pressedR][pressedC];
        buttons[pressedR][pressedC] = buttons[row][column];
        buttons[row][column] = temp;
    }

    public void generateButtons() {
        Random random = new Random();
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                if (buttons[i][j].counted) {
                    buttons[i][j] = new Button(Colour.colour(random.nextInt(3)), random.nextInt(100));
                    triggerNewButton(i, j, buttons[i][j].colour.getAndroidColor());
                }
            }
        }
    }

    private Button[][] generateNewButtons() {
        Button[][] tempList = new Button[3][3];
        Random random = new Random();
        for (int i = 0; i < tempList.length; i++) {
            for (int j = 0; j < tempList.length; j++) {
                tempList[i][j] = new Button(Colour.colour(random.nextInt(3)), random.nextInt(100));
                triggerNewButton(i, j, tempList[i][j].colour.getAndroidColor());
            }
        }
        return tempList;
    }

     private boolean isNeighbour(int row, int column) {

         if(row == pressedR && (column+1 == pressedC || column-1 == pressedC)) {
            return true;
         } else if(column == pressedC && (row+1 == pressedR || row-1 == pressedR)) {
             return true;
         } else {
             return false;
         }
     }

}
