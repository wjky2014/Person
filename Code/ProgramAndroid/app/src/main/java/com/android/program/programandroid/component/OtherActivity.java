package com.android.program.programandroid.component;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.program.programandroid.R;

/**
 * Created by wangjie on 2017/9/7.
 * please attention weixin get more info
 * weixin number: ProgramAndroid
 * 微信公众号： 程序员Android
 */
public class OtherActivity extends AppCompatActivity {

    private static String TAG = "OtherActivity wjwj:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        Log.i(TAG, "----onCreate----");
        Button setResultBtn = (Button) findViewById(R.id.btn_set_result);
        setResultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resultCode = 101;
                Intent intent = new Intent();
                intent.putExtra("str_set_result", "带返回结果的Activity");
                setResult(resultCode, intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "----onRestart----");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "----onStart----");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "----onResume----");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "----onPause----");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "----onStop----");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "----onDestroy----");
    }
}
