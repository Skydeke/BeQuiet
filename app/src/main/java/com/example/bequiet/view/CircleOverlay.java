package com.example.bequiet.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.bequiet.R;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

public class CircleOverlay extends Overlay {


    private final Paint paint;

    private float referenceScale;

    private final float radiusPx;

    public CircleOverlay(Context context, float radiusPx, float referenceScale) {
        super(context);
        this.radiusPx = radiusPx;
        this.referenceScale = referenceScale;
        // Set up the paint for drawing the circle
        this.paint = new Paint();
        this.paint.setColor(context.getResources().getColor(R.color.blue_gray_600));
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(5);

    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);

        // Convert latitude and longitude to pixel coordinates on the map
        Point point = mapView.getProjection().toPixels(mapView.getMapCenter(), null); //keep the point in the center
        // Calculate the radius in pixels based on the map's scale factor
        // Draw the circle on the canvas
        canvas.drawCircle(point.x, point.y, radiusPx, this.paint);
    }


    public void setReferenceScale(float referenceScale){
        this.referenceScale = referenceScale;
    }

    public float getRadiusInMeter() {
        return this.radiusPx/this.referenceScale;

    }

}