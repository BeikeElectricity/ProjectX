package eic.beike.projectx.util;


import eic.beike.projectx.R;

/**
 * This is the color that is both represented in the GameModel and GameActivity for the UI
 *
 * @author simon
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
