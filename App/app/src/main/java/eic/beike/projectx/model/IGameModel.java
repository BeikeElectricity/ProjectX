package eic.beike.projectx.model;


/**
 * Interface called by the game activity.
 *
 * @author alex
 */
public interface IGameModel {

    /**
     * End the round and stop tracking the bus.
     */
    void abortRound();

    /**
     * Try to claim the bonus points.
     */
    void claimBonus();

    /**
     * Press the grid button at the specified position. First row is 0 and first column is 0.
     */
    void pressButton(int row, int column);

    void generateButtons();


    Count getCount();

    void triggerError(String msg);

}
