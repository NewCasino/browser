package com.youkes.browser.http;

import android.os.AsyncTask;
import android.os.Build;

import java.io.InputStream;
import java.util.List;

public class HttpStreamUploadTask {

	public static void execute(OnUploadTaskHandler listener, List<NameValuePair> params, InputStream f, String url) {
		HttpStreamUploadTaskInner task = new HttpStreamUploadTaskInner(listener, params, f);
		executeHttpTask(task, url);

	}

	public static void executeHttpTask(HttpStreamUploadTaskInner task,String url){

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
