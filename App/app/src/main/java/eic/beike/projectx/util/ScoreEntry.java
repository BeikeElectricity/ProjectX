package eic.beike.projectx.util;

/**
 * Created by Adam on 2015-09-29.
 */
public class ScoreEntry {

    private String name;
    private String score;

    public ScoreEntry(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getName() { return name; }
    public String getScore() { return score; }

}
