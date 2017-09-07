package com.sprocomm.processmanager.receiver;

import java.util.List;

import com.sprocomm.processmanager.db.AppForbiddenDao;
import com.sprocomm.processmanager.utils.ProcessManagerUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KillProcessReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(ProcessManagerUtils.KILL_PROCESS_ACTION)) {
			AppForbiddenDao mAppForbiddenDao = new AppForbiddenDao(context);
			List<String> mDBForviddenRunList;

			// get packagename from forbidden Database
			mDBForviddenRunList = mAppForbiddenDao.findAll();
			if (mDBForviddenRunList != null) {
				for (String packagename : mDBForviddenRunList) {
					ProcessManagerUtils.ForceStopPacakge(context, packagename);
				}
			}

		}
	}

}
