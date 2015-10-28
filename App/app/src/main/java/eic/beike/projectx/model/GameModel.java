package eic.beike.projectx.model;

import java.util.Random;

import eic.beike.projectx.android.event.IGameEventTrigger;
import eic.beike.projectx.network.busdata.BusCollector;
import eic.beike.projectx.network.busdata.SimpleBusCollector;
import eic.beike.projectx.util.GameColor;

/**
 * @author Simon
 * @author Alex
 */

/**
 * This is the model for the game, which handles the representation of the game
 */
public class GameModel implements IGameModel{


    private Count count;
    private BusCollector busCollector;
    /**
     * Used to represent the board
     */
    private Button[][] buttons;

    /**
     * Used to represent which button is pressed
     */
    private int pressedC = -1;
    private int pressedR = -1;

    private double percentOfScore = 0;
    private int score = 0;

    private IGameEventTrigger triggers;
    private RoundTracker tracker;


    public GameModel(IGameEventTrigger triggers){
        super();
        busCollector = SimpleBusCollector.getInstance();
        this.triggers = triggers;
        buttons = generateNewButtons();

        //Count up instances and then create a count object.
        Count.addRunning();
        count = new Count(this);

        tracker = new RoundTracker();
        tracker.track(this);
    }

    /**
     * Adds points to the score count.
     * @param score The new score points.
     */
    @Override
    public synchronized void addScore(int score){
        this.score += score;
        triggers.triggerNewScore(this.score);
    }

    /**
     * Adds to the total scores.
     * @param percentOfScore the extra points that should be added.
     */
    @Override
    public synchronized void addPercentScore(double percentOfScore){
        this.percentOfScore = percentOfScore;
        triggers.triggerNewFactor(percentOfScore);
    }
    /**
    * Used when the stop sign lights up in the bus and the player wants a bigger multiplier
     */
    @Override
    public void claimFactor() {
        Long currentTime = System.currentTimeMillis();
        count.count(currentTime);
    }

    @Override
    public Count getCount() {
        return count;
    }

    /**
     * Generates new buttons for those that have been pressed
     */
    @Override
    public void generateButtons() {
        Random random = new Random();
        int generated;

        // While there exists rows or columns of the same color, count them and generate new.
        do {
            generated = 0;
            for (int i = 0; i < buttons.length; i++) {
                for (int j = 0; j < buttons.length; j++) {
                    if (buttons[i][j].counted) {
                        buttons[i][j] = new Button(GameColor.color(random.nextInt(3)), random.nextInt(50));
                        generated++;
                        triggers.triggerNewButton(i, j, buttons[i][j].color.getAndroidColor());
                    }
                }
            }
            count.sum(buttons);
        } while (generated > 0);
    }

    /**
     * Handles button interaction
     * @param row, represents the row of the button pressed
     * @param column, represents the column of the  button pressed
     */
    @Override
    public void pressButton(int row, int column) {
        if(pressedR < 0 && pressedC < 0) {
            //Select button
            pressedR = row;
            pressedC = column;
            triggers.triggerSelectButton(row,column);
        } else if(isSame(row, column)) {
            //if we have a selected button and it's pressed again, deselect it
            triggers.triggerDeselectButton(row, column);
            pressedR = -1;
            pressedC = -1;
        } else if (isNeighbour(row, column)) {
            //Valid swap, swap and deselect and remember to update ui
            triggers.triggerSwapButtons(row, column, pressedR, pressedC);
            swapButtons(row, column);
            triggers.triggerDeselectButton(pressedR, pressedC);
            pressedR = -1;
            pressedC = -1;
            count.sum(buttons);
            //We might have counted buttons and need to regenerate the board.
            generateButtons();
        } else {
            // Clicked button far away, select it and deselect prev selected.
            triggers.triggerDeselectButton(pressedR,pressedC);
            pressedR = row;
            pressedC = column;
            triggers.triggerSelectButton(pressedR, pressedC);
        }
    }

    private boolean isSame(int row, int column) {
       return pressedR == row && pressedC == column;
    }

    private void swapButtons(int row, int column) {
        Button temp = buttons[pressedR][pressedC];
        buttons[pressedR][pressedC] = buttons[row][column];
        buttons[row][column] = temp;
    }



    /**
     * Updates view and resets score.
     */
    protected void endRound() {
        triggers.triggerEndRound(percentOfScore * (double) score);
        score = 0;
        percentOfScore = 0;
    }

    /**
     * Abort the round without reporting a score.
     */
    public void abortRound(){
        if(tracker != null) {
            tracker.stopTracking();
        }
    }

    /**
     * Generate a new board, without three in a row
     * @return, returns a new board
     */
    private Button[][] generateNewButtons() {
        Button[][] tempList = new Button[3][3];
        Random random = new Random();
        for (int i = 0; i < tempList.length; i++) {
            for (int j = 0; j < tempList.length; j++) {
                tempList[i][j] = new Button(GameColor.color(random.nextInt(3)), random.nextInt(50));
            }
        }

        while(hasThreeInRow(tempList)) {
            for (int i = 0; i < tempList.length; i++) {
                for (int j = 0; j < tempList.length; j++) {
                    tempList[i][j].color = GameColor.color(random.nextInt(3));
                }
            }
        }
        triggerAllNewButtons(tempList);
        return tempList;
    }

    /**
     * Tells the view to update all the buttons
     * @param tempList, the board to be updated
     */
    private void triggerAllNewButtons(Button[][] tempList) {
        for (int i = 0; i < tempList.length; i++) {
            for (int j = 0; j < tempList.length; j++) {
                triggers.triggerNewButton(i, j, tempList[i][j].color.getAndroidColor());
            }
        }
    }

    public boolean hasThreeInRow(Button[][] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            if((buttons[i][0].color == buttons[i][1].color) && (buttons[i][0].color == buttons[i][2].color)) {
                return true;
            }
            if((buttons[0][i].color == buttons[1][i].color) && (buttons[0][i].color == buttons[2][i].color)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNeighbour(int row, int column) {
         if(row == pressedR && (column+1 == pressedC || column-1 == pressedC)) {
            return true;
         } else if(column == pressedC && (row+1 == pressedR || row-1 == pressedR)) {
             return true;
         } else {
             return false;
         }
     }

    public int getScore() {
        return score;
    }

    /*
    *Used for testing
     */
    public Button[][] getButtons() {
        return buttons;
    }

    public void triggerError(String msg) {
        triggers.triggerError(msg);
    }
}
