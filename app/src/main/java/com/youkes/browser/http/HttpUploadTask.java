package com.youkes.browser.http;

import android.os.AsyncTask;
import android.os.Build;

import java.io.InputStream;
import java.util.List;

public class HttpUploadTask  {

	public static void execute(OnTaskCompleted listener, List<NameValuePair> params, InputStream f, String url) {
		HttpUploadTaskInner task = new HttpUploadTaskInner(listener, params, f);
		executeHttpTask(task, url);
	}

	private static void executeHttpTask(HttpUploadTaskInner task,String url){

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
