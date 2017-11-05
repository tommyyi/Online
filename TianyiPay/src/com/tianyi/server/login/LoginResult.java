package com.tianyi.server.login;

public class LoginResult
{
	public final static String UID = "uid";
	public final static String ACCESS_TOKEN = "access_token";
	public final static String USERNAME = "username";
	public final static String PHONE = "phone";
	public final static String EXPIRE_TIE = "expire_time";
	
	public String uid;
	public String access_token;
	public String username;
	public String phone;
	public String expire_time;
	
	public LoginResult(String uid, String access_token, String username, String phone,
			String expire_time)
	{
		super();
		this.uid = uid;
		this.access_token = access_token;
		this.username = username;
		this.phone=phone;
		this.expire_time = expire_time;
	}
}
