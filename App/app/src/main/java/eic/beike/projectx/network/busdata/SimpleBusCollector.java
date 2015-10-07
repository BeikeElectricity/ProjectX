package eic.beike.projectx.network.busdata;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eic.beike.projectx.network.RetrieveReader;
import eic.beike.projectx.util.Constants;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * A on phone middle layer for the Cybercom api. Perhaps this code would be better placed on a server
 * somewhere.
 *
 * Created by alex on 9/21/15.
 */
public class SimpleBusCollector implements BusCollector {

    /**
     * The Vin number of the bus, this is how we identify buses.
     */
    private String vinNumber;

    /**
     * @param reader buffered reader for gson.
     * @return a list of sensor data sorted by timestamp. This is NULL if
     * there where no entries in the response.
     */
    private List<ResponseEntry> fetchSensorData(BufferedReader reader) {
        //Parse the reader, gson needs the type which is weird below.
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<ResponseEntry>>() {}.getType();
        return (ArrayList<ResponseEntry>) gson.fromJson(reader, listType);

    }



    /**
     * @param sensor the sensor that we want to check.
     * @param time the time in epoch seconds that the data should be centered at.
     * @return The closest resource data that came from that given sensor.
     */
    @Override
    public BusData getBusData(long time, Sensor sensor)
            throws Exception
    {

        BusData data = new BusData();

        //Retrieve a stream with the results for 10 seconds before to 10 seconds after the interesting time.
        //TODO: remove hardcoded request and interval.
        long t2 = time + 10000;
        long t1 = time - 10000;
        BufferedReader reader = RetrieveReader.get(Constants.BASE_URL +
                "?dgw=" + vinNumber +
                "&sensorSpec=Ericsson$" + sensor.toString() +
                "&t1=" + String.valueOf(t1) + "&t2=" + String.valueOf(t2));

        //If request was successful fetch data and find best match.
        if (reader != null) {
            List<ResponseEntry> response = fetchSensorData(reader);
            try {
                reader.close();
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "Could not close reader!");
            }

            // Try to find best match on timestamp. And add all corresponding resources
            // to the data.
            if (response != null) {

                ArrayList<ResponseEntry> chosenEntrys = new ArrayList<ResponseEntry>();
                long bestDiff = Long.MAX_VALUE;
                long currentDiff;

                // Go through the resources and see how well they match the desired time.
                for (ResponseEntry e : response) {
                    currentDiff = e.timestamp - time;
                    if (Math.abs(currentDiff) < Math.abs(bestDiff)) {
                        chosenEntrys.clear();
                        chosenEntrys.add(e);
                        bestDiff = currentDiff;
                    } else if( bestDiff - currentDiff == 0 ){
                        chosenEntrys.add(e);
                    }
                }

                //Populate all resource fields of the found sensor.
                for(ResponseEntry chosenEntry : chosenEntrys) {
                    data.populate(chosenEntry);
                }
            }
        }
        //Return the stamped data, this is can be empty.
        return data;
    }

    /**
     * Determine which bus the player is on.
     */
    @Override
    public void determineBus() {
        Log.w(getClass().getSimpleName(), "Someone tried to call unimplemented method!");
        throw new UnsupportedOperationException("Bus determination not implemented jet!");
    }

    /**
     * Choose which bus to retrieve data from.
     *
     * @param vinNumber the Vin number of the bus that the app should use
     */
    @Override
    public void chooseBus(String vinNumber) {
        this.vinNumber = vinNumber;
    }
}