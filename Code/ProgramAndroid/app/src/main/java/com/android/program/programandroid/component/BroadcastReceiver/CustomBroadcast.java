package com.android.program.programandroid.component.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomBroadcast extends BroadcastReceiver {
    public CustomBroadcast() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("SendCustomBroadcast")){
            Toast.makeText(context,"自定义广播接收成功：Action:SendCustomBroadcast",Toast.LENGTH_SHORT).show();
        }
    }
}
