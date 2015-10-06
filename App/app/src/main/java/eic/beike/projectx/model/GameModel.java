package eic.beike.projectx.model;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eic.beike.projectx.busdata.BusCollector;
import eic.beike.projectx.busdata.BusData;
import eic.beike.projectx.busdata.SimpleBusCollector;
import eic.beike.projectx.util.Colour;

/**
 * @author Mikael
 * @author Adam
 */
public class GameModel extends Thread {

    public static final long ONE_SECOND_IN_MILLI = 1000;
    public static final long USER_EVENT_EXPIRATION_TIME = 1 * ONE_SECOND_IN_MILLI;

    private BusCollector busCollector;
    private List<UserEvent> userEvents;
    private List<BusData> matchedData;
    private boolean isRunning;
    private Button[][] buttons;

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
        matchedData = new ArrayList();
        isRunning = true;
        buttons = generateNewButtons();
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
                }
                else{
                    BusData d = findMatch(e);
                    //TODO: fix match to the counted event, it seems the bus api is giving back different timestamps from the counted event.
                    if (isValidMatch(d, e)) {
                        removeUserEvent(e);
                        rememberMatchedData(d);
                        //TODO: Calculate real score
                        triggerNewScore(10);
                    }
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

    private boolean isToOld(BusData d){
        return System.currentTimeMillis() > d.getTimestamp() + USER_EVENT_EXPIRATION_TIME;
    }

    private boolean isValidMatch(BusData d, UserEvent e){
        return d.getSensor() == e.sensor && !isAlreadyMatched(d);
    }

    /**
     * Checks if the busData already have bin matched to an UserEvent,
     * and also frees up memory by removing no longer relevant already matched data.
     * @param busData to be checked if it the counted BusData that has bin matched before
     */
    private boolean isAlreadyMatched(BusData busData){
        List<BusData> copy = new ArrayList(matchedData);
        boolean result = false;
        for(BusData d : copy){
            if(isToOld(d)){
                matchedData.remove(d);
            }
            if(busData.getSensor() == d.getSensor() && busData.getTimestamp() == busData.getTimestamp()){
                result = true;
            }
        }
        return result;
    }

    // To keep the abstraction on the counted level in run(). Might be overdoing the abstraction,
    // but it states the purpose of the list matchedData.
    private void rememberMatchedData(BusData d){
        matchedData.add(d);
    }

    private BusData findMatch(UserEvent e){
        return busCollector.getBusData(e.timeStamp, e.sensor);
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

    /**
     *
     * @return returns the score fomr all buttons that are "three of a kind"
     *         it also sets that they are counted so they can be generated again
     */
    private int columns() {
        int count = 0;
        for(int i = 0; i < buttons.length-1; i++) {
           if(buttons[i][i].colour == buttons[i][i+1].colour
                   && buttons[i][i].colour == buttons[i][i+2].colour) {

               count += buttons[i][i].score + buttons[i][i+1].score +  buttons[i][i+2].score;
               buttons[i][i].counted = true;
               buttons[i][i+1].counted = true;
               buttons[i][i+2].counted = true;
           }
        }
        return count;
    }
    /**
     *
     * @return returns the score fomr all buttons that are "three of a kind"
     *         it also sets that they are counted so they can be generated again
     */
    private int rows() {
        int count = 0;
        for (int i = 0; i < buttons.length - 1; i++) {
            if (buttons[i][i].colour == buttons[i + 1][i].colour
                    && buttons[i][i].colour == buttons[i + 2][i].colour) {
                count += buttons[i][i].score + buttons[i + 1][i].score + buttons[i + 2][i].score;
                buttons[i][i].counted = true;
                buttons[i + 1][i].counted = true;
                buttons[i + 2][i].counted = true;
            }
        }
        return count;
    }

    private void generateButtons() {
        Random random = new Random();
        for (int i = 0; i < buttons.length-1; i++) {
            for (int j = 0; j < buttons.length-1; j++) {
                if (buttons[i][j].counted) {
                    buttons[i][j] = new Button(Colour.colour(random.nextInt(3)), random.nextInt(100));
                }
            }
        }
    }

    private Button[][] generateNewButtons() {
        Button[][] tempList = new Button[3][3];
        Random random = new Random();
        for (int i = 0; i < tempList.length-1; i++) {
            for (int j = 0; j < tempList.length-1; j++) {
                tempList[i][j] = new Button(Colour.colour(random.nextInt(3)), random.nextInt(100));
            }
        }
        return tempList;
    }
}
