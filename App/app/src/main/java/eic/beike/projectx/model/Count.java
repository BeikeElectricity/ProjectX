package eic.beike.projectx.model;

/**
 * Created by Simon on 2015-10-08.
 */
public class Count implements ScoreCountApi{

    /**
     * The game that uses this counter.
     */
    private GameModel game;
    public int currentBonus = 0;

    public Count(GameModel game){
        this.game = game;
    }

    @Override
    public int count(long t1, long t2) {
        if(t1 > t2) {
            return Math.abs((int)(t1 / t2) * currentBonus);
        } else {
            return Math.abs((int)(t2 / t1) * currentBonus);
        }
    }

    public void sum(Button[][] buttons) {
        game.addBonus(columns(buttons) + rows(buttons));
    }

    /**
     *
     * @return returns the score fomr all buttons that are "three of a kind"
     *         it also sets that they are counted so they can be generated again
     */
    private int columns(Button[][] buttons) {
        int count = 0;
        for(int i = 0; i < buttons.length-1; i++) {
            if(buttons[i][0].colour == buttons[i][1].colour
                    && buttons[i][0].colour == buttons[i][2].colour) {

                count += buttons[i][0].score + buttons[i][1].score +  buttons[i][2].score;
                buttons[i][0].counted = true;
                buttons[i][1].counted = true;
                buttons[i][2].counted = true;
            }
        }
        return count;
    }
    /**
     *
     * @return returns the score fomr all buttons that are "three of a kind"
     *         it also sets that they are counted so they can be generated again
     */
    private int rows(Button[][] buttons) {
        int count = 0;
        for (int i = 0; i < buttons.length - 1; i++) {
            if (buttons[0][i].colour == buttons[1][i].colour
                    && buttons[1][i].colour == buttons[2][i].colour) {
                count += buttons[0][i].score + buttons[1][i].score + buttons[2][i].score;
                buttons[0][i].counted = true;
                buttons[1][i].counted = true;
                buttons[2][i].counted = true;
            }
        }
        return count;
    }
}
