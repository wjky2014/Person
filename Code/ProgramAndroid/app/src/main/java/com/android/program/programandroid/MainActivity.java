package com.android.program.programandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.program.programandroid.component.ActivityMethods;

/**
 * Created by wangjie on 2017/9/7.
 * please attention weixin get more info
 * weixin number: ProgramAndroid
 * 公众号： 程序员Android
 */
public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private static final int MACTIVITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView= (ListView) findViewById(R.id.lv_main);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case MACTIVITY:
                        startActivity(new Intent(MainActivity.this, ActivityMethods.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }
    }

