package eic.beike.projectx.network;

import android.util.Log;
import eic.beike.projectx.util.Constants;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Used to retrieve a buffered readers from URLs.
 *
 * @author alex
 */
public class RetrieveReader {

    /**
     * Make a http request and return a reader for the response.
     *
     * @param url the formatted rest call
     * @return an input stream with the server response, this needs to be parsed. NULL if something goes wrong.
     */
    public static BufferedReader get(String url)
            throws Exception
    {

        try {
            BufferedReader in;
            URL requestURL = new URL(url);

            //Handle https and http connections differently.
            if (url.startsWith("https")) {
                HttpsURLConnection con = (HttpsURLConnection) requestURL.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", "Basic " + Constants.AUTHORIZATION);
                int responseCode = con.getResponseCode();

                //Check whether the request was successful.
                if (responseCode != Constants.REQUEST_OK) {
                    Log.w("RetrieveReader", "Error " + responseCode + " for URL " + url);
                    throw new Exception(String.format("Response %d from url (%s).",responseCode,url));
                }
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                URLConnection yc = requestURL.openConnection();
                in = new BufferedReader(
                        new InputStreamReader(
                                yc.getInputStream()));
            }
            return in;
        } catch (IOException e) {
            Log.w("RetrieveReader", "Error for URL " + url, e);
            throw new Exception(String.format("Unable to reach host (%s).",url));
        }
    }
}