/* GraphView.java
 * ------------------
 * Author: Danial Fitri Ghazali (dfx) from dot cat
 * Contact: danialfitrighazali@gmail.com
 * Description: Custom drawable that will display and update the graph in the
 * Normal Distribution mode
 */

// Package declaration
package com.dotcat.graph;

// Imports
import androidx.annotation.NonNull;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;

// Start of class
public class GraphView extends Drawable implements Drawable.Callback, Runnable
{
    // Properties

    // Used for redrawing the graph
    private Paint paint;
    private Paint line;

    // Defines the size of the graph
    private double offX;
    private double w;

    // Constructor
    GraphView(double offX, double w)
    {
        paint = new Paint();
        line = new Paint();
        paint.setARGB(0xff, 0xad, 0xad, 0xff);
        line.setARGB(0xff, 0xad, 0xad, 0xff);
        this.w = w;
        this.offX = offX;
    }

    // This method will be used to update the graph.
    // It will then redraw itself
    void setBounds(double offX, double w)
    {
        this.w = w;
        this.offX = offX;
        invalidateSelf();
    }

    // All the redraw logic

    @Override
    public void draw(Canvas c)
    {
        int x = getBounds().left;
        int y = getBounds().top;
        int width = getBounds().width();
        int height = getBounds().height();
        c.drawRect((int)(x + offX * width), y, (int)(w * width), height, paint);
        c.drawRect((int)(x + offX * width - 1), y, (int)(x + offX * width + 1), height, line);
        c.drawRect((int)(w * width - 1), y, (int)(w * width + 1), height, line);
    }

    // REQUIRED OVERRIDES
    // NOTE: One of the methods are apparently deprecated. It works for now and this class
    // doesn't really use it. But it's better to update it later to use new updated graphic methods.
    // TODO: Update to newer android animation/graphics classes or convert the class to a View

    @Override
    public void setAlpha(int alpha)
    {

    }

    @Override
    public void setColorFilter(ColorFilter color)
    {

    }

    @Override
    public int getOpacity()
    {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable)
    {
        super.invalidateSelf();
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable thread, long time)
    {
        invalidateDrawable(drawable);
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable thread)
    {
        unscheduleSelf(thread);
    }

    @Override
    public void run()
    {

    }
}