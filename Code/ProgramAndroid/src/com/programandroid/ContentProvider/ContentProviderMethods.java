package com.programandroid.ContentProvider;

import com.programandroid.MainActivity;
import com.programandroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
