package com.wit.alarmhotspot;

import java.lang.reflect.Method;
 
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
 
public class WifiApManager {
    
    public enum WIFI_AP_STATE {
        WIFI_AP_STATE_DISABLING, WIFI_AP_STATE_DISABLED, WIFI_AP_STATE_ENABLING, WIFI_AP_STATE_ENABLED, WIFI_AP_STATE_FAILED
    }
    
    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    public static final String EXTRA_WIFI_AP_STATE = "wifi_state";
    
	private final WifiManager mWifiManager;
 
	public WifiApManager(Context context) {
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}
 
	/**
     * Start AccessPoint mode with the specified
     * configuration. If the radio is already running in
     * AP mode, update the new configuration
     * Note that starting in access point mode disables station
     * mode operation
     * @param wifiConfig SSID, security and channel details as part of WifiConfiguration
     * @return {@code true} if the operation succeeds, {@code false} otherwise
     */
	public boolean setWifiApEnabled(WifiConfiguration wifiConfig, boolean enabled) {
		try {
			if (enabled) { // disable WiFi in any case
				mWifiManager.setWifiEnabled(false);
			}
 
			Method method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
			return (Boolean) method.invoke(mWifiManager, wifiConfig, enabled);
		} catch (Exception e) {
			Log.e(this.getClass().toString(), "", e);
			return false;
		}
	}
 
	/**
     * Gets the Wi-Fi enabled state.
     * @return {@link WIFI_AP_STATE}
     * @see #isWifiApEnabled()
     */
	public WIFI_AP_STATE getWifiApState() {
		try {
			Method method = mWifiManager.getClass().getMethod("getWifiApState");
 
			int tmp = ((Integer)method.invoke(mWifiManager));
 
			// Fix for Android 4
			if (tmp > 10) {
				tmp = tmp - 10;
			}
 
			return WIFI_AP_STATE.class.getEnumConstants()[tmp];
		} catch (Exception e) {
			Log.e(this.getClass().toString(), "", e);
			return WIFI_AP_STATE.WIFI_AP_STATE_FAILED;
		}
	}
 
	/**
     * Return whether Wi-Fi AP is enabled or disabled.
     * @return {@code true} if Wi-Fi AP is enabled
     * @see #getWifiApState()
     *
     * @hide Dont open yet
     */
    public boolean isWifiApEnabled() {
        return getWifiApState() == WIFI_AP_STATE.WIFI_AP_STATE_ENABLED;
    }
 
    /**
     * Gets the Wi-Fi AP Configuration.
     * @return AP details in {@link WifiConfiguration}
     */
    public WifiConfiguration getWifiApConfiguration() {
		try {
			Method method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
			return (WifiConfiguration) method.invoke(mWifiManager);
		} catch (Exception e) {
			Log.e(this.getClass().toString(), "", e);
			return null;
		}
    }
 
    /**
     * Sets the Wi-Fi AP Configuration.
     * @return {@code true} if the operation succeeded, {@code false} otherwise
     */
    public boolean setWifiApConfiguration(WifiConfiguration wifiConfig) {
    	try {
			Method method = mWifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
			return (Boolean) method.invoke(mWifiManager, wifiConfig);
		} catch (Exception e) {
			Log.e(this.getClass().toString(), "", e);
			return false;
		}
	}
    
    private static WifiApManager instance;
    
    public static WifiApManager get(Context context) {
        if (instance == null) {
            instance = new WifiApManager(context);
        }
        return instance;
    }
}
