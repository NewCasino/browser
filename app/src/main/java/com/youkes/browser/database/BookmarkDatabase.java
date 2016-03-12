
package com.youkes.browser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.youkes.browser.R;

import java.util.ArrayList;
import java.util.List;

public class BookmarkDatabase extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	public static final String DATABASE_NAME = "bookmark_db";

	// HistoryItems table name
	public static final String TABLE_HISTORY = "bookmark";

	// HistoryItems Table Columns names
	public static final String KEY_ID = "id";
	public static final String KEY_URL = "url";
	public static final String KEY_TITLE = "title";
	public static final String KEY_TIME_VISITED = "time";
	public static final String Col_History_userId = "userId";


	public static SQLiteDatabase mDatabase;

	private static BookmarkDatabase mInstance;

	public static BookmarkDatabase getInstance(Context context) {
		if (mInstance == null || mInstance.isClosed()) {
			mInstance = new BookmarkDatabase(context);
		}
		return mInstance;
	}

	private BookmarkDatabase(Context context) {
		super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
		mDatabase = this.getWritableDatabase();
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_URL + " TEXT," + KEY_TITLE + " TEXT,"
				+ Col_History_userId + " TEXT,"
				+ KEY_TIME_VISITED + " INTEGER" + ")";
		db.execSQL(CREATE_HISTORY_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if it exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
		// Create tables again
		onCreate(db);
	}

	public boolean isClosed() {
		return mDatabase == null || !mDatabase.isOpen();
	}

	@Override
	public synchronized void close() {
		if (mDatabase != null) {
			mDatabase.close();
		}
		super.close();
	}

	public synchronized boolean deleteBookmarkItem(String userId, String url) {
		mDatabase.delete(TABLE_HISTORY, KEY_URL + " = ? AND " + Col_History_userId + " = ? ", new String[]{url, userId});
		return true;
	}

	public synchronized boolean addBookmarkItem(String userId,String url, String title) {
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, title);
		values.put(KEY_TIME_VISITED, System.currentTimeMillis());
		values.put(Col_History_userId, userId);
		Cursor q = mDatabase.query(false, TABLE_HISTORY, new String[] { KEY_URL },
				KEY_URL + " = ? AND "+Col_History_userId+" = ? ", new String[] { url,userId }, null, null, null, "1");
		if (q.getCount() > 0) {
			mDatabase.update(TABLE_HISTORY, values, KEY_URL + " = ?", new String[] { url });
		} else {
			addHistoryItem(userId,new HistoryItem(url, title));
		}
		q.close();
		return true;
	}

	private synchronized boolean addHistoryItem(String userId,HistoryItem item) {
		ContentValues values = new ContentValues();
		values.put(KEY_URL, item.getUrl());
		values.put(KEY_TITLE, item.getTitle());
		values.put(Col_History_userId,userId);
		values.put(KEY_TIME_VISITED, System.currentTimeMillis());
		mDatabase.insert(TABLE_HISTORY, null, values);
		return true;
	}

	String getHistoryItem(String userId,String url) {
		Cursor cursor = mDatabase.query(TABLE_HISTORY, new String[] { KEY_ID, KEY_URL, KEY_TITLE },
				KEY_URL + " = ? AND "+Col_History_userId+" = ? ", new String[] { url,userId }, null, null, null, null);
		String m = null;
		if (cursor != null) {
			cursor.moveToFirst();
			m = cursor.getString(0);

			cursor.close();
		}
		return m;
	}

	public List<HistoryItem> findItemsContaining(String userId,String search) {
		List<HistoryItem> itemList = new ArrayList<>();
		String selectQuery = "SELECT * FROM " + TABLE_HISTORY
				+ " WHERE " + KEY_TITLE + " LIKE '%"
				+ search + "%' OR " + KEY_URL + " LIKE '%" + search + "%' "
				+" AND userId='"+userId+"'"
				+ "ORDER BY "
				+ KEY_TIME_VISITED + " DESC LIMIT 5";
		Cursor cursor = mDatabase.rawQuery(selectQuery, null);

		int n = 0;
		if (cursor.moveToFirst()) {
			do {
				HistoryItem item = new HistoryItem();
				item.setID(Integer.parseInt(cursor.getString(0)));
				item.setUrl(cursor.getString(1));
				item.setTitle(cursor.getString(2));
				item.setImageId(R.drawable.ic_history);
				itemList.add(item);
				n++;
			} while (cursor.moveToNext() && n < 5);
		}
		cursor.close();
		return itemList;
	}

	public List<HistoryItem> getLastHundredItems(String userId) {
		List<HistoryItem> itemList = new ArrayList<>();
		String selectQuery = "SELECT * FROM " + TABLE_HISTORY
				+" WHERE userId='"+userId+"'"
				+ " ORDER BY " + KEY_TIME_VISITED
				+ " DESC";

		Cursor cursor = mDatabase.rawQuery(selectQuery, null);
		int counter = 0;
		if (cursor.moveToFirst()) {
			do {
				HistoryItem item = new HistoryItem();
				item.setID(Integer.parseInt(cursor.getString(0)));
				item.setUrl(cursor.getString(1));
				item.setTitle(cursor.getString(2));
				item.setImageId(R.drawable.ic_history);
				itemList.add(item);
				counter++;
			} while (cursor.moveToNext() && counter < 100);
		}
		cursor.close();
		return itemList;
	}

	public List<HistoryItem> getLastItems(String userId,int pageIndex,int pageSize) {
		List<HistoryItem> itemList = new ArrayList<>();
		int skip=pageIndex*pageSize;
		String selectQuery = "SELECT * FROM " + TABLE_HISTORY
				+" WHERE userId='"+userId+"'"
				+ " ORDER BY " + KEY_TIME_VISITED
				+ " DESC "+" LIMIT "+String.valueOf(pageSize)+" OFFSET "+String.valueOf(skip);

		Cursor cursor = mDatabase.rawQuery(selectQuery, null);
		int counter = 0;
		if (cursor.moveToFirst()) {
			do {
				HistoryItem item = new HistoryItem();
				item.setID(Integer.parseInt(cursor.getString(0)));
				item.setUrl(cursor.getString(1));
				item.setTitle(cursor.getString(2));
				item.setImageId(R.drawable.ic_history);
				itemList.add(item);
				counter++;
			} while (cursor.moveToNext() && counter < pageSize);
		}
		cursor.close();
		return itemList;
	}

	public List<HistoryItem> getAllHistoryItems(String userId) {
		List<HistoryItem> itemList = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + TABLE_HISTORY
				+" WHERE userId='"+userId+"'"
				+ " ORDER BY " + KEY_TIME_VISITED
				+ " DESC";

		Cursor cursor = mDatabase.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				HistoryItem item = new HistoryItem();
				item.setID(Integer.parseInt(cursor.getString(0)));
				item.setUrl(cursor.getString(1));
				item.setTitle(cursor.getString(2));
				item.setImageId(R.drawable.ic_history);
				itemList.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return itemList;
	}

	public synchronized int updateHistoryItem(String userId,HistoryItem item) {

		ContentValues values = new ContentValues();
		values.put(KEY_URL, item.getUrl());
		values.put(KEY_TITLE, item.getTitle());
		values.put(Col_History_userId,userId);
		values.put(KEY_TIME_VISITED, System.currentTimeMillis());
		return mDatabase.update(TABLE_HISTORY, values, KEY_ID + " = ?",
				new String[] { String.valueOf(item.getId()) });
	}

	public int getHistoryItemsCount(String userId) {
		String countQuery = "SELECT * FROM " + TABLE_HISTORY +" WHERE userId='"+userId+"'";
		Cursor cursor = mDatabase.rawQuery(countQuery, null);
		int n = cursor.getCount();
		cursor.close();

		return n;
	}

	public static void reset() {

	}
}
