package eic.beike.projectx.network.busdata;

/**
 * The name of the resources from the bus coupled with their sensor.
 * Created by alex on 9/22/15.
 */
public enum Resource {

    Accelerator_Pedal_Position_Value(Sensor.Accelerator_Pedal_Position),
    Ambient_Temperature_Value(Sensor.Ambient_Temperature),
    At_Stop_Value(Sensor.At_Stop),
    Cooling_Air_Conditioning_Value(Sensor.Cooling_Air_Conditioning),
    Driver_Cabin_Temperature_Value(Sensor.Driver_Cabin_Temperature),
    Fms_Sw_Version_Supported_Value(Sensor.Fms_Sw_Version_Supported),

    Latitude_Value(Sensor.GPS),
    Longitude_Value(Sensor.GPS),
    Speed_Value(Sensor.GPS),
    Course_Value(Sensor.GPS),

    Latitude2_Value(Sensor.GPS2),
    Longitude2_Value(Sensor.GPS2),

    Speed2_Value(Sensor.GPS2),
    Course2_Value(Sensor.GPS2),
    Altitude_Value(Sensor.GPS2),

    RMC_Value(Sensor.GPS_NMEA),

    Journey_Name_Value(Sensor.Journey_Info),
    Destination_Value(Sensor.Journey_Info),

    Mobile_Network_Cell_Info_Value(Sensor.Mobile_Network_Cell_Info),

    Mobile_Network_Signal_Strength_Value(Sensor.Mobile_Network_Signal_Strength),

    Bus_Stop_Name_Value(Sensor.Next_Stop),

    Offroute_Value(Sensor.Offroute),

    Total_Online_Users_Value(Sensor.Online_Users),
    Authenticated_Users_Value(Sensor.Online_Users),

    Open_Door_Value(Sensor.Opendoor),

    Position_Of_Doors_Value(Sensor.Position_Of_Doors),

    Pram_Request_Value(Sensor.Pram_Request),

    Ramp_Wheel_Chair_Lift_Value(Sensor.Ramp_Wheel_Chair_Lift),

    Status_2_Of_Doors_Value(Sensor.Status_2_Of_Doors),

    Stop_Pressed_Value(Sensor.Stop_Pressed),
    Stop_Request_Value(Sensor.Stop_Request),

    Total_Vehicle_Distance_Value(Sensor.Total_Vehicle_Distance),

    Turn_signals_Value(Sensor.Turn_Signals),

    Rssi_Value(Sensor.Wlan_Connectivity),
    Rssi2_Value(Sensor.Wlan_Connectivity),
    Cell_Id_Value(Sensor.Wlan_Connectivity),
    Cell_Id2_Value(Sensor.Wlan_Connectivity),

    EMPTY(Sensor.UNKNOWN);

    public final Sensor sensor;
    Resource(Sensor sensor) {
        this.sensor = sensor;
    }
}
