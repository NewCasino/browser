package com.youkes.browser.utils;



import com.youkes.browser.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class URLUtil {


	public static String getHost(String urlstr){

		try {
			URL u = new URL(urlstr);
			String ret=u.getProtocol()+"://"+u.getHost();
			return ret;
		} catch (MalformedURLException e) {

			return "";
		}

	}

	public static String getHostOnly(String urlstr){

		try {
			URL u = new URL(urlstr);
			String ret=u.getHost();
			return ret;
		} catch (MalformedURLException e) {

			return "";
		}

	}

	public static String addQueryMap(HashMap<String, String> queryMap, String url) {
		Iterator iter = queryMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();

			try {
				String q = (String) entry.getValue();
				String e = URLEncoder.encode(q, "utf-8");
				url += "&" + entry.getKey() + "=" + e;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return url;
	}
	
	public static String getQueryString(HashMap<String, String> queryMap) {
		String url = "";
		Iterator iter = queryMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();

			try {
				String q = (String) entry.getValue();
				String e = URLEncoder.encode(q, "utf-8");
				url += "&" + entry.getKey() + "=" + e;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return url;
		
	}

	public static String getQueryString(List<NameValuePair> queryMap) {
		String url = "";
		for(NameValuePair p:queryMap){
			try {
				String q = p.getValue();
				if(q!=null&&!q.equals("")) {
					String e = URLEncoder.encode(q, "utf-8");
					url += "&" + p.getName() + "=" + e;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return url;

	}

	
	public static String getQueryURL(String urlbase,HashMap<String, String> queryMap) {
		return "";
		
	}


	public static String encode(String w, String s) {
		try {
			return URLEncoder.encode(w,s);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static Map<String, String> getQueryMap(String urlstr) {
		Map<String, String> map = new HashMap<String, String>();
		if(urlstr==null){
			return map;
		}
		String query = null;
		try {
			query = new URL(urlstr).getQuery();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return map;
		}

		String[] params = query.split("&");

		for (String param : params) {
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			try {
				String s= URLDecoder.decode(value, "utf-8");
				map.put(name, s);
			}catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return map;


	}

	public static boolean isValideUrl(String url) {
		if(url==null){
			return false;
		}
		if(url.startsWith("http://")||url.startsWith("https://")){
			return true;
		}
		return false;
	}
}
