package com.android.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.io.File;
import java.io.IOException;
import android.os.RecoverySystem;
import android.plugin.Features;
import android.util.Log;
import android.os.Environment;
import android.os.SystemProperties;
import android.os.storage.StorageManager;

public class RecoveryRebootReceiver extends BroadcastReceiver {
    final static String TAG = "RecoveryRebootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null) {
        	String action = intent.getAction();
        	Log.v(TAG, "RecoveryRebootReceiver :onCreate() action = " + action);
        	if("com.android.suc.startupdate".equals(action)) {
    			String filePath = intent.getStringExtra("filePath");
                Log.v(TAG, "original path=" + filePath);
				if(filePath.startsWith("/mnt")) {
                    filePath = filePath.substring(4);
                }
                Log.v(TAG, "replaced path=" + filePath);
                //if(true) return;
    			File upgradeFile = new File(filePath);
    			try{
    				RecoverySystem.installPackage(context, upgradeFile);
    			} catch(IOException e) {
    				e.printStackTrace();
    			}
        	}
        }
    }
}