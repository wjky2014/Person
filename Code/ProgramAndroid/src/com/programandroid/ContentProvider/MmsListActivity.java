package com.programandroid.ContentProvider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.programandroid.MainActivity;
import com.programandroid.R;

/*
 * MmsListActivity.java
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
public class MmsListActivity extends Activity {
	private ContentResolver resolver;
    private ListView listView;
    private static final String SMS_URI = "content://sms";
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mms_list);
        listView = (ListView) findViewById(R.id.lv_mms);
        resolver = getContentResolver();

    }

    public void GetMMSBtn(View view) {

//插入数据
        ContentValues values = new ContentValues();
        values.put("address", "13917625629");
        values.put("body", "测试数据中。。。。。");
        Log.i("TAG", "values=" + values);
        resolver.insert(Uri.parse(SMS_URI), values);
        Log.i("TAG", "values=" + values);
//查询数据方法
        cursor = resolver.query(Uri.parse(SMS_URI), null, null, null, null);
//将数据显示到ListView中
        listView.setAdapter(new MyAdapter(MmsListActivity.this, cursor,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            //关闭cursor
//        cursor.close();
        }
    }

    class MyAdapter extends CursorAdapter {

        public MyAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        //创建一个视图，引入listview要展示的子视图
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return getLayoutInflater().inflate(R.layout.list_item_mms, null);
        }

        //绑定数据的方法
        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
            TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
            TextView tvState = (TextView) view.findViewById(R.id.tv_state);
            TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
            TextView tvId = (TextView) view.findViewById(R.id.tv_id);
            TextView tvRead = (TextView) view.findViewById(R.id.tv_read);

            String number = cursor.getString(cursor.getColumnIndex("address"));
            String body = cursor.getString(cursor.getColumnIndex("body"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            int read = cursor.getInt(cursor.getColumnIndex("read"));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));

            if (read == 0) {
                tvRead.setText("短信状态：未读");
            } else {
                tvRead.setText("短信状态：已读");
            }

            tvNumber.setText("手机号：" + number);
            tvContent.setText("短信内容：" + body);
            tvDate.setText("接收短信时间:" + date);
            tvId.setText("短信Id：" + id);

            if (type == 1) {
                tvState.setText("短信状态：已接收");

            } else {
                tvState.setText("短信状态：已发送");
            }
        }
    }

}
