package com.android.program.programandroid.component.BroadcastReceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.program.programandroid.R;

public class BroadcastMethods extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_methods);
    }
    public void SendCustomBroadcast(View view){


        Intent customIntent=new Intent();
        customIntent.setAction("SendCustomBroadcast");
        sendBroadcast(customIntent);

    }
    public void SendCustomOrderBroadcast(View view){
        Intent customOrderIntent=new Intent();
        customOrderIntent.setAction("SendCustomOrderBroadcast");
        customOrderIntent.putExtra("str_order_broadcast","老板说：公司每人发 10 个 月饼");
        sendOrderedBroadcast(customOrderIntent,"android.permission.ORDERBROADCAST");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
