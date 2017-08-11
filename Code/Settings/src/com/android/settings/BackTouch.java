/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.TrustAgentUtils.TrustAgentComponentInfo;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.fingerprint.FingerprintSettings;
import com.android.settings.widget.SwitchBar;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.provider.Settings;
import android.os.UserHandle;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.service.trust.TrustAgentService;
import com.android.settingslib.RestrictedPreference;
import com.android.internal.widget.LockPatternUtils;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.Preference;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceScreen;
import static com.android.settingslib.RestrictedLockUtils.EnforcedAdmin;
import com.android.settingslib.RestrictedLockUtils;
/**
 * Activity with the backtouch settings.
 */
public class BackTouch extends SettingsPreferenceFragment implements  Preference.OnPreferenceChangeListener{

    private static final String TAG = "BackTouch";
	
	private SwitchBar mSwitch;
	private Activity mContext = null;
	private static final String KEY_TAKE_PHOTO = "take_photo";
    private static final String KEY_SWITCH_CAMERA = "switch_camera";
	private static final String KEY_OPEN_CAMERA = "qucikly_open_camera";
	private static final String KEY_OPEN_TORCH = "open_torch";
	private static final String KEY_ANSWER_CALL = "answer_call";
	private static final String KEY_STOP_ALARM = "stop_almrm";
	private static final String KEY_SHOW_LOCKED = "show_locked";
	
	private static final String KEY_FINGER_PRINT = "finger_print";
	private static final String KEY_LONG_CLICK = "long_click";
	
