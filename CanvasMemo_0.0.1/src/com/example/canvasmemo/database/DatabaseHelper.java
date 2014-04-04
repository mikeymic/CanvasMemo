package com.example.canvasmemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "MemoPages.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "Page";


	//コンテンツプロバイダは使う予定がないので最初のカラムに（＿）はつけない
	public static final String CLM_ID = "id";
	public static final String CLM_NAME = "name";
//	public static final String CLM_TEXTS = "texts";
//	public static final String CLM_IMAGES = "images";
	public static final String CLM_IMAGE = "image";

	private static final String CREATE_DATABASE_TABLE =
			"CREATE TABLE " + TABLE_NAME +" ("
	+ CLM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
	+ CLM_NAME + " TEXT, "
//	+ CLM_TEXTS + " BLOB, "
//	+ CLM_IMAGES + " BLOB, "
	+ CLM_IMAGE + " BLOB "
	+	");";

	private static final String DROP_TABLE =
			"DROP TABLE IF EXIST" + TABLE_NAME + ";";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DATABASE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE);
	}

}
