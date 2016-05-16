package game.alyosharomanov.tiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Prints the board in the main Activity
 */
public class BoardView extends View {
    private GameLogic drawGame;
    private int boardSize;
    private int cellSize;
    private int xOffset;
    private int yOffset;
    private Paint text;
    private Paint paints[];

    /**
     * Creates main Board view, sets basic parameters
     *
     * @param context implemented from Android.view
     * @param attrs   implemented from Android.view
     */
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        text = new Paint();
        text.setColor(Color.BLACK);
        text.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * Override for onSizeChanged from Android.view
     *
     * @param newWidth
     * @param newHight
     * @param oldWidth
     * @param oldHeight
     */
    @Override
    protected void onSizeChanged(int newWidth, int newHight, int oldWidth, int oldHeight) {
        setInfo();
        return;
    }

    /**
     * Sets info for the grid layout
     */
    private void setInfo() {
        int dimension;
        if (getWidth() > getHeight()) {
            dimension = getHeight();
        } else {
            dimension = getWidth();
        }

        if (boardSize != 0) {
            cellSize = dimension / boardSize;
            xOffset = (getWidth() - dimension) / 2;
            yOffset = (getHeight() - dimension) / 2;
            text.setTextSize(cellSize);
        }
        return;
    }

    /**
     * Sets the board size
     * @param boardSize uses given dimension to make a board
     */
    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        setInfo();
        return;
    }

    /**
     * Draws the board view
     *
     * @param game to be drawn
     */
    public void draw(GameLogic game) {
        drawGame = game;
        invalidate();
        return;
    }

    /**
     * Sets colors to be used for board
     *
     * @param paints differnet paints to be used
     */
    public void setPaints(Paint[] paints) {
        this.paints = paints;
        invalidate();
        return;
    }

    /**
     * Override for onDraw from Android.view
     *
     * @param temp Canvas that will be modified each GameActivity
     */
    @Override
    protected void onDraw(Canvas temp) {
        //safety net to stop nullPointerException error
        if (drawGame == null) {
            return;
        }

        //double embedded for loop to initialize the board
        for (int y = 0; y < drawGame.getBoardDimensions(); y++) {
            for (int x = 0; x < drawGame.getBoardDimensions(); x++) {
                temp.drawRect(  x * cellSize + xOffset,             //left
                                y * cellSize + yOffset,             //top
                                (x + 1) * cellSize + xOffset,       //right
                                (y + 1) * cellSize + yOffset,       //bottom
                                paints[drawGame.getColor(x, y)]);     //paint
            }
        }
        return;
    }
}
