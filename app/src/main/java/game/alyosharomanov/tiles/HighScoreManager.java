package game.alyosharomanov.tiles;

import android.content.SharedPreferences;

/**
 * Class responsible for managing highscores
 */
public class HighScoreManager {
    SharedPreferences sp;

    /**
     * Gets previous highscore state
     *
     * @param sp shared prefernces
     */
    public HighScoreManager(SharedPreferences sp) {
        this.sp = sp;
    }

    /**
     * Checks if a score is a highscore
     *
     * @param boardSize size of board
     * @param numColors number of colors
     * @param steps number of steps
     * @return true/false depending on if the score is a highscore
     */
    public boolean isHighScore(int boardSize, int numColors, int steps) {
        if (!highScoreExists(boardSize, numColors)) {
            return true;
        } else {
            return sp.getInt(getString(boardSize, numColors), -1) > steps;
        }
    }

    /**
     * HighScore exists for a given boardSize and number of colors
     *
     * @param boardSize size of board
     * @param numColors number of colors
     * @return true/false
     */
    public boolean highScoreExists(int boardSize, int numColors) {
        return sp.contains(getString(boardSize, numColors));
    }

    /**
     * Gets high score for a specific boardsize and number of colors
     *
     * @param boardSize size of baord
     * @param numColors number of colors
     * @return score as an integer
     */
    public int getHighScore(int boardSize, int numColors) {
        return sp.getInt(getString(boardSize, numColors), -1);
    }

    /**
     * Sets high score for a specific boardsize and number of colors
     *
     * @param boardSize size of baord
     * @param numColors number of colors
     * @param steps number of steps
     */
    public void setHighScore(int boardSize, int numColors, int steps) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(getString(boardSize, numColors), steps);
        editor.apply();
    }

    /**
     * Converts highscore to a printable String
     *
     * @param boardSize size of board
     * @param numColors number of colors
     * @return highscore as a String
     */
    private String getString(int boardSize, int numColors) {
        return String.format("highscore_%1$d_%1$d", boardSize, numColors);
    }
}
