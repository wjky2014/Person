
package com.android.program.programandroid.component.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BindServiceMethods extends Service {
    private static final String TAG = "BindService wjwj:";

    public BindServiceMethods() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "----onCreate----");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "----onBind----");

        MyBinder myBinder = new MyBinder();
        return myBinder;
    }


    @Override
    public boolean onUnbind(Intent intent) {

        Log.i(TAG, "----onUnbind----");
        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "----onDestroy----");
        super.onDestroy();
    }
}
