package game.alyosharomanov.tiles;

import java.util.Random;

/**
 * Main game component, with all the logic, and back-end of the game
 */
public class GameLogic {
    private CellLogic board[][];
    private int boardSize;
    private int numColors;
    private int steps = 0;
    private int maxSteps;

    //seed creation
    private String seed;
    private static final String SEED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 15;

    /**
     * Creates a board with based on size and color
     *
     * @param boardSize size of the board
     * @param numColors number of total possible colors
     */
    public GameLogic(int boardSize, int numColors) {
        this.boardSize = boardSize;
        this.numColors = numColors;
        this.seed = generateRandomSeed();

        initializeBoard();

        //formula for maximum number of moves
        maxSteps = 30 * (boardSize * numColors) / (15 * 6);

    }

    /**
     * Gets color of the board's x and y coordinates
     *
     * @param x "x" or column number
     * @param y "y" or row number
     * @return color of the cell
     */
    public int getColor(int x, int y){
        return board[y][x].getColor();
    }

    /**
     * Returns the board's dimension
     *
     * @return board dimension as an integer
     */
    public int getBoardDimensions() {
        return boardSize;
    }

    /**
     * Returns the current board's seed as a String
     *
     * @return seed as an String
     */
    public String getSeed() {
        return seed;
    }

    /**
     * Returns the current number of steps used
     *
     * @return current number of steps
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Returns the max amount of steps of this board arrangement
     *
     * @return max number of steps
     */
    public int getMaxSteps() {
        return maxSteps;
    }

    /**
     * Method to initialize the board
     * using double embedded for loop and seed
     */
    private void initializeBoard() {
        board = new CellLogic[boardSize][boardSize];
        Random r = new Random(seed.hashCode());
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                board[y][x] = new CellLogic(r.nextInt(numColors));
            }
        }
        board[0][0].setIsProcessed();
        fillCycle(board[0][0].getColor(), true);
        return;
    }

    /**
     * main logic behind updating colors on the board
     * @param passedColor color given by user
     * @param init true/false depending if the saftey net should be bypassed
     *             (only used for initialing board)
     */
    public void fillCycle(int passedColor, boolean init) { //replacement
        boolean keepGoing = true;
        int currColor = board[0][0].getColor(); //current
        //no change; invalid input
        if (currColor == passedColor) {
            if (!init){
                return;
            }
        }

        while (keepGoing) {
            keepGoing = false;
            for (int y = 0; y < board.length; y++) {
                for (int x = 0; x < board.length; x++) {
                    if (board[y][x].getColor() == currColor && board[y][x].getIsProcessed()) {
                        board[y][x].setColor(passedColor);
                    } else if (board[y][x].getColor() == passedColor) {
                        if ((y != boardSize - 1 && board[y + 1][x].getIsProcessed() == true) ||
                                (y != 0 && board[y - 1][x].getIsProcessed() == true) ||
                                (x != boardSize - 1 && board[y][x + 1].getIsProcessed() == true) ||
                                (x != 0 && board[y][x - 1].getIsProcessed() == true)) {
                            if (board[y][x].getIsProcessed() == false){
                                board[y][x].setIsProcessed();
                                keepGoing = true;
                            }
                        }
                    }
                }
            }
        }
        steps++;
        return;
    }

    /**
     * Check if the game has been won
     *
     * @return true if won; false if not
     */
    public boolean checkWin() {
        int finalColor = board[0][0].getColor();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                if (finalColor != board[y][x].getColor()) {
                    return false;
                }
                finalColor = board[y][x].getColor();
            }
        }
        return true;

    }

    /**
     * Deed creation based of of milliseconds and math.random
     *
     * @return returns seed as a String which will be used to
     * generate random colors on the board
     */
    private String generateRandomSeed() {
        Random randInt = new Random(System.currentTimeMillis());
        String currSeed = "";
        for (int i = 0; i < randInt.nextInt((MAX_LENGTH - MIN_LENGTH) + 1) + MIN_LENGTH; i++) {
            currSeed += SEED_CHARS.charAt(randInt.nextInt(SEED_CHARS.length()));
        }
        return currSeed;
    }
}
