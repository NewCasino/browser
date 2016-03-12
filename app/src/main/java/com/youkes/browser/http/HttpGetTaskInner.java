package com.youkes.browser.http;

import android.os.AsyncTask;


import com.youkes.browser.utils.SystemInfoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpGetTaskInner extends AsyncTask<String, Void, String> {
	private OnTaskCompleted listener;

	public HttpGetTaskInner(OnTaskCompleted listener){
        this.listener=listener;
    }
	
   @Override
   protected String doInBackground(String... urls) {
       try {
           return downloadUrl(urls[0]);
       } catch (IOException e) {
           return "{}";
       }
   }
   
   
   @Override
   protected void onPostExecute(String result) {
	   listener.onTaskCompleted(result);
  }
   
   
   public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	    Reader reader = null;
	    reader = new InputStreamReader(stream, "UTF-8");    
	    char[] data = new char [len];
	    StringBuilder buffer = new StringBuilder(len * 10);
        int size;

        size = reader.read(data, 0, data.length);
        while (size > 0)
        {
            String str = new String(data, 0, size);
            buffer.append(str);
            size = reader.read(data, 0, data.length);
        }
        return buffer.toString();
        
	}
   
   
   private String downloadUrl(String myurl) throws IOException {
	    InputStream is = null;
	    // Only display the first 500 characters of the retrieved
	    // web page content.
	    int len = 500;
	        
	    try {
	        URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000);
	        //conn.setW
	        conn.setConnectTimeout(5000);
	        conn.setRequestMethod("GET");

	        conn.setDoInput(true);

			conn.setRequestProperty("User-Agent", SystemInfoUtil.getAppInfo());
			conn.setRequestProperty("Phone-Info", SystemInfoUtil.getPhoneInfo());
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");//("userId", "abc");
			conn.setRequestProperty("charset", "utf-8");


	        // Starts the query
	        conn.connect();
	        //int response = conn.getResponseCode();
	        //Log.d(DEBUG_TAG, "The response is: " + response);
	        is = conn.getInputStream();
	        //len=conn.getContentLength();
	       
	        String contentAsString = readIt(is, len);
	        return contentAsString;
	        
	    // Makes sure that the InputStream is closed after the app is
	    // finished using it.
	    }catch(IOException e){
			e.printStackTrace();
			return "{'api':'/error/network',status:4,msg:'网络错误,请检查网络连接',ex:''}";
		} finally {
	        if (is != null) {
	            is.close();
	        } 
	    }
	    
	    
	}
   
}
