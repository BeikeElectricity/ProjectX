package eic.beike.projectx.busdata;


/**
 * The latest bus data before the given timestamp.
 * Created by alex on 9/22/15.
 */
public class BusData {

    /**
     * The time that the sensor data was compiled to this object.
     */
    public Integer timestamp;


    /**
     * The pedal position in percent (0 to 100) TODO: Confirm 100 is full throttle.
     * Updated once per second in bus
     */
    public Integer pedalPosition;

    /**
     * The temperature outside of the bus.
     * Updated once per second in bus
     */
    public Integer temperatureOutside;

    /**
     * Whether the bus is currently at a bustation
     */
    public Boolean atStop;


    /**
     * Whether the stop button has been pressed.
     */
    public Boolean stopPressed;

    /**
     * The name of the next stop.
     */
    public String nextStop;

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
