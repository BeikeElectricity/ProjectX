package eic.beike.projectx.model;

import eic.beike.projectx.busdata.Sensor;

/**
 * Created by Mikael on 2015-09-30.
 */
public class UserEvent {
    public final long timeStamp;
    public final Sensor sensor;

    public UserEvent(long timeStamp, Sensor sensor){
        this.timeStamp = timeStamp;
        this.sensor = sensor;
    }
}
