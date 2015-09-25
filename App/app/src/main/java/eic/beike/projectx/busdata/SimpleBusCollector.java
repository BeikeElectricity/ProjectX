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
 * Created by alex on 9/21/15.
 */
public class SimpleBusCollector implements BusCollector {

    /**
     * The Vin number of the bus, this is how we identify buses.
     */
    private String vinNumber;

    /**
     *
     * @param reader bufferedreader for gson.
     * @return a list of sensordata sorted by timestamp.
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
     * @param url the formatet rest call
     * @return an inputstream with the server response, this needs to be parsed. NULL if something goes wrong.
     */
    private BufferedReader retrieveReader(String url) {
        BufferedReader in = null;
        try {
            URL requestURL = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) requestURL.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic " + Constants.AUTHORIZATION);

            int responseCode = con.getResponseCode();
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
     * @return The stamped data.
     */
    @Override
    public BusData getBusData() {
        BusData data = new BusData();
        ResponseEntry entry;

        /**
         * Sensors we consider in the app.
         */
        List<Resource> currentlyParsedResources = new ArrayList<Resource>();
        currentlyParsedResources.add(Resource.Accelerator_Pedal_Position);
        currentlyParsedResources.add(Resource.Ambient_Temperature);
        currentlyParsedResources.add(Resource.At_Stop);
        currentlyParsedResources.add(Resource.Next_Stop);
        currentlyParsedResources.add(Resource.Stop_Pressed);

        //Stamp the data so that caller knows when we collected the sensor data.
        data.timestamp = (int) System.currentTimeMillis();

        //Retrieve a stream with the results for the last five seconds on the active bus.
        long t1 = System.currentTimeMillis();
        long t2 = t1 - 5000;
        //TODO: remove hardcoded request.
        BufferedReader reader = retrieveReader(Constants.BASE_URL +
                "?dgw=" + vinNumber + "&" + "t1=" + String.valueOf(t1) + "&t2=" + String.valueOf(t2));

        //Fetch data and parse the result.
        List<ResponseEntry> response = fetchSensorData(reader);
        try {
            reader.close();
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(),"Could not close reader!");
        }

        //Try to fill in the response
        if(response!=null) {
            Iterator<ResponseEntry> iterator = response.iterator();
            while (!currentlyParsedResources.isEmpty() && iterator.hasNext()) {
                entry = iterator.next();
                Resource r = Resource.valueOf(entry.resource);
                switch (r) {
                    case Accelerator_Pedal_Position:
                        data.pedalPosition = Integer.valueOf(entry.value);
                        break;
                    case Ambient_Temperature:
                        data.temperatureOutside = Integer.valueOf(entry.value);
                        break;
                    case At_Stop:
                        data.atStop = Boolean.valueOf(entry.value);
                        break;
                    case Stop_Pressed:
                        data.stopPressed = Boolean.valueOf(entry.value);
                        break;
                    case Next_Stop:
                        data.nextStop = entry.value;
                        break;
                    default:
                        Log.d(getClass().getSimpleName(), "Value of sensor " + r.name() + " not currently used");
                }
                //Remove whatever sensor we just considered.
                currentlyParsedResources.remove(r);
            }
        }
        //TODO: See that currentlyParsedResources is empty and handle the not empty case.
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

    @Override
    public void chooseBus(String vinNumber) {
        this.vinNumber = vinNumber;
    }
}
