package eic.beike.projectx.model;

import eic.beike.projectx.util.GameColor;

/**
 * Created by Simon on 2015-10-06.
 */
public class Button {

    public GameColor color;
    public int score;
    public boolean counted;

    public Button(GameColor colour, int score) {
        this.color = colour;
        this.score = score;
        this.counted = false;
    }

    @Override
    public boolean equals(Object o) {

        if(o instanceof Button) {
            Button other = (Button) o;
            if (this.color == other.color) {
                if(this.score == other.score) {
                    if(this.counted == other.counted) {
                        return true;
                    }

                }
            }
        }

        return false;
    }

}
