package com.android.program.programandroid.component.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.program.programandroid.component.Service.StartServiceMethods;
/**
 * Created by wangjie on 2017/9/11.
 * please attention weixin get more info
 * weixin number: ProgramAndroid
 * 微信公众号： 程序员Android
 */
public class BootReceiverMethod extends BroadcastReceiver {
    public BootReceiverMethod() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

     //   接收开机广播处理事情，比如启动服务
         Intent mStartIntent = new Intent(context, StartServiceMethods.class);
         context.startService(mStartIntent);
        ScreenOnOffReceiver.ReceiverScreenOnOff(context);
    }
}
