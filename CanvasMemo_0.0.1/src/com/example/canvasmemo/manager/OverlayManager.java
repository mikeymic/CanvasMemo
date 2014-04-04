package com.example.canvasmemo.manager;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

 /**
  * @author takashi
 *
 **/
public class OverlayManager {

	public static final String overlayName = "OverLayBitmap";

	private static Bitmap overlayBitmap;
	private static Point hardwareSize;
	private static int overlayWidth;
	private static int overlayHeight;
	private static int actionBarHeight;
	private static int statusBarHeight;
	private static float viewTop;
	private static float viewLeft;


	/**
	 * Window, View ActionBar, StatusBarのサイズを取得し、Overlay用のBitmapを作成する<p>
	 * @param context
	 */
	public static void onUpdateMeasureSize(Activity activity){

		//actionBar
		if (activity.getActionBar() == null) {
			actionBarHeight = 32;
		}
		else {
			actionBarHeight = activity.getActionBar().getHeight();
		}

		//hardwareSize
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		int x = displaymetrics.widthPixels;
		int y = displaymetrics.heightPixels;

	    hardwareSize = new Point(x, y);


	    //statusBar
		Rect rectgle = new Rect();
		 activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectgle);
		statusBarHeight = rectgle.top;


		//overlaysize
		overlayHeight = hardwareSize.y -  statusBarHeight;
		overlayWidth = hardwareSize.x;

		//overlay bitmap
		overlayBitmap = Bitmap.createBitmap(overlayWidth, overlayHeight, Config.ARGB_8888);


	}


	//	public static void onUpdateMeasureSize(Context context){
//		actionBarHeight = ((Activity) context).getActionBar().getHeight();
//		onMeasureHardwareSize(context);
//		onMeasureStatusBarHeight(context);
//		createOverlay();
//	}

	/**
	 * ウィンドウサイズを取得
	 * @param context ウィンドウサイズを取得したいActivityを指定
	 * @return Point ウィンドウサイズ(x,y)を保持したPointを返す
	 */
	private static void onMeasureHardwareSize(Context context) {

		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		int x = displaymetrics.widthPixels;
		int y = displaymetrics.heightPixels;

	    hardwareSize = new Point(x, y);
	}

	/**
	 * キャプチャするViewのあるActivityのステータスバーの高さを取得
	 * @param context キャプチャするViewのあるActivityのContextを指定
	 */
	private static void onMeasureStatusBarHeight(Context context) {
		Rect rectgle = new Rect();
		 ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rectgle);
		statusBarHeight = rectgle.top;
	}

	/**
	 * キャプチャするViewの描画位置を取得
	 * @param top キャプチャするViewのtopの値を指定
	 * @param left キャプチャするViewのleftの値を指定
	 */
	private static void onMeasureViewSize(float top, float left) {
		viewTop = top + actionBarHeight;
		viewLeft = left;
	}

	/**
	 * キャプチャするViewの描画位置を指定する
	 * @param view キャプチャするViewを指定
	 */
	private static void onMeasureViewSize(View view) {
		viewTop = view.getTop() + actionBarHeight;
		viewLeft = view.getLeft();
	}

	/**
	 * オーバーレイ用のBitmapの描画領域を指定する
	 */
	private static void onMeasureOverlaySize() {
		overlayHeight = hardwareSize.y -  statusBarHeight;
		overlayWidth = hardwareSize.x;
	}

	/**
	 * オーバーレイ用のBitmapを作成。<p>
	 * 高さはウィンドウサイズからステータスバーの高さを引いたサイズを、
	 * 幅はウィンドウサイズを指定<p>
	 * この時点では何も書かれていないので注意<p>
	 */
	private static void createOverlay(){
		onMeasureOverlaySize();
		overlayBitmap = Bitmap.createBitmap(overlayWidth, overlayHeight, Config.ARGB_8888);
	}

	/**
	 * オーバーレイ用のBitmapを作成。<p>
	 * 高さと幅は自分で指定する<p>
	 * この時点では何も書かれていないので注意<p>
	 * @param width bitmapの横幅
	 * @param height bitmapの縦幅
	 */
	public static void createOverlay(int width, int height){
		overlayBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	}

	/**
	 * Viewの描画キャッシュを取得（スクリーンショット）
	 * @param view キャプチャしたいViewを指定する
	 * @return 画像キャッシュから生成したBitmapを返す
	 */
	public static Bitmap takeCapture(View view) {
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		return cache;
		}

	/**
	 * オーバーレイ用のBitmapにスクリーンしたBitmapを追加、合成する。
	 * 合成したBitmapはーーーーで取得
	 * @param bitmap 合成したいBitmap
	 * @param top Bitmapの横の配置位置
	 * @param left Bitmapの縦の配置位置
	 */
	public static void addViewToOverlay(Bitmap bitmap, float top, float left) {
		onMeasureViewSize(top, left);
		Canvas canvas = new Canvas(overlayBitmap);
		canvas.drawBitmap(bitmap, viewLeft, viewTop, null);
	}

	/**
	 * 対象のViewのスクリーンショットを取り、
	 * オーバーレイ用のBitmapにキャプチャしたBitmapを追加、合成する。<p>
	 * 合成したBitmapはーーーーで取得<p>
	 * @param view キャプチャするView。描画開始位置を取得するためにも使用する<p>
	 */
	public static void takeCaptureThenAddToOverlay(View view) {
//		onMeasureViewSize(view);
//		Canvas canvas = new Canvas(overlayBitmap);
//		canvas.drawBitmap(takeCapture(view), viewLeft, viewTop, null);

		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
//		int viewTop = view.getTop() + actionBarHeight ;
		int viewTop = view.getTop() + actionBarHeight ;
		int viewLeft = view.getLeft();
		Canvas canvas = new Canvas(overlayBitmap);
		canvas.drawBitmap(cache, viewLeft, viewTop, null);
		cache.recycle();
	}

	/**
	 * Bitmapをバッファに変換する
	 * @return Byte[] Bitmapを変換したバッファを返す
	 * @serialData オーバレイ用のBitmapをByteArrayOutputStreamを使いByte[]に変換する
	 */
	public static byte[] createOverlayBuffer() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		overlayBitmap.compress(CompressFormat.PNG, 100, bos);
		return bos.toByteArray();
	}
	
	public static byte[] createOverlayBuffer(Bitmap bitmap) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bos);
		return bos.toByteArray();
	}



}
