package game.alyosharomanov.tiles;

/**
 * A class that represents one cell of the BoardView
 */
public class CellLogic {
    private boolean isProcessed = false;
    private int color;

    /**
     * Initializer for the cell;
     * defaults to require color for less bugs
     *
     * @param color for the cell to be
     */
    public CellLogic(int color) {
        this.color = color;
    }

    /**
     * Returns true if the cell is owned, false otherwise
     *
     * @return true/false depending on if the cell is owned
     */
    public boolean getIsProcessed() {
        return isProcessed;
    }

    /**
     * Sets to true the fact that the cell is processed;
     * setting to false not included for less bugs
     */
    public void setIsProcessed() {
        isProcessed = true;
    }

    /**
     * Returns the cell's color
     *
     * @return color of the cell
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the cell's color
     *
     * @param i an integer representing the cell's color
     */
    public void setColor(int i) {
        color = i;
    }
}

