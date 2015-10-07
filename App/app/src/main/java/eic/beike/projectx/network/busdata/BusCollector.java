package eic.beike.projectx.network.busdata;

/**
 * Interface for communicating with the Electricity API.
 *
 * Created by alex on 9/22/15.
 */
public interface BusCollector {
    /**
     * Get the closest data from the given sensor.
     *
     * @param sensor the sensor that we want to check.
     * @param time time time in epoch seconds that the data should be centered at.
     * @return The closest resource data that came from that given sensor.
     */
    BusData getBusData(long time, Sensor sensor) throws Exception;

    /**
     * Determine which bus the user is currently interested in. This is the active bus.
     */
    void determineBus();

    /**
     * Manually pick a bus as active.
     *
     * @param vinNumber the Vin number of the bus that the app should use
     */
    void chooseBus(String vinNumber);

}
