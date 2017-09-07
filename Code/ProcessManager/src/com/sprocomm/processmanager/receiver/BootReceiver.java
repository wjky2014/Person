package com.sprocomm.processmanager.receiver;

import com.sprocomm.processmanager.BackgroundProcessManangService;
import com.sprocomm.processmanager.utils.ProcessManagerUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		
		ReceiverScreenOnOff(context);
		StartBackgroundProcessService(context);
	
		}
				
	private void StartBackgroundProcessService(Context context) {
		// TODO Auto-generated method stub
	       Intent   mSetartService =new Intent(context, BackgroundProcessManangService.class);
	       context.startService(mSetartService);
	}

	public void ReceiverScreenOnOff(Context context) {
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
				AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
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
		context.getApplicationContext().registerReceiver(mScreenOnOffReceiver, screenOffFilter);

	}
		
	

}
