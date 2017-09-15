package com.programandroid.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.programandroid.R;

/*
 * DiaLogMethods.java
 *
 *  Created on: 2017-9-15
 *      Author: wangjie
 * 
 *  Welcome attention to weixin public number get more info
 *
 *  WeiXin Public Number : ProgramAndroid
 *  微信公众号 ：程序员Android
 *
 */
public class DiaLogMethods extends Activity {

	private int count;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);
	}

	/**
	 * 简单对话框
	 */
	public void SimpleDialog(View view) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.gril).setTitle("简单对话框")
				.setMessage("设置Dialog 显示的内容")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Toast.makeText(DiaLogMethods.this, "点击了确定按钮",
								Toast.LENGTH_SHORT).show();
					}
				}).setNegativeButton("Cancle", null).create().show();

	}

	/**
	 * 自定义多选按钮对话框
	 * */
	public void MultiChoiceDialog(View view) {
		final String font[] = { "小号字体", "中号字体", "大号字体", "超大号字体" };
		final boolean[] MultiChoice = new boolean[] { false, true, false, false };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("多选对话框")
				.setIcon(R.drawable.ic_launcher)
				.setMultiChoiceItems(font, MultiChoice,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								MultiChoice[which] = isChecked;
								String choiceString = "";
								for (int i = 0; i < MultiChoice.length; i++) {
									if (MultiChoice[i]) {
										choiceString = choiceString + font[i]
												+ "  ";
									}
								}

								if (choiceString.equals("")
										|| choiceString.length() == 0) {

									// 都不选的处理方法

									Toast.makeText(DiaLogMethods.this,
											"请选择一个内容", Toast.LENGTH_SHORT)
											.show();
								} else {

									Toast.makeText(DiaLogMethods.this,
											"选择的字体为" + choiceString,
											Toast.LENGTH_SHORT).show();

								}

							}
						}).setPositiveButton("OK", null)
				.setNegativeButton("Cancle", null).create().show();

	}

	/**
	 * 单选按钮对话框实现
	 **/
	public void SingleChoiceDialog(View view) {
		final String font[] = { "小号字体", "中号字体", "大号字体", "超大号字体" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("单选对话框")
				.setIcon(R.drawable.ic_launcher)
				.setSingleChoiceItems(font, 0,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Toast.makeText(DiaLogMethods.this,
										"选择的字体为:" + font[which],
										Toast.LENGTH_SHORT).show();
								dialog.dismiss();
							}
						}).setPositiveButton("OK", null)
				.setNegativeButton("Cancle", null).create().show();

	}

	/**
	 * 列表对话框实现
	 **/
	public void ListItemDialog(View view) {
		final String font[] = { "小号字体", "中号字体", "大号字体", "超大号字体" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher)
				.setTitle(" 列表对话框")
				.setItems(font, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(DiaLogMethods.this,
								"选择内容是：" + font[which], Toast.LENGTH_SHORT)
								.show();
					}
				}).setNegativeButton("Cancle", null)
				.setPositiveButton("OK", null).create().show();

	}

	/**
	 * 水平进度条对话框实现
	 **/
	@SuppressWarnings("deprecation")
	public void HorProgressDialog(View view) {

		final ProgressDialog progressDialog = new ProgressDialog(
				DiaLogMethods.this);
		progressDialog.setTitle("进度对话框");
		progressDialog.setIcon(R.drawable.ic_launcher);
		progressDialog.setMessage("加载中...");
		// 水平进度条显示
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// 圆形进度条显示
		// progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(true);
		progressDialog.setButton("Cancle",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(DiaLogMethods.this, "取消进度条对话框",
								Toast.LENGTH_LONG).show();
						progressDialog.cancel();
						count = 0;
					}
				});
		progressDialog.setMax(100);
		progressDialog.show();
		count = 0;
		new Thread() {
			@Override
			public void run() {

				while (count <= 100) {
					progressDialog.setProgress(count++);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						progressDialog.dismiss();
						e.printStackTrace();
					}

				}
				progressDialog.dismiss();
			}
		}.start();

	}

	/**
	 * 圆形进度条显示
	 **/
	@SuppressWarnings("deprecation")
	public void SpinerProgressDialog(View view) {

		final ProgressDialog progressDialog = new ProgressDialog(
				DiaLogMethods.this);
		progressDialog.setTitle("进度对话框");
		progressDialog.setIcon(R.drawable.ic_launcher);
		progressDialog.setMessage("加载中...");
		// 水平进度条显示
		// progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// 圆形进度条显示
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(true);
		progressDialog.setButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(DiaLogMethods.this, "取消进度条对话框",
						Toast.LENGTH_LONG).show();
				progressDialog.cancel();
				count = 0;
			}
		});
		progressDialog.setMax(100);
		progressDialog.show();
		count = 0;
		new Thread() {
			@Override
			public void run() {

				while (count <= 100) {
					progressDialog.setProgress(count++);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						progressDialog.dismiss();
						e.printStackTrace();
					}

				}
				progressDialog.dismiss();
			}
		}.start();

	}

	/**
	 * 自定义图文对话框实现
	 **/
	public void CustomImgTvDialog(View view) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		View contextview = getLayoutInflater().inflate(
				R.layout.dialog_custom_img_tv, null);
		LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linlout1);
		LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.linlout2);
		ImageView img1 = (ImageView) contextview.findViewById(R.id.img1);
		TextView tv1 = (TextView) contextview.findViewById(R.id.tv1);
		// 这里可以处理一些点击事件

		builder.setIcon(R.drawable.gril).setTitle("自定义对话框")
				.setView(contextview)
				// 或者在这里处理一些事件
				.setPositiveButton("OK", null)
				.setNegativeButton("Cancle", null).create().show();

	}

	/**
	 * 自定义EditText对话框
	 **/
	public void CustomEditTextDialog(View view) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View Tittleview = getLayoutInflater().inflate(
				R.layout.dialog_custom_layout, null);
		ImageView img2 = (ImageView) Tittleview.findViewById(R.id.img2);
		TextView textView = (TextView) Tittleview.findViewById(R.id.tv2);

		textView.setText("自定义对话框");
		img2.setImageResource(R.drawable.ic_launcher);
		// 自定义tittle
		builder.setCustomTitle(Tittleview);

		View contentView = getLayoutInflater().inflate(
				R.layout.dialog_custom_et, null);
		EditText username = (EditText) contentView.findViewById(R.id.username);
		EditText passworld = (EditText) contentView
				.findViewById(R.id.passworld);

		builder.setView(contentView);
		builder.setPositiveButton("OK", null).setNegativeButton("Cancle", null)
				.create().show();

	}

	public void CustomStyleDialog(View v) {

		// 对话框和activity绑定,所以必须传递activity对象
		Builder builder = new AlertDialog.Builder(this);
		// 获取对话框对象
		final AlertDialog dialog = builder.create();
		// 修改对话框的样式(布局结构)
		View view = View.inflate(this, R.layout.dialog_custom_style, null);

		// 因为在2.3.3版本上,系统默认设置内间距,所以需要去除此内间距
		// dialog.setView(view);
		dialog.setView(view, 0, 0, 0, 0);

		// 找到对话框中所有控件
		Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

		final EditText et_set_psd = (EditText) view
				.findViewById(R.id.et_set_psd);
		final EditText et_confirm_psd = (EditText) view
				.findViewById(R.id.et_confirm_psd);

		bt_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 如果用户没有输入两次密码,告知用户输入密码
				String psd = et_set_psd.getText().toString().trim();
				String confirmPsd = et_confirm_psd.getText().toString().trim();
				if (!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(confirmPsd)) {
					if (psd.equals(confirmPsd)) {
						// 当前的对话框隐藏
						dialog.dismiss();

					} else {
						Toast.makeText(getApplicationContext(), "两次输入密码不一致",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "密码不能为空",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// 展示对话框
		dialog.show();

	}

	public void CustomStyleProgressDialog(View view) {

		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.dialog_custom_style_progress, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);

		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);

		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this,
				R.anim.loading_animation);

		spaceshipImage.startAnimation(hyperspaceJumpAnimation);

		Dialog loadingDialog = new Dialog(this, R.style.loading_dialog);

		// loadingDialog.setCancelable(true);//“返回键”取消 不可以用
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		loadingDialog.show();
	}

	public void CustomFragmentDialog(View view) {

		CustomDialogFragment customDialogFragment = new CustomDialogFragment();
		customDialogFragment.show(getFragmentManager(), "fragment");
	}

}
