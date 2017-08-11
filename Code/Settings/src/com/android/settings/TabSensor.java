package com.android.settings;

import com.android.settings.SettingsPreferenceFragment;
import android.support.v7.preference.Preference;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import android.os.Bundle;
import android.widget.Switch;
import android.util.Log;
import com.android.settings.widget.SwitchBar;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceScreen;
import android.app.Activity;
import android.provider.Settings;
import android.os.UserHandle;
import android.content.Context;
import android.provider.Settings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


public class TabSensor extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener,
    SwitchBar.OnSwitchChangeListener{

    private static final String TAG = "TabSensor";

	private SwitchBar mSwitch;
	private Activity mContext = null;
	
	private static final String KEY_TAB_SENSOR = "tab_sensor";
	
	
	private static final String KEY_TAKE_PHOTO = "take_photo";
    private static final String KEY_SWITCH_CAMERA = "switch_camera";
	private static final String KEY_OPEN_CAMERA = "qucikly_open_camera";
	private static final String KEY_OPEN_TORCH = "open_torch";
	private static final String KEY_SHOW_LOCKED = "show_locked";
	
	
	
	private SwitchPreference takePhoto;
	private SwitchPreference switchCamera;
	private SwitchPreference openCamera;
	private SwitchPreference openTorch;
	private SwitchPreference showLocked;
	
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
        addPreferencesFromResource(R.xml.tab_sensor);
		
		try {
            mSwitch = ((SettingsActivity)mContext).getSwitchBar();
            mSwitch.addOnSwitchChangeListener(this);
            mSwitch.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		
        root = getPreferenceScreen();
        takePhoto = (SwitchPreference) root.findPreference(KEY_TAKE_PHOTO);
		switchCamera = (SwitchPreference) root.findPreference(KEY_SWITCH_CAMERA);
		openCamera = (SwitchPreference) root.findPreference(KEY_OPEN_CAMERA);
		openTorch = (SwitchPreference) root.findPreference(KEY_OPEN_TORCH);
		showLocked = (SwitchPreference) root.findPreference(KEY_SHOW_LOCKED);
		
		
		mSwitch.setChecked(isTabSensorEnabled());
		
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
		
		if(showLocked!=null){
			showLocked.setOnPreferenceChangeListener(this);
			showLocked.setChecked(isShowLockedEnabled());
		}
			
		updataPreferenceStatus(mSwitch.isChecked());
        return root;
	}
	private boolean isTabSensorEnabled() {
        String state = Settings.System.getStringForUser(mContext.getContentResolver(), 
            KEY_TAB_SENSOR, UserHandle.USER_OWNER);
        return "1".equals(state);
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
            return true;
		}
        if(KEY_SWITCH_CAMERA.equals(preference.getKey())){
			boolean value = (Boolean) newValue;	
			Settings.System.putStringForUser(mContext.getContentResolver(), KEY_SWITCH_CAMERA, value? "1":"0", UserHandle.USER_OWNER);
            return true;
        }
		if(KEY_OPEN_CAMERA.equals(preference.getKey())){
			boolean value = (Boolean) newValue;			
            Settings.System.putStringForUser(mContext.getContentResolver(), KEY_OPEN_CAMERA, value? "1":"0", UserHandle.USER_OWNER);
            return true;                    
		}
		if(KEY_OPEN_TORCH.equals(preference.getKey())){
			boolean value = (Boolean) newValue;	
            Settings.System.putStringForUser(mContext.getContentResolver(), KEY_OPEN_TORCH, value? "1":"0", UserHandle.USER_OWNER);
            return true;                    
		}
		if(KEY_SHOW_LOCKED.equals(preference.getKey())){
			boolean value = (Boolean) newValue;	
            Settings.System.putStringForUser(mContext.getContentResolver(), KEY_SHOW_LOCKED, value? "1":"0", UserHandle.USER_OWNER);
            return true;                    
        }
		return false;
	}
	
	 @Override
	 public void onSwitchChanged(Switch switchView, boolean isChecked) {
	 	updataPreferenceStatus(isChecked);
	 	Settings.System.putStringForUser(mContext.getContentResolver(), KEY_TAB_SENSOR, isChecked? "1":"0", UserHandle.USER_OWNER);
		updateTabSensor();
	 }	
	
	 private void updateTabSensor(){
	 	if(isTabSensorEnabled()&& android.plugin.Features.JAVA_FEATURE_BACK_TOUCH){
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
	 
	 private void updataPreferenceStatus(boolean isChecked) {
		 mSwitch.setChecked(isChecked);
		 if (isChecked) { 
			if(takePhoto != null) {
                takePhoto.setEnabled(true);
            }		 
            if(switchCamera != null){
				switchCamera.setEnabled(true);
			}		
            if(openCamera != null){
				openCamera.setEnabled(true);
			}
			if(openTorch != null){
				openTorch.setEnabled(true);
			}
			if(showLocked != null){
				showLocked.setEnabled(true);
			}			
        } else {           
            if(takePhoto != null) {
                takePhoto.setEnabled(false);
            }
			if(switchCamera != null) {
                switchCamera.setEnabled(false);
            }
			 if(openCamera != null){
				openCamera.setEnabled(false);
			}
			if(openTorch != null){
				openTorch.setEnabled(false);
			}
			if(showLocked != null){
				showLocked.setEnabled(false);
			}		
        }    
    }  
}
