package com.englishwords;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WordsDataSource {
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_ENG,
			MySQLiteHelper.COLUMN_RUS,
			MySQLiteHelper.COLUMN_LEV,
			MySQLiteHelper.COLUMN_KNOWLG };

	public WordsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void createWord(String english, String russian, long level, long knowledge) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ENG, english);
		values.put(MySQLiteHelper.COLUMN_RUS, russian);
		values.put(MySQLiteHelper.COLUMN_LEV, level);
		values.put(MySQLiteHelper.COLUMN_KNOWLG, knowledge);
		long insertId = database.insert(MySQLiteHelper.TABLE_WORDS, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_WORDS,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		cursor.close();
	}

	long getMinLevel() {
		long minLevel = 0;
		Cursor cursor = database.query(MySQLiteHelper.TABLE_WORDS, new String [] {"MIN(" + MySQLiteHelper.COLUMN_LEV + ")"}, null, null, null, null, null);
		cursor.moveToFirst();
		minLevel = cursor.getLong(0);
		cursor.close();
		return minLevel;
	}

	public void changeLevel(Word word, long level) {
		long id = word.getId();
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_LEV, level);
		database.update(MySQLiteHelper.TABLE_WORDS, values, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	public void updateWord(Word word, String eng, String rus) {
		long id = word.getId();
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ENG, eng);
		values.put(MySQLiteHelper.COLUMN_RUS, rus);
		database.update(MySQLiteHelper.TABLE_WORDS, values, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	public void changeKnowledge(Word word, long knowledge) {
		long id = word.getId();
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_KNOWLG, knowledge);
		database.update(MySQLiteHelper.TABLE_WORDS, values, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	public int countZeroKnowledge() {
		String selection = MySQLiteHelper.COLUMN_KNOWLG + "= ?";
		String[] selectionArg = {"0"};
		Cursor c = database.query(MySQLiteHelper.TABLE_WORDS, null, selection, selectionArg, null, null, null);
		int result = c.getCount();
		c.close();
		return result;
	}

	public List<Word> getZeroKnowledge() {

		List<Word> words = new ArrayList<Word>();

		String selection = MySQLiteHelper.COLUMN_KNOWLG + "= ?";
		String[] selectionArg = {"0"};
		Cursor cursor = database.query(MySQLiteHelper.TABLE_WORDS, null, selection, selectionArg, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Word word = cursorToWord(cursor);
			words.add(word);
			cursor.moveToNext();
		}
		cursor.close();

		return words;
	}

	public void deleteWord(Word word) {
		long id = word.getId();
		database.delete(MySQLiteHelper.TABLE_WORDS, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Word> getAllWords() {
		List<Word> words = new ArrayList<Word>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_WORDS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Word word = cursorToWord(cursor);
			words.add(word);
			cursor.moveToNext();
		}
		cursor.close();

		return words;
	}

	public List<Word> getSortEngWords() {
		List<Word> words = new ArrayList<Word>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_WORDS,
				allColumns, null, null, null, null, MySQLiteHelper.COLUMN_ENG + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Word word = cursorToWord(cursor);
			words.add(word);
			cursor.moveToNext();
		}
		cursor.close();
		return words;
	}

	private Word cursorToWord(Cursor cursor) {
		Word word = new Word();
		word.setId(cursor.getLong(0));
		word.setEnglish(cursor.getString(1));
		word.setRussian(cursor.getString(2));
		word.setLevel(cursor.getLong(3));
		word.setKnowledge(cursor.getLong(4));
		return word;
	}

}
