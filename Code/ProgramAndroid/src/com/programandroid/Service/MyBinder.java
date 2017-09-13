package com.programandroid.Service;

import android.os.Binder;

/*
 * MyBinder.java
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
public class MyBinder extends Binder {
	private int count = 0;

	public int getBindCount() {
		return ++count;
	}

	public int getUnBindCount() {
		return count > 0 ? count-- : 0;
	}
}
