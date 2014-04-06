package com.example.canvasmemo;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.canvasmemo.database.DatabaseDao;
import com.example.canvasmemo.database.DatabaseHelper;
import com.example.canvasmemo.service.OverlayService;
import com.example.canvasmemo.view.CanvasSurfaceView;

public class CanvasMemoActivity extends Activity {

	private CanvasSurfaceView canvas;

	private int pageCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_canvas_memo);

		//ビュー取得
		canvas = (CanvasSurfaceView) findViewById(R.id.canvasView); //XmlからSurfaceViwを取得

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.canvas_memo, menu);
		return true;
	}

	boolean overlayFlg =false;
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
//		---------------------------------------------------------------------------
		Rect rect = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

		int displayHeight = rect.bottom;
		int statusBarHeight = rect.top;
		int viewHeight = canvas.getHeight();

		int viewTop = displayHeight - viewHeight - statusBarHeight;
//		---------------------------------------------------------------------------


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

		DatabaseDao dao = new DatabaseDao(OpenReadableDatabase());
		pageCount = dao.getPageCount();
	}

	/**
	 * 新規作成
	 */
	private void createNewPage() {
		DatabaseDao dao = new DatabaseDao(OpenWritableDatabase());
		dao.insertNewPage();
	}

	/**
	 * 上書き保存
	 * @param index
	 */
	private void updatePage(int index) {
		DatabaseDao dao = new DatabaseDao(OpenWritableDatabase());
		dao.updatePage(serializeImage(), index);
	}

	/**
	 * 読み込み
	 */
	private void loadPage(int index) {
		DatabaseDao dao = new DatabaseDao(OpenReadableDatabase());
		dao.getImageOfPage(index);
	}

	/**
	 * 削除
	 * @param index
	 */
	private void deletePage(int index) {
		DatabaseDao dao = new DatabaseDao(OpenWritableDatabase());
		dao.deletePage(index);
	}


	/*--------------------<<<データベース読み込み> >> -------------------*/

	/**
	 * 書き込みモード
	 * @return
	 */
 	private SQLiteDatabase OpenWritableDatabase() {
		DatabaseHelper helper = new DatabaseHelper(CanvasMemoActivity.this);
		SQLiteDatabase db = helper.getWritableDatabase();
		return db;
	}

	/**
	 * 読み込みモード
	 * @return
	 */
	private SQLiteDatabase OpenReadableDatabase() {
		DatabaseHelper helper = new DatabaseHelper(CanvasMemoActivity.this);
		SQLiteDatabase db = helper.getReadableDatabase();
		return db;
	}




}
