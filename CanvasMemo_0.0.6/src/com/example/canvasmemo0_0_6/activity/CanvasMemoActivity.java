package com.example.canvasmemo0_0_6.activity;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.canvasmemo0_0_6.R;
import com.example.canvasmemo0_0_6.database.DatabaseDao;
import com.example.canvasmemo0_0_6.database.DatabaseHelper;
import com.example.canvasmemo0_0_6.service.OverlayService;
import com.example.canvasmemo0_0_6.view.CanvasSurfaceView;
import com.example.canvasmemo0_0_6.view.util.Util;

public class CanvasMemoActivity extends Activity {

	private CanvasSurfaceView canvas;

	private int pageCount;

	boolean overlayFlg =false;


	private GestureDetector gestureDetector;
	private ScaleGestureDetector scaleGestureDetector;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_canvas_memo);


		/*--------------------<<<ActionBarの設定> >> -------------------*/
		Util.actionBarUpsideDown(this); //ActionBarを下に表示する
		Util.actionBarSetVisiblity(this, View.GONE); //ActionBarを非表示

		View root = getWindow().getDecorView();
		List<View> views = Util.findViewsWithClassName(root, "com.android.internal.widget.ActionBarContainer");

		//ビュー取得
		canvas = (CanvasSurfaceView) findViewById(R.id.canvasView); //XmlからSurfaceViwを取得

	    GestureListener listener = new GestureListener(this, canvas);


	    gestureDetector = new GestureDetector(canvas.getContext(), listener);
	    scaleGestureDetector = new ScaleGestureDetector(canvas.getContext(), listener);
	    canvas.setOnTouchListener(onTouchView );


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.canvas_memo, menu);
		return true;
	}








	/* (非 Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_page_new:

			break;
		case R.id.action_page_delete:

			break;
		case R.id.action_page_previous:

			break;
		case R.id.action_page_next:

			break;
		case R.id.action_overlay_start_stop:
			overlayFlg = !overlayFlg;
//			MenuItem menuItem = (MenuItem) findViewById(R.id.action_overlay_start_stop);
			if (overlayFlg) {
				startOverlayService();
//				menuItem.setTitle(R.string.action_overlay_stop);
			} else {
				stopOverlayService();
//				menuItem.setTitle(R.string.action_overlay_start);
			}

			break;

		default:
			break;
		}
		return true;
	}

	/*--------------------<<<ファイル出力> >> -------------------*/
	/**
	 * オーバーレイ出力
	 */
	private void startOverlayService() {

		//オーバーレイのズレ防止 (マージンの取得) !!!一番重要なところ。絶対いじらないこと
//		---------------------------------------------------------------------------	//
		Rect rect = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

		int displayHeight = rect.bottom;
		int statusBarHeight = rect.top;
		int viewHeight = canvas.getHeight();

		int viewTop = displayHeight - viewHeight - statusBarHeight;
//		---------------------------------------------------------------------------	//


		Intent intent = new Intent(this, OverlayService.class);
		intent.putExtra(OverlayService.streamName, serializeImage());
		intent.putExtra(OverlayService.streamHeight, viewTop);
		startService(intent);
	}

	private void stopOverlayService() {
		stopService(new Intent(CanvasMemoActivity.this, OverlayService.class));
	}

	/*--------------------<<<ストリーム変換> >> -------------------*/

	/**
	 * シリアライズ
	 * @return
	 */
	private byte[] serializeImage() {
		ByteArrayOutputStream outFile = new ByteArrayOutputStream();
		canvas.getBitmap().compress(CompressFormat.PNG, 100, outFile);
		return outFile.toByteArray();
	}

	/*--------------------<<<ページ操作> >> -------------------*/

	/**
	 *ページ数取得
	 */
	private void getPageCount() {

		DatabaseDao dao = new DatabaseDao(openReadMode());
		pageCount = dao.getPageCount();
	}

	/**
	 * ページの新規作成
	 */
	private void createNewPage() {
		DatabaseDao dao = new DatabaseDao(openWriteMode());
		dao.insertNewPage();
	}

	/**
	 * ページの上書き保存
	 * @param index
	 */
	private void updatePage(int index) {
		DatabaseDao dao = new DatabaseDao(openWriteMode());
		dao.updatePage(serializeImage(), index);
	}

	/**
	 * ページの読み込み
	 */
	private void loadPage(int index) {
		DatabaseDao dao = new DatabaseDao(openReadMode());
		dao.getImageOfPage(index);
	}

	/**
	 * ページの削除
	 * @param index
	 */
	private void deletePage(int index) {
		DatabaseDao dao = new DatabaseDao(openWriteMode());
		dao.deletePage(index);
	}


	/*--------------------<<<データベース読み込み> >> -------------------*/

	/**
	 * 書き込みモード
	 * @return
	 */
 	private SQLiteDatabase openWriteMode() {
		DatabaseHelper helper = new DatabaseHelper(CanvasMemoActivity.this);
		SQLiteDatabase db = helper.getWritableDatabase();
		return db;
	}

	/**
	 * 読み込みモード
	 * @return
	 */
	private SQLiteDatabase openReadMode() {
		DatabaseHelper helper = new DatabaseHelper(CanvasMemoActivity.this);
		SQLiteDatabase db = helper.getReadableDatabase();
		return db;
	}


	/*--------------------<<<タッチイベント読み込み> >> -------------------*/

	private OnTouchListener onTouchView = new OnTouchListener() {
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	    	return event.getPointerCount() == 1 ? gestureDetector.onTouchEvent(event) : scaleGestureDetector.onTouchEvent(event);
			/* Pointerの数が1つの時はGestureDetectorにイベントを委譲し
			 * Pointerの数が２つの時はScaleGestureDetectorにイベントを委譲する
			 * タッチ後の処理はリスナーを参照
			 */
	    }
	};







}
