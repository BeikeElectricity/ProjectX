package eic.beike.projectx.network.projectXServer;

import lombok.Getter;

/**
 * Small response class to get status of database insertions. Use getField()
 * to get the value of field.
 *
 * @author alex
 */
public class InsertResponse {
    @Getter
    private int success;
    @Getter
    private String message;
}
