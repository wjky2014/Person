package com.android.program.programandroid.component.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;
/**
 * Created by wangjie on 2017/9/11.
 * please attention weixin get more info
 * weixin number: ProgramAndroid
 * 微信公众号： 程序员Android
 */
public class AppModifyMethods extends BroadcastReceiver {
    public AppModifyMethods() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String appName=null;
        try {
            PackageManager mPackageManager=context.getApplicationContext().getPackageManager();
            String packageName = intent.getData().getSchemeSpecificPart();
            ApplicationInfo mApplicationInfo=mPackageManager.getApplicationInfo(packageName,0);
            appName= (String) mPackageManager.getApplicationLabel(mApplicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            Toast.makeText(context, appName + " app 安装成功", Toast.LENGTH_SHORT).show();
        }

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            Toast.makeText(context, appName + "app 被替换", Toast.LENGTH_SHORT).show();
        }

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            Toast.makeText(context, appName + "app 卸载成功", Toast.LENGTH_SHORT).show();
        }


        if (intent.getAction().equals(Intent.ACTION_PACKAGE_CHANGED)) {
            Toast.makeText(context, appName + "app 状态发生改变", Toast.LENGTH_SHORT).show();
        }

        if (intent.getAction().equals(Intent.ACTION_INSTALL_PACKAGE)) {
            Toast.makeText(context, appName + "app 安装成功广播", Toast.LENGTH_SHORT).show();
        }

        if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
            Toast.makeText(context, appName + "app 数据改变广播", Toast.LENGTH_SHORT).show();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_FIRST_LAUNCH)) {
            Toast.makeText(context, appName + "app 第一次打开广播", Toast.LENGTH_SHORT).show();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_RESTARTED)) {
            Toast.makeText(context, appName + "接收 app 被重置(Data 被清清理)的广播", Toast.LENGTH_SHORT).show();
        }

        if (intent.getAction().equals(Intent.ACTION_MANAGE_PACKAGE_STORAGE)) {
            Toast.makeText(context, appName + "app storage 发生变化", Toast.LENGTH_SHORT).show();
        }
    }
}