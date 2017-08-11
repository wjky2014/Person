package com.android.settings.smartawake;

import android.content.Context;
import com.android.settings.R;

import com.android.settings.SettingsActivity;
import com.android.settings.widget.SwitchBar;
import android.content.BroadcastReceiver;
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
import android.support.v7.preference.PreferenceScreen;
import android.support.v14.preference.SwitchPreference;
import android.content.Intent;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.preference.PreferenceActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.os.AsyncTask;
import android.content.IntentFilter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import android.os.UserHandle;

import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.SettingsPreferenceFragment;
public class SmartawakeSettings extends SettingsPreferenceFragment implements 
            Preference.OnPreferenceChangeListener,Preference.OnPreferenceClickListener, SwitchBar.OnSwitchChangeListener, 
            AppListItem.onAppListItemClickListenter {

    private final static String TAG = "SmartawakeSettings";
    private final static String pathSM = "/proc/gesture_enable";

    private final static String path = "sys/bus/i2c/devices/0-0038/gesture";
    private final static String pathL = "sys/bus/i2c/devices/0-0038/gesture_mode_LEFT";
    private final static String pathR = "sys/bus/i2c/devices/0-0038/gesture_mode_RIGHT";
    private final static String pathU = "sys/bus/i2c/devices/0-0038/gesture_mode_UP";
    private final static String pathD = "sys/bus/i2c/devices/0-0038/gesture_mode_DOWN";
    private final static String pathC = "sys/bus/i2c/devices/0-0038/gesture_mode_C";
    private final static String pathO = "sys/bus/i2c/devices/0-0038/gesture_mode_O";
    private final static String pathW = "sys/bus/i2c/devices/0-0038/gesture_mode_W";
    private final static String pathE = "sys/bus/i2c/devices/0-0038/gesture_mode_E";
    private final static String pathV = "sys/bus/i2c/devices/0-0038/gesture_mode_V";
    private final static String pathM = "sys/bus/i2c/devices/0-0038/gesture_mode_M";
    private final static String pathS = "sys/bus/i2c/devices/0-0038/gesture_mode_S";
    private final static String pathZ = "sys/bus/i2c/devices/0-0038/gesture_mode_Z";
    private final static String pathDouble = "sys/bus/i2c/devices/0-0038/gesture_mode_DOUBLECLICK";
    private final static String pathMStart = "sys/class/ms-touchscreen-msg20xx/device/gesture_wakeup_mode";

    private Activity mContext;
    private SwitchBar mSwitch;
    private ArrayList<HashMap<String, Object>> listItem;
    private LayoutInflater layoutInflate;
    private View view;
    private ListView applist;
    private List<Item> appItem;
    private MyListAdapter appListAdapter;
    private MyHandler handler;
    private AlertDialog dialog;
    private Preference clickedPreference;
    private SharedPreferences mySharedPreferences;
    private ContentResolver mContentResolver;

    private static final int SHOW_DIALOG = 1001;
    private static final String KEY_DOUBLE_CLICK = "double_click";
    private static final String KEY_SMART_MUSIC = "smart_music";
    private static final String KEY_GESTURE_LEFT = "gesture_left";
    private static final String KEY_GESTURE_ROGHT = "gesture_right";
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
    private SwitchPreference mDouble;
    private SwitchPreference mSmartMusic;
    private Preference mGestureLeft;
    private Preference mGestureRight;
    private Preference mGestureUp;
    private Preference mGestureDown;
    private Preference mGestureC;
    private Preference mGestureO;
    private Preference mGestureW;
    private Preference mGestureE;
    private Preference mGestureV;
    private Preference mGestureM;
    private Preference mGestureS;
    private Preference mGestureZ;

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.QS_AIRPLANEMODE;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.smart_awake);
        layoutInflate = getLayoutInflater(icicle); 
        mContext = getActivity();
        mContentResolver = getContentResolver();
        handler = new MyHandler();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction("xdd.updataseetingswich");
        mContext.registerReceiver(mReceiver, filter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mReceiver);	
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mSwitch = ((SettingsActivity)mContext).getSwitchBar();
            mSwitch.addOnSwitchChangeListener(this);
            mSwitch.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        createPreferenceHierarchy();
		if(mPackageInfoList == null){
			loadPackagesInfos();
		}
    }

    @Override
    public void onSwitchChanged(Switch switchView, boolean isChecked) {
        Log.i(TAG, "onSwitchChanged:" + isChecked);
        if (switchView != mSwitch.getSwitch()) {
            return;
        }
        SmartAwakeUtil.enableSmartGuesture(mContext, isChecked);
        updataPreferenceStatus(isChecked);
    }

    private PreferenceScreen createPreferenceHierarchy() {
        PreferenceScreen root = getPreferenceScreen();
        if (root != null) {
            root.removeAll();
        }

        addPreferencesFromResource(R.xml.smart_awake);
        root = getPreferenceScreen();
        
        mDouble = (SwitchPreference) root.findPreference(KEY_DOUBLE_CLICK);
		if(mDouble!=null)
			mDouble.setOnPreferenceChangeListener(this);		
        mSmartMusic = (SwitchPreference) root.findPreference(KEY_SMART_MUSIC);
		if(mSmartMusic!=null)
			mSmartMusic.setOnPreferenceChangeListener(this);
		
        mGestureLeft = (Preference) root.findPreference(KEY_GESTURE_LEFT);
		if(mGestureLeft!=null)mGestureLeft.setOnPreferenceClickListener(this);
		
        mGestureRight = (Preference) root.findPreference(KEY_GESTURE_ROGHT);
		if(mGestureRight!=null)mGestureRight.setOnPreferenceClickListener(this);
		
        mGestureUp = (Preference) root.findPreference(KEY_GESTURE_UP);
		if(mGestureUp!=null)mGestureUp.setOnPreferenceClickListener(this);	
		
        mGestureDown = (Preference) root.findPreference(KEY_GESTURE_DOWN);
		if(mGestureDown!=null)mGestureDown.setOnPreferenceClickListener(this);	
		
        mGestureC = (Preference) root.findPreference(KEY_GESTURE_C);
		if(mGestureC!=null)mGestureC.setOnPreferenceClickListener(this);
		
        mGestureO = (Preference) root.findPreference(KEY_GESTURE_O);
		if(mGestureO!=null)mGestureO.setOnPreferenceClickListener(this);
		
        mGestureW = (Preference) root.findPreference(KEY_GESTURE_W);
		if(mGestureW!=null)mGestureW.setOnPreferenceClickListener(this);
		
        mGestureE = (Preference) root.findPreference(KEY_GESTURE_E);
		if(mGestureE!=null)mGestureE.setOnPreferenceClickListener(this);
		
        mGestureV = (Preference) root.findPreference(KEY_GESTURE_V);
		if(mGestureV!=null)mGestureV.setOnPreferenceClickListener(this);
		
        mGestureM = (Preference) root.findPreference(KEY_GESTURE_M);
		if(mGestureM!=null)mGestureM.setOnPreferenceClickListener(this);
		
        mGestureS = (Preference) root.findPreference(KEY_GESTURE_S);
		if(mGestureS!=null)mGestureS.setOnPreferenceClickListener(this);
		
        mGestureZ = (Preference) root.findPreference(KEY_GESTURE_Z);
		if(mGestureZ!=null)mGestureZ.setOnPreferenceClickListener(this);
 
        
        mGestureLeft.setSummary(SmartAwakeUtil.getKeyIntentName(mContext,"left"));
        mGestureRight.setSummary(SmartAwakeUtil.getKeyIntentName(mContext,"right"));
        mGestureUp.setSummary(SmartAwakeUtil.getKeyIntentName(mContext,"up"));
        mGestureDown.setSummary(SmartAwakeUtil.getKeyIntentName(mContext,"down"));
        mGestureC.setSummary(SmartAwakeUtil.getKeyIntentName(mContext,"c"));
        mGestureO.setSummary(SmartAwakeUtil.getKeyIntentName(mContext,"o"));
        mGestureW.setSummary(SmartAwakeUtil.getKeyIntentName(mContext,"w"));
        mGestureE.setSummary(SmartAwakeUtil.getKeyIntentName(mContext,"e"));
        mGestureV.setSummary(SmartAwakeUtil.getKeyIntentName(mContext,"v"));
        mGestureM.setSummary(SmartAwakeUtil.getKeyIntentName(mContext,"m"));
        String sLabel = SmartAwakeUtil.getKeyIntentName(mContext,"s");

        if("com.mediatek.camera".equals(SmartAwakeUtil.getKeyIntentPackage(mContext, "s"))) {
            sLabel = mContext.getString(R.string.gesture_app_selfie);
        }
        mGestureS.setSummary(sLabel);
        mGestureZ.setSummary(SmartAwakeUtil.getKeyIntentName(mContext,"z"));

        mSwitch.setChecked(SmartAwakeUtil.isSmartGuestureEnabled(mContext));
        		
        if(mDouble != null) {
            mDouble.setChecked(SmartAwakeUtil.isDoubleTapEnabled(mContext));
        }
        if(mSmartMusic != null) {
            mSmartMusic.setChecked(SmartAwakeUtil.isSmartMusicEnabled(mContext));
        }
        updataPreferenceStatus(mSwitch.isChecked());
        return root;
    }

	PackageManager mPackageManager = null;
	List<ResolveInfo> mPackageInfoList = null;
	void loadPackagesInfos() {
		mPackageManager = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);  
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
        mPackageInfoList = mPackageManager.queryIntentActivities(mainIntent, 0);
	}

    class InitAppList extends AsyncTask<Void, Void, List<ResolveInfo>> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<ResolveInfo> doInBackground(Void... arg0) {
            PackageManager mPackageManager = getPackageManager();
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);  
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
            List<ResolveInfo> resolveInfos = mPackageManager.queryIntentActivities
                    (mainIntent, 0);  
            Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(mPackageManager));
            return resolveInfos;
        }
        @Override
        protected void onPostExecute(List<ResolveInfo> result) {
            appItem.add(new AppListItem(mContext, getPreferenceName(clickedPreference.getKey())).setAppListItemClickListener(SmartawakeSettings.this));
            for(ResolveInfo appList : result) {
                appItem.add(new AppListItem(mContext, appList).setAppListItemClickListener(SmartawakeSettings.this));
            }
            
            appListAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }
    }

    private String getPreferenceName(String key) {
        if (key.equals(KEY_GESTURE_LEFT)) return mContext.getString(R.string.gesture_left);
        if (key.equals(KEY_GESTURE_ROGHT)) return mContext.getString(R.string.gesture_right);
        if (key.equals(KEY_GESTURE_UP)) return mContext.getString(R.string.gesture_up);
        if (key.equals(KEY_GESTURE_DOWN)) return mContext.getString(R.string.gesture_down);
        if (key.equals(KEY_GESTURE_C)) return mContext.getString(R.string.gesture_c);
        if (key.equals(KEY_GESTURE_O)) return mContext.getString(R.string.gesture_o);
        if (key.equals(KEY_GESTURE_W)) return mContext.getString(R.string.gesture_w);
        if (key.equals(KEY_GESTURE_E)) return mContext.getString(R.string.gesture_e);
        if (key.equals(KEY_GESTURE_V)) return mContext.getString(R.string.gesture_v);
        if (key.equals(KEY_GESTURE_M)) return mContext.getString(R.string.gesture_m);
        if (key.equals(KEY_GESTURE_S)) return mContext.getString(R.string.gesture_s);
        if (key.equals(KEY_GESTURE_Z)) return mContext.getString(R.string.gesture_z);
        return "Error";
    }
