package com.android.program.programandroid.component;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.program.programandroid.R;

/**
 * Created by wangjie on 2017/9/7.
 * please attention weixin get more info
 * weixin number: ProgramAndroid
 * 微信公众号： 程序员Android
 */
public class ActivityMethods extends AppCompatActivity {
    private static final String TAG = "ActivityMethods wjwj:";
    private int mRequestCode = 100;
    private int mResultCode = 101;


    //  Activity 创建方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "----onCreate----");
        setContentView(R.layout.activity_methods);
    }

    //  Activity 在最新任务列表中打开时候会走此方法
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "----onRestart----");
    }

    //  Activity 在onCreate 或者 onRestart之后执行
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "----onStart----");
    }

    //    正在与用户交互的界面
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "----onResume----");
    }

    //  被其他与用户交互的Activity 部分覆盖
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "----onPause----");
    }

    // 被其它与用户交互的Activity 全部覆盖
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "----onStop----");
    }

    // Activity 销毁时候调用此方法
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "----onDestroy----");
    }

    // Activity 恢复数据的方法,经常在 oncreate 方法中恢复数据
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "----onRestoreInstanceState----");
    }

    // Activity 保存数据的方法，经常在 onPause 方法中保存数据
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(TAG, "----onSaveInstanceState----");
    }

    //    启动Activity的方法
    public void BtnStartActivity(View view) {
        startActivity(new Intent(ActivityMethods.this, OtherActivity.class));
    }

    //    启动带返回值Activity的方法
    public void BtnStartForResult(View view) {

        Intent intent = new Intent();
        intent.setClass(ActivityMethods.this, OtherActivity.class);
        startActivityForResult(intent, mRequestCode);
    }

    //    获取 Activity 返回结果的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode && resultCode == mResultCode) {
            String result = data.getStringExtra("str_set_result");
            Toast.makeText(this, "result :" + result, Toast.LENGTH_SHORT).show();
        }

    }
}