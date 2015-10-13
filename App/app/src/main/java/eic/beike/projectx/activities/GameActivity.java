package eic.beike.projectx.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import eic.beike.projectx.R;
import eic.beike.projectx.network.busdata.Sensor;
import eic.beike.projectx.handlers.GameHandler;
import eic.beike.projectx.model.UserEvent;
import eic.beike.projectx.model.GameModel;
import eic.beike.projectx.util.MessageDialog;

/**
 * @author Mikael
 * @author Adam
 */
public class GameActivity extends Activity
        implements MessageDialog.MessageDialogListener
{

    private GameModel gameModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("Score", Thread.currentThread().getName() + ":onCreate");
        Handler h = makeHandler();

        gameModel = new GameModel();
        gameModel.setHandler(h);
        gameModel.start();
        setContentView(R.layout.activity_game);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameModel.stopLoop();
    }

    /**
     * @param v The input form the "Stop Button" button. Not used, but required
     */
    public void onStopButton(View v) {
        gameModel.onClick(new UserEvent(System.currentTimeMillis(), Sensor.Stop_Pressed));
    }

    /**
     * @param v The input form the "Door Open" button. Not used, but required
     */
    public void onDoorOpen(View v) {
        gameModel.onClick(new UserEvent(System.currentTimeMillis(), Sensor.Opendoor));
    }

    public void onLeft(View v) {

    }

    public void onRight(View v) {

    }

    /**
     * Creates a handler to update the ui
     * @return The handler
     */
    private Handler makeHandler() {
        return new GameHandler(Looper.getMainLooper(), this);
    }
    /**
     * Used to update the score
     * @param totalScore New score that the user got
     */
    public void showScore(int latestScore, int totalScore) {
        Log.d("Score", Thread.currentThread().getName() + ":onNewScore");
        TextView scoreText = (TextView) findViewById(R.id.textScore);
        TextView scoreEventText = (TextView) findViewById(R.id.textScoreEvent);

        scoreText.setText(String.valueOf(totalScore));
        scoreEventText.setText(String.format("Du fick %d poäng", latestScore));
    }

    /**
     *
     */
    public void showErrorDialog() {
        MessageDialog dialog = new MessageDialog();
        dialog.show(getFragmentManager(), "bus_data_unavailable");
    }



    /**
     * Used to finish the activity of ok is clicked.
     * @param dialog The triggering dialog.
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        finish();
    }

    /**
     * Used to finish the activity if the dialog is dismissed, i.e. user pressed beside the dialog.
     * @param dialog The triggering dialog.
     */
    @Override
    public void onDialogDismiss(DialogFragment dialog) {
        finish();
    }

    /**
     * If the negative (no) button is clicked. Not used.
     * @param dialog Triggering dialog
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) { /* Unused. */ }

}
