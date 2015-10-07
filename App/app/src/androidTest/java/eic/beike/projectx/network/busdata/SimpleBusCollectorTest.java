package eic.beike.projectx.network.busdata;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import eic.beike.projectx.util.Constants;
import junit.framework.TestCase;

import javax.net.ssl.SSLSocketFactory;
import java.lang.reflect.Field;

/**
 * Test our SimpleBusCollector class, we mock the server so make
 * sure this is up to date with the actual api!
 *
 * Created by alex on 9/23/15.
 */
public class SimpleBusCollectorTest extends TestCase {

    MockWebServer server;
    SimpleBusCollector collector = new SimpleBusCollector();
    long testTime = 1443513328000l;
    /**
     * Change the util.Constants.BASE_URL field to our mock server. This must be run after the server is started.
     */
    private void reflectBaseUrl() throws  Exception{
        //Change base url to the mockserver.
        HttpUrl url = server.url("/projectx");
        Field field = Constants.class.getDeclaredField("BASE_URL");
        field.setAccessible(true);
        field.set(null, url.toString());
    }

    /**
     * Run before every test. Chooses a vin number and makes the server speak https.
     * @throws Exception
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        server = new MockWebServer();
        server.useHttps((SSLSocketFactory) SSLSocketFactory.getDefault(), true);
        collector.chooseBus("Ericsson$Vin_Num_001");
    }

    /**
     * Shuts down the server so a new can be started for every test.
     * @throws Exception
     */
    @Override
    public void tearDown() throws Exception {
        server.shutdown();
    }

    /****************************************************************************************************************
     *                                        THE TESTS
     * TODO: Currently the mocking has problems with the https handshake so the test are perform on live data. When
     *       this issue is fixed uncoment the reflectBaseUrl call to test on the mock server.
     *
     ****************************************************************************************************************/


    public void testBusDataStamped() {
        BusData response = collector.getBusData(testTime,Sensor.At_Stop);
        assertTrue(response.getTimestamp() ==1443513325166l );
    }


    public void testEmptyResponse() throws Exception {
        server.enqueue(
                new MockResponse().setBody("[]"));
        server.start();
        // reflectBaseUrl();
        BusData response = collector.getBusData(testTime, Sensor.Ambient_Temperature);
        assert(response.getSensor() == Sensor.UNKNOWN);
    }

    public void testHttpRequestNotOK() throws Exception {
        server.enqueue(
                new MockResponse().setStatus("HTTP/1.1 401 Unauthorized"));
        server.start();
        // reflectBaseUrl();
        BusData response = collector.getBusData(testTime, Sensor.At_Stop);
        assertTrue(response != null);
    }
}