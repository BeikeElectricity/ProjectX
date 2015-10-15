package eic.beike.projectx.model;


import android.util.Log;


import org.junit.Test;

import java.util.logging.Logger;

import eic.beike.projectx.handlers.ITriggers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Created by Mikael on 2015-09-30.
 * @author Simon
 */
public class GameModelTest{

    private GameModel gameModel;
    private Button[][] buttons;
    private int pressedR = -1, pressedC = -1;
    private int testBonus = 0;

    public GameModelTest() {
        this.gameModel  = newGameModel();
        this.buttons = gameModel.getButtons();
    }

    private GameModel newGameModel() {
        return new GameModel(new TestTrigger());
    }

    @Test
    public void TestGenerateButtons() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                assertNotNull(buttons[i][j]);
                assertTrue(buttons[i][j] instanceof Button);
            }
        }
    }

    @Test
    public void TestSelectFirstButton() {
        gameModel.pressButton(0,0);
        assertTrue("row", pressedC == 0);
        assertTrue("column", pressedR == 0);
    }

    @Test
    public void TestIsSame() {
        gameModel.pressButton(0,0);
        gameModel.pressButton(0,0);
        assertTrue("row", pressedC == -1);
        assertTrue("column", pressedR == -1);
    }

    @Test
    public void TestIsNeighbor() {
        gameModel.pressButton(0,0);
        gameModel.pressButton(1,0);
        assertTrue(buttons[0][0].equals(gameModel.getButtons()[0][0]));
        assertTrue(buttons[1][0].equals(gameModel.getButtons()[1][0]));
    }

    @Test
    public void TestClickedFarAway() {
        gameModel.pressButton(0,0);
        gameModel.pressButton(2,2);
        assertTrue("row", pressedC == 2);
        assertTrue("column", pressedR == 2);
    }

    @Test
    public void TestNewBonus() {
        gameModel.getButtons()[0][0].counted = true;
        gameModel.getButtons()[1][0].counted = true;
        gameModel.pressButton(0,0);
        gameModel.pressButton(1, 0);

        assertFalse(gameModel.getButtons()[0][0].counted);
        assertFalse(gameModel.getButtons()[1][0].counted);
        assertTrue(gameModel.getBonus() > 0);
    }

    class TestTrigger implements ITriggers {

        public TestTrigger() {

        }


        @Override
        public void triggerNewScore(int latestScore, int totalscore) {

        }

        @Override
        public void triggerNewBonus(int bonus) {
            testBonus += bonus;
        }

        @Override
        public void triggerDeselectButton(int row, int column) {
            assertNotNull(row);
            assertNotNull(column);
            pressedR = -1;
            pressedC = -1;
        }

        @Override
        public void triggerSelectButton(int row, int column) {
            assertNotNull(row);
            assertNotNull(column);
            pressedR = row;
            pressedC = column;

        }

        @Override
        public void triggerSwapButtons(int row, int column, int pressedR, int pressedC) {
            Button temp = buttons[row][column];
            buttons[row][column] = buttons[pressedR][pressedC];
            buttons[pressedR][pressedC] = temp;
        }

        @Override
        public void triggerNewButton(int row, int column, int androidColor) {
            assertNotNull(row);
            assertNotNull(column);
            assertNotNull(androidColor);
            assertTrue(row < 3);
            assertTrue(column < 3);

        }

        @Override
        public void triggerError(String errorText) {

        }

        @Override
        public void triggerEndRound(int score) {

        }
    }
}
