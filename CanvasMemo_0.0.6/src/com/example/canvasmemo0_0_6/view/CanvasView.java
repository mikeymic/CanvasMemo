package com.example.canvasmemo0_0_6.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View {

	private Bitmap bitmap;
	private Path line;
	private int clearColor;
	private Paint paint;
	private boolean drawingEnd = false;

	/*--------------------<<<コンストラクタ> >> -------------------*/
	public CanvasView(Context context) {
		super(context);
		setPaint();
	}

	public CanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPaint();

	}

	public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setPaint();
	}


	/*--------------------<<<ペイントセット> >> -------------------*/
	private void setPaint() {
		if (paint == null) {
			paint = new Paint();
		}
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setStrokeCap(Cap.ROUND);
		paint.setStrokeJoin(Join.ROUND);
		paint.setStrokeWidth(5);
		paint.setColor(Color.RED);

	}

	/*--------------------<<<ゲッター／セッター> >> -------------------*/
	/**
	 * @return bitmap
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * @param bitmap
	 *            セットする bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	/**
	 * @param line
	 *            セットする line
	 */
	public void setLine(Path line) {
		this.line = line;
	}



	/*--------------------<<<描画> >> -------------------*/

	/*
	 * (非 Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);


		if (bitmap == null && line == null) {
			canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		}

		if (bitmap != null && drawingEnd) {
			canvas.drawBitmap(bitmap, 0, 0, null);
		}

		if (line != null) {
			canvas.drawPath(line, paint);
		}

	}

	float sx = 0;
	float sy = 0;

	/*--------------------<<<タッチイベント> >> -------------------*/

	int downCounter = 0;
	/*
	 * (非 Javadoc)
	 *
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();

		if (true/*メニューバーが表示されていたら*/) {
			//メニューバーを非表示
		}


		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
				sx = x;
				sy = y;
				downCounter++;
			break;
		case MotionEvent.ACTION_MOVE:
			if (Math.abs(x - sx) < 20.0f || Math.abs(y - sy) < 20.0f) {

				if (downCounter == 2) {

					//メニューボタンを表示する
				}

				break;
			} else {
				downCounter = 0;
				if (line == null) {
					line = new Path();
					line.moveTo(sx, sy);
				} else {
					line.lineTo(x, y);
				}
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
			if (line == null) {
				break;
			}
				line.lineTo(x, y);
				invalidate();
			drawingEnd = true;
			 setDrawingCacheEnabled(true);
			 bitmap = Bitmap.createBitmap(getDrawingCache());
			 setDrawingCacheEnabled(false);
			 invalidate();
			 line = null;
			break;
		}
		return true;
	}

}
