package eic.beike.projectx.network.projectXServer;

import junit.framework.TestCase;

/**
 * Test the database interaction. As of now this is against the live server.
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

    }

    public void testGetTopTen() throws Exception {

    }

    public void testGetPlayerTopTen() throws Exception {

    }
}