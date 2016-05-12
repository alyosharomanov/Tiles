package game.alyosharomanov.tiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

/**
 * The buttons to be displayed at the bottom of main activity to select wanted color
 */
public class ButtonsView extends View{
    Paint buttonPaint;
    Drawable button;

    /**
     * Initialize buttons at the bottom of the screen
     *
     * @param context depends on number of colors, boardsize, and other factors
     */
    public ButtonsView(Context context) {
        super(context);
        button = ContextCompat.getDrawable(getContext(), R.drawable.button);
        buttonPaint = new Paint();
        buttonPaint.setColor(Color.BLACK);
        buttonPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * Sets button colors
     *
     * @param color of the current button
     */
    public void setColor(int color) {
        button.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return;
    }

    /**
     * override for onSizeChanged; updates board when a new size is selected
     * @param width
     * @param height
     * @param oldWidth
     * @param oldHeight
     */
    @Override
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        if (width > height) {
            buttonPaint.setTextSize(height);
        } else {
            buttonPaint.setTextSize(width);
        }
    }

    /**
     * Override for onDraw; modifies canvas to draw the grid
     *
     * @param c Canavs that has been passed to onDraw
     */
    @Override
    public void onDraw(Canvas c) {
        button.setBounds(   getPaddingLeft(), getPaddingTop(),
                            c.getWidth() - getPaddingRight(),
                            c.getHeight() - getPaddingBottom());
        button.draw(c);
    }
}
