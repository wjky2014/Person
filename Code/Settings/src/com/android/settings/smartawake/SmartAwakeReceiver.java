package com.android.settings.smartawake;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import android.provider.Settings;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.android.settings.R;
import android.os.UserHandle;
public class SmartAwakeReceiver extends BroadcastReceiver {
    
    private final static String TAG = "SmartAwakeReceiver";
    private final static String pathSM = "/proc/gesture_enable";
    private static final String KEY_GESTURE_LEFT = "gesture_left";
    private static final String KEY_GESTURE_RIGHT = "gesture_right";
    private static final String KEY_GESTURE_UP = "gesture_up";
    private static final String KEY_GESTURE_DOWN = "gesture_down";
    private static final String KEY_GESTURE_C = "gesture_c";
    private static final String KEY_GESTURE_O = "gesture_o";
    private static final String KEY_GESTURE_W = "gesture_w";
    private static final String KEY_GESTURE_E = "gesture_e";
    private static final String KEY_GESTURE_V = "gesture_v";
    private static final String KEY_GESTURE_M = "gesture_m";
    private static final String KEY_GESTURE_S = "gesture_s";
    private static final String KEY_GESTURE_Z = "gesture_z";
    
    @Override
    public void onReceive(Context context, Intent arg1) {
        Log.i(TAG, "SmartAwakeReceiver onReceive");
        if(! SmartAwakeUtil.isSmartGuestureV1disabled(context)) {
            updateSmartGuestureToV2(context);
        }
        if("xdd.updataseetingswich".equals(arg1.getAction())){
            SmartAwakeUtil.writeFlagFile(pathSM, SmartAwakeUtil.isSmartGuestureEnabled(context)? 1 : 0);
        }
    }

    void updateSmartGuestureToV2(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("gesture_cmds", 0);
        if(preferences.getBoolean("isInited", false)){
            SmartAwakeUtil.setKeyIntent(context, "right", preferences.getString(KEY_GESTURE_RIGHT, null));
            SmartAwakeUtil.setKeyIntent(context, "left", preferences.getString(KEY_GESTURE_LEFT, null));
            SmartAwakeUtil.setKeyIntent(context, "down", preferences.getString(KEY_GESTURE_DOWN, null));
            SmartAwakeUtil.setKeyIntent(context, "up", preferences.getString(KEY_GESTURE_UP, null));
            SmartAwakeUtil.setKeyIntent(context, "o", preferences.getString(KEY_GESTURE_O, null));
            SmartAwakeUtil.setKeyIntent(context, "w", preferences.getString(KEY_GESTURE_W, null));
            SmartAwakeUtil.setKeyIntent(context, "m", preferences.getString(KEY_GESTURE_M, null));
            SmartAwakeUtil.setKeyIntent(context, "e", preferences.getString(KEY_GESTURE_E, null));
            SmartAwakeUtil.setKeyIntent(context, "c", preferences.getString(KEY_GESTURE_C, null));
            SmartAwakeUtil.setKeyIntent(context, "s", preferences.getString(KEY_GESTURE_S, null));
            SmartAwakeUtil.setKeyIntent(context, "v", preferences.getString(KEY_GESTURE_V, null));
            SmartAwakeUtil.setKeyIntent(context, "z", preferences.getString(KEY_GESTURE_Z, null));
        }
        String state = Settings.System.getStringForUser(context.getContentResolver(), "gesture_enable", UserHandle.USER_OWNER);
        if (state == null) {
            state = "11011111";
        }
        SmartAwakeUtil.enableSmartGuesture(context, state.length() > 2 && state.charAt(2)=='1');
        SmartAwakeUtil.enableDoubleTap(context, state.length() > 2 && state.charAt(0)=='1');
        SmartAwakeUtil.disableSmartGuestureV1(context);
        //SmartAwakeUtil.enableSmartMusic(context, state.charAt(0)=='1'); //music not changed
    }
}
