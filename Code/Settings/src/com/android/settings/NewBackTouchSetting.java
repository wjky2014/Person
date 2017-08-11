package com.android.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import com.android.settings.SettingsPreferenceFragment;
import android.view.View;
import android.widget.Switch;
import android.util.AttributeSet;
import android.util.Log;

import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceClickListener;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceScreen;

import android.app.Activity;
import android.provider.Settings;
import android.os.UserHandle;
import android.content.Context;




public class NewBackTouchSetting extends SubSettings {
	 static final String KEY_FINGERPRINT_SETTINGS = "back_touch_settingpublic";
	    public static Preference getBackTouchPreferenceForUser(Context context, final int userId) {		    	
	        Preference backTouchPreference = new Preference(context);
	        backTouchPreference.setTitle(R.string.security_settings_fingerprint_preference_title);
	        backTouchPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	            @Override
	            public boolean onPreferenceClick(Preference preference) {
	                final Context context = preference.getContext();
	                Intent intent = new Intent();              
	                intent.setClass(context, com.android.settings.Settings.BackTouchActivity.class);
	                context.startActivity(intent);
	                return true;
	            }
	        });
	        return backTouchPreference;
	    }
	   	    
}
