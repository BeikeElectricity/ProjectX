package eic.beike.projectx.busdata;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import eic.beike.projectx.util.Constants;
import junit.framework.TestCase;

import javax.net.ssl.SSLSocketFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;

/**
 * Test our SimpleBusCollector class, we mock the server so make
 * sure this is up to date with the actual api!
 *
 * Created by alex on 9/23/15.
 */
public class SimpleBusCollectorTest extends TestCase {

    MockWebServer server;
    SimpleBusCollector collector = new SimpleBusCollector();

    /**
     * Change the util.Constants.BASE_URL field to our mock server. This must be run after the server is started.
     */
    private void reflectBaseUrl() throws  Exception{
        //Change base url to the mockserver.
        HttpUrl url = server.url("/projectx/");
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
        server.useHttps((SSLSocketFactory) SSLSocketFactory.getDefault(), false);
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

    //////////////////////////////////////// THE TESTS ////////////////////////////////////////////////////////////////

    public void testLatestDataChosen() throws Exception {
        //Add response and start server.
        server.enqueue(
                new MockResponse().setBody("[{\"resourceSpec\":\"Stop_Pressed\",\"timestamp\":\"1442391279000\",\"value\":\"false\",\"gatewayId\":\"Vin_Num_001\"}" +
                                           " {\"resourceSpec\":\"Stop_Pressed\",\"timestamp\":\"1442391279999\",\"value\":\"true\",\"gatewayId\":\"Vin_Num_001\"} ]"));
        server.start();
        reflectBaseUrl();
        BusData response = collector.getBusData();
        assertTrue(response.isStopPressed());
    }

    public void testBusDataStamped() {
        long t1 =  System.currentTimeMillis();
        BusData response = collector.getBusData();
        long t2 = System.currentTimeMillis();
        assertTrue(t1 <= response.getTimestamp() && response.getTimestamp() <= t2);

    }

    public void testBusDataFilled() throws Exception {
        //Add response and start server.
        server.enqueue(
                new MockResponse().setBody("[{\"resourceSpec\":\"Stop_Pressed\",\"timestamp\":\"1442391279000\",\"value\":\"false\",\"gatewayId\":\"Vin_Num_001\"}"  +
                                           " {\"resourceSpec\":\"At_Stop\",\"timestamp\":\"1442391279999\",\"value\":\"true\",\"gatewayId\":\"Vin_Num_001\"} ]" +
                                           " {\"resourceSpec\":\"Accelerator_Pedal_Position\",\"timestamp\":\"1442391279999\",\"value\":\"50\",\"gatewayId\":\"Vin_Num_001\"} ]" +
                                           " {\"resourceSpec\":\"Ambient_Temperature\",\"timestamp\":\"1442391279999\",\"value\":\"25\",\"gatewayId\":\"Vin_Num_001\"} ]" +
                                           " {\"resourceSpec\":\"Next_Stop\",\"timestamp\":\"1442391279999\",\"value\":\"Chalmers\",\"gatewayId\":\"Vin_Num_001\"} ]"));
        server.start();
        reflectBaseUrl();
        BusData response = collector.getBusData();
        assertTrue(response.isFull());
    }

    public void testEmptyResponse() throws Exception {
        server.enqueue(
                new MockResponse().setBody("[]"));
        server.start();
        reflectBaseUrl();
        BusData response = collector.getBusData();
        assertTrue(!response.isFull());
    }

    public void testHttpRequestNotOK() throws Exception {
        server.enqueue(
                new MockResponse().setStatus("HTTP/1.1 401 Unauthorized"));
        server.start();
        reflectBaseUrl();
        BusData response = collector.getBusData();
        assertTrue(response != null);
    }
}