package eic.beike.projectx.util;

/**
 * Created by Simon on 2015-10-06.
 */
public enum Colour {
    GREEN,
    BLUE,
    RED;


    public static Colour colour(int val) {
    if (val == 0) {
        return GREEN;
    } else if(val == 1) {
        return BLUE;
    } else if (val == 2) {
        return RED;
    }
    return GREEN;
   }
}
