package eic.beike.projectx.model;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import eic.beike.projectx.busdata.BusCollector;
import eic.beike.projectx.busdata.BusData;
import eic.beike.projectx.busdata.SimpleBusCollector;

/**
 * @author Mikael
 * @author Adam
 */
public class GameModel extends Thread {

    public static final long ONE_SECOND_IN_MILLI = 1000;
    public static final long USER_EVENT_EXPIRATION_TIME = 1 * ONE_SECOND_IN_MILLI;

    private BusCollector busCollector;
    private List<UserEvent> userEvents;
    private boolean isRunning;

    /**
     * Persistent total score
     */
    private int score = 0;


    private Handler handler;


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
                    //TODO: Calculate real score
                    triggerNewScore(-10);
                } else if (findMatch(e)) {
                    removeUserEvent(e);
                    //TODO: Calculate read score
                    triggerNewScore(10);
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

    /**
     * Add a handler that handles
     * @param h The handler to be added
     */
    public synchronized void setHandler(Handler h) {
        handler = h;
    }

    /**
     * Used to notify the handlers about a new score
     * @param latestScore The latest score the player received
     */
    private synchronized void triggerNewScore(int latestScore) {
        if (handler == null) {
            return;
        }
        Log.d("Score", Thread.currentThread().getName() + ":triggerNewScore");
        this.score += latestScore;
        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();

        data.putInt("score", this.score);
        data.putInt("latest_score", latestScore);

        msg.setData(data);
        msg.sendToTarget();
    }
}
