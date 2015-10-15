package eic.beike.projectx.model;

import eic.beike.projectx.network.busdata.BusCollector;
import eic.beike.projectx.network.busdata.BusData;
import eic.beike.projectx.network.busdata.Sensor;
import eic.beike.projectx.network.busdata.SimpleBusCollector;
import eic.beike.projectx.util.Constants;

/**
 * Thread that keeps of the rounds, for now it ends the round every time the bus goes from
 * not being at a stop to being at a stop.
 *
 * Created by alex on 10/13/15.
 */
public class RoundTracker extends Thread {
    BusCollector bus = SimpleBusCollector.getInstance();
    GameModel game;
    Boolean isAtStop;

    public void track(GameModel game){
        this.game = game;
        start();
    }

    @Override
    public void run(){
        try {
            //Get initial status, i.e see if the bus already left the station.
            isAtStop = bus.getBusData(System.currentTimeMillis(),Sensor.At_Stop).isAtStop();

            //Check to see if the bus comes to a stop or leaves a stop.
            while (true) {
                BusData atStop = bus.getBusData(System.currentTimeMillis(), Sensor.At_Stop);
                if (atStop.isAtStop() != isAtStop) {
                    isAtStop = atStop.isAtStop();
                    if(isAtStop) {
                        game.endRound();
                    }
                }
                currentThread().sleep(Constants.ONE_SECOND_IN_MILLI);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            game.triggerError("Lost internet connection");
        } catch (Exception e) {
            e.printStackTrace();
            game.triggerError("Lost internet connection");
        }

    }
}
