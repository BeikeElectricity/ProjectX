package eic.beike.projectx.network.projectXServer;

import lombok.Getter;

/**
 * Small response class to get status of database insertions.
 * Created by alex on 10/1/15.
 */
public class InsertResponse {
    @Getter
    private int success;
    @Getter
    private String message;
}
