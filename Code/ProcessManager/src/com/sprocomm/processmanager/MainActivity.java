package com.sprocomm.processmanager;

import com.sprocomm.processmanager.utils.ProcessManagerUtils;

import android.support.v7.app.ActionBarActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ReceiverScreenOff();
	}

	public void BackgroundProcessManager(View view) {
		startActivity(new Intent(MainActivity.this,
				BackgroundPrcoessManagerAty.class));

	}

	public void ReceiverScreenOff() {
		IntentFilter screenOffFilter = new IntentFilter();
		screenOffFilter.addAction(Intent.ACTION_SCREEN_OFF);
		screenOffFilter.addAction(Intent.ACTION_SCREEN_ON);
		BroadcastReceiver mScreenOnOffReceiver = new BroadcastReceiver() {

			long screenOnTime, sceenOffTime;
			long killProcessTime = ProcessManagerUtils.SCREEN_OFF_KILL_PROCESS_TIME;

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getAction();
				Intent mAlarmintent = new Intent(
						ProcessManagerUtils.KILL_PROCESS_ACTION);
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				PendingIntent alarmPendingItent = PendingIntent.getBroadcast(
						context, 0, mAlarmintent, 0);

				if (action.equals(Intent.ACTION_SCREEN_OFF)) {

					sceenOffTime = System.currentTimeMillis();
					alarmManager.set(AlarmManager.RTC_WAKEUP, sceenOffTime
							+ killProcessTime, alarmPendingItent);
				}
				if (action.equals(Intent.ACTION_SCREEN_ON)) {
					screenOnTime = System.currentTimeMillis();
					if (screenOnTime - sceenOffTime <= killProcessTime) {
						alarmManager.cancel(alarmPendingItent);
					}
				}

			}

		};
		registerReceiver(mScreenOnOffReceiver, screenOffFilter);

	}
}
