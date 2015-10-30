package eic.beike.projectx.util;

/**
 * Data object used to send score entries from the database to the high score activity.
 *
 * @autor adam
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
