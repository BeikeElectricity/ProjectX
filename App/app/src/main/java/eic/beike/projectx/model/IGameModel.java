package eic.beike.projectx.model;

import android.os.Handler;

/**
 * Interface called by the game activity.
 *
 * @author alex
 */
public interface IGameModel {
    /**
     * Try to claim the bonus points.
     */
    void claimBonus();

    void setHandler(Handler h);

    /**
     * Press the grid button at the specified position. First row is 0 and first column is 0.
     */
    void pressButton(int row, int column);

    void generateButtons();
}