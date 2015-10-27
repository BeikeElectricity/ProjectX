package eic.beike.projectx.util;


import eic.beike.projectx.R;

/**
 * Created by Simon on 2015-10-06.
 * This is the color that is both represented in the GameModel and GameActivity for the UI
 */
public enum GameColor {

    GREEN(R.drawable.green),
    BLUE(R.drawable.blue),
    RED(R.drawable.red);

    /**
     * Carried color code initiated at first use.
     */
    private  int androidColor;
    GameColor(int androidColor){
        this.androidColor = androidColor;
    }

    /**
     * @return the value of the android color.
     */
    public int getAndroidColor(){ return androidColor; }


    public static GameColor color(int val) {
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
