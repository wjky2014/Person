package com.android.settings.smartawake;

import android.provider.Settings;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.text.TextUtils;
import android.content.ComponentName;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import android.util.Log;

public class SmartAwakeUtil {
    private static final String TAG = "SmartAwakeUtil";
    private static final String KEY_SMART_GESTURE_DEFAULT = "11011111";
    private static final String KEY_SMART_GESTURE = "gesture_v2_enable";
    private static final String KEY_SMART_MUSIC = "gesture_music";
    private static final String KEY_DOUBLE_CLICK = "gesture_double";

    public static String keyCode2String(int keyCode) {
        String key = "";
        switch(keyCode) {
            case 21: key = "right";
                break;
            case 22: key = "left";
                break;
            case 19: key = "down";
                break;
            case 20: key = "up";
                break;
            case 43: key = "o";
                break;
            case 51: key = "w";
                break;
            case 41: key = "m";
                break;
            case 33: key = "e";
                break;
            case 31: key = "c";
                break;
            case 47: key = "s";
                break;
            case 50: key = "v";
                break;
            case 54: key = "z";
                break;
            default:
                return null;
        }
        return key;
    }
    
    public static String getSettingsKeyFromKeyString(String key) {
        return "gesture_cmd_" + key;
    }
    
    public static void setKeyIntent(Context context, String key, String cmd) {
        Settings.System.putStringForUser(context.getContentResolver(), getSettingsKeyFromKeyString(key), 
            cmd, UserHandle.USER_OWNER);
    }
    public static void setKeyIntent(Context context, String key, String name, String intentPackage, String intentClass) {
        Log.d(TAG, "setKeyIntent=" + key +":" + name + "/" + intentPackage + "/" + intentClass);
        setKeyIntent(context, key, name + "/" + intentPackage + "/" + intentClass);
    }

    public static Intent getKeyIntent(Context context, String key) {
        String cmd = Settings.System.getStringForUser(context.getContentResolver(), 
            getSettingsKeyFromKeyString(key), UserHandle.USER_OWNER);
        if (cmd == null) {
            return null;
        }
        String[] arr = TextUtils.split(cmd, "/");
       
        if (arr == null || arr.length != 3 
                || (TextUtils.isEmpty(arr[1]))
                || (TextUtils.isEmpty(arr[2]))) {
            return null;
        }
        Intent intent = null;
        ComponentName componentName = new ComponentName(arr[1], arr[2]);
        if (componentName != null) {
            intent = Intent.makeMainActivity(componentName);
        }
        return intent;
    }

    public static String getKeyIntentName(Context context, String key) {
        String cmd = Settings.System.getStringForUser(context.getContentResolver(), 
            getSettingsKeyFromKeyString(key), UserHandle.USER_OWNER);
        if (cmd == null) {
            return null;
        }
        String[] arr = TextUtils.split(cmd, "/");
       
        if (arr == null || arr.length != 3 
                || (TextUtils.isEmpty(arr[1]))
                || (TextUtils.isEmpty(arr[2]))) {
            return null;
        }
        return arr[0];
    }
    public static String getKeyIntentPackage(Context context, String key) {
        String cmd = Settings.System.getStringForUser(context.getContentResolver(), 
            getSettingsKeyFromKeyString(key), UserHandle.USER_OWNER);
        if (cmd == null) {
            return null;
        }
        String[] arr = TextUtils.split(cmd, "/");
       
        if (arr == null || arr.length != 3 
                || (TextUtils.isEmpty(arr[1]))
                || (TextUtils.isEmpty(arr[2]))) {
            return null;
        }
        return arr[1];
    }
    public static boolean isSmartGuestureEnabled(Context context) {
        String state = Settings.System.getStringForUser(context.getContentResolver(), 
            KEY_SMART_GESTURE, UserHandle.USER_CURRENT);
        return "1".equals(state);
    }

    public static boolean isDoubleTapEnabled(Context context) {
        String state = Settings.System.getStringForUser(context.getContentResolver(), 
            KEY_DOUBLE_CLICK, UserHandle.USER_CURRENT);
        return "1".equals(state);
    }

    public static boolean isSmartMusicEnabled(Context context) {
        String state = Settings.System.getStringForUser(context.getContentResolver(), 
            KEY_SMART_MUSIC, UserHandle.USER_CURRENT);
        return "1".equals(state);
    }

    public static void enableSmartMusic(Context context, boolean enable) {
        Settings.System.putStringForUser(context.getContentResolver(), KEY_SMART_MUSIC, enable? "1":"0", UserHandle.USER_CURRENT);
    }

    public static void enableDoubleTap(Context context, boolean enable) {
        Settings.System.putStringForUser(context.getContentResolver(), KEY_DOUBLE_CLICK, enable? "1":"0", UserHandle.USER_CURRENT);
    }

    public static void enableSmartGuesture(Context context, boolean enable) {
        Settings.System.putStringForUser(context.getContentResolver(), KEY_SMART_GESTURE, enable? "1":"0", UserHandle.USER_CURRENT);
    }

    public static boolean isSmartGuestureV1disabled(Context context) {
        String state = Settings.System.getStringForUser(context.getContentResolver(), 
            "gesture_enable", UserHandle.USER_OWNER);
        return state == null || "disabled".equals(state);
    }
    public static void disableSmartGuestureV1(Context context) {
        Settings.System.putStringForUser(context.getContentResolver(), "gesture_enable", "disabled", UserHandle.USER_OWNER);
    }

	static public boolean isApkExist(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = null;
        String versionName = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    public static void writeFlagFile(String strFile, String strBuf) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(strFile));
            out.write(strBuf);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException start");
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    
                }
            }
        }
    }
    public static void writeFlagFile(String strFile, int nValue) {
        String strBuf = String.format("%01d", nValue);
        writeFlagFile(strFile, strBuf);
    }
}