package com.youkes.browser.site;


import com.youkes.browser.utils.JSONUtil;

import org.json.JSONObject;

import java.util.Date;

public class SiteHotListItem {

	private String _id = "";
	private String userId = "";
	private String site = "";
	private String title = "";
	private String url = "";
	private String desc = "";
	private int cnt = 0;
	private Date date = new Date();
	private String img;


	public SiteHotListItem(JSONObject obj) {

		this._id = JSONUtil.getString(obj, "_id");
		this.userId = JSONUtil.getString(obj, "userId");
		this.site = JSONUtil.getString(obj, "site");
		this.title = JSONUtil.getString(obj, "title");
		this.desc = JSONUtil.getString(obj, "desc");
		this.setUrl(JSONUtil.getString(obj, "url"));
		this.cnt = JSONUtil.getInt(obj, "cnt");
		this.date = JSONUtil.getLongDate(obj, "date");

	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTagText() {
		return "";
	}

	public String getImg() {
		return img;
	}
}
