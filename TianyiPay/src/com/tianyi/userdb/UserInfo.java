package com.tianyi.userdb;

public class UserInfo
{
	private String	username		= "";
	private String	password	= "";
	private String	accesstoken	= "";
	private String	bindedPhoneNumber		= "";
	private String islatestlogin = "0";
	private String isremember = "0";

	public String getBindedPhoneNumber()
	{
		return bindedPhoneNumber;
	}

	public void setBindedPhoneNumber(String bindedPhoneNumber)
	{
		this.bindedPhoneNumber = bindedPhoneNumber;
	}

	public UserInfo()
	{
	}

	public UserInfo(String username, String password, String signature, 
			String bindedPhoneNumber, String isremember, String islatestlogin)
	{
		super();
		this.username = username;
		this.password = password;
		this.accesstoken=signature;
		this.bindedPhoneNumber=bindedPhoneNumber;
		this.isremember=isremember;
		this.islatestlogin=islatestlogin;
	}

	public String getPassword()
	{
		return password;
	}

	public String getAccesstoken()
	{
		return accesstoken;
	}

	public String getUsername()
	{
		return username;
	}

	public String isIslatestlogin()
	{
		return islatestlogin;
	}

	public String isIsremember()
	{
		return isremember;
	}
	public void setIslatestlogin(String islatestlogin)
	{
		this.islatestlogin = islatestlogin;
	}
	public void setIsremember(String isremember)
	{
		this.isremember = isremember;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setAccesstoken(String signature)
	{
		this.accesstoken = signature;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}
}