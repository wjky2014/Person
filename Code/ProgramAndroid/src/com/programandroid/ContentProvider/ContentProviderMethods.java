package com.programandroid.ContentProvider;

import com.programandroid.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/*
 * ContentProviderMethods.java
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
public class ContentProviderMethods extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contentprovider);

		ContentObserverDatabase();
		MmsContentObserver();
	}

	/**
	 * 自定义短信内容观察者
	 */
	private void MmsContentObserver() {
		// TODO Auto-generated method stub
		// [1]注册一个内容观察者
		Uri uri = Uri.parse("content://sms/");
		getContentResolver().registerContentObserver(uri, true,
				new MmsContentObserver(this, new Handler()));

	}

	/**
	 * 监听ContentProvider数据库变化
	 */
	private void ContentObserverDatabase() {
		// [1]注册内容观察者
		Uri uri = Uri.parse("content://ProgramAndroid/person");
		// false 观察的uri 必须是一个确切的uri 如果是true
		getContentResolver().registerContentObserver(uri, true,
				new CustomContentObserver(this, new Handler()));

	}

	public void GetContacts(View view) {
		startActivity(new Intent(ContentProviderMethods.this,
				ContactListActivity.class));
	}

	public void GetMessaging(View view) {

		startActivity(new Intent(ContentProviderMethods.this,
				MmsListActivity.class));
	}
}
