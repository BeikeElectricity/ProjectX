package eic.beike.projectx.eicapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 9/22/15.
 */
public class ResponseEntry implements Comparable<ResponseEntry>{
    @SerializedName("resourceSpec")
    String resource;

    @SerializedName("timestamp")
    Integer timestamp;

    @SerializedName("value")
    String value;

    @SerializedName("gatewayId")
    String busVin;

    @Override
    public int compareTo(ResponseEntry other){
        return this.timestamp - other.timestamp;
    }
}
