package eic.beike.projectx.busdata;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eic.beike.projectx.util.Constants;
import org.apache.http.HttpStatus;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
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
        List<ResponseEntry> response = (ArrayList<ResponseEntry>) gson.fromJson(reader, listType);
        return response;
    }

    /**
     * Make a http request and return a reader for the response.
     *
     * @param url the formatted rest call
     * @return an input stream with the server response, this needs to be parsed. NULL if something goes wrong.
     */
    private BufferedReader retrieveReader(String url) {
        BufferedReader in = null;
        try {
            URL requestURL = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) requestURL.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic " + Constants.AUTHORIZATION);
            int responseCode = con.getResponseCode();

            //Check whether the request was successful.
            if (responseCode != HttpStatus.SC_OK) {
                Log.w(getClass().getSimpleName(),
                        "Error " + responseCode + " for URL " + url);
                return null;
            }
            in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
        } catch (IOException e) {
            Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }
        return in;
    }

    /**
     * @param sensor the sensor that we want to check.
     * @param time the time in epoch seconds that the data should be centered at.
     * @return The closest resource data that came from that given sensor.
     */
    @Override
    public BusData getBusData(long time, Sensor sensor) {

        BusData data = new BusData();

        //Retrieve a stream with the results for 10 seconds before to 10 seconds after the interesting time.
        long t2 = time + 10000;
        long t1 = time - 10000;
        //TODO: remove hardcoded request.
        BufferedReader reader = retrieveReader(Constants.BASE_URL +
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