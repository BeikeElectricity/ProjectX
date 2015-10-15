package eic.beike.projectx.network.projectXServer;

import eic.beike.projectx.util.ScoreEntry;

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
   boolean register(String id, String name) throws Exception;

    /**
     * @return True if score was recorded correctly, false if it failed.
     */
    boolean recordScore(String playerId, int score, long time, String bus) throws Exception;

    /**
     * @return The best ten scores for all players on all buses.
     */
    List<ScoreEntry> getTopTen() throws Exception;

    /**
     * @return The best ten scores for the given player.
     */
    List<ScoreEntry> getPlayerTopTen(String playerId) throws Exception;

}
