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
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isErasing) {
                    drawPath.moveTo(touchX, touchY);
                    startX = touchX;
                    startY = touchY;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isErasing) {
                    drawPath.lineTo(touchX, touchY);
                } else {
                    erasePath(touchX, touchY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isErasing && !drawPath.isEmpty()) {
                    canvas.drawPath(drawPath, drawPaint);
                }
                drawPath.reset();
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


    public void toggleDrawingMode() {
        isErasing = !isErasing;
    }

    public void setThickness(float thickness) {
        currentThickness = thickness;
        drawPaint.setStrokeWidth(thickness);
    }

    public boolean isErasing() {
        return isErasing;
    }
}


