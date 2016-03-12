
package com.youkes.browser.http;

import android.os.AsyncTask;
import android.os.Build;

import java.util.List;

public class HttpPostTask {

	public static void execute(OnTaskCompleted listener,List<NameValuePair> params,String url){
		HttpPostTaskInner task=new HttpPostTaskInner(listener,params);
		executeHttpTask(task, url);
	}

	private static void executeHttpTask(HttpPostTaskInner task,String url){

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


