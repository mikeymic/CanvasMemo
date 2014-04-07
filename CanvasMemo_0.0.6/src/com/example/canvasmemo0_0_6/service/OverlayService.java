package com.example.canvasmemo0_0_6.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class OverlayService extends Service {


	private WindowManager wm;

//	private WindowManager.LayoutParams params;
	private  View view;
	private  Bitmap bmp;

	public static final String streamName = "OverlayImage";
	public static final String streamHeight = "Overlayheight";


	// オーバーレイ用のBitmapを格納するViewの表示設定
//	private static final WindowManager.LayoutParams setLayoutParam () {
//		final int MP = WindowManager.LayoutParams.MATCH_PARENT; //幅を親に合わせる
//		final int LAYER = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY; //システムアラートウィンドウを使う
//		final int FLG = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH; //このViewに対してonTouchEventを検知しない
//		final int FORMAT = PixelFormat.TRANSLUCENT;
//		final WindowManager.LayoutParams params =new WindowManager.LayoutParams(
//				MP, MP,LAYER, FLG, FORMAT);
//		return params;
//	}

	/*
	 * (非 Javadoc)
	 *
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		view = new View(this); //Bitmapを格納するViewの生成

		//デシリアライズ
		byte[] data = intent.getByteArrayExtra(OverlayService.streamName); //インテントから送られてきたバッファを取得
		bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
		final int height = intent.getIntExtra(streamHeight, 0);
//-------------------- << <速度テスト中> >> -------------------

		//BitmapをViewにセット ver 16 から
//		view.setBackground(new BitmapDrawable(getResources(), bmp));
		//Canvasに書き出してからコピー
		Canvas canvas = new Canvas();


		view = new View(this) {

			/* (非 Javadoc)
			 * @see android.view.View#onDraw(android.graphics.Canvas)
			 */
			@Override
			protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);
				canvas.drawBitmap(bmp, 0, height, null);
			}

		};

//-------------------- << <システムレイヤーにオーバーレイ用のビューをセット> >> -------------------
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); // WindowManager[画像レイヤー]を取得する


		int MP = WindowManager.LayoutParams.MATCH_PARENT; //幅を親に合わせる
		int LAYER = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY; //システムアラートウィンドウを使う
		int FLG = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH; //このViewに対してonTouchEventを検知しない
		int FORMAT = PixelFormat.TRANSLUCENT;
		WindowManager.LayoutParams params =new WindowManager.LayoutParams(
				MP, MP,LAYER, FLG, FORMAT);

		wm.addView(view, params); //Viewを警告画面レイヤー上に重ね合わせする

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "オーバーレイを開始しました", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		wm.removeView(view); // サービスが破棄されるときには重ね合わせしていたViewを削除する
		view = null;
		bmp.recycle();
		Toast.makeText(this, "オーバーレイを終了しました", Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}