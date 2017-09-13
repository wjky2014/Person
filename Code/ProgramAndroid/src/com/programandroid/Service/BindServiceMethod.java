package com.programandroid.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/*
 * BindServiceMethod.java
 *
 *  Created on: 2017-9-13
 *      Author: wangjie
 * 
 *  Welcome attention to weixin public number get more info
 *
 *  WeiXin Public Number : ProgramAndroid
 *  微信公众号 ：程序员Android
 *
 */

public class BindServiceMethod extends Service {
	private static final String TAG = "BindService wjwj:";

	public BindServiceMethod() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "----onCreate----");
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "----onBind----");

		MyBinder myBinder = new MyBinder();
		return myBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {

		Log.i(TAG, "----onUnbind----");
		return super.onUnbind(intent);

	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "----onDestroy----");
		super.onDestroy();
	}
}
