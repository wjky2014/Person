package com.android.program.programandroid.component.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.program.programandroid.R;

/**
 * Created by wangjie on 2017/9/7.
 * please attention weixin get more info
 * weixin number: ProgramAndroid
 * 微信公众号： 程序员Android
 */
public class StartServiceMethods extends Service {
    private static final String TAG = "StartService wjwj:";
    NotificationManager notifyManager;

    public StartServiceMethods() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "----onCreate----");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "----onStartCommand----");

//        获取NotificationManager实例
        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        实例化NotificationCompat.Builder并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                设置通知标题
                .setContentTitle("我是通过StartService服务启动的通知")
//                设置通知不能自动取消
                .setAutoCancel(false)
                .setOngoing(true)
//                设置通知时间，默认为系统发出通知的时间，通常不用设置
//                .setWhen(System.currentTimeMillis())
//               设置通知内容
                .setContentText("请使用StopService 方法停止服务");

        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notifyManager.notify(1, builder.build());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "----onDestroy----");
        notifyManager.cancelAll();
        super.onDestroy();
    }
}
