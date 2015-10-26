package eic.beike.projectx.util;

import eic.beike.projectx.BuildConfig;

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
    public static final String AUTHORIZATION = BuildConfig.CYBERCOM_API_KEY;
    public static final long ONE_SECOND_IN_MILLI = 1000;
    public static final String SETTINGS_FILE = "settings";
    public static final String NAME_FIELD = "name";
    public static final String ID_FIELD = "id";

    /**
     * Game play constants, anything that alters the feel of the game.
     */


    public static final double BUS_NORMAL_SPEED = 50;
}
