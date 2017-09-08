package com.android.program.programandroid.component.Service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.android.program.programandroid.R;

public class MyStartForcegroundService extends Service {

    public MyStartForcegroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals("start_forceground_service")) {

//        获取NotificationManager实例
            NotificationManager notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        实例化NotificationCompat.Builder并设置相关属性
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                设置小图标
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                设置通知标题
                    .setContentTitle("我是通过startForeground 启动前台服务通知")
//                设置通知不能自动取消
                    .setAutoCancel(false)
                    .setOngoing(true)
//                设置通知时间，默认为系统发出通知的时间，通常不用设置
//                .setWhen(System.currentTimeMillis())
//               设置通知内容
                    .setContentText("请使用stopForeground 方法改为后台服务");

            //通过builder.build()方法生成Notification对象,并发送通知,id=1
//        设置为前台服务
            startForeground(1, builder.build());

        } else if (intent.getAction().equals("stop_forceground_service")) {
//            取消前台服务
            stopForeground(true);
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
