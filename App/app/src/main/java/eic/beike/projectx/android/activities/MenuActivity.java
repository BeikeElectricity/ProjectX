package eic.beike.projectx.android.activities;

import eic.beike.projectx.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import eic.beike.projectx.android.activities.highScore.HighscoreActivity;
import eic.beike.projectx.android.dialogs.MessageDialog;

/**
 * This class handels the Menu. It has several buttons for starting different activities.
 *
 *  @author Simon
 */
public class MenuActivity extends Activity implements MessageDialog.MessageDialogListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    public void onBackPressed() {
        exit(null);
    }

    /**
     * @param v View of the triggering ui element. Not used, but required
     */
    public void play(View v) {
        Intent intentBusWaiting = new Intent(this, BusWaitingActivity.class);
        startActivity(intentBusWaiting);
    }


    /**
     * @param v View of the triggering ui element. Not used, but required
     */
    public void tutorial(View v){
        Intent intentTutorial = new Intent(this, TutorialActivity.class);
        startActivity(intentTutorial);
    }


    /**
     * @param v View of the triggering ui element. Not used, but required
     */

    public void exit(View v) {

        MessageDialog msg = new MessageDialog();
        msg.setMessage(R.string.exit_confirm);
        msg.setHaveCancelButton(true);
        msg.show(getFragmentManager(),"menu_exit");

    }


    /**
     *
     * @param v View of the triggering ui element
     */
    public void startHighscore(View v) {
        startActivity(new Intent(this, HighscoreActivity.class));
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        setResult(MenuActivity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onDialogDismiss(DialogFragment dialog) { /* unused */ }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) { /* unused */ }
}