	private SwitchPreference takePhoto;
	private SwitchPreference switchCamera;
	private SwitchPreference openCamera;
	private SwitchPreference openTorch;
	private SwitchPreference answerCall;
	private SwitchPreference stopAlarm;
	private SwitchPreference showLocked;
	
	
	private LockPatternUtils mLockPatternUtils;
	private static final int MY_USER_ID = UserHandle.myUserId();
	private DevicePolicyManager mDPM;
	private static final Intent TRUST_AGENT_INTENT =
            new Intent(TrustAgentService.SERVICE_INTERFACE);
	private static final boolean ONLY_ONE_TRUST_AGENT = true;
	private static final String KEY_TRUST_AGENT = "trust_agent";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		mContext = getActivity();
	
    }
	
	
	@Override
    public void onResume() {
        super.onResume();
        createPreferenceHierarchy();
    }
	
	
	public PreferenceScreen createPreferenceHierarchy(){
		PreferenceScreen root = getPreferenceScreen();
        if (root != null) {
            root.removeAll();
        }
		addPreferencesFromResource(R.xml.back_touch);
		root = getPreferenceScreen();
		PreferenceGroup securityCategory = (PreferenceGroup)root.findPreference(KEY_FINGER_PRINT);
		if(android.plugin.Features.JAVA_FEATURE_FINGER_TAB){
			if(securityCategory != null){
				boolean result =maybeAddFingerprintPreference(securityCategory, UserHandle.myUserId());	
				if(!result){
					android.util.Log.i("eric","FingerprintPreference null so remove securityCategory");
					root.removePreference(securityCategory);
				}else{
					mContext.getActionBar().setDisplayHomeAsUpEnabled(true);
				}
			}
		}else{
			if(securityCategory != null){
				root.removePreference(securityCategory);	
			}
		}
        takePhoto = (SwitchPreference) root.findPreference(KEY_TAKE_PHOTO);
		switchCamera = (SwitchPreference) root.findPreference(KEY_SWITCH_CAMERA);
		openCamera = (SwitchPreference) root.findPreference(KEY_OPEN_CAMERA);
		openTorch = (SwitchPreference) root.findPreference(KEY_OPEN_TORCH);

		PreferenceGroup longClickCategory = (PreferenceGroup)root.findPreference(KEY_LONG_CLICK);
		answerCall = (SwitchPreference) root.findPreference(KEY_ANSWER_CALL);
		stopAlarm  = (SwitchPreference) root.findPreference(KEY_STOP_ALARM);
		showLocked = (SwitchPreference) root.findPreference(KEY_SHOW_LOCKED);
		if(!android.plugin.Features.JAVA_FEATURE_FINGER_TAB){
			longClickCategory.removePreference(answerCall);
			longClickCategory.removePreference(stopAlarm);
		}
		if(!android.plugin.Features.JAVA_FEATURE_BACK_TOUCH){
			longClickCategory.removePreference(showLocked);
		}
		if(takePhoto!=null){
			takePhoto.setOnPreferenceChangeListener(this);
			takePhoto.setChecked(isTakePhotoEnabled());
		}
		if(switchCamera!=null){
			switchCamera.setOnPreferenceChangeListener(this);
			switchCamera.setChecked(isSwitchCameraEnabled());
		}
		if(openCamera!=null){
            openCamera.setOnPreferenceChangeListener(this);
            openCamera.setChecked(isOpenCameraEnabled());
        }
		if(openTorch!=null){
            openTorch.setOnPreferenceChangeListener(this);
            openTorch.setChecked(isOpenTorchEnabled());
        }
		
		if(answerCall!=null){
			answerCall.setOnPreferenceChangeListener(this);
			answerCall.setChecked(isAnswerCallEnabled());
		}
		if(stopAlarm!=null){
			stopAlarm.setOnPreferenceChangeListener(this);
			stopAlarm.setChecked(isStopAlarmEnabled());
		}
		if(showLocked!=null){
			showLocked.setOnPreferenceChangeListener(this);
			showLocked.setChecked(isShowLockedEnabled());
		}
        return root;
	}
	
	
	private boolean isTakePhotoEnabled() {
        String state = Settings.System.getStringForUser(mContext.getContentResolver(), 
            KEY_TAKE_PHOTO, UserHandle.USER_OWNER);
        return "1".equals(state);
    }
	
	private boolean isSwitchCameraEnabled() {
        String state = Settings.System.getStringForUser(mContext.getContentResolver(), 
            KEY_SWITCH_CAMERA, UserHandle.USER_OWNER);
        return "1".equals(state);
    }
	

	private boolean isOpenCameraEnabled() {
       String state = Settings.System.getStringForUser(mContext.getContentResolver(),
           KEY_OPEN_CAMERA, UserHandle.USER_OWNER);
       return "1".equals(state);
	}
	
	private boolean isOpenTorchEnabled() {
       String state = Settings.System.getStringForUser(mContext.getContentResolver(),
           KEY_OPEN_TORCH, UserHandle.USER_OWNER);
       return "1".equals(state);
	}
	
	private boolean isAnswerCallEnabled() {
       String state = Settings.System.getStringForUser(mContext.getContentResolver(),
           KEY_ANSWER_CALL, UserHandle.USER_OWNER);
       return "1".equals(state);
	}
	
	private boolean isStopAlarmEnabled() {
       String state = Settings.System.getStringForUser(mContext.getContentResolver(),
           KEY_STOP_ALARM, UserHandle.USER_OWNER);
       return "1".equals(state);
	}
	private boolean isShowLockedEnabled() {
       String state = Settings.System.getStringForUser(mContext.getContentResolver(),
           KEY_SHOW_LOCKED, UserHandle.USER_OWNER);
       return "1".equals(state);
	}
	@Override
    protected int getMetricsCategory() {
        return MetricsEvent.QS_AIRPLANEMODE;
    }
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (KEY_TAKE_PHOTO.equals(preference.getKey())) {	
			boolean value = (Boolean) newValue;
            Settings.System.putStringForUser(mContext.getContentResolver(), KEY_TAKE_PHOTO, value? "1":"0", UserHandle.USER_OWNER);
			updateTabSensor();
            return true;
        } 
        if(KEY_SWITCH_CAMERA.equals(preference.getKey())){	
			boolean value = (Boolean) newValue;		
            Settings.System.putStringForUser(mContext.getContentResolver(), KEY_SWITCH_CAMERA, value? "1":"0", UserHandle.USER_OWNER);
			updateTabSensor();
            return true;
        }
		if(KEY_OPEN_CAMERA.equals(preference.getKey())){
			boolean value = (Boolean) newValue;			
            Settings.System.putStringForUser(mContext.getContentResolver(), KEY_OPEN_CAMERA, value? "1":"0", UserHandle.USER_OWNER);
			updateTabSensor();
            return true;                    
        }	
		if(KEY_OPEN_TORCH.equals(preference.getKey())){
			boolean value = (Boolean) newValue;	
            Settings.System.putStringForUser(mContext.getContentResolver(), KEY_OPEN_TORCH, value? "1":"0", UserHandle.USER_OWNER);
			updateTabSensor();
            return true;                    
        }
		if(KEY_SHOW_LOCKED.equals(preference.getKey())){
			boolean value = (Boolean) newValue;	
            Settings.System.putStringForUser(mContext.getContentResolver(), KEY_SHOW_LOCKED, value? "1":"0", UserHandle.USER_OWNER);
			updateTabSensor();
            return true;                    
        }
		if(KEY_ANSWER_CALL.equals(preference.getKey())){
			boolean value = (Boolean) newValue;	
            Settings.System.putStringForUser(mContext.getContentResolver(), KEY_ANSWER_CALL, value? "1":"0", UserHandle.USER_OWNER);
            return true;                    
        }
		if(KEY_STOP_ALARM.equals(preference.getKey())){
			boolean value = (Boolean) newValue;	
            Settings.System.putStringForUser(mContext.getContentResolver(), KEY_STOP_ALARM, value? "1":"0", UserHandle.USER_OWNER);
            return true;                    
        }
		return false;
    }
		
	private void updateTabSensor(){
		if((isTakePhotoEnabled() || isSwitchCameraEnabled()|| isOpenCameraEnabled()||isOpenTorchEnabled()||isShowLockedEnabled())&& android.plugin.Features.JAVA_FEATURE_BACK_TOUCH){
			setTabSenorValue(1);
		}else{
			setTabSenorValue(0);
		}
	}
	private void setTabSenorValue(int value){
		  FileWriter fileWriter = null;
               try {
                   fileWriter = new FileWriter(new File("/sys/devices/bus/11009000.i2c/i2c-2/2-0045/OnOff"));
                   try {
                       fileWriter.write("" + value);
                       if (fileWriter != null) {
                           fileWriter.close();
                       }
                   } catch (IOException e) {
                         e.printStackTrace();
                         if (fileWriter != null) {
                             fileWriter.close();
                         }                             
                   }
               } catch (IOException ie) {
                   ie.printStackTrace();
                   try{
                           if (fileWriter != null) {
                                   fileWriter.close();
                                   fileWriter=null;
                           }                               
                   }catch(IOException io){
                           io.printStackTrace();
                   }
               }
   }   
	
	
	
	private boolean maybeAddFingerprintPreference(PreferenceGroup securityCategory, int userId) {
        Preference fingerprintPreference =
        		FingerprintSettings.getFingerprintPreferenceForUser(
                        securityCategory.getContext(), userId);
        if (fingerprintPreference != null) {
            securityCategory.addPreference(fingerprintPreference);
        }else{
        	return false;
        }
        return true;
    }
	 
}
