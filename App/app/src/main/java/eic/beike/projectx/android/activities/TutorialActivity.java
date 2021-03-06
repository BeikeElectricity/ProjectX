package eic.beike.projectx.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import eic.beike.projectx.R;

/**
 * Simple class to display our tutorial images in the right order.
 *
 * @author Mikael
 */
public class TutorialActivity extends Activity {

    private ImageView imageView;
    private Button nextButton;
    private Button backButton;
    private int index;

    private final int[] imageIds = new int[]{
            R.drawable.tutorial_1,
            R.drawable.tutorial_2,
            R.drawable.tutorial_3,
            R.drawable.tutorial_4,
            R.drawable.tutorial_5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        imageView = (ImageView) findViewById(R.id.imageView);
        nextButton = (Button) findViewById(R.id.nextButton);
        backButton = (Button) findViewById(R.id.backButton);
        index = 0;
    }

    /**
     * @param v The input form the "Next" button. Not used, but required
     */
    public void next(View v) {
        if(index == imageIds.length - 1){
            finish();
        }

        if(index < imageIds.length - 1){
            index++;
        }

        if(index == imageIds.length - 1){
            nextButton.setText("Finish");
        }

        backButton.setEnabled(true);
        imageView.setImageResource(imageIds[index]);
    }

    /**
     * @param v The input form the "Back" button. Not used, but required
     */
    public void back(View v) {
        if(index == 0){
            finish();
        }

        if(index == imageIds.length - 1){
            nextButton.setText("Next");
        }

        if(index > 0){
            index--;
        }

        nextButton.setEnabled(true);
        imageView.setImageResource(imageIds[index]);
    }
}
