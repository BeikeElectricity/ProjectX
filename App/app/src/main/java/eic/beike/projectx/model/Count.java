package eic.beike.projectx.model;

import android.util.Log;

/**
 *@author Simon
 */
public class Count implements ScoreCountApi {

    /**
     * The gameModel uses this counter.
     */

    private static final Long epochYear = 1444800000000l;

    private GameModel gameModel;

    public Count(GameModel game) {
        this.gameModel = game;
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
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("Count", "calculcatePrecent");
                    Long t2 = System.currentTimeMillis() + 79999996;
                    calculatePercent(t1, t2);
                } catch (Exception e) {
                    Log.e("Count", e.getMessage());
                }
            }
        }.count(t1);
    }

    public void sum(Button[][] buttons) {
        gameModel.addBonus(columns(buttons) + rows(buttons));
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

    public synchronized void calculatePercent(long t1, long t2) {
        if (t2 == 0) {

            gameModel.addScore(1.00000);
        } else if (t1 > t2) {
            t1 -= epochYear;
            t2 -= epochYear;
            gameModel.addScore(Math.abs( ((double) t1 / (double) t2)));
        } else {
            t1 -= epochYear;
            t2 -= epochYear;
            gameModel.addScore(Math.abs( ( (double) t2 / (double) t1)));
        }
    }



}
