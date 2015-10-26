package eic.beike.projectx.android.event;

/**
 * Created by Simon on 2015-10-14.
 */
public interface IGameEventTrigger {

    void triggerNewScore(double latestScore);
    void triggerNewBonus(int bonus);
    void triggerDeselectButton(int row,int column);
    void triggerSelectButton(int row, int column);
    void triggerSwapButtons(int row, int column, int pressedR, int pressedC);
    void triggerNewButton(int row, int column, int androidColor);
    void triggerError(String errorText);
    void triggerEndRound(double score);
}
