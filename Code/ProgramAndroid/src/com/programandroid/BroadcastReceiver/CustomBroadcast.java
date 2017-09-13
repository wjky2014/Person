package com.programandroid.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/*
 * CustomBroadcast.java
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
public class CustomBroadcast extends BroadcastReceiver {

	public CustomBroadcast() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals("SendCustomBroadcast")) {
			Toast.makeText(context, "自定义广播接收成功：Action:SendCustomBroadcast",
					Toast.LENGTH_SHORT).show();
		}
	}
}
