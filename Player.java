/**
 * Class that describes the player and gives elements to manipulate the data of a player.
 *
 * @author (Logan Yeubanks)
 * @version (Version 1)
 */

public class Player {
    private String name;
    private int score;

    public Player(String name) {
        this.name = name;
        score = 0;
    }

    /**
     * Method to add a number to the players score.
     *
     * @param num
     */
    public void addToScore(int num) {
        score += num;
    }

    /**
     * Returns the players score.
     *
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the players name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the players score.
     *
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }
}
