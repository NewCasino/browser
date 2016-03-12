package com.youkes.browser;


import com.youkes.browser.config.ServerConfig;

public class AccountInfo {

	private String userId = "";
	private String accessKey = "";

	public String getChatId() {
		return chatId;
	}


	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public String getChatPwd() {
		return chatPwd;
	}

	public void setChatPwd(String chatPwd) {
		this.chatPwd = chatPwd;
	}

	private String chatId="";
	private String chatPwd="";

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}


	private String name = "";
	private String nick = "";
	private String sign = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPhoto() {
		if(photo==null||photo.indexOf("http")!=0){
			return ServerConfig.getUserAvatarUnknown();
		}
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	private String photo = "";
	private String phone = "";

	public AccountInfo(String userId, String accessKey, String chatId,
					   String chatPwd, String name, String nick, String sign,
					   String photo, String phone) {

		super();

		this.userId = userId;
		this.accessKey = accessKey;

		this.name = name;
		this.nick = nick;
		this.sign = sign;

		this.photo = photo;
		this.phone = phone;

		this.chatId=chatId;
		this.chatPwd=chatPwd;

	}

	public String getUserName() {
		return this.name;
	}

	public int getSex() {

		return 1;
	}

}
