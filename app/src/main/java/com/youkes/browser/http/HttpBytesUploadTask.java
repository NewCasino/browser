package com.youkes.browser.http;

import android.os.AsyncTask;
import android.os.Build;

import java.util.List;

public class HttpBytesUploadTask  {

	public static void execute(OnUploadTaskHandler listener, List<NameValuePair> params, byte[] f, String url) {
		HttpBytesUploadTaskInner task = new HttpBytesUploadTaskInner(listener, params, f);
		executeHttpTask(task, url);

	}

	public static void executeHttpTask(HttpBytesUploadTaskInner task,String url){

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
