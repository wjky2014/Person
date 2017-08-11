/*
 * Copyright (C) 2008 The Android Open Source Project
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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.HotspotClient;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.System;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Switch;

import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.datausage.DataSaverBackend;
import com.android.settings.widget.SwitchBar;
import com.android.settings.wifi.WifiApDialog;

import com.mediatek.settings.UtilsExt;

import static android.net.ConnectivityManager.TETHERING_WIFI;

import java.lang.ref.WeakReference;
import java.util.List;

/*
 * Displays preferences for Tethering.
 */
public class TetherWifiSettings extends SettingsPreferenceFragment implements
        DialogInterface.OnClickListener, Preference.OnPreferenceChangeListener,
        ButtonPreference.OnButtonClickCallback, SwitchBar.OnSwitchChangeListener,
        DataSaverBackend.Listener {
    private static final String TAG = "TetherWifiSettings";
    private static final String WIFI_AP_SSID_AND_SECURITY = "wifi_ap_ssid_and_security";
    private static final String WIFI_AUTO_DISABLE = "wifi_auto_disable";
    private static final String WPS_CONNECT = "wps_connect";
    private static final String CONNECTED_CATEGORY = "connected_category";
    private static final String BLOCKED_CATEGORY = "blocked_category";

    private static final int DIALOG_WPS_CONNECT = 1;
    private static final int DIALOG_AP_SETTINGS = 2;
    private static final int DIALOG_AP_CLIENT_DETAIL = 3;

    private SwitchBar mSwitchBar;
    private OnStartTetheringCallback mStartTetheringCallback;
    private Handler mHandler = new Handler();

    private TetherWifiApEnabler mTetherWifiApEnabler;
    private ListPreference mWifiAutoDisable;
    private Preference mCreateNetwork;
    private Preference mWpsConnect;

    private String[] mSecurityType;

    private WifiApDialog mDialog;
    private WifiManager mWifiManager;
    private WifiConfiguration mWifiConfig = null;
    private IntentFilter mIntentFilter;
    private ConnectivityManager mCm;

    private boolean mRestartWifiApAfterConfigChange;

    private PreferenceCategory mConnectedCategory;
    private PreferenceCategory mBlockedCategory;

    private List<HotspotClient> mClientList;
    private View mDetailView;

    private DataSaverBackend mDataSaverBackend;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive action: " + action);
            if (WifiManager.WIFI_HOTSPOT_CLIENTS_CHANGED_ACTION.equals(action)
                || "android.net.wifi.WIFI_HOTSPOT_CLIENTS_IP_READY".equals(action)) {
                handleWifiApClientsChanged();
            } else if (WifiManager.WIFI_WPS_CHECK_PIN_FAIL_ACTION.equals(action)) {
                Toast.makeText(context, R.string.wifi_tether_wps_pin_error, Toast.LENGTH_LONG)
                        .show();
            } else if (WifiManager.WIFI_HOTSPOT_OVERLAP_ACTION.equals(action)) {
                Toast.makeText(context, R.string.wifi_wps_failed_overlap, Toast.LENGTH_LONG).show();
            } else if (WifiManager.WIFI_AP_STATE_CHANGED_ACTION.equals(action)) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_AP_STATE, 0);
                if (state == WifiManager.WIFI_AP_STATE_DISABLED
                        && mRestartWifiApAfterConfigChange) {
                    mRestartWifiApAfterConfigChange = false;
                    Log.d(TAG, "Restarting WifiAp due to prior config change.");
                    mCm.startTethering(TETHERING_WIFI, true, mStartTetheringCallback, mHandler);
                }
                handleWifiApStateChanged(intent.getIntExtra(WifiManager.EXTRA_WIFI_AP_STATE,
                        WifiManager.WIFI_AP_STATE_FAILED));
            } else if (ConnectivityManager.ACTION_TETHER_STATE_CHANGED.equals(action)) {
                if (mWifiManager.getWifiApState() == WifiManager.WIFI_AP_STATE_DISABLED
                        && mRestartWifiApAfterConfigChange) {
                    mRestartWifiApAfterConfigChange = false;
                    Log.d(TAG, "Restarting WifiAp due to prior config change.");
                    mCm.startTethering(TETHERING_WIFI, true, mStartTetheringCallback, mHandler);
                }
            }
        }
    };

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.WIFI;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tether_wifi_prefs);

        mWifiAutoDisable = (ListPreference) findPreference(WIFI_AUTO_DISABLE);
        Preference wifiApSettings = findPreference(WIFI_AP_SSID_AND_SECURITY);
        mWpsConnect = findPreference(WPS_CONNECT);
        mWpsConnect.setEnabled(false);
        mConnectedCategory = (PreferenceCategory) findPreference(CONNECTED_CATEGORY);
        mBlockedCategory = (PreferenceCategory) findPreference(BLOCKED_CATEGORY);
        mDetailView = getActivity().getLayoutInflater().inflate(R.layout.wifi_ap_client_dialog,
                null);

        mCm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mDataSaverBackend = new DataSaverBackend(getContext());
        mDataSaverBackend.addListener(this);

        initWifiTethering();

        mIntentFilter = new IntentFilter(WifiManager.WIFI_HOTSPOT_CLIENTS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiManager.WIFI_WPS_CHECK_PIN_FAIL_ACTION);
        mIntentFilter.addAction(WifiManager.WIFI_HOTSPOT_OVERLAP_ACTION);
        mIntentFilter.addAction(WifiManager.WIFI_AP_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(ConnectivityManager.ACTION_TETHER_STATE_CHANGED);
        mIntentFilter.addAction("android.net.wifi.WIFI_HOTSPOT_CLIENTS_IP_READY");
    }

    @Override
    public void onDestroy() {
        mDataSaverBackend.remListener(this);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        final SettingsActivity activity = (SettingsActivity) getActivity();
        mSwitchBar = activity.getSwitchBar();
        mTetherWifiApEnabler = new TetherWifiApEnabler(mSwitchBar, activity, mDataSaverBackend);
        mSwitchBar.addOnSwitchChangeListener(this);
        mSwitchBar.show();

        mStartTetheringCallback = new OnStartTetheringCallback(this);
        onDataSaverChanged(mDataSaverBackend.isDataSaverEnabled());
    }

    @Override
    public void onStop() {
        super.onStop();
        mSwitchBar.removeOnSwitchChangeListener(this);
        mSwitchBar.hide();

        mStartTetheringCallback = null;
    }

    private void initWifiTethering() {
        final Activity activity = getActivity();
        mWifiConfig = mWifiManager.getWifiApConfiguration();
        mSecurityType = getResources().getStringArray(R.array.wifi_ap_security);

        mCreateNetwork = findPreference(WIFI_AP_SSID_AND_SECURITY);

        mRestartWifiApAfterConfigChange = false;

        if (mWifiConfig == null) {
            String s = com.mediatek.custom.CustomProperties.getString(
                    com.mediatek.custom.CustomProperties.MODULE_WLAN,
                    com.mediatek.custom.CustomProperties.SSID, activity
                            .getString(R.string.wifi_tether_configure_ssid_default));
            mCreateNetwork.setSummary(String.format(activity
                    .getString(R.string.wifi_tether_configure_subtext), s,
                    mSecurityType[WifiApDialog.OPEN_INDEX]));
        } else {
            int index = WifiApDialog.getSecurityTypeIndex(mWifiConfig);
            Log.d(TAG, "index = " + index);
            mCreateNetwork.setSummary(String.format(activity
                    .getString(R.string.wifi_tether_configure_subtext), mWifiConfig.SSID,
                    mSecurityType[index]));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mTetherWifiApEnabler != null) {
            mTetherWifiApEnabler.resume();
        }
        if (mWifiAutoDisable != null) {
            mWifiAutoDisable.setOnPreferenceChangeListener(this);
            int value = System.getInt(getContentResolver(), System.WIFI_HOTSPOT_AUTO_DISABLE,
                   "Micromax".equals(android.os.Build.CUSTOMER) || "Lava".equals(android.os.Build.CUSTOMER)? System.WIFI_HOTSPOT_AUTO_DISABLE_OFF : System.WIFI_HOTSPOT_AUTO_DISABLE_FOR_FIVE_MINS);
            mWifiAutoDisable.setValue(String.valueOf(value));
        }
        getActivity().registerReceiver(mReceiver, mIntentFilter);
        handleWifiApClientsChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
        if (mTetherWifiApEnabler != null) {
            mTetherWifiApEnabler.pause();
        }
        if (mWifiAutoDisable != null) {
            mWifiAutoDisable.setOnPreferenceChangeListener(null);
        }
    }

    @Override
    public Dialog onCreateDialog(int id) {
        if (id == DIALOG_AP_SETTINGS) {
            final Activity activity = getActivity();
            mDialog = new WifiApDialog(activity, this, mWifiConfig);
            return mDialog;
        } else if (id == DIALOG_WPS_CONNECT) {
            Dialog d = new WifiApWpsDialog(getActivity());
            Log.d(TAG, "onCreateDialog, return dialog");
            return d;
        } else if (id == DIALOG_AP_CLIENT_DETAIL) {
            ViewParent parent = mDetailView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mDetailView);
            }
            AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle(
                    R.string.wifi_ap_client_details_title).setView(mDetailView).setNegativeButton(
                    com.android.internal.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
            return dialog;
        }

        return null;
    }

    @Override
    public void onDataSaverChanged(boolean isDataSaving) {
        if (mSwitchBar != null) {
            mSwitchBar.setEnabled(!isDataSaving);
        }
    }

    @Override
    public void onWhitelistStatusChanged(int uid, boolean isWhitelisted) {
    }

    @Override
    public void onBlacklistStatusChanged(int uid, boolean isBlacklisted) {
    }

    @Override
    public void onSwitchChanged(Switch switchView, boolean isChecked) {
        sendBroadcast(); // ALPS01831234
        // Do nothing if called as a result of a state machine event
        if (mTetherWifiApEnabler.getSwitchSatateMachineEvent()) {
            return;
        }
        Log.d(TAG, "onSwitchChanged, hotspot switch isChecked:" + isChecked);
        if (isChecked) {
            mCm.startTethering(TETHERING_WIFI, true, mStartTetheringCallback, mHandler);
        } else {
            mCm.stopTethering(TETHERING_WIFI);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object value) {
        String key = preference.getKey();
        Log.d(TAG, "onPreferenceChange key= " + key + " value= " + value);
        if (WIFI_AUTO_DISABLE.equals(key)) {
            System.putInt(getContentResolver(), System.WIFI_HOTSPOT_AUTO_DISABLE, Integer
                    .parseInt(((String) value)));
        }
        return true;
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mCreateNetwork) {
            showDialog(DIALOG_AP_SETTINGS);
        } else if (preference == mWpsConnect) {
            showDialog(DIALOG_WPS_CONNECT);
        } else if (preference instanceof ButtonPreference) {
            removeDialog(DIALOG_AP_CLIENT_DETAIL);
            final ButtonPreference client = (ButtonPreference) preference;

            ((TextView) mDetailView.findViewById(R.id.mac_address)).setText(client.getMacAddress());
            if (client.isBlocked()) {
                mDetailView.findViewById(R.id.ip_filed).setVisibility(View.GONE);
            } else {
                mDetailView.findViewById(R.id.ip_filed).setVisibility(View.VISIBLE);
                String ipAddr = mWifiManager.getClientIp(client.getMacAddress());
                Log.d(TAG, "connected client ip address is:" + ipAddr);
                ((TextView) mDetailView.findViewById(R.id.ip_address)).setText(ipAddr);
            }
            showDialog(DIALOG_AP_CLIENT_DETAIL);
        }
        return super.onPreferenceTreeClick(preference);
    }

    public void onClick(DialogInterface dialogInterface, int button) {
        if (button == DialogInterface.BUTTON_POSITIVE) {
            mWifiConfig = mDialog.getConfig();
            if (mWifiConfig != null) {
                /**
                 * if soft AP is stopped, bring up else restart with new config
                 * TODO: update config on a running access point when framework
                 * support is added
                 */
                if (mWifiManager.getWifiApState() == WifiManager.WIFI_AP_STATE_ENABLED) {
                    Log.d("TetheringSettings",
                            "Wifi AP config changed while enabled, stop and restart");
                    mRestartWifiApAfterConfigChange = true;
                    mCm.stopTethering(TETHERING_WIFI);
                }
                mWifiManager.setWifiApConfiguration(mWifiConfig);
                int index = WifiApDialog.getSecurityTypeIndex(mWifiConfig);
                if (index == 0) {
                    Toast.makeText(getActivity(), R.string.security_not_set, Toast.LENGTH_LONG)
                            .show();
                }
                mCreateNetwork.setSummary(String.format(getActivity().getString(
                        R.string.wifi_tether_configure_subtext), mWifiConfig.SSID,
                        mSecurityType[index]));
            }
        }
    }

    public void onClick(View v, HotspotClient client) {
        if (v.getId() == R.id.preference_button && client != null) {
            if (client.isBlocked) {
                Log.d(TAG, "onClick,client is blocked, unblock now");
                mWifiManager.unblockClient(client);
            } else {
                Log.d(TAG, "onClick,client isn't blocked, block now");
                mWifiManager.blockClient(client);
            }
            handleWifiApClientsChanged();
        }
    }

    private void handleWifiApClientsChanged() {
        mConnectedCategory.removeAll();
        mBlockedCategory.removeAll();
        mClientList = mWifiManager.getHotspotClients();
        if (mClientList != null) {
            Log.d(TAG, "client number is " + mClientList.size());
            for (HotspotClient client : mClientList) {
                ButtonPreference preference = new ButtonPreference(getActivity(), client, this);
                preference.setMacAddress(client.deviceAddress);
                if (client.isBlocked) {
                    preference.setButtonText(getResources().getString(
                            R.string.wifi_ap_client_unblock_title));
                    mBlockedCategory.addPreference(preference);
                    Log.d(TAG, "blocked client MAC is " + client.deviceAddress);
                } else {
                    preference.setButtonText(getResources().getString(
                            R.string.wifi_ap_client_block_title));
                    mConnectedCategory.addPreference(preference);
                    Log.d(TAG, "connected client MAC is " + client.deviceAddress);
                }
            }
            if (mConnectedCategory.getPreferenceCount() == 0) {
                Preference preference = new Preference(getActivity());
                preference.setTitle(R.string.wifi_ap_no_connected);
                mConnectedCategory.addPreference(preference);
            }
            if (mBlockedCategory.getPreferenceCount() == 0) {
                Preference preference = new Preference(getActivity());
                preference.setTitle(R.string.wifi_ap_no_blocked);
                mBlockedCategory.addPreference(preference);
            }
        }
    }

    private void handleWifiApStateChanged(int state) {
        switch (state) {
        case WifiManager.WIFI_AP_STATE_ENABLING:
            setPreferenceState(false);
            break;
        case WifiManager.WIFI_AP_STATE_ENABLED:
            setPreferenceState(true);
            break;
        case WifiManager.WIFI_AP_STATE_DISABLING:
        case WifiManager.WIFI_AP_STATE_DISABLED:
            setPreferenceState(false);
            removeDialog(DIALOG_WPS_CONNECT);
            break;
        default:
            break;
        }
    }

    private void setPreferenceState(boolean enabled) {
        Log.d(TAG, "setPreferenceState, enabled = " + enabled);
        mWpsConnect.setEnabled(enabled);
    }

    private static final class OnStartTetheringCallback extends
            ConnectivityManager.OnStartTetheringCallback {
        final WeakReference<TetherWifiSettings> mTetherWifiSettings;

        OnStartTetheringCallback(TetherWifiSettings settings) {
            mTetherWifiSettings = new WeakReference<TetherWifiSettings>(settings);
        }

        @Override
        public void onTetheringStarted() {
        }

        @Override
        public void onTetheringFailed() {
        }

        private void update() {
        }
    }

    private static final String ACTION_WIFI_TETHERED_SWITCH = "action.wifi.tethered_switch";

    /*
     * Send broadcast to tell the action: Wifi tethered switch changed
     * ALPS01831234: IPV6 Preference state is not right
     */
    private void sendBroadcast() {
        Intent wifiTetherIntent = new Intent(ACTION_WIFI_TETHERED_SWITCH);
        getActivity().sendBroadcast(wifiTetherIntent);
    }
}
