package eic.beike.projectx.network;

import android.util.Log;
import eic.beike.projectx.util.Constants;
import org.apache.http.HttpStatus;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Used to retrieve a buffered readers from URLs.
 * Created by alex on 10/1/15.
 */
public class RetrieveReader {

    /**
     * Make a http request and return a reader for the response.
     *
     * @param url the formatted rest call
     * @return an input stream with the server response, this needs to be parsed. NULL if something goes wrong.
     */
    public static BufferedReader get(String url) {
        BufferedReader in = null;

        try {
            URL requestURL = new URL(url);

            //Handle https and http connections differently.
            if (url.startsWith("https")) {
                HttpsURLConnection con = (HttpsURLConnection) requestURL.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", "Basic " + Constants.AUTHORIZATION);
                int responseCode = con.getResponseCode();

                //Check whether the request was successful.
                if (responseCode != HttpStatus.SC_OK) {
                    Log.w("RetrieveReader", "Error " + responseCode + " for URL " + url);
                    return null;
                }
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                URLConnection yc = requestURL.openConnection();
                in = new BufferedReader(
                        new InputStreamReader(
                                yc.getInputStream()));
            }
        } catch (IOException e) {
            Log.w("RetrieveReader", "Error for URL " + url, e);
        }
        return in;
    }
}