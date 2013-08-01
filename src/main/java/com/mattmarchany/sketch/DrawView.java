package com.mattmarchany.sketch;

/**
 * Created by Matt Marchany on 7/29/13.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener {
    private static final String TAG = "DrawView";

    List<Point> points = new ArrayList<Point>();
    List<Integer> newLine = new ArrayList<Integer>();

    Paint paint = new Paint();

    public void setColor(int color) {
        paint.setColor(color);
        invalidate();
    }

    public void clearScreen() {
        points = new ArrayList<Point>();
        newLine = new ArrayList<Integer>();
        invalidate();
    }

    public DrawView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        for (int i = 0; i < points.size(); i++) {
            Point newPoint = new Point();

            if (newLine.contains(i) || i == 0) {
                newPoint = points.get(i);
                path.moveTo(newPoint.x, newPoint.y);
            } else {
                newPoint = points.get(i);

                path.lineTo(newPoint.x, newPoint.y);
            }

        }

        canvas.drawPath(path, paint);
    }

    public boolean onTouch(View view, MotionEvent event) {
        Point point = new Point();
        point.x = event.getX();
        point.y = event.getY();
        points.add(point);
        invalidate();
        Log.d(TAG, "point: " + point);

        if(event.getAction() == MotionEvent.ACTION_UP) {
            // return super.onTouchEvent(event);
            newLine.add(points.size());
        }
        return true;
    }
}

class Point {
    float x, y;

    @Override
    public String toString() {
        return x + ", " + y;
    }
}
