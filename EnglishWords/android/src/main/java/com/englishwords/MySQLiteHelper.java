package com.englishwords;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_WORDS = "words";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ENG = "english";
	public static final String COLUMN_RUS = "russian";
	public static final String COLUMN_LEV = "level";
	public static final String COLUMN_KNOWLG = "knowledge";

	private static final String DATABASE_NAME = "commments.db";
	private static final int DATABASE_VERSION = 2;

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_WORDS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_ENG
			+ " text not null, " + COLUMN_RUS
			+ " text not null, " + COLUMN_LEV
			+ " int not null, " + COLUMN_KNOWLG
			+ " int not null);";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
		onCreate(db);
		/*if (newVersion > oldVersion) {
	        db.execSQL("ALTER TABLE " + TABLE_WORDS + " ADD COLUMN " + COLUMN_KNOWLG + " INTEGER DEFAULT 1");
	    }*/
	}



}
