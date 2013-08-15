package com.mattmarchany.sketch;

/**
 * Created by Matt Marchany on 7/29/13.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawView extends View {
    private static final String TAG = "DrawView";

    private List<Path> paths = new ArrayList<Path>();
    private List<Path> undoPaths = new ArrayList<Path>();
    private Map<Path, Integer> colorMap = new HashMap<Path, Integer>();

    private Paint currentPaint;
    private Path currentPath;

    private int currentColor;

    public void setColor(int color) {
        currentPaint = new Paint();
        currentPaint.setColor(color);
        currentPaint.setAntiAlias(true);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeWidth(5);
        currentColor = color;
    }

    public int getColor() {
        return currentPaint.getColor();
    }

    public void clearScreen() {
        paths = new ArrayList<Path>();
        undoPaths = new ArrayList<Path>();
        colorMap = new HashMap<Path, Integer>();
        currentPath = new Path();
        invalidate();
    }

    public void undo() {
        if (paths.size() > 0) {
            undoPaths.add(paths.remove(paths.size() - 1));
            invalidate();
        }
    }

    public void redo() {
        if (undoPaths.size() > 0) {
            paths.add(undoPaths.remove(undoPaths.size() - 1));
            invalidate();
        }
    }

    public DrawView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        currentPaint = new Paint();
        currentPaint.setColor(Color.BLACK);
        currentPaint.setAntiAlias(true);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeWidth(5);
        currentColor = Color.BLACK;

        currentPath = new Path();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        for (Path p : paths) {
            currentPaint.setColor(colorMap.get(p));
            canvas.drawPath(p, currentPaint);
        }

        currentPaint.setColor(currentColor);
        canvas.drawPath(currentPath, currentPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                undoPaths = new ArrayList<Path>();
                currentPath.moveTo(x, y);
                currentPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                currentPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                currentPath.lineTo(x, y);
                paths.add(currentPath);
                colorMap.put(currentPath, currentColor);
                currentPath = new Path();
                break;
            default:
                break;
        }

        invalidate();
        return true;
    }
}
