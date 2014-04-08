package com.example.canvasmemo0_0_6.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.canvasmemo0_0_6.activity.CanvasMemoActivity;
import com.example.canvasmemo0_0_6.activity.listener.AcceleroListener;
import com.example.canvasmemo0_0_6.activity.listener.AcceleroListener.OnAcceleroListener;

public class IntentActivityService extends Service {
	
	private static boolean flg;
	

	public static void setFlg(boolean flg) {
		IntentActivityService.flg = flg;
	}
	


	private AcceleroListener acceleroListener;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	
    /*--------------------<<<センサーのリスナーの設定> >> -------------------*/
	acceleroListener = new AcceleroListener(getApplicationContext());
	acceleroListener.setOnAccelaroListener(OnDetectAccelero);

	Toast.makeText(this, "サービスを開始しました", Toast.LENGTH_SHORT).show();
	
	return  START_STICKY;
	}
	

	/*--------------------<<<加速度検知時の処理> >> -------------------*/
	private OnAcceleroListener OnDetectAccelero = new OnAcceleroListener() {
		@Override
		public void onAccelero() {
				Intent intent = new Intent(IntentActivityService.this, CanvasMemoActivity.class);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setAction((Intent.ACTION_MAIN));
				startActivity(intent);
			
			
		}
	};

	@Override
	public void onCreate() {
		Toast.makeText(this, "サービスを開始しました", Toast.LENGTH_SHORT).show();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "サービスを停止しました", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}


	
	

}
