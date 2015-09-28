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
     *         there where no entries in the response.
     */
    private List<ResponseEntry> fetchSensorData(BufferedReader reader) {
        //Parse the reader, gson needs the type which is weird below.
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ResponseEntry>>() {
        }.getType();
        List<ResponseEntry> response = gson.fromJson(reader, listType);

        //Sort if there is something to sort.
        if (response!=null) {
            Collections.sort(response);
        }
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
     * @return The stamped data. Check the boolean method busdata.isFull() to see if all sensors where
     *         where set.
     */
    @Override
    public BusData getBusData() {
        BusData data = new BusData();
        ResponseEntry entry;

        //Stamp the data so that caller knows when we collected the sensor data.
        data.setTimestamp(System.currentTimeMillis());

        //Retrieve a stream with the results for the last five seconds on the active bus.
        long t2 = System.currentTimeMillis();
        long t1 = t2 - 5000;
        //TODO: remove hardcoded request.
        BufferedReader reader = retrieveReader(Constants.BASE_URL +
                "?dgw=" + vinNumber + "&" + "t1=" + String.valueOf(t1) + "&t2=" + String.valueOf(t2));

        //If request was successful fetch data and parse the result.
        if(reader != null) {
            List<ResponseEntry> response = fetchSensorData(reader);
            try {
                reader.close();
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), "Could not close reader!");
            }

            //Try to fill in the response.
            if (response != null) {
                Iterator<ResponseEntry> iterator = response.iterator();
                while (!data.isFull() && iterator.hasNext()) {
                    entry = iterator.next();
                    data.populateField(entry);
                }
            }
        }

        //Return the stamped data, this is not necessarily complete.
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
     * @param vinNumber the Vin number of the bus that the app should use
     */
    @Override
    public void chooseBus(String vinNumber) {
        this.vinNumber = vinNumber;
    }
}