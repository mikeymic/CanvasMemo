package com.example.canvasmemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View {

	private Paint paint;
	private Bitmap bitmap;
	private Path path;

	private float startX;
	private float startY;

	public static class Painter {

		private static final int defaultColor = Color.BLACK;
		private static final int defaultWidth = 6;

		private static final int clearColor = Color.TRANSPARENT;

		public static Paint createDefaultPaint(){
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStyle(Style.STROKE);
			paint.setStrokeCap(Cap.ROUND);
			paint.setStrokeJoin(Join.ROUND);
			paint.setStrokeWidth(defaultWidth);
			paint.setColor(defaultColor);
			return paint;
		}

	}

	public CanvasView(Context context) {
		super(context);
		paint = Painter.createDefaultPaint();
	}
	public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
		paint = Painter.createDefaultPaint();
	}
	public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		paint = Painter.createDefaultPaint();
	}


	
	
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = Bitmap.createBitmap(bitmap);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.argb(0, 255, 255, 255));

		if (bitmap != null) {
			canvas.drawBitmap(bitmap, 0, 0, null);
		}
		if (path != null) {
			canvas.drawPath(path, paint);
		}
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			startY = event.getY();
			path = new Path();
			path.moveTo(startX, startY);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			float areaX = Math.abs(x - startX);
			float areaY = Math.abs(y - startY);
			if (areaX < 1.0f || areaY < 1.0f) {
				break;
			}

			path.lineTo(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			path.lineTo(x, y);
			invalidate();

			setDrawingCacheEnabled(true);
			bitmap = Bitmap.createBitmap(getDrawingCache());
			setDrawingCacheEnabled(false);
			break;

		}

		return true;
	}








}
