package eic.beike.projectx.network.busdata;

import android.location.Location;

/**
 * Interface for communicating with the Electricity API.
 *
 * Created by alex on 9/22/15.
 */
public interface BusCollector {

    public static final String TEST_BUSS_VIN_NUMBER = "Ericsson$Vin_Num_001";

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
    boolean determineBus(Location location);

    /**
     * Manually pick a bus as active.
     *
     * @param vinNumber the Vin number of the bus that the app should use
     */
    void chooseBus(String vinNumber);

}
