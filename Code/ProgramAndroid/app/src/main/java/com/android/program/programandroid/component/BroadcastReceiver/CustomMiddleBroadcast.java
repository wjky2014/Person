package com.android.program.programandroid.component.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class CustomMiddleBroadcast extends BroadcastReceiver {
    public CustomMiddleBroadcast() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("SendCustomOrderBroadcast")) {
            Toast.makeText(context, getResultExtras(true).getString("str_order_broadcast"), Toast.LENGTH_SHORT).show();
            Bundle bundle=new Bundle();
            bundle.putString("str_order_broadcast","主管说：公司每人发 2 个 月饼");
            setResultExtras(bundle);
        }
    }
}
