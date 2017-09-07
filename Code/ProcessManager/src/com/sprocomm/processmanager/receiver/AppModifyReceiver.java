package com.sprocomm.processmanager.receiver;

import java.util.Arrays;
import java.util.List;

import com.sprocomm.processmanager.db.AppForbiddenDao;
import com.sprocomm.processmanager.utils.ProcessManagerUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppModifyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		AppForbiddenDao mAppForbiddenDao = AppForbiddenDao.getInstance(context);
		List<String> specappList = Arrays.asList(ProcessManagerUtils.apps);
		if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {

			String packageName = intent.getData().getSchemeSpecificPart();
			if (!specappList.contains(packageName)&& mAppForbiddenDao.findAll().size()!=0 ) {
				mAppForbiddenDao.insert(packageName);
			}

		}
		if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
			String packageName = intent.getData().getSchemeSpecificPart();
			mAppForbiddenDao.delete(packageName);
		}

	}

}
