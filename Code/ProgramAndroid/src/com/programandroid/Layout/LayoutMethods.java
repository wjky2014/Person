package com.programandroid.Layout;

import com.programandroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;

/*
 * LayoutMethods.java
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
public class LayoutMethods extends Activity {
	LinearLayout mLinearLayout;
	RelativeLayout mRelativeLayout;
	FrameLayout mFrameLayout;
	TableLayout mTableLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout);
		mLinearLayout = (LinearLayout) findViewById(R.id.ll_layout);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_layout);
		mFrameLayout = (FrameLayout) findViewById(R.id.fl_layout);
		mTableLayout = (TableLayout) findViewById(R.id.tl_layout);
	}

	public void AllViewGone() {
		mLinearLayout.setVisibility(View.GONE);
		mRelativeLayout.setVisibility(View.GONE);
		mFrameLayout.setVisibility(View.GONE);
		mTableLayout.setVisibility(View.GONE);
	}

	public void LinearLayoutMethod(View view) {
		AllViewGone();
		mLinearLayout.setVisibility(View.VISIBLE);
	}

	public void RelativeLayoutMethod(View view) {
		AllViewGone();
		mRelativeLayout.setVisibility(View.VISIBLE);

	}

	public void FrameLayoutMethod(View view) {
		AllViewGone();
		mFrameLayout.setVisibility(View.VISIBLE);

	}

	public void TableLayoutMethod(View view) {
		AllViewGone();
		mTableLayout.setVisibility(View.VISIBLE);
	}

	public void AbsoluteLayoutMethod(View view) {
		AllViewGone();
		Toast.makeText(this, "布局已经被弃用,请更换其他布局", Toast.LENGTH_SHORT).show();
	}
}
