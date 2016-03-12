package com.youkes.browser.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 * 打印Activity相关信息
 */
public class ActivityTaskUtils {

    private Context mContext;

    /**
     *
     */
    public ActivityTaskUtils(Context context) {
        mContext = context;
    }

    public String toString() {
        Context context = mContext;
        if(context == null) {
            return null ;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        if(activityManager == null || TextUtils.isEmpty(packageName)) {
            return null;
        }

        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
        StringBuffer buffer = new StringBuffer();
        for(ActivityManager.RunningTaskInfo info : runningTasks) {
            if(!info.baseActivity.getClassName().startsWith(packageName) && !info.topActivity.getClassName().startsWith(packageName)) {
                continue;
            }
            Object[] args = new Object[5];
            args[0] = info.id;
            args[1] = info.numRunning;
            args[2] = info.numActivities;
            args[3] = info.topActivity.getShortClassName();
            args[4] = info.baseActivity.getShortClassName();
            String.format("{id:%d num:%d/%d top:%s base:%s}", args);
        }

        return buffer.toString();
    }
}
