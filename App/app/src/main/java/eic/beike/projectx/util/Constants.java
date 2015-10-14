package eic.beike.projectx.util;

/**
 * Global constants.
 *
 * Constructor call in network constants to avoid compile time inlining making
 * testing easier (reflect to change possible.)
 *
 * Created by alex on 9/22/15.
 */
public class Constants {
    /**
     * Network constants.
     */
    public static final String SERVER_URL = new String("http://ec2-52-28-202-131.eu-central-1.compute.amazonaws.com/");
    public static final String BASE_URL = new String("https://ece01.ericsson.net:4443/ecity");
    public static final String SETTINGS_FILE = "settings";
    public static final String NAME_FIELD = "name";
    public static final String ID_FIELD = "id";
    /**
     * Bundle operation types. Used when passing messages in a bundle to an activity.
     */

    public static final String UPDATESCORE = "UPDATESCORE";
    public static final String UPDATEBOARD = "UPDATEBOARD";
    public static final String SELECTBUTTON = "SELECTBUTTON";
    public static final String DESELECTBUTTON = "DESELECTBUTTON";
    public static final String BONUSBUTTON = "BONUSBUTTON";
    public static final String SWOPBUTTON = "SWOPBUTTON";
    public static final String AUTHORIZATION = new String("Z3JwMjU6cnZyMkMqT25kNw==");
    public static final long ONE_SECOND_IN_MILLI = 1000;
}
