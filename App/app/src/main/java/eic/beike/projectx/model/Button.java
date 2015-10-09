package eic.beike.projectx.model;

import eic.beike.projectx.util.Colour;

/**
 * Created by Simon on 2015-10-06.
 */
public class Button {

    public Colour colour;
    public int score;
    public boolean counted;

    public Button(Colour colour, int score) {
        this.colour = colour;
        this.score = score;
        this.counted = false;
    }

    @Override
    public boolean equals(Object o) {

        if(o instanceof Button) {
            Button other = (Button) o;
            if (this.colour == other.colour) {
                if(this.score == other.score) {
                    return true;
                }
            }
        }

        return false;
    }

}