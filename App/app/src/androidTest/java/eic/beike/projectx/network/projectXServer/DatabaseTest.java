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

    }

    public void testGetPlayerTopTen() throws Exception {

    }
}