package eic.beike.projectx.model;

/**
 * Created by Simon on 2015-10-08.
 */
public class Count implements ScoreCountApi{

    public int currentBonus = 0;

    @Override
    public int count(long t1, long t2) {
        if(t1 > t2) {
            return Math.abs((int)(t1 / t2) * currentBonus);
        } else {
            return Math.abs((int)(t2 / t1) * currentBonus);
        }
    }

    public int sum(Button[][] buttons) {
        return columns(buttons) + rows(buttons);
    }

    /**
     *
     * @return returns the score fomr all buttons that are "three of a kind"
     *         it also sets that they are counted so they can be generated again
     */
    private int columns(Button[][] buttons) {
        int count = 0;
        for(int i = 0; i < buttons.length-1; i++) {
            if(buttons[i][i].colour == buttons[i][i+1].colour
                    && buttons[i][i].colour == buttons[i][i+2].colour) {

                count += buttons[i][i].score + buttons[i][i+1].score +  buttons[i][i+2].score;
                buttons[i][i].counted = true;
                buttons[i][i+1].counted = true;
                buttons[i][i+2].counted = true;
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
            if (buttons[i][i].colour == buttons[i + 1][i].colour
                    && buttons[i][i].colour == buttons[i + 2][i].colour) {
                count += buttons[i][i].score + buttons[i + 1][i].score + buttons[i + 2][i].score;
                buttons[i][i].counted = true;
                buttons[i + 1][i].counted = true;
                buttons[i + 2][i].counted = true;
            }
        }
        return count;
    }
}
