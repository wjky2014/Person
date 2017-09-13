package com.programandroid.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/*
 * CustomMiddleBroadcast.java
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
public class CustomLowerBroadcast extends BroadcastReceiver {

	public CustomLowerBroadcast() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("SendCustomOrderBroadcast")) {
			String notice = getResultExtras(true).getString(
					"str_order_broadcast");
			Toast.makeText(context, notice, Toast.LENGTH_SHORT).show();
			// 终止广播继续传播下去
			abortBroadcast();
		}
	}
}
