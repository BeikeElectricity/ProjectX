package eic.beike.projectx.model;

import eic.beike.projectx.network.busdata.SimpleBusCollector;

import android.util.Log;

import eic.beike.projectx.network.busdata.BusCollector;
import eic.beike.projectx.network.busdata.Sensor;
import eic.beike.projectx.util.Constants;

/**
 * @author Simon
 */
public class Count implements ScoreCountApi {

    /**
     * The gameModel uses this counter.
     */

    private final Long epochyear = 1444800000000l;

    private GameModel gameModel;

    private static int isRunning = 0;

    private int myIsRunning = isRunning;

    public Count(GameModel game) {
        this.gameModel = game;
    }

    /**
     * Calculate the bonus percent the player will get at the end of the round based
     * on how fast she reacted to the stop sign being lit on the bus.
     *
     * @param t1 the time the player pressed the button
     */
    @Override
    public void count(long t1) {
        //Start a thread that makes the network call and then updates the game.
        new Thread() {
            long t1;
            public void count(long t1) {
                this.t1 = t1;
                this.start();
            }

            // Runs until a Stop pressed event is found.
            @Override
            public void run() {
                try {
                    Long startTime = System.currentTimeMillis() + Constants.ONE_SECOND_IN_MILLI * 20;
                    BusCollector bus = SimpleBusCollector.getInstance();
                    Long t2 = 0l;
                    boolean hasNotCalculated = true;
                    while ((System.currentTimeMillis() < startTime) && (isRunning == myIsRunning) && hasNotCalculated) {
                        t2 = bus.getBusData(t1, Sensor.Stop_Pressed).timestamp;
                        if (t2 != 0) {
                            calculatePercent(t1, t2);
                            hasNotCalculated = false;
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (hasNotCalculated) {
                        calculatePercent(t1, t2);
                    }
                } catch (Exception e) {
                    Log.e("Count", e.getMessage()+"");
                }
            }
        }.count(t1);
    }

    /**
     *  Sum the rows and columns of the board to get their points and then give them to the player
     *  factoring in the speed of the bus.
     *
     * @param buttons the board that should be summed and updated.
     */
    public void sum(Button[][] buttons) {
        // The speed is updated every 5 seconds so we're likely to have gotten a speed
        // event within the last 5 seconds.
        final long time = System.currentTimeMillis()-5*Constants.ONE_SECOND_IN_MILLI;

        // Get the sums
        final int columns = columns(buttons);
        final int rows =  rows(buttons);

        // Factor in the speed in a separate thread.
        new Thread() {
            @Override
            public void run() {
                        try {
                            double speed = SimpleBusCollector.getInstance().getBusData(time, Sensor.GPS2).getSpeed();
                            if(isRunning == myIsRunning) {
                                //Round still active, update score!
                                gameModel.addBonus((int) (rows / (speed + 1) + columns * speed));
                            }
                        } catch (Exception e) {
                            Log.e("Count", e.getMessage() + "");
                        }
                    }
        }.start();
    }



    /**
     * @return returns the score from all buttons that are "three of a kind"
     * it also sets that they are counted so they can be generated again
     */
    private int columns(Button[][] buttons) {
        int count = 0;
        for (Button[] button : buttons) {
            if (button[0].color == button[1].color
                    && button[0].color == button[2].color) {

                count += button[0].score + button[1].score + button[2].score;
                button[0].counted = true;
                button[1].counted = true;
                button[2].counted = true;
            }
        }
        return count;
    }

    /**
     * @return returns the score from all buttons that are "three of a kind"
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
    * @param t1, time bonus button is pressed
    * @param t2, time actual stop signal
     */
    public synchronized void calculatePercent(long t1, long t2) {
        if (t2 == 0) {
            gameModel.addScore(0.3);
        } else if (t1 < t2) {
            t1 -= epochyear;
            t2 -= epochyear;
            gameModel.addScore(Math.abs(((double) t1 / (double) t2) +1));
        } else {
            t1 -= epochyear;
            t2 -= epochyear;
            gameModel.addScore(Math.abs(((double) t2 / (double) t1) +1));
        }
    }

    public static void addRunning() {
        isRunning++;
    }

}
