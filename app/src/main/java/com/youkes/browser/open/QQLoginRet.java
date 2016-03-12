package com.youkes.browser.open;

import com.youkes.browser.utils.JSONUtil;

import org.json.JSONObject;

/**
 * Created by xuming on 2016/3/2.
 */
public class QQLoginRet {


    protected String pf="";
    protected String expires_in="";
    protected int ret=0;
    protected String pay_token="";
    protected String openid="";
    protected  String pfkey="";
    protected  String msg="";
    protected String access_token="";

    public static QQLoginRet parseJSONObject(JSONObject jobj) {
        QQLoginRet ret = new QQLoginRet();
        ret.ret= JSONUtil.getInt(jobj, "ret");
        ret.pay_token= JSONUtil.getString(jobj, "pay_token");
        ret.pf= JSONUtil.getString(jobj, "pf");
        ret.expires_in= JSONUtil.getString(jobj, "expires_in");
        ret.openid= JSONUtil.getString(jobj, "openid");
        ret.pfkey= JSONUtil.getString(jobj, "pfkey");
        ret.msg= JSONUtil.getString(jobj, "msg");
        ret.access_token= JSONUtil.getString(jobj, "access_token");
        return ret;

    }

    public String getPay_token() {
        return pay_token;
    }
    public int getRet() {
        return ret;
    }
    public String getOpenid() {
        return openid;
    }
    public String getPfkey() {
        return pfkey;
    }
    public String getMsg() {
        return msg;
    }
    public String getAccess_token() {
        return access_token;
    }
    public String getPf() {
        return pf;
    }

    public String getExpires_in() {
        return expires_in;
    }


}
