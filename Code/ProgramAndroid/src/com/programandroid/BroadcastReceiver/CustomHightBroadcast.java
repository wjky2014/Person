package com.programandroid.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/*
 * CustomHightBroadcast.java
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
public class CustomHightBroadcast extends BroadcastReceiver {
	public void CustomHightBrodcast() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals("SendCustomOrderBroadcast")) {
			Toast.makeText(context,
					intent.getStringExtra("str_order_broadcast"),
					Toast.LENGTH_SHORT).show();
			Bundle bundle = new Bundle();
			bundle.putString("str_order_broadcast", "经理说：公司每人发 5 个 月饼");
			// 修改广播传输数据
			setResultExtras(bundle);
		}
	}

}
