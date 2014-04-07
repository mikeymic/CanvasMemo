package com.example.canvasmemo0_0_6.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class DatabaseDao {


	private static final String TABLE_NAME = DatabaseHelper.TABLE_NAME;
	private static final String ID = DatabaseHelper.CLM_ID;
	private static final String NAME = DatabaseHelper.CLM_NAME;
	public static final String IMAGE = DatabaseHelper.CLM_IMAGE;
	private static final String DATE = DatabaseHelper.CLM_DATE;

	private SQLiteDatabase db;
	private static final String[] COLUMNS = {ID, NAME, IMAGE, DATE};


	public DatabaseDao (SQLiteDatabase db) {
		this.db = db;
	}

	/*--------------------<<<ページ操作> >> -------------------*/

	//ページ数取得
	public int getPageCount(){

		Cursor cursor = db.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
		return cursor.getCount();
	}

	//新規作成
	public long insertNewPage() {

		return db.insert(TABLE_NAME, null, null);
	}

	//新規保存
	public long insertImages(byte[] stream) {

		ContentValues values = new ContentValues();
		values.put(IMAGE, stream);
		return db.insert(TABLE_NAME, null, values);
	}

	//上書き保存
	public long updatePage(byte[] stream, int index) {

		String where = ID + " = " + String.valueOf(index);
		ContentValues values = new ContentValues();
		values.put(IMAGE, stream);
		return db.update(TABLE_NAME, values, where, null);
	}

	//読み込み
	public byte[] getImageOfPage(int index){

		String where = ID + "= " + String.valueOf(index);
		Cursor cursor = db.query(TABLE_NAME, COLUMNS, where, null, null, null, null);

		byte[] stream = null;
		while (cursor.moveToNext() != false) {
			stream = cursor.getBlob(cursor.getColumnIndex(IMAGE));
		}
		return stream;
	}

	//削除
	public void deletePage(int index) {
		String where = ID + " = " + String.valueOf(index) ;
		db.delete(TABLE_NAME, where, null);
	}





}
