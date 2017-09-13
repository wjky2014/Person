package com.programandroid.BroadcastReceiver;

import com.programandroid.Service.StartServiceMethod;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/*
 * BootReceiverMethod.java
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
public class BootReceiverMethod extends BroadcastReceiver {
	public BootReceiverMethod() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		// 接收开机广播处理事情，比如启动服务
		Intent mStartIntent = new Intent(context, StartServiceMethod.class);
		context.startService(mStartIntent);
		ScreenOnOffReceiver.ReceiverScreenOnOff(context);
	}

}
