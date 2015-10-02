package eic.beike.projectx.network.projectXServer;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eic.beike.projectx.network.RetrieveReader;
import eic.beike.projectx.util.Constants;
import eic.beike.projectx.util.ScoreEntry;

import java.io.BufferedReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to interact with our database.
 * @Author alex
 */
public class Database implements IDatabase {

    /**
     * Register a name for the given id.
     *
     * @return True if registration succeded, false if it failed.
     */
    @Override
    public boolean register(String id, String name) {
        //Make request.
        BufferedReader in = RetrieveReader.get(
                Constants.SERVER_URL+"Register.php?id="+ id + "&name=" + name
        );
        //If server connection succeeded see if the registration did as well.
        if (in != null) {
            Gson gson = new Gson();
            InsertResponse response = gson.fromJson(in, InsertResponse.class);
            Log.d(this.getClass().getSimpleName(),"Got answer from server, message:" + response.getMessage());
            return (response.getSuccess() == 1);
        }
        Log.w(this.getClass().getSimpleName(),"Failed to contact the database.");
        return false;
    }

    /**
     * Record a score in the database, yet unclear when a score should be recorded.
     *
     * @param id The id, not the name, of the player
     * @param bus the vin-number of the bus.
     * @return True if score was recorded correctly, false if it failed.
     */
    @Override
    public boolean recordScore(String id, int score, long time, String bus) {
        //We need a player id and a vin number.
        if(id == null || bus == null){
            return false;
        }
        //Make request.
        BufferedReader in = RetrieveReader.get(
                Constants.SERVER_URL+"AddScore.php?" +
                        "id="+ id +
                        "&score=" + score +
                        "&time=" + Long.toString(time) +
                        "&bus=" + bus
        );
        //If connection established see if the score was recorded.
        if (in != null) {
            Gson gson = new Gson();
            InsertResponse response = gson.fromJson(in, InsertResponse.class);
            Log.d(this.getClass().getSimpleName(),"Got answer from server, message:" + response.getMessage());
            return (response.getSuccess() == 1);
        }
        Log.w(this.getClass().getSimpleName(),"Failed to contact the database.");
        return false;
    }

    /**
     * Get the ten best scores in the database.
     *
     * @return The best ten scores for all players on all buses.
     */
    @Override
    public List<ScoreEntry> getTopTen() {
        //Make request.
        BufferedReader in = RetrieveReader.get(
                Constants.SERVER_URL+"GetHighscore.php"
        );

        //Parse the result
        return parseResultsFromReader(in);
    }

    /**
     * Get the ten best scores for a specific player.
     *
     * @param playerId the id of the player that we want the high score for.
     * @return An list of ScoreEntry:s of the players best scores.
     */
    @Override
    public List<ScoreEntry> getPlayerTopTen(String playerId) {
        //Make request.
        BufferedReader in = RetrieveReader.get(
                Constants.SERVER_URL+"GetHighscore.php?id=" + playerId
        );

        //Parse the result
        return parseResultsFromReader(in);
    }

    /**
     *  Helper method that converts the json to an array of ScoreEntry:s
     *
     *  @param in reader hooked to a response from the GetHighscore.php script.
     *  @return
     */
    private List<ScoreEntry> parseResultsFromReader(Reader in){

        ArrayList<ScoreEntry> results = new ArrayList<ScoreEntry>();

        //If server connection succeeded try to get an array of ScoreEntry:s
        if (in != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<ScoreEntry>>() {}.getType();
            results = gson.fromJson(in, listType);
            Log.d(this.getClass().getSimpleName(),"Got answer from server.");
            return results;
        }
        Log.w(this.getClass().getSimpleName(),"Failed to contact the database.");
        return results;
    }
}
