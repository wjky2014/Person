package com.android.program.programandroid.component.Service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.program.programandroid.R;

public class ServiceMethods extends AppCompatActivity {

    private boolean isStartService = false;
    private boolean isBindService = false;
    private boolean isForcegroundService = false;
    private Intent mStartIntent;
    private Intent mBindIntent;
    private Intent mForcegroundServie;
    private MyBinder myBinder;
    private int mBindCount;
    private int mUnBindCount;
    //    绑定服务连接处理方法
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MyBinder) service;
            mBindCount = myBinder.getBindCount();
            mUnBindCount = myBinder.getUnBindCount();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_methods);
        mStartIntent = new Intent(ServiceMethods.this, StartServiceMethods.class);
        mBindIntent = new Intent(ServiceMethods.this, BindServiceMethods.class);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStartService = false;
        isBindService = false;
    }

    //    启动服务处理方法
    public void BtnStartService(View view) {
        startService(mStartIntent);
        isStartService = true;
        Toast.makeText(ServiceMethods.this, "启动服务", Toast.LENGTH_SHORT).show();
    }

    //    启动绑定服务处理方法
    public void BtnStartBindService(View view) {

        bindService(mBindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        isBindService = true;
        Toast.makeText(ServiceMethods.this, "启动 " + mBindCount + " 次绑定服务", Toast.LENGTH_SHORT).show();
    }

    //    解除绑定服务处理方法
    public void BtnSopBindService(View view) {
        if (isBindService) {
            unbindService(serviceConnection);
            Toast.makeText(ServiceMethods.this, "解除 " + mUnBindCount + " 次绑定服务", Toast.LENGTH_SHORT).show();
            isBindService = false;
        }

    }

    //    停止服务处理方法
    public void BtnStopService(View view) {

        if (!isBindService && isStartService) {
            stopService(mStartIntent);
            isStartService = false;
            Toast.makeText(ServiceMethods.this, "停止服务", Toast.LENGTH_SHORT).show();
        }
    }


    //    启动前台服务处理方法
    public void BtnStartForcegroundService(View view) {

        mForcegroundServie = new Intent(ServiceMethods.this, MyStartForcegroundService.class);
        mForcegroundServie.setAction("start_forceground_service");
        startService(mForcegroundServie);
        isForcegroundService = true;
    }

    //    后台服务处理方法
    public void BtnBackgroundService(View view) {
        mForcegroundServie = new Intent(ServiceMethods.this, MyStartForcegroundService.class);
        mForcegroundServie.setAction("stop_forceground_service");
        startService(mForcegroundServie);
        isForcegroundService = true;

    }

}
