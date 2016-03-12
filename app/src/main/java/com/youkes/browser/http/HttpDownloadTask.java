package com.youkes.browser.http;

import android.os.AsyncTask;
import android.os.Build;


public class HttpDownloadTask {

    public static HttpDownloadTaskInner execute(HttpDownloadTaskInner.DownloadListener listener, String url, String downloadPath) {


        HttpDownloadTaskInner task = new HttpDownloadTaskInner(listener, downloadPath);
        executeHttpTask(task, url);
        return task;
    }

    public static void executeHttpTask(HttpDownloadTaskInner task, String url) {

        if (task == null) {
            return;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 11) {
            task.executeOnExecutor(
                    AsyncTask.THREAD_POOL_EXECUTOR, url);
        } else {
            task.execute(url);
        }
    }

}
