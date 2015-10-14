package eic.beike.projectx.handlers;

/**
 * Created by Simon on 2015-10-14.
 */
public interface ITriggers {

    void triggerNewScore(int latestScore, int totalscore);
    void triggerNewBonus(int bonus);
    void triggerDeselectButton(int row,int column);
    void triggerSelectButton(int row, int column);
    void triggerSwapButtons(int row, int column, int pressedR, int pressedC);
    void triggerNewButton(int row, int column, int androidColor);
    void triggerError(String errorText);
}
