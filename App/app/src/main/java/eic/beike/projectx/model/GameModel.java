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

    public static final long ONE_SECOND_IN_MILLI = 1000;
    public static final long USER_EVENT_EXPIRATION_TIME = 10 * ONE_SECOND_IN_MILLI;

    private BusCollector busCollector;
    private List<UserEvent> userEvents;
    private boolean isRunning;

    public GameModel(){
        super();
        busCollector = new SimpleBusCollector();
        busCollector.chooseBus(BusCollector.TEST_BUSS_VIN_NUMBER);
        userEvents = new ArrayList();
        isRunning = true;
    }

    /**
     * Lets the thread die a natural way.(not killing it in the middle of a loop)
     */
    public void stopLoop(){
        isRunning = false;
    }

    /**
     * Called by the buttons from GameActivity
     * @param e Sent from the GameActivity when a button is clicked, to be processed in the thread.
     */
    public void onClick(UserEvent e){
        synchronized (userEvents){
            userEvents.add(e);
        }
    }

    /**
     * The GameModels main loop which takes user events and gives score based upon them.
     */
    @Override
    public void run() {
        while (isRunning) {
            List<UserEvent> events = getUserEvents();
            for(UserEvent e : events) {
                if (isToOld(e)) {
                    removeUserEvent(e);
                    //TODO: give negative score
                    System.out.println("Score: -10");
                } else if (findMatch(e)) {
                    removeUserEvent(e);
                    //TODO: give positive score
                    System.out.println("Score: +10");
                }
            }
            sleepThread(ONE_SECOND_IN_MILLI);
        }
    }

    private List<UserEvent> getUserEvents(){
        synchronized (userEvents) {
            return new ArrayList(userEvents);
        }
    }

    private void removeUserEvent(UserEvent e){
        synchronized (userEvents) {
            userEvents.remove(e);
        }
    }

    private boolean isToOld(UserEvent e){
        return System.currentTimeMillis() > e.timeStamp + USER_EVENT_EXPIRATION_TIME;
    }

    private boolean findMatch(UserEvent e){
        BusData d = busCollector.getBusData(e.timeStamp, e.sensor);
        return d.getSensor() == e.sensor;
    }

    private void sleepThread(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
