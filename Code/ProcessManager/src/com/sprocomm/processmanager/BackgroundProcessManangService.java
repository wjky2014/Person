package com.sprocomm.processmanager;

import java.util.Arrays;
import java.util.List;

import com.sprocomm.processmanager.db.AppForbiddenDao;
import com.sprocomm.processmanager.utils.ProcessManagerUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class BackgroundProcessManangService extends Service {
	Context context;
	private List<ProcessInfo> mAppInfoList;
	private List<String> mDBForviddenRunList;
	List<String> specappList = Arrays.asList(ProcessManagerUtils.apps);
	AppForbiddenDao mAppForbiddenDao = AppForbiddenDao
			.getInstance(BackgroundProcessManangService.this);

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {

		InitBackgroundDB();
		super.onCreate();
	}

	public void InitBackgroundDB() {
		new Thread() {
			public void run() {
				mAppInfoList = ProcessManagerUtils
						.getProcessList(getApplicationContext());
				for (ProcessInfo processInfo : mAppInfoList) {
					if (mDBForviddenRunList == null
							|| mDBForviddenRunList.size() == 0) {
						if (!specappList.contains(processInfo.packageName)) {
							mAppForbiddenDao.insert(processInfo.packageName);
						}

					}
				}
			};
		}.start();
	}
}
