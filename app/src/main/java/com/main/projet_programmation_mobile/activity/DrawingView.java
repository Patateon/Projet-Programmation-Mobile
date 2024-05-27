package com.main.projet_programmation_mobile.activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.Queue;
import java.util.LinkedList;

public class DrawingView extends View {
    private float currentThickness = 5f;
    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;

    private float startX, startY, endX, endY;
    private int currentColor = Color.BLACK;

    private Path drawPath;
    private Paint drawPaint;

    private boolean isErasing = false;
    private boolean isFilling = false;
    private boolean isDrawing = true;

    private boolean isDrawingSquare = false;

    private boolean isDrawingCircle = false;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(currentColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        drawPath = new Path();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        if (!isErasing) {
            canvas.drawPath(drawPath, drawPaint);
        }
        if (isDrawingSquare) {
            drawSquare(canvas);
        }
        if (isDrawingCircle) {
            drawCircle(canvas);
        }
    }

    private void drawSquare(Canvas canvas) {
        float left = Math.min(startX, endX);
        float right = Math.max(startX, endX);
        float top = Math.min(startY, endY);
        float bottom = Math.max(startY, endY);
        canvas.drawRect(left, top, right, bottom, drawPaint);
    }

    private void drawCircle(Canvas canvas) {
        float radius = (float) Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2)) / 2;
        float centerX = (startX + endX) / 2;
        float centerY = (startY + endY) / 2;
        canvas.drawCircle(centerX, centerY, radius, drawPaint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isFilling) {
                    fillArea((int) touchX, (int) touchY);
                } else if (isDrawing) {
                    drawPath.moveTo(touchX, touchY);
                    startX = touchX;
                    startY = touchY;
                } else if (isDrawingSquare || isDrawingCircle) {
                    startX = touchX;
                    startY = touchY;
                    endX = touchX;
                    endY = touchY;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDrawing) {
                    drawPath.lineTo(touchX, touchY);
                } else if (isErasing) {
                    erasePath(touchX, touchY);
                } else if (isDrawingSquare || isDrawingCircle) {
                    endX = touchX;
                    endY = touchY;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDrawing && !drawPath.isEmpty()) {
                    canvas.drawPath(drawPath, drawPaint);
                } else if (isDrawingSquare) {
                    endX = touchX;
                    endY = touchY;
                    canvas.drawRect(startX, startY, endX, endY, drawPaint);
                } else if (isDrawingCircle) {
                    endX = touchX;
                    endY = touchY;
                    float radius = (float) Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2)) / 2;
                    float centerX = (startX + endX) / 2;
                    float centerY = (startY + endY) / 2;
                    canvas.drawCircle(centerX, centerY, radius, drawPaint);
                }
                drawPath.reset();
                invalidate();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    private void erasePath(float x, float y) {
        float eraseRadius = 30;
        Paint erasePaint = new Paint();
        erasePaint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, eraseRadius, erasePaint);
        invalidate();
    }



    public void setColor(int color) {
        currentColor = color;
        drawPaint.setColor(currentColor);
    }


    public void clearDrawing() {
        canvas.drawColor(Color.WHITE);
        invalidate();
    }

    public void toggleFillMode() {
        isFilling = !isFilling;
        if(isFilling) {
            isDrawing=false;
            isErasing=false;
            isDrawingSquare=false;
            isDrawingCircle=false;
        }
        else isDrawing=true;
    }
    public void toggleDrawingMode() {
        isErasing = !isErasing;
        if(isErasing){
            isDrawing=false;
            isFilling=false;
            isDrawingSquare=false;
            isDrawingCircle=false;
        }
        else isDrawing=true;
    }

    public void setThickness(float thickness) {
        currentThickness = thickness;
        drawPaint.setStrokeWidth(thickness);
    }

    public boolean isFilling() {
        return isFilling;
    }

    public boolean isErasing() {
        return isErasing;
    }

    public boolean isDrawing() {
        return isDrawing;
    }

    public void toggleSquareDrawingMode() {
        isDrawingSquare = !isDrawingSquare;
        if (isDrawingSquare) {
            isDrawing = false;
            isErasing = false;
            isFilling = false;
            isDrawingCircle=false;
        } else {
            isDrawing = true;
        }
    }

    public boolean isDrawingSquare() {
        return isDrawingSquare;
    }

    public void toggleCircleDrawingMode() {
        isDrawingCircle = !isDrawingCircle;
        if (isDrawingCircle) {
            isDrawing = false;
            isErasing = false;
            isFilling = false;
            isDrawingSquare = false;
        } else {
            isDrawing = true;
        }
    }

    public boolean isDrawingCircle() {
        return isDrawingCircle;
    }

    private void fillArea(int x, int y) {
        int targetColor = bitmap.getPixel(x, y);
        if (targetColor != currentColor) {
            floodFill(x, y, targetColor, currentColor);
        }
    }
    private void floodFill(int x, int y, int targetColor, int replacementColor) {
        if (targetColor == replacementColor) return;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point point = queue.poll();
            int px = point.x;
            int py = point.y;

            if (px < 0 || px >= width || py < 0 || py >= height) continue;
            if (pixels[py * width + px] != targetColor) continue;

            pixels[py * width + px] = replacementColor;
            queue.add(new Point(px + 1, py));
            queue.add(new Point(px - 1, py));
            queue.add(new Point(px, py + 1));
            queue.add(new Point(px, py - 1));
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        invalidate();
    }

    private class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}


