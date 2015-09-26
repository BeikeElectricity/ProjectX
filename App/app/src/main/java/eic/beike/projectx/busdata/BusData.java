package eic.beike.projectx.busdata;


import android.util.Log;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The latest bus data before the given timestamp.
 * Use lombok getters to access the fields.
 * Created by alex on 9/22/15.
 */
public class BusData {

    /**
     * Whether all fields have been populated.
     */
    private boolean full = true;

    /**
     * Sensors we consider in the app. These are initialized in the constructor.
     */
    private List<Resource> unmarkedResources;
    public BusData() {
        unmarkedResources = new ArrayList<>(5);
        unmarkedResources.add(Resource.Accelerator_Pedal_Position);
        unmarkedResources.add(Resource.Ambient_Temperature);
        unmarkedResources.add(Resource.At_Stop);
        unmarkedResources.add(Resource.Next_Stop);
        unmarkedResources.add(Resource.Stop_Pressed);
    }

    /**
     * The time that the sensor data was compiled to this object.
     */
    @Setter
    @Getter
    private Long timestamp;


    /**
     * The pedal position in percent (0 to 100) TODO: Confirm 100 is full throttle.
     * Updated once per second in bus
     */
    @Getter
    private int pedalPosition;

    /**
     * The temperature outside of the bus.
     * Updated once per second in bus
     */
    @Getter
    private int temperatureOutside;

    /**
     * Whether the bus is currently at a bus station
     */
    @Getter
    private boolean atStop;


    /**
     * Whether the stop button has been pressed.
     */
    @Getter
    private boolean stopPressed;

    /**
     * The name of the next stop.
     */
    @Getter
    private String nextStop;

    /**
     * @return true if all tracked sensors where set.
     */
    public boolean isFull() {
       return  full;
    }

    /**
     * Awkwardly switch the response for further parsing to our own model.
     *
     * @param entry One parsed line from the json, this corresponds to one value of on sensor
     *              in one bus at one time.
     */
    protected void populateField(ResponseEntry entry) {
        Resource r = Resource.valueOf(entry.resource);
        switch (r) {
            case Accelerator_Pedal_Position:
                pedalPosition = Integer.valueOf(entry.value);
                break;
            case Ambient_Temperature:
                temperatureOutside = Integer.valueOf(entry.value);
                break;
            case At_Stop:
                atStop = Boolean.valueOf(entry.value);
                break;
            case Stop_Pressed:
                stopPressed = Boolean.valueOf(entry.value);
                break;
            case Next_Stop:
                nextStop = entry.value;
                break;
            default:
                Log.d(getClass().getSimpleName(), "Value of sensor " + r.name() + " not currently used");
        }

        //Keep track of which sensors have been recorded.
        unmarkedResources.remove(r);
        full = unmarkedResources.isEmpty();
    }

//   TODO: Deal with remaining signals.
//
//    @SerializedName(Cooling_Air_Conditioning) // - Instrumentpanelens info om luftkonditioneringen
//    @SerializedName(Driver_Cabin_Temperature) // - Lufttemperatur inne i f?rarhytten
//    @SerializedName(Fms_Sw_Version_Supported) // - FMS-version som fordonet st?der
//    @SerializedName(GPS) // - GPS-position fr?n V?sttrafik
//    @SerializedName(GPS2) // - GPS-position fr?n Icomera X6
//    @SerializedName(GPS_NMEA) // - GPS-position fr?n Keolis (rmc-format)
//    @SerializedName(Journey_Info) // - Information om turen
//    @SerializedName(Mobile_Network_Cell_Info) // - Mobiln?tets cellinfo f?r Keolis WAN-l?nk
//    @SerializedName(Mobile_Network_Signal_Strength) // - Mobilsignalstyrka f?r Keolis WAN-l?nk
//    @SerializedName(Offroute) // - Fordonet avviker fr?n sin rutt
//    @SerializedName(Online_Users) // - Antalet anv?ndare som ?r uppkopplade p? WiFi-n?tet
//    @SerializedName(Opendoor) // - ?r n?gon av bussens d?rrar ?ppna
//    @SerializedName(Position_Of_Doors) // - Indikerar aktuell position p? d?rrarna
//    @SerializedName(Pram_Request) // - Instrumentpanelens info om barnvagnsknappen
//    @SerializedName(Ramp_Wheel_Chair_Lift) // - Positionen p? ramp/lift
//    @SerializedName(Status_2_Of_Doors) // - ?r d?rrarna aktiverade f?r automatisk ?ppning/st?ngning
//    @SerializedName(Ericsson$Stop_Request) // - Instrumentpanelens info om stoppknappen
//    @SerializedName(Total_Vehicle_Distance) // - Bussens totala k?rstr?cka
//    @SerializedName(Turn_Signals) // - Instrumentpanelens info om blinkersljus
//    @SerializedName(Wlan_Connectivity) // - Information om wifi-uppkopplingen
}
