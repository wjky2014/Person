package com.sprocomm.processmanager.db;

import com.sprocomm.processmanager.utils.ProcessManagerUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppProcessForbiddenOpenHelper extends SQLiteOpenHelper {

	// create db to save forbidden database
	public AppProcessForbiddenOpenHelper(Context context) {
		super(context, ProcessManagerUtils.DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + ProcessManagerUtils.TABLE_NAME + "" + "("
				+ ProcessManagerUtils.ID
				+ " integer primary key autoincrement,"
				+ ProcessManagerUtils.PACKAGE_NAME + " varchar(50));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
