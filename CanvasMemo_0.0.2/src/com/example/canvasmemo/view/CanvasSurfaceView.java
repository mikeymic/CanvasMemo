package com.example.canvasmemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CanvasSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

// タッチイベント用
	private int sx; //タッチ時の初期のX座標
	private int sy; //タッチ時の初期のX座標
	private boolean isFirstTime = true; //線の書き込み初回フラグ

	// 描画
	private Line aLine; //PointのArrayListから出来ている線
	private Paint paint; //線のスタイル

	private Thread drawingThread; //描画スレッド
	private SurfaceHolder holder; //SurfaceviewのCanvas情報を保持
	private Canvas canvas; //SurfaceViewの描画に使うCanvas
	private Bitmap bitmap = null; // SurfaceViewに書き込むbitmap


	/*--------------------<<<コンストラクタ> >> -------------------*/

	public CanvasSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder = getHolder();//ホルダーの取得
		holder.setFormat(PixelFormat.TRANSLUCENT);//透明色で塗りつぶす !!これが無いと透過しないので注意
		holder.addCallback(this); //SurfaceViewの状態遷移をこのクラスの処理メソッドに通知
	}

	/*--------------------<<<セッター／ゲッターの追加> >> -------------------*/

	/**
	 * @return bitmap
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * @param bitmap セットする bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	/*--------------------<<<SurfaceViewの処理> >> -------------------*/
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
		setPaint();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
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
		paint.setStrokeWidth(4);
		paint.setColor(Color.RED);

	}

	/*--------------------<<<描画スレッド> >> -------------------*/
	@Override
	public void run() {

//		 線が無い、もしくはPointが２つ以下の場合はなにもしない
		if (aLine.getPoints() == null & (aLine.getPoints().size() < 2)) {
			return;
		}

		Canvas c = new Canvas(bitmap); //bitmapに書き込むためのCanvasを設定

//		Pointを２つずつ使い、canvasに線を描く
		for (int i = 0; i < (aLine.getPoints().size() - 1); i++) {
			Point s = aLine.getPoints().get(i); //前の点取得
			Point e = aLine.getPoints().get(i + 1); //後の点取得
			c.drawLine(s.x, s.y, e.x, e.y, paint); //canvasに線を描く
		}

//		SurfaceViewに描画
		canvas = holder.lockCanvas(); //SurfaceViewのCanvasをロック
		canvas.drawBitmap(bitmap, 0, 0, null); //SurfaceViewのCanvasにbitmapの中身を描画
		holder.unlockCanvasAndPost(canvas); //ロックしていたCanvasをアンロック

	}

	float sizef;
	int size;
	/*--------------------<<<タッチイベント> >> -------------------*/
	/*
	 * (非 Javadoc)
	 *
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int x = (int) event.getX(); //タッチした場所のX座標を取得
		int y = (int) event.getY(); //タッチした場所のY座標を取得

		switch (event.getAction()) {

		/*-----押下時-----*/
		case MotionEvent.ACTION_DOWN:
			sx = x; //初期のX座標
			sy = y; //初期のY座標

			break;

			/*-----移動時-----*/
		case MotionEvent.ACTION_MOVE:
			// ブレ防止 [座標のズレが範囲内なら何もしない]
			if (Math.abs(x - sx) < 1.0f || Math.abs(y - sy) < 1.0f) {
				return false;
			}
			// 初回移動
			if (isFirstTime) {
				aLine = new Line(); //PointとPointのArrayListを持つクラス
				aLine.addPoint(new Point(sx, sy));//タッチした初期の場所を追加
				isFirstTime = !isFirstTime; //初回フラグの無効
			}
			// 初回移動以降
			aLine.addPoint(new Point(x, y)); // 点を線に追加

			// 描画スレッド起動
			drawingThread = new Thread(this); //新しくスレッドをインスタンス化
			drawingThread.start(); //スレッドの開始
			break;

			/*-----終了時----*/
		case MotionEvent.ACTION_UP:
			isFirstTime = !isFirstTime; //初回フラグの設定
			break;
		}
		return true;
	}

}
