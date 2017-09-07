package com.sprocomm.processmanager.db;

import java.util.ArrayList;
import java.util.List;

import com.sprocomm.processmanager.utils.ProcessManagerUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AppForbiddenDao {
	private Context ctx;

	public AppForbiddenDao(Context ctx) {
		appForbiddenOpenHelper = new AppProcessForbiddenOpenHelper(ctx);
		this.ctx = ctx;
	};

	private static AppForbiddenDao mAppForbiddenDao = null;
	private AppProcessForbiddenOpenHelper appForbiddenOpenHelper;

	public static AppForbiddenDao getInstance(Context ctx) {
		if (mAppForbiddenDao == null) {
			mAppForbiddenDao = new AppForbiddenDao(ctx);
		}
		return mAppForbiddenDao;
	}

	// insert data to db
	public void insert(String packageName) {
		SQLiteDatabase db = appForbiddenOpenHelper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(ProcessManagerUtils.PACKAGE_NAME, packageName);

		db.insert(ProcessManagerUtils.TABLE_NAME, null, contentValues);

		db.close();

	}

	// delete data from db
	public void delete(String packageName) {
		SQLiteDatabase db = appForbiddenOpenHelper.getWritableDatabase();

		db.delete(ProcessManagerUtils.TABLE_NAME,
				ProcessManagerUtils.PACKAGE_NAME + " = ?",
				new String[] { packageName });

		db.close();
	}

	// find all forbidden packagename
	public List<String> findAll() {
		List<String> packageList = new ArrayList<String>();
		SQLiteDatabase db = appForbiddenOpenHelper.getWritableDatabase();
		Cursor cursor = db.query(ProcessManagerUtils.TABLE_NAME,
				new String[] { ProcessManagerUtils.PACKAGE_NAME }, null, null,
				null, null, null);
		while (cursor.moveToNext()) {
			packageList.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return packageList;
	}
}
