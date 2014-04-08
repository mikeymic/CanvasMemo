package com.example.canvasmemo0_0_6.activity.listener;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

/**
 * 　ローパスとハイパスについて（例） ・ローパス： 　ー　計算方法 ・センサーの中央値 ・直近の値を少しずつ取得　（
 * 
 * ・ハイパス：直近の値 センサー値ーローパスフィルターの値
 * 
 **/

public class AcceleroListener implements SensorEventListener {

	private static float DETECT_ACCELERO = 11.0f;
	private static int DETECT_ACCELERO_COUNT = 3;

	private SensorManager sensorManager;
	private Context context;
	private OnAcceleroListener acceleroListener;

	private int counter;

	public AcceleroListener(Context context) {
		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		this.context = context;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int id) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		/** センサー検知値変更イベント **/

		/* 加速度センサーを検知しなかった場合 */
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
			return; // 処理をしない
		}

		/* 加速度センサーを検知した場合 */

		/*
		 * 画面の真ん中が原点 符号は必要ないので、絶対値で値を取得する
		 */
		float x = Math.abs(event.values[0]);
		float y = Math.abs(event.values[1]);
		float z = Math.abs(event.values[2]);

		// 加速度が所定値より大きいか判断
		if (x > DETECT_ACCELERO || y > DETECT_ACCELERO || z > DETECT_ACCELERO) {
			// 加速度の変化をカウント
			counter++;
			if (counter > DETECT_ACCELERO_COUNT) {// 3回カウントされたら（x,y,z）
				Toast.makeText(
						context,
						"加速度検知\n" + "X = " + String.valueOf(x) + "\n" + "Y = "
								+ String.valueOf(y) + "\n" + "Z = "
								+ String.valueOf(z) + "\n", Toast.LENGTH_SHORT)
								.show();
				if (acceleroListener != null) {
					acceleroListener.onAccelero();
				}
			}
		} else {
			counter = 0; // 一回でも加速度が小さい場合はリセット
		}
	}

	public void setOnAccelaroListener(OnAcceleroListener listener) {
		acceleroListener = listener;
	}

	/* 加速度検知リスナーの定義 */
	public interface OnAcceleroListener {
		void onAccelero(); // 加速度検知の通知メソッド
	}

	// activityの同メソッドから呼び出して、Activityのライフサイクルに組み込む
	protected void onResume() {
		List<Sensor> list = sensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);// 加速度センサーを取得
		// 加速度センサーが一つもない場合は何もしない
		if (list.size() < 1) {
			return;
		}
		sensorManager.registerListener(this, list.get(0),
				SensorManager.SENSOR_DELAY_UI);// 加速度センサーの反応制度
	}

	// activityの同メソッドから呼び出して、Activityのライフサイクルに組み込む
	protected void onPause() {
		sensorManager.unregisterListener(this);// リスナー解除　（処理低下、消費電力増加の防止のため）
	}
}
