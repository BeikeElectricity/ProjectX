package eic.beike.projectx.busdata;

/**
 * Small data class used by gson, comparation on timestamp
 * Created by alex on 9/22/15.
 */
public class ResponseEntry implements Comparable<ResponseEntry>{
    /**
     * The name of the resource in the api.
     */
    String resourceSpec;

    /**
     * The timestamp of when the event was recorded on the bus.
     */
    long timestamp;

    /**
     * The value of the sensor.
     */
    String value;

    /**
     * The vin number of the bus on which the sensor recorded data.
     */
    String gatewayId;

    @Override
    public int compareTo(ResponseEntry other){
        return (int) (this.timestamp - other.timestamp);
    }
}
