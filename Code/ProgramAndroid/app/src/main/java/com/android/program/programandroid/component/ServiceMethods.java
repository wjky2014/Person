package com.android.program.programandroid.component;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by wangjie on 2017/9/7.
 * please attention weixin get more info
 * weixin number: ProgramAndroid
 * 微信公众号： 程序员Android
 */
public class ServiceMethods extends Service {
    public ServiceMethods() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
