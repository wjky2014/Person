package com.programandroid.ContentProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.programandroid.R;


import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactListActivity extends Activity {
	private static final String tag = "ContactListActivity";
	private ListView lv_contact_list;
	private List<HashMap<String, String>> mContactList = new ArrayList<HashMap<String, String>>();

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 给数据适配器设置数据
			MyAdapter myAdapter = new MyAdapter();

			TextView emptyView = new TextView(getApplicationContext());
			emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			emptyView.setText(getResources().getString(R.string.please_add_contanct));
			emptyView.setVisibility(View.GONE);
			emptyView.setTextColor(Color.BLACK);
			emptyView.setTextSize(20);
			emptyView.setGravity(Gravity.CENTER);
			((ViewGroup) lv_contact_list.getParent()).addView(emptyView);
			lv_contact_list.setEmptyView(emptyView);

			lv_contact_list.setAdapter(myAdapter);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);

		initUI();
		initData();
	}

	/**
	 * 从系统数据库中获取联系人数据,权限,读取联系人
	 */
	private void initData() {
		new Thread() {
			public void run() {
				// 1,获取内容解析器(访问地址(后门))
				ContentResolver contentResolver = getContentResolver();
				// 2,对数据库指定表进行查询操作
				Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
						new String[] { "contact_id" }, null, null, null);
				// 3,判断游标中是否有数据,有数据一直度
				while (cursor.moveToNext()) {
					String id = cursor.getString(0);
					Log.i(tag, "id = " + id);// 1,2,3
					// 4,通过此id去关联data表和mimetype表生成视图,data1(数据),mimetype(数据类型)
					Cursor indexCursor = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
							new String[] { "data1", "mimetype" }, "raw_contact_id = ?", new String[] { id }, null);
					HashMap<String, String> hashMap = new HashMap<String, String>();
					// 5,游标向下移动获取数据过程
					while (indexCursor.moveToNext()) {
						String data = indexCursor.getString(0);
						String type = indexCursor.getString(1);

						// Log.i(tag, "data = "+data);
						// Log.i(tag, "type = "+type);

						if (type.equals("vnd.android.cursor.item/phone_v2")) {
							// data就为电话号码
							hashMap.put("phone", data);
						} else if (type.equals("vnd.android.cursor.item/name")) {
							// data 为联系人名字
							hashMap.put("name", data);
						}
					}
					indexCursor.close();
					mContactList.add(hashMap);
				}
				cursor.close();
				// 告知主线程集合中的数据以及准备完毕,可以让主线程去使用此集合,填充数据适配器
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}

	private void initUI() {
		lv_contact_list = (ListView) findViewById(R.id.lv_contact_list);
		lv_contact_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 1,position点中条目的索引值,集合的索引值
				String phone = mContactList.get(position).get("phone");
				// 2,将此电话号码传递给前一个界面
				Intent intent = new Intent();
				intent.putExtra("phone", phone);
				setResult(0, intent);
				// 3,关闭此界面
				finish();
			}
		});
	}

	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mContactList.size();
		}

		@Override
		public HashMap<String, String> getItem(int position) {
			return mContactList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Holder holder;
			if (convertView == null) {
				holder = new Holder();
				// 1,生成当前listview一个条目相应的view对象
				convertView = View.inflate(getApplicationContext(), R.layout.list_item_contact, null);
				// 2,找到view中的控件
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
				convertView.setTag(holder);

			} else {
				holder = (Holder) convertView.getTag();
			}

			// 3,给控件赋值
			holder.tv_name.setText(getItem(position).get("name"));
			holder.tv_phone.setText(getItem(position).get("phone"));

			return convertView;
		}
	}

	class Holder {

		public TextView tv_name;
		public TextView tv_phone;
	}
}
