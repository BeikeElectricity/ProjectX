package eic.beike.projectx.model;

import java.util.Random;

import eic.beike.projectx.android.event.IGameEventTrigger;
import eic.beike.projectx.network.busdata.BusCollector;
import eic.beike.projectx.network.busdata.SimpleBusCollector;
import eic.beike.projectx.util.GameColor;

import eic.beike.projectx.util.Constants;

/**
 * @author Mikael
 * @author Adam
 */
public class GameModel implements IGameModel{


    public static final long USER_EVENT_EXPIRATION_TIME = 1 * Constants.ONE_SECOND_IN_MILLI;

    private BusCollector busCollector;
    private Button[][] buttons;
    private Count count;
    private int pressedC = -1;
    private int pressedR = -1;

    /**
     * Persistent total score
     */
    private double percentOfScore = 0;
    private int bonus = 0;

    private IGameEventTrigger triggers;
    private RoundTracker tracker;


    public GameModel(IGameEventTrigger triggers){
        super();
        busCollector = SimpleBusCollector.getInstance();
        busCollector.chooseBus(BusCollector.TEST_BUSS_VIN_NUMBER);
        this.triggers = triggers;
        buttons = generateNewButtons();

        //Count up instances and then create a count object.
        //TODO: The coupling between count and GameModel is very shaky and unclear...
        Count.addRunning();
        count = new Count(this);


        tracker = new RoundTracker();
        tracker.track(this);
    }

    /**
     * Adds points to the bonus count.
     * @param bonus The new bonus points.
     */
    protected synchronized void addBonus(int bonus){
        this.bonus += bonus;
        triggers.triggerNewBonus(this.bonus);
    }

    /**
     * Adds to the total scores.
     * @param percentOfScore the extra points that should be added.
     */
    protected synchronized void addScore(double percentOfScore){
        this.percentOfScore = percentOfScore;
        triggers.triggerNewScore(percentOfScore);
    }

    @Override
    public void claimBonus() {
        Long currentTime = System.currentTimeMillis();
        count.count(currentTime);
    }


    @Override
    public void pressButton(int row, int column) {
        if(pressedR < 0 && pressedC < 0) {
            pressedR = row;
            pressedC = column;
            triggers.triggerSelectButton(row,column);
        } else if(isSame(row, column)) {
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
     * Updates view and resets score.
     */
    protected void endRound() {
        triggers.triggerEndRound(percentOfScore * (double) bonus);
        bonus = 0;
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

    private Button[][] generateNewButtons() {
        Button[][] tempList = new Button[3][3];
        Button notSame = new Button(GameColor.BLUE, 0);
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

    public int getBonus() {
        return bonus;
    }

    /*
    *Used for testing
     */
    public Button[][] getButtons() {
        return buttons;
    }

    public Count getCount() {
        return count;
    }

    public void triggerError(String msg) {
        triggers.triggerError(msg);
    }
}
