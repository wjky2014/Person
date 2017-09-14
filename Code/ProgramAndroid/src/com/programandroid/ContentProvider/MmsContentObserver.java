package com.programandroid.ContentProvider;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.widget.Toast;

/*
 * MmsContentObserver.java
 *
 *  Created on: 2017-9-14
 *      Author: wangjie
 * 
 *  Welcome attention to weixin public number get more info
 *
 *  WeiXin Public Number : ProgramAndroid
 *  微信公众号 ：程序员Android
 *
 */
public class MmsContentObserver extends ContentObserver {
	Context mContext;

	/**
	 * @param handler
	 */
	public MmsContentObserver(Context context, Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	// 当观察的内容发生改变的时候调用
	@Override
	public void onChange(boolean selfChange) {
		Toast.makeText(mContext, "有人篡改短信的数据库，请注意手机安全", Toast.LENGTH_SHORT)
				.show();

		super.onChange(selfChange);
	}

}
