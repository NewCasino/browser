package com.youkes.browser;

import com.youkes.browser.utils.JSONUtil;

import org.json.JSONObject;

/**
 * Created by xuming on 2016/3/7.
 */
public class RecentAccountJSON {

    public RecentAccountJSON(String userId, String accessKey, String name, String photo,String sign) {
        this.userId = userId;
        this.accessKey = accessKey;
        this.name = name;
        this.photo=photo;
        this.sign=sign;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getSign() {
        return sign;
    }

    private String userId="";
    private String accessKey="";
    private String name="";
    private String photo="";
    private String sign="";

    public RecentAccountJSON(JSONObject obj) {
        this.userId = JSONUtil.getString(obj, "userId");
        this.accessKey = JSONUtil.getString(obj, "accessKey");
        this.name = JSONUtil.getString(obj, "name");
        this.photo=JSONUtil.getString(obj, "photo");
        this.sign=JSONUtil.getString(obj,"sign");

    }
}
