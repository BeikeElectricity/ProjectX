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
        finish();
    }

    /**
     * @param v The input form the "Play" button. Not used, but required
     */
    public void play(View v) {
        Intent intentBusWaiting = new Intent(this, BusWaitingActivity.class);
        startActivity(intentBusWaiting);
    }

    /**
     * @param v The input form the "Exit" button. Not used, but required
     */

    public void exit(View v) {
        onBackPressed();
        finish();
    }


    /**
     *
     * @param v View of the triggering ui element
     */
    public void startHighscore(View v) {
        startActivity(new Intent(this, HighscoreActivity.class));
    }

}