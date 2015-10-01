package eic.beike.projectx.network.projectXServer;

import android.util.Log;
import com.google.gson.Gson;
import eic.beike.projectx.network.RetrieveReader;
import eic.beike.projectx.util.Constants;
import eic.beike.projectx.util.ScoreEntry;

import java.io.BufferedReader;

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
        BufferedReader in = RetrieveReader.get(
                Constants.SERVER_URL+"Register.php?id="+ id + "&name=" + name
        );
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
     * @param playerId The id, not the name, of the player
     * @param bus
     * @return True if score was recorded correctly, false if it failed.
     */
    @Override
    public boolean recordScore(String playerId, int score, long time, String bus) {
        return false;
    }

    /**
     * @return The best ten scores for all players on all buses.
     */
    @Override
    public ScoreEntry[] getTopTen() {
        return new ScoreEntry[0];
    }

    /**
     * @param playerId
     * @return The best ten scores for the given player.
     */
    @Override
    public ScoreEntry[] getPlayerTopTen(String playerId) {
        return new ScoreEntry[0];
    }
}
