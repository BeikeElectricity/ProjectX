package eic.beike.projectx.network.projectXServer;

import android.util.Log;
import com.google.gson.Gson;
import eic.beike.projectx.network.RetrieveReader;
import eic.beike.projectx.util.Constants;
import eic.beike.projectx.util.ScoreEntry;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to interact with our database.
 * Created by alex on 10/1/15.
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
     * @return The best ten scores for all players on all buses.
     */
    @Override
    public List<ScoreEntry> getTopTen() {
        return new ArrayList<ScoreEntry>();
    }

    /**
     * @param playerId
     * @return The best ten scores for the given player.
     */
    @Override
    public List<ScoreEntry> getPlayerTopTen(String playerId) {
        return new ArrayList<ScoreEntry>();
    }
}
