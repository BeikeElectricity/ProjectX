package eic.beike.projectx.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import eic.beike.projectx.R;
import eic.beike.projectx.busdata.Sensor;
import eic.beike.projectx.model.UserEvent;
import eic.beike.projectx.model.GameModel;

/**
 * Created by Mikael on 2015-09-22.
 */
public class GameActivity extends Activity {

    private GameModel gameModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        gameModel = new GameModel();
        gameModel.start();
        setContentView(R.layout.activity_game);
    }

    @Override
    protected void onDestroy(){
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
    public void onDoorOpen(View v){
        gameModel.onClick(new UserEvent(System.currentTimeMillis(), Sensor.Opendoor));
    }

    public void onLeft(View v){

    }

    public void onRight(View v){

    }
}
