package com.sprocomm.processmanager.receiver;

import com.sprocomm.processmanager.db.AppForbiddenDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class AppModifyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		AppForbiddenDao mAppForbiddenDao = new AppForbiddenDao(context);
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            mAppForbiddenDao.insert(packageName);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            mAppForbiddenDao.delete(packageName);
        }
        
	}

}
