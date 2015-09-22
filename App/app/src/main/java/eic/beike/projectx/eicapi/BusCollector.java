package eic.beike.projectx.eicapi;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eic.beike.projectx.util.Constants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by alex on 9/21/15.
 */
public class BusCollector implements EICCommunicator{

    /**
     * The Vin number of the bus, this is how we identify buses.
     */
    private String vinNumber;

    /**
     * @return a list of sensordata sorted by timestamp.
     */
    private List<ResponseEntry> fetchSensorData() {
        //Retrieve a stream with the results for the last five seconds on the active bus.
        long t1 = System.currentTimeMillis();
        long t2 = t1 - 5000;
        InputStream source = retrieveStream(Constants.BASE_URL +
                "dgw=" + vinNumber + "&" + "t1=" + String.valueOf(t1) + "&t2=" + String.valueOf(t2));
        Reader reader = new InputStreamReader(source);

        //Parse the response, gson needs the type which is weird below.
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ResponseEntry>>() {}.getType();
        List<ResponseEntry> response = gson.fromJson(reader, listType);

        Collections.sort(response);
        return response;
    }

    private InputStream retrieveStream(String url) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(url);
        getRequest.setHeader("Authorization", "Basic " + Constants.AUTHORIZATION);
        try {
            HttpResponse getResponse = client.execute(getRequest);
            final int statusCode = getResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w(getClass().getSimpleName(),
                        "Error " + statusCode + " for URL " + url);
                return null;
            }
            HttpEntity getResponseEntity = getResponse.getEntity();
            return getResponseEntity.getContent();
        } catch (IOException e) {
            getRequest.abort();
            Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }
        return null;
    }

    /**
     *
     * @return
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

        //Fetch data and parse the result.
        List<ResponseEntry> response = fetchSensorData();
        Iterator<ResponseEntry> iterator = response.iterator();
        while (!currentlyParsedResources.isEmpty()){
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
