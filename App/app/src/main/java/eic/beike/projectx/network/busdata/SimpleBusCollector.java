package eic.beike.projectx.network.busdata;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eic.beike.projectx.network.RetrieveReader;
import eic.beike.projectx.util.Constants;
import org.apache.http.HttpStatus;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

/**
 * A on phone middle layer for the Cybercom api. Perhaps this code would be better placed on a server
 * somewhere.
 *
 * Created by alex on 9/21/15.
 */
public class SimpleBusCollector implements BusCollector {

    private static final SimpleBusCollector instance = new SimpleBusCollector();

    private static final String ALL_BUSES = "ALL_BUSES";
    private static final double MAX_DISTANCE_ALLOWED = 10000000; //TODO: some value here

    /**
     * The Vin number of the bus, this is how we identify buses.
     */
    private String vinNumber;

    /**
     * @param sensor the sensor that we want to check.
     * @param time the time in epoch seconds that the data should be centered at.
     * @return The closest resource data that came from that given sensor.
     */

    /**
     * Private constructor to enforce singleton.
     */
    private SimpleBusCollector(){
    }


    /**
     * To get access to this API (the singleton).
     */
    public static BusCollector getInstance(){
        return instance;
    }


    @Override
    public BusData getBusData(long time, Sensor sensor) {

        BusData data = new BusData();

        //Retrieve a stream with the results for 10 seconds before to 10 seconds after the interesting time.
        long t2 = time + 10 * Constants.ONE_SECOND_IN_MILLI;
        long t1 = time - 10 * Constants.ONE_SECOND_IN_MILLI;

        String url = constructUrl(vinNumber, sensor, t1, t2);
        List<ResponseEntry> response = getResponse(url);

        // Try to find best match on timestamp. And add all corresponding resources
        // to the data.

        ArrayList<ResponseEntry> chosenEntrys = new ArrayList();
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

        //Return the stamped data, this is can be empty.
        return data;
    }


    private String constructUrl(String busDgw, Sensor sensor, long t1, long t2){
        String dgw        = busDgw == ALL_BUSES ? "?" : "?dgw=" + busDgw + "&";
        String sensorSpec = "sensorSpec=Ericsson$" + sensor.toString();
        String timeSpan   = "&t1=" + String.valueOf(t1) + "&t2=" + String.valueOf(t2);

        return Constants.BASE_URL + dgw + sensorSpec + timeSpan;
    }


    private List<ResponseEntry> getResponse(String url){
        List<ResponseEntry> response = null;
        try {
            BufferedReader reader = retrieveReader(url);
            response = fetchSensorData(reader);
            reader.close();
        } catch (IOException e) {
            Log.w(getClass().getSimpleName(), e);
        }

        if(response == null){ //TODO: Give exception to the user instead of swallowing it as empty list?
            return new ArrayList();
        }
        else{
            return response;
        }
    }


    /**
     * Make a http request and return a reader for the response.
     *
     * @param url the formatted rest call
     * @return an input stream with the server response, this needs to be parsed. NULL if something goes wrong.
     */
    private BufferedReader retrieveReader(String url)
            throws IOException
    {
        BufferedReader in = null;
        try {
            URL requestURL = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) requestURL.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic " + Constants.AUTHORIZATION);
            int responseCode = con.getResponseCode();

            //Check whether the request was successful.
            if (responseCode != HttpStatus.SC_OK) {
                Log.w(getClass().getSimpleName(), "Error " + responseCode + " for URL " + url);
                return null;
            }
            in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
        } catch (IOException e) {
            throw new IOException("Error for URL" + url, e);
        }
        return in;
    }


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
     * Determine which bus the player is on.
     */
    @Override
    public boolean determineBus(Location userLocation) {
        long t1 = System.currentTimeMillis() - 3 * Constants.ONE_SECOND_IN_MILLI;
        long t2 = System.currentTimeMillis() + 3 * Constants.ONE_SECOND_IN_MILLI;

        String url = constructUrl(ALL_BUSES, Sensor.GPS_NMEA, t1, t2);
        List<ResponseEntry> response = getResponse(url);
        response = filterLatestData(response);

        ResponseEntry entry = getBestLocationMatch(response, userLocation);
        if(busIsCloseEnough(entry, userLocation)){
            vinNumber = entry.gatewayId;
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * Removes all ResponseEntries with a lower timestamp that have the same id.
     * @param response list of ResponseEntries to filter
     * @return a list only containing ResponseEntries with different ids.
     */
    private List<ResponseEntry> filterLatestData(List<ResponseEntry> response){
        HashMap<String, ResponseEntry> latestData = new HashMap();

        for(ResponseEntry r : response){
            ResponseEntry latest = latestData.get(r.gatewayId);
            if(latest == null || latest.timestamp < r.timestamp){
                latestData.put(r.gatewayId, r);
            }
        }
        return new ArrayList<ResponseEntry>(latestData.values());
    }


    private ResponseEntry getBestLocationMatch(List<ResponseEntry> response, Location location){
        double shortestDistance = Double.MAX_VALUE;
        ResponseEntry bestMatch = null;

        for(ResponseEntry e : response){
            double distanceAway = getLocationDifference(e, location);

            if(bestMatch == null || distanceAway < shortestDistance){
                shortestDistance = distanceAway;
                bestMatch = e;
            }
        }
        return bestMatch;
    }


    private boolean busIsCloseEnough(ResponseEntry entry, Location location){
        return getLocationDifference(entry, location) <= MAX_DISTANCE_ALLOWED;
    }


    private double getLocationDifference(ResponseEntry entry, Location location){
        try {
            double northCoord = parseNorthCoordinate(entry.value);
            double eastCoord = parseEastCoordinate(entry.value);

            // The locations coordinates are multiplied by 100 to match the format from the bus.
            return Math.sqrt(Math.pow(100 * location.getLatitude() - northCoord, 2) + Math.pow(100 * location.getLongitude() - eastCoord, 2));
        }
        catch(NumberFormatException ex){
            ex.printStackTrace();
            return Double.MAX_VALUE;
        }
    }


    private double parseNorthCoordinate(String s){
        int stopIndex = s.indexOf(",N");
        int startIndex = s.indexOf("A,") + 2;
        return Double.parseDouble(s.substring(startIndex, stopIndex));
    }


    private double parseEastCoordinate(String s){
        int stopIndex = s.indexOf(",E");
        int startIndex = s.indexOf("N,") + 2;
        return Double.parseDouble(s.substring(startIndex, stopIndex));
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