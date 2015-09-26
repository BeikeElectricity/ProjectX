package eic.beike.projectx.busdata;

import com.google.gson.annotations.SerializedName;

/**
 * Small data class used by gson, comparation on timestamp
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
