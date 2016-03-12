
package com.youkes.browser.http;

import android.os.AsyncTask;


import com.youkes.browser.preference.PreferenceUtils;
import com.youkes.browser.utils.HttpReqUtil;
import com.youkes.browser.utils.StringUtils;
import com.youkes.browser.utils.SystemInfoUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class HttpPostTaskInner extends AsyncTask<String, Void, String> {

	private OnTaskCompleted listener;
	List<NameValuePair> params;


	public HttpPostTaskInner(OnTaskCompleted listener, List<NameValuePair> paramsInput){
        this.listener=listener;
        this.params=paramsInput;

		String userId= PreferenceUtils.getUserId();
		String accessKey=PreferenceUtils.getAccessKey();
		if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(accessKey)) {
			HttpReqUtil.addNameValueIfNotExist(this.params, "userId", PreferenceUtils.getUserId());
			HttpReqUtil.addNameValueIfNotExist(this.params, "accessKey", PreferenceUtils.getAccessKey());
		}

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
	   if(listener!=null){
		   listener.onTaskCompleted(result);
	   }
	   
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
	    DataOutputStream out=null;
	    // Only display the first 500 characters of the retrieved
	    // web page content.
	    int len = 500;
	        
	    try {
	    	
	    	String paramsstr = getParamString();
	        
	        
	        URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000);
	        conn.setConnectTimeout(5000);
	        conn.setRequestMethod("POST");
	        conn.setFixedLengthStreamingMode(paramsstr.getBytes().length);
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
			conn.setRequestProperty("User-Agent", SystemInfoUtil.getAppInfo());
			conn.setRequestProperty("Phone-Info", SystemInfoUtil.getPhoneInfo());
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");//("userId", "abc");
	        conn.setRequestProperty("charset","utf-8");
	        
	        
	        conn.connect();
	        
	         out=new DataOutputStream(conn.getOutputStream());
	        
	        
	        
	        out.writeBytes(paramsstr);
	        out.flush();
	       // if(cookieStr!=null && cookieStr.length()>0){
	            
	       // }else{
	        /*
    	        String cookie = conn.getHeaderField("Set-Cookie");
    	        if(cookie!=null && cookie.length()>0){
    	        	cookieStr = cookie;
    	        }
    	        */
	       // }
	        
	        
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
	    }
	    finally {
	        if (is != null) {
	            is.close();
	        } 
	        
	        if (out != null) {
	        	out.close();
	        } 
	        
	    }
	    
	    
	    
	    
	}

   private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
   {
       StringBuilder result = new StringBuilder();
       boolean first = true;

       for (NameValuePair pair : params)
       {
           if (first)
               first = false;
           else
               result.append("&");

           if(pair!=null&&pair.getName()!=null&&pair.getValue()!=null){
	           result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
	           result.append("=");
	           result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
           }
           
       }

       return result.toString();
   }
   
protected String getParamString() throws UnsupportedEncodingException {
	return getQuery(params);
	/*
	String paramsstr="";
	//
	for(int i=0; i<params.size();i++)    {   
		NameValuePair p=params.get(i);
		String n=p.getName();
		String v=p.getValue();
		if(v!=null&&v!=""){
			paramsstr+=n+"="+URLEncoder.encode(v,"UTF-8")+"&";
	    	
			//URLEncoder.encode(query, "utf-8")
		}
		
	}
	return paramsstr;
	*/
}
   
   
}


