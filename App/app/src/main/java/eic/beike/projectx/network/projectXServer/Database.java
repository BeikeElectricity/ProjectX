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
 * @author alex
 */
public class Database implements IDatabase {

    /**
     * Register a name for the given id.
     *
     * @return True if registration succeeded, false if it failed.
     */
    @Override
    public boolean register(String id, String name) throws Exception {
        //Make request.
        BufferedReader in = RetrieveReader.get(
                Constants.SERVER_URL+"Register.php?id="+ id + "&name=" + name
        );

        return parseInsertFromReader(in);

    }

    /**
     * Record a score in the database, yet unclear when a score should be recorded.
     *
     * @param id The id, not the name, of the player
     * @param bus the vin-number of the bus.
     * @return True if score was recorded correctly, false if it failed.
     */
    @Override
    public boolean recordScore(String id, int score, long time, String bus) throws Exception {
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
        return parseInsertFromReader(in);
    }

    /**
     * Helper method that converts json to InsertResponse and then to boolean.
     */
    private boolean parseInsertFromReader(Reader in) {
        //If server connection succeeded see if the registration did as well.
        if (in != null) {
            Gson gson = new Gson();
            InsertResponse response = gson.fromJson(in, InsertResponse.class);
            //TODO: There's a strange bug where a 0 causes the php to fail. This will give null here.
            if(response != null ) {
                Log.d(this.getClass().getSimpleName(), "Got answer from server, message:" + response.getMessage());
                return (response.getSuccess() == 1);
            }
        }
        Log.w(this.getClass().getSimpleName(), "Failed to contact the database.");
        return false;
    }

    /**
     * Get the ten best scores in the database.
     *
     * @return The best ten scores for all players on all buses.
     */
    @Override
    public List<ScoreEntry> getTopTen() throws Exception {
        //Make request.
        BufferedReader in = RetrieveReader.get(
                Constants.SERVER_URL+"GetHighscore.php"
        );

        //Parse the result
        return parseHighScoreFromReader(in);
    }

    /**
     * Get the ten best scores for a specific player.
     *
     * @param playerId the id of the player that we want the high score for.
     * @return An list of ScoreEntry:s of the players best scores.
     */
    @Override
    public List<ScoreEntry> getPlayerTopTen(String playerId) throws Exception {
        //Make request.
        BufferedReader in = RetrieveReader.get(
                Constants.SERVER_URL+"GetHighscore.php?id=" + playerId
        );

        //Parse the result
        return parseHighScoreFromReader(in);
    }

    /**
     *  Helper method that converts the json to an array of ScoreEntry:s
     *
     *  @param in reader hooked to a response from the GetHighscore.php script.
     *  @return List of highscores
     */
    private List<ScoreEntry> parseHighScoreFromReader(Reader in){

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
