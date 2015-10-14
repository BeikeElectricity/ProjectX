package eic.beike.projectx.model;

import android.content.Intent;
import android.os.Handler;

import junit.framework.TestCase;

import eic.beike.projectx.activities.GameActivity;
import eic.beike.projectx.handlers.ITriggers;
import eic.beike.projectx.handlers.UITriggers;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Mikael on 2015-09-30.
 * @author Simon
 */
public class GameModelTest extends TestCase {

    private GameModel newGameModel() {
        return new GameModel(new TestTrigger());
    }

    class TestTrigger implements ITriggers {

        public TestTrigger() {

        }

        @Override
        public void triggerNewScore(int latestScore, int totalscore) {

        }

        @Override
        public void triggerNewBonus(int bonus) {

        }

        @Override
        public void triggerDeselectButton(int row, int column) {

        }

        @Override
        public void triggerSelectButton(int row, int column) {

        }

        @Override
        public void triggerSwapButtons(int row, int column, int pressedR, int pressedC) {

        }

        @Override
        public void triggerNewButton(int row, int column, int androidColor) {

        }
    }
}
