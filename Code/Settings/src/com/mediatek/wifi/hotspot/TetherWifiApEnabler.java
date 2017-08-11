/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.mediatek.wifi.hotspot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;

import com.android.settings.datausage.DataSaverBackend;
import com.android.settings.widget.SwitchBar;
import com.mediatek.settings.FeatureOption;

import java.util.ArrayList;

public class TetherWifiApEnabler {
    static final String TAG = "TetherWifiApEnabler";
    private static final int WIFI_IPV4 = 0x0f;
    private static final int WIFI_IPV6 = 0xf0;

    private Context mContext;
    private final DataSaverBackend mDataSaverBackend;
    private WifiManager mWifiManager;
    private IntentFilter mIntentFilter;
    private ConnectivityManager mCm;
    private String[] mWifiRegexs;

    private SwitchBar mSwitchBar;
    private boolean mStateMachineEvent;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.WIFI_AP_STATE_CHANGED_ACTION.equals(action)) {
                handleWifiApStateChanged(intent.getIntExtra(WifiManager.EXTRA_WIFI_AP_STATE,
                        WifiManager.WIFI_AP_STATE_FAILED));
            } else if (ConnectivityManager.ACTION_TETHER_STATE_CHANGED.equals(action)) {
                ArrayList<String> available = intent
                        .getStringArrayListExtra(ConnectivityManager.EXTRA_AVAILABLE_TETHER);
                ArrayList<String> active = intent
                        .getStringArrayListExtra(ConnectivityManager.EXTRA_ACTIVE_TETHER);
                ArrayList<String> errored = intent
                        .getStringArrayListExtra(ConnectivityManager.EXTRA_ERRORED_TETHER);
                if (available != null && active != null && errored != null) {
                    if (FeatureOption.MTK_TETHERINGIPV6_SUPPORT) {
                        updateTetherStateForIpv6(available.toArray(), active.toArray(), errored
                                .toArray());
                    } else {
                        updateTetherState(available.toArray(), active.toArray(), errored.toArray());
                    }
                }
            } else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
                enableWifiSwitch();
            }
        }
    };

    public TetherWifiApEnabler(SwitchBar switchBar, Context context,
            DataSaverBackend dataSaverBackend) {
        mContext = context;
        mSwitchBar = switchBar;
        mDataSaverBackend = dataSaverBackend;
        init(context);
    }

    public void init(Context context) {
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mCm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        mWifiRegexs = mCm.getTetherableWifiRegexs();

        mIntentFilter = new IntentFilter(WifiManager.WIFI_AP_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(ConnectivityManager.ACTION_TETHER_STATE_CHANGED);
        mIntentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
    }

    public void resume() {
        mContext.registerReceiver(mReceiver, mIntentFilter);
        enableWifiSwitch();
    }

    public void pause() {
        mContext.unregisterReceiver(mReceiver);
    }

    private void enableWifiSwitch() {
        boolean isAirplaneMode = Settings.Global.getInt(mContext.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        if (!isAirplaneMode) {
            setSwitchEnabled(!mDataSaverBackend.isDataSaverEnabled());
        } else {
            setSwitchEnabled(false);
        }
    }

    private void updateTetherStateForIpv6(Object[] available, Object[] tethered, Object[] errored) {
        boolean wifiTethered = false;
        boolean wifiErrored = false;

        int wifiErrorIpv4 = ConnectivityManager.TETHER_ERROR_NO_ERROR;
        int wifiErrorIpv6 = ConnectivityManager.TETHER_ERROR_IPV6_NO_ERROR;
        for (Object o : available) {
            String s = (String) o;
            for (String regex : mWifiRegexs) {
                if (s.matches(regex)) {
                    if (wifiErrorIpv4 == ConnectivityManager.TETHER_ERROR_NO_ERROR) {
                        wifiErrorIpv4 = (mCm.getLastTetherError(s) & WIFI_IPV4);
                    }
                    if (wifiErrorIpv6 == ConnectivityManager.TETHER_ERROR_IPV6_NO_ERROR) {
                        wifiErrorIpv6 = (mCm.getLastTetherError(s) & WIFI_IPV6);
                    }
                }
            }
        }

        for (Object o : tethered) {
            String s = (String) o;
            for (String regex : mWifiRegexs) {
                if (s.matches(regex)) {
                    wifiTethered = true;
                    if (FeatureOption.MTK_TETHERINGIPV6_SUPPORT) {
                        if (wifiErrorIpv6 == ConnectivityManager.TETHER_ERROR_IPV6_NO_ERROR) {
                            wifiErrorIpv6 = (mCm.getLastTetherError(s) & WIFI_IPV6);
                        }
                    }
                }
            }
        }

        for (Object o : errored) {
            String s = (String) o;
            for (String regex : mWifiRegexs) {
                if (s.matches(regex)) {
                    wifiErrored = true;
                }
            }
        }
    }

    private void updateTetherState(Object[] available, Object[] tethered, Object[] errored) {
        boolean wifiTethered = false;
        boolean wifiErrored = false;

        for (Object o : tethered) {
            String s = (String) o;
            for (String regex : mWifiRegexs) {
                if (s.matches(regex))
                    wifiTethered = true;
            }
        }
        for (Object o : errored) {
            String s = (String) o;
            for (String regex : mWifiRegexs) {
                if (s.matches(regex))
                    wifiErrored = true;
            }
        }
    }

    private void handleWifiApStateChanged(int state) {
        switch (state) {
        case WifiManager.WIFI_AP_STATE_ENABLING:
            setSwitchEnabled(false);
            setStartTime(false);
            break;
        case WifiManager.WIFI_AP_STATE_ENABLED:
            /**
             * Summary on enable is handled by tether broadcast notice
             */
            long eableEndTime = System.currentTimeMillis();
            Log.i("WifiHotspotPerformanceTest", "[Performance test][Settings][wifi hotspot]"
                    + " wifi hotspot turn on end [" + eableEndTime + "]");
            setSwitchChecked(true);
            setSwitchEnabled(!mDataSaverBackend.isDataSaverEnabled());
            setStartTime(true);
            break;
        case WifiManager.WIFI_AP_STATE_DISABLING:
            setSwitchChecked(false);
            setSwitchEnabled(false);
            break;
        case WifiManager.WIFI_AP_STATE_DISABLED:
            long disableEndTime = System.currentTimeMillis();
            Log.i("WifiHotspotPerformanceTest", "[Performance test][Settings][wifi hotspot]"
                    + " wifi hotspot turn off end [" + disableEndTime + "]");
            setSwitchChecked(false);
            setSwitchEnabled(true);
            enableWifiSwitch();
            break;
        default:
            enableWifiSwitch();
            break;
        }
    }

    public boolean getSwitchSatateMachineEvent() {
        return mStateMachineEvent;
    }

    private void setSwitchChecked(boolean checked) {
        mStateMachineEvent = true;
        if (mSwitchBar != null) {
            mSwitchBar.setChecked(checked);
        }
        Log.d(TAG, "setSwitchChecked checked = " + checked);
        mStateMachineEvent = false;
    }

    private void setSwitchEnabled(boolean enabled) {
        mStateMachineEvent = true;
        if (mSwitchBar != null) {
            mSwitchBar.setEnabled(enabled);
        }
        mStateMachineEvent = false;
    }

    private void setStartTime(boolean enable) {
        long startTime = Settings.System.getLong(mContext.getContentResolver(),
                Settings.System.WIFI_HOTSPOT_START_TIME,
                Settings.System.WIFI_HOTSPOT_DEFAULT_START_TIME);
        if (enable) {
            if (startTime == Settings.System.WIFI_HOTSPOT_DEFAULT_START_TIME) {
                Settings.System.putLong(mContext.getContentResolver(),
                        Settings.System.WIFI_HOTSPOT_START_TIME, System.currentTimeMillis());
                Log.d(TAG, "enable value: " + System.currentTimeMillis());
            }
        } else {
            long newValue = Settings.System.WIFI_HOTSPOT_DEFAULT_START_TIME;
            Log.d(TAG, "disable value: " + newValue);
            Settings.System.putLong(mContext.getContentResolver(),
                    Settings.System.WIFI_HOTSPOT_START_TIME, newValue);
        }
    }
}
