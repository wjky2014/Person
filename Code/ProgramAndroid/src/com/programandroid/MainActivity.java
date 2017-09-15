package com.programandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.programandroid.Activity.ActivityMethods;
import com.programandroid.BroadcastReceiver.BroadcastMethods;
import com.programandroid.ContentProvider.ContentProviderMethods;
import com.programandroid.Dialog.DiaLogMethods;
import com.programandroid.Layout.LayoutMethods;
import com.programandroid.Service.ServiceMethod;

/*
 * MainActivity.java
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
public class MainActivity extends ActionBarActivity {

	private ListView mListView;
	private static final int MACTIVITY = 0;
	private static final int MSERVICE = 1;
	private static final int MBROADCASTRECEIVER = 2;
	private static final int MCONTENTPROVIDER = 3;
	private static final int MLAYOUT = 4;
	private static final int MDIALOG = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mListView = (ListView) findViewById(R.id.lv_main);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				switch (position) {
				case MACTIVITY:
					startActivity(new Intent(MainActivity.this,
							ActivityMethods.class));
					break;

				case MSERVICE:
					startActivity(new Intent(MainActivity.this,
							ServiceMethod.class));
					break;
				case MBROADCASTRECEIVER:
					startActivity(new Intent(MainActivity.this,
							BroadcastMethods.class));
					break;
				case MCONTENTPROVIDER:
					startActivity(new Intent(MainActivity.this,
							ContentProviderMethods.class));
					break;
				case MLAYOUT:
					startActivity(new Intent(MainActivity.this,
							LayoutMethods.class));
					break;
				case MDIALOG:
					startActivity(new Intent(MainActivity.this,
							DiaLogMethods.class));
					break;

				default:
					break;
				}
			}
		});
	}
}
