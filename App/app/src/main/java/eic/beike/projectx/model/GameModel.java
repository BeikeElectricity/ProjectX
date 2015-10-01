package eic.beike.projectx.model;

import java.util.ArrayList;
import java.util.List;

import eic.beike.projectx.activities.GameActivity;
import eic.beike.projectx.busdata.BusCollector;
import eic.beike.projectx.busdata.BusData;
import eic.beike.projectx.busdata.SimpleBusCollector;

/**
 * Created by Mikael on 2015-09-30.
 */
public class GameModel extends Thread {

    public static final long ONE_SECOND_MILLI = 1000;

    private BusCollector busCollector;
    private List<UserEvent> userEvents;
    private boolean isRunning;

    private int score = 0;


    public GameModel(){
        super();
        busCollector = new SimpleBusCollector();
        busCollector.chooseBus(BusCollector.TEST_BUSS_VIN_NUMBER);
        userEvents = new ArrayList();
        isRunning = true;
    }

    public int getScore() { return score; }
    public synchronized void addScore(int points) { score += points; }

    public void stopLoop(){
        isRunning = false;
    }

    public void onClick(UserEvent e){
        synchronized (userEvents){
            userEvents.add(e);
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            UserEvent e = null;
            synchronized (userEvents) {
                if (!userEvents.isEmpty()) {
                    e = userEvents.remove(0);
                }
            }

            if (e != null) {
                BusData d = busCollector.getBusData(e.timeStamp, e.sensor);
                System.out.println(d.getSensor().toString());
            }

            try {
                Thread.sleep(ONE_SECOND_MILLI);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
