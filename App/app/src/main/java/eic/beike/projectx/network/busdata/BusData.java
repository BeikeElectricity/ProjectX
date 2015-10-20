package eic.beike.projectx.network.busdata;


import android.util.Log;
import lombok.Getter;

/**
 * Data carrying class. If the value is needed use getSensor and then the get the appropriate field.
 * Created by alex on 9/22/15.
 */
public class BusData {

    /**
     * What sensor the data came from.
     */
    @Getter
    private Sensor sensor = Sensor.UNKNOWN;

    /**
     * The time that the sensor data was recorded on the bus. Zero if no data was found.
     */
    @Getter
    public Long timestamp = 0l;


    /**
     * The pedal position in percent (0 to 100) TODO: Confirm 100 is full throttle.
     * Updated once per second in bus
     */
    @Getter
    private int pedalPosition;

    /**
     * The temperature outside of the bus.
     * Updated once per second in bus
     */
    @Getter
    private int temperatureOutside;

    /**
     * Whether the bus is currently at a bus station
     */
    @Getter
    private boolean atStop;


    /**
     * Whether the stop button has been pressed.
     */
    @Getter
    private boolean stopPressed;

    /**
     * The name of the next stop.
     */
    @Getter
    private String nextStop;

    /**
     * The speed of the bus in km/h
     */
    @Getter
    private double speed;

    /**
     * Awkwardly switch the response for further parsing to our own model.
     *
     * @param entry One parsed line from the json, this corresponds to one value of on sensor
     *              in one bus at one time.
     */
    protected void populate(ResponseEntry entry) {
        try {
            Resource r = Resource.valueOf(entry.resourceSpec);
            switch (r) {
                case Accelerator_Pedal_Position_Value:
                    pedalPosition = Integer.valueOf(entry.value);
                    break;
                case Ambient_Temperature_Value:
                    temperatureOutside = Integer.valueOf(entry.value);
                    break;
                case At_Stop_Value:
                    atStop = Boolean.valueOf(entry.value);
                    break;
                case Stop_Pressed_Value:
                    stopPressed = Boolean.valueOf(entry.value);
                    break;
                case Bus_Stop_Name_Value:
                    nextStop = entry.value;
                    break;
                case Speed2_Value:
                    speed = Double.valueOf(entry.value);
                    break;
                default:
                    Log.d(getClass().getSimpleName(), "Value of sensor " + r.name() + " not currently used.");
            }
            timestamp = entry.timestamp;
            sensor = r.sensor;
        }
        catch (IllegalArgumentException e){
            Log.d(getClass().getSimpleName(), "Value of sensor " + entry.resourceSpec + " not currently recognized.");
        }
    }
}
