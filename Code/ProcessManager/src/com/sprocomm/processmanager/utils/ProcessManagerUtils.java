package com.sprocomm.processmanager.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.sprocomm.processmanager.ProcessInfo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class ProcessManagerUtils {

	public static String DATABASE_NAME = "process_forbidden.db";
	public static String TABLE_NAME = "appforbidden";
	public static String ID = "_id";
	public static String PACKAGE_NAME = "packagename";
	public static String KILL_PROCESS_ACTION = "KILL_PROCESS_RECEIVER";
	public static String MY_SELF_PACKAGE_NAME = "com.sprocomm.processmanager";
	public static long SCREEN_OFF_KILL_PROCESS_TIME = 120 * 1000;

	public static String SPECIAL_QQ = "com.tencent.mobileqq";
	public static String SPECIAL_WECHAT = "com.tencent.mm";
	public static String SPECIAL_WHATSPP = "com.whatsapp";
	public static String SPECIAL_SKYPE = "com.skype.raider";
	public static String SPECIAL_TWITTER = "com.twitter.android";
	public static String SPECIAL_IMO = "com.imo.android.imoim";
	public static String SPECIAL_GOOGLE_MAPS = "com.google.android.apps.maps";
	public static String SPECIAL_GOOGLE_PLUS = "com.google.android.apps.plus";
	public static String SPECIAL_HIKE = "com.bsb.hike";
	public static String SPECIAL_FACEBOOK_MANAGER = "com.facebook.orca";
	public static String SPECIAL_FACEBOOK = "com.facebook.katana";
	public static String SPECIAL_FACEBOOK_KATANA = "com.facebook.katana.stub";
	public static String SPECIAL_FACEBOOK_SERVICE = "com.facebook.services";
	public static String SPECIAL_FACEBOOK_APPMANAGER = "com.facebook.appmanager";
	public static String SPECIAL_FACEBOOK_SYSTEM = "com.facebook.system";
	public static String SPECIAL_VIBER = "com.viber.voip";
	public static String SPECIAL_BBM = "com.bbm";
	public static String SPECIAL_APPLOCK_DO = "com.domobile.applock";
	public static String SPECIAL_APPLOCK_FO = "com.fotoable.applock";
	public static String SPECIAL_GOOGLE_PLAY_SERVICE = "com.google.android.gms";
	public static String SPECIAL_LINE = "jp.naver.line.android";
	public static String SPECIAL_CTS_VERIFIER = "com.android.cts.verifier";
	public static String[] apps = { SPECIAL_WECHAT, SPECIAL_QQ,
			SPECIAL_WHATSPP, SPECIAL_SKYPE, SPECIAL_TWITTER, SPECIAL_IMO,
			SPECIAL_GOOGLE_MAPS, SPECIAL_GOOGLE_PLUS, SPECIAL_HIKE,
			SPECIAL_FACEBOOK_MANAGER, SPECIAL_FACEBOOK, SPECIAL_VIBER,
			SPECIAL_BBM, SPECIAL_APPLOCK_DO, SPECIAL_APPLOCK_FO,
			SPECIAL_GOOGLE_PLAY_SERVICE, SPECIAL_LINE, SPECIAL_FACEBOOK_KATANA,
			SPECIAL_FACEBOOK_SERVICE, SPECIAL_FACEBOOK_APPMANAGER,
			SPECIAL_FACEBOOK_SYSTEM ,SPECIAL_CTS_VERIFIER};

	// forceStopPackage
	public static void ForceStopPacakge(Context context, String packagename) {
		try {
			ActivityManager mActivityManager = (ActivityManager) context
					.getSystemService(context.ACTIVITY_SERVICE);
			Method method = Class.forName("android.app.ActivityManager")
					.getMethod("forceStopPackage", String.class);
			method.invoke(mActivityManager, packagename);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// get mobile apps
	public static List<ProcessInfo> getProcessList(Context context) {

		List<ProcessInfo> appInfoList = new ArrayList<ProcessInfo>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
		for (PackageInfo packageInfo : installedPackages) {
			ProcessInfo appInfo = new ProcessInfo();
			String packageName = packageInfo.packageName;
			appInfo.packageName = packageName;

			int UID = packageInfo.applicationInfo.uid;
			String name = packageInfo.applicationInfo.loadLabel(pm).toString();
			appInfo.name = name;

			Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
			appInfo.icon = icon;

			ApplicationInfo applicationInfo = packageInfo.applicationInfo;

			if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
				appInfo.isSystem = true;
			} else {
				appInfo.isSystem = false;
			}
			if (!appInfo.isSystem) {
				appInfoList.add(appInfo);
			}
			if (appInfo.packageName.equals(MY_SELF_PACKAGE_NAME)) {
				appInfoList.remove(appInfo);
			}

		}

		return appInfoList;
	}

}
