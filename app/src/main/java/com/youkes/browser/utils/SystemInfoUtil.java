package com.youkes.browser.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.youkes.browser.MainApp;


/**
 * Created by xuming on 2016/2/20.
 */
public class SystemInfoUtil {

    public static String getAppInfo(){
        try {
            Context context = MainApp.getInstance();
            PackageManager pm = context.getPackageManager();

            String packageName=context.getPackageName();
            PackageInfo pinfo = pm.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
            String versionName = pinfo.versionName;
            int versionCode = pinfo.versionCode;
            return packageName+","+String.valueOf(versionCode)+","+versionName;
        }catch(PackageManager.NameNotFoundException e){

        }

        return "";

    }


    public static String getPhoneInfo() {
        String phoneInfo = "Product: " + android.os.Build.PRODUCT;
        phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
        phoneInfo += ", TAGS: " + android.os.Build.TAGS;
        phoneInfo += ", VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE;
        phoneInfo += ", MODEL: " + android.os.Build.MODEL;
        phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK;
        phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
        phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
        phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
        phoneInfo += ", BRAND: " + android.os.Build.BRAND;
        phoneInfo += ", BOARD: " + android.os.Build.BOARD;
        phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
        phoneInfo += ", ID: " + android.os.Build.ID;
        phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
        phoneInfo += ", USER: " + android.os.Build.USER;
        return phoneInfo;
    }


}
