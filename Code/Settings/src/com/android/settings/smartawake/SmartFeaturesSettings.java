package com.android.settings.smartawake;

import com.android.settings.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import android.provider.Settings;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.SettingsPreferenceFragment;
import android.os.UserHandle;
public class SmartFeaturesSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener,Preference.OnPreferenceClickListener {
    private final static String TAG = "SmartFeaturesSettings";

    private Activity mContext;
    private LayoutInflater layoutInflate;
    private ContentResolver mContentResolver;

	private static final String KEYDOUBLE_LAUNCH_CAMERA="double_launch_camera";
	private static final String ACTIVE_SHOT="activit_shot";
    private static final String KEY_SMART_AWAKE = "smart_awake";
    static final String EXTRA_PREFERENCE_KEY = "preference_key";
    static final String EXTRA_CHECKED = "checked";
    static final String EXTRA_TITLE = "title";
    static final String EXTRA_SUMMARY = "summary";
	// added by tangbaowen for multi pointer screen shot
    private static final String MULTI_POINTER_TAKE_SCREEN_SHOT_KEY="multi_pointer_take_screen_shot";

	private SwitchPreference mToggleDoubleLaunchCameraPreference;
	private SwitchPreference mToggleActiveShotPreference;
	// added by tangbaowen for multi pointer screen shot
    private SwitchPreference mToggleMultiPointerTakeScreenShotPreference;
    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.QS_AIRPLANEMODE;
    }
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.smart_features);
        layoutInflate = getLayoutInflater(icicle);
        mContext = getActivity();
        mContentResolver = mContext.getContentResolver();
        initializeAllPreferences();
    }

    private void initializeAllPreferences() {
		findPreference(KEY_SMART_AWAKE).setSummary(R.string.smart_awake_summary);
		if(android.plugin.Features.JAVA_FEATURE_QUICK_LAUNCH_CAMERA){
			mToggleDoubleLaunchCameraPreference =(SwitchPreference) findPreference(KEYDOUBLE_LAUNCH_CAMERA);
			mToggleDoubleLaunchCameraPreference.setOnPreferenceChangeListener(this);
		}else{
			 removePreference(KEYDOUBLE_LAUNCH_CAMERA);
		}
		
		if(android.plugin.Features.JAVA_FEATURE_EXTRA_ACTIVESHOT_KEY){
			mToggleActiveShotPreference =(SwitchPreference) findPreference(ACTIVE_SHOT);
			mToggleActiveShotPreference.setOnPreferenceChangeListener(this);
		}else{
			 removePreference(ACTIVE_SHOT);
		}
		
		if(android.plugin.Features.JAVA_FEATURE_MULTI_POINT_SCREENSHOT){
            mToggleMultiPointerTakeScreenShotPreference  =(SwitchPreference) findPreference(MULTI_POINTER_TAKE_SCREEN_SHOT_KEY);
			mToggleMultiPointerTakeScreenShotPreference .setOnPreferenceChangeListener(this);
        }else{
			removePreference(MULTI_POINTER_TAKE_SCREEN_SHOT_KEY);
		}
    }

    @Override
    public void onResume() {
        super.onResume();
	    if (mToggleDoubleLaunchCameraPreference != null) {
               boolean mdoubileLaunchCameraEnable = Settings.Secure.getInt(mContext.getContentResolver(),
                       Settings.Secure.DOUBLE_LAUNCH_CAMERA, 0) != 0;
               mToggleDoubleLaunchCameraPreference.setChecked(mdoubileLaunchCameraEnable);
       }
	   
	   if (mToggleActiveShotPreference != null) {
               boolean mactiveshotEnable = Settings.Secure.getInt(mContext.getContentResolver(),
                       Settings.Secure.ACTIVE_SHOT, 0) != 0;
               mToggleActiveShotPreference.setChecked(mactiveshotEnable);
       }
	   
	   if (mToggleMultiPointerTakeScreenShotPreference  != null) {
               boolean mmultiEnable = Settings.Secure.getInt(mContext.getContentResolver(),
                       Settings.Secure.MULTI_POINTER_TAKE_SCREEN_SHOT, 0) != 0;
               mToggleMultiPointerTakeScreenShotPreference .setChecked(mmultiEnable);
       }
        
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
		if(mToggleDoubleLaunchCameraPreference == preference){
			boolean value = (Boolean) newValue;
			Settings.Secure.putInt(mContext.getContentResolver(),Settings.Secure.DOUBLE_LAUNCH_CAMERA, 
						value ? 1 : 0);
			return true;
		} 
		
		if(mToggleActiveShotPreference == preference){
			boolean value = (Boolean) newValue;
			Settings.Secure.putInt(mContext.getContentResolver(),Settings.Secure.ACTIVE_SHOT, 
						value ? 1 : 0);
			return true;
		}
		
		if(mToggleMultiPointerTakeScreenShotPreference == preference){
			boolean value = (Boolean) newValue;
			Settings.Secure.putInt(mContext.getContentResolver(),Settings.Secure.MULTI_POINTER_TAKE_SCREEN_SHOT, 
						value ? 1 : 0);
			return true;
		}
        return false;
    }
    @Override
    public boolean onPreferenceClick(Preference preference) {	
        return true;
    }
}
