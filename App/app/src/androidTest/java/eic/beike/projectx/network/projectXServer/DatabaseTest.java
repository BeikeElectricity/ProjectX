package eic.beike.projectx.network.projectXServer;

import eic.beike.projectx.util.ScoreEntry;
import junit.framework.TestCase;

import java.util.List;

/**
 * Test the database interaction. As of now this is against the live server.
 * TODO: Setup test database that resets after every test.
 * Created by alex on 10/1/15.
 */
public class DatabaseTest extends TestCase {
    private Database db;

    /**
     * We use a fresh object for every test.
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        db = new Database();

    }

    public void testRegister() throws Exception {
        String id = "test" + Long.toString(System.currentTimeMillis());
        //Should be able to register.
        boolean success = db.register(id, "alex");
        assertTrue(success);
        //Should not be able to register twice.
        success = db.register(id, "alex");
        assertFalse(success);
    }

    public void testRecordScore() throws Exception {
        //Create two users.
        String id1 = "test" + Long.toString(System.currentTimeMillis());
        String id2 = "test" + Long.toString(System.currentTimeMillis() + 10);
        //Register one of them.
        if(db.register(id1, "alex")) {
            //Record a valid score.
            long t = System.currentTimeMillis();
            boolean success = db.recordScore(id1,10,t,"Ericsson$100020");
            assertTrue(success);
            //Try again. This should not be allowed.
            success = db.recordScore(id1,10,t,"Ericsson$100020");
            assertFalse(success);
            //Try with unknown player, should not be allowed.
            success = db.recordScore(id2,10,System.currentTimeMillis(),"Ericsson$100020");
            assertFalse(success);
        }
    }

    public void testGetTopTen() throws Exception {
        //Get the high score
        List<ScoreEntry> results = db.getTopTen();
        //We should get ten entries
        assertTrue(results.size() == 10 );
        //All entries should be filled.
        for(ScoreEntry entry : results){
            assertTrue(entry.getName() != null);
            assertTrue(entry.getScore() != null);
        }

    }

    public void testGetPlayerTopTen() throws Exception {

        //Get the high score for existing player
        List<ScoreEntry> results = db.getPlayerTopTen("alex");
        //We should get ten entries
        assertTrue(results.size() != 0 );
        //All entries should be filled.
        for(ScoreEntry entry : results){
            assertTrue(entry.getName() != null);
            assertTrue(entry.getScore() != null);
        }

        //Try to get for player with no recorded scores.
        results = db.getPlayerTopTen(Long.toString(System.currentTimeMillis()));
        //We should get no results.
        assertTrue(results.size() == 0);
    }
}