package eic.beike.projectx.model;

import android.os.Handler;
import android.util.Log;

import eic.beike.projectx.handlers.ITriggers;
import eic.beike.projectx.handlers.UITriggers;
import eic.beike.projectx.network.busdata.BusCollector;
import eic.beike.projectx.network.busdata.Sensor;
import eic.beike.projectx.network.busdata.SimpleBusCollector;

/**
 * Created by Simon on 2015-10-08.
 */
public class Count implements ScoreCountApi {

    /**
     * The game uses this counter.
     */

    private final String eopchyear = String.valueOf("1444800000000");

    private GameModel game;

    public Count(GameModel game) {
        this.game = game;
    }

    @Override
    public void count(long t1) {
        new Thread() {
            long t1;

            public void count(long t1) {
                this.t1 = t1;
                this.start();
            }

            /**
             * Runs until a Stop pressed event is found.
             */
            @Override
            public void run() {
                try {
                    this.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                BusCollector bus = SimpleBusCollector.getInstance();
                long t2 = bus.getBusData(t1, Sensor.Stop_Pressed).timestamp;
                calculate(t1,t2);
                }
        }.count(t1);
    }

    public void sum(Button[][] buttons) {
        game.addBonus(columns(buttons) + rows(buttons));
    }

    /**
     * @return returns the score fomr all buttons that are "three of a kind"
     * it also sets that they are counted so they can be generated again
     */
    private int columns(Button[][] buttons) {
        int count = 0;
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i][0].color == buttons[i][1].color
                    && buttons[i][0].color == buttons[i][2].color) {

                count += buttons[i][0].score + buttons[i][1].score + buttons[i][2].score;
                buttons[i][0].counted = true;
                buttons[i][1].counted = true;
                buttons[i][2].counted = true;
            }
        }
        return count;
    }

    /**
     * @return returns the score fomr all buttons that are "three of a kind"
     * it also sets that they are counted so they can be generated again
     */
    private int rows(Button[][] buttons) {
        int count = 0;
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[0][i].color == buttons[1][i].color
                    && buttons[1][i].color == buttons[2][i].color) {
                count += buttons[0][i].score + buttons[1][i].score + buttons[2][i].score;
                buttons[0][i].counted = true;
                buttons[1][i].counted = true;
                buttons[2][i].counted = true;
            }
        }
        return count;
    }
    /*
    * @Param t1, Time clicked
    * @Param t2, Time for signal
     */
    public synchronized void calculate(long t1, long t2) {
        if (t2 == 0) {
            game.addScore(0);
        } else if (t1 > t2) {
            t1 -= Long.getLong(eopchyear);
            t2 -= Long.getLong(eopchyear);
            game.addScore(Math.abs((int) (t1 - t2) * game.getBonus()));
        } else {
            t1 -= Long.getLong(eopchyear);
            t2 -= Long.getLong(eopchyear);
            game.addScore(Math.abs((int) (t2 - t1) * game.getBonus()));
        }
    }
}
