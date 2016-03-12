package com.youkes.browser.open;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.youkes.browser.MainApp;
import com.youkes.browser.utils.StringUtils;


import java.util.ArrayList;


/**
 * Created by xuming on 2016/3/1.
 */
public class QQShareApi {

    public static final String APP_ID="1104709445";
    private static Tencent api=null;



    public static void shareWebPageQQFriend(final Activity activity,final String url,final String title,final String desc,final String img,IUiListener listener ){
        if (api == null) {
            api = Tencent.createInstance(APP_ID, MainApp.getContext());
        }

        if(api==null){
            return;
        }
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  desc);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  url);

        String postimg=img;
        if(StringUtils.isEmpty(img)) {
            postimg="http://youkes.oss.aliyuncs.com/icon/icon_96.png";
        }

        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,postimg);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "优分享");

        api.shareToQQ(activity, params,listener);

    }


    public static void shareAppQQ(final Activity activity,IUiListener listener) {
        if (api == null) {
            api = Tencent.createInstance(APP_ID, MainApp.getContext());
        }

        if(api==null){
            return;
        }

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "优分享");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "优分享是一个内嵌移动浏览器的网页图片社交分享软件。\n" +
                "优分享可以向你的朋友家人收发文字，语音，动态图片，视频等信息。");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://youkes.oss.aliyuncs.com/icon/icon_96.png");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "优分享");
        api.shareToQQ(activity, params, listener);
    }

    public static void shareToQzone (final Activity activity,final String url,final String title,final String desc,final String img,IUiListener listener) {

        if (api == null) {
            api = Tencent.createInstance(APP_ID, MainApp.getContext());
        }


        if(api==null){
            return;
        }

        ArrayList<String> imgs=new ArrayList<>();
        if(!StringUtils.isEmpty(img)) {
            imgs.add(img);
        }else{
            imgs.add("http://youkes.oss.aliyuncs.com/icon/icon_96.png");
        }
        final Bundle params = new Bundle();
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, desc);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgs);
        api.shareToQzone(activity, params, listener);
    }


    public static void login(Activity activity,String scope,IUiListener listener) {
        if (api == null) {
            api = Tencent.createInstance(APP_ID, MainApp.getContext());
        }

        if(api==null){
            return;
        }

        if (!api.isSessionValid()) {

            api.login(activity, scope, listener);
        }

    }


    public static void getUserInfo(Activity activity,IUiListener listener) {
        if (api == null) {
            api = Tencent.createInstance(APP_ID, MainApp.getContext());
        }

        if(api==null){
            return;
        }

        QQToken token=api.getQQToken();
        UserInfo info = new UserInfo(activity, token);
        info.getUserInfo(listener);

    }




}
