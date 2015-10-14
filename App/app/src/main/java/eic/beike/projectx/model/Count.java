package eic.beike.projectx.model;


import eic.beike.projectx.network.busdata.BusCollector;
import eic.beike.projectx.network.busdata.BusData;
import eic.beike.projectx.network.busdata.Sensor;
import eic.beike.projectx.network.busdata.SimpleBusCollector;

/**
 *@author Simon
 */
public class Count implements ScoreCountApi {

    /**
     * The game that uses this counter.
     */
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
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    BusCollector bus = SimpleBusCollector.getInstance();
                    BusData data = bus.getBusData(t1, Sensor.Stop_Pressed);
                    long t2 = data.timestamp;

                    if (t2 == 0) {
                        game.addScore(0);
                    } else if (t1 > t2) {
                        t1 -= Long.getLong("1444800000000");
                        t2 -= Long.getLong("1444800000000");
                        game.addScore(Math.abs((int) (t1 - t2) * game.getBonus()));
                    } else {
                        t1 -= Long.getLong("1444800000000");
                        t2 -= Long.getLong("1444800000000");
                        game.addScore(Math.abs((int) (t2 - t1) * game.getBonus()));
                    }
                } catch (Exception e) {
                    game.triggerError(e.getMessage());
                }
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
}
