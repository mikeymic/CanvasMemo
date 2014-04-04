package com.example.canvasmemo.service;

import com.example.canvasmemo.manager.OverlayManager;
import com.example.canvasmemo.view.CanvasView;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.Toast;

public class LayerService extends Service {

	static CanvasView view;

	WindowManager wm;

	private Bitmap bmp;

	/*
	 * (非 Javadoc)
	 *
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		WindowManager.LayoutParams params = new WindowManager.LayoutParams( // オーバーレイ用のBitmapを格納するViewの表示設定
				WindowManager.LayoutParams.MATCH_PARENT, //横幅を親に合わせる
				WindowManager.LayoutParams.MATCH_PARENT, //縦幅を親に合わせる
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, //システムアラートウィンドウを使う
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, //このViewに対してonTouchEventを検知しない
				PixelFormat.TRANSLUCENT);
		view = new CanvasView(this); //Bitmapを格納するViewの生成

		byte[] data = intent.getByteArrayExtra(OverlayManager.overlayName); //インテントから送られてきたバッファを取得
		bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
		view.setBackground(new BitmapDrawable(getResources(), bmp)); //BitmapをViewにセット

		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); // WindowManager[画像レイヤー]を取得する
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