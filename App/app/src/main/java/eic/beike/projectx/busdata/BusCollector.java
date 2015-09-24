package eic.beike.projectx.busdata;

/**
 * Interface for communicating with the Electricity API.
 *
 * Created by alex on 9/22/15.
 */
public interface BusCollector {
    /**
     * Get the latest timestamped data for the active bus.
     *
     * @return the sensor data wrapped in a BusData java object.
     */
    BusData getBusData();

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
