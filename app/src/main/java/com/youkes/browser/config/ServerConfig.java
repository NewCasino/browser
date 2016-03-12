package com.youkes.browser.config;


import com.youkes.browser.constant.Constants;
import com.youkes.browser.utils.URLUtil;

import java.util.Map;

public class ServerConfig {

	public static String API_HOST = "api.youkes.com";
	public static int API_PORT = 8081;
	public static final int Interval = 3;
	public static final int BrowserTabMax = 9;

	public static String STS_Server= "http://"+API_HOST+":"+API_PORT+"/api/aliyun/oss/sts";
	public static String Cloud_Upload_Bucket= "youkesupload";
	public static String Cloud_Callback= "http://"+API_HOST+":"+API_PORT+"/api/aliyun/oss/callback";
	public static String Def_City= "北京";

	public static String Pub_Home = "http://"+API_HOST+":"+API_PORT+"/pub/no";
	public static String Browser_Blank = "https://m.baidu.com/";
	public static String Navigation_Home = "https://m.baidu.com/";
	public static String Navigation_Page = "http://m.hao123.com/";
	public static String URL_Web_Search = "https://m.baidu.com/s";
	public static int Max_Upload_Imgs = 9;
	public static int Max_Show_Local_Images = 1024 * 16;
	public static String About_Bookmarks = "about:bookmarks";//about:bookmarks

	public static String Chat_Host = "youkes.com";
	public static String Chat_Service_Name = "youkes.com";

	public static int Chat_Port = 5222;


	public static void init() {

	}

	public static String getCaptchaRegistUrl(String code) {
		return "http://" + API_HOST + ":" + API_PORT
				+ "/api/captcha/regist?id=" + code;// xuming";
	}

	public static String getUserAvatar(String userId) {
		return "http://" + API_HOST + ":" + API_PORT + "/user/avatar/" + userId;// xuming";
	}

	
	public static String getUserAvatarUnknown() {
		return "http://file.youkes.com/public/user/avatar.png";
	}

	public static String getUserAvatarDefault() {
		return "http://file.youkes.com/public/user/avatar.png";
	}
	public static String getChatAvatar(String userId) {
		return "http://" + API_HOST + ":" + API_PORT + "/chat/avatar/" + userId;// xuming";
	}
	
	
	public static String getVideoUrl(String id) {
		return "http://youkes.com/m/video/detail?id=" + id;// xuming";

	}

	public static String getPubUrl(String userId) {
		return "http://" + API_HOST + ":" + API_PORT + "/pub";
	}

	public static String getUpdateUrl() {
		return "http://" + API_HOST + ":" + API_PORT
				+ "/api/apk/version/latest";
	}

	
	public static String getUploadFileUrl(String sha1) {
		
		String url="http://file.youkes.com/upload/public/"+sha1;
		return url;
		
	}


	public static String Pub_No_View = "http://"+API_HOST+":"+API_PORT+"/pub/no/home";
	public static String getPubNoUrl(String id) {
		String url=Pub_No_View+"/"+id;
		return url;
	}

	public static String getSearchUrl(String w) {
		String url= Constants.BAIDU_SEARCH+ URLUtil.encode(w, "UTF-8");;
		return url;
	}

	public static String getSearchWord(String urlstr) {

		Map<String, String> qmap= URLUtil.getQueryMap(urlstr);
		if(urlstr.indexOf(Constants.BAIDU_SEARCH)>=0){
			if(qmap.containsKey("wd")){

				return qmap.get("wd");
			}
		}
		return "";
	}

	public static boolean isSearchUrl(String urlstr) {
		if(urlstr==null){
			return false;
		}
		if(urlstr.indexOf(Constants.BAIDU_SEARCH)>=0){
			return true;
		}
		return false;
	}


	public static String Voice_Upload_Url = "http://"+API_HOST+":"+API_PORT+"/api/voice/upload";
	public static String getVoiceUploadUrl() {
		return Voice_Upload_Url;
	}

	public static String getAnonymousUserId() {

		return null;
	}

	public static String getHomeUrl() {
		return Constants.Default_Page;
	}

	public static String getArticleUrl(String id) {
		//return "http://" + "192.168.1.123:8081" + "/article/detail/" + id;
		return "http://youkes.com/article/detail/"+id;
	}

}
