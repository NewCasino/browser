package com.youkes.browser.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.youkes.browser.AccountInfo;
import com.youkes.browser.MainApp;
import com.youkes.browser.RecentAccountJSON;
import com.youkes.browser.RecentAccountJSONList;
import com.youkes.browser.utils.RandomUtil;
import com.youkes.browser.utils.StringUtils;

import java.util.ArrayList;

public class PreferenceUtils {
	public static int CollectPublic = 0;
	public static int CollectPrivate = 1;
	public static String getUserId() {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MainApp.getInstance());
		return settings.getString(PreferenceConstants.UserId, "");
	}


	public static void setCity(final String value) {
		setUserPrefStr(PreferenceConstants.City, value);
	}

	public static String getCity() {
		return getUserPrefStr(PreferenceConstants.City);
	}

	public static int getWebShareType() {
		return getUserPrefInt(PreferenceConstants.WebShareType);
	}

	public static void setWebShareType(final int value) {
		setUserPrefInt(PreferenceConstants.WebShareType, value);
	}

	public static void setCollectType(final int value) {
		setUserPrefInt(PreferenceConstants.CollectType, value);
	}

	public static int getCollectType() {
		return getUserPrefInt(PreferenceConstants.CollectType);
	}

	public static void setAccessKey(final String value) {
		setUserPrefStr(PreferenceConstants.AccessKey, value);
	}




	public static void setUserId(final String value) {
		if(StringUtils.isEmpty(value)){
			setAnonymous();
			return;
		}
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MainApp.getInstance());
		settings.edit().putString(PreferenceConstants.UserId, value).commit();
	}



	public static void setUserPrefInt(final String key,final int value) {
		String userId=PreferenceUtils.getUserId();
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MainApp.getInstance());
		settings.edit().putInt(userId + ":" + key, value).commit();
	}


	public static int getUserPrefInt(final String key) {
		String userId=PreferenceUtils.getUserId();
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MainApp.getInstance());
		return settings.getInt(userId + ":" + key, 0);
	}


	public static boolean getUserPrefBool(final String key) {
		String userId=PreferenceUtils.getUserId();
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MainApp.getInstance());
		return settings.getBoolean(userId + ":" + key, false);
	}

	public static void setUserPrefBool(final String key,final boolean value) {
		String userId=PreferenceUtils.getUserId();
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MainApp.getInstance());
		settings.edit().putBoolean(userId + ":" + key, value).commit();
	}



	public static void setUserPrefStr(final String key,final String value) {
		String userId=PreferenceUtils.getUserId();
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MainApp.getInstance());
		settings.edit().putString(userId + ":" + key, value).commit();
	}


	public static String getUserPrefStr(final String key) {
		String userId=PreferenceUtils.getUserId();
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MainApp.getInstance());
		return settings.getString(userId + ":" + key, "");
	}

	public static String getAccessKey() {
		return getUserPrefStr(PreferenceConstants.AccessKey);
	}


	public static void clearPreference(Context context,
			final SharedPreferences p) {
		final Editor editor = p.edit();
		editor.clear();
		editor.commit();

	}

	public static boolean getPrefBoolean(Context context, final String key,
										 final boolean defaultValue) {

		String userId=PreferenceUtils.getUserId();
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getBoolean(userId+":"+key, defaultValue);

	}



	public static AccountInfo getAccountInfo() {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MainApp.getInstance());
		String userId= settings.getString(PreferenceConstants.UserId, "");

		String accessKey= settings.getString(userId+":"+PreferenceConstants.AccessKey, "");
		String chatId= settings.getString(userId+":"+PreferenceConstants.ChatId, "");
		String chatPwd= settings.getString(userId+":"+PreferenceConstants.ChatPwd, "");
		String UserName= settings.getString(userId+":"+PreferenceConstants.UserName, "");
		String UserSign= settings.getString(userId+":"+PreferenceConstants.UserSign, "");
		String UserPhoto= settings.getString(userId+":"+PreferenceConstants.UserPhoto, "");
		AccountInfo info=new AccountInfo(userId,accessKey,chatId,chatPwd,UserName,"",UserSign,UserPhoto,"");
		return info;
	}


	public static void setAccountInfo(String userId, String accessKey, String chatId, String chatPwd, String userName, String userPhoto, String uploadId) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MainApp.getInstance());
		settings.edit()
				.putString(PreferenceConstants.UserId, userId)

				.putString(userId + ":" + PreferenceConstants.AccessKey, accessKey)
				.putString(userId + ":" + PreferenceConstants.ChatId, chatId)
				.putString(userId+":"+PreferenceConstants.ChatPwd, chatPwd)
				.putString(userId+":"+PreferenceConstants.UserName, userName)
				.putString(userId + ":" + PreferenceConstants.UserPhoto, userPhoto)
				.putString(userId + ":" + PreferenceConstants.UploadId, uploadId)
				.commit();

		addRecentAccount();

	}

	public static String getChatId(){
		return getUserPrefStr(PreferenceConstants.ChatId);

	}


	public static String getChatPwd(){
		return getUserPrefStr(PreferenceConstants.ChatPwd);

	}

	public static void setPicTags(ArrayList<String> picTags) {

		String s= StringUtils.toArrayStr(picTags);
		setUserPrefStr(PreferenceConstants.PicTags, s);


	}


	public static ArrayList<String> getPicTags() {
		String s=getUserPrefStr(PreferenceConstants.PicTags);
		return StringUtils.fromArrayStr(s);
	}


	public static void setNewsTags(ArrayList<String> picTags) {
		String s= StringUtils.toArrayStr(picTags);
		setUserPrefStr(PreferenceConstants.NewsTags, s);
	}


	public static ArrayList<String> getNewsTags() {
		String s=getUserPrefStr(PreferenceConstants.NewsTags);
		return StringUtils.fromArrayStr(s);
	}


	public static String getUploadId() {
		String s=getUserPrefStr(PreferenceConstants.UploadId);
		return s;
	}

	public static void setAnonymous() {
		String s = getUserId();
		if (s.indexOf("Anonymous") >= 0) {
			return;
		}
		String userId = "Anonymous_" + RandomUtil.secureRandomString();
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MainApp.getInstance());
		settings.edit().putString(PreferenceConstants.UserId, userId)
				.putString(userId + ":" + PreferenceConstants.AccessKey, "")
				.putString(userId + ":" + PreferenceConstants.ChatId, "")
				.putString(userId + ":" + PreferenceConstants.ChatPwd, "")
				.commit();
	}

	public static boolean isUserVisitor() {
		String userId=getUserId();
		if(StringUtils.isEmpty(userId)){
			return true;
		}
		String accessKey=getAccessKey();
		if(StringUtils.isEmpty(accessKey)){
			return true;
		}
		if(userId.indexOf("Anonymous_")>=0){
			return true;
		}
		return false;
	}

	public static boolean isBookmarkInited(){
		return getUserPrefBool(PreferenceConstants.BookmarkInited);
	}
	public static void setBookmarkInited(){
		 setUserPrefBool(PreferenceConstants.BookmarkInited, true);
	}

	public static void setUserPhoto(String photo) {
		setUserPrefStr(PreferenceConstants.UserPhoto, photo);
	}


	private static void addRecentAccount() {
		AccountInfo info=getAccountInfo();
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MainApp.getInstance());
		String jsonstr=settings.getString(PreferenceConstants.RecentAccount, "");
		ArrayList<RecentAccountJSON> list=RecentAccountJSONList.parse(jsonstr);
		if(info!=null&&!info.getUserId().contains("Anonymous_")) {
			RecentAccountJSON ajson=new RecentAccountJSON(info.getUserId(), info.getAccessKey(), info.getName(), info.getPhoto(), info.getSign());
			RecentAccountJSONList.addItem(list, ajson);
		}

		String saveJsonStr=RecentAccountJSONList.convertJSONString(list);
		settings.edit().putString(PreferenceConstants.RecentAccount, saveJsonStr)
				.commit();
	}

	public static ArrayList<RecentAccountJSON> getRecentAccountList() {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(MainApp.getInstance());
		String jsonstr=settings.getString(PreferenceConstants.RecentAccount, "");
		return RecentAccountJSONList.parse(jsonstr);
	}
}
