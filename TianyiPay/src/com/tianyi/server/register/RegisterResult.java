package com.tianyi.server.register;

public class RegisterResult
{
	public final static String USER_ID = "user_id";
	public final static String NAME = "name";
	public final static String NICKNAME = "nickname";
	public final static String AVATAR = "avatar";
	public final static String REGISTER_TIME = "register_time";
	public final static String PHONE = "phone";
	public final static String EMAIL = "email";
	public final static String UID = "uid";
	
	public String user_id;
	public String name;
	public String nickname;
	public String avatar;
	public String register_time;
	public String phone;
	public String email;
	public String uid;
	public RegisterResult(String user_id, String name, String nickname,
			String avatar, String register_time, String phone,
			String email, String uid)
	{
		super();
		this.user_id = user_id;
		this.name = name;
		this.nickname = nickname;
		this.avatar = avatar;
		this.register_time = register_time;
		this.phone = phone;
		this.email = email;
		this.uid = uid;
	}
}
