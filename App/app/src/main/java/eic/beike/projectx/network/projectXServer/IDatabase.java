package eic.beike.projectx.network.projectXServer;

import eic.beike.projectx.util.ScoreEntry;

import java.io.Serializable;
import java.util.List;

/**
 * Used to interact with the database, these are long running network operations and should not be called
 * from the UI thread.
 *
 * @author alex
 */
public interface IDatabase {

    /**
     * Register a name for the given id.
     * @return True if registration succeded, false if it failed.
     */
   boolean register(String id, String name);

    /**
     * @return True if score was recorded correctly, false if it failed.
     */
    boolean recordScore(String playerId, int score, long time, String bus);

    /**
     * @return The best ten scores for all players on all buses.
     */
    List<ScoreEntry> getTopTen();

    /**
     * @return The best ten scores for the given player.
     */
    List<ScoreEntry> getPlayerTopTen(String playerId);

}
