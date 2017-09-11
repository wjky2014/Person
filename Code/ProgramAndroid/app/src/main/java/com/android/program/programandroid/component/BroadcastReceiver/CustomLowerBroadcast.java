package com.android.program.programandroid.component.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class CustomLowerBroadcast extends BroadcastReceiver {
    public CustomLowerBroadcast() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("SendCustomOrderBroadcast")) {
            String notice= getResultExtras(true).getString("str_order_broadcast");
            Toast.makeText(context,notice, Toast.LENGTH_SHORT).show();
//          终止广播继续传播下去
            abortBroadcast();
        }
    }
}
