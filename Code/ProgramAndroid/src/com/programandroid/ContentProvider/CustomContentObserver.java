package com.programandroid.ContentProvider;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/*
 * CustomContentObserver.java
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
public class CustomContentObserver extends ContentObserver {

	Context mContext;

	/**
	 * @param handler
	 */
	public CustomContentObserver(Context context, Handler handler) {
		super(handler);
		mContext = context;
	}

	// 当我们观察的uri发生改变的时候调用
	@Override
	public void onChange(boolean selfChange) {

		Toast.makeText(mContext, "自定义数据库提供者内容发生变化，请注意安全", Toast.LENGTH_SHORT)
				.show();
		super.onChange(selfChange);
	}

}
