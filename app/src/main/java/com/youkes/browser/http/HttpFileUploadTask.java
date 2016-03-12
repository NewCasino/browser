package com.youkes.browser.http;

import android.os.AsyncTask;
import android.os.Build;

import java.io.File;
import java.util.List;

public class HttpFileUploadTask {

	public static void execute(OnTaskCompleted listener, List<NameValuePair> params, File f, String url) {
		HttpFileUploadTaskInner task = new HttpFileUploadTaskInner(listener, params, f);
		executeHttpTask(task, url);
	}

	public static void executeHttpTask(HttpFileUploadTaskInner task,String url){

		if(task==null){
			return;
		}
		int sdkInt= Build.VERSION.SDK_INT;
		if(sdkInt>=11) {
			task.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, url);
		}else{
			task.execute(url);
		}
	}
}
