package eic.beike.projectx;

import eic.beike.projectx.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class Menu extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }


    /**
     * @param v The input form the "Play" button. Not used, but required
     */
    public void play(View v) {
        Intent intentPlay = new Intent(this, GameActivity.class);
        startActivity(intentPlay);
    }

    /**
     * @param v The input form the "Exit" button. Not used, but required
     */

    public void exit(View v) {
        System.exit(0);
    }
}