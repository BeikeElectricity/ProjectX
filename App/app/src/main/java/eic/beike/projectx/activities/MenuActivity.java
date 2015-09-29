package eic.beike.projectx.activities;

import eic.beike.projectx.R;
import eic.beike.projectx.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(MenuActivity.RESULT_CANCELED);
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


    /**
     *
     * @param v
     */
    public void startHighscore(View v) {
        startActivity(new Intent(this, HighscoreActivity.class));
    }

}