/*
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {

        if (KEY_DOUBLE_CLICK.equals(preference.getKey())) {
           // boolean isEnabled= SmartAwakeUtil.isDoubleTapEnabled(mContext);
            SmartAwakeUtil.enableDoubleTap(mContext, !isEnabled);
            return false;
        } 
        if(KEY_SMART_MUSIC.equals(preference.getKey())){
            boolean isEnabled= SmartAwakeUtil.isSmartMusicEnabled(mContext);
            SmartAwakeUtil.enableSmartMusic(mContext, !isEnabled);
            return false;
        }
        clickedPreference = preference;
        getSysApplication(preference);
        return super.onPreferenceTreeClick(preference);
    }*/

    @Override
    public boolean onPreferenceClick(Preference preference) {

        if (KEY_DOUBLE_CLICK.equals(preference.getKey())) {
			return true;
        } 
        if(KEY_SMART_MUSIC.equals(preference.getKey())){
            return true;
        }
        clickedPreference = preference;
        getSysApplication(preference);		
        return true;
    }
    private void updataPreferenceStatus(boolean isChecked) {


        Log.i(TAG, "updataPreferenceStatus" + isChecked);
        mSwitch.setChecked(isChecked);

        if (isChecked) {
            SmartAwakeUtil.writeFlagFile(pathSM, "1");
            SmartAwakeUtil.writeFlagFile(pathMStart, "1fff");

            if(mDouble != null) {
				mDouble.setEnabled(true);
            }
            if(mSmartMusic != null) {
            mSmartMusic.setEnabled(true);
            }
            mGestureLeft.setEnabled(true);
            mGestureRight.setEnabled(true);
            mGestureUp.setEnabled(true);
            mGestureDown.setEnabled(true);
            mGestureC.setEnabled(true);
            mGestureO.setEnabled(true);
            mGestureW.setEnabled(true);
            mGestureE.setEnabled(true);
            mGestureV.setEnabled(true);
            mGestureM.setEnabled(true);
            mGestureS.setEnabled(true);
            mGestureZ.setEnabled(true);
        } else {
            SmartAwakeUtil.writeFlagFile(pathMStart, 0x0);
            SmartAwakeUtil.writeFlagFile(pathSM, "0");
			
            if(mSmartMusic != null) {
            mSmartMusic.setEnabled(false);
            }
			if(mDouble != null) {
            mDouble.setEnabled(false);
            }
            mGestureLeft.setEnabled(false);
            mGestureRight.setEnabled(false);
            mGestureUp.setEnabled(false);
            mGestureDown.setEnabled(false);
            mGestureC.setEnabled(false);
            mGestureO.setEnabled(false);
            mGestureW.setEnabled(false);
            mGestureE.setEnabled(false);
            mGestureV.setEnabled(false);
            mGestureM.setEnabled(false);
            mGestureS.setEnabled(false);
            mGestureZ.setEnabled(false);
        }
    }  
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (KEY_DOUBLE_CLICK.equals(preference.getKey())) {
			boolean value = (Boolean) newValue;	
            SmartAwakeUtil.enableDoubleTap(mContext, value);
            return true;
        } 
		
        if(KEY_SMART_MUSIC.equals(preference.getKey())){
			boolean value = (Boolean) newValue;	
            SmartAwakeUtil.enableSmartMusic(mContext, value);
            return true;
        }
       // clickedPreference = preference;
       // getSysApplication(preference);
        return true;
    }
    @Override
    public void onStop() {
        try {
            mSwitch.removeOnSwitchChangeListener(this);
            mSwitch.setVisibility(View.GONE);
            } catch (Exception e){
            e.printStackTrace();
        }
        super.onStop();
    }

    private void getSysApplication(Preference preference) {
        View view = layoutInflate.inflate(R.layout.select_app_list, null);

        appItem = new ArrayList<Item>();
        appListAdapter = new MyListAdapter(mContext, layoutInflate, appItem);
        
        applist = (ListView) view.findViewById(R.id.applist);
        applist.setAdapter(appListAdapter);
        
        new InitAppList().execute();
        
        final Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(R.string.select_app)
                .setView(view);
        dialog = builder.create();
        handler.sendEmptyMessageDelayed(SHOW_DIALOG, 300);
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case SHOW_DIALOG :
                dialog.show();
                break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onItemClickListener(String name, String packageName, String className) {
		if(packageName.equalsIgnoreCase("com.android.camera2") && 
                clickedPreference.getKey().equalsIgnoreCase("gesture_s")){
			name = mContext.getString(R.string.gesture_app_selfie);
		}
        SmartAwakeUtil.setKeyIntent(mContext, perferenceKeyToSettingsKey(clickedPreference.getKey()), 
            name, packageName, className);
        clickedPreference.setSummary(name);
        dialog.dismiss();
    }
    
    private String perferenceKeyToSettingsKey(String perferenceKey) {
        if(perferenceKey == null || perferenceKey.length() < "gesture_".length())
            return null;
        return perferenceKey.substring("gesture_".length());
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action == "xdd.updataseetingswich") {
                updataPreferenceStatus(SmartAwakeUtil.isSmartGuestureEnabled(context));
            }
        }
    };
}
