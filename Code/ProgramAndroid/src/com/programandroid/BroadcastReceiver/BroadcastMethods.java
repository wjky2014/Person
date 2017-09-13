package com.programandroid.BroadcastReceiver;

import com.programandroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/*
 * BroadcastMethods.java
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
public class BroadcastMethods extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broadcast_methods);
	}

	public void SendCustomBroadcast(View view) {

		Intent customIntent = new Intent();
		customIntent.setAction("SendCustomBroadcast");
		sendBroadcast(customIntent);

	}

	public void SendCustomOrderBroadcast(View view) {
		Intent customOrderIntent = new Intent();
		customOrderIntent.setAction("SendCustomOrderBroadcast");
		customOrderIntent.putExtra("str_order_broadcast", "老板说：公司每人发 10 个 月饼");
		sendOrderedBroadcast(customOrderIntent,
				"android.permission.ORDERBROADCAST");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
