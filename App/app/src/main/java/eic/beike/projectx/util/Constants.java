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
<<<<<<< HEAD
    /**
     * Network constants.
     */
=======
    public static final String SERVER_URL = new String("http://ec2-52-28-202-131.eu-central-1.compute.amazonaws.com/");
>>>>>>> d888b9646419f1b3bf3a08e59ffc74c38123f02c
    public static final String BASE_URL = new String("https://ece01.ericsson.net:4443/ecity");
    public static final String AUTHORIZATION = new String(!!!!!!!!!!!);

    /**
     * Bundle operation types. Used when passing messages in a bundle to an activity.
     */

    public static final String UPDATESCORE = "UPDATESCORE";
    public static final String UPDATEBOARD = "UPDATEBOARD";
    public static final String SELECTBUTTON = "SELECTBUTTON";
    public static final String DESELECTBUTTON = "DESELECTBUTTON";
    public static final String BONUSBUTTON = "BONUSBUTTON";
    public static final String SWOPBUTTON = "SWOPBUTTON";
}
