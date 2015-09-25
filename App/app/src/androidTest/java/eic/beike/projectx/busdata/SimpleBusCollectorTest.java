package eic.beike.projectx.busdata;

import junit.framework.TestCase;

/**
 * Created by alex on 9/23/15.
 */
public class SimpleBusCollectorTest extends TestCase {

    SimpleBusCollector collector = new SimpleBusCollector();

    public void setUp() throws Exception {
        super.setUp();
        collector.chooseBus("Ericsson$Vin_Num_001");
    }

    public void testHttpGetBusData() throws Exception {
        BusData response = collector.getBusData();
        System.out.printf(response.toString());
    }

    public void testDetermineBus() throws Exception {

    }

    public void testChooseBus() throws Exception {

    }
}