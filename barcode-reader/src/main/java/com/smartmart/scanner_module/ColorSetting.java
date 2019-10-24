package com.smartmart.scanner_module;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.google.android.gms.vision.barcode.Barcode;
import com.smartmart.scanner_module.camera.Overlay;

/**
 * Graphic instance for rendering barcode position, size, and ID within an associated graphic
 * overlay view.
 */
public class ColorSetting extends Overlay.Graphic {

    private int output;

    private static final int COLOR_CHOICES[] = {
            android.graphics.Color.BLUE,
            android.graphics.Color.CYAN,
            android.graphics.Color.GREEN
    };

    private static int ColorIndex = 0;

    private Paint framecolor;
    private Paint textcolor;
    private volatile Barcode barcode;

    ColorSetting(Overlay overlay) {
        super(overlay);

        ColorIndex = (ColorIndex + 1) % COLOR_CHOICES.length;
        final int color = COLOR_CHOICES[ColorIndex];

        framecolor = new Paint();
        framecolor.setColor(color);
        framecolor.setStyle(Paint.Style.STROKE);
        framecolor.setStrokeWidth(4.0f);

        textcolor = new Paint();
        textcolor.setColor(color);
        textcolor.setTextSize(36.0f);
    }

    public int getId() {
        return output;
    }

    public void setId(int id) {
        this.output = id;
    }

    public Barcode getBarcode() {
        return barcode;
    }

    /**
     * Updates the barcode instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateItem(Barcode code) {
        barcode = code;
        postInvalidate();
    }

    /**
     * Draws the barcode annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Barcode code = barcode;
        if (code == null) {
            return;
        }

        // Draws the bounding box around the barcode.
        RectF rect = new RectF(code.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
        canvas.drawRect(rect, framecolor);

        // Draws a label at the bottom of the barcode indicate the barcode value that was detected.
        canvas.drawText(barcode.rawValue, rect.left, rect.bottom, textcolor);
    }
